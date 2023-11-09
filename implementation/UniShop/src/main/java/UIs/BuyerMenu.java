package UIs;

import LoginUtility.Catalog;
import LoginUtility.DataBase;
import Users.Address;
import Users.Buyer;
import Users.CreditCard;
import Users.Seller;
import otherUtility.Category;
import products.Evaluation;
import products.Order;
import products.Product;

import java.util.HashMap;
import java.util.Objects;

import static java.lang.Integer.parseInt;

public class BuyerMenu extends Menu {
    private Buyer user;
    private DataBase database;
    private Product pointedProduct = null;
    private Catalog catalog;

    public BuyerMenu(Buyer user, DataBase database) {
        super(user);
        this.user = user;
        this.database = database;
    }

    public boolean displayMenu() {
        boolean continueLoop = true;
        InputManager inputManager = InputManager.getInstance();

        while(continueLoop) {
            line();
            System.out.println("Welcome to UniShop, " + user.getId() + "!");
            line();
            System.out.println();
            System.out.println("Please select an option:");

            System.out.println("1. Display Profile");
            System.out.println("2. Display Order History");
            System.out.println("3. Display Cart");
            System.out.println("4. Display Wishlist");
            System.out.println("5. Display Catalog");
            System.out.println("6. Log out");
            String choice = inputManager.nextLine();

            switch (choice) {
                case "1":
                    continueLoop = displayProfile();
                    break;
                case "2":
                    continueLoop = displayOrderHistory();
                    break;
                case "3":
                    continueLoop = displayCart();
                    break;
                case "4":
                    continueLoop = displayWishList();
                    break;
                case "5":
                    continueLoop = displayCatalog();
                    break;
                case "6":
                    return false;  // Add this to handle log out
                default:
                    System.out.println("RICKROLL");
                    break;
            }
        }
        return true;
    }

    public boolean displayProfile() {
        boolean continueLoop = true;
        while (continueLoop) {
            line();
            System.out.println("PROFILE");
            InputManager inputManager = InputManager.getInstance();
            System.out.println("Name: " + user.getId());
            System.out.println("Email: " + user.getEmail());
            System.out.println();
            System.out.println("METRICS");
            displayMetrics();
            line();
            System.out.println();
            System.out.println("1. Modify profile");
            System.out.println("2. Return to menu");
            System.out.println("3. Delete account");
            String choice = inputManager.nextLine();

            switch (choice) {
                case "1":
                    System.out.println("Modifying profile...");
                    break;
                case "2":
                    System.out.println("Returning to menu...");
                    continueLoop = false;
                    break;
                default:
                    System.out.println("RICKROLL");
                    return false;  // continue the loop
            }
        }
        return true;  // continue the loop
    }

    public boolean displayOrderHistory() {
        InputManager inputManager = InputManager.getInstance();
        String choice = "";
        while (!choice.equals("1")) {
            System.out.println("ORDER HISTORY");
            System.out.println(user.ordersMadeToString());
            System.out.println("1. Return to menu");
            choice = inputManager.nextLine();
        }
        return true;  // continue the loop
    }

    public boolean displayCart() {
        InputManager inputManager = InputManager.getInstance();
        String choice = "";
        System.out.println(user.getCart().toString());
        System.out.println();
        System.out.println("1. Proceed to checkout");
        System.out.println("2. Return to menu");
        System.out.println("3. Empty cart");
        choice = inputManager.nextLine();
        switch (choice) {
            case "1" -> {
                System.out.println("Proceeding to checkout...");
                makeCheckout();
            }
            case "2" -> System.out.println("Returning to menu...");
            case "3" -> emptyCart();
            default -> {
                System.out.println("RICKROLL");
                return false;  // continue the loop
            }
        }

        return true;  // continue the loop
    }

    public boolean displayNotifications() {
        //TODO
        return true;  // this can be changed depending on the outcome of the TODO
    }

