
package atm.model;

import java.io.UnsupportedEncodingException;
import org.junit.Before;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.io.IOException;
import java.util.ArrayList;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author heldercorreia
 */
public class AccountPersistTest {

    private final double precision = 1e-5;
    private final DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private final File testFileTemplate = new File("testClienteTemplate.dat");
    private final File testFileWritten  = new File("testClienteWritten.dat");
    private File testFile = new File("testCliente.dat");
    private AccountPersist dataSource;

    public AccountPersistTest() {
        dataSource = new AccountPersist(testFile);
    }

    @BeforeClass
    public static void setUpClass() throws Exception {}

    @AfterClass
    public static void tearDownClass() throws Exception {}

    @Before
    public void setUp() throws IOException {
        testFile.copy(testFileTemplate);
    }

    @Test
    public void canReadBalance() throws IOException {
        assertEquals(600.00, dataSource.getBalance(), precision);
    }

    @Test
    public void canReadTransactions() throws ParseException, IOException {
        ArrayList<Transaction> actual   = dataSource.getTransactions();
        ArrayList<Transaction> expected = new ArrayList<Transaction>();

        expected.add(new Transaction(
            df.parse("15/11/2010 17:03:33"),
            "Depósito MB", Transaction.Type.CREDIT, 500
        ));
        expected.add(new Transaction(
            df.parse("17/11/2010 14:20:12"),
            "Levantamento MB", Transaction.Type.DEBIT, 150
        ));
        expected.add(new Transaction(
            df.parse("20/11/2010 20:13:44"),
            "Depósito MB", Transaction.Type.CREDIT, 250
        ));
        
        assertTrue(
            "Collections not the same size",
            expected.size() == actual.size()
        );

        System.out.println("### AccountPersistTest: Transactions ###");
        for (int i = 0; i < expected.size(); i++) {
            assertTrue(
                "Expected on index "+i+": "
              + "<"+expected.get(i)+"> but was <"+actual.get(i)+">",
                expected.get(i).equals(actual.get(i))
            );
            System.out.println(actual.get(i));
        }
    }

    @Test
    public void canWriteToFile() throws ParseException,
                                        UnsupportedEncodingException,
                                        IOException {
        dataSource.setBalance(400);
        dataSource.addTransaction(new Transaction(
            df.parse("20/11/2010 20:13:44"),
            "Depósito MB", Transaction.Type.CREDIT, 650
        ));
        dataSource.addTransaction(new Transaction(
            df.parse("17/11/2010 14:20:12"),
            "Pagamento de serviços Electricidade", Transaction.Type.DEBIT, 250
        ));
        dataSource.save();
        assertTrue("not saved correctly", testFile.equals(testFileWritten));
    }
}