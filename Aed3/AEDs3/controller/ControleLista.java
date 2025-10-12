package controller;

import model.Usuario;
import model.Lista;
import dao.ArquivoLista;
import view.VisaoLista;
import java.util.ArrayList;

public class ControleLista {
    private Usuario usuarioLogado;
    private ArquivoLista arquivoLista;
    private VisaoLista visaoLista;

    /**
     * Agora o ArquivoLista é passado pelo ControlePrincipal,
     * evitando múltiplas instâncias desnecessárias.
     */
    public ControleLista(Usuario usuarioLogado, ArquivoLista arquivoLista) {
        this.usuarioLogado = usuarioLogado;
        this.arquivoLista = arquivoLista;
        this.visaoLista = new VisaoLista();
    }

    public void handleMenuListas() {
        String opcao;
        do {
            try {
                ArrayList<Lista> listas = arquivoLista.readListasByUsuario(usuarioLogado.getId());
                opcao = visaoLista.menuListas(listas, usuarioLogado.getNome());

                if (opcao.equalsIgnoreCase("N")) {
                    handleNovaLista();
                } else if (!opcao.equalsIgnoreCase("R")) {
                    try {
                        int index = Integer.parseInt(opcao) - 1;
                        if (index >= 0 && index < listas.size()) {
                            menuDetalhesLista(listas.get(index));
                        } else {
                            System.out.println("Opção inválida.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Opção inválida.");
                    }
                }
            } catch(Exception e) {
                System.err.println("ERRO: Falha ao gerenciar listas.");
                e.printStackTrace();
                opcao = "R";
            }
        } while (!opcao.equalsIgnoreCase("R"));
    }

    private void handleNovaLista() throws Exception {
        Lista novaLista = visaoLista.leLista();
        novaLista.setIdUsuario(usuarioLogado.getId());
        arquivoLista.create(novaLista);
        System.out.println("\nLista '" + novaLista.getNome() + "' criada com sucesso!");
        System.out.println("Código compartilhável: " + novaLista.getCodigoCompartilhavel());
    }

    private void menuDetalhesLista(Lista lista) throws Exception {
        int opcao;
        do {
            opcao = visaoLista.menuDetalhesLista(lista, usuarioLogado.getNome());
            switch(opcao) {
                case 1:
                    System.out.println("\nFuncionalidade de Produtos será implementada no TP2.");
                    break;
                case 2:
                    // TODO: implementar edição da lista
                    System.out.println("\nFuncionalidade de alteração não implementada.");
                    break;
                case 3:
                    // Excluir a lista
                    arquivoLista.delete(lista.getId());
                    System.out.println("\nLista excluída com sucesso!");
                    opcao = 0; // Sai do menu de detalhes
                    break;
                case 0: break;
                default: System.out.println("\nOpção inválida.");
            }
        } while(opcao != 0);
    }
}

