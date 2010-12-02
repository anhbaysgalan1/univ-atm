
package atm.model;

import org.junit.Test;
import static org.junit.Assert.*;

public class TransactionTest {

    private final Transaction credit =
            Transaction.newCredit("Test credit", 200);

    private final Transaction debit =
            Transaction.newDebit("Test debit", 200);

    /** Verifica se dois movimentos são iguais, ignorando a data */
    public static boolean equalTransactions(
                        Transaction expected, Transaction actual) {
        return actual.getDescription().equals(expected.getDescription())
            && (Double.compare(expected.getAmmount(), actual.getAmmount()) == 0)
            && (expected.getType() == actual.getType());
    }

    @Test
    public void canInstanciateNewCredit() {
        assertEquals(Transaction.Type.CREDIT, credit.getType());
    }

    @Test
    public void canInstanciateNewDebit() {
        assertEquals(Transaction.Type.DEBIT, debit.getType());
    }

}