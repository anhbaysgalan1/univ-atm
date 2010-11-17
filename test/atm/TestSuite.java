
package atm;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import atm.model.*;

/**
 *
 * @author heldercorreia
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ 
    AcceptanceTests.class,
    FileTest.class,
    AccountTest.class
})
public class TestSuite {

    @BeforeClass
    public static void setUpClass() throws Exception {}

    @AfterClass
    public static void tearDownClass() throws Exception {}

}
