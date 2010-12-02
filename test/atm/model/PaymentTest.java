
package atm.model;

import org.junit.Test;
import static org.junit.Assert.*;

public class PaymentTest {

    @Test(expected=IllegalArgumentException.class)
    public void cantUseNonDigitsReference() {
        new Payment("12345", "1234a4567", 20);
    }

    @Test(expected=IllegalArgumentException.class)
    public void cantUseReferenceWithLessThenNineDigits() {
        new Payment("12345", "12345", 20);
    }

    @Test(expected=IllegalArgumentException.class)
    public void cantUseReferenceWithMoreThenNineDigits() {
        new Payment("12345", "1234567891", 20);
    }

    @Test(expected=IllegalArgumentException.class)
    public void cantUseNonDigitEntity() {
        new Payment("12a45", "123456789", 20);
    }

    @Test(expected=IllegalArgumentException.class)
    public void cantUseEntityWithLessThenFiveDigits() {
        new Payment("123", "123456789", 20);
    }

    @Test(expected=IllegalArgumentException.class)
    public void cantUseEntityWithMoreThenFiveDigits() {
        new Payment("123456", "123456789", 20);
    }

    @Test
    public void paymentWithNegativeAmmountShouldBeSetToPositive() {
        Payment payment = new Payment("12345", "123456789", -20);
        assertEquals(20.0, payment.getAmmount(), 1e-6);
    }
}