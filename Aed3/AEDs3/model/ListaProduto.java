package model;

import base.Registro;
import java.io.*;

public class ListaProduto implements Registro {
    private int id;
    private int idLista;
    private int idProduto;
    private int quantidade;
    private String observacoes;

    public ListaProduto() {
        this.id = -1;
        this.idLista = -1;
        this.idProduto = -1;
        this.quantidade = 1;
        this.observacoes = "";
    }

    public ListaProduto(int idLista, int idProduto, int quantidade, String observacoes) {
        this();
        this.idLista = idLista;
        this.idProduto = idProduto;
        this.quantidade = quantidade;
        this.observacoes = observacoes;
    }

    @Override
    public int getId() { return id; }

    @Override
    public void setId(int id) { this.id = id; }

    @Override
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(id);
        dos.writeInt(idLista);
        dos.writeInt(idProduto);
        dos.writeInt(quantidade);
        dos.writeUTF(observacoes);
        return baos.toByteArray();
    }

    @Override
    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        this.id = dis.readInt();
        this.idLista = dis.readInt();
        this.idProduto = dis.readInt();
        this.quantidade = dis.readInt();
        this.observacoes = dis.readUTF();
    }

    // Getters e Setters
    public int getIdLista() { return idLista; }
    public void setIdLista(int idLista) { this.idLista = idLista; }
    
    public int getIdProduto() { return idProduto; }
    public void setIdProduto(int idProduto) { this.idProduto = idProduto; }
    
    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }
    
    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
}