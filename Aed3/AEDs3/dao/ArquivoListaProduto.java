package dao;

import base.Arquivo;
import model.ListaProduto;
import java.util.ArrayList;

public class ArquivoListaProduto extends Arquivo<ListaProduto> {

    public ArquivoListaProduto(String nomeArquivo) throws Exception {
        super(nomeArquivo, ListaProduto.class.getConstructor());
    }

    @Override
    public int create(ListaProduto obj) throws Exception {
        return super.create(obj);
    }

    @Override
    public ListaProduto read(int id) throws Exception {
        return super.read(id);
    }

    @Override
    public boolean delete(int id) throws Exception {
        return super.delete(id);
    }

    @Override
    public boolean update(ListaProduto obj) throws Exception {
        return super.update(obj);
    }

    /**
     * Busca todas as associações de um produto com listas
     */
    public ArrayList<ListaProduto> readByProduto(int idProduto) throws Exception {
        ArrayList<ListaProduto> resultados = new ArrayList<>();
        
        file.seek(4);
        while (file.getFilePointer() < file.length()) {
            char lapide = (char) file.readByte();
            short tam = file.readShort();
            byte[] ba = new byte[tam];
            file.readFully(ba);

            if (lapide == ' ') {
                ListaProduto lp = constructor.newInstance();
                lp.fromByteArray(ba);
                if (lp.getIdProduto() == idProduto) {
                    resultados.add(lp);
                }
            }
        }
        return resultados;
    }

    /**
     * Busca todas as associações de uma lista com produtos
     */
    public ArrayList<ListaProduto> readByLista(int idLista) throws Exception {
        ArrayList<ListaProduto> resultados = new ArrayList<>();
        
        file.seek(4);
        while (file.getFilePointer() < file.length()) {
            char lapide = (char) file.readByte();
            short tam = file.readShort();
            byte[] ba = new byte[tam];
            file.readFully(ba);

            if (lapide == ' ') {
                ListaProduto lp = constructor.newInstance();
                lp.fromByteArray(ba);
                if (lp.getIdLista() == idLista) {
                    resultados.add(lp);
                }
            }
        }
        return resultados;
    }

    /**
     * Busca uma associação específica entre lista e produto
     */
    public ListaProduto readByListaAndProduto(int idLista, int idProduto) throws Exception {
        file.seek(4);
        while (file.getFilePointer() < file.length()) {
            char lapide = (char) file.readByte();
            short tam = file.readShort();
            byte[] ba = new byte[tam];
            file.readFully(ba);

            if (lapide == ' ') {
                ListaProduto lp = constructor.newInstance();
                lp.fromByteArray(ba);
                if (lp.getIdLista() == idLista && lp.getIdProduto() == idProduto) {
                    return lp;
                }
            }
        }
        return null;
    }
}