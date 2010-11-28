
package atm.model;

import org.junit.Test;
import static org.junit.Assert.*;

public class TransactionTest {

    private final Transaction credit =
            Transaction.newCredit("Test credit", 200);

    private final Transaction debit =
            Transaction.newDebit("Test debit", 200);

    @Test
    public void canInstanciateNewCredit() {
        assertEquals(Transaction.Type.CREDIT, credit.getType());
    }

    @Test
    public void canInstanciateNewDebit() {
        assertEquals(Transaction.Type.DEBIT, debit.getType());
    }

}