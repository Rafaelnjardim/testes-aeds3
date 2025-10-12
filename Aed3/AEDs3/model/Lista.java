package model;

import base.Registro;
import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Lista implements Registro {

    private int id;
    private int idUsuario;
    private String nome;
    private String descricao;
    private long dataCriacao;
    private long dataLimite;
    private String codigoCompartilhavel;

    public Lista() {
        this.id = -1;
        this.idUsuario = -1;
        this.nome = "";
        this.descricao = "";
        this.dataCriacao = System.currentTimeMillis();
        this.dataLimite = 0;
        this.codigoCompartilhavel = NanoIdUtils.randomNanoId();
    }

    public Lista(int idUsuario, String nome, String descricao, long dataLimite) {
        this();
        this.idUsuario = idUsuario;
        this.nome = nome;
        this.descricao = descricao;
        this.dataLimite = dataLimite;
    }
    
    @Override public int getId() { return id; }
    @Override public void setId(int id) { this.id = id; }
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public long getDataCriacao() { return dataCriacao; }
    public long getDataLimite() { return dataLimite; }
    
    // SETTER NOVO PARA DATA LIMITE
    public void setDataLimite(long dataLimite) { 
        this.dataLimite = dataLimite; 
    }
    
    public String getCodigoCompartilhavel() { return codigoCompartilhavel; }
    
    private String formatarData(long timestamp) {
        if (timestamp == 0) return "N/A";
        return new SimpleDateFormat("dd/MM/yyyy").format(new Date(timestamp));
    }

    @Override
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(id);
        dos.writeInt(idUsuario);
        dos.writeUTF(nome);
        dos.writeUTF(descricao);
        dos.writeLong(dataCriacao);
        dos.writeLong(dataLimite);
        dos.writeUTF(codigoCompartilhavel);
        return baos.toByteArray();
    }

    @Override
    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        this.id = dis.readInt();
        this.idUsuario = dis.readInt();
        this.nome = dis.readUTF();
        this.descricao = dis.readUTF();
        this.dataCriacao = dis.readLong();
        this.dataLimite = dis.readLong();
        this.codigoCompartilhavel = dis.readUTF();
    }

    @Override
    public String toString() {
        return "CÓDIGO: " + codigoCompartilhavel +
               "\nNOME: " + nome +
               "\nDESCRIÇÃO: " + descricao +
               "\nDATA DE CRIAÇÃO: " + formatarData(dataCriacao) +
               "\nDATA LIMITE: " + formatarData(dataLimite);
    }
}