package UIs;

import LoginUtility.Catalog;
import LoginUtility.DataBase;
import Users.Address;
import Users.Buyer;
import Users.CreditCard;
import Users.Seller;
import otherUtility.Category;
import otherUtility.OrderState;
import products.Evaluation;
import products.Order;
import products.Product;

import java.util.HashMap;
import java.util.Objects;

public class BuyerMenu extends Menu {
    private final Buyer user;
    private Product pointedProduct = null;
    private Seller pointedSeller = null;
    private Catalog catalog;

    // MENU

    public BuyerMenu(Buyer user, DataBase database) {
        super(user);
        this.user = user;
        this.database = database;
    }

    public boolean displayMenu() {
        boolean continueLoop = true;

        while (continueLoop) {
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
            System.out.println("6. Display Notifications");
            System.out.println("7. Log out");
            int choice = getUserInputAsInteger();

            switch (choice) {
                case 1 -> continueLoop = displayProfile();
                case 2 -> continueLoop = displayOrderHistory();
                case 3 -> continueLoop = displayCart();
                case 4 -> continueLoop = displayWishList();
                case 5 -> continueLoop = displayCatalog();
                case 6 -> continueLoop = displayNotifications();
                case 7 -> {
                    return false;  // Add this to handle log out
                }
                default -> System.out.println("Invalid selection. Please try again.");
            }
        }
        return true;
    }
    // PROFILE
    public boolean displayProfile() {
        boolean continueLoop = true;
        while (continueLoop) {
            line();
            System.out.println("PROFILE");
            InputManager inputManager = InputManager.getInstance();
            System.out.println("Name: " + user.getId());
            System.out.println("Email: " + user.getEmail());
            System.out.println("Buying points: " + user.getPoints());
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
                case "1" -> {
                    System.out.println("Modifying profile...");
                    modifyProfile();
                }
                case "2" -> {
                    System.out.println("Returning to menu...");
                    continueLoop = false;
                }
                default -> {
                    System.out.println("Are you sure you want to delete your account? (y/n)");
                    String input = inputManager.nextLine();
                    boolean continueLoop2 = true;
                    while (continueLoop2) {
                        if (Objects.equals(input, "y")) {
                            System.out.println("Deleting account...");
                            database.removeUser(user);
                            continueLoop2 = false;
                        } else if (Objects.equals(input, "n")) {
                            System.out.println("Returning to menu...");
                            continueLoop2 = false;
                        } else {
                            System.out.println("Invalid input");
                        }
                    }
                    return false;  // continue the loop
                }
            }
        }
        return true;  // continue the loop
    }
    public void modifyProfile() {
        System.out.println("1. Modify personal info");
        System.out.println("2. Modify shipping address");
        System.out.println("3. Modify password");
        System.out.println("4. Modify payment info");
        System.out.println("5. Return to menu");
        int choice = getUserInputAsInteger();
        switch (choice) {
            case 1 -> modifyPersonalInfo();
            case 2 -> modifyShippingAddress();
            case 3 -> modifyPassword();
            case 4 -> modifyPaymentInfo();
            case 5 -> System.out.println("Returning to menu...");
            default -> System.out.println("Invalid selection. Please try again.");
        }
    }
    public void modifyPersonalInfo() {
        System.out.println("Enter your first name:");
        String firstName = InputManager.getInstance().nextLine();
        System.out.println("Enter your last name:");
        String lastName = InputManager.getInstance().nextLine();
        System.out.println("Enter your new id:");
        String id = InputManager.getInstance().nextLine();
        String email = "";
        while (!email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
            System.out.println("Please enter your email:");
            email = InputManager.getInstance().nextLine();
        }
        String phoneNumber = "a";
        while (!phoneNumber.matches("[0-9]+")) {
            System.out.println("Enter your phone number:");
            phoneNumber = InputManager.getInstance().nextLine();
        }
        user.setFirstName(firstName);
        user.setLastName(lastName);
        if (!database.validateNewUser(id, email)) {
            System.out.println("This id or email is already taken");
            System.out.println("Your other info are changed but your id and email were not changed");
            return;
        }
        user.setId(id);
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);
        System.out.println("Personal info modified");
    }
    public void modifyShippingAddress() {
        System.out.println("Enter your street name:");
        String street = InputManager.getInstance().nextLine();
        System.out.println("Enter your city:");
        String city = InputManager.getInstance().nextLine();
        System.out.println("Enter your province:");
        String province = InputManager.getInstance().nextLine();
        System.out.println("Enter your country:");
        String country = InputManager.getInstance().nextLine();
        System.out.println("Enter your postal code:");
        String postalCode = InputManager.getInstance().nextLine();
        Address shippingAddress = new Address(street, city, province, country, postalCode);
        user.setAddress(shippingAddress);
    }
    public void modifyPassword() {
        while (true) {
            System.out.println("Enter your current password:");
            String currentPassword = InputManager.getInstance().nextLine();
            if (Objects.equals(currentPassword, user.getPassword())) {
                System.out.println("Enter your new password:");
                String newPassword = InputManager.getInstance().nextLine();
                user.setPassword(newPassword);
                database.changePassword(user, newPassword);
                System.out.println("Password modified");
                break;
            } else {
                System.out.println("Wrong password");
            }
        }
    }
    public void modifyPaymentInfo() {
        System.out.println("Enter your credit card number:");
        String cardNumber = InputManager.getInstance().nextLine();
        System.out.println("Enter your credit card expiration date:");
        String expirationDate = InputManager.getInstance().nextLine();
        System.out.println("Enter your credit card owner's first name:");
        String ownerName = InputManager.getInstance().nextLine();
        System.out.println("Enter your credit card owner's last name:");
        String ownerLastName = InputManager.getInstance().nextLine();
        user.setCard(new CreditCard(cardNumber, ownerName, ownerLastName, expirationDate));
        System.out.println("Payment info modified");
    }
    public void interactWithOrder(Order order) {
        System.out.println(order);
        System.out.println();
        System.out.println("1. Cancel order");
        System.out.println("2. Report a problem");
        System.out.println("3. Confirm order reception");
        System.out.println("4. Return to order history");
        int choice = getUserInputAsInteger();
        switch (choice) {
            case 1:
                order.cancelOrder();
                System.out.println("Order cancelled");
                break;
            case 2:
                System.out.println("Reporting problem...");
                break;
            case 3:
                order.changeStatus(OrderState.DELIVERED);
                System.out.println("Order confirmed");
            case 4:
                System.out.println("Returning to order history...");
                break;
            default:
                System.out.println("Invalid selection. Please try again.");
                break;
        }
    }
    // SHOPPING CART
    public boolean displayCart() {
        System.out.println(user.getCart().toString());
        System.out.println();
        System.out.println("1. Proceed to checkout");
        System.out.println("2. Remove an item from the cart");
        System.out.println("3. Empty cart");
        System.out.println("4. Return to menu");
        int choice = getUserInputAsInteger();
        switch (choice) {
            case 1 -> {
                System.out.println("Proceeding to checkout...");
                if (user.getCart().getProducts().isEmpty()) {
                    System.out.println("Your cart is empty!");
                    return true;
                }
                makeCheckout();
            }
            case 2 -> removeProductFromCart();
            case 3 -> emptyCart();
            case 4 -> System.out.println("Returning to menu...");
            default -> System.out.println("Invalid selection. Returning to menu...");
        }
        return true;  // continue the loop
    }
    // METRICS
    public void displayMetrics() {
        System.out.println(user.getMetrics().toString());
    }
    // CATALOG
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

            int choice = getUserInputAsInteger();

            switch (choice) {
                case 1 -> searchAndDisplayProduct();
                case 2 -> displaySellerInfo();
                case 3 -> filterProducts();
                case 4 -> filterSellers();
                case 5 -> displayProductsLikedByFollowing();
                case 6 -> {
                    System.out.println("Returning to menu...");
                    continueLoop = false;
                }
                default -> System.out.println("Invalid selection. Please try again.");
            }
        }
        return true;
    }
    private void displayProductsLikedByFollowing() {
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
    private void searchAndDisplayProduct() {
        if (searchProduct(catalog)) {
            System.out.println(pointedProduct);
            interactWithProduct();
        } else {
            System.out.println("Product not found. Please try again.");
        }
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
            if (user.getWishList().contains(pointedProduct)) {
                System.out.println("2. Remove product from wishlist");
            } else {
                System.out.println("2. Add product to wishlist");
            }
            System.out.println("3. Make an evaluation");
            System.out.println("4. Interact with an evaluation");
            System.out.println("5. Return to catalog");

            int choice = getUserInputAsInteger();

            switch (choice) {
                case 1 -> addProductToCart(pointedProduct);
                case 2 -> user.toggleProductToWishList(pointedProduct);
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
    private void interactWithEvaluations() {
        System.out.println("Enter the number of the evaluation you want to interact with, or 0 to return:");
        int choice = getUserInputAsInteger();
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
            int choice2 = getUserInputAsInteger();
            switch (choice2) {
                case 1:
                    user.toggleEvaluationLike(pointedEvaluation);
                    break;
                case 2:
                    user.toggleBuyerToFollowing(pointedEvaluation.getAuthor());
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
    private void displaySellerInfo() {
        System.out.println("Enter the name of the seller you want to check out:");
        String id = InputManager.getInstance().nextLine();
        pointedSeller = catalog.searchSellerByName(id);
        if (pointedSeller == null) {
            System.out.println("Seller not found.");
        } else {
            System.out.println(pointedSeller);
        }
        interactWithSeller();
    }
    private void interactWithSeller() {
        if (user.getSellersFollowed().contains(pointedSeller)) {
            System.out.println("1. Unfollow this seller");
        } else {
            System.out.println("1. Follow this seller");
        }
        System.out.println("2. Return to catalog");
        int choice = getUserInputAsInteger();
        switch (choice) {
            case 1 -> user.toggleSellerToFollowing(pointedSeller);
            case 2 -> {
                System.out.println("Returning to catalog...");
                catalog.displayCatalog();
            }
            default -> System.out.println("Invalid selection. Please try again.");
        }
    }
    public void filterProducts() {
        System.out.println("1. Filter by category");
        System.out.println("2. Order by price");
        System.out.println("3. Order by likes");
        System.out.println("4. Order by average rating");
        System.out.println("5. Return to menu");
        int choice = getUserInputAsInteger();
        switch (choice) {
            case 1 -> {
                System.out.println("1. Books");
                System.out.println("2. Learning resources");
                System.out.println("3. Stationery");
                System.out.println("4. Electronics");
                System.out.println("5. Desktop accessories");
                System.out.println("6. Return to menu");
                int choice2 = getUserInputAsInteger();
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
                int choice3 = getUserInputAsInteger();
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
                int choice4 = getUserInputAsInteger();
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
                int choice5 = getUserInputAsInteger();
                switch (choice5) {
                    case 1 -> catalog.orderProducts(true, "averageNote");
                    case 2 -> catalog.orderProducts(false, "averageNote");
                    case 3 -> System.out.println("Returning to menu...");
                    default -> System.out.println("Invalid selection. Please try again.");
                }
            }
        }
    }
    public void filterSellers() {
        System.out.println("1. Filter by category");
        System.out.println("2. Order by likes");
        System.out.println("3. Order by average rating");
        System.out.println("4. Display only favorite sellers");
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
                int choice4 = getUserInputAsInteger();
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
                int choice5 = getUserInputAsInteger();
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
    // WISH LIST
    public boolean displayWishList() {
        System.out.println("WISHLIST");
        boolean continueLoop = true;
        while (continueLoop) {
            System.out.println(user.wishListToString());
            System.out.println("Type the number of the product you want to add to the cart, or 0 to return to menu:");
            int choice = getUserInputAsInteger();
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
    // EVALUATIONS
    public void addEvaluationToProduct(Product product) {
        InputManager inputManager = InputManager.getInstance();
        System.out.println("Enter a comment:");
        String comment = inputManager.nextLine();
        float rating = -1F;
        while (rating < 0 || rating > 5) {
            System.out.println("Enter a rating between 0 and 5:");
            rating = Float.parseFloat(inputManager.nextLine());
        }
        product.addEvaluation(new Evaluation(comment, rating, user));
    }
    // SHOPPING CART
    public void removeProductFromCart() {
        InputManager inputManager = InputManager.getInstance();
        System.out.println("Enter the name of the product you want to remove:");
        String title = inputManager.nextLine();
        Product product = user.getCart().searchProductByName(title);
        if (product == null) {
            System.out.println("Product not found");
            return;
        }
        System.out.println("How many of it do you want to remove?");
        int quantity = getUserInputAsInteger();
        if (quantity > product.getQuantity()) {
            System.out.println("Not enough products in cart");
            return;
        }
        user.getCart().removeProduct(product, quantity);
        product.setQuantity(product.getQuantity() + quantity);
        System.out.println("Product removed from cart");
    }
    public void addProductToCart(Product product) {
        System.out.println("How many of it do you want?");
        int quantity = getUserInputAsInteger();
        if (quantity > product.getQuantity()) {
            System.out.println("Not enough products in stock");
            return;
        }
        user.getCart().addProduct(product, quantity);
        product.setQuantity(product.getQuantity() - quantity);
        System.out.println("Product added to cart");
    }
    public void makeCheckout() {
        InputManager im = InputManager.getInstance();
        System.out.println("You currently have " + user.getPoints() + " points\n");
        System.out.println("Do you want to use your registered info for this order? (y/n)");
        String choice = "";
        while (!choice.matches("[yn]")) {
            choice = im.nextLine();
        }
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
            System.out.println("Enter your payment type (credit card/points):");
            String paymentType = "";
            while (!paymentType.matches("credit card|points")) {
                paymentType = im.nextLine();
            }
            if (Objects.equals(paymentType, "credit card")) {
                generateOrders();
                System.out.println("Order successful!");
            }
            else if (Objects.equals(paymentType, "points")) { // 1 point for 2 cents
                if (user.getPoints() < user.getCart().getTotalPrice() * 50) {
                    System.out.println("Not enough points. You need " + (user.getCart().getTotalPrice() * 50 - user.getPoints()) + " more points to pay this order");
                    return;
                }
                user.removePoints((int) user.getCart().getTotalPrice() * 50);
                generateOrders(paymentType);
                System.out.println("Order successful!");
            }
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
                generateOrders(creditCard, shippingAddress, phoneNumber);
                System.out.println("Order successful!");
            }
            else if (Objects.equals(paymentType, "points")) { // 1 point for 2 cents
                if (user.getPoints() < user.getCart().getTotalPrice() * 50) {
                    System.out.println("Not enough points. You need " + Math.ceil((user.getCart().getTotalPrice() * 50 - user.getPoints())) + " more points to pay this order");
                    return;
                }
                user.removePoints((int) user.getCart().getTotalPrice() * 50);
                generateOrders(paymentType, shippingAddress, phoneNumber);
                System.out.println("Order successful!");
            }
        }

        database.updateOrderIDCounts();
        user.getCart().getProducts().clear();
    }
    private void generateOrders(String paymentType, Address shippingAddress, String phoneNumber) {
        HashMap<Seller, HashMap<Product, Integer>> splitCart = user.splitCartBeforeOrder();
        for (Seller seller : splitCart.keySet()) {
            HashMap<Product, Integer> sellerProducts = splitCart.get(seller);
            database.addOrder(new Order(user, paymentType, shippingAddress, phoneNumber, sellerProducts));
        }
    }
    private void generateOrders(CreditCard creditCard, Address shippingAddress, String phoneNumber) {
        HashMap<Seller, HashMap<Product, Integer>> splitCart = user.splitCartBeforeOrder();
        for (Seller seller : splitCart.keySet()) {
            HashMap<Product, Integer> sellerProducts = splitCart.get(seller);
            database.addOrder(new Order(user, "credit card", creditCard, shippingAddress, phoneNumber, sellerProducts));
        }
    }
    private void generateOrders() {
        HashMap<Seller, HashMap<Product, Integer>> splitCart = user.splitCartBeforeOrder();
        for (Seller seller : splitCart.keySet()) {
            HashMap<Product, Integer> products = splitCart.get(seller);
            database.addOrder(new Order(user, "credit card", user.getCard(), products));
        }
    }
    private void generateOrders(String paymentType) {
        HashMap<Seller, HashMap<Product, Integer>> splitCart = user.splitCartBeforeOrder();
        for (Seller seller : splitCart.keySet()) {
            HashMap<Product, Integer> products = splitCart.get(seller);

            for(Product product : user.getCart().getProducts().keySet()){
                database.getSeller(seller).sellProduct(product, splitCart.size());
            }

            database.addOrder(new Order(user, paymentType, products));
        }
    }
    public void emptyCart() {
        InputManager im = InputManager.getInstance();
        System.out.println("Are you sure you want to empty the cart? (y/n)");
        String input = im.nextLine();
        boolean continueLoop = true;
        while (continueLoop) {
            if (Objects.equals(input, "y")) {
                user.getCart().getProducts().forEach((product, integer) -> product.setQuantity(product.getQuantity() + integer));
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
}
