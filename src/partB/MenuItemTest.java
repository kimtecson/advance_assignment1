package partB;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;

public class MenuItemTest {

	@Test
    public void totalPrice_usesDiscountForCombo() {
        Product muffin = new Product("Muffin", 2.00, 10);
        Product coffee = new Product("Coffee", 5.00);
        Combo combo = new Combo("Coffee + Muffin", Map.of(coffee, 1, muffin, 1));
        assertEquals(Double.compare(7, combo.getUnitPrice()),0);
        assertEquals(Double.compare(1, combo.getDiscount()),0);
        assertEquals(Double.compare(12, combo.totalPrice(2)),0);
    }

    @Test
    public void totalPrice_forProduct_hasNoDiscount() {
    	Product muffin = new Product("Muffin", 2.00, 10);
        MenuItem item = muffin; // upcast to use interface defaults

        // No discount for plain Product
        assertEquals(Double.compare(0, item.getDiscount()),0);
        assertEquals(Double.compare(item.getUnitPrice(), item.netUnitPrice()),0);

        // totalPrice uses unit price when discount is zero
        assertEquals(Double.compare(6, item.totalPrice(3)),0);

        assertEquals(Double.compare(0, item.totalPrice(0)),0);
        assertThrows(IllegalArgumentException.class, () -> item.totalPrice(-1));
    }

}
