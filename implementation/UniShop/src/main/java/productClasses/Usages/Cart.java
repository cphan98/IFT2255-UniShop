package productClasses.Usages;

import productClasses.Product;
import java.util.HashMap;
import java.util.Map;

public class Cart implements java.io.Serializable {
    private final HashMap<Product, Integer> productQuantities;

    public Cart() {
        this.productQuantities = new HashMap<>();
    }
  
    public Product searchProductByName(String name) {
        for (Map.Entry<Product, Integer> entry : productQuantities.entrySet()) {
            if (entry.getKey().getTitle().equals(name)) {
                return entry.getKey();
            }
        }
        return null;
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
        float total = productQuantities.entrySet().stream()
                .map(entry -> entry.getKey().getPrice() * entry.getValue())
                .reduce(0f, Float::sum);
        return Math.round(total * 100.0) / 100.0f;

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
        sb.append("Total Price: ").append(getTotalPrice()).append("$").append(" (or ").append(Math.ceil(getTotalPrice() * 50)).append(" points)").append("\n\n");

        int pointsWon = productQuantities.entrySet().stream()
                .mapToInt(entry -> entry.getKey().getBasePoints() * entry.getValue())
                .sum();
        sb.append("You'll get ").append(pointsWon).append(" points for this purchase!");
        return sb.toString();
    }
}
