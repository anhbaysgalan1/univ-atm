
package atm.model;

import java.io.IOException;
import org.junit.rules.TemporaryFolder;
import org.junit.Rule;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AccountTest {

    @Rule public TemporaryFolder bucket = new TemporaryFolder();

    private final double PRECISION = 1e-6;
    private final String testAccountNum = "0010029289641272009";
    private final String testClientName = "Rui Filipe Tavares Melo";

    private AccountMapper mapper;
    private Account account;

    @Before
    public void setUp() throws IOException {
        mapper  = new AccountMapper(bucket.newFile("testFile.dat"));
        account = new Account(testAccountNum, testClientName, mapper);
        account.setBalance(600);
    }

    @Test
    public void accountLoadsBalance() {
        assertEquals(600.0, account.getBalance(), PRECISION);
    }

    @Test
    public void canMakeCredit() {
        account.setBalance(600.0);
        account.credit(200.0);
        assertEquals(800.0, account.getBalance(), PRECISION);
    }

    @Test
    public void negativeValuesCreditAPositiveAmount() {
        account.setBalance(100.0);
        account.credit(-50.0);
        assertEquals(150.0, account.getBalance(), PRECISION);
    }

    @Test
    public void canMakeDebit() {
        account.setBalance(600.0);
        account.debit(50.0);
        assertEquals(550.0, account.getBalance(), PRECISION);
    }

    @Test
    public void negativeValuesDebitAPositiveAmount() {
        account.setBalance(100.0);
        account.debit(-50.0);
        assertEquals(50.0, account.getBalance(), PRECISION);
    }

    @Test(expected=IllegalArgumentException.class)
    public void doesNotAllowDebitWhenNotEnoughFunds() {
        account.setBalance(50.0);
        account.debit(100.0);
    }

    @Test
    public void allowsDebitOfAllFunds() {
        account.setBalance(150.0);
        account.debit(150.0);
        assertEquals(0.0, account.getBalance(), PRECISION);
    }
}