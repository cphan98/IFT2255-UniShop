package UIs.Buyer.Controllers;

import BackEndUtility.Catalog;
import BackEndUtility.Category;
import BackEndUtility.DataBase;
import BackEndUtility.InputManager;
import UIs.Buyer.BuyerMenu;
import UIs.UIUtilities;
import Users.Buyer;
import Users.Seller;
import UtilityObjects.Notification;
import productClasses.Product;
import productClasses.Usages.Evaluation;
import productClasses.Usages.Order;

import java.util.ArrayList;
import java.util.HashMap;

import static UIs.Menu.line;
/**
 * The CatalogController class represents a controller responsible for managing
 * the interaction between the buyer user interface and the catalog, allowing the user
 * to browse, search, and interact with products and sellers.
 *
 */
public class CatalogController {

    // ATTRIBUTES
    private final Buyer user;
    private final DataBase database;
    private Product pointedProduct = null;
    private Seller pointedSeller = null;
    private Catalog catalog;
    private final UIUtilities uiUtilities;

    // CONSTRUCTOR
    /**
     * Constructs a new CatalogController with the specified buyer user and database.
     *
     * @param user     the buyer user associated with the controller
     * @param database the database containing product and seller information
     */
    public CatalogController(Buyer user, DataBase database) {
        this.user = user;
        this.database = database;
        this.uiUtilities = new UIUtilities(database, user);
    }

    // UTILITIES

    // catalog page ---------------------------------------------------------------------------------------------------
    /**
     * Displays the catalog and provides options to: get a product, Get a seller's info and products
     * filter products, filter sellers and display products liked by the buyers you follow
     * The user can interact with everone of these options.
     *
     * @return {@code true} if the user interaction loop continues, {@code false} if
     * the user chooses to return to the menu
     */
    public boolean displayCatalog() {
        line();
        System.out.println("CATALOG");
        catalog = new Catalog(database.getSellers());
        catalog.displayCatalog();
        line();

        boolean continueLoop = true;
        while (continueLoop) {
            System.out.println("1. Get a product");
            System.out.println("2. Get a seller's info and products");
            System.out.println("3. Filter products");
            System.out.println("4. Filter sellers");
            System.out.println("5. Display products liked by the buyers you follow");
            System.out.println("6. Return to menu");

            int choice = uiUtilities.getUserInputAsInteger();

            switch (choice) {
                case 1 -> searchAndDisplayProduct();
                case 2 -> searchSeller();
                case 3 -> filterProducts();
                case 4 -> filterSellers();
                case 5 -> displayProductsLikedByFollowing();
                case 6 -> {
                    System.out.println();
                    System.out.println("Returning to menu...");
                    continueLoop = false;
                }
                default -> System.out.println("Invalid selection. Please try again.");
            }
        }
        return true;
    }

    // product page ---------------------------------------------------------------------------------------------------
    /**
     * Searches for a product in the catalog and displays its details.
     */
    public void searchAndDisplayProduct() {
        if (searchProduct(catalog)) {
            System.out.println(pointedProduct);
            interactWithProduct();
        } else {
            System.out.println("Product not found. Please try again.");
        }
    }
    /**
     * Searches for a product in the catalog by the specified title.
     *
     * @param catalog the catalog to search within
     * @return boolean true if the user decides to continue searching, boolean false otherwise
     */
    public boolean searchProduct(Catalog catalog) {
        boolean continueLoop = true;
        while (continueLoop) {
            System.out.println("Enter the name of the product you want to search:");
            String title = InputManager.getInstance().nextLine();
            pointedProduct = catalog.searchProductByName(title);
            continueLoop = pointedProduct == null;
        }
        return true;  // continue the loop
    }
    /**
     * Displays options for the user to interact with the pointed product.
     * Options include adding the product to the cart, removing and adding it from the wishlist,
     * making an evaluation, interacting with existing evaluations, and returning to the catalog.
     */
    public void interactWithProduct() {
        boolean continueInteraction = true;
        while (continueInteraction) {
            System.out.println("\n1. Add product to cart");
            if (user.getWishList().contains(pointedProduct)) {
                System.out.println("2. Remove product from wishlist");
            } else {
                System.out.println("2. Add product to wishlist");
            }
            System.out.println("3. Make an evaluation");
            System.out.println("4. Interact with an evaluation");
            System.out.println("5. Return to catalog");

            int choice = uiUtilities.getUserInputAsInteger();

            switch (choice) {
                case 1 -> addProductToCart(pointedProduct);
                case 2 -> uiUtilities.toggleProductToWishList(user, pointedProduct);
                case 3 -> addEvaluationToProduct(pointedProduct);
                case 4 -> interactWithEvaluations();
                case 5 -> {
                    catalog.displayCatalog();
                    continueInteraction = false;
                }
                default -> System.out.println("Invalid selection. Please try again.");
            }
        }
    }
    /**
     * Adds the specified product to the user's cart with a specified quantity.
     * if there are not enough products as required, a message is shown
     *
     * @param product  the product to add to the cart
     */
    public void addProductToCart(Product product) {
        System.out.println();
        System.out.println("Adding product to cart...");

        // ask quantity
        System.out.println();
        System.out.println("How many of it do you want?");

        // validate quantity
        int quantity = uiUtilities.getUserInputAsInteger();
        if (quantity > product.getQuantity()) {
            System.out.println("Not enough products in stock");
            return;
        }

        // add to cart
        user.getCart().addProduct(product, quantity);

        // update product quantity
        database.getProducts().get(database.getProducts().indexOf(product)).setQuantity(product.getQuantity() - quantity);

        // confirm product add
        System.out.println();
        System.out.println("Product added to cart");
    }
    /**
     * Adds an evaluation to the specified product if the user has purchased the product.
     *
     * @param product the product to evaluate
     */
    private void addEvaluationToProduct(Product product) {
        System.out.println();
        System.out.println("Adding evaluation...");

        // evaluation can only be made if user bought product
        ArrayList<Product> productsBought = new ArrayList<>(); // list of products bought
        for (Order order : user.getOrderHistory())
            productsBought.addAll(order.getProducts().keySet());
        if (!productsBought.contains(product)) {
            System.out.println();
            System.out.println("WARNING : Cannot make evaluation");
            System.out.println("You did not purchase this product.");
            return;
        }

        // ask evaluation details
        InputManager inputManager = InputManager.getInstance();
        System.out.println("Enter a comment:");
        String comment = inputManager.nextLine();
        float rating = -1F;
        while (rating < 0 || rating > 5) {
            System.out.println("Enter a rating between 0 and 5:");
            rating = Float.parseFloat(inputManager.nextLine());
        }
        database.addEvaluationToProduct(pointedProduct,new Evaluation(comment, rating, user));
        String title = "You got a new evaluation!";
        String summary = this.user + " added a new comment on " + product.getTitle() + "!";
        product.getSeller().addNotification(new Notification(title, summary));
    }
    /**
     * Interacts with the evaluations of the pointed product, allowing the user to like/unlike
     * evaluations and follow/unfollow buyers who authored the evaluations.
     */
    private void interactWithEvaluations() {
        System.out.println("Enter the number of the evaluation you want to interact with, or 0 to return:");
        int choice = uiUtilities.getUserInputAsInteger();
        if (choice == 0) {
            System.out.println("Returning to product...");
        } else if (choice > pointedProduct.getEvaluations().size()) {
            System.out.println("Invalid selection. Please try again.");
        } else {
            Evaluation pointedEvaluation = pointedProduct.getEvaluations().get(choice - 1);
            System.out.println(pointedEvaluation);
            if (user.getEvaluationsLiked().contains(pointedEvaluation)) {
                System.out.println("1. Unlike this evaluation");
            } else {
                System.out.println("1. Like this evaluation");
            }
            if (user.getBuyersFollowed().contains(pointedEvaluation.getAuthor())) {
                System.out.println("2. Unfollow this buyer");
            } else {
                System.out.println("2. Follow this buyer");
            }
            System.out.println("3. Return to product");
            int choice2 = uiUtilities.getUserInputAsInteger();
            switch (choice2) {
                case 1:
                    uiUtilities.toggleEvaluationLike(user, pointedEvaluation);
                    break;
                case 2:
                    uiUtilities.toggleBuyerToFollowing(user, pointedEvaluation.getAuthor());
                    break;
                case 3:
                    System.out.println("Returning to product...");
                    break;
                default:
                    System.out.println("Invalid selection. Please try again.");
                    break;
            }
        }
    }

    // seller page ----------------------------------------------------------------------------------------------------
    /**
     * Displays options for searching and interacting with sellers.
     * The user can search for sellers by ID or address and choose to view a seller's profile.
     * Options include following/unfollowing a seller and returning to the main catalog menu.
     */
    public void searchSeller() {
        line();
        ArrayList<Seller> listOfSellers = null;
        boolean continueLoop = true;
        while (continueLoop) {
            System.out.println("1. Search by ID");
            System.out.println("2. Search by address");
            System.out.println("3. Return to menu");
            int choice = uiUtilities.getUserInputAsInteger();

            switch (choice) {
                case 1 -> {
                    System.out.println("Enter the id of the seller you want to view:");
                    String id = InputManager.getInstance().nextLine();
                    listOfSellers = database.searchSellerById(id);
                    if (listOfSellers == null) {
                        System.out.println("Seller not found.");
                    } else {
                        System.out.println(listOfSellers);
                        while (true) {
                            System.out.println("Enter the desired seller to view their profile:");
                            int i = 0;
                            for (Seller seller : listOfSellers) {
                                i++;
                                System.out.println(i + ") " + seller.getId());
                            }
                            int index = uiUtilities.getUserInputAsInteger();
                            if (index > 0 && index <= listOfSellers.size()) {
                                pointedSeller = listOfSellers.get(index-1);
                                interactWithSeller();
                                break;
                            } else {
                                System.out.println("Invalid selection. Please try again.");
                            }
                        }
                    }
                }
                case 2 -> {
                    System.out.println("Enter the address of the seller you want to view:");
                    String address = InputManager.getInstance().nextLine();
                    listOfSellers = database.searchSellerByAddress(address);
                    if (listOfSellers == null) {
                        System.out.println("Seller not found.");
                    } else {
                        System.out.println(listOfSellers);
                        while (true) {
                            System.out.println("Enter the desired seller to view their profile:");
                            int i = 0;
                            for (Seller seller : listOfSellers) {
                                i++;
                                System.out.println(i + ") " + seller.getId());
                            }
                            int index = uiUtilities.getUserInputAsInteger();
                            if (index > 0 && index <= listOfSellers.size()) {
                                pointedSeller = listOfSellers.get(index-1);
                                interactWithSeller();
                                break;
                            } else {
                                System.out.println("Invalid selection. Please try again.");
                            }
                        }
                    }
                }
                case 3 -> {
                    System.out.println("Returning to menu...");
                    continueLoop = false;
                }
                default -> System.out.println("Invalid selection. Please try again.");
            }
        }
    }
    /**
     * Interacts with the profile of the pointed seller, allowing the user to follow or unfollow the seller
     * and return to the main catalog menu.
     */
    public void interactWithSeller() {
        line();
        System.out.println(pointedSeller.toString());
        line();
        System.out.println();
        if (user.getSellersFollowed().contains(pointedSeller)) {
            System.out.println("1. Unfollow this seller");
        } else {
            System.out.println("1. Follow this seller");
        }
        System.out.println("2. Return to catalog");
        int choice = uiUtilities.getUserInputAsInteger();
        switch (choice) {
            case 1 -> uiUtilities.toggleSellerToFollowing(user, pointedSeller);
            case 2 -> {
                System.out.println("Returning to catalog...");
                catalog.displayCatalog();
            }
            default -> System.out.println("Invalid selection. Please try again.");
        }
    }

    // filter products ------------------------------------------------------------------------------------------------
    /**
     * Displays options for filtering products in the catalog based on categories,
     * price, likes, and average rating.
     */
    public void filterProducts() {
        System.out.println("1. Filter by category");
        System.out.println("2. Order by price");
        System.out.println("3. Order by likes");
        System.out.println("4. Order by average rating");
        System.out.println("5. Return to menu");
        int choice = uiUtilities.getUserInputAsInteger();
        switch (choice) {
            case 1 -> {
                System.out.println("1. Books");
                System.out.println("2. Learning resources");
                System.out.println("3. Stationery");
                System.out.println("4. Electronics");
                System.out.println("5. Desktop accessories");
                System.out.println("6. Return to menu");
                int choice2 = uiUtilities.getUserInputAsInteger();
                switch (choice2) {
                    case 1 -> catalog.filterProductsByCategory(Category.BOOKS);
                    case 2 -> catalog.filterProductsByCategory(Category.LEARNING_RESOURCES);
                    case 3 -> catalog.filterProductsByCategory(Category.STATIONERY);
                    case 4 -> catalog.filterProductsByCategory(Category.ELECTRONICS);
                    case 5 -> catalog.filterProductsByCategory(Category.DESKTOP_ACCESSORIES);
                    case 6 -> System.out.println("Returning to menu...");
                    default -> System.out.println("Invalid selection. Please try again.");
                }
            }
            case 2 -> {
                System.out.println("1. Ascending");
                System.out.println("2. Descending");
                System.out.println("3. Return to menu");
                int choice3 = uiUtilities.getUserInputAsInteger();
                switch (choice3) {
                    case 1 -> catalog.orderProducts(true, "price");
                    case 2 -> catalog.orderProducts(false, "price");
                    case 3 -> System.out.println("Returning to menu...");
                    default -> System.out.println("Invalid selection. Please try again.");
                }
            }
            case 3 -> {
                System.out.println("1. Ascending");
                System.out.println("2. Descending");
                System.out.println("3. Return to menu");
                int choice4 = uiUtilities.getUserInputAsInteger();
                switch (choice4) {
                    case 1 -> catalog.orderProducts(true, "likes");
                    case 2 -> catalog.orderProducts(false, "likes");
                    case 3 -> System.out.println("Returning to menu...");
                    default -> System.out.println("Invalid selection. Please try again.");
                }
            }
            case 4 -> {
                System.out.println("1. Ascending");
                System.out.println("2. Descending");
                System.out.println("3. Return to menu");
                int choice5 = uiUtilities.getUserInputAsInteger();
                switch (choice5) {
                    case 1 -> catalog.orderProducts(true, "averageNote");
                    case 2 -> catalog.orderProducts(false, "averageNote");
                    case 3 -> System.out.println("Returning to menu...");
                    default -> System.out.println("Invalid selection. Please try again.");
                }
            }
        }
    }

    // filter sellers -------------------------------------------------------------------------------------------------
    /**
     * Displays options for filtering sellers in the catalog based on categories, likes,
     * and average rating.
     */
    public void filterSellers() {
        System.out.println("1. Filter by category");
        System.out.println("2. Order by likes");
        System.out.println("3. Order by average rating");
        System.out.println("4. Display only favorite sellers");
        System.out.println("5. Return to menu");
        int choice = uiUtilities.getUserInputAsInteger();
        switch (choice) {
            case 1:
                System.out.println("1. Books");
                System.out.println("2. Learning resources");
                System.out.println("3. Stationery");
                System.out.println("4. Electronics");
                System.out.println("5. Desktop accessories");
                System.out.println("6. Return to menu");
                int choice2 = uiUtilities.getUserInputAsInteger();
                switch (choice2) {
                    case 1:
                        catalog.filterSellersByCategory(Category.BOOKS);
                        break;
                    case 2:
                        catalog.filterSellersByCategory(Category.LEARNING_RESOURCES);
                        break;
                    case 3:
                        catalog.filterSellersByCategory(Category.STATIONERY);
                        break;
                    case 4:
                        catalog.filterSellersByCategory(Category.ELECTRONICS);
                        break;
                    case 5:
                        catalog.filterSellersByCategory(Category.DESKTOP_ACCESSORIES);
                        break;
                    case 6:
                        System.out.println("Returning to menu...");
                        break;
                    default:
                        System.out.println("Invalid selection. Please try again.");
                        break;
                }
                break;
            case 2:
                System.out.println("1. Ascending");
                System.out.println("2. Descending");
                System.out.println("3. Return to menu");
                int choice4 = uiUtilities.getUserInputAsInteger();
                switch (choice4) {
                    case 1:
                        catalog.orderSellers(true, "likes");
                        break;
                    case 2:
                        catalog.orderSellers(false, "likes");
                        break;
                    case 3:
                        System.out.println("Returning to menu...");
                        break;
                    default:
                        System.out.println("Invalid selection. Please try again.");
                        break;
                }
                break;
            case 3:
                System.out.println("1. Ascending");
                System.out.println("2. Descending");
                System.out.println("3. Return to menu");
                int choice5 = uiUtilities.getUserInputAsInteger();
                switch (choice5) {
                    case 1 -> catalog.orderSellers(true, "averageNote");
                    case 2 -> catalog.orderSellers(false, "averageNote");
                    case 3 -> System.out.println("Returning to menu...");
                    default -> System.out.println("Invalid selection. Please try again.");
                }
                break;
            case 4:
                catalog.filterSellersByFollowing(user);
                break;
            case 5:
                System.out.println("Returning to menu...");
                break;
        }
    }

    // display products liked -----------------------------------------------------------------------------------------
    /**
     * Displays products liked by the buyers followed by the current user.
     */
    public void displayProductsLikedByFollowing() {
        System.out.println("Products liked by the buyers you're following:");
        HashMap<Product, Integer> productsLikedByFollowing = new HashMap<>();
        for (Buyer buyer : user.getBuyersFollowed()) {
            for (Product product : buyer.getWishList()) {
                if (productsLikedByFollowing.containsKey(product)) {
                    productsLikedByFollowing.put(product, productsLikedByFollowing.get(product) + 1);
                } else {
                    productsLikedByFollowing.put(product, 1);
                }
            }
        }
        for (Product product : productsLikedByFollowing.keySet()) {
            System.out.println(product.smallToString());
        }
    }
}
