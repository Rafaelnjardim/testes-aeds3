package view;

import model.Usuario;
import java.util.Scanner;

public class VisaoUsuario {
    private Scanner scanner;

    public VisaoUsuario() {
        this.scanner = new Scanner(System.in, "UTF-8");
    }

    public String[] leLogin() {
        System.out.println("\n-- LOGIN --");
        System.out.print("E-mail: ");
        String email = scanner.nextLine();
        System.out.print("Senha: ");
        String senha = scanner.nextLine();
        return new String[]{email, senha};
    }

    public Usuario leUsuario() {
        System.out.println("\n-- NOVO USUÁRIO --");
        System.out.print("Nome completo: ");
        String nome = scanner.nextLine();
        System.out.print("E-mail: ");
        String email = scanner.nextLine();
        System.out.print("Crie uma senha: ");
        String senha = scanner.nextLine();
        System.out.print("Pergunta secreta (ex: nome do primeiro animal): ");
        String pergunta = scanner.nextLine();
        System.out.print("Resposta secreta: ");
        String resposta = scanner.nextLine();
        return new Usuario(nome, email, senha, pergunta, resposta);
    }
    
    public void mostraUsuario(Usuario u) {
        System.out.println("\n-- MEUS DADOS --");
        System.out.println(u.toString());
    }

    //Para recuperação de senha via pergunta secreta
    public boolean tentarRecuperacaoSenha(Usuario usuario) {
        System.out.println("\n-- RECUPERAÇÃO DE SENHA --");
        System.out.println("Pergunta secreta: " + usuario.getPerguntaSecreta());
        System.out.print("Resposta: ");
        String resposta = scanner.nextLine();
        
        if (resposta.equalsIgnoreCase(usuario.getRespostaSecreta())) {
            System.out.print("Nova senha: ");
            String novaSenha = scanner.nextLine();
            usuario.setSenha(novaSenha);
            System.out.println("Senha alterada com sucesso!");
            return true;
        } else {
            System.out.println("Resposta incorreta. Tente novamente.");
            return false;
        }
    }

    //Oferece opção de recuperação
    public boolean oferecerRecuperacao() {
        System.out.print("\nEsqueceu a senha? (S/N): ");
        String opcao = scanner.nextLine().toUpperCase();
        return opcao.equals("S");
    }
}