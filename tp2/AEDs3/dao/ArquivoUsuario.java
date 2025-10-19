package dao;

import base.Arquivo;
import base.HashExtensivel;
import model.Usuario;
import index.ParEmailID;

public class ArquivoUsuario extends Arquivo<Usuario> {

    private HashExtensivel<ParEmailID> indiceEmail;

    public ArquivoUsuario(String nomeArquivo) throws Exception {
        super(nomeArquivo, Usuario.class.getConstructor());
        indiceEmail = new HashExtensivel<>(
            ParEmailID.class.getConstructor(), 4,
            "dados/usuarios.email.idx_d.db",
            "dados/usuarios.email.idx_c.db"
        );
    }

    @Override
    public int create(Usuario obj) throws Exception {
        int id = super.create(obj);
        indiceEmail.create(new ParEmailID(obj.getEmail(), id));
        return id;
    }

    public Usuario readByEmail(String email) throws Exception {
        ParEmailID par = indiceEmail.read(email.hashCode());
        if (par != null && par.getEmail().equals(email)) {
            return super.read(par.getId());
        }
        return null;
    }

    @Override
    public boolean delete(int id) throws Exception {
        Usuario u = super.read(id);
        if (u != null) {
            if (indiceEmail.delete(u.getEmail().hashCode())) {
                return super.delete(id);
            }
        }
        return false;
    }

    @Override
    public boolean update(Usuario objAlterado) throws Exception {

        Usuario usuarioAntigo = super.read(objAlterado.getId());
        if (usuarioAntigo != null) {
            indiceEmail.delete(usuarioAntigo.getEmail().hashCode());
        }

        boolean sucesso = super.update(objAlterado);
 
        if (sucesso) {
            indiceEmail.create(new ParEmailID(objAlterado.getEmail(), objAlterado.getId()));
        }
        
        return sucesso;
    }
}