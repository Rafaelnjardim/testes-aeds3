package view;

import model.Lista;
import model.Produto;
import model.ListaProduto;
import dao.ArquivoProduto;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class VisaoLista {
    private Scanner scanner;

    public VisaoLista() {
        this.scanner = new Scanner(System.in, "UTF-8");
    }

    private String formatarData(long timestamp) {
        if (timestamp == 0) return "N/A";
        return new SimpleDateFormat("dd/MM/yyyy").format(new Date(timestamp));
    }

    public String menuListas(ArrayList<Lista> listas, String nomeUsuario) {
        System.out.println("\nPresenteFácil 1.0");
        System.out.println("-----------------");
        System.out.println("> Início > Minhas listas de " + nomeUsuario + "\n");
        System.out.println("LISTAS");

        if (listas.isEmpty()) {
            System.out.println("Nenhuma lista cadastrada.");
        } else {
            for (int i = 0; i < listas.size(); i++) {
                Lista l = listas.get(i);
                System.out.println("(" + (i + 1) + ") " + l.getNome() + " - " + formatarData(l.getDataLimite()));
            }
        }
        
        System.out.println("\n(N) Nova lista");
        System.out.println("(R) Retornar ao menu anterior");
        System.out.print("\nOpção: ");
        return scanner.nextLine().toUpperCase();
    }

    public Lista leLista() {
        System.out.println("\n-- NOVA LISTA --");
        System.out.print("Nome da lista (ex: Aniversário 2025): ");
        String nome = scanner.nextLine();
        System.out.print("Descrição detalhada: ");
        String desc = scanner.nextLine();
        System.out.print("Data limite (dd/mm/aaaa) (opcional, deixe em branco): ");
        String dataStr = scanner.nextLine();
        
        long dataLimite = 0;
        if (!dataStr.isBlank()) {
            try {
                dataLimite = new SimpleDateFormat("dd/MM/yyyy").parse(dataStr).getTime();
            } catch (Exception e) {
                System.out.println("Formato de data inválido, será ignorada.");
            }
        }
        return new Lista(0, nome, desc, dataLimite);
    }


    public Lista editarLista(Lista lista) {
        System.out.println("\n-- EDITAR LISTA --");
        System.out.println("(deixe em branco para manter o valor atual)");

        System.out.println("Nome atual: " + lista.getNome());
        System.out.print("Novo nome: ");
        String novoNome = scanner.nextLine();
        if (!novoNome.isBlank()) lista.setNome(novoNome);

        System.out.println("Descrição atual: " + lista.getDescricao());
        System.out.print("Nova descrição: ");
        String novaDesc = scanner.nextLine();
        if (!novaDesc.isBlank()) lista.setDescricao(novaDesc);

        System.out.println("Data limite atual: " + formatarData(lista.getDataLimite()));
        System.out.print("Nova data limite (dd/mm/aaaa) (opcional): ");
        String novaDataStr = scanner.nextLine();
        
        if (!novaDataStr.isBlank()) {
            try {
                long novaDataLimite = new SimpleDateFormat("dd/MM/yyyy").parse(novaDataStr).getTime();
                lista.setDataLimite(novaDataLimite);
            } catch (Exception e) {
                System.out.println("Formato de data inválido. Data não alterada.");
            }
        } else {
            System.out.println("Data limite mantida.");
        }

        return lista;
    }
    
    public int menuDetalhesLista(Lista lista, String nomeUsuario) {
        System.out.println("\nPresenteFácil 1.0");
        System.out.println("-----------------");
        System.out.println("> Início > Minhas listas > " + lista.getNome() + "\n");
        
        System.out.println(lista.toString());
        
        System.out.println("\n(1) Gerenciar os produtos da lista");
        System.out.println("(2) Alterar os dados da lista"); 
        System.out.println("(3) Excluir a lista");
        System.out.println("\n(R) Retornar ao menu anterior");

        System.out.print("\nOpção: ");
        String opcao = scanner.nextLine().toUpperCase();
        if (opcao.equals("R")) return 0;
        try {
            return Integer.parseInt(opcao);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public boolean confirmarExclusaoLista(Lista lista) {
        System.out.println("\n-- EXCLUIR LISTA --");
        System.out.println("Você tem certeza que deseja excluir a lista '" + lista.getNome() + "'?");
        System.out.print("Digite 'S' para confirmar ou qualquer outra tecla para cancelar: ");
        String resp = scanner.nextLine().trim().toUpperCase();
        return resp.equals("S");
    }

    // Novos métodos para gerenciar produtos na lista
    public String menuProdutosLista(ArrayList<ListaProduto> produtosNaLista, ArquivoProduto arquivoProduto, String nomeLista) throws Exception {
        System.out.println("\nPresenteFácil 1.0");
        System.out.println("-----------------");
        System.out.println("> Início > Minhas listas > " + nomeLista + " > Produtos\n");

        if (produtosNaLista.isEmpty()) {
            System.out.println("Nenhum produto na lista.");
        } else {
            for (int i = 0; i < produtosNaLista.size(); i++) {
                ListaProduto lp = produtosNaLista.get(i);
                Produto produto = arquivoProduto.read(lp.getIdProduto());
                if (produto != null) {
                    System.out.println("(" + (i + 1) + ") " + produto.getNome() + " (x" + lp.getQuantidade() + ")");
                }
            }
        }
        
        System.out.println("\n(A) Acrescentar produto");
        System.out.println("(R) Retornar ao menu anterior");
        System.out.print("\nOpção: ");
        return scanner.nextLine().toUpperCase();
    }

    public int menuDetalhesProdutoLista(Produto produto, ListaProduto listaProduto, String nomeLista) {
        System.out.println("\nPresenteFácil 1.0");
        System.out.println("-----------------");
        System.out.println("> Início > Minhas listas > " + nomeLista + " > Produtos > " + produto.getNome() + "\n");
        
        System.out.println("NOME.......: " + produto.getNome());
        System.out.println("GTIN-13....: " + produto.getGtin13());
        System.out.println("DESCRIÇÃO..: " + produto.getDescricao());
        System.out.println("QUANTIDADE.: " + listaProduto.getQuantidade());
        System.out.println("OBSERVAÇÕES: " + listaProduto.getObservacoes());

        System.out.println("\n(1) Alterar a quantidade");
        System.out.println("(2) Alterar as observações");
        System.out.println("(3) Remover o produto desta lista");
        System.out.println("\n(R) Retornar ao menu anterior");

        System.out.print("\nOpção: ");
        String opcao = scanner.nextLine().toUpperCase();
        if (opcao.equals("R")) return 0;
        try {
            return Integer.parseInt(opcao);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public boolean confirmarRemocaoProduto(Produto produto) {
        System.out.println("\n-- REMOVER PRODUTO DA LISTA --");
        System.out.println("Você tem certeza que deseja remover '" + produto.getNome() + "' da lista?");
        System.out.print("Digite 'S' para confirmar ou qualquer outra tecla para cancelar: ");
        String resp = scanner.nextLine().trim().toUpperCase();
        return resp.equals("S");
    }

    public int menuAcrescentarProduto(String nomeLista) {
        System.out.println("\nPresenteFácil 1.0");
        System.out.println("-----------------");
        System.out.println("> Início > Minhas listas > " + nomeLista + " > Produtos > Acrescentar produto\n");

        System.out.println("(1) Buscar produtos por GTIN");
        System.out.println("(2) Listar todos os produtos");
        System.out.println("\n(R) Retornar ao menu anterior");

        System.out.print("\nOpção: ");
        String opcao = scanner.nextLine().toUpperCase();
        if (opcao.equals("R")) return 0;
        try {
            return Integer.parseInt(opcao);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public String menuListagemProdutosParaAdicionar(ArrayList<Produto> produtos, int paginaAtual, int totalPaginas, String nomeLista) {
        System.out.println("\nPresenteFácil 1.0");
        System.out.println("-----------------");
        System.out.println("> Início > Minhas listas > " + nomeLista + " > Produtos > Acrescentar produto > Listagem\n");
        
        System.out.println("Página " + paginaAtual + " de " + totalPaginas);
        System.out.println();
        
        int inicio = (paginaAtual - 1) * 10;
        int fim = Math.min(inicio + 10, produtos.size());
        
        for (int i = inicio; i < fim; i++) {
            Produto p = produtos.get(i);
            String numero = (i - inicio == 9) ? "0" : String.valueOf((i - inicio) + 1);
            System.out.println("(" + numero + ") " + p.getNome());
        }
        
        System.out.println();
        if (paginaAtual > 1) {
            System.out.println("(A) Página anterior");
        }
        if (paginaAtual < totalPaginas) {
            System.out.println("(P) Próxima página");
        }
        System.out.println("\n(R) Retornar ao menu anterior");
        
        System.out.print("\nOpção: ");
        return scanner.nextLine().toUpperCase();
    }
}