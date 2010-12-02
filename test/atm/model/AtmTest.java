
package atm.model;

import java.io.IOException;
import org.junit.rules.TemporaryFolder;
import org.junit.Rule;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AtmTest {

    @Rule public TemporaryFolder bucket = new TemporaryFolder();

    private final double PRECISION = 1e-6;

    private Atm atm;
    private AccountMapper mapper;
    private Account account;
    private Payment payment;

    @Before
    public void setUp() throws IOException {
        atm     = new Atm(300);
        mapper  = new AccountMapper(bucket.newFile("testFile.dat"));
        account = new Account("123456789", "Dummy client", mapper);
        payment = new Payment("12345", "123456789", 60.53);
        account.setBalance(600);
    }

    @Test
    public void testHasEnoughWithdrawalFunds() {
        atm = new Atm(5);
        assertFalse("can't allow when 5", atm.hasEnoughWithdrawalFunds());
        atm = new Atm(10);
        assertTrue("must allow when 10", atm.hasEnoughWithdrawalFunds());
    }

    @Test
    public void depositDoesNotUpdateFunds() {
        atm.deposit(50, account);
        assertEquals(300, atm.getFunds(), PRECISION);
    }

    @Test
    public void depositIsRegistered() {
        atm.deposit(300, account);
        Transaction expected = Transaction.newCredit("Depósito MB", 300);
        assertTrue(TransactionTest.equalTransactions(
                expected, account.getLastTransaction()
        ));
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
        atm = new Atm(5);
        atm.withdraw(10, account);
    }

    @Test
    public void allowsWithrawingAllFunds() {
        atm = new Atm(100);
        atm.withdraw(100, account);
        assertEquals(0, atm.getFunds(), PRECISION);
    }

    @Test
    public void withdrawalIsRegistered() {
        atm.withdraw(200, account);
        Transaction expected = Transaction.newDebit("Levantamento MB", 200);
        assertTrue(TransactionTest.equalTransactions(
                expected, account.getLastTransaction()
        ));
    }

    @Test
    public void canPayWaterBill() {
        Transaction expected =
                Transaction.newDebit("Pagamento de serviços Água", 60.53);

        atm.payWaterBill(payment, account);

        assertEquals(expected, account.getLastTransaction());
        assertEquals(539.47, account.getBalance(), 1e-6);
    }

    @Test
    public void canPayElectricityBill() {
        Transaction expected =
            Transaction.newDebit("Pagamento de serviços Electricidade", 60.53);

        atm.payElectricityBill(payment, account);

        assertEquals(expected, account.getLastTransaction());
        assertEquals(539.47, account.getBalance(), 1e-6);
    }

    @Test
    public void canPayPhoneBill() {
        Transaction expected =
                Transaction.newDebit("Pagamento de serviços Telemóvel", 60.53);

        atm.payPhoneBill(payment, account);

        assertEquals(expected, account.getLastTransaction());
        assertEquals(539.47, account.getBalance(), 1e-6);
    }

    @Test(expected=IllegalArgumentException.class)
    public void cantPayWaterBillForLackOfFunds() {
        account.setBalance(50);
        atm.payWaterBill(payment, account);
    }

    @Test(expected=IllegalArgumentException.class)
    public void cantPayElectricityBillForLackOfFunds() {
        account.setBalance(50);
        atm.payElectricityBill(payment, account);
    }

    @Test(expected=IllegalArgumentException.class)
    public void cantPayPhoneBillForLackOfFunds() {
        account.setBalance(50);
        atm.payPhoneBill(payment, account);
    }

    @Test
    public void canGetCorrectPhoneEntities() {
        assertEquals("10158", atm.getPhoneEntity("918135235"));
        assertEquals("20638", atm.getPhoneEntity("932352352"));
        assertEquals("10559", atm.getPhoneEntity("965234235"));
    }

    @Test(expected=IllegalArgumentException.class)
    public void cantGetEntityFromUnknownPhoneNetwork() {
        atm.getPhoneEntity("123456789");
    }
}