package model;
import base.Registro;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Produto implements Registro {
    private int id;
    private String gtin13;
    private String nome;
    private String descricao;
    private boolean ativo;
    private long dataCadastro;

    public Produto(){
        this.id = -1;
        this.nome = "";
        this.descricao = "";
        this.ativo = true;
        this.dataCadastro = System.currentTimeMillis();
    }

    public Produto(String nome, String descricao, String gtin13){
        this();
        this.nome = nome;
        this.descricao = descricao;
        this.gtin13 = gtin13;
    }

    @Override
    public int getId(){
        return id;
    }

    @Override
    public void setId(int id){
        this.id = id;
    }

    @Override
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(id);
        dos.writeUTF(nome);
        dos.writeUTF(descricao);
        dos.writeUTF(gtin13);
        dos.writeBoolean(ativo);
        dos.writeLong(dataCadastro);
        return baos.toByteArray();
    }

    @Override
    public void fromByteArray(byte[] ba) throws IOException{
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        this.id = dis.readInt();
        this.nome = dis.readUTF();
        this.descricao = dis.readUTF();
        this.gtin13 = dis.readUTF();
        this.ativo = dis.readBoolean();
        this.dataCadastro = dis.readLong();
    }

    public String getNome(){
        return nome;
    }
    public void setNome(String nome){
        this.nome = nome;
    }

    public String getDescricao(){
        return descricao;
    }
    public void setDescricao(String descricao){
        this.descricao = descricao;
    }

    public String getGtin13(){
        return gtin13;
    }
    public void setGtin13(String gtin13){
        this.gtin13 = gtin13;
    }

    public boolean getAtivo(){
        return ativo;
    }
    public void setAtivo(boolean ativo){
        this.ativo = ativo;
    }

    public long getDataCadastro(){
        return dataCadastro;
    }
    public void setDataCadastro(long dataCadastro){
        this.dataCadastro = dataCadastro;
    }

    //Conferir depois
    private String formatarData(long timestamp){
        return new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date(timestamp));
    }

    @Override
    public String toString(){
        return "ID: " + id + "\nNOME: " + nome + "\nGTIN 13: " + gtin13 + "\nDESCRIÇÃO: " + descricao + "\nSTATUS: " + (ativo ? "Ativo" : "Inativo") + "\nDATA DE CADASTRO: " + formatarData(dataCadastro);
    }

    public static boolean validadeGtin13(String gtin){
        if(gtin == null || !gtin.matches("\\d{13}")){
            return false;
        }
        int soma = 0;
            for(int i = 0; i < 12; i++){
                int digito = gtin.charAt(i) - '0';
                soma += (i % 2 == 0) ? digito * 1 : digito * 3;
            }
        int mod = soma % 10;
        int verificador = (mod == 0) ? 0 : 10 - mod;
         return verificador == (gtin.charAt(12) - '0');
    }
}