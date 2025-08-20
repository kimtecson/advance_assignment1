package partB;

import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Seed items (no codes, no inventory class)
        Product muffin = new Product("Muffin", 4.50, 50);
        Product coffee = new Product("Coffee", 5.00, 50);
        Product shake  = new Product("Shake", 6.00, 50);

        // Fixed $1 off combo (via Discounts.COMBO_DISCOUNT)
        Combo coffeeMuffin = new Combo("Coffee + Muffin Combo", Map.of(coffee, 1, muffin, 1));
        Combo shakeMuffin = new Combo("Shake + Muffin Combo", Map.of(shake, 1, muffin, 1));

        // Menu catalog
        MenuItem[] catalog = { coffee, muffin, shake, coffeeMuffin, shakeMuffin };

        Scanner sc = new Scanner(System.in);
        boolean running = true;
        while (running) {
            System.out.println("\n=== Geek Cafe ===");
            System.out.println("a) Order");
            System.out.println("b) Bake muffins");
            System.out.println("c) Show sales report");
            System.out.println("d) Update prices");
            System.out.println("e) Exit");
            System.out.print("Choose: ");
            String choice = sc.nextLine().trim();
            switch (choice) {
            	case "a" -> handleOrder(sc, catalog);
            	case "b" -> {
            		try {
            			muffin.replenishMuffin(); // +25 muffins
            			System.out.printf("Baked 25 muffins. Stock is now %d.%n", muffin.getStock());
            		} catch (RuntimeException ex) {
            			System.out.println(ex.getMessage());
            		}
            	}
            	case "c" -> showSalesReport(coffee, muffin, shake);
            	case "d" -> updatePrices(sc, coffee, muffin, shake);
            	case "e" -> running = false;
            	default -> System.out.println("Invalid choice. Please select a–e.");
            }
        }
        System.out.println("Bye!");
        sc.close();
    }

    private static int readInt(Scanner sc) {
        try { return Integer.parseInt(sc.nextLine().trim()); }
        catch (Exception e) { return -1; }
    }

    private static void handleOrder(Scanner sc, MenuItem[] catalog) {
        Order order = new Order();

        // Build the order
        while (true) {
            printCatalog(catalog);
            System.out.print("Select item # (0 to finish): ");
            int pick = readInt(sc);
            if (pick == 0) break;
            if (pick < 1 || pick > catalog.length) {
                System.out.println("Invalid selection.");
                continue;
            }

            MenuItem item = catalog[pick - 1];
            System.out.print("Quantity: ");
            int qty = readInt(sc);
            if (qty <= 0) {
                System.out.println("Quantity must be > 0.");
                continue;
            }

            try {
                order.addItem(item, qty);
                System.out.printf("Added: %s x%d%n", item.getName(), qty);
            } catch (IllegalArgumentException ex) {
                System.out.println(ex.getMessage());
            }
        }

        if (order.isEmpty()) {
            System.out.println("Order is empty.");
            return;
        }

        // Show receipt and total
        printReceipt(order);
        double total = order.getTotal();

        // --- Payment (single-tender, reject if insufficient) ---
        while (true) {
            System.out.printf("Enter cash (TOTAL $%.2f): ", total);
            String s = sc.nextLine().trim();
            try {
                double paid = Double.parseDouble(s);
                if (paid < total) {
                    System.out.println("Insufficient payment. Payment rejected. Please try again.");
                    continue; // re-prompt for another transaction
                }
                Payment pmt = new Payment(paid);
                double change = pmt.change(total);
                order.finalizeOrder(); // commit stock
                System.out.printf("Change: $%.2f%n", change);
                System.out.println("Payment successful. Thank you!");
                break; // done
            } catch (NumberFormatException e) {
                System.out.println("Invalid amount. Please enter a number.");
            }
        }
    }

    
    private static void showSalesReport(Product... products) {
        System.out.println("\n--- Sales report ---");
        double total = 0.0;
        for (Product p : products) {
            double revenue = p.getQtySold() * p.getPrice();
            total += revenue;
            System.out.printf("%-12s  sold %3d  revenue $%.2f%n",
                    p.getName(), p.getQtySold(), revenue);
        }
        System.out.printf("TOTAL REVENUE: $%.2f%n%n", total);
    }

    private static void updatePrices(Scanner sc, Product... products) {
        System.out.println("\n--- Update prices ---");
        for (int i = 0; i < products.length; i++) {
            Product p = products[i];
            System.out.printf("%d) %-12s current $%.2f%n", (i + 1), p.getName(), p.getPrice());
        }
        System.out.print("Choose product #: ");
        int pick = readInt(sc);
        if (pick < 1 || pick > products.length) { System.out.println("Invalid selection."); return; }

        Product target = products[pick - 1];
        System.out.printf("Enter new price for %s: ", target.getName());
        try {
            double newPrice = Double.parseDouble(sc.nextLine().trim());
            target.updatePrice(newPrice);
            System.out.printf("%s price updated to $%.2f%n%n", target.getName(), target.getPrice());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void printCatalog(MenuItem[] catalog) {
        System.out.println("\n--- Menu ---");
        for (int i = 0; i < catalog.length; i++) {
            MenuItem mi = catalog[i];
            double list = mi.getUnitPrice();
            double disc = mi.getDiscount();
            double net  = mi.netUnitPrice();
            String badge = disc > 0 ? String.format(" (-$%.2f off)", disc) : "";
            String reg   = (net != list) ? String.format(" (reg $%.2f)", list) : "";
            System.out.printf("%d) %-26s  $%.2f%s%s%n", (i + 1), mi.getName(), net, badge, reg);
        }
        System.out.println("0) Done");
    }

    private static void printStock(Product... products) {
        System.out.println("\n--- Stock ---");
        for (Product p : products) {
            System.out.printf("%-12s  price $%.2f  stock %d  sold %d%n",
                    p.getName(), p.getPrice(), p.getStock(), p.getQtySold());
        }
    }

    private static void printReceipt(Order order) {
        System.out.println("\n=== RECEIPT " + order.getOrderId() + " ===");
        double subtotal = 0.0;
        double savings = 0.0;

        for (OrderLine line : order.getLines()) {
            MenuItem it = line.getItem();
            double lt = line.lineTotal();
            subtotal += lt;
            System.out.printf("%-28s x%-2d  $%.2f%n", it.getName(), line.getQuantity(), lt);

            if (it.getDiscount() > 0) {
                double s = it.getDiscount() * line.getQuantity();
                savings += s;
                System.out.printf("  -> Savings: -$%.2f%n", s);
            }
        }

        System.out.printf("TOTAL: $%.2f%n", subtotal);
        if (savings > 0) System.out.printf("You saved: $%.2f%n", savings);
        System.out.println("==========================");
    }
}
