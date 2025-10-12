package index;

import base.RegistroHashExtensivel;
import java.io.*;

public class ParEmailID implements RegistroHashExtensivel<ParEmailID> {
    private String email;
    private int id;

    // 40 caracteres fixos para o e-mail (40 bytes) + 4 bytes para int = 44
    private static final short SIZEOF_STRING = 40;
    private static final short SIZEOF_RECORD = SIZEOF_STRING + 4;

    public ParEmailID() {
        this.email = "";
        this.id = -1;
    }

    public ParEmailID(String email, int id) {
        this.email = email;
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public String getEmail() {
        return this.email;
    }

    @Override
    public int hashCode() {
        return this.email.hashCode();
    }

    @Override
    public short size() {
        return SIZEOF_RECORD;
    }

    @Override
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        // Escreve o email com tamanho fixo de 40 caracteres (com padding)
        String s = String.format("%1$-" + SIZEOF_STRING + "s", this.email);
        dos.writeBytes(s.substring(0, SIZEOF_STRING));

        // Escreve o id
        dos.writeInt(this.id);

        return baos.toByteArray();
    }

    @Override
    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);

        // Lê os 40 bytes do e-mail
        byte[] emailBytes = new byte[SIZEOF_STRING];
        dis.readFully(emailBytes);
        this.email = new String(emailBytes).trim();

        // Lê o id
        this.id = dis.readInt();
    }

    @Override
    public ParEmailID clone() {
        return new ParEmailID(this.email, this.id);
    }
}

