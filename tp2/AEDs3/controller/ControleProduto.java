package controller;

import dao.ArquivoProduto;
import dao.ArquivoLista;
import dao.ArquivoListaProduto;
import model.Produto;
import model.Lista;
import model.ListaProduto;
import model.Usuario;
import view.VisaoProduto;
import java.util.ArrayList;
import java.util.Comparator;

public class ControleProduto {

    private ArquivoProduto arquivoProduto;
    private ArquivoLista arquivoLista;
    private ArquivoListaProduto arquivoListaProduto;
    private VisaoProduto visaoProduto;
    private Usuario usuarioLogado;

    public ControleProduto(ArquivoProduto arquivoProduto, ArquivoLista arquivoLista, 
                          ArquivoListaProduto arquivoListaProduto, Usuario usuarioLogado) {
        this.arquivoProduto = arquivoProduto;
        this.arquivoLista = arquivoLista;
        this.arquivoListaProduto = arquivoListaProduto;
        this.visaoProduto = new VisaoProduto();
        this.usuarioLogado = usuarioLogado;
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
                                processarSelecaoProduto(produtos, indiceGlobal);
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

    private void processarSelecaoProduto(ArrayList<Produto> produtos, int indiceGlobal) {
        try {
            Produto produtoSelecionado = produtos.get(indiceGlobal);
            menuDetalhesProduto(produtoSelecionado);
        } catch (Exception e) {
            System.out.println("Erro ao acessar produto: " + e.getMessage());
        }
    }

    private void handleNovoProduto() {
        try {
            Produto novo = visaoProduto.lerProduto();

            if (!Produto.validadeGtin13(novo.getGtin13())) {
                System.out.println("\nGTIN-13 inválido! Produto não cadastrado.");
                return;
            }

            // Verificar se GTIN já existe
            Produto existente = arquivoProduto.readByGtin(novo.getGtin13());
            if (existente != null) {
                System.out.println("\nGTIN-13 já cadastrado para o produto: " + existente.getNome());
                return;
            }

            arquivoProduto.create(novo);
            System.out.println("\nProduto '" + novo.getNome() + "' cadastrado com sucesso!");
        } catch (Exception e) {
            System.out.println("\nErro: " + e.getMessage());
        }
    }

    // Método para obter informações sobre as listas do produto
    private ListaInfo obterInfoListasProduto(int idProduto, int idUsuarioLogado) throws Exception {
        ListaInfo info = new ListaInfo();
        
        // Buscar todas as associações do produto com listas
        ArrayList<ListaProduto> associacoes = arquivoListaProduto.readByProduto(idProduto);
        
        for (ListaProduto associacao : associacoes) {
            Lista lista = arquivoLista.read(associacao.getIdLista());
            if (lista != null) {
                if (lista.getIdUsuario() == idUsuarioLogado) {
                    info.minhasListas.add(lista);
                } else {
                    info.outrasListas++;
                }
            }
        }
        
        // Ordenar minhas listas por nome
        info.minhasListas.sort(Comparator.comparing(Lista::getNome, String.CASE_INSENSITIVE_ORDER));
        
        return info;
    }

    // Menu de detalhes do produto atualizado
    private void menuDetalhesProduto(Produto produto) throws Exception {
        int opcao;
        do {
            // Usa o ID real do usuário logado
            ListaInfo info = obterInfoListasProduto(produto.getId(), usuarioLogado.getId());
            
            opcao = visaoProduto.menuDetalhesProdutoCompleto(produto, info.minhasListas, info.outrasListas);

            switch(opcao) {
                case 1:
                    handleEditarProduto(produto);
                    break;
                case 2:
                    handleAlternarStatusProduto(produto);
                    opcao = 0; // Sai após alterar status
                    break;
                case 0:
                    break;
                default:
                    System.out.println("\nOpção inválida.");
            }
        } while(opcao != 0);
    }

    // Método para alternar entre ativo/inativo
    private void handleAlternarStatusProduto(Produto produto) throws Exception {
        if (produto.getAtivo()) {
            // Verificar se o produto está em alguma lista antes de inativar
            ListaInfo info = obterInfoListasProduto(produto.getId(), usuarioLogado.getId());
            int totalListas = info.minhasListas.size() + info.outrasListas;
            
            produto.setAtivo(false);
            arquivoProduto.update(produto);
            
            if (totalListas > 0) {
                System.out.println("\nProduto inativado com sucesso!");
                System.out.println("Observação: O produto continua vinculado a " + totalListas + " lista(s), mas não poderá ser adicionado a novas listas.");
            } else {
                System.out.println("\nProduto inativado com sucesso!");
            }
        } else {
            produto.setAtivo(true);
            arquivoProduto.update(produto);
            System.out.println("\nProduto reativado com sucesso!");
        }
    }

    private void handleEditarProduto(Produto produto) throws Exception {
        Produto atualizado = visaoProduto.editarProduto(produto);
        
        // Validar GTIN se foi alterado
        if (!produto.getGtin13().equals(atualizado.getGtin13())) {
            if (!Produto.validadeGtin13(atualizado.getGtin13())) {
                System.out.println("\nGTIN-13 inválido! Alterações não salvas.");
                return;
            }
            
            // Verificar se novo GTIN já existe em outro produto
            Produto existente = arquivoProduto.readByGtin(atualizado.getGtin13());
            if (existente != null && existente.getId() != atualizado.getId()) {
                System.out.println("\nGTIN-13 já cadastrado para outro produto: " + existente.getNome());
                return;
            }
        }
        
        arquivoProduto.update(atualizado);
        System.out.println("\nProduto atualizado com sucesso!");
    }

    // Classe auxiliar para informações das listas
    private static class ListaInfo {
        ArrayList<Lista> minhasListas = new ArrayList<>();
        int outrasListas = 0;
    }
}