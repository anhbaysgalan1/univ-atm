
package atm.model;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author heldercorreia
 */
public class TransactionTest {

    private final Transaction credit =
            Transaction.newCredit("Test credit", 200);

    private final Transaction debit =
            Transaction.newDebit("Test debit", 200);

    public TransactionTest() {}

    @BeforeClass
    public static void setUpClass() throws Exception {}

    @AfterClass
    public static void tearDownClass() throws Exception {}

    @Test
    public void canInstanciateNewCredit() {
        assertEquals(Transaction.Type.CREDIT, credit.getType());
    }

    @Test
    public void canInstanciateNewDebit() {
        assertEquals(Transaction.Type.DEBIT, debit.getType());
    }

}