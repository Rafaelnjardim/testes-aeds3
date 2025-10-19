package controller;

import model.Usuario;
import model.Lista;
import model.Produto;
import model.ListaProduto;
import dao.ArquivoLista;
import dao.ArquivoProduto;
import dao.ArquivoListaProduto;
import view.VisaoLista;
import java.util.ArrayList;
import java.util.Scanner;

public class ControleLista {
    private Usuario usuarioLogado;
    private ArquivoLista arquivoLista;
    private ArquivoProduto arquivoProduto;
    private ArquivoListaProduto arquivoListaProduto;
    private VisaoLista visaoLista;

    public ControleLista(Usuario usuarioLogado, ArquivoLista arquivoLista, ArquivoProduto arquivoProduto, ArquivoListaProduto arquivoListaProduto) {
        this.usuarioLogado = usuarioLogado;
        this.arquivoLista = arquivoLista;
        this.arquivoProduto = arquivoProduto;
        this.arquivoListaProduto = arquivoListaProduto;
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
                    handleGerenciarProdutosLista(lista);
                    break;
                case 2:
                    handleAlterarDadosLista(lista); 
                    break;
                case 3:
                    if (visaoLista.confirmarExclusaoLista(lista)) {
                        // Antes de excluir, remover todas as associações com produtos
                        ArrayList<ListaProduto> associacoes = arquivoListaProduto.readByLista(lista.getId());
                        for (ListaProduto associacao : associacoes) {
                            arquivoListaProduto.delete(associacao.getId());
                        }
                        // Agora excluir a lista
                        arquivoLista.delete(lista.getId());
                        System.out.println("\nLista excluída com sucesso!");
                        opcao = 0;
                    } else {
                        System.out.println("\nExclusão cancelada.");
                    }
                    break;
                case 0:
                    break;
                default:
                    System.out.println("\nOpção inválida.");
            }
        } while(opcao != 0);
    }

    private void handleAlterarDadosLista(Lista lista) throws Exception {
        Lista listaEditada = visaoLista.editarLista(lista);
        
        // Validar se o nome não ficou vazio
        if (listaEditada.getNome().isBlank()) {
            System.out.println("\nERRO: O nome da lista não pode ficar vazio.");
            return;
        }
        
        // Atualizar no arquivo
        boolean sucesso = arquivoLista.update(listaEditada);
        
        if (sucesso) {
            System.out.println("\nLista atualizada com sucesso!");
            System.out.println("Código compartilhável: " + listaEditada.getCodigoCompartilhavel());
        } else {
            System.out.println("\nERRO: Não foi possível atualizar a lista.");
        }
    }

    private void handleGerenciarProdutosLista(Lista lista) throws Exception {
        String opcao;
        do {
            ArrayList<ListaProduto> produtosNaLista = arquivoListaProduto.readByLista(lista.getId());
            opcao = visaoLista.menuProdutosLista(produtosNaLista, arquivoProduto, lista.getNome());

            if (opcao.equalsIgnoreCase("A")) {
                handleAcrescentarProduto(lista);
            } else if (!opcao.equalsIgnoreCase("R")) {
                try {
                    int index = Integer.parseInt(opcao) - 1;
                    if (index >= 0 && index < produtosNaLista.size()) {
                        menuDetalhesProdutoLista(lista, produtosNaLista.get(index));
                    } else {
                        System.out.println("Opção inválida.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Opção inválida.");
                }
            }
        } while (!opcao.equalsIgnoreCase("R"));
    }

    private void menuDetalhesProdutoLista(Lista lista, ListaProduto listaProduto) throws Exception {
        int opcao;
        do {
            Produto produto = arquivoProduto.read(listaProduto.getIdProduto());
            if (produto == null) {
                System.out.println("Produto não encontrado.");
                return;
            }

            opcao = visaoLista.menuDetalhesProdutoLista(produto, listaProduto, lista.getNome());

            switch(opcao) {
                case 1:
                    handleAlterarQuantidade(listaProduto);
                    break;
                case 2:
                    handleAlterarObservacoes(listaProduto);
                    break;
                case 3:
                    if (visaoLista.confirmarRemocaoProduto(produto)) {
                        arquivoListaProduto.delete(listaProduto.getId());
                        System.out.println("\nProduto removido da lista com sucesso!");
                        opcao = 0;
                    } else {
                        System.out.println("\nRemoção cancelada.");
                    }
                    break;
                case 0:
                    break;
                default:
                    System.out.println("\nOpção inválida.");
            }
        } while(opcao != 0);
    }

    private void handleAlterarQuantidade(ListaProduto listaProduto) throws Exception {
        System.out.print("\nNova quantidade: ");
        Scanner scanner = new Scanner(System.in);
        try {
            int novaQuantidade = Integer.parseInt(scanner.nextLine());
            if (novaQuantidade > 0) {
                listaProduto.setQuantidade(novaQuantidade);
                arquivoListaProduto.update(listaProduto);
                System.out.println("Quantidade atualizada com sucesso!");
            } else {
                System.out.println("Quantidade deve ser maior que zero.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Quantidade inválida.");
        }
    }

    private void handleAlterarObservacoes(ListaProduto listaProduto) throws Exception {
        System.out.print("\nNovas observações: ");
        Scanner scanner = new Scanner(System.in);
        String novasObservacoes = scanner.nextLine();
        listaProduto.setObservacoes(novasObservacoes);
        arquivoListaProduto.update(listaProduto);
        System.out.println("Observações atualizadas com sucesso!");
    }

    private void handleAcrescentarProduto(Lista lista) throws Exception {
        int opcao;
        do {
            opcao = visaoLista.menuAcrescentarProduto(lista.getNome());

            switch(opcao) {
                case 1:
                    handleBuscarProdutoPorGTIN(lista);
                    break;
                case 2:
                    handleListarTodosProdutos(lista);
                    break;
                case 0:
                    break;
                default:
                    System.out.println("\nOpção inválida.");
            }
        } while(opcao != 0);
    }

    private void handleBuscarProdutoPorGTIN(Lista lista) throws Exception {
        System.out.print("\nDigite o GTIN-13 do produto: ");
        Scanner scanner = new Scanner(System.in);
        String gtin = scanner.nextLine();

        Produto produto = arquivoProduto.readByGtin(gtin);
        if (produto != null && produto.getAtivo()) {
            if (confirmarAcrescentarProduto(lista, produto)) {
                acrescentarProdutoALista(lista, produto);
            }
        } else {
            System.out.println("Produto não encontrado ou inativo.");
        }
    }

    private void handleListarTodosProdutos(Lista lista) throws Exception {
        ArrayList<Produto> produtos = arquivoProduto.readAllActiveSortedByName();
        if (produtos.isEmpty()) {
            System.out.println("\nNenhum produto cadastrado.");
            return;
        }

        int paginaAtual = 1;
        int totalPaginas = (int) Math.ceil(produtos.size() / 10.0);
        String opcao;

        do {
            opcao = visaoLista.menuListagemProdutosParaAdicionar(produtos, paginaAtual, totalPaginas, lista.getNome());

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
                    try {
                        int indiceNaPagina;
                        if (opcao.equals("0")) {
                            indiceNaPagina = 9;
                        } else {
                            indiceNaPagina = Integer.parseInt(opcao) - 1;
                        }

                        if (indiceNaPagina >= 0 && indiceNaPagina < 10) {
                            int indiceGlobal = ((paginaAtual - 1) * 10) + indiceNaPagina;
                            if (indiceGlobal < produtos.size()) {
                                Produto produtoSelecionado = produtos.get(indiceGlobal);
                                if (confirmarAcrescentarProduto(lista, produtoSelecionado)) {
                                    acrescentarProdutoALista(lista, produtoSelecionado);
                                    opcao = "R";
                                }
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

    private boolean confirmarAcrescentarProduto(Lista lista, Produto produto) {
        System.out.println("\nProduto selecionado: " + produto.getNome());
        System.out.print("Deseja acrescentar este produto à lista '" + lista.getNome() + "'? (S/N): ");
        Scanner scanner = new Scanner(System.in);
        String resposta = scanner.nextLine().trim().toUpperCase();
        return resposta.equals("S");
    }

    private void acrescentarProdutoALista(Lista lista, Produto produto) throws Exception {
        // Verificar se o produto já está na lista
        ListaProduto existente = arquivoListaProduto.readByListaAndProduto(lista.getId(), produto.getId());
        if (existente != null) {
            System.out.println("\nEste produto já está na lista.");
            return;
        }

        // Ler quantidade e observações
        Scanner scanner = new Scanner(System.in);
        System.out.print("Quantidade desejada: ");
        int quantidade = 1;
        try {
            quantidade = Integer.parseInt(scanner.nextLine());
            if (quantidade <= 0) {
                System.out.println("Quantidade inválida. Usando 1 como padrão.");
                quantidade = 1;
            }
        } catch (NumberFormatException e) {
            System.out.println("Quantidade inválida. Usando 1 como padrão.");
        }

        System.out.print("Observações (opcional): ");
        String observacoes = scanner.nextLine();

        // Criar associação
        ListaProduto novaAssociacao = new ListaProduto(lista.getId(), produto.getId(), quantidade, observacoes);
        arquivoListaProduto.create(novaAssociacao);

        System.out.println("\nProduto '" + produto.getNome() + "' adicionado à lista com sucesso!");
    }
}