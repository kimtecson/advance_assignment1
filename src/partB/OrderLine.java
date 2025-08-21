package partB;

/** One entry in an order (single menu item + quantity).
 * Multiple item orders are just multiple lines for simple pricing and stock updates.
 * Pricing uses the item's discount-aware totalPrice(). */

public class OrderLine {
    private final MenuItem item;
    private final int quantity;

    public OrderLine(MenuItem item, int quantity) {
        if (item == null) throw new IllegalArgumentException("item cannot be null");
        if (quantity <= 0) throw new IllegalArgumentException("quantity must be > 0");
        this.item = item;
        this.quantity = quantity;
    }

    public MenuItem getItem() { return item; }
    public int getQuantity() { return quantity; }

    /** Uses discount-aware pricing automatically. */
    public double lineTotal() { return item.totalPrice(quantity); }

    public String displayName() { return item.getName(); }
}
