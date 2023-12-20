package productClasses.Usages;

import productClasses.Product;

import java.util.HashMap;
import java.util.Map;

/**
 * Class that represents the cart of a buyer, which contains the products that the buyer wants to buy, as well as their
 * quantities
 */
public class Cart implements java.io.Serializable {

    // ATTRIBUTES

    private final HashMap<Product, Integer> productQuantities;

    // CONSTRUCTOR

    public Cart() {
        this.productQuantities = new HashMap<>();
    }

    // GETTER

    public HashMap<Product, Integer> getProducts() {
        return productQuantities;
    }

    // UTILITIES

    // quantities ------------------------------------------------------------------------------------------------------

    /**
     * Method that returns a product, given its name if it exists in the cart
     *
     * @param name the name of the product
     * @return the product if it exists there, null otherwise
     */
    public Product searchProductByName(String name) {
        for (Map.Entry<Product, Integer> entry : productQuantities.entrySet()) {
            if (entry.getKey().getTitle().equals(name)) {
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * Method that adds a product to the cart, or increases its quantity if it already is in there
     *
     * @param product the product to add
     * @param quantity the quantity to add
     */
    public void addProduct(Product product, int quantity) {
        productQuantities.merge(product, quantity, Integer::sum);
    }

    /**
     * Method that removes a certain quantity of the product chosen or removes it entirely if quantity drops to 0
     *
     * @param product
     * @param quantity
     */
    public void removeProduct(Product product, int quantity) {
        productQuantities.computeIfPresent(product, (key, val) -> val - quantity > 0 ? val - quantity : null);
    }

    // cost -----------------------------------------------------------------------------------------------------------

    /**
     * Method that returns the total price of all the products in the cart
     *
     * @return the total price of the cart
     */
    public float getTotalPrice() {
        float total = productQuantities.entrySet().stream()
                .map(entry -> entry.getKey().getPrice() * entry.getValue())
                .reduce(0f, Float::sum);
        return Math.round(total * 100.0) / 100.0f;

    }

    // to string ------------------------------------------------------------------------------------------------------

    /**
     * Method that returns a string representation of the cart
     *
     * @return the string representation of the cart
     */
    public String toString() {
        if (productQuantities.isEmpty()) return ("Cart is empty");
        StringBuilder sb = new StringBuilder();
        sb.append("Cart Contents:\n");
        productQuantities.forEach((product, quantity) -> sb.append("\t")
                .append(product.getTitle())
                .append(" - Quantity: ")
                .append(quantity)
                .append(", Price: ")
                .append(product.getPrice()).append("$")
                .append("\n"));
        sb.append("Total Price: ")
                .append(getTotalPrice())
                .append("$")
                .append(" (or ")
                .append(Math.ceil(getTotalPrice() * 50))
                .append(" points)")
                .append("\n\n");

        int pointsWon = productQuantities.entrySet().stream()
                .mapToInt(entry -> entry.getKey().getBasePoints() * entry.getValue())
                .sum();
        sb.append("You'll get ").append(pointsWon).append(" points for this purchase!");
        return sb.toString();
    }
}
