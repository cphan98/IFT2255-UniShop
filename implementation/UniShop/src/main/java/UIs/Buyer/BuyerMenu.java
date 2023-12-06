package UIs.Buyer;

import BackEndUtility.Catalog;
import BackEndUtility.DataBase;
import BackEndUtility.InputManager;
import UIs.Menu;
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
    // ATTRIBUTES

    private final Buyer user;
    private Product pointedProduct = null;
    private Seller pointedSeller = null;
    private Catalog catalog;

    // CONSTRUCTOR

    public BuyerMenu(Buyer user, DataBase database) {
        super(user, database);
        this.user = user;
    }

    // MAIN MENU

    public boolean displayMenu() {
        boolean continueLoop = true;

        while (continueLoop) {
            line();
            System.out.println("Welcome to UniShop, " + user.getId() + "!");
            line();
            System.out.println();
            displayRanks();
            System.out.println();
            line();
            System.out.println("Please select an option:");

            System.out.println("1. Display Profile");
            System.out.println("2. Display Order History");
            System.out.println("3. Display Cart");
            System.out.println("4. Display Wishlist");
            System.out.println("5. Display Catalog");
            System.out.println("6. Display Buyers");
            System.out.println("7. Display Notifications");
            System.out.println("8. Display Metrics");
            System.out.println("9. Log out");
            int choice = uiUtilities.getUserInputAsInteger();

            switch (choice) {
                case 1 -> continueLoop = displayProfile();
                case 2 -> continueLoop = displayOrderHistory();
                case 3 -> continueLoop = displayCart();
                case 4 -> continueLoop = displayWishList();
                case 5 -> continueLoop = displayCatalog();
                case 6 -> continueLoop = displayBuyers();
                case 7 -> displayNotifications();
                case 8 -> displayMetrics();
                case 9 -> {
                    return false;  // Add this to handle log out
                }
                default -> {
                    System.out.println();
                    System.out.println("Invalid selection. Please try again.");
                }
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
            System.out.println("Buying points: " + user.getMetrics().getBuyPoints());
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
                // modify profile
                case 1:
                    System.out.println();
                    System.out.println("Modifying profile...");
                    modifyProfile();
                    break;

                // return to menu
                case 2:
                    System.out.println();
                    System.out.println("Returning to menu...");
                    continueLoop = false;
                    break;

                // delete account
                case 3:
                    System.out.println();
                    System.out.println("Deleting account...");
                    uiUtilities.deleteAccount();
                    return false;

                // invalid input
                default:
                    System.out.println();
                    System.out.println("Invalid selection. Please try again.");
                    return false;  // continue the loop
            }
        }
        return true;  // continue the loop
    }

    public void displayRanks() {
        System.out.println("Your rank is the " + user.getYourRank());
        int i = 0;
        for (Buyer buyer : user.getTop5ExpBuyers())
        {
            i++;
            System.out.println(i + ")" + "\t\t" + buyer.getId() + ": " + buyer.getPoints());
        }
    }

    public boolean displayBuyers() {
        line();
        System.out.println("BUYERS");
        line();
        ArrayList<Buyer> buyers = database.getBuyers();
        for (Buyer buyer : buyers) {
            if (user.getBuyersFollowed().contains(buyer)) {
                System.out.println("You are following this buyer!");
            }
            System.out.println("ID: " + buyer.getId());
            System.out.println("Name: " + buyer.getFirstName());
            System.out.println("Points: " + buyer.getPoints());
        }
        line();

        boolean continueLoop = true;
        while (continueLoop) {
            System.out.println("1. Get a buyer's profile");
            System.out.println("2. Filter buyers");
            System.out.println("3. Display your followers");
            System.out.println("4. Return to menu");
            int choice = uiUtilities.getUserInputAsInteger();

            switch (choice) {
                case 1 -> continueLoop = searchBuyer();
                case 2 -> continueLoop = filterBuyers();
                case 3 -> continueLoop = displayFollowers();
                case 4 -> {
                    System.out.println("Returning to menu...");
                    continueLoop = false;
                }
                default -> System.out.println("Invalid selection. Please try again.");
            }
        }
        return true;
    }

    private boolean searchBuyer() {
        line();
        ArrayList<Buyer> pointedBuyer = null;
        boolean continueLoop = true;
        while (continueLoop) {
            System.out.println("1. Search by ID");
            System.out.println("2. Search by name");
            System.out.println("3. Return to menu");
            int choice = uiUtilities.getUserInputAsInteger();

            switch (choice) {
                case 1 -> {
                    System.out.println("Enter the id of the buyer you want to view:");
                    String id = InputManager.getInstance().nextLine();
                    pointedBuyer = database.searchBuyerById(id);
                    if (pointedBuyer == null) {
                        System.out.println("Buyer not found.");
                    } else {
                        System.out.println(pointedBuyer);
                        while (true) {
                            System.out.println("Enter the desired buyer to view their profile:");
                            int i = 0;
                            for (Buyer buyer : pointedBuyer) {
                                i++;
                                System.out.println(i + ") " + buyer.getId());
                            }
                            int index = uiUtilities.getUserInputAsInteger();
                            if (index > 0 && index <= pointedBuyer.size()) {
                                interactWithBuyer(pointedBuyer.get(index-1));
                                break;
                            } else {
                                System.out.println("Invalid selection. Please try again.");
                            }
                        }

                    }
                }

                case 2 -> {
                    System.out.println("Enter the name of the buyer you want to view:");
                    String name = InputManager.getInstance().nextLine();
                    pointedBuyer = database.searchBuyerByName(name);
                    if (pointedBuyer == null) {
                        System.out.println("Buyer not found.");
                    } else {
                        System.out.println(pointedBuyer);
                        while (true) {
                            System.out.println("Enter the desired buyer to view their profile:");
                            int i = 0;
                            for (Buyer buyer : pointedBuyer) {
                                i++;
                                System.out.println(i + ") " + buyer.getId());
                            }
                            int index = uiUtilities.getUserInputAsInteger();
                            if (index > 0 && index <= pointedBuyer.size()) {
                                interactWithBuyer(pointedBuyer.get(index-1));
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
        return true;
    }

    public void interactWithBuyer(Buyer pointedBuyer) {
        if (user.getBuyersFollowed().contains(pointedBuyer)) {
            System.out.println("1. Unfollow this buyer");
        } else {
            System.out.println("1. Follow this buyer");
        }
        System.out.println("2. View Profile");
        System.out.println("3. Return to menu");
        int choice = uiUtilities.getUserInputAsInteger();
        switch (choice) {
            case 1 -> user.toggleBuyerToFollowing(pointedBuyer);
            case 2 -> displayBuyersProfile(pointedBuyer);
            case 3 -> {
                System.out.println("Returning to menu...");
            }
            default -> System.out.println("Invalid selection. Please try again.");
        }
    }

    public boolean filterBuyers() {
        System.out.println("1. Sort by name");
        System.out.println("2. Sort by ID");
        System.out.println("3. Sort by followers");
        System.out.println("4. Sort by experience points");
        System.out.println("5. Return to menu");
        int choice = uiUtilities.getUserInputAsInteger();
        switch (choice) {
            case 1:
                System.out.println("1. Ascending");
                System.out.println("2. Descending");
                System.out.println("3. Return to menu");
                int choice1 = uiUtilities.getUserInputAsInteger();
                switch (choice1) {
                    case 1 -> database.sortBuyer(true, "name");
                    case 2 -> database.sortBuyer(false, "name");
                    case 3 -> System.out.println("Returning to menu...");
                    default -> System.out.println("Invalid selection. Please try again.");
                }

            case 2:
                System.out.println("1. Ascending");
                System.out.println("2. Descending");
                System.out.println("3. Return to menu");
                int choice2 = uiUtilities.getUserInputAsInteger();
                switch (choice2) {
                    case 1 -> database.sortBuyer(true, "ID");
                    case 2 -> database.sortBuyer(false, "ID");
                    case 3 -> System.out.println("Returning to menu...");
                    default -> System.out.println("Invalid selection. Please try again.");
                }
            case 3:
                System.out.println("1. Ascending");
                System.out.println("2. Descending");
                System.out.println("3. Return to menu");
                int choice3 = uiUtilities.getUserInputAsInteger();
                switch (choice3) {
                    case 1 -> database.sortBuyer(true, "followers");
                    case 2 -> database.sortBuyer(false, "followers");
                    case 3 -> System.out.println("Returning to menu...");
                    default -> System.out.println("Invalid selection. Please try again.");
                }
            case 4:
                System.out.println("1. Ascending");
                System.out.println("2. Descending");
                System.out.println("3. Return to menu");
                int choice4 = uiUtilities.getUserInputAsInteger();
                switch (choice4) {
                    case 1 -> database.sortBuyer(true, "xp");
                    case 2 -> database.sortBuyer(false, "xp");
                    case 3 -> System.out.println("Returning to menu...");
                    default -> System.out.println("Invalid selection. Please try again.");
                }
        }
        return true;
    }

    private void displayBuyersProfile(Buyer buyer) {
        line();
        System.out.println("Buyer's name: " + buyer.getFirstName() + "\t" + buyer.getLastName());
        System.out.println("Buyer's ID: " + buyer.getId());
        System.out.println(buyer.getMetrics().toString());
    }

    public void modifyProfile() {
        System.out.println();
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
            case 5 -> {
                System.out.println();
                System.out.println("Returning to menu...");
            }
            default -> {
                System.out.println();
                System.out.println("Invalid selection. Please try again.");
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

    // ORDERS

    public void interactWithOrder(Order order) {
        // if the reshipment has not been received within 30 days of the request, cancel reshipment request
        if (check30DaysFromReshipmentRequest(order.getIssue())) {
            order.setStatus(OrderState.RESHIPMENT_CANCELLED);

            // update product quantities in database and seller's inventory
            // if issue's solution description is 'exchange', put back quantities of replacement products
            if (Objects.equals(order.getIssue().getSolutionDescription(), "Exchange")) {
                addIventoryQuantities(order.getIssue().getReplacementProduct(), order.getIssue().getReplacementProduct().entrySet().iterator().next().getKey().getSeller());
                addDatabaseProductQuantities(order.getIssue().getReplacementProduct());
            }

            // send notification to buyer and seller
            sendBuyerNotification(user, "Reshipment for order " + order.getId() + " is cancelled", "The reshipment package has not been received by the seller within 30 days of the reshipment request.");
            sendSellerNotification(order.getProducts().keySet().iterator().next().getSeller(), "Issue " + order.getIssue().getId() + " cancelled", "The reshipment package has not been received within 30 days of the reshipment request.");
        }

        System.out.println();
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
            // cancel order
            case 1:
                System.out.println();
                System.out.println("Cancelling order...");

                // order can only be cancelled when status is 'in production'
                if (order.getStatus() != OrderState.IN_PRODUCTION) {
                    System.out.println();
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

                // cancel order
                cancelOrder(user, order);

                // send notification to buyer and seller
                sendBuyerNotification(user, "Order cancelled", "Your order " + order.getId() + " has been cancelled!");
                sendSellerNotification(order.getProducts().keySet().iterator().next().getSeller(), "Order cancelled", "your order " + order.getId() + " has been cancelled!");

                // confirm cancellation
                System.out.println();
                System.out.println("Order cancelled");

                break;

            // return order
            case 2:
                System.out.println();
                System.out.println("Returning order...");

                // order can only be returned when status is 'delivered'
                if (order.getStatus() != OrderState.DELIVERED) {
                    System.out.println();
                    System.out.println("WARNING : Cannot return this order!");
                    if (order.getStatus() == OrderState.IN_PRODUCTION || order.getStatus() == OrderState.IN_DELIVERY)
                        System.out.println("Your order is being processed.");
                    if (order.getStatus() == OrderState.CANCELLED)
                        System.out.println("Your order is cancelled.");
                    if (order.getStatus() == OrderState.RESHIPMENT_DELIVERED || order.getStatus() == OrderState.RESHIPMENT_IN_DELIVERY)
                        System.out.println("You have already requested a return.");
                    if (order.getStatus() == OrderState.REPLACEMENT_IN_PRODUCTION || order.getStatus() == OrderState.REPLACEMENT_IN_DELIVERY || order.getStatus() == OrderState.REPLACEMENT_DELIVERED)
                        System.out.println("You cannot return this order.");
                    break;
                }

                // order can only be returned within 30 days since it's reception
                if (!check30DaysFromOrderReception(order)) {
                    System.out.println();
                    System.out.println("WARNING : Cannot return this order! More than 30 days have passed since the receipt of your order.");
                    break;
                }

                // begin return process
                returnOrder(order);

                break;

            // exchange order
            case 3:
                System.out.println();
                System.out.println("Exchanging order...");

                // order can only be exchanged when status is 'delivered'
                if (order.getStatus() != OrderState.DELIVERED) {
                    System.out.println();
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
                    System.out.println();
                    System.out.println("WARNING : Cannot exchange this order! More than 30 days have passed since the receipt of your order.");
                    break;
                }

                // begin exchange process
                exchangeOrder(order);

                break;

            // report a problem
            case 4:
                System.out.println();
                System.out.println("Reporting a problem...");
                order.reportProblem();
                for (Product product : order.getProducts().keySet()){
                    product.getSeller().addNotification(new Notification("there is a problem with a product", user.getFirstName() +" reported a problem with a " + product.getTitle()));
                }

                break;

            // confirm solution for the problem
            case 5:
                if (order.getIssue() == null){
                    System.out.println("You haven't reported a problem yet");
                }else{

                    if (order.getIssue().acceptSolution()) {
                        for (Product product : order.getProducts().keySet()){
                            product.getSeller().addNotification(new Notification("Response to proposed solution", order.getBuyer().getFirstName() + " has accepted your solution"));
                        }
                        if (Objects.equals(order.getIssue().getSolutionDescription(), "Reshipment of a replacement product")) {
                            break; //TODO: implement catalog of seller to ask for another product to replace the defect one
                        } else {
                            System.out.println("We'll repair it for you lol");
                            break;
                        }

                    }
                    break;
                }
                break;

            // confirm order reception
            case 6:
                // can only confirm when status is 'in delivery' or 'replacement in delivery'
                if (order.getStatus() != OrderState.IN_DELIVERY || order.getStatus() != OrderState.REPLACEMENT_IN_DELIVERY) {
                    System.out.println("WARNING : Cannot confirm this order!");
                    if (order.getStatus() == OrderState.DELIVERED || order.getStatus() == OrderState.REPLACEMENT_DELIVERED)
                        System.out.println("Your order has already been confirmed.");
                    if (order.getStatus() == OrderState.IN_PRODUCTION || order.getStatus() == OrderState.REPLACEMENT_IN_PRODUCTION)
                        System.out.println("Your order has not been shipped yet.");
                    if (order.getStatus() == OrderState.CANCELLED)
                        System.out.println("Your order is cancelled.");
                    if (order.getStatus() == OrderState.RESHIPMENT_CANCELLED || order.getStatus() == OrderState.RESHIPMENT_IN_DELIVERY || order.getStatus() == OrderState.RESHIPMENT_DELIVERED)
                        System.out.println("You cannot confirm a reshipment.");
                    break;
                }

                // change status to 'delivered'
                order.setStatus(OrderState.DELIVERED);

                // send notification to buyer
                sendBuyerNotification(order.getBuyer(), "Order status changed", "your order " + order.getId() + " is now " + order.getStatus().toString().toLowerCase() + "!");

                // add points
                int buyPointsWon = 0;
                for (Product product : order.getProducts().keySet()) {
                    buyPointsWon += (int) (product.getPrice() * order.getProducts().get(product));
                }
                user.getMetrics().addBuyPoints(buyPointsWon);
                user.getMetrics().addExpPoints(10);
                System.out.println("You have won " + buyPointsWon + " buy points and 10 experience points by confirming this order!");

                // confirm order reception
                System.out.println("Order confirmed");

                break;

            // return to order history
            case 7:
                System.out.println("Returning to order history...");
                break;

            // invalid input
            default:
                System.out.println("Invalid selection. Please try again.");
                break;
        }
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
        while (!returnChoice.matches("[yn]")) returnChoice = im.nextLine();

        // begin return process
        if (Objects.equals(returnChoice, "y")) {
            // ask products to return
            HashMap<Product, Integer> returnProducts = askProducts(order, "return");

            // ask reason of return
            String reason = askReason("return");

            // create return query
            IssueQuery returnQuery = new IssueQuery(reason);
            returnQuery.setSolutionDescription("Return");
            returnQuery.setReshipmentProducts(returnProducts);
            order.setIssue(returnQuery);

            // update order status
            order.setStatus(OrderState.RESHIPMENT_IN_DELIVERY);

            // confirm return query creation
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

    // Asks products to return/exchange
    private HashMap<Product, Integer> askProducts(Order order, String returnOrExchange) {
        HashMap<Product, Integer> selectedProducts = new HashMap<>(); // product list to return/exchange
        ArrayList<String> orderProducts = new ArrayList<>(); // list of order product titles
        for (Product product : order.getProducts().keySet()) orderProducts.add(product.getTitle());

        // ask product(s) to return/exchange
        boolean moreProducts = true;
        while (moreProducts) {
            // ask title of product
            switch (returnOrExchange) {
                case "return" -> System.out.println("Enter the name of the product you would like to return. The product must be listed above.");
                case "exchange" -> System.out.println("Enter the name of the product you would like to exchange. The product must be listed above.");
            }
            String productTitle = InputManager.getInstance().nextLine();

            // validate product title
            while (Objects.equals(productTitle, "") || !orderProducts.contains(productTitle)) {
                System.out.println("Please enter a valid product!");
                productTitle = InputManager.getInstance().nextLine();
            }

            // get Product object
            Product product = getProductFromOrder(order.getProducts(), productTitle);

            // check if product is already in return product list
            if (selectedProducts.containsKey(product) && selectedProducts.get(product) >= order.getProducts().get(product)) {
                switch (returnOrExchange) {
                    case "return":
                        System.out.println("A maximum of " + order.getProducts().get(product) + " " + product.getTitle() + " can be returned.");
                        System.out.println("Would you like to return another item? (y/n)");
                    case "exchange":
                        System.out.println("A maximum of " + order.getProducts().get(product) + " " + product.getTitle() + " can be exchanged.");
                        System.out.println("Would you like to exchange another item? (y/n)");
                }
                String choice = "";
                while (!choice.matches("[yn]")) choice = InputManager.getInstance().nextLine();
                if (Objects.equals(choice, "n")) break;
            }

            // if > 1 units available to return, ask quantity to return
            if (order.getProducts().get(product) > 1) {
                // ask product quantity
                switch (returnOrExchange) {
                    case "return" -> System.out.println("Enter the quantity of " + productTitle + " you would like to return.");
                    case "exchange" -> System.out.println("Enter the quantity of " + productTitle + " you would like to exchange.");
                }
                int productQuantity = uiUtilities.getUserInputAsInteger();

                // validate product quantity
                while (productQuantity <= 0 || productQuantity > order.getProducts().get(product)) {
                    switch (returnOrExchange) {
                        case "return" -> System.out.println("Please enter a valid quantity! You can only return up to " + order.getProducts().get(product) + " units.");
                        case "exchange" -> System.out.println("Please enter a valid quantity! You can only exchange up to " + order.getProducts().get(product) + " units.");
                    }
                    productQuantity = uiUtilities.getUserInputAsInteger();
                }

                // add product to return product list
                selectedProducts.put(product, productQuantity);
            } else selectedProducts.put(product, 1);

            // ask more products to return
            System.out.println("Would you like to return another item? (y/n)");
            String choice = "";
            while (!choice.matches("[yn]")) choice = InputManager.getInstance().nextLine();
            if (Objects.equals(choice, "n")) moreProducts = false;
        }

        // return list of products to return
        return selectedProducts;
    }

    // Asks the reason of the return/exchange
    private String askReason(String returnOrExchange) {
        switch (returnOrExchange) {
            case "return" -> System.out.println("What is the reason of your return?");
            case "exchange" -> System.out.println("What is the reason of your exchange?");
        }
        System.out.println("1. Wrong product(s) ordered");
        System.out.println("2. Wrong product(s) received");
        switch (returnOrExchange) {
            case "return" :
                System.out.println("3. No longer need the product(s)");
                System.out.println("4. Not satisfied with the product(s)");
                System.out.println("5. Did not make this order");
                System.out.println("6. Other");
            case "exchange" :
                System.out.println("3. Not satisfied with the product(s)");
                System.out.println("4. Other");
        }

        int choice = uiUtilities.getUserInputAsInteger();
        String reason = "";
        if (returnOrExchange.equals("return")) {
            switch (choice) {
                case 1 -> reason = "Wrong product(s) ordered";
                case 2 -> reason = "Wrong product(s) received";
                case 3 -> reason = "No longer need the product(s)";
                case 4 -> reason = "Not satisfied with the product(s)";
                case 5 -> reason = "Did not make this order";
                case 6 -> reason = "Other";
                default -> System.out.println("Invalid selection. Please try again.");
            }
        } else if (returnOrExchange.equals("exchange")) {
            switch (choice) {
                case 1 -> reason = "Wrong product(s) ordered";
                case 2 -> reason = "Wrong product(s) received";
                case 3 -> reason = "Not satisfied with the product(s)";
                case 4 -> reason = "Other";
            }
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

    // Exchanges an order
    private void exchangeOrder(Order order) {
        // display order products
        order.productsToString();

        // confirm exchange process
        System.out.println("Do you want to make an exchange? (y/n)");
        InputManager im = InputManager.getInstance();
        String exchangeChoice = "";
        while (!exchangeChoice.matches("[yn]")) exchangeChoice = im.nextLine();

        // begin exchange process
        if (Objects.equals(exchangeChoice, "y")) {
            // ask products to exchange
            HashMap<Product, Integer> exchangeProducts = askProducts(order, "exchange");

            // ask reason of exchange
            String reason = askReason("exchange");

            // ask replacement products
            HashMap<Product, Integer> replacementProducts = askReplacementProducts(order);

            // create exchange query
            IssueQuery exchangeQuery = new IssueQuery(reason);
            exchangeQuery.setSolutionDescription("Exchange");
            exchangeQuery.setReshipmentProducts(exchangeProducts);
            exchangeQuery.setReplacementProducts(replacementProducts);
            order.setIssue(exchangeQuery);

            // pay or refund price/points difference
            float priceDiff = calculatePriceDiff(exchangeProducts, replacementProducts, order, order.getProducts().entrySet().iterator().next().getKey().getSeller());
            int pointsDiff = calculatePointsDiff(exchangeProducts, replacementProducts, order, order.getProducts().entrySet().iterator().next().getKey().getSeller());
            switch (order.getPaymentType()) {
                case "credit card" :
                    String priceAction = payPriceDiffOrRefund(priceDiff);
                    switch (priceAction) {
                        case "refund" :
                            refundPriceDiff(priceDiff, replacementProducts, order);
                            break;
                        case "pay" :
                            payPriceDiff(priceDiff, replacementProducts, order);
                            break;
                        default :
                            System.out.println("No payment due.");

                            // create order
                            Order exchangeOrder = new Order(user, "points", replacementProducts);
                            exchangeOrder.setPaymentInfo(order.getPaymentInfo());
                            exchangeOrder.setShippingAddress(order.getShippingAddress());
                            exchangeOrder.setPhoneNumber(order.getPhoneNumber());
                            exchangeOrder.setTotalCost(priceDiff);

                            // add exchange order to issue
                            order.getIssue().setReplacementOrder(exchangeOrder);

                            // add order to buyer's and seller's order history
                            user.addOrder(exchangeOrder);
                            Seller seller = replacementProducts.entrySet().iterator().next().getKey().getSeller();
                            seller.addOrder(exchangeOrder);

                            // add order to database
                            database.addOrder(exchangeOrder);

                            // update product quantities in database and seller's inventory
                            removeDatabaseProductQuantities(replacementProducts);
                            removeInventoryQuantities(replacementProducts, seller);

                            // send notification to buyer and seller
                            sendBuyerNotification(user, "Order Status Update", "Your order " + exchangeOrder.getId() + " is now " + exchangeOrder.getStatus().toString().toLowerCase() + "!");
                            sendSellerNotification(seller, "New Order", "You have a new order: " + exchangeOrder.getId());
                    }
                    break;
                case "points" :
                    String pointsAction = payPointsDiffOrRefund(pointsDiff);
                    switch (pointsAction) {
                        case "refund" :
                            refundPointsDiff(pointsDiff, priceDiff, replacementProducts, order);
                            break;
                        case "pay" :
                            payPointsDiff(pointsDiff, priceDiff, replacementProducts, order);
                            break;
                        default :
                            System.out.println("No payment due.");

                            // create order
                            Order exchangeOrder = new Order(user, "points", replacementProducts);
                            exchangeOrder.setPaymentInfo(order.getPaymentInfo());
                            exchangeOrder.setShippingAddress(order.getShippingAddress());
                            exchangeOrder.setPhoneNumber(order.getPhoneNumber());
                            exchangeOrder.setTotalCost(priceDiff);

                            // add exchange order to issue
                            order.getIssue().setReplacementOrder(exchangeOrder);

                            // add order to buyer's and seller's order history
                            user.addOrder(exchangeOrder);
                            Seller seller = replacementProducts.entrySet().iterator().next().getKey().getSeller();
                            seller.addOrder(exchangeOrder);

                            // add order to database
                            database.addOrder(exchangeOrder);

                            // update product quantities in database and seller's inventory
                            removeDatabaseProductQuantities(replacementProducts);
                            removeInventoryQuantities(replacementProducts, seller);

                            // send notification to buyer and seller
                            sendBuyerNotification(user, "Order Status Update", "Your order " + exchangeOrder.getId() + " is now " + exchangeOrder.getStatus().toString().toLowerCase() + "!");
                            sendSellerNotification(seller, "New Order", "You have a new order: " + exchangeOrder.getId());
                    }
                    break;
            }

            // update order status
            order.setStatus(OrderState.RESHIPMENT_IN_DELIVERY);

            // confirm exchange query creation
            System.out.println("You have successfully requested an exchange!");

            // send notification to seller
            sendSellerNotification(exchangeQuery.getReshipmentProducts().keySet().iterator().next().getSeller(), "Return requested: " + exchangeQuery.getId(), user.getId() + " requested an exchange.");

            // print reshipment label
            printReshipmentLabel(exchangeQuery.getReshipmentProducts().keySet().iterator().next().getSeller());

            // show next instructions
            System.out.println("Please follow the next steps:");
            System.out.println("1. Prepare the reshipment package with the given label.");
            System.out.println("2. Give the package to your closest post service.");
        }
    }

    // Asks replacement products
    private HashMap<Product, Integer> askReplacementProducts(Order order) {
        HashMap<Product, Integer> replacementProducts = new HashMap<>(); // list of replacement products
        Seller seller = order.getProducts().entrySet().iterator().next().getKey().getSeller(); // seller of the order
        ArrayList<Product> sellerProducts = seller.getProducts(); // seller's products
        ArrayList<String> sellerProductTitles = new ArrayList<>(); // list of titles of seller's products
        for (Product product : sellerProducts) sellerProductTitles.add(product.getTitle());

        // display seller products
        System.out.println("Available replacement products");
        seller.productsToString();

        // ask replacement products
        boolean moreProducts = true;
        while (moreProducts) {
            // ask title of replacement product
            System.out.println("Please enter the name of the desired replacement product.");
            String productTitle = InputManager.getInstance().nextLine();

            // validate replacement product title
            while (Objects.equals(productTitle, "") || !sellerProductTitles.contains(productTitle)) {
                System.out.println("Please enter a valid product!");
                productTitle = InputManager.getInstance().nextLine();
            }

            // get replacement Product object
            Product product = getProductFromSeller(sellerProducts, productTitle);

            // get replacement product's index from seller's product list
            int index = sellerProducts.indexOf(product);

            // check if product is already in return product list
            if (replacementProducts.containsKey(product) && replacementProducts.get(product) >= sellerProducts.get(index).getQuantity()) {
                System.out.println("A maximum of " + sellerProducts.get(index).getQuantity() + " " + product.getTitle() + " can be selected.");
                System.out.println("Would you like to select another item? (y/n)");
                String choice = "";
                while (!choice.matches("[yn]")) choice = InputManager.getInstance().nextLine();
                if (Objects.equals(choice, "n")) break;
            }

            // if > 1 units can be selected, ask quantity of replacement product
            if (sellerProducts.get(index).getQuantity() > 1) {
                // ask replacement product quantity
                System.out.println("Enter the quantity of " + productTitle + ".");
                int productQuantity = uiUtilities.getUserInputAsInteger();

                // validate product quantity
                while (productQuantity <= 0 || productQuantity > sellerProducts.get(index).getQuantity()) {
                    System.out.println("Please enter a valid quantity! You can only select up to " + sellerProducts.get(index).getQuantity() + " units.");
                    productQuantity = uiUtilities.getUserInputAsInteger();
                }

                // add product to replacement products list
                replacementProducts.put(product, productQuantity);
            } else replacementProducts.put(product, 1);

            // as more replacement products
            System.out.println("Would you like to select another item? (y/n)");
            String choice = "";
            while (!choice.matches("[yn]")) choice = InputManager.getInstance().nextLine();
            if (Objects.equals(choice, "n")) moreProducts = false;
        }

        // return list of replacement products
        return replacementProducts;
    }

    // Gets Product object with product title from seller's list of products
    private Product getProductFromSeller(ArrayList<Product> products, String title) {
        Product productFound = null;
        for (Product product : products) {
            if (Objects.equals(product.getTitle(), title)) productFound = product;
        }
        return productFound;
    }

    // Determines if the buyer should pay the price difference or get a refund
    private String payPriceDiffOrRefund(float priceDiff) {
        // refund > replacement
        if (priceDiff < 0) return "refund";

        // refund < replacement
        if (priceDiff > 0) return "pay";

        // refund = replacement
        return "nothing";
    }

    // Determines if the buyer should pay the points difference or get a refund
    private String payPointsDiffOrRefund(int pointsDiff) {
        // refund > replacement
        if (pointsDiff < 0) return "refund";

        // refund < replacement
        if (pointsDiff > 0) return "pay";

        // refund = replacement
        return "nothing";
    }

    // Pays price difference
    private void payPriceDiff(float priceDiff, HashMap<Product, Integer> replacementProducts, Order order) {
        // (remove money)
        System.out.println("Payment in process...");

        // confirm payment
        System.out.println("Payment confirmed!");

        // create order
        Order exchangeOrder = new Order(user, "credit card", replacementProducts);
        exchangeOrder.setPaymentInfo(order.getPaymentInfo());
        exchangeOrder.setShippingAddress(order.getShippingAddress());
        exchangeOrder.setPhoneNumber(order.getPhoneNumber());
        exchangeOrder.setTotalCost(priceDiff);

        // add exchange order to issue
        order.getIssue().setReplacementOrder(exchangeOrder);

        // add order to buyer's and seller's order history
        user.addOrder(exchangeOrder);
        Seller seller = replacementProducts.entrySet().iterator().next().getKey().getSeller();
        seller.addOrder(exchangeOrder);

        // add order to database
        database.addOrder(exchangeOrder);

        // update product quantities in database and seller's inventory
        removeDatabaseProductQuantities(replacementProducts);
        removeInventoryQuantities(replacementProducts, seller);

        // send notification to buyer and seller
        sendBuyerNotification(user, "Order Status Update", "Your order " + exchangeOrder.getId() + " is now " + exchangeOrder.getStatus().toString().toLowerCase() + "!");
        sendSellerNotification(seller, "New Order", "You have a new order: " + exchangeOrder.getId());
    }

    // Pays points difference
    private void payPointsDiff(int pointsDiff, float priceDiff, HashMap<Product, Integer> replacementProducts, Order order) {
        // if buyer does not have enough points, pay with credit card
        if (user.getPoints() < pointsDiff) {
            System.out.println("Not enough points. You need " + (pointsDiff - user.getPoints()) + " more points to pay this order");

            // ask buyer to use registered credit card
            System.out.println("Do you want to use your registered credit card for this order? (y/n)");
            String choice = "";
            InputManager im = InputManager.getInstance();
            while (!choice.matches("[yn]")) choice = im.nextLine();

            // if buyer does not have registered credit card, ask to enter credit card info
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

            // if buyer has registered credit card
            if (Objects.equals(choice, "y")) {
                payPriceDiff(priceDiff, replacementProducts, order);
                return;
            }
        }

        // remove points
        System.out.println("Payment in process...");
        user.removePoints(pointsDiff);

        // confirm payment
        System.out.println("Payment confirmed!");

        // create order
        Order exchangeOrder = new Order(user, "points", replacementProducts);
        exchangeOrder.setPaymentInfo(order.getPaymentInfo());
        exchangeOrder.setShippingAddress(order.getShippingAddress());
        exchangeOrder.setPhoneNumber(order.getPhoneNumber());
        exchangeOrder.setTotalCost(priceDiff);

        // add exchange order to issue
        order.getIssue().setReplacementOrder(exchangeOrder);

        // add order to buyer's and seller's order history
        user.addOrder(exchangeOrder);
        Seller seller = replacementProducts.entrySet().iterator().next().getKey().getSeller();
        seller.addOrder(exchangeOrder);

        // add order to database
        database.addOrder(exchangeOrder);

        // update product quantities in database and seller's inventory
        removeDatabaseProductQuantities(replacementProducts);
        removeInventoryQuantities(replacementProducts, seller);

        // send notification to buyer and seller
        sendBuyerNotification(user, "Order Status Update", "Your order " + exchangeOrder.getId() + " is now " + exchangeOrder.getStatus().toString().toLowerCase() + "!");
        sendSellerNotification(seller, "New Order", "You have a new order: " + exchangeOrder.getId());
    }

    // Refunds price difference
    private void refundPriceDiff(float priceDiff, HashMap<Product, Integer> replacementProducts, Order order) {
        // (refund to original credit card)
        System.out.println("Refund in process...");

        // create order
        Order exchangeOrder = new Order(user, "credit card", replacementProducts);
        exchangeOrder.setPaymentInfo(order.getPaymentInfo());
        exchangeOrder.setShippingAddress(order.getShippingAddress());
        exchangeOrder.setPhoneNumber(order.getPhoneNumber());
        exchangeOrder.setTotalCost(priceDiff);

        // add exchange order to issue
        order.getIssue().setReplacementOrder(exchangeOrder);

        // add order to buyer's and seller's order history
        user.addOrder(exchangeOrder);
        Seller seller = replacementProducts.entrySet().iterator().next().getKey().getSeller();
        seller.addOrder(exchangeOrder);

        // add order to database
        database.addOrder(exchangeOrder);

        // update product quantities in database and seller's inventory
        removeDatabaseProductQuantities(replacementProducts);
        removeInventoryQuantities(replacementProducts, replacementProducts.entrySet().iterator().next().getKey().getSeller());

        // send notification to buyer
        sendBuyerNotification(order.getBuyer(), "You've received a refund", "You've received a refund of " + Math.abs(priceDiff) + " from your exchange request " + order.getIssue().getId() + ".");
        sendSellerNotification(replacementProducts.entrySet().iterator().next().getKey().getSeller(), "New Order", "You have a new order: " + exchangeOrder.getId());

        // confirm refund
        System.out.println("Refund completed!");
    }

    // Refunds points difference
    private void refundPointsDiff(int pointsDiff, float priceDiff, HashMap<Product, Integer> replacementProducts, Order order) {
        // refund points
        System.out.println("Refund in process...");
        user.addPoints(Math.abs(pointsDiff));

        // create order
        Order exchangeOrder = new Order(user, "points", replacementProducts);
        exchangeOrder.setPaymentInfo(order.getPaymentInfo());
        exchangeOrder.setShippingAddress(order.getShippingAddress());
        exchangeOrder.setPhoneNumber(order.getPhoneNumber());
        exchangeOrder.setTotalCost(priceDiff);

        // add exchange order to issue
        order.getIssue().setReplacementOrder(exchangeOrder);

        // add order to buyer's and seller's order history
        user.addOrder(exchangeOrder);
        Seller seller = replacementProducts.entrySet().iterator().next().getKey().getSeller();
        seller.addOrder(exchangeOrder);

        // add order to database
        database.addOrder(exchangeOrder);

        // update product quantities in database and seller's inventory
        removeInventoryQuantities(replacementProducts, replacementProducts.entrySet().iterator().next().getKey().getSeller());
        removeDatabaseProductQuantities(replacementProducts);

        // send notification to buyer
        sendBuyerNotification(order.getBuyer(), "You've received a refund", "You've received a refund of " + Math.abs(pointsDiff) + " from your exchange request " + order.getIssue().getId() + ".");
        sendSellerNotification(replacementProducts.entrySet().iterator().next().getKey().getSeller(), "New Order", "You have a new order: " + exchangeOrder.getId());

        // confirme refund
        System.out.println("Refund completed!");
    }

    // Calculates price difference for order exchange
    private float calculatePriceDiff(HashMap<Product, Integer> exchangeProducts, HashMap<Product, Integer> replacementProducts, Order order, Seller seller) {
        float priceDiff = 0; // price difference to pay
        float refund = 0; // cost of products to reship
        float replacementCost = 0; // cost of replacement products
        HashMap<Product, Integer> orderProducts = order.getProducts(); // order's product list
        ArrayList<Product> sellerProducts = seller.getProducts(); // seller's product list

        // calculate cost of products to reship
        refund = calculateCostToRefund(exchangeProducts, orderProducts);

        // calculate cost of replacement products
        replacementCost = calculateReplacementCost(replacementProducts, sellerProducts);

        // calculate price difference
        priceDiff = replacementCost - refund;

        // return price difference
        return priceDiff;
    }

    // Calculates points difference for order exchange
    private int calculatePointsDiff(HashMap<Product, Integer> exchangeProducts, HashMap<Product, Integer> replacementProducts, Order order, Seller seller) {
        int pointsDiff = 0; // points difference to pay
        int refund = 0; // points for products to reship
        int replacementPoints = 0; // points for replacement products
        HashMap<Product, Integer> orderProducts = order.getProducts(); // order's product list
        ArrayList<Product> sellerProducts = seller.getProducts(); // seller's product list

        // calculate points for products to reship
        refund = calculatePointsToRefund(exchangeProducts, orderProducts);

        // calculate points for replacement products
        replacementPoints = calculateReplacementPoints(replacementProducts, sellerProducts);

        // calculate points difference
        pointsDiff = replacementPoints - refund;

        // return points difference
        return pointsDiff;
    }

    // Calculates cost to refund
    private float calculateCostToRefund(HashMap<Product, Integer> exchangeProducts, HashMap<Product, Integer> orderProducts) {
        float sum = 0; // sum to refund
        Set<Product> orderProductsList = orderProducts.keySet();

        // calculate sum
        for (Map.Entry<Product, Integer> exchangeProduct : exchangeProducts.entrySet()) {

            int quantity = exchangeProduct.getValue();
            float price = 0;

            // find the corresponding product in the order
            for (Product orderProduct : orderProductsList)
                if (Objects.equals(orderProduct, exchangeProduct.getKey())) price = orderProduct.getPrice();

            // add price to sum
            sum += quantity * price;
        }

        // return sum
        return sum;
    }

    // Calculates points to refund
    private int calculatePointsToRefund(HashMap<Product, Integer> exchangeProducts, HashMap<Product, Integer> orderProducts) {
        int sum = 0;
        Set<Product> orderProductsList = orderProducts.keySet();

        // calculate sum
        for (Map.Entry<Product, Integer> exchangeProduct : exchangeProducts.entrySet()) {

            int quantity = exchangeProduct.getValue();
            int points = 0;

            // find the corresponding product in the order
            for (Product orderProduct : orderProductsList)
                if (Objects.equals(orderProduct, exchangeProduct.getKey())) points = orderProduct.getBasePoints();

            // add price to sum
            sum += quantity * points;
        }

        // return sum
        return sum;
    }

    // Calculates replacement products' cost
    private float calculateReplacementCost(HashMap<Product, Integer> replacementProducts, ArrayList<Product> sellerProducts) {
        float sum = 0;

        // calculate sum
        for (Map.Entry<Product, Integer> replacementProduct : replacementProducts.entrySet()) {
            int quantity = replacementProduct.getValue();
            float price = 0;

            // find the corresponding product in the order
            for (Product sellerProduct : sellerProducts)
                if (Objects.equals(sellerProduct, replacementProduct.getKey())) price = sellerProduct.getPrice();

            // add price to sum
            sum += quantity * price;
        }

        // return sum
        return sum;
    }

    // Calculates replacement products' points
    private int calculateReplacementPoints(HashMap<Product, Integer> replacementProducts, ArrayList<Product> sellerProducts) {
        int sum = 0;

        // calculate sum
        for (Map.Entry<Product, Integer> replacementProduct : replacementProducts.entrySet()) {
            int quantity = replacementProduct.getValue();
            int points = 0;

            // find the corresponding product in the order
            for (Product sellerProduct : sellerProducts)
                if (Objects.equals(sellerProduct, replacementProduct.getKey())) points = sellerProduct.getBasePoints();

            // add price to sum
            sum += quantity * points;
        }

        // return sum
        return sum;
    }

    // Removes product quantities from database
    private void removeDatabaseProductQuantities(HashMap<Product, Integer> replacementProducts) {
        ArrayList<Product> databaseProducts = database.getProducts();
        for (Map.Entry<Product, Integer> replacementProduct : replacementProducts.entrySet()) {
            // find product in seller inventory
            for (Product product : databaseProducts) {
                if (Objects.equals(product, replacementProduct.getKey())) {
                    int index = databaseProducts.indexOf(product);
                    database.getProducts().get(index).setQuantity(product.getQuantity() - replacementProduct.getValue());
                }
            }
        }
    }

    // Removes product quantities from seller's inventory
    private void removeInventoryQuantities(HashMap<Product, Integer> replacementProducts, Seller seller) {
        ArrayList<Product> inventory = seller.getProducts();
        for (Map.Entry<Product, Integer> replacementProduct : replacementProducts.entrySet()) {
            // find product in seller inventory
            for (Product product : inventory) {
                if (Objects.equals(product, replacementProduct.getKey())) {
                    int index = inventory.indexOf(product);
                    seller.getProducts().get(index).setQuantity(product.getQuantity() - replacementProduct.getValue());
                }
            }
        }
    }

    // Puts back product quantities to database
    private void addDatabaseProductQuantities(HashMap<Product, Integer> replacementProducts) {
        ArrayList<Product> databaseProducts = database.getProducts();
        for (Map.Entry<Product, Integer> replacementProduct : replacementProducts.entrySet()) {
            // find product in seller inventory
            for (Product product : databaseProducts) {
                if (Objects.equals(product, replacementProduct.getKey())) {
                    int index = databaseProducts.indexOf(product);
                    database.getProducts().get(index).setQuantity(product.getQuantity() + replacementProduct.getValue());
                }
            }
        }
    }

    // Puts back product quantities to seller's inventory
    private void addIventoryQuantities(HashMap<Product, Integer> replacementProducts, Seller seller) {
        ArrayList<Product> inventory = seller.getProducts();
        for (Map.Entry<Product, Integer> replacementProduct : replacementProducts.entrySet()) {
            // find product in seller inventory
            for (Product product : inventory) {
                if (Objects.equals(product, replacementProduct.getKey())) {
                    int index = inventory.indexOf(product);
                    seller.getProducts().get(index).setQuantity(product.getQuantity() - replacementProduct.getValue());
                }
            }
        }
    }

    // CART

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
        if (user.getMetrics().getSelectedMetrics().isEmpty()) {
            System.out.println("No metrics selected");
            return;
        }
        for (String metrics : user.getMetrics().getSelectedMetrics()) {
            System.out.println(metrics);
        }
    }

    public void displayMetrics(){
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
                case 2 -> searchSeller();
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

    private void searchSeller() {
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
            interactWithSeller();
        }
    }

    private void interactWithSeller() {
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
        database.addEvaluationToProduct(pointedProduct,new Evaluation(comment, rating, user));
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
        System.out.println("You currently have " + user.getMetrics().getBuyPoints() + " points\n");
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
                if (user.getMetrics().getBuyPoints() < user.getCart().getTotalPrice() * 50) {
                    System.out.println("Not enough points. You need " + (user.getCart().getTotalPrice() * 50 - user.getMetrics().getBuyPoints()) + " more points to pay this order");
                    return;
                }
                user.getMetrics().removeBuyPoints((int) user.getCart().getTotalPrice() * 50);
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
                if (user.getMetrics().getBuyPoints() < user.getCart().getTotalPrice() * 50) {
                    System.out.println("Not enough points. You need " + Math.ceil((user.getCart().getTotalPrice() * 50 - user.getMetrics().getBuyPoints())) + " more points to pay this order");
                    return;
                }
                user.getMetrics().removeBuyPoints((int) user.getCart().getTotalPrice() * 50);
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
