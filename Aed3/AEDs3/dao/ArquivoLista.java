package dao;

import base.Arquivo;
import index.ParIntInt;
import index.ArvoreBMais;
import model.Lista;

import java.util.ArrayList;
import java.util.Comparator;

public class ArquivoLista extends Arquivo<Lista> {

    private ArvoreBMais<ParIntInt> indiceUsuarioLista;

    public ArquivoLista(String nomeArquivo) throws Exception {
        super(nomeArquivo, Lista.class.getConstructor());
        indiceUsuarioLista = new ArvoreBMais<>(
            ParIntInt.class.getConstructor(),
            5,
            "dados/listas.usuario.idx.db"
        );
    }

    @Override
    public int create(Lista obj) throws Exception {
        int id = super.create(obj);
        indiceUsuarioLista.create(new ParIntInt(obj.getIdUsuario(), id));
        return id;
    }

    @Override
    public Lista read(int id) throws Exception {
        return super.read(id);
    }

    @Override
    public boolean delete(int id) throws Exception {
        Lista l = super.read(id);
        if (l != null) {
            indiceUsuarioLista.delete(new ParIntInt(l.getIdUsuario(), id));
            return super.delete(id);
        }
        return false;
    }

    @Override
    public boolean update(Lista obj) throws Exception {
        return super.update(obj);
    }

    /**
     * Lê todas as listas de um usuário.
     * Primeiro tenta via índice, se não encontrar, faz varredura no arquivo.
     */
    public ArrayList<Lista> readListasByUsuario(int idUsuario) throws Exception {
        ArrayList<Lista> listas = new ArrayList<>();

        //  Tenta pelo índice B+
        try {
            ArrayList<ParIntInt> pares = indiceUsuarioLista.read(new ParIntInt(idUsuario, -1));
            for (ParIntInt p : pares) {
                Lista l = super.read(p.getNum2()); // num2 = idLista
                if (l != null) listas.add(l);
            }
        } catch (Exception e) {
            // se der problema, vai para a leitura direta
        }

        //  Se índice não retornou nada, varredura direta no arquivo
        if (listas.isEmpty()) {
            file.seek(4); // pula cabeçalho
            while (file.getFilePointer() < file.length()) {
                char lapide = (char) file.readByte();
                short tam = file.readShort();
                byte[] ba = new byte[tam];
                file.readFully(ba);

                if (lapide == ' ') {
                    Lista l = constructor.newInstance();
                    l.fromByteArray(ba);
                    if (l.getIdUsuario() == idUsuario) {
                        listas.add(l);
                    }
                }
            }
        }

        // ordena por nome antes de devolver
        listas.sort(Comparator.comparing(Lista::getNome));
        return listas;
    }

    /**
     * Busca lista pelo código compartilhável.
     */
    public Lista readByCodigo(String codigo) throws Exception {
        file.seek(4);
        while (file.getFilePointer() < file.length()) {
            char lapide = (char) file.readByte();
            short tam = file.readShort();
            byte[] ba = new byte[tam];
            file.readFully(ba);

            if (lapide == ' ') {
                Lista l = constructor.newInstance();
                l.fromByteArray(ba);
                if (l.getCodigoCompartilhavel().equals(codigo)) {
                    return l;
                }
            }
        }
        return null;
    }
}

