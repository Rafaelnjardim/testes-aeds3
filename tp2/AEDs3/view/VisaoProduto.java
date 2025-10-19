package view;

import model.Produto;
import model.Lista;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class VisaoProduto {
    private Scanner scanner;

    public VisaoProduto() {
        this.scanner = new Scanner(System.in, "UTF-8");
    }

    private String formatarData(long timestamp) {
        return new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date(timestamp));
    }

    public String menuProdutos(ArrayList<Produto> produtos) {
        System.out.println("\nPresenteFácil 1.0");
        System.out.println("-----------------");
        System.out.println("> Início > Produtos\n");
    
        System.out.println("(1) Buscar produtos por GTIN");
        System.out.println("(2) Listar todos os produtos");
        System.out.println("(3) Cadastrar um novo produto");
        System.out.println("\n(R) Retornar ao menu anterior");
    
        System.out.print("\nOpção: ");
        return scanner.nextLine().toUpperCase();
    }

    public String menuListagemProdutos(ArrayList<Produto> produtos, int paginaAtual, int totalPaginas) {
        System.out.println("\nPresenteFácil 1.0");
        System.out.println("-----------------");
        System.out.println("> Início > Produtos > Listagem\n");
        
        System.out.println("Página " + paginaAtual + " de " + totalPaginas);
        System.out.println();
        
        int inicio = (paginaAtual - 1) * 10;
        int fim = Math.min(inicio + 10, produtos.size());
        
        for (int i = inicio; i < fim; i++) {
            Produto p = produtos.get(i);
            String numero = (i - inicio == 9) ? "0" : String.valueOf((i - inicio) + 1);
            String status = p.getAtivo() ? "" : " (INATIVO)";
            System.out.println("(" + numero + ") " + p.getNome() + status);
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

    public Produto lerProduto() {
        System.out.println("\n-- NOVO PRODUTO --");
        System.out.print("Nome do produto: ");
        String nome = scanner.nextLine();
        System.out.print("Descrição: ");
        String descricao = scanner.nextLine();
        System.out.print("GTIN-13: ");
        String gtin = scanner.nextLine();

        return new Produto(nome, descricao, gtin);
    }

    // Método atualizado para menu de detalhes completo
    public int menuDetalhesProdutoCompleto(Produto produto, List<Lista> minhasListas, int outrasListas) {
        System.out.println("\nPresenteFácil 1.0");
        System.out.println("-----------------");
        System.out.println("> Início > Produtos > Listagem > " + produto.getNome() + "\n");
        
        System.out.println("NOME.......: " + produto.getNome());
        System.out.println("GTIN-13....: " + produto.getGtin13());
        System.out.println("DESCRIÇÃO..: " + produto.getDescricao());
        System.out.println("STATUS.....: " + (produto.getAtivo() ? "Ativo" : "Inativo"));
        
        System.out.println("\nAparece nas minhas listas:");
        if (minhasListas.isEmpty()) {
            System.out.println("- Nenhuma");
        } else {
            for (Lista lista : minhasListas) {
                System.out.println("- " + lista.getNome());
            }
        }
        
        if (outrasListas > 0) {
            System.out.println("\nAparece também em " + outrasListas + " lista(s) de outras pessoas.");
        }

        System.out.println("\n(1) Alterar os dados do produto");
        if (produto.getAtivo()) {
            System.out.println("(2) Inativar o produto");
        } else {
            System.out.println("(2) Reativar o produto");
        }
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

    public Produto editarProduto(Produto produto) {
        System.out.println("\n-- EDITAR PRODUTO --");
        System.out.println("(deixe em branco para manter o valor atual)");

        System.out.println("Nome atual: " + produto.getNome());
        System.out.print("Novo nome: ");
        String novoNome = scanner.nextLine();
        if (!novoNome.isBlank()) produto.setNome(novoNome);

        System.out.println("Descrição atual: " + produto.getDescricao());
        System.out.print("Nova descrição: ");
        String novaDesc = scanner.nextLine();
        if (!novaDesc.isBlank()) produto.setDescricao(novaDesc);

        System.out.println("GTIN-13 atual: " + produto.getGtin13());
        System.out.print("Novo GTIN-13: ");
        String novoGtin = scanner.nextLine();
        if (!novoGtin.isBlank()) produto.setGtin13(novoGtin);

        System.out.println("Status atual: " + (produto.getAtivo() ? "Ativo" : "Inativo"));
        System.out.print("Deseja alterar o status? (S/N): ");
        String alteraStatus = scanner.nextLine().trim().toUpperCase();
        if (alteraStatus.equals("S")) produto.setAtivo(!produto.getAtivo());

        return produto;
    }

    public boolean confirmarExclusao(Produto produto) {
        System.out.println("\n-- EXCLUIR PRODUTO --");
        System.out.println("Você tem certeza que deseja excluir o produto '" + produto.getNome() + "'?");
        System.out.print("Digite 'S' para confirmar ou qualquer outra tecla para cancelar: ");
        String resp = scanner.nextLine().trim().toUpperCase();
        return resp.equals("S");
    }
}