package partB;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Order {
    private static int NEXT = 1;
    private static String nextOrderId() { return "ORD" + (NEXT++); }

    private final String orderId = nextOrderId();
    private final List<OrderLine> lines = new ArrayList<>();

    public String getOrderId() { return orderId; }

    public void addItem(MenuItem item, int qty) {
        if (!item.isAvailable(qty))
            throw new IllegalArgumentException("Insufficient stock for " + item.getName());
        lines.add(new OrderLine(item, qty));
    }

    public List<OrderLine> getLines() { return Collections.unmodifiableList(lines); }

    public double getSubtotal() {
        double sum = 0.0;
        for (OrderLine l : lines) sum += l.lineTotal();
        return sum;
    }

    /** Total equals subtotal since we removed tax. */
    public double getTotal() { return getSubtotal(); }

    /** Commit inventory AFTER successful payment. */
    public void finalizeOrder() {
        for (OrderLine l : lines) l.getItem().deductStock(l.getQuantity());
    }

    public boolean isEmpty() { return lines.isEmpty(); }
}
