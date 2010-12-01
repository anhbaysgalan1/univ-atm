
package atm.model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AccountTest {

    private final double PRECISION = 1e-6;
    private final String testAccountNum = "0010029289641272009";
    private final String testClientName = "Rui Filipe Tavares Melo";

    private AccountManager manager;
    private Account account;

    @Before
    public void setUp() {
        manager = new MockAccountPersist();
        account = new Account(testAccountNum, testClientName, manager);
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
    public void negativeValuesCreditAPositiveAmmount() {
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
    public void negativeValuesDebitAPositiveAmmount() {
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