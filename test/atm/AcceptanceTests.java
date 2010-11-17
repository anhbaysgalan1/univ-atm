
package atm;

import atm.model.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author heldercorreia
 */
public class AcceptanceTests {

    public AcceptanceTests() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void canGetAccount() throws Exception {
        AccountBroker accounts = new AccountBroker();
        Account account = accounts.getAccountWithPin("1234");
        assertEquals("0010029289641272009", account.getNumber());
        assertEquals("Rui Filipe Tavares Melo", account.getClient());
    }
}