
package atm.model;

import java.io.IOException;
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

    private final double precision = 1e-5;
    private final String testAccountNum = "0010029289641272009";
    private final String testClientName = "Rui Filipe Tavares Melo";
    private final File testFileTemplate = new File("testClienteTemplate.dat");
    private File testFile = new File("testCliente.dat");
    private AccountPersist dataSource;
    private Account account;

    public AccountTest() {}

    @BeforeClass
    public static void setUpClass() throws Exception {}

    @AfterClass
    public static void tearDownClass() throws Exception {}

    @Before
    public void setUp() throws java.io.IOException {
        testFile.copy(testFileTemplate);
        dataSource = new AccountPersist(testFile);
        account = new Account(testAccountNum, testClientName, dataSource);
    }

    @After
    public void tearDown() {
        account = null;
    }

    @Test
    public void accountLoadsBalance() throws Exception {
        assertEquals(600.0, account.getBalance(), precision);
    }

    @Test
    public void canMakeDeposit() throws IOException {
        account.setBalance(600.0);
        account.deposit(200.0);
        assertEquals(800.0, account.getBalance(), precision);
    }

    @Test
    public void negativeValuesDepositAPositiveAmmount() throws IOException {
        account.setBalance(100.0);
        account.deposit(-50.0);
        assertEquals(150.0, account.getBalance(), precision);
    }

    @Test
    public void canMakeWithdrawal() throws IOException {
        account.setBalance(600.0);
        account.withdraw(50.0);
        assertEquals(550.0, account.getBalance(), precision);
    }

    @Test
    public void negativeValuesWithdrawAPositiveAmmount() throws IOException {
        account.setBalance(100.0);
        account.withdraw(-50.0);
        assertEquals(50.0, account.getBalance(), precision);
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
    public void allowsWithrawingAllFunds() throws IOException {
        account.setBalance(600.0);
        account.withdraw(600.0);
        assertEquals(0.0, account.getBalance(), precision);
    }

}