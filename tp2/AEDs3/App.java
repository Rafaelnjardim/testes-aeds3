import controller.ControlePrincipal;
import java.io.File;

public class App {
    public static void main(String[] args) {
        // Cria a pasta 'dados' se ela não existir
        File dataDir = new File("dados");
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }

        ControlePrincipal cp = new ControlePrincipal();
        cp.iniciar();
    }
}
