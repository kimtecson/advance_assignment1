package partB;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Combo implements MenuItem {
    private final String name;
    private final Map<Product, Integer> components;

    public Combo(String name, Map<Product, Integer> components) {
        if (components == null || components.isEmpty())
            throw new IllegalArgumentException("Combo must have components");
        this.name = name;
        this.components = Collections.unmodifiableMap(new HashMap<>(components));
    }

    @Override public String getName() { return name; }

    @Override
    public double getUnitPrice() {
        double sum = 0.0;
        for (Map.Entry<Product,Integer> e : components.entrySet())
            sum += e.getKey().getPrice() * e.getValue();
        return sum;
    }
    @Override public double getDiscount() { return Discount.COMBO_DISCOUNT; }

    @Override
    public boolean isAvailable(int qty) {
        if (qty <= 0) return false;
        for (Map.Entry<Product,Integer> e : components.entrySet()) {
            Product p = e.getKey();
            int need = e.getValue() * qty;
            if (!p.isAvailable(need)) return false;
        }
        return true;
    }

    @Override
    public void deductStock(int qty) {
        if (!isAvailable(qty)) throw new IllegalArgumentException("Insufficient stock for combo");
        for (Map.Entry<Product,Integer> e : components.entrySet())
            e.getKey().deductStock(e.getValue() * qty);
    }

    @Override
    public void returnStock(int qty) {
        for (Map.Entry<Product,Integer> e : components.entrySet())
            e.getKey().returnStock(e.getValue() * qty);
    }

    public Map<Product,Integer> getComponents() { return components; }
    public double comboPrice() { return netUnitPrice(); }
}
