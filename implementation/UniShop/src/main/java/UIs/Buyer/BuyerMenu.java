package UIs.Buyer;

import BackEndUtility.Catalog;
import BackEndUtility.DataBase;
import BackEndUtility.InputManager;
import UIs.Menu;
import UIs.UIUtilities;
import Users.*;
import UtilityObjects.Address;
import UtilityObjects.CreditCard;
import UtilityObjects.Notification;
import BackEndUtility.Category;
import BackEndUtility.OrderState;
import productClasses.Usages.Evaluation;
import productClasses.Usages.IssueQuery;
import productClasses.Usages.Order;
import productClasses.Product;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;


public class BuyerMenu extends Menu {
    private final Buyer user;
    private Product pointedProduct = null;
    private Seller pointedSeller = null;
    private Catalog catalog;
    // MENU
    public BuyerMenu(Buyer user, DataBase database) {
        super(user, database);
        this.user = user;
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

            System.out.println("7. Display Metrics");
            System.out.println("8. Log out");
            int choice = uiUtilities.getUserInputAsInteger();

            switch (choice) {
                case 1 -> continueLoop = displayProfile();
                case 2 -> continueLoop = displayOrderHistory();
                case 3 -> continueLoop = displayCart();
                case 4 -> continueLoop = displayWishList();
                case 5 -> continueLoop = displayCatalog();
                case 6 -> continueLoop = displayNotifications();
                case 7 -> continueLoop = displayMetrics();
                case 8 -> {
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
            System.out.println("Name: " + user.getId());
            System.out.println("Email: " + user.getEmail());
            System.out.println("Buying points: " + user.getPoints());
            System.out.println();
            System.out.println("METRICS");
            displayMetricsProfil();

            line();
            System.out.println();
            System.out.println("1. Modify profile");
            System.out.println("2. Return to menu");
            System.out.println("3. Delete account");
            int choice = uiUtilities.getUserInputAsInteger();

            switch (choice) {
                case 1:
                    System.out.println("Modifying profile...");
                    modifyProfile();
                    break;
                case 2:
                    System.out.println("Returning to menu...");
                    continueLoop = false;
                    break;
                case 3:
                    uiUtilities.deleteAccount();
                    return false;
                default:
                    System.out.println("Invalid selection. Please try again.");
                    return false;  // continue the loop
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
        int choice = uiUtilities.getUserInputAsInteger();
        switch (choice) {
            case 1 -> uiUtilities.modifyPersonalInfo(user);
            case 2 -> uiUtilities.modifyAddress();
            case 3 -> uiUtilities.modifyPassword();
            case 4 -> modifyPaymentInfo();
            case 5 -> System.out.println("Returning to menu...");
            default -> System.out.println("Invalid selection. Please try again.");
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

    public void cancelOrder(Buyer buyer, Order order) {
        order.setStatus(OrderState.CANCELLED);
        buyer.getMetrics().setOrdersMade((buyer.getMetrics().getOrdersMade() - 1));
        int productsCancelled = 0;
        for (Product p : order.getProducts().keySet()) {
            productsCancelled += order.getProducts().get(p);
        }
        buyer.getMetrics().setProductsBought((buyer.getMetrics().getProductsBought() - productsCancelled));
    }

    public void interactWithOrder(Order order) {

        System.out.println(order);
        System.out.println();
        System.out.println("1. Cancel order");
        System.out.println("2. Return order");
        System.out.println("3. Exchange order");
        System.out.println("4. Report a problem");
        System.out.println("5. Accept solution for the problem");
        System.out.println("6. Confirm order reception");
        System.out.println("7. Return to order history");

        int choice = uiUtilities.getUserInputAsInteger();

        switch (choice) {
            case 1: // cancel order
                System.out.println("Cancelling order...");

                // order can only be cancelled when status is 'in production'
                if (order.getStatus() != OrderState.IN_PRODUCTION) {
                    System.out.println("WARNING : Cannot cancel this order!");
                    if (order.getStatus() == OrderState.IN_DELIVERY)
                        System.out.println("Your order is out for delivery.");
                    if (order.getStatus() == OrderState.DELIVERED)
                        System.out.println("You have already received your order.");
                    if (order.getStatus() == OrderState.CANCELLED)
                        System.out.println("You have already cancelled your order.");
                    if (order.getStatus() == OrderState.RESHIPMENT_IN_DELIVERY || order.getStatus() == OrderState.RESHIPMENT_DELIVERED)
                        System.out.println("You cannot cancel a returning order.");
                    break;
                }

                cancelOrder(user, order);
                sendBuyerNotification(user, "Order cancelled", "Your order " + order.getId() + " has been cancelled!");
                sendSellerNotification(order.getProducts().keySet().iterator().next().getSeller(), "Order cancelled", "your order " + order.getId() + " has been cancelled!");
                System.out.println("Order cancelled");

                break;

            case 2: // return order
                System.out.println("Returning order...");

                // order can only be returned when status is 'delivered'
                if (order.getStatus() != OrderState.DELIVERED) {
                    System.out.println("WARNING : Cannot return this order!");
                    if (order.getStatus() == OrderState.IN_PRODUCTION || order.getStatus() == OrderState.IN_DELIVERY)
                        System.out.println("Your order is being processed.");
                    if (order.getStatus() == OrderState.CANCELLED)
                        System.out.println("Your order is cancelled.");
                    if (order.getStatus() == OrderState.RESHIPMENT_DELIVERED || order.getStatus() == OrderState.RESHIPMENT_IN_DELIVERY)
                        System.out.println("You have already requested a return.");
                    break;
                }

                // order can only be returned within 30 days since it's reception
                if (!check30DaysFromOrderReception(order)) {
                    System.out.println("WARNING : Cannot return this order! More than 30 days have passed since the receipt of your order.");
                    break;
                }

                // begin return process
                returnOrder(order);

                break;

            case 3: // exchange order
                System.out.println("Exchanging order...");

                // order can only be exchanged when status is 'delivered'
                if (order.getStatus() != OrderState.DELIVERED) {
                    System.out.println("WARNING : Cannot exchange this order!");
                    if (order.getStatus() == OrderState.IN_PRODUCTION || order.getStatus() == OrderState.IN_DELIVERY)
                        System.out.println("Your order is being processed.");
                    if (order.getStatus() == OrderState.CANCELLED)
                        System.out.println("Your order is cancelled.");
                    if (order.getStatus() == OrderState.RESHIPMENT_DELIVERED || order.getStatus() == OrderState.RESHIPMENT_IN_DELIVERY)
                        System.out.println("You have already requested an exchange.");
                    break;
                }

                // order can only be exchanged within 30 days since it's reception
                if (!check30DaysFromOrderReception(order)) {
                    System.out.println("WARNING : Cannot exchange this order! More than 30 days have passed since the receipt of your order.");
                    break;
                }
                // if the reshipment has not been received within 30 days of the request, cancel reshipment request
                if (check30DaysFromReshipmentRequest(order.getIssue())) {
                    order.setStatus(OrderState.RESHIPMENT_CANCELLED);

                    // send buyer and seller a notification
                    sendBuyerNotification(user, "Reshipment for order " + order.getId() + " is cancelled", "The reshipment package has not been received by the seller within 30 days of the reshipment request.");
                    sendSellerNotification(order.getProducts().keySet().iterator().next().getSeller(), "Issue " + order.getIssue().getId() + " cancelled", "The reshipment package has not been received within 30 days of the reshipment request.");
                }

                // TODO

                break;

            case 4: // report a problem

                order.reportProblem();
                for (Product product : order.getProducts().keySet()){
                    product.getSeller().addNotification(new Notification("there is a problem with a product", user.getFirstName() +" reported a problem with a " + product));
                }

                break;

            case 5:
                if (order.getIssue() == null){
                    System.out.println("You haven't reported a problem yet");
                }else{
                    order.getIssue().acceptSolution();
                    if (order.getIssue().acceptSolution()){
                        for (Product product : order.getProducts().keySet()){
                            product.getSeller().addNotification(new Notification("Reponse to proposed solution", order.getBuyer().getFirstName() + " has accepted your solution"));
                        }
                        break;

                    }else{
                        break;
                    }
                }
                break;


            case 6: // confirm order reception
                order.changeStatus(OrderState.DELIVERED);
                sendBuyerNotification(order.getBuyer(), "Order status changed", "your order " + order.getId() + " is now " + order.getStatus().toString().toLowerCase() + "!");
                System.out.println("Order confirmed");

            case 7: // return order history
                System.out.println("Returning to order history...");
                break;

            default: // invalid input
                System.out.println("Invalid selection. Please try again.");
                break;
        }
    }

    // Checks if the time between the order reception and the return request is <= 30 days
    private boolean check30DaysFromOrderReception(Order order) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        long diffInDays;

        try {
            Date reception = sdf.parse(order.getOrderDate());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate today = LocalDate.now();
            Date request = sdf.parse(today.format(formatter));

            long diffInMs = Math.abs(request.getTime() - reception.getTime());
            diffInDays = TimeUnit.DAYS.convert(diffInMs, TimeUnit.MILLISECONDS);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        if (diffInDays > 30) {
            return false;
        }

        return true;
    }

    // Returns an order
    private void returnOrder(Order order) {
        // display order products
        order.productsToString();

        // confirm return process
        System.out.println("Do you want to make a return? (y/n)");
        InputManager im = InputManager.getInstance();
        String returnChoice = "";
        while (!returnChoice.matches("[yn]")) {
            returnChoice = im.nextLine();
        }

        // begin return process
        if (Objects.equals(returnChoice, "y")) {
            // ask products to return
            HashMap<Product, Integer> returnProducts = askReturnProducts(order);

            // ask reason of return
            String reason = askReturnReason();

            // create return query
            IssueQuery returnQuery = new IssueQuery(reason);
            returnQuery.setSolutionDescription("Return");
            returnQuery.setReshipmentProducts(returnProducts);
            order.setIssue(returnQuery);

            // update order status
            order.setStatus(OrderState.RESHIPMENT_IN_DELIVERY);

            // confirm return i  creation
            System.out.println("You have successfully requested a return!");

            // send notification to seller
            sendSellerNotification(returnQuery.getReshipmentProducts().keySet().iterator().next().getSeller(), "Return requested: " + returnQuery.getId(), user.getId() + " requested a return.");

            // print reshipment label
            printReshipmentLabel(returnQuery.getReshipmentProducts().keySet().iterator().next().getSeller());

            // show next instructions
            System.out.println("Please follow the next steps:");
            System.out.println("1. Prepare the reshipment package with the given label.");
            System.out.println("2. Give the package to your closest post service.");
        }
    }

    // Gets Product object with product title from order products
    private Product getProductFromOrder(HashMap<Product, Integer> products, String title) {
        Product productFound = null;
        for (Map.Entry<Product, Integer> product : products.entrySet()) {
            if (Objects.equals(title, product.getKey().getTitle()))
                productFound = product.getKey();
        }
        return productFound;
    }

    // Asks products to return
    private HashMap<Product, Integer> askReturnProducts(Order order) {
        HashMap<Product, Integer> returnProducts = new HashMap<>(); // product list to return
        ArrayList<String> orderProducts = new ArrayList<>(); // list of order product titles
        for (Product product : order.getProducts().keySet()) {
            orderProducts.add(product.getTitle());
        }

        // ask product(s) to return
        boolean moreProdcutsToReturn = true;
        while (moreProdcutsToReturn) {
            System.out.println("Enter the name of the product you would like to return. The product must be listed above.");
            String productTitle = InputManager.getInstance().nextLine();

            // validate product title
            while (Objects.equals(productTitle, "") || !orderProducts.contains(productTitle)) {
                System.out.println("Please enter a valid product!");
                productTitle = InputManager.getInstance().nextLine();
            }

            // get Product object
            Product product = getProductFromOrder(order.getProducts(), productTitle);

            // check if product is already in return product list
            if (returnProducts.containsKey(product) && Objects.equals(returnProducts.get(product), order.getProducts().get(product))) {
                System.out.println("A maximum of " + order.getProducts().get(product) + product.getTitle() + " can be returned.");
                System.out.println("Would you like to return another item? (y/n)");
                String returnChoice = "";
                while (!returnChoice.matches("[yn]")) {
                    returnChoice = InputManager.getInstance().nextLine();
                }
                if (Objects.equals(returnChoice, "n")) break;
            }

            // if > 1 units available to return, ask quantity to return
            if (order.getProducts().get(product) > 1) {
                System.out.println("Enter the quantity of " + productTitle + " you would like to return.");
                int productQuantity = uiUtilities.getUserInputAsInteger();

                // validate product quantity
                while (productQuantity <= 0 || productQuantity > order.getProducts().get(product)) {
                    System.out.println("Please enter a valid quantity! You can only return up to " + order.getProducts().get(product) + " units.");
                    productQuantity = uiUtilities.getUserInputAsInteger();
                }

                // add product to return product list
                returnProducts.put(product, productQuantity);
            } else returnProducts.put(product, 1);

            // ask more products to return
            System.out.println("Would you like to return another item? (y/n)");
            String returnChoice = "";
            while (!returnChoice.matches("[yn]")) {
                returnChoice = InputManager.getInstance().nextLine();
            }
            if (Objects.equals(returnChoice, "n")) moreProdcutsToReturn = false;
        }

        // return list of products to return
        return returnProducts;
    }

    // Asks the reason of the return
    private String askReturnReason() {
        System.out.println("What is the reason of your return?");
        System.out.println("1. Wrong product(s) ordered");
        System.out.println("2. Wrong product(s) received");
        System.out.println("3. No longer need the product(s)");
        System.out.println("4. Not satisfied with the product(s)");
        System.out.println("5. Did not make this order");
        System.out.println("6. Other");
        int choice = uiUtilities.getUserInputAsInteger();
        String reason = "";
        switch (choice) {
            case 1 -> reason = "Wrong product(s) ordered";
            case 2 -> reason = "Wrong product(s) received";
            case 3 -> reason = "No longer need the product(s)";
            case 4 -> reason = "Not satisfied with the product(s)";
            case 5 -> reason = "Did not make this order";
            case 6 -> reason = "Other";
            default -> System.out.println("Invalid selection. Please try again.");
        }
        return reason;
    }

    // Prints reshipment label
    private void printReshipmentLabel(Seller seller) {
        System.out.println();
        System.out.println("Printing label...");
        System.out.println();
        System.out.println("--------------------------------------------------");
        System.out.println();
        System.out.println("FROM: " + user.getFirstName() + " " + user.getLastName());
        System.out.println("      " + user.getAddress().getAddressLine());
        System.out.println("      " + user.getAddress().getCity() + ", " + user.getAddress().getProvince() + ", " + user.getAddress().getCountry());
        System.out.println("      " + user.getAddress().getPostalCode());
        System.out.println();
        System.out.println("TO:   " + seller.getId());
        System.out.println("      " + seller.getAddress().getAddressLine());
        System.out.println("      " + seller.getAddress().getCity() + ", " + seller.getAddress().getProvince() + ", " + seller.getAddress().getCountry());
        System.out.println("      " + seller.getAddress().getPostalCode());
        System.out.println();
        System.out.println("--------------------------------------------------");
        System.out.println();
        System.out.println("Label printed!");
        System.out.println();
    }

    // Checks if the reshipment has been received within 30 days of the return request
    private boolean check30DaysFromReshipmentRequest(IssueQuery query) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        long diffInDays;

        try {
            Date request = sdf.parse(query.getRequestDate());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate today = LocalDate.now();
            Date reception = sdf.parse(today.format(formatter));

            long diffInMs = Math.abs(request.getTime() - reception.getTime());
            diffInDays = TimeUnit.DAYS.convert(diffInMs, TimeUnit.MILLISECONDS);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        if (diffInDays > 30) {
            return false;
        }

        return true;
    }

    // SHOPPING CART
    public boolean displayCart() {
        System.out.println(user.getCart().toString());
        System.out.println();
        System.out.println("1. Proceed to checkout");
        System.out.println("2. Remove an item from the cart");
        System.out.println("3. Empty cart");
        System.out.println("4. Return to menu");
        int choice = uiUtilities.getUserInputAsInteger();
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
    public void displayMetricsProfil() {

        System.out.println(user.getMetrics().getSelectedMetrics().get(0));
        System.out.println(user.getMetrics().getSelectedMetrics().get(1));
        System.out.println(user.getMetrics().getSelectedMetrics().get(2));

    }

    public boolean displayMetrics(){
        boolean continueLoop = true;
        while (continueLoop){
            System.out.println("All metrics available for " + user.getId() + " : ");
            System.out.println();
            System.out.println(user.getMetrics().AlltoString());
            System.out.println();
            System.out.println("1. Configure metrics to display in profile (3 max.)");
            System.out.println("2. Return to menu ");
            int choice = uiUtilities.getUserInputAsInteger();

            switch (choice){
                case 1:
                    user.getMetrics().configureMetrics();
                    break;
                case 2: continueLoop = false;
            }
        }
        return true;
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

            int choice = uiUtilities.getUserInputAsInteger();

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
        while (continueLoop) {
            System.out.println("Enter the name of the product you want to search:");
            String title = InputManager.getInstance().nextLine();
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
    // WISH LIST
    public boolean displayWishList() {
        System.out.println("WISHLIST");
        boolean continueLoop = true;
        while (continueLoop) {
            System.out.println(user.wishListToString());
            System.out.println("Type the number of the product you want to add to the cart, or 0 to return to menu:");
            int choice = uiUtilities.getUserInputAsInteger();
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
        String title = "You got a new evaluation!";
        String summary = this.user + " added a new comment on " + product.getTitle() + "!";
        product.getSeller().addNotification(new Notification(title, summary));
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
        int quantity = uiUtilities.getUserInputAsInteger();
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
        int quantity = uiUtilities.getUserInputAsInteger();
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
                database.generateAndAddOrders(user);
                System.out.println("Order successful!");
            }
            else if (Objects.equals(paymentType, "points")) { // 1 point for 2 cents
                if (user.getPoints() < user.getCart().getTotalPrice() * 50) {
                    System.out.println("Not enough points. You need " + (user.getCart().getTotalPrice() * 50 - user.getPoints()) + " more points to pay this order");
                    return;
                }
                user.removePoints((int) user.getCart().getTotalPrice() * 50);
                database.generateAndAddOrders(user, paymentType);
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
                database.generateAndAddOrders(user, creditCard, shippingAddress, phoneNumber);
                System.out.println("Order successful!");
            }
            else if (Objects.equals(paymentType, "points")) { // 1 point for 2 cents
                if (user.getPoints() < user.getCart().getTotalPrice() * 50) {
                    System.out.println("Not enough points. You need " + Math.ceil((user.getCart().getTotalPrice() * 50 - user.getPoints())) + " more points to pay this order");
                    return;
                }
                user.removePoints((int) user.getCart().getTotalPrice() * 50);
                database.generateAndAddOrders(user, paymentType, shippingAddress, phoneNumber);
                System.out.println("Order successful!");
            }

        }

        database.updateOrderIDCounts();
        user.getCart().getProducts().clear();
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
