
package atm;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import atm.model.*;

@RunWith(Suite.class)
@Suite.SuiteClasses({ 
    AcceptanceTests.class,
    FileTest.class,
    TransactionTest.class,
    AccountPersistTest.class,
    AccountTest.class
})
public class TestSuite {}
