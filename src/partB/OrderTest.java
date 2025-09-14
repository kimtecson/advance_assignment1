package partB;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class OrderTest {
	
	private Product muffin;
	private Product coffee;
	private Combo combo1;

	    @Before
		public void setUp() {
			muffin = new Product("Muffin", 2.00, 25); // price, initial stock
			coffee = new Product("Coffee", 2.50);
			combo1 = new Combo("Coffee + Muffin", Map.of(coffee, 1, muffin, 1));
		}
	

	@Test
    public void addItem_deductsImmediately_andTotalsUseDiscount() {
       

        Order order = new Order();
        order.addItem(combo1, 4); // needs 4 muffins

        // stock was deducted on add
        assertEquals(21, muffin.getStock());

        // subtotal/total uses discount-aware pricing
        double expectedLine = (combo1.getUnitPrice() - Discount.COMBO_DISCOUNT) * 4; // 6.00 * 4
        assertEquals(Double.compare(expectedLine, order.getSubtotal()), 0);
        assertEquals(Double.compare(expectedLine, order.getTotal()), 0);
        assertEquals(21, muffin.getStock());
    }

    @Test
    public void addItem_insufficientStock() {
    	
        Order order = new Order();
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> order.addItem(combo1, 26));
        assertEquals(25, muffin.getStock()); // unchanged
        assertTrue(order.getLines().isEmpty());
    }

    @Test
    public void cancel_returnsAllStock_andClearsLines() {

        Order order = new Order();
        order.addItem(combo1, 3); // stock now 2
        assertEquals(22, muffin.getStock());

        order.cancel(); // put everything back
        assertEquals(25, muffin.getStock());
        assertTrue(order.getLines().isEmpty());
    }

}
