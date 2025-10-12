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
            System.out.println("\n(1) Excluir minha conta");
            System.out.println("(0) Retornar");
            System.out.print("\nOpção: ");
            
            try {
                opcao = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                opcao = -1;
            }

            switch(opcao) {
                case 1:
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

    public boolean excluirUsuario(int id) throws Exception {
        ArrayList<Lista> listas = arquivoLista.readListasByUsuario(id);
        if (!listas.isEmpty()) {
            System.out.println("\nERRO: Usuário possui listas vinculadas e não pode ser excluído.");
            return false;
        }
        return arquivoUsuario.delete(id);
    }
}

