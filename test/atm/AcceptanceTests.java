
package atm;

import atm.model.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AcceptanceTests {

    private AtmClient atm;
    private Account account;

    @Before
    public void setUp() {
        atm     = new AtmClient(1000);
        account = atm.getAccountWithPin("1234");
    }

    @Test
    public void canGetAccount() {
        assertEquals("0010029289641272009", account.getNumber());
        assertEquals("Rui Filipe Tavares Melo", account.getClient());
    }
}