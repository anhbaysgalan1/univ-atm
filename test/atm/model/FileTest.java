
package atm.model;

import org.junit.Before;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;


/**
 *
 * @author heldercorreia
 */
public class FileTest {

    private File template = new File("testClienteTemplate.dat");
    private File instance;

    public FileTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {}

    @AfterClass
    public static void tearDownClass() throws Exception {}

    @Before
    public void setUp() throws Exception {
        if (instance != null && instance.exists()) {
            instance.delete();
        }
        instance = File.createTempFile();
    }

    @Test
    public void canCopyFileContents() throws Exception {
        instance.copy(template);
        assertTrue("files are not equal", instance.equals(template));
    }

}