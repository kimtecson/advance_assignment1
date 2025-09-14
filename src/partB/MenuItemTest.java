package partB;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class MenuItemTest {
	
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
    public void totalPrice_usesDiscountForCombo() {

        assertEquals(Double.compare(4.5, combo1.getUnitPrice()),0);
        assertEquals(Double.compare(1, combo1.getDiscount()),0);
        assertEquals(Double.compare(7, combo1.totalPrice(2)),0);
    }

    @Test
    public void totalPrice_forProduct_hasNoDiscount() {

        MenuItem item = muffin;

        // No discount for plain Product
        assertEquals(Double.compare(0, item.getDiscount()),0);
        assertEquals(Double.compare(item.getUnitPrice(), item.netUnitPrice()),0);

        // totalPrice uses unit price when discount is zero
        assertEquals(Double.compare(6, item.totalPrice(3)),0);

        assertEquals(Double.compare(0, item.totalPrice(0)),0);
        assertThrows(IllegalArgumentException.class, () -> item.totalPrice(-1));
    }

}
