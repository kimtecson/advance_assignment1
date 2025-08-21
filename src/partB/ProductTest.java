package partB;

import static org.junit.Assert.*;

import org.junit.Test;

public class ProductTest {

	@Test
    public void product() {
        Product coffee = new Product("Coffee", 5.00);
        assertEquals("Coffee", coffee.getName());
        assertEquals(Double.compare(5, coffee.getUnitPrice()),0);
        assertFalse(coffee.isStockManaged());
        assertTrue(coffee.isAvailable(10_000));
        assertThrows(IllegalArgumentException.class, () -> new Product("Muffin", 0.00, -1));
        assertThrows(IllegalArgumentException.class, () -> new Product("Muffin", 2.00, -1));
        assertThrows(IllegalArgumentException.class, () -> new Product("Coffee", -2.00));
        assertThrows(IllegalArgumentException.class, () -> new Product("Coffee", 0.00));
    }

    @Test
    public void muffin() {
        Product muffin = new Product("Muffin", 2.00, 25);
        assertTrue(muffin.isStockManaged());
        assertTrue(muffin.isAvailable(25));
        assertFalse(muffin.isAvailable(26));
        assertEquals(25, muffin.getStock());
    }
    
    @Test
    public void updatePrice_rules() {
        Product muffin = new Product("Muffin", 2.00, 25);

        assertThrows(IllegalArgumentException.class, () -> muffin.updatePrice(2.00)); // same
        assertThrows(IllegalArgumentException.class, () -> muffin.updatePrice(0.0));  // zero
        assertThrows(IllegalArgumentException.class, () -> muffin.updatePrice(-1.0)); // negative

        muffin.updatePrice(2.50);
        assertEquals(2.50, muffin.getPrice(), 1e-9);
    }
    
    @Test
    public void replenishMuffin() {
        Product muffin = new Product("Muffin", 2.00, 25);
        muffin.replenishMuffin(); // +25
        assertEquals(50, muffin.getStock());

        Product coffee = new Product("Coffee", 5.00);
        
        assertThrows(UnsupportedOperationException.class, () -> coffee.replenishMuffin());
    }
    
    @Test
    public void deduct_and_returnStock_stockManaged() {
        Product muffin = new Product("Muffin", 2.00, 10);

        muffin.deductStock(3);
        assertEquals(7, muffin.getStock());
        assertEquals(3, muffin.getQtySold());
        assertEquals(Double.compare(6.00, muffin.getSales()), 0);

        // put back
        muffin.returnStock(2);
        assertEquals(9, muffin.getStock());
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
        Product coffee = new Product("Coffee", 5.00);
        coffee.deductStock(4);  // no physical stock, but sales tracked
        assertEquals(0, coffee.getStock());
        assertEquals(4, coffee.getQtySold());
        assertEquals(Double.compare(20.00, coffee.getSales()), 0);

        coffee.returnStock(3); // undo counters only
        assertEquals(0, coffee.getStock());
        assertEquals(1, coffee.getQtySold());
        assertEquals(Double.compare(5.00, coffee.getSales()), 0);
        
    
    }

    @Test
    public void isAvailable() {
        Product muffin = new Product("Muffin", 2.00, 5);
        assertFalse(muffin.isAvailable(0));   // qty must be > 0 by contract
        assertFalse(muffin.isAvailable(-1));
        assertTrue(muffin.isAvailable(5));
        assertFalse(muffin.isAvailable(6));
    }

}
