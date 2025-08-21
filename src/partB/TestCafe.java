package partB;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TestCafe {

	 private Product muffin;
	 private Product coffee;
	 private Product shake;

	    @Before
	    void setUp() {
	        muffin = new Product("Muffin", 2.00, 25);   // price, initial stock
	        coffee = new Product("Coffe", 2.50);
	        shake = new Product("Shake", 3.00);
	    }

	    @Test
	    void testOrderMuffinSuccess() {
	        boolean result = muffin.order(5); // should reduce stock
	        assertTrue(result);
	        assertEquals(20, muffin.getStock());
	        assertEquals(10.00, muffin.getSales(), 0.001);
	    }

	    @Test
	    void testOrderMuffinInsufficientStock() {
	        boolean result = muffin.order(30); // too many
	        assertFalse(result);
	        assertEquals(25, muffin.getStock()); // unchanged
	    }

	    @Test
	    void testBakeMuffins() {
	        muffin.replenishMuffin(); // +25
	        assertEquals(50, muffin.getStock());
	    }

	    @Test
	    void testRejectInsufficientPayment() {
	        double total = muffin.getPrice() * 2; // $4.00
	        double payment = 3.00; // insufficient
	        assertThrows(IllegalArgumentException.class, () -> {
	            muffin.processPayment(payment, total);
	        });
	    }

	    @Test
	    void testSuccessfulPaymentAndChange() {
	        double total = muffin.getPrice() * 2; // $4.00
	        double payment = 5.00;
	        double change = muffin.processPayment(payment, total);
	        assertEquals(1.00, change, 0.001);
	    }

	    @Test
	    void testUpdatePriceSuccess() {
	        muffin.updatePrice(2.50);
	        assertEquals(2.50, muffin.getPrice());
	    }

	    @Test
	    void testUpdatePriceSameValueThrows() {
	        assertThrows(IllegalArgumentException.class, () -> {
	            muffin.updatePrice(2.00); // same as current
	        });
	    }

	    @Test
	    void testComboDeal() {
	        Combo combo = new Combo(new Muffin(2.00, 10), new Coffee(2.50));
	        assertEquals(3.50, combo.getPrice(), 0.001); // 4.50 - 1.00 discount
	    }

}
