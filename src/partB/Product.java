package partB;

/**
 * A single physical product that can have stocks or no stocks
 * Notes:
 *  updatePrice() to support price changes.
 *  replenishMuffin() is a fixed +25 restock and only valid for the Muffin product.
 */
public class Product implements MenuItem {
    private final String name;
    private double price;
    private final boolean stockManaged; // true for Muffin; false for Coffee/Shake
    private int stock;                  // ignored when !stockManaged
    private int qtySold;
    private double sales;


    // Constructor for products that are always available
    public Product(String name, double price) {
        if (price <= 0) throw new IllegalArgumentException("price must be > 0");
        this.name = name;
        this.price = price;
        this.stockManaged = false;
    }

 // Constructor for products that needs stocks
    public Product(String name, double price, int initialStock) {
        if (price <= 0) throw new IllegalArgumentException("price must be > 0");
        if (initialStock < 0) throw new IllegalArgumentException("initial stock must be >= 0");
        this.name = name;
        this.price = price;
        this.stockManaged = true;
        this.stock = initialStock;
    }

    @Override public String getName() { return name; }
    @Override public double getUnitPrice() { return price; }
    
    //getters
    public double getPrice() { return price; }
    public int getStock() { return stock; }
    public int getQtySold() { return qtySold; }
    public double getSales() { return sales; }
    public boolean isStockManaged() { return stockManaged; }

    // change product price (must be > 0 and different from current)
    public void updatePrice(double newPrice) {
        if (newPrice <= 0) throw new IllegalArgumentException("new price must be > 0");
        double epsilon = 1e-9;
        if (Math.abs(newPrice - this.price) < epsilon)
            throw new IllegalArgumentException("new price must be different from current price");
        this.price = newPrice;
    }

    @Override
    public boolean isAvailable(int qty) {
        if (qty <= 0) return false;
        return !stockManaged || qty <= stock;
    }

    @Override
    public void deductStock(int qty) {
        if (qty <= 0) throw new IllegalArgumentException("qty must be > 0");
        if (stockManaged) {
            if (qty > stock) throw new IllegalArgumentException("invalid qty to deduct");
            stock -= qty;
        }
        qtySold += qty;
        sales += price * qty;
    }
    
    @Override
    public void returnStock(int qty) {
        if (qty <= 0) throw new IllegalArgumentException("qty must be > 0");
        if (stockManaged) stock += qty;     // put physical stock back (for Muffin)
        qtySold -= qty;                     // undo sales counters
        sales   -= price * qty;
        if (qtySold < 0) qtySold = 0;
        if (sales   < 0) sales   = 0.0;
    }

    
    // Fixed restock: only for the Muffin product, adds 25 units.
    public void replenishMuffin() {
        if (!stockManaged || !"Muffin".equalsIgnoreCase(name)) {
            throw new UnsupportedOperationException("replenishMuffin is only for the Muffin product");
        }
        stock += 25;
    }
}
