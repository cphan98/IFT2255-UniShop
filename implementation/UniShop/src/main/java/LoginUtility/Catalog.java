package LoginUtility;
import Users.Buyer;
import Users.Seller;
import otherUtility.Category;
import products.Product;

import java.util.*;
import java.util.stream.Stream;

public class Catalog
{
    private final HashSet<Product> books_catalog;
    private final HashSet<Product> learningRes_catalog;
    private final HashSet<Product> stationery_catalog;
    private final HashSet<Product> electronics_catalog;
    private final HashSet<Product> desktopAcc_catalog;
    private final HashSet<Seller> sellers_list;

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
    public HashSet<Product> getCatalogType(Category category)
    {
        return switch (category) {
            case BOOKS -> books_catalog;
            case LEARNING_RESOURCES -> learningRes_catalog;
            case STATIONERY -> stationery_catalog;
            case ELECTRONICS -> electronics_catalog;
            case DESKTOP_ACCESSORIES -> desktopAcc_catalog;
        };
    }
    public void addProduct(Category category, Product product)
    {
        getCatalogType(category).add(product);
    }
    public void displayCatalog()
    {
        System.out.println("Books: \n" + categoryCatalogToString(books_catalog) + "\n");
        System.out.println("Learning Resources: \n" + categoryCatalogToString(learningRes_catalog) + "\n");
        System.out.println("Stationery: \n" + categoryCatalogToString(stationery_catalog) + "\n");
        System.out.println("Electronics: \n" + categoryCatalogToString(electronics_catalog) + "\n");
        System.out.println("Desktop Accessories: \n" + categoryCatalogToString(desktopAcc_catalog) + "\n");
        displaySellers();
    }

    public void displaySellers()
    {
        //create a method to add sellers in list when sign up
        System.out.println("List of Sellers: \n" + sellersToString() + "\n");
    }


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

    public Seller searchSellerByName(String id) {
        for (Seller seller : sellers_list) {
            if (seller.getId().equals(id))
                return seller;
        }
        return null;
    }
    public String categoryCatalogToString(HashSet<Product> catalog) {
        StringBuilder result = new StringBuilder();
        for (Product product : catalog) {
            result.append("\t").append(product.smallToString());
        }
        return result.toString();
    }
    public String sellersToString() {
        StringBuilder result = new StringBuilder();
        for (Seller seller : sellers_list) {
            result.append("\t").append(seller.getId()).append(": ").append(seller.getCategory()).append("\t\t").append("Likes: ").append(seller.getMetrics().getLikes()).append("\n");
        }
        return result.toString();
    }
    public void filterProductsByCategory(Category category) {
        HashSet<Product> catalog = getCatalogType(category);
        System.out.println("Products in category " + category + ":");
        for (Product product : catalog) {
            System.out.println("\t" + product.smallToString());
        }
    }
    public void orderProductsByPrice(boolean ascending) {
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
    public void orderProductsByLikes(boolean ascending) {
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
    public void orderProductsByAverageNote(boolean ascending) {
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
    public void filterSellersByCategory(Category category) {
        System.out.println("Sellers in category " + category + ":");
        for (Seller seller : sellers_list) {
            if (seller.getCategory().equals(category)) {
                System.out.println("\t" + seller.getId() + ": " + seller.getCategory() + "\t\tLikes: " + seller.getMetrics().getLikes() + "\n");
            }
        }
    }
    public void orderSellersByLikes(boolean ascending) {
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
                System.out.println(entry.getKey().getId() + ": " + entry.getKey().getCategory() + "\t\tLikes: " + entry.getKey().getMetrics().getLikes() + "\n")
        );
    }
    public void orderSellersByAverageNote(boolean ascending) {
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
                System.out.println(entry.getKey().getId() + ": " + entry.getKey().getCategory() + "\t\tAverage note: " + entry.getKey().getMetrics().getAverageNoteReceived() + "\n")
        );
    }
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
    public void filterSellersByFollowing(Buyer user) {
        System.out.println("Sellers you are following:");
        for (Seller seller : user.getSellersFollowed()) {
            System.out.println("\t" + seller.getId() + ": " + seller.getCategory() + "\t\tLikes: " + seller.getMetrics().getLikes() + "\n");
        }
    }
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
