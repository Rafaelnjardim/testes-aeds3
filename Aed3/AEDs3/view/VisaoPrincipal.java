package view;

import java.util.Scanner;

public class VisaoPrincipal {
    private Scanner scanner;

    public VisaoPrincipal() {
        this.scanner = new Scanner(System.in, "UTF-8");
    }

    public int menuLogin() {
        System.out.println("\nPresenteFácil 1.0");
        System.out.println("-----------------");
        System.out.println("\n(1) Login");
        System.out.println("(2) Novo usuário");
        System.out.println("\n(S) Sair");
        System.out.print("Opção: ");
        String opcao = scanner.nextLine().toUpperCase();
        if (opcao.equals("S")) return 0;
        try {
            return Integer.parseInt(opcao);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    
    public int menuPrincipal() {
        System.out.println("\nPresenteFácil 1.0");
        System.out.println("-----------------");
        System.out.println("> Início\n");
        System.out.println("(1) Meus dados");
        System.out.println("(2) Minhas listas");
        System.out.println("(3) Produtos");
        System.out.println("(4) Buscar lista por código");
        System.out.println("\n(S) Sair (Logout)");
        System.out.print("Opção: ");
        String opcao = scanner.nextLine().toUpperCase();
        if (opcao.equals("S")) return 0;
        try {
            return Integer.parseInt(opcao);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
