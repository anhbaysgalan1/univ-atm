
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
    public void canMakeDeposit() {
        account.setBalance(600.0);
        account.deposit(200.0);
        assertEquals(800.0, account.getBalance(), PRECISION);
    }

    @Test
    public void negativeValuesDepositAPositiveAmmount() {
        account.setBalance(100.0);
        account.deposit(-50.0);
        assertEquals(150.0, account.getBalance(), PRECISION);
    }

    @Test
    public void canMakeWithdrawal() {
        account.setBalance(600.0);
        account.withdraw(50.0);
        assertEquals(550.0, account.getBalance(), PRECISION);
    }

    @Test
    public void negativeValuesWithdrawAPositiveAmmount() {
        account.setBalance(100.0);
        account.withdraw(-50.0);
        assertEquals(50.0, account.getBalance(), PRECISION);
    }

    @Test(expected=IllegalArgumentException.class)
    public void doesNotAllowWithdrawalsNotMultipleOfFive() {
        account.setBalance(600.0);
        account.withdraw(57.0);
    }

    @Test(expected=IllegalArgumentException.class)
    public void doesNotAllowWithdrawalsBellowTen() {
        account.setBalance(600.0);
        account.withdraw(5.0);
    }

    @Test(expected=IllegalArgumentException.class)
    public void doesNotAllowWithdrawalsAboveTwoHundred() {
        account.setBalance(600.0);
        account.withdraw(205.0);
    }

    @Test(expected=IllegalArgumentException.class)
    public void doesNotAllowWithdrawalWhenNotEnoughFunds() {
        account.setBalance(50.0);
        account.withdraw(100.0);
    }

    @Test
    public void allowsWithrawingAllFunds() {
        account.setBalance(150.0);
        account.withdraw(150.0);
        assertEquals(0.0, account.getBalance(), PRECISION);
    }
}