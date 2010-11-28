
package atm;

import atm.model.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class AcceptanceTests {

    @Test
    public void canGetAccount() {
        AccountBroker accounts = new AccountBroker();
        Account account = accounts.getAccountWithPin("1234");
        assertEquals("0010029289641272009", account.getNumber());
        assertEquals("Rui Filipe Tavares Melo", account.getClient());
    }
}