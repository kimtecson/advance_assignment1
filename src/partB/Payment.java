package partB;

/**
 * Takes one cash payment and tells you if it’s enough, then calculates the change. 
 * If not enough, order is cancelled.
*/
public class Payment {
    private final double amountTendered;

    public Payment(double amountTendered) {
        if (amountTendered < 0) throw new IllegalArgumentException("Negative cash not allowed");
        this.amountTendered = amountTendered;
    }

    public double getAmountTendered() { return amountTendered; }
    public boolean covers(double total) { return amountTendered >= total; }

    public double change(double total) {
        if (!covers(total)) throw new IllegalStateException("Insufficient payment");
        return amountTendered - total;
    }
}

