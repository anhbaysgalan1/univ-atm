
package atm.model;

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

public class AccountMapperTest {

    @Rule public TemporaryFolder bucket = new TemporaryFolder();

    private Account account;
    private AccountMapper mapper;
    private ArrayList<Transaction> transactions = new ArrayList<Transaction>();

    private void setUpTransactions() throws ParseException {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
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

    private void setUpAccount(double startingBalance) {
        account = new Account("123456789", "Dummy User", mapper);
        account.setBalance(startingBalance);
    }

    @Before
    public void setUp() throws IOException, ParseException {
        mapper = new AccountMapper(bucket.newFile("testFile.dat"));
        setUpTransactions();
        setUpAccount(600);
        for (Transaction transaction : transactions) {
            account.addTransaction(transaction);
        }
        mapper.save(account);
    }

    @Test
    public void canLoadDataFromMapper() {
        setUpAccount(0);
        mapper.load(account);
        assertEquals(600, account.getBalance(), 1e-6);
        assertTrue(account.getTransactions().containsAll(transactions));
    }
}