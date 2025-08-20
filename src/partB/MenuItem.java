package partB;

public interface MenuItem {
    String getName();

    /** List price per unit (before any discount). */
    double getUnitPrice();

    /** Fixed discount per unit; defaults to 0 for normal products. */
    default double getDiscount() { return 0.0; }

    /** Net/unit price after discount (floored at $0). */
    default double netUnitPrice() {
        double net = getUnitPrice() - getDiscount();
        return net < 0 ? 0 : net;
    }

    /** Total price for a quantity at net price. */
    default double totalPrice(int qty) {
        if (qty < 0) throw new IllegalArgumentException("qty must be >= 0");
        return netUnitPrice() * qty;
    }

    /** Stock check for the requested quantity. */
    boolean isAvailable(int qty);

    /** Reserve/commit stock AFTER successful payment. */
    void deductStock(int qty);
}
