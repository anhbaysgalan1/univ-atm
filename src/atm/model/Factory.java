
package atm.model;

import java.io.File;

public class Factory {
    
    /**
     * Permite abstracção na localização dos ficheiros de dados.
     *
     * Uso: File file = Factory.getFile("dados.txt");
     *
     * @param filename  nome do ficheiro de dados
     * @return          objecto File
     */
    static File getFile(String filename) {
        return new File("data" + File.separator + filename);
    }
}
