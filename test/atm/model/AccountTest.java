
package atm.model;

import org.junit.After;
import org.junit.Before;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author heldercorreia
 */
public class AccountTest {

    private double precision = 1e-5;
    private File testFileTemplate = new File("testClienteTemplate.dat");
    private File testFile = new File("testCliente.dat");
    private Account account;

    public AccountTest() {}

    @BeforeClass
    public static void setUpClass() throws Exception {}

    @AfterClass
    public static void tearDownClass() throws Exception {}

    @Before
    public void setUp() throws java.io.IOException {
        testFile.copy(testFileTemplate);
        account = new Account("0010029289641272009", "Rui Filipe Tavares Melo");
        account.load(testFile);
    }

    @After
    public void tearDown() {
        account = null;
    }

    @Test
    public void accountLoadsBalance() {
        assertEquals(200.0, account.getBalance(), precision);
    }

    @Test
    public void canMakeDeposit() {
        account.deposit(200.0);
        assertEquals(400.0, account.getBalance(), precision);
    }

}