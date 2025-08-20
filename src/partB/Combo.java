package partB;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Combo implements MenuItem {
    private final String name;
    /** Per-combo component requirements: Product object -> quantity needed per 1 combo. */
    private final Map<Product, Integer> components;

    /**
     * @param name display name (e.g., "Coffee + Muffin Combo")
     * @param components map of products and their required quantities per combo
     */
    public Combo(String name, Map<Product, Integer> components) {
        if (components == null || components.isEmpty())
            throw new IllegalArgumentException("Combo must have components");
        this.name = name;
        this.components = Collections.unmodifiableMap(new HashMap<>(components));
    }

    @Override public String getName() { return name; }

    /** Regular price = sum of component list prices (before discount). */
    @Override
    public double getUnitPrice() {
        double sum = 0.0;
        for (Map.Entry<Product, Integer> e : components.entrySet()) {
            sum += e.getKey().getPrice() * e.getValue();
        }
        return sum;
    }

    /** Fixed discount from central policy. */
    @Override
    public double getDiscount() { return Discount.COMBO_DISCOUNT; }

    /** Availability = every component has enough stock for qty combos. */
    @Override
    public boolean isAvailable(int qty) {
        if (qty <= 0) return false;
        for (Map.Entry<Product, Integer> e : components.entrySet()) {
            Product p = e.getKey();
            int need = e.getValue() * qty;
            if (!p.isAvailable(need)) return false;
        }
        return true;
    }

    /** Deduct each component’s stock according to qty combos. */
    @Override
    public void deductStock(int qty) {
        if (!isAvailable(qty)) throw new IllegalArgumentException("Insufficient stock for combo");
        for (Map.Entry<Product, Integer> e : components.entrySet()) {
            e.getKey().deductStock(e.getValue() * qty);
        }
    }

    /** Optional helpers */
    public double comboPrice() { return netUnitPrice(); } // after $1 off
    public double savingsPerCombo() { return getDiscount(); }

    /** Optional: expose component map (read-only) if you need it for UI/receipts. */
    public Map<Product, Integer> getComponents() { return components; }
}
