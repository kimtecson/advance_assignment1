package partA;

import java.util.Scanner;

public class Menu {
    // Prices
    static double muffinPrice = 2.00;
    static double shakePrice = 3.00;
    static double coffeePrice = 2.50;

    // track stocks level and sales
    static int muffinStock = 25;
    static int muffinQtySold = 0;
    static int shakeQtySold = 0;
    static int coffeeQtySold = 0;
    static double muffinSales = 0.0;
    static double shakeSales = 0.0;
    static double coffeeSales = 0.0;
    
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean running = true;
        
        while (running) {
            System.out.println("===============================================================");
            System.out.println("The Geek Cafe");
            System.out.println("===============================================================");
            System.out.println("a) Order");
            System.out.println("b) Bake muffins");
            System.out.println("c) Show sales report");
            System.out.println("d) Update prices");
            System.out.println("e) Exit");
            System.out.print("Please select: ");
            String choice = scanner.nextLine().trim().toLowerCase();
            
            switch(choice) {
                case "a":
                    order();
                    break;
                case "b":
                    bakeMuffins();
                    break;
                case "c":
                    showSalesReport();
                    break;
                case "d":
                    updatePrices();
                    break;
                case "e":
                    exitCafe();
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }
    
    private static void order() {
        double totalSales = 0.0;
        int muffinOrder = 0, shakeOrder = 0, coffeeOrder = 0;
        boolean ordering = true;
        boolean firstSelection = true;
       
        while (ordering) {
            System.out.println("> Select the food item");
            System.out.println("1. Muffin");
            System.out.println("2. Shake");
            System.out.println("3. Coffee");
            if (!firstSelection) {
                System.out.println("4. No more");
            }
            System.out.print("Please select: ");
            int item = Integer.parseInt(scanner.nextLine());

            switch (item) {
                case 1:
                    System.out.print("How many muffins would you like to buy: ");
                    int mQty = Integer.parseInt(scanner.nextLine());
                    if (mQty > muffinStock) {
                        System.out.println("Sorry! Not enough muffins left. Please bake more.");
                        return;
                    } else {
                        muffinOrder += mQty;
                        totalSales += mQty * muffinPrice;
                    }
                    break;
                case 2:
                    System.out.print("How many shakes would you like to buy: ");
                    int sQty = Integer.parseInt(scanner.nextLine());
                    shakeOrder += sQty;
                    totalSales += sQty * shakePrice;
                    break;
                case 3:
                    System.out.print("How many coffees would you like to buy: ");
                    int cQty = Integer.parseInt(scanner.nextLine());
                    coffeeOrder += cQty;
                    totalSales += cQty * coffeePrice;
                    break;
                case 4:
                    if (firstSelection) {
                        System.out.println("Invalid selection.");
                    } else {
                        ordering = false;
                    }
                    break;
                default:
                    System.out.println("Invalid selection.");
            }
            firstSelection = false;
            
            
        }
        
        //only process payment when there is an order
        if (totalSales > 0) {
            processPayment(totalSales, muffinOrder, shakeOrder, coffeeOrder);
   
        }
    }
    
    public static void processPayment(double totalSales, int muffinOrder, int shakeOrder, int coffeeOrder) {
    	System.out.printf("Total cost of order is: $%.2f.%n", totalSales);
        double payment = 0.0;
        while (payment < totalSales) {
        	
        	System.out.println("Please enter money for payment: ");
        	payment = Double.parseDouble(scanner.nextLine());
        	if (payment < totalSales) {
        		System.out.println("Insufficient payment. Transaction cancelled. Please try again.");
        	}
        	
        }
        double change = payment - totalSales;
        System.out.printf("Change returned $%.2f.%n", change);

        // Update stock & sales only when the payment is >= the cost of the order
        muffinStock -= muffinOrder;
        muffinQtySold += muffinOrder;
        shakeQtySold += shakeOrder;
        coffeeQtySold += coffeeOrder;
        muffinSales += muffinOrder * muffinPrice;
        shakeSales += shakeOrder * shakePrice;
        coffeeSales += coffeeOrder * coffeePrice;
    }
      
    public static void bakeMuffins() {
        muffinStock += 25;
        System.out.println("Ok, 25 Muffins added. Total muffins in cafe is now " + muffinStock);
    }
        
    public static void showSalesReport() {
        System.out.printf("Unsold Muffins: %d%n", muffinStock);
        System.out.println("Total Sales:");
        System.out.printf("Muffin: %d $%.2f%n", muffinQtySold, muffinSales);
        System.out.printf("Shake: %d $%.2f%n", shakeQtySold, shakeSales);
        System.out.printf("Coffee: %d $%.2f%n", coffeeQtySold, coffeeSales);
        double totalItems = muffinQtySold + shakeQtySold + coffeeQtySold;
        double totalSales = muffinSales + shakeSales + coffeeSales;
        System.out.println("--------------------------------------");
        System.out.printf("%.0f $%.2f%n", totalItems, totalSales);
    }
        
    public static void updatePrices() {
        System.out.println("Which price would you like to update?");
        System.out.println("1. Muffin");
        System.out.println("2. Shake");
        System.out.println("3. Coffee");
        System.out.println("4. Cancel");
        System.out.print("Please select: ");
        
        int choice = Integer.parseInt(scanner.nextLine());
        
        switch (choice) {
            case 1:
                System.out.print("Enter new price for Muffin (current: $" + muffinPrice + "): ");
                muffinPrice = Double.parseDouble(scanner.nextLine());
                System.out.println("Muffin price updated to $" + muffinPrice);
                break;
            case 2:
                System.out.print("Enter new price for Shake (current: $" + shakePrice + "): ");
                shakePrice = Double.parseDouble(scanner.nextLine());
                System.out.println("Shake price updated to $" + shakePrice);
                break;
            case 3:
                System.out.print("Enter new price for Coffee (current: $" + coffeePrice + "): ");
                coffeePrice = Double.parseDouble(scanner.nextLine());
                System.out.println("Coffee price updated to $" + coffeePrice);
                break;
            case 4:
                System.out.println("Price update cancelled.");
                break;
            default:
                System.out.println("Invalid selection. No prices were updated.");
        }
    }

        
        public static void exitCafe() {
            System.out.println("Bye Bye.");
            scanner.close();
        }

}
