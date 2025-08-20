package partB;

public class Product implements MenuItem {

    private final String name;
    private double price;
    private int stock;
    private int qtySold;


    public Product(String name, double price, int initialStock) {
        if (initialStock < 0) throw new IllegalArgumentException("Initial stock must be >= 0");
        this.name = name;
        this.price = price;
        this.stock = initialStock;
    }
    
    @Override public String getName() { return name; }
    /** Products have no fixed discount; use list price as unit price. */
    @Override public double getUnitPrice() { return price; }

    public double getPrice() { return price; }
    public int getStock() { return stock; }
    public int getQtySold() { return qtySold; }
    
    /** change product price */
    public void updatePrice(double newPrice) {
        if (newPrice <= 0) {
            throw new IllegalArgumentException("new price must be > 0");
        }
        double epsilon = 1e-9; // handle tiny float differences
        if (Math.abs(newPrice - this.price) < epsilon) {
            throw new IllegalArgumentException("new price must be different from current price");
        }
        this.price = newPrice;
    }

    @Override
    public boolean isAvailable(int qty) {
        return qty >= 0 && qty <= stock;
    }

    @Override
    public void deductStock(int qty) {
        if (!isAvailable(qty)) throw new IllegalArgumentException("Invalid qty to deduct");
        stock -= qty;
        qtySold += qty;
    }

    /** Restock method restricted to the Muffin product by name. */
    public void replenishMuffin() {
        if (!"Muffin".equalsIgnoreCase(name)) {
            throw new UnsupportedOperationException("replenishMuffin is only for the Muffin product");
        }
        stock += 25;
    }
}
