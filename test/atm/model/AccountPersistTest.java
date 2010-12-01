
package atm.model;

import java.io.File;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;
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

    private final DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    private File testFile;
    private Account account;
    private AccountPersist persist;
    private ArrayList<Transaction> transactions = new ArrayList<Transaction>();

    private void setUpTransactions() throws ParseException {
        transactions.add(new Transaction(
            df.parse("15/11/2010 17:03:33"),
            "Depósito MB", Transaction.Type.CREDIT, 500
        ));
        transactions.add(new Transaction(
            df.parse("17/11/2010 14:20:12"),
            "Levantamento MB", Transaction.Type.DEBIT, 150
        ));
        transactions.add(new Transaction(
            df.parse("20/11/2010 20:13:44"),
            "Depósito MB", Transaction.Type.CREDIT, 250
        ));
        transactions.add(new Transaction(
            df.parse("22/11/2010 14:20:12"),
            "Pagamento de serviços Electricidade", Transaction.Type.DEBIT, 250
        ));
    }

    @Before
    public void setUp() throws IOException, ParseException {
        setUpTransactions();
        testFile = bucket.newFile("testFile.dat");
        persist  = new AccountPersist(testFile);
        account  = new Account("123456789", "Dummy User", persist);
        account.setBalance(600);
        for (Transaction transaction : transactions) {
            account.addTransaction(transaction);
        }
        persist.save(account);
    }

    @Test
    public void canReadBalance() {
        assertEquals(600, persist.restore().getBalance(), 1e-6);
    }

    @Test
    public void canReadTransactions() throws ParseException {
        ArrayList<Transaction> actual   = persist.restore().getTransactions();
        ArrayList<Transaction> expected = transactions;
        assertTrue(actual.containsAll(expected));
    }
}