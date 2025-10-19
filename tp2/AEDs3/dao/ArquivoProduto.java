package dao;

import base.Arquivo;
import model.Produto;
import java.util.ArrayList;
import java.util.Comparator;

public class ArquivoProduto extends Arquivo<Produto> {

    public ArquivoProduto(String nomeArquivo) throws Exception {
        super(nomeArquivo, Produto.class.getConstructor());
    }

    @Override
    public int create(Produto obj) throws Exception {
        Produto existente = readByGtin(obj.getGtin13());
        if (existente != null) {
            throw new Exception("GTIN já cadastrado (ID: " + existente.getId() + ")");
        }
        int id = super.create(obj);
        return id;
    }

    @Override
    public Produto read(int id) throws Exception {
        return super.read(id);
    }

    @Override
    public boolean delete(int id) throws Exception {
        Produto p = super.read(id);
        if (p != null) {
            return super.delete(id);
        }
        return false;
    }

    @Override
    public boolean update(Produto obj) throws Exception {
        return super.update(obj);
    }

    public Produto readByGtin(String gtin) throws Exception {
        file.seek(4);
        while (file.getFilePointer() < file.length()) {
            char lapide = (char) file.readByte();
            short tam = file.readShort();
            byte[] ba = new byte[tam];
            file.readFully(ba);

            if (lapide == ' ') {
                Produto p = constructor.newInstance();
                p.fromByteArray(ba);
                if (p.getGtin13().equals(gtin)) {
                    return p;
                }
            }
        }
        return null;
    }

    public ArrayList<Produto> readAllActiveSortedByName() throws Exception {
        ArrayList<Produto> produtos = new ArrayList<>();

        file.seek(4);
        while (file.getFilePointer() < file.length()) {
            char lapide = (char) file.readByte();
            short tam = file.readShort();
            byte[] ba = new byte[tam];
            file.readFully(ba);

            if (lapide == ' ') {
                Produto p = constructor.newInstance();
                p.fromByteArray(ba);
                if (p.getAtivo()) produtos.add(p);
            }
        }

        produtos.sort(Comparator.comparing(Produto::getNome, String.CASE_INSENSITIVE_ORDER));
        return produtos;
    }
}