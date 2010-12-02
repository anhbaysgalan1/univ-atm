
package atm;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import atm.model.*;

@RunWith(Suite.class)
@Suite.SuiteClasses({ 
    AcceptanceTests.class,
    AtmTest.class,
    TransactionTest.class,
    AccountMapperTest.class,
    AccountTest.class
})
public class TestSuite {}