    public void displayMetrics() {
        System.out.println(user.getMetrics().toString());
    }
    public void filterProducts() {
        System.out.println("1. Filter by category");
        System.out.println("2. Order by price");
        System.out.println("3. Order by likes");
        System.out.println("4. Order by average rating");
        System.out.println("5. Return to menu");
        int choice = getUserInputAsInteger();
        switch (choice) {
            case 1:
                System.out.println("1. Books");
                System.out.println("2. Learning resources");
                System.out.println("3. Stationery");
                System.out.println("4. Electronics");
                System.out.println("5. Desktop accessories");
                System.out.println("6. Return to menu");
                int choice2 = getUserInputAsInteger();
                switch (choice2) {
                    case 1:
                        catalog.filterProductsByCategory(Category.BOOKS);
                        break;
                    case 2:
                        catalog.filterProductsByCategory(Category.LEARNING_RESOURCES);
                        break;
                    case 3:
                        catalog.filterProductsByCategory(Category.STATIONERY);
                        break;
                    case 4:
                        catalog.filterProductsByCategory(Category.ELECTRONICS);
                        break;
                    case 5:
                        catalog.filterProductsByCategory(Category.DESKTOP_ACCESSORIES);
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
                int choice3 = getUserInputAsInteger();
                switch (choice3) {
                    case 1:
                        catalog.orderProductsByPrice(true);
                        break;
                    case 2:
                        catalog.orderProductsByPrice(false);
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
                int choice4 = getUserInputAsInteger();
                switch (choice4) {
                    case 1:
                        catalog.orderProductsByLikes(true);
                        break;
                    case 2:
                        catalog.orderProductsByLikes(false);
                        break;
                    case 3:
                        System.out.println("Returning to menu...");
                        break;
                    default:
                        System.out.println("Invalid selection. Please try again.");
                        break;
                }
                break;
            case 4:
                System.out.println("1. Ascending");
                System.out.println("2. Descending");
                System.out.println("3. Return to menu");
                int choice5 = getUserInputAsInteger();
                switch (choice5) {
                    case 1:
                        catalog.orderProductsByAverageNote(true);
                        break;
                    case 2:
                        catalog.orderProductsByAverageNote(false);
                        break;
                    case 3:
                        System.out.println("Returning to menu...");
                        break;
                    default:
                        System.out.println("Invalid selection. Please try again.");
                        break;
                }
        }
    }
    public boolean displayCatalog() {
        line();
        System.out.println("CATALOG");
        catalog = new Catalog(database.getSellers());
        catalog.displayCatalog();
        line();

        boolean continueLoop = true;
        while (continueLoop) {
            System.out.println("1. Get a product or display current product we are pointing at");
            System.out.println("2. Get a seller's infos and products");
            System.out.println("3. Filter products");
            System.out.println("4. Return to menu");

            int choice = getUserInputAsInteger();

            switch (choice) {
                case 1:
                    searchAndDisplayProduct();
                    break;
                case 2:
                    displaySellerInfo();
                    break;
                case 3:
                    filterProducts();
                    break;
                case 4:
                    System.out.println("Returning to menu...");
                    continueLoop = false;
                    break;
                default:
                    System.out.println("Invalid selection. Please try again.");
                    break;
            }
        }
        return true;
    }

    private void searchAndDisplayProduct() {
        if (searchProduct(catalog)) {
            System.out.println(pointedProduct);
            interactWithProduct();
        } else {
            System.out.println("Product not found. Please try again.");
        }
    }

    private void displaySellerInfo() {
        System.out.println("Enter the name of the seller you want to check out:");
        String id = InputManager.getInstance().nextLine();
        Seller seller = catalog.searchSellerByName(id);
        if (seller == null) {
            System.out.println("Seller not found.");
        } else {
            System.out.println(seller);
        }
    }
    public void addProductToCart(Product product) {
        System.out.println("How many of it do you want?");
        InputManager inputManager = InputManager.getInstance();
        int quantity = parseInt(inputManager.nextLine());
        if (quantity > product.getQuantity()) {
            System.out.println("Not enough products in stock");
            return;
        }
        user.getCart().addProduct(product, quantity);
        product.setQuantity(product.getQuantity() - quantity);
        System.out.println("Product added to cart");
    }
    public void addProductToWishlist(Product product) {
        if (user.getWishList().contains(product)) {
            System.out.println("Product already in wishlist");
            return;
        }
        product.setLikes(product.getLikes() + 1);
        System.out.println("Product added to wishlist");
        user.getMetrics().setLikesGiven(user.getMetrics().getLikesGiven() + 1);
        user.addToWishList(product);
    }
    public boolean searchProduct(Catalog catalog) {
        boolean continueLoop = true;
        InputManager im = InputManager.getInstance();
        while (continueLoop) {
            System.out.println("Enter the name of the product you want to search:");
            String title = im.nextLine();
            pointedProduct = catalog.searchProductByName(title);
            continueLoop = pointedProduct == null;
        }
        return true;  // continue the loop
    }
    private void interactWithProduct() {
        boolean continueInteraction = true;
        while (continueInteraction) {
            System.out.println("\n1. Add product to cart");
            System.out.println("2. Like the product (add it to the wishlist)");
            System.out.println("3. Make an evaluation");
            System.out.println("4. Return to catalog");

            int choice = getUserInputAsInteger();

            switch (choice) {
                case 1:
                    addProductToCart(pointedProduct);
                    break;
                case 2:
                    addProductToWishlist(pointedProduct);
                    break;
                case 3:
                    addEvaluationToProduct(pointedProduct);
                    break;
                case 4:
                    continueInteraction = false;
                    break;
                default:
                    System.out.println("Invalid selection. Please try again.");
                    break;
            }
        }
    }
    public boolean displayWishList() {
        InputManager inputManager = InputManager.getInstance();
        System.out.println("WISHLIST");
        boolean continueLoop = true;
        while (continueLoop) {
            System.out.println(user.wishListToString());
            System.out.println("0. Return to menu");
            System.out.println("Type the number of the product you want to add to the cart:");
            int choice = parseInt(inputManager.nextLine());
            if (choice < 0 || choice > user.getWishList().size()) {
                System.out.println("Invalid choice");
            }
            else if (choice == 0) {
                System.out.println("Returning to menu...");
                continueLoop = false;
            }
            else {
                addProductToCart(user.getWishList().get(choice-1));
            }
        }
        return true;
    }
    public void addEvaluationToProduct(Product product) {
        InputManager inputManager = InputManager.getInstance();
        System.out.println("Enter a comment:");
        String comment = inputManager.nextLine();
        System.out.println("Enter a rating between 0 and 5:");
        float rating = Float.parseFloat(inputManager.nextLine());
        product.addEvaluation(new Evaluation(comment, rating, user));
        user.getMetrics().setEvaluationsMade(user.getMetrics().getEvaluationsMade() + 1);
        user.getMetrics().updateAverageNoteGiven(rating);
    }
    public void makeCheckout() {
        InputManager im = InputManager.getInstance();
        System.out.println("Do you want to use your registered infos for this order? (y/n)");
        String choice = im.nextLine();
        if (user.getCard() == null) {
            System.out.println("You don't have a credit card registered");
            System.out.println("Enter your credit card number:");
            String cardNumber = im.nextLine();
            System.out.println("Enter your credit card expiration date:");
            String expirationDate = im.nextLine();
            System.out.println("Enter your credit card owner's first name:");
            String ownerName = im.nextLine();
            System.out.println("Enter your credit card owner's last name:");
            String ownerLastName = im.nextLine();
            user.setCard(new CreditCard(cardNumber, ownerName, ownerLastName, expirationDate));
        }
        if (Objects.equals(choice, "y")) {
            generateOrders();
            System.out.println("Order successful!");
        }
        else {
            System.out.println("Shipping address:");
            System.out.println("Enter your street:");
            String street = im.nextLine();
            System.out.println("Enter your city:");
            String city = im.nextLine();
            System.out.println("Enter your province:");
            String province = im.nextLine();
            System.out.println("Enter your country:");
            String country = im.nextLine();
            System.out.println("Enter your postal code:");
            String postalCode = im.nextLine();
            Address shippingAddress = new Address(street, city, province, country, postalCode);
            System.out.println("Enter your phone number:");
            String phoneNumber = im.nextLine();
            System.out.println("Enter your payment type (credit card/points):");
            String paymentType = im.nextLine();
            if (Objects.equals(paymentType, "credit card")) {
                System.out.println("Enter your credit card number:");
                String cardNumber = im.nextLine();
                System.out.println("Enter your credit card expiration date:");
                String expirationDate = im.nextLine();
                System.out.println("Enter your credit card owner's first name:");
                String ownerName = im.nextLine();
                System.out.println("Enter your credit card owner's last name:");
                String ownerLastName = im.nextLine();
                CreditCard creditCard = new CreditCard(cardNumber, ownerName, ownerLastName, expirationDate);
                generateOrders(creditCard, shippingAddress, phoneNumber, user.getCart().getProducts());
                System.out.println("Order successful!");

            }
            else if (Objects.equals(paymentType, "points")) { // 1 point for 2 cents
                if (user.getPoints() < user.getCart().getTotalPrice() * 50) {
                    System.out.println("Not enough points. You need " + (user.getCart().getTotalPrice() * 50 - user.getPoints()) + " more points to pay this order");
                    return;
                }
                generateOrders(paymentType, shippingAddress, phoneNumber, user.getCart().getProducts());
                System.out.println("Order successful!");
            }
        }
        database.updateOrderIDCounts();
        user.getCart().getProducts().clear();
    }
    private void generateOrders(String paymentType, Address shippingAddress, String phoneNumber, HashMap<Product, Integer> products) {
        HashMap<Seller, HashMap<Product, Integer>> splitCart = user.splitCartBeforeOrder();
        for (Seller seller : splitCart.keySet()) {
            HashMap<Product, Integer> sellerProducts = splitCart.get(seller);
            Seller databaseSeller = database.getSeller(seller);
            user.addOrder(new Order(user, paymentType, shippingAddress, phoneNumber, sellerProducts));
            databaseSeller.addOrder(new Order(user, paymentType, shippingAddress, phoneNumber, sellerProducts));
        }
    }
    private void generateOrders(CreditCard creditCard, Address shippingAddress, String phoneNumber, HashMap<Product, Integer> products) {
        HashMap<Seller, HashMap<Product, Integer>> splitCart = user.splitCartBeforeOrder();
        for (Seller seller : splitCart.keySet()) {
            HashMap<Product, Integer> sellerProducts = splitCart.get(seller);
            Seller databaseSeller = database.getSeller(seller);
            user.addOrder(new Order(user, "credit card", creditCard, shippingAddress, phoneNumber, sellerProducts));
            databaseSeller.addOrder(new Order(user, "credit card", creditCard, shippingAddress, phoneNumber, sellerProducts));
        }

    }
    private void generateOrders() {
        HashMap<Seller, HashMap<Product, Integer>> splitCart = user.splitCartBeforeOrder();
        for (Seller seller : splitCart.keySet()) {
            HashMap<Product, Integer> products = splitCart.get(seller);
            Seller databaseSeller = database.getSeller(seller);
            user.addOrder(new Order(user, "credit card", user.getCard(), products));
            databaseSeller.addOrder(new Order(user, "credit card", user.getCard(), products));
        }
    }


    public void emptyCart() {
        InputManager im = InputManager.getInstance();
        System.out.println("Are you sure you want to empty the cart? (y/n)");
        String input = im.nextLine();
        boolean continueLoop = true;
        while (continueLoop) {
            if (Objects.equals(input, "y")) {
                user.getCart().getProducts().clear();
                System.out.println("Cart emptied");
                continueLoop = false;
            }
            else if (Objects.equals(input, "n")) {
                System.out.println("Returning to menu...");
                continueLoop = false;
            }
            else {
                System.out.println("Invalid input");
            }
        }
    }
    private int getUserInputAsInteger() {
        while (true) {
            try {
                return Integer.parseInt(InputManager.getInstance().nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }
}
