
package atm.model;

import java.io.File;
import org.junit.rules.TemporaryFolder;
import org.junit.Rule;
import java.io.UnsupportedEncodingException;
import org.junit.Before;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.io.IOException;
import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.*;

public class AccountPersistTest {

    @Rule public TemporaryFolder bucket = new TemporaryFolder();

    private final double PRECISION = 1e-6;
    private final DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    private final File testFileTemplate =
            Factory.getFile("testClienteTemplate.dat");
    private final File testFileWritten  =
            Factory.getFile("testClienteWritten.dat");

    private File testFile;
    private Account account;
    private AccountPersist dataSource;

    @Before
    public void setUp() throws IOException {
        testFile = bucket.newFile("testFile.dat");
        FileTest.copy(testFileTemplate, testFile);
        dataSource = new AccountPersist(testFile);
        account = new Account("123456789", "Dummy User", dataSource);
    }

    @Test
    public void canReadBalance() {
        assertEquals(600.00, account.getBalance(), PRECISION);
    }

    @Test
    public void canReadTransactions() throws ParseException {
        ArrayList<Transaction> actual   = account.getTransactions();
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
        account.setBalance(400);
        account.addTransaction(new Transaction(
            df.parse("20/11/2010 20:13:44"),
            "Depósito MB", Transaction.Type.CREDIT, 650
        ));
        account.addTransaction(new Transaction(
            df.parse("17/11/2010 14:20:12"),
            "Pagamento de serviços Electricidade", Transaction.Type.DEBIT, 250
        ));

        dataSource.save(account);
        assertTrue("not saved correctly",
            FileTest.equals(testFile, testFileWritten)
        );
    }
}