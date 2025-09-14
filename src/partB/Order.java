package partB;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A customer's order: a list of OrderLines and helpers for totals.
 * Simplified order process. Order quantites are deducted when the user add an item and will be returned back when the payment was not finalized.
 */
public class Order {
    private static int NEXT = 1;
    private static String nextOrderId() { return "ORD" + (NEXT++); }

    private final String orderId = nextOrderId();
    private final List<OrderLine> lines = new ArrayList<>();

    public String getOrderId() { return orderId; }

    // Deduct stock immediately when the line is added.
    public void addItem(MenuItem item, int qty) {
        if (qty <= 0) throw new IllegalArgumentException("Quantity must be > 0");
        if (!item.isAvailable(qty))
            throw new IllegalArgumentException("Sorry! Not enough " + item.getName() + " left. Please bake more.");
        item.deductStock(qty);
        lines.add(new OrderLine(item, qty));
    }

    public List<OrderLine> getLines() { return Collections.unmodifiableList(lines); }

    public double getSubtotal() {
        double sum = 0.0;
        for (OrderLine l : lines) sum += l.lineTotal();
        return sum;
    }

    public double getTotal() { return getSubtotal(); }

    // Cancel/failed payment: return all stock and clear the cart. */
    public void cancel() {
        for (OrderLine l : lines) l.getItem().returnStock(l.getQuantity());
        lines.clear();
    }

    public boolean isEmpty() { return lines.isEmpty(); }
    
    
}
