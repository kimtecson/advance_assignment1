package partB;

import java.util.Map;

public class Demo {
    public static void main(String[] args) {
        Product muffin = new Product("Muffin", 5.00, 5);
        Product coffee = new Product("Coffee", 5.00, 5);

        // restock only muffins
        muffin.replenishMuffin(); // muffin stock -> 15

        Combo combo = new Combo("Coffee + Muffin", Map.of(coffee, 1, muffin, 1));
        System.out.printf("Per-combo regular: $%.2f | discount: $%.2f | final: $%.2f%n",
                combo.getUnitPrice(), combo.getDiscount(), combo.comboPrice());

        Order order = new Order();
        order.addItem(combo, 2); // needs 2 coffee + 2 muffin
        System.out.printf("Order %s total: $%.2f%n", order.getOrderId(), order.getTotal());

        order.finalizeOrder();
        System.out.printf("Stock left — Muffin: %d, Coffee: %d%n",
                muffin.getStock(), coffee.getStock());
    }
}