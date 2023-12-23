package BackEndUtility;

import Users.Buyer;
import Users.Seller;
import productClasses.Product;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Stream;

/**
 * The Catalog class represents a catalog of products and sellers in UniShop.
 * It organizes products into different categories and provides various functionalities for
 * displaying, filtering, and ordering products and sellers.
 *
 */
public class Catalog implements Serializable {

    // ATTRIBUTES

    private final HashSet<Product> books_catalog;
    private final HashSet<Product> learningRes_catalog;
    private final HashSet<Product> stationery_catalog;
    private final HashSet<Product> electronics_catalog;
    private final HashSet<Product> desktopAcc_catalog;
    private final HashSet<Seller> sellers_list;

    // CONSTRUCTOR
    /**
     * Constructs a catalog with the given list of sellers. Populates the catalog with products
     * from each seller based on their category.
     *
     * @param sellers List of sellers to initialize the catalog with.
     */
    public Catalog(ArrayList<Seller> sellers) {
        this.books_catalog = new HashSet<>();
        this.learningRes_catalog = new HashSet<>();
        this.stationery_catalog = new HashSet<>();
        this.electronics_catalog = new HashSet<>();
        this.desktopAcc_catalog = new HashSet<>();
        sellers_list = new HashSet<>();
        // add seller to list of sellers
        sellers_list.addAll(sellers);
        for (Seller seller : sellers) { // add all sellers' products to catalog according to its category
            for (Product product : seller.getProducts()) {
                addProduct(product.getCategory(), product);
            }
        }
    }

    // GETTER
    /**
     * Returns the set of products for a specific category.
     *
     * @param category The category of products to retrieve.
     * @return A HashSet of products belonging to the specified category.
     */
    public HashSet<Product> getCatalogType(Category category) {
        return switch (category) {
            case BOOKS -> books_catalog;
            case LEARNING_RESOURCES -> learningRes_catalog;
            case STATIONERY -> stationery_catalog;
            case ELECTRONICS -> electronics_catalog;
            case DESKTOP_ACCESSORIES -> desktopAcc_catalog;
        };
    }

    // UTILITIES

    // catalog --------------------------------------------------------------------------------------------------------
    /**
     * Displays the entire catalog organized by categories, and prints the list of sellers.
     */
    public void displayCatalog() {
        System.out.println("Books: \n" + categoryCatalogToString(books_catalog) + "\n");
        System.out.println("Learning Resources: \n" + categoryCatalogToString(learningRes_catalog) + "\n");
        System.out.println("Stationery: \n" + categoryCatalogToString(stationery_catalog) + "\n");
        System.out.println("Electronics: \n" + categoryCatalogToString(electronics_catalog) + "\n");
        System.out.println("Desktop Accessories: \n" + categoryCatalogToString(desktopAcc_catalog) + "\n");
        displaySellers();
    }

    // products -------------------------------------------------------------------------------------------------------

    private void addProduct(Category category, Product product) {
        getCatalogType(category).add(product);
    }
    /**
     * Filters and prints products in the catalog based on the specified category.
     *
     * @param category The category by which products will be filtered and displayed.
     */
    public void filterProductsByCategory(Category category) {
        HashSet<Product> catalog = getCatalogType(category);
        System.out.println("Products in category " + category + ":");
        for (Product product : catalog) {
            System.out.println("\t" + product.smallToString());
        }
    }
    /**
     * Searches for a product in the catalog by its title.
     *
     * @param title The title of the product to search for.
     * @return The information for the found product or null if there is no matching product.
     */
    public Product searchProductByName(String title) {
        for (Product product : books_catalog) {
            if (product.getTitle().equals(title))
                return product;
        }
        for (Product product : learningRes_catalog) {
            if (product.getTitle().equals(title))
                return product;
        }
        for (Product product : stationery_catalog) {
            if (product.getTitle().equals(title))
                return product;
        }
        for (Product product : electronics_catalog) {
            if (product.getTitle().equals(title))
                return product;
        }
        for (Product product : desktopAcc_catalog) {
            if (product.getTitle().equals(title))
                return product;
        }
        return null;
    }

    private void orderProductsByPrice(boolean ascending) {
        HashMap<Product, Float> productPrices = new HashMap<>();
        for (Product product : books_catalog) {
            productPrices.put(product, product.getPrice());
        }
        for (Product product : learningRes_catalog) {
            productPrices.put(product, product.getPrice());
        }
        for (Product product : stationery_catalog) {
            productPrices.put(product, product.getPrice());
        }
        for (Product product : electronics_catalog) {
            productPrices.put(product, product.getPrice());
        }
        for (Product product : desktopAcc_catalog) {
            productPrices.put(product, product.getPrice());
        }

        Stream<Map.Entry<Product, Float>> sortedStream = productPrices.entrySet().stream()
                .sorted(Map.Entry.comparingByValue());

        if (!ascending) {
            sortedStream = sortedStream.sorted(Collections.reverseOrder(Map.Entry.comparingByValue()));
        }

        System.out.println("Products ordered by price:");
        sortedStream.forEach(entry ->
                System.out.println(entry.getKey().smallToString() + "\n")
        );
    }

    private void orderProductsByLikes(boolean ascending) {
        HashMap<Product, Integer> productLikes = new HashMap<>();
        for (Product product : books_catalog) {
            productLikes.put(product, product.getLikes());
        }
        for (Product product : learningRes_catalog) {
            productLikes.put(product, product.getLikes());
        }
        for (Product product : stationery_catalog) {
            productLikes.put(product, product.getLikes());
        }
        for (Product product : electronics_catalog) {
            productLikes.put(product, product.getLikes());
        }
        for (Product product : desktopAcc_catalog) {
            productLikes.put(product, product.getLikes());
        }

        Stream<Map.Entry<Product, Integer>> sortedStream = productLikes.entrySet().stream()
                .sorted(Map.Entry.comparingByValue());

        if (!ascending) {
            sortedStream = sortedStream.sorted(Collections.reverseOrder(Map.Entry.comparingByValue()));
        }

        System.out.println("Products ordered by likes:");
        sortedStream.forEach(entry ->
                System.out.println(entry.getKey().smallToString() + "\n")
        );
    }

    private void orderProductsByAverageNote(boolean ascending) {
        HashMap<Product, Float> productNotes = new HashMap<>();
        for (Product product : books_catalog) {
            productNotes.put(product, product.getOverallRating());
        }
        for (Product product : learningRes_catalog) {
            productNotes.put(product, product.getOverallRating());
        }
        for (Product product : stationery_catalog) {
            productNotes.put(product, product.getOverallRating());
        }
        for (Product product : electronics_catalog) {
            productNotes.put(product, product.getOverallRating());
        }
        for (Product product : desktopAcc_catalog) {
            productNotes.put(product, product.getOverallRating());
        }

        Stream<Map.Entry<Product, Float>> sortedStream = productNotes.entrySet().stream()
                .sorted(Map.Entry.comparingByValue());

        if (!ascending) {
            sortedStream = sortedStream.sorted(Collections.reverseOrder(Map.Entry.comparingByValue()));
        }

        System.out.println("Products ordered by average note:");
        sortedStream.forEach(entry ->
                System.out.println(entry.getKey().smallToString() + "\n")
        );
    }
    /**
     * Orders and prints products in the catalog based on the specified sorting criteria.
     *
     * @param ascending Indicates if the sorting should be in ascending order or not.
     * @param filter    The criteria by which products will be ordered ("price", "likes", or "averageNote").
     */
    public void orderProducts(boolean ascending, String filter) {
        switch (filter) {
            case "price":
                orderProductsByPrice(ascending);
                break;
            case "likes":
                orderProductsByLikes(ascending);
                break;
            case "averageNote":
                orderProductsByAverageNote(ascending);
                break;
        }
    }

    // sellers --------------------------------------------------------------------------------------------------------

    private void displaySellers() {
        //create a method to add sellers in list when sign up
        System.out.println("List of Sellers: \n" + sellersToString() + "\n");
    }

    // to string ------------------------------------------------------------------------------------------------------

    private String categoryCatalogToString(HashSet<Product> catalog) {
        StringBuilder result = new StringBuilder();
        for (Product product : catalog) {
            result.append("\t").append(product.smallToString());
        }
        return result.toString();
    }

    private String sellersToString() {
        StringBuilder result = new StringBuilder();
        for (Seller seller : sellers_list) {
            result.append("\t")
                    .append(seller.getId())
                    .append(": ")
                    .append(seller.getCategory())
                    .append("\t\t").append("Likes: ")
                    .append(seller.getMetrics().getLikes())
                    .append("\n");
        }
        return result.toString();
    }
    /**
     * Filters sellers in the catalog based on the specified category and prints the ones that meet the criteria.
     *
     * @param category The category by which sellers will be filtered and displayed.
     */
    public void filterSellersByCategory(Category category) {
        System.out.println("Sellers in category " + category + ":");
        for (Seller seller : sellers_list) {
            if (seller.getCategory().equals(category)) {
                System.out.println("\t" + seller.getId() + ": " + seller.getCategory() + "\t\tLikes: " +
                        seller.getMetrics().getLikes() + "\n");
            }
        }
    }

    private void orderSellersByLikes(boolean ascending) {
        HashMap<Seller, Integer> sellerLikes = new HashMap<>();
        for (Seller seller : sellers_list) {
            sellerLikes.put(seller, seller.getMetrics().getLikes());
        }

        Stream<Map.Entry<Seller, Integer>> sortedStream = sellerLikes.entrySet().stream()
                .sorted(Map.Entry.comparingByValue());

        if (!ascending) {
            sortedStream = sortedStream.sorted(Collections.reverseOrder(Map.Entry.comparingByValue()));
        }

        System.out.println("Sellers ordered by likes:");
        sortedStream.forEach(entry ->
                System.out.println(entry.getKey().getId() + ": " + entry.getKey().getCategory()
                        + "\t\tLikes: " + entry.getKey().getMetrics().getLikes() + "\n")
        );
    }

    private void orderSellersByAverageNote(boolean ascending) {
        HashMap<Seller, Float> sellerNotes = new HashMap<>();
        for (Seller seller : sellers_list) {
            sellerNotes.put(seller, seller.getMetrics().getAverageNoteReceived());
        }

        Stream<Map.Entry<Seller, Float>> sortedStream = sellerNotes.entrySet().stream()
                .sorted(Map.Entry.comparingByValue());

        if (!ascending) {
            sortedStream = sortedStream.sorted(Collections.reverseOrder(Map.Entry.comparingByValue()));
        }

        System.out.println("Sellers ordered by average note:");
        sortedStream.forEach(entry ->
                System.out.println(entry.getKey().getId() + ": " + entry.getKey().getCategory()
                        + "\t\tAverage note: " + entry.getKey().getMetrics().getAverageNoteReceived() + "\n")
        );
    }
    /**
     * Filters and prints sellers in the catalog based on the sellers followed by a specific buyer.
     *
     * @param user The buyer for whom to display the sellers they are following.
     */
    public void filterSellersByFollowing(Buyer user) {
        System.out.println("Sellers you are following:");
        for (Seller seller : user.getSellersFollowed()) {
            System.out.println("\t" + seller.getId() + ": " + seller.getCategory() + "\t\tLikes: "
                    + seller.getMetrics().getLikes() + "\n");
        }
    }
    /**
     * Orders and prints sellers in the catalog based on the specified sorting criteria.
     *
     * @param ascending Indicates whether the sorting should be in ascending order.
     * @param filter    The criteria by which sellers will be ordered ("likes" or "averageNote").
     */
    public void orderSellers(boolean ascending, String filter) {
        switch (filter) {
            case "likes":
                orderSellersByLikes(ascending);
                break;
            case "averageNote":
                orderSellersByAverageNote(ascending);
                break;
        }
    }
}
