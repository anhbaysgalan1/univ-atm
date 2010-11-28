
package atm.model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AtmClientTest {

    private final double PRECISION = 1e-6;

    private AtmClient atm;
    private AccountManager manager;
    private Account account;

    @Before
    public void setUp() {
        atm     = new AtmClient(300);
        manager = new MockAccountPersist();
        account = new Account("123456789", "Dummy client", manager);
    }

    @Test
    public void testHasEnoughWithdrawalFunds() {
        atm = new AtmClient(5);
        assertFalse("can't allow when 5", atm.hasEnoughWithdrawalFunds());
        atm = new AtmClient(10);
        assertTrue("must allow when 10", atm.hasEnoughWithdrawalFunds());
    }

    @Test
    public void depositDoesNotUpdateFunds() {
        atm.deposit(50, account);
        assertEquals(300, atm.getFunds(), PRECISION);
    }

    @Test
    public void canMakeWithdrawal() {
        atm.withdraw(50, account);
        assertEquals(250, atm.getFunds(), PRECISION);
    }

    @Test
    public void negativeValuesWithdrawAPositiveAmmount() {
        atm.withdraw(-50, account);
        assertEquals(250, atm.getFunds(), PRECISION);
    }

    @Test(expected=IllegalArgumentException.class)
    public void doesNotAllowWithdrawalsNotMultipleOfFive() {
        atm.withdraw(57, account);
    }

    @Test(expected=IllegalArgumentException.class)
    public void doesNotAllowWithdrawalsBellowTen() {
        atm.withdraw(5, account);
    }

    @Test(expected=IllegalArgumentException.class)
    public void doesNotAllowWithdrawalsAboveTwoHundred() {
        atm.withdraw(210, account);
    }

    @Test(expected=IllegalArgumentException.class)
    public void doesNotAllowWithdrawalWhenNotEnoughFunds() {
        atm = new AtmClient(5);
        atm.withdraw(10, account);
    }

    @Test
    public void allowsWithrawingAllFunds() {
        atm = new AtmClient(100);
        atm.withdraw(100, account);
        assertEquals(0, atm.getFunds(), PRECISION);
    }

}