
package atm;

import atm.model.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AcceptanceTests {

    private Atm atm;
    private Account account;

    @Before
    public void setUp() {
        atm = new Atm(1000);
    }

    @Test
    public void canGetAccount() {
        account = atm.getAccountWithPin("1234");
        assertEquals("0010029289641272009", account.getNumber());
        assertEquals("Rui Filipe Tavares Melo", account.getClient());
    }
}