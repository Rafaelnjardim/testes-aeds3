package model;

import base.Registro;
import java.io.*;
import java.security.MessageDigest;
import java.util.HexFormat;

public class Usuario implements Registro {

    private int id;
    private String nome;
    private String email;
    private String hashSenha;
    private String perguntaSecreta;
    private String respostaSecreta;

    public Usuario() {
        this.id = -1;
        this.nome = "";
        this.email = "";
        this.hashSenha = "";
        this.perguntaSecreta = "";
        this.respostaSecreta = "";
    }

    public Usuario(String nome, String email, String senha, String pergunta, String resposta) {
        this();
        this.nome = nome;
        this.email = email;
        this.setSenha(senha);
        this.perguntaSecreta = pergunta;
        this.respostaSecreta = resposta;
    }
    
    @Override public int getId() { return id; }
    @Override public void setId(int id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getHashSenha() { return hashSenha; }
    public String getPerguntaSecreta() { return perguntaSecreta; }
    public String getRespostaSecreta() { return respostaSecreta; }

    public void setSenha(String senha) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(senha.getBytes());
            this.hashSenha = HexFormat.of().formatHex(hash);
        } catch (Exception e) {
            this.hashSenha = "";
        }
    }
    
    public boolean validarSenha(String senha) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(senha.getBytes());
            return HexFormat.of().formatHex(hash).equals(this.hashSenha);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(id);
        dos.writeUTF(nome);
        dos.writeUTF(email);
        dos.writeUTF(hashSenha);
        dos.writeUTF(perguntaSecreta);
        dos.writeUTF(respostaSecreta);
        return baos.toByteArray();
    }

    @Override
    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        this.id = dis.readInt();
        this.nome = dis.readUTF();
        this.email = dis.readUTF();
        this.hashSenha = dis.readUTF();
        this.perguntaSecreta = dis.readUTF();
        this.respostaSecreta = dis.readUTF();
    }
    
    @Override
    public String toString() {
        return "ID: " + this.id + "\nNome: " + this.nome + "\nE-mail: " + this.email;
    }
}
