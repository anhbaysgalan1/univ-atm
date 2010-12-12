
package atm.model;

import java.util.List;
import java.util.ArrayList;
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

    @Test
    public void canGetTenLastestTransactions() {
        List<Transaction> source = generateTransactions(15);
        List<Transaction> actual = account.getLatestTransactions(10);
        assertLatestTransactions(10, source, actual);
    }

    @Test
    public void canGetLastestTransactionsWhenLessThenTenAvailable() {
        List<Transaction> source = generateTransactions(5);
        List<Transaction> actual = account.getLatestTransactions(10);
        assertLatestTransactions(5, source, actual);
    }

    private void assertLatestTransactions(int expected,
                                                List<Transaction> source,
                                                List<Transaction> actual) {
        assertEquals(expected, actual.size());
        for (int i = 0; i < expected; i++) {
            assertEquals(
                source.get(source.size()-1-i).getAmount(),
                actual.get(i).getAmount(),
                1e-6
            );
        }
    }

    private List<Transaction> generateTransactions(int num) {
        Transaction transaction;
        List<Transaction> transactions = new ArrayList<Transaction>(num);
        for (int i = 1; i <= num; i++) {
            transaction = Transaction.newCredit("Crédito "+i, 100+2*i);
            transactions.add(transaction);
            account.addTransaction(transaction);
        }
        return transactions;
    }
}