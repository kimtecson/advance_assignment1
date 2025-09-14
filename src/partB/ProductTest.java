package partB;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class ProductTest {
	private Product muffin;
	private Product coffee;

	    @Before
		public void setUp() {
			muffin = new Product("Muffin", 2.00, 25); // price, initial stock
			coffee = new Product("Coffee", 2.50);
		}


	@Test
    public void product() {
        assertEquals("Coffee", coffee.getName());
        assertEquals(Double.compare(2.5, coffee.getUnitPrice()),0);
        assertFalse(coffee.isStockManaged());
        assertTrue(coffee.isAvailable(10_000));
        assertThrows(IllegalArgumentException.class, () -> new Product("Muffin", 0.00, -1));
        assertThrows(IllegalArgumentException.class, () -> new Product("Muffin", 2.00, -1));
        assertThrows(IllegalArgumentException.class, () -> new Product("Coffee", -2.00));
        assertThrows(IllegalArgumentException.class, () -> new Product("Coffee", 0.00));
    }

    @Test
    public void muffin() {
        assertTrue(muffin.isStockManaged());
        assertTrue(muffin.isAvailable(25));
        assertFalse(muffin.isAvailable(26));
        assertEquals(25, muffin.getStock());
    }
    
    @Test
    public void updatePrice_rules() {
        assertThrows(IllegalArgumentException.class, () -> muffin.updatePrice(2.00)); // same
        assertThrows(IllegalArgumentException.class, () -> muffin.updatePrice(0.0));  // zero
        assertThrows(IllegalArgumentException.class, () -> muffin.updatePrice(-1.0)); // negative

        muffin.updatePrice(2.50);
        assertEquals(Double.compare(2.50, muffin.getPrice()), 0);
    }
    
    @Test
    public void replenishMuffin() {
        muffin.replenishMuffin(); // +25
        assertEquals(50, muffin.getStock());
        assertThrows(UnsupportedOperationException.class, () -> coffee.replenishMuffin());
    }
    
    @Test
    public void deduct_and_returnStock_stockManaged() {
        muffin.deductStock(3);
        assertEquals(22, muffin.getStock());
        assertEquals(3, muffin.getQtySold());
        assertEquals(Double.compare(6.00, muffin.getSales()), 0);

        // put back
        muffin.returnStock(2);
        assertEquals(24, muffin.getStock());
        assertEquals(1, muffin.getQtySold());
        assertEquals(Double.compare(2.00, muffin.getSales()), 0);
        
        //invalid deductions and returns
        assertThrows(IllegalArgumentException.class, () -> muffin.deductStock(0));
        assertThrows(IllegalArgumentException.class, () -> muffin.deductStock(-1));
        assertThrows(IllegalArgumentException.class, () -> muffin.returnStock(0));
        assertThrows(IllegalArgumentException.class, () -> muffin.returnStock(-1));
        assertThrows(IllegalArgumentException.class, () -> muffin.deductStock(50));
        
        muffin.deductStock(3);      // stock: 7, sold: 3, sales: 6.00
        muffin.returnStock(5);      // over-return by 2 → clamp qtySold & sales to 0

    }
    
    @Test
    public void deduct_and_returnStock_coffee() {
        coffee.deductStock(4);  // no physical stock, but sales tracked
        assertEquals(0, coffee.getStock());
        assertEquals(4, coffee.getQtySold());
        assertEquals(Double.compare(10.00, coffee.getSales()), 0);

        coffee.returnStock(3); // undo counters only
        assertEquals(0, coffee.getStock());
        assertEquals(1, coffee.getQtySold());
        assertEquals(Double.compare(2.50, coffee.getSales()), 0);
        
    
    }

    @Test
    public void isAvailable() {
        assertFalse(muffin.isAvailable(0));   // qty must be > 0 by contract
        assertFalse(muffin.isAvailable(-1));
        assertTrue(muffin.isAvailable(25));
        assertFalse(muffin.isAvailable(26));
    }

}
