package Users;

import products.Product;
import java.util.HashMap;
import java.util.Map;

public class Cart {
    private HashMap<Product, Integer> productQuantities;

    public Cart() {
        this.productQuantities = new HashMap<>();
    }

    // Adds a product to the cart or increases the quantity if it already exists
    public void addProduct(Product product, int quantity) {
        productQuantities.merge(product, quantity, Integer::sum);
    }

    // Removes a certain quantity of the product or removes it entirely if quantity drops to 0
    public void removeProduct(Product product, int quantity) {
        productQuantities.computeIfPresent(product, (key, val) -> val - quantity > 0 ? val - quantity : null);
    }

    // Calculate total price based on the quantities of each product
    public float getTotalPrice() {
        return productQuantities.entrySet().stream()
                .map(entry -> entry.getKey().getPrice() * entry.getValue())
                .reduce(0f, Float::sum);
    }
    public HashMap<Product, Integer> getProducts() {
        return productQuantities;
    }

    // String representation of the cart
    public String toString() {
        if (productQuantities.isEmpty()) return ("Cart is empty");
        StringBuilder sb = new StringBuilder();
        sb.append("Cart Contents:\n");
        productQuantities.forEach((product, quantity) -> sb.append("\t").append(product.getTitle())
                .append(" - Quantity: ")
                .append(quantity)
                .append(", Price: ")
                .append(product.getPrice()).append("$")
                .append("\n"));
        sb.append("Total Price: ").append(getTotalPrice()).append("$").append(" (or ").append(getTotalPrice() * 50).append(" points)");
        return sb.toString();
    }
}
