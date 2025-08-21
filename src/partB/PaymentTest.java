package partB;

import static org.junit.Assert.*;

import org.junit.Test;

public class PaymentTest {

	@Test
    public void covers_and_change() {
        Payment p = new Payment(10.00);
        assertTrue(p.covers(6.50));
        assertEquals(Double.compare(3.50, p.change(6.50)), 0);
     // zero is allowed
        Payment zero = new Payment(0.0);
        assertEquals(Double.compare(0.0, zero.getAmountTendered()), 0);
    }

    @Test
    public void change_throws_whenInsufficient() {
        Payment p = new Payment(4.00);
        assertThrows(IllegalStateException.class, () -> p.change(5.00));
    }

    @Test
    public void rejects_negative_payment() {
        assertThrows(IllegalArgumentException.class, () -> new Payment(-1.0));
    }

}
