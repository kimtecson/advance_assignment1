package partB;

/**
 * Contract for anything that can appear on the menu (products, combos, etc.).
 * It provides list price, discount policy, and stock semantics.
 *
 * Design:
 *  Default discount is 0 (plain products). Combos override getDiscount().
 *  Pricing helpers (netUnitPrice/totalPrice) keep receipts consistent.
 */
public interface MenuItem {
   
	String getName();
    double getUnitPrice(); // List price per unit (before any discount)

    // Fixed discount per unit; defaults to 0 for normal products.
    default double getDiscount() { return 0.0; }
    
    // Net unit price after discount (floored at $0)
    default double netUnitPrice() {
        double net = getUnitPrice() - getDiscount();
        return net < 0 ? 0 : net;
    }

    // Line total for a given quantity at net price.
    default double totalPrice(int qty) {
        if (qty < 0) throw new IllegalArgumentException("qty must be >= 0");
        return netUnitPrice() * qty;
    }

    boolean isAvailable(int qty);
    void deductStock(int qty);
    void returnStock(int qty); //when payment is cancelled
}
