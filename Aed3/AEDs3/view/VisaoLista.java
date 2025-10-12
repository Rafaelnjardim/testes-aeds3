package view;

import model.Lista;
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
        return new Lista(0, nome, desc, dataLimite); // idUsuario será setado no controller
    }
    
    public int menuDetalhesLista(Lista lista, String nomeUsuario) {
        System.out.println("\nPresenteFácil 1.0");
        System.out.println("-----------------");
        System.out.println("> Início > Minhas listas > " + lista.getNome() + "\n");
        
        System.out.println(lista.toString());
        
        System.out.println("\n(1) Gerenciar produtos da lista (Não implementado)");
        System.out.println("(2) Alterar dados da lista");
        System.out.println("(3) Excluir lista");
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
}
