
package atm.model;

import java.io.File;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FileTest {

    /**
     * Copia os conteúdos de um ficheiro para este, assumindo que
     * não há erros de leitura.
     *
     * @param src  ficheiro de origem da cópia
     * @param dst  ficheiro de destino da cópia
     */
    static void copy(File src, File dst) throws java.io.IOException {
        java.io.FileReader in  = new java.io.FileReader(src);
        java.io.FileWriter out = new java.io.FileWriter(dst);

        int c;
        while ((c = in.read()) != -1) {
            out.write(c);
        }

        in.close();
        out.close();
    }

    /**
     * Compara dois ficheiros, para ver se têm o mesmo conteúdo.
     *
     * @param orig  ficheiro de origem
     * @param dest  ficheiro de destino
     * @return      true se os ficheiros têm o mesmo conteúdo;
     *              false caso contrário
     */
    static boolean equals(File orig, File dest) throws java.io.IOException {
        java.io.FileReader src = new java.io.FileReader(orig);
        java.io.FileReader dst = new java.io.FileReader(dest);

        int s, d;
        while (((s = src.read()) != -1) && ((d = dst.read()) != -1)) {
            if (s != d) {
                return false;
            }
        }

        return true;
    }

    @Rule public TemporaryFolder bucket = new TemporaryFolder();

    private final File template = Factory.getFile("testClienteTemplate.dat");
    private File instance;

    @Before
    public void setUp() throws Exception {
        instance = bucket.newFile("testFile.txt");
    }

    @Test
    public void canCopyFileContents() throws Exception {
        copy(template, instance);
        assertTrue("files are not equal", equals(instance, template));
    }
}