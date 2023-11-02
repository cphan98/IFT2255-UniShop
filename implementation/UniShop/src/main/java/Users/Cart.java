package Users;

public class Cart {
    private float totalPrice;
    public Cart() {
        this.totalPrice = 0;
    }

    public String toString() {
        return "Total price: " + totalPrice;
    }
}
