package controller;

import dao.ArquivoProduto;
import model.Produto;
import view.VisaoProduto;
import java.util.ArrayList;

public class ControleProduto {

    private ArquivoProduto arquivoProduto;
    private VisaoProduto visaoProduto;

    public ControleProduto(ArquivoProduto arquivoProduto) {
        this.arquivoProduto = arquivoProduto;
        this.visaoProduto = new VisaoProduto();
    }

    public void handleMenuProdutos() {
        String opcao;
        do {
            try {
                ArrayList<Produto> produtos = arquivoProduto.readAllActiveSortedByName();
                opcao = visaoProduto.menuProdutos(produtos);

                if (opcao.equals("1")) {
                    handleBuscarPorGTIN();
                } else if (opcao.equals("2")) {
                    handleListarTodosProdutos(produtos);
                } else if (opcao.equals("3")) {
                    handleNovoProduto();
                } else if (!opcao.equalsIgnoreCase("R")) {
                    System.out.println("Opção inválida.");
                }
            } catch(Exception e) {
                System.err.println("ERRO: Falha ao gerenciar produtos.");
                e.printStackTrace();
                opcao = "R";
            }
        } while (!opcao.equalsIgnoreCase("R"));
    }

    private void handleBuscarPorGTIN() {
        try {
            System.out.print("\nDigite o GTIN-13 para buscar: ");
            String gtin = new java.util.Scanner(System.in).nextLine();
            
            Produto produto = arquivoProduto.readByGtin(gtin);
            if (produto != null && produto.getAtivo()) {
                menuDetalhesProduto(produto);
            } else {
                System.out.println("\nProduto não encontrado ou inativo.");
            }
        } catch (Exception e) {
            System.out.println("\nErro ao buscar produto: " + e.getMessage());
        }
    }

    private void handleListarTodosProdutos(ArrayList<Produto> produtos) {
        if (produtos.isEmpty()) {
            System.out.println("\nNenhum produto cadastrado.");
            return;
        }

        int paginaAtual = 1;
        int totalPaginas = (int) Math.ceil(produtos.size() / 10.0);
        String opcao;
        
        do {
            opcao = visaoProduto.menuListagemProdutos(produtos, paginaAtual, totalPaginas);
            
            switch (opcao) {
                case "A":
                    if (paginaAtual > 1) {
                        paginaAtual--;
                    } else {
                        System.out.println("\nJá está na primeira página.");
                    }
                    break;
                    
                case "P":
                    if (paginaAtual < totalPaginas) {
                        paginaAtual++;
                    } else {
                        System.out.println("\nJá está na última página.");
                    }
                    break;
                    
                case "R":
                    break;
                    
                default:
                    // Processar seleção de produto (1-9, 0)
                    try {
                        int indiceNaPagina;
                        if (opcao.equals("0")) {
                            indiceNaPagina = 9; // O "0" representa o décimo item
                        } else {
                            indiceNaPagina = Integer.parseInt(opcao) - 1;
                        }
                        
                        if (indiceNaPagina >= 0 && indiceNaPagina < 10) {
                            int indiceGlobal = ((paginaAtual - 1) * 10) + indiceNaPagina;
                            if (indiceGlobal < produtos.size()) {
                                
                            } else {
                                System.out.println("Opção inválida.");
                            }
                        } else {
                            System.out.println("Opção inválida.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Opção inválida.");
                    }
                    break;
            }
        } while (!opcao.equals("R"));
    }

    private void handleNovoProduto() {
        try {
            Produto novo = visaoProduto.lerProduto();

            if (!Produto.validadeGtin13(novo.getGtin13())) {
                System.out.println("\nGTIN-13 inválido! Produto não cadastrado.");
                return;
            }

            arquivoProduto.create(novo);
            System.out.println("\nProduto '" + novo.getNome() + "' cadastrado com sucesso!");
        } catch (Exception e) {
            System.out.println("\nErro: " + e.getMessage());
        }
    }

    private void menuDetalhesProduto(Produto produto) throws Exception {
        int opcao;
        do {
            opcao = visaoProduto.menuDetalhesProduto(produto);

            switch(opcao) {
                case 1:
                    handleEditarProduto(produto);
                    break;
                case 2:
                    handleExcluirProduto(produto);
                    opcao = 0; 
                    break;
                case 0: break;
                default:
                    System.out.println("\nOpção inválida.");
            }
        } while(opcao != 0);
    }

    private void handleEditarProduto(Produto produto) throws Exception {
        Produto atualizado = visaoProduto.editarProduto(produto);
        arquivoProduto.update(atualizado);
        System.out.println("\nProduto atualizado com sucesso!");
    }

    private void handleExcluirProduto(Produto produto) throws Exception {
        boolean confirmado = visaoProduto.confirmarExclusao(produto);
        if (confirmado) {
            arquivoProduto.delete(produto.getId());
            System.out.println("\nProduto excluído com sucesso!");
        } else {
            System.out.println("\nExclusão cancelada.");
        }
    }
}