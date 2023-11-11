package Users;

public class Cart {
    private float totalPrice;
    public Cart() {
        this.totalPrice = 0;
    }

<<<<<<< Updated upstream
=======
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
        return productQuantities.entrySet().stream()
                .map(entry -> entry.getKey().getPrice() * entry.getValue())
                .reduce(0f, Float::sum);

    }
    public HashMap<Product, Integer> getProducts() {
        return productQuantities;
    }

    // String representation of the cart
>>>>>>> Stashed changes
    public String toString() {
        return "Total price: " + totalPrice;
    }
}
