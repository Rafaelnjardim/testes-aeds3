package controller;

import dao.ArquivoUsuario;
import dao.ArquivoLista;
import model.Usuario;
import model.Lista;
import view.VisaoUsuario;
import java.util.ArrayList;
import java.util.Scanner;

public class ControleUsuario {
    private ArquivoUsuario arquivoUsuario;
    private ArquivoLista arquivoLista;
    private Usuario usuarioLogado;
    private VisaoUsuario visaoUsuario;

    public ControleUsuario(ArquivoUsuario arquivoUsuario, ArquivoLista arquivoLista, Usuario usuarioLogado) {
        this.arquivoUsuario = arquivoUsuario;
        this.arquivoLista = arquivoLista;
        this.usuarioLogado = usuarioLogado;
        this.visaoUsuario = new VisaoUsuario();
    }

    public void handleNovoUsuario() {
        try {
            Usuario novo = visaoUsuario.leUsuario();
            if (arquivoUsuario.readByEmail(novo.getEmail()) == null) {
                arquivoUsuario.create(novo);
                System.out.println("\nUsuário " + novo.getNome() + " criado com sucesso!");
            } else {
                System.out.println("\nERRO: O e-mail informado já está em uso.");
            }
        } catch(Exception e) {
            System.err.println("ERRO: Falha ao criar novo usuário.");
            e.printStackTrace();
        }
    }
    
    public void handleMenuDados() {
        int opcao;
        Scanner sc = new Scanner(System.in);
        do {
            visaoUsuario.mostraUsuario(usuarioLogado);
            System.out.println("\n(1) Atualizar meus dados");
            System.out.println("(2) Excluir minha conta");
            System.out.println("(0) Retornar");
            System.out.print("\nOpção: ");
            
            try {
                opcao = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                opcao = -1;
            }

            switch(opcao) {
                case 1:
                    handleAtualizarUsuario();
                    // Recarregar usuário atualizado
                    try {
                        usuarioLogado = arquivoUsuario.read(usuarioLogado.getId());
                    } catch (Exception e) {
                        System.err.println("ERRO: Falha ao recarregar dados do usuário.");
                    }
                    break;
                case 2:
                    try {
                        if (excluirUsuario(usuarioLogado.getId())) {
                            System.out.println("\nConta excluída com sucesso!");
                            usuarioLogado = null;
                            opcao = 0;
                        } else {
                            System.out.println("\nNão foi possível excluir a conta.");
                        }
                    } catch (Exception e) {
                        System.err.println("ERRO: Falha ao excluir usuário.");
                        e.printStackTrace();
                    }
                    break;
                case 0:
                    break;
                default:
                    System.out.println("\nOpção inválida.");
            }
        } while (opcao != 0 && usuarioLogado != null);
    }

    // Novo método para lidar com a atualização de dados do usuário
    public void handleAtualizarUsuario() {
        try {
            // Ler dados atualizados do usuário
            Usuario usuarioAtualizado = visaoUsuario.leDadosAtualizados(usuarioLogado);
            
            // Verificar se o e-mail foi alterado e se já existe
            if (!usuarioAtualizado.getEmail().equals(usuarioLogado.getEmail())) {
                Usuario usuarioComEmail = arquivoUsuario.readByEmail(usuarioAtualizado.getEmail());
                if (usuarioComEmail != null && usuarioComEmail.getId() != usuarioLogado.getId()) {
                    System.out.println("\nERRO: O e-mail informado já está em uso por outro usuário.");
                    return;
                }
            }
            
            // Tentar atualizar no arquivo
            boolean sucesso = arquivoUsuario.update(usuarioAtualizado);
            
            if (sucesso) {
                System.out.println("\nDados atualizados com sucesso!");
                // Atualizar referência do usuário logado
                usuarioLogado = usuarioAtualizado;
            } else {
                System.out.println("\nERRO: Falha ao atualizar dados.");
            }
            
        } catch (Exception e) {
            System.err.println("ERRO: Falha ao atualizar usuário.");
            e.printStackTrace();
        }
    }

    public boolean excluirUsuario(int id) throws Exception {
        ArrayList<Lista> listas = arquivoLista.readListasByUsuario(id);
        if (!listas.isEmpty()) {
            System.out.println("\nERRO: Usuário possui listas vinculadas e não pode ser excluído.");
            return false;
        }
        return arquivoUsuario.delete(id);
    }
}