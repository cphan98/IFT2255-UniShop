package UIs.Buyer.Controllers;

import BackEndUtility.DataBase;
import BackEndUtility.InputManager;
import BackEndUtility.OrderState;
import UIs.Buyer.BuyerMenu;
import UIs.UIUtilities;
import Users.Buyer;
import Users.Seller;
import UtilityObjects.CreditCard;
import UtilityObjects.Notification;
import UtilityObjects.NotificationSender;
import productClasses.Product;
import productClasses.Usages.IssueQuery;
import productClasses.Usages.Order;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class OrderController {

    // ATTRIBUTES

    private final Buyer user;
    private final DataBase database;
    private final UIUtilities uiUtilities;
    private final NotificationSender notificationSender = new NotificationSender();

    // CONSTRUCTOR

    public OrderController(Buyer user, DataBase database) {
        this.user = user;
        this.database = database;
        this.uiUtilities = new UIUtilities(database, user);
    }

    // UTILITIES

    // order history page ---------------------------------------------------------------------------------------------

    public boolean displayOrderHistory() {
        System.out.println();
        System.out.println("ORDER HISTORY");
        System.out.println(user.ordersMadeToString());
        System.out.println("Write the number of the order you want to see the details of, or write 0 to return to menu");
        int choice = uiUtilities.getUserInputAsInteger();
        if (choice == 0) {
            System.out.println("Returning to menu...");
            return true;  // continue the loop
        }
        Order order = null;
        while (order == null) {
            if (choice > user.getOrderHistory().size()) {
                System.out.println("Invalid selection. Please try again.");
                choice = uiUtilities.getUserInputAsInteger();
            }
            order = user.getOrderHistory().get(choice - 1);
            if (order == null) {
                System.out.println("Invalid selection. Please try again.");
                choice = uiUtilities.getUserInputAsInteger();
            }
        }
        interactWithOrder(order);

        return true;  // continue the loop
    }

    // order page -----------------------------------------------------------------------------------------------------

    public void interactWithOrder(Order order) {
        // check if there is an issue
        if (order.getIssue() != null) {
            // if the reshipment has not been received within 30 days of the request, cancel reshipment request
            if (check30DaysFromReshipmentRequest(order.getIssue())) {
                user.getOrderHistory().get(user.getOrderHistory().indexOf(order)).setStatus(OrderState.RESHIPMENT_CANCELLED);

                // update product quantities in inventory
                // if issue's solution description is 'exchange', put back quantities of replacement products
                if (Objects.equals(order.getIssue().getSolutionDescription(), "Exchange")) {
                    addInventoryQuantities(order.getIssue().getReplacementProduct(),
                            order.getIssue().getReplacementProduct().entrySet().iterator().next().getKey().getSeller());
                }

                // send notification to buyer and seller
                notificationSender.sendBuyerNotification(user,
                        "Reshipment for order " + order.getId() + " is cancelled",
                        "The reshipment package has not been received by the seller within 30 days of the reshipment request.");
                notificationSender.sendSellerNotification(order.getProducts().keySet().iterator().next().getSeller(),
                        "Issue " + order.getIssue().getId() + " cancelled",
                        "The reshipment package has not been received within 30 days of the reshipment request.");
            }
        }

        // display options
        System.out.println();
        System.out.println(order);
        System.out.println("Please make a selection:");
        System.out.println("1. Cancel order");
        System.out.println("2. Return order");
        System.out.println("3. Exchange order");
        System.out.println("4. Report a problem");
        System.out.println("5. Accept solution for the problem");
        System.out.println("6. Confirm order reception");
        System.out.println("7. Return to order history");

        // process selection
        int choice = uiUtilities.getUserInputAsInteger();
        switch (choice) {
            // cancel order
            case 1:
                cancelOrder(order);
                break;
            // return order
            case 2:
                returnOrder(order);
                break;
            // exchange order
            case 3:
                exchangeOrder(order);
                break;
            // report a problem
            case 4:
                reportProblem(order);
                break;
            // confirm solution for the problem
            case 5:
                confirmSolution(order);
                break;
            // confirm order reception
            case 6:
                confirmOrderReception(order);
                break;
            // return to order history
            case 7:
                System.out.println();
                System.out.println("Returning to order history...");
                displayOrderHistory();
                break;
            // invalid input
            default:
                System.out.println();
                System.out.println("Invalid selection. Please try again.");
        }
    }

    // cancel order ---------------------------------------------------------------------------------------------------

    // Cancels an order
    private void cancelOrder(Order order) {
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
            return;
        }

        // update status
        user.getOrderHistory().get(user.getOrderHistory().indexOf(order)).setStatus(OrderState.CANCELLED);

        // update metrics
        user.getMetrics().setOrdersMade(user.getMetrics().getOrdersMade() - 1);
        int productsCancelled = 0;
        for (Product p : order.getProducts().keySet()) {
            productsCancelled += order.getProducts().get(p);
        }
        user.getMetrics().setProductsBought((user.getMetrics().getProductsBought() - productsCancelled));

        // send notification to buyer and seller
        notificationSender.sendBuyerNotification(user, "Order cancelled", "Your order " + order.getId() + " has been cancelled!");
        notificationSender.sendSellerNotification(order.getProducts().keySet().iterator().next().getSeller(), "Order cancelled",
                "your order " + order.getId() + " has been cancelled!");

        // confirm cancellation
        System.out.println();
        System.out.println("Order cancelled");
    }

    // return order ---------------------------------------------------------------------------------------------------

    // Returns an order
    private void returnOrder(Order order) {
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
            if (order.getStatus() == OrderState.REPLACEMENT_IN_PRODUCTION
                    || order.getStatus() == OrderState.REPLACEMENT_IN_DELIVERY
                    || order.getStatus() == OrderState.REPLACEMENT_DELIVERED)
                System.out.println("You cannot return this order.");
            return;
        }

        // order can only be returned within 30 days since it's reception
        if (!check30DaysFromOrderReception(order)) {
            System.out.println();
            System.out.println("WARNING : Cannot return this order! More than 30 days have passed since you received your order.");
            return;
        }

        // confirm return process
        System.out.println();
        System.out.println("Do you want to make a return? (y/n)");
        InputManager im = InputManager.getInstance();
        String returnChoice = "";
        while (!returnChoice.matches("[yn]")) returnChoice = im.nextLine();

        // begin return process
        if (Objects.equals(returnChoice, "y")) {
            // display order products
            System.out.println();
            System.out.println("Products eligible for return:");
            System.out.println(order.productsToString());

            // ask products to return
            HashMap<Product, Integer> returnProducts = askProducts(order, "return");

            // ask reason of return
            String reason = askReason("return");

            // create return query
            IssueQuery returnQuery = new IssueQuery(reason);
            returnQuery.setSolutionDescription("Return");
            returnQuery.setReshipmentProducts(returnProducts);

            // add issue query to buyer's order
            user.getOrderHistory().get(user.getOrderHistory().indexOf(order)).setIssue(returnQuery);

            // update order status
            user.getOrderHistory().get(user.getOrderHistory().indexOf(order)).setStatus(OrderState.RESHIPMENT_IN_DELIVERY);

            // confirm return query creation
            System.out.println("You have successfully requested a return!");

            // send notification to seller
            notificationSender.sendSellerNotification(returnQuery.getReshipmentProducts().keySet().iterator().next().getSeller(),
                    "Return requested: " + returnQuery.getId(), user.getId() + " requested a return.");

            // print reshipment label
            printReshipmentLabel(returnQuery.getReshipmentProducts().keySet().iterator().next().getSeller());

            // show next instructions
            System.out.println();
            System.out.println("Please follow the next steps:");
            System.out.println("1. Prepare the reshipment package with the given label.");
            System.out.println("2. Give the package to your closest post service.");
        }
    }

    // exchange order -------------------------------------------------------------------------------------------------

    // Exchanges an order
    private void exchangeOrder(Order order) {
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
            return;
        }

        // order can only be exchanged within 30 days since it's reception
        if (!check30DaysFromOrderReception(order)) {
            System.out.println();
            System.out.println("WARNING : Cannot exchange this order! More than 30 days have passed since you received your order.");
            return;
        }

        // display order products
        System.out.println();
        System.out.println("Products eligible for exchange:");
        System.out.println(order.productsToString());

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
            user.getOrderHistory().get(user.getOrderHistory().indexOf(order)).setIssue(exchangeQuery);

            // pay or refund price/points difference
            float priceDiff = calculatePriceDiff(exchangeProducts,
                    replacementProducts,
                    order,
                    order.getProducts().entrySet().iterator().next().getKey().getSeller());
            int pointsDiff = calculatePointsDiff(exchangeProducts,
                    replacementProducts,
                    order,
                    order.getProducts().entrySet().iterator().next().getKey().getSeller());
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
                            user.getOrderHistory().get(user.getOrderHistory().indexOf(order)).getIssue().setReplacementOrder(exchangeOrder);

                            // add order to buyer's and seller's order history
                            user.addOrder(exchangeOrder);
                            Seller seller = replacementProducts.entrySet().iterator().next().getKey().getSeller();
                            seller.addOrder(exchangeOrder);

                            // add order to database
                            database.addOrder(exchangeOrder);

                            // update product quantities in inventory
                            removeInventoryQuantities(replacementProducts, seller);

                            // send notification to buyer and seller
                            notificationSender.sendBuyerNotification(user, "Order Status Update",
                                    "Your order " + exchangeOrder.getId() + " is now "
                                            + exchangeOrder.getStatus().toString().toLowerCase() + "!");
                            notificationSender.sendSellerNotification(seller, "New Order",
                                    "You have a new order: " + exchangeOrder.getId());
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
                            user.getOrderHistory().get(user.getOrderHistory().indexOf(order)).getIssue().setReplacementOrder(exchangeOrder);

                            // add order to buyer's and seller's order history
                            user.addOrder(exchangeOrder);
                            Seller seller = replacementProducts.entrySet().iterator().next().getKey().getSeller();
                            seller.addOrder(exchangeOrder);

                            // add order to database
                            database.addOrder(exchangeOrder);

                            // update product quantities in inventory
                            removeInventoryQuantities(replacementProducts, seller);

                            // send notification to buyer and seller
                            notificationSender.sendBuyerNotification(user, "Order Status Update",
                                    "Your order " + exchangeOrder.getId() + " is now "
                                            + exchangeOrder.getStatus().toString().toLowerCase() + "!");
                            notificationSender.sendSellerNotification(seller, "New Order",
                                    "You have a new order: " + exchangeOrder.getId());
                    }

                    break;
            }

            // update order status
            user.getOrderHistory().get(user.getOrderHistory().indexOf(order)).setStatus(OrderState.RESHIPMENT_IN_DELIVERY);

            // confirm exchange query creation
            System.out.println();
            System.out.println("You have successfully requested an exchange!");

            // send notification to seller
            notificationSender.sendSellerNotification(exchangeQuery.getReshipmentProducts().keySet().iterator().next().getSeller(),
                    "Return requested: " + exchangeQuery.getId(), user.getId() + " requested an exchange.");

            // print reshipment label
            printReshipmentLabel(exchangeQuery.getReshipmentProducts().keySet().iterator().next().getSeller());

            // show next instructions
            System.out.println();
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
        System.out.println();
        System.out.println("Available replacement products:");
        System.out.println(seller.productsToString());

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
                    System.out.println("Please enter a valid quantity! You can only select up to "
                            + sellerProducts.get(index).getQuantity() + " units.");
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
        user.getOrderHistory().get(user.getOrderHistory().indexOf(order)).getIssue().setReplacementOrder(exchangeOrder);

        // add order to buyer's and seller's order history
        user.addOrder(exchangeOrder);
        Seller seller = replacementProducts.entrySet().iterator().next().getKey().getSeller();
        seller.addOrder(exchangeOrder);

        // add order to database
        database.addOrder(exchangeOrder);

        // update product quantities in inventory
        removeInventoryQuantities(replacementProducts, seller);

        // send notification to buyer and seller
        notificationSender.sendBuyerNotification(user, "Order Status Update",
                "Your order " + exchangeOrder.getId() + " is now " + exchangeOrder.getStatus().toString().toLowerCase() + "!");
        notificationSender.sendSellerNotification(seller, "New Order", "You have a new order: " + exchangeOrder.getId());
    }

    // Pays points difference
    private void payPointsDiff(int pointsDiff, float priceDiff, HashMap<Product, Integer> replacementProducts, Order order) {
        // if buyer does not have enough points, pay with credit card
        if (user.getMetrics().getBuyPoints() < pointsDiff) {
            System.out.println("Not enough points. You need " + (pointsDiff - user.getMetrics().getBuyPoints())
                    + " more points to pay this order");

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
        System.out.println();
        System.out.println("Payment in process...");
//        user.removePoints(pointsDiff);

        // confirm payment
        System.out.println();
        System.out.println("Payment confirmed!");

        // create order
        Order exchangeOrder = new Order(user, "points", replacementProducts);
        exchangeOrder.setPaymentInfo(order.getPaymentInfo());
        exchangeOrder.setShippingAddress(order.getShippingAddress());
        exchangeOrder.setPhoneNumber(order.getPhoneNumber());
        exchangeOrder.setTotalCost(priceDiff);

        // add exchange order to issue
        user.getOrderHistory().get(user.getOrderHistory().indexOf(order)).getIssue().setReplacementOrder(exchangeOrder);

        // add order to buyer's and seller's order history
        user.addOrder(exchangeOrder);
        Seller seller = replacementProducts.entrySet().iterator().next().getKey().getSeller();
        seller.addOrder(exchangeOrder);

        // add order to database
        database.addOrder(exchangeOrder);

        // update product quantities in inventory
        removeInventoryQuantities(replacementProducts, seller);

        // send notification to buyer and seller
        notificationSender.sendBuyerNotification(user, "Order Status Update",
                "Your order " + exchangeOrder.getId() + " is now " + exchangeOrder.getStatus().toString().toLowerCase() + "!");
        notificationSender.sendSellerNotification(seller, "New Order", "You have a new order: " + exchangeOrder.getId());
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
        user.getOrderHistory().get(user.getOrderHistory().indexOf(order)).getIssue().setReplacementOrder(exchangeOrder);

        // add order to buyer's and seller's order history
        user.addOrder(exchangeOrder);
        Seller seller = replacementProducts.entrySet().iterator().next().getKey().getSeller();
        seller.addOrder(exchangeOrder);

        // add order to database
        database.addOrder(exchangeOrder);

        // update product quantities in inventory
        removeInventoryQuantities(replacementProducts, replacementProducts.entrySet().iterator().next().getKey().getSeller());

        // send notification to buyer
        notificationSender.sendBuyerNotification(user, "You've received a refund",
                "You've received a refund of " + Math.abs(priceDiff) + " from your exchange request " + order.getIssue().getId() + ".");
        notificationSender.sendSellerNotification(replacementProducts.entrySet().iterator().next().getKey().getSeller(), "New Order",
                "You have a new order: " + exchangeOrder.getId());

        // confirm refund
        System.out.println("Refund completed!");
    }

    // Refunds points difference
    private void refundPointsDiff(int pointsDiff, float priceDiff, HashMap<Product, Integer> replacementProducts, Order order) {
        // refund points
        System.out.println("Refund in process...");
        user.getMetrics().addBuyPoints(Math.abs(pointsDiff));

        // create order
        Order exchangeOrder = new Order(user, "points", replacementProducts);
        exchangeOrder.setPaymentInfo(order.getPaymentInfo());
        exchangeOrder.setShippingAddress(order.getShippingAddress());
        exchangeOrder.setPhoneNumber(order.getPhoneNumber());
        exchangeOrder.setTotalCost(priceDiff);

        // add exchange order to issue
        user.getOrderHistory().get(user.getOrderHistory().indexOf(order)).getIssue().setReplacementOrder(exchangeOrder);

        // add order to buyer's and seller's order history
        user.addOrder(exchangeOrder);
        Seller seller = replacementProducts.entrySet().iterator().next().getKey().getSeller();
        seller.addOrder(exchangeOrder);

        // add order to database
        database.addOrder(exchangeOrder);

        // update product quantities in inventory
        removeInventoryQuantities(replacementProducts, replacementProducts.entrySet().iterator().next().getKey().getSeller());

        // send notification to buyer
        notificationSender.sendBuyerNotification(user, "You've received a refund",
                "You've received a refund of " + Math.abs(pointsDiff) + " from your exchange request " + order.getIssue().getId() + ".");
        notificationSender.sendSellerNotification(replacementProducts.entrySet().iterator().next().getKey().getSeller(),
                "New Order", "You have a new order: " + exchangeOrder.getId());

        // confirme refund
        System.out.println("Refund completed!");
    }

    // Calculates price difference for order exchange
    private float calculatePriceDiff(HashMap<Product, Integer> exchangeProducts,
                                     HashMap<Product, Integer> replacementProducts,
                                     Order order,
                                     Seller seller) {
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
    private int calculatePointsDiff(HashMap<Product, Integer> exchangeProducts,
                                    HashMap<Product, Integer> replacementProducts,
                                    Order order,
                                    Seller seller) {
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

    // Removes product quantities from seller's inventory
    private void removeInventoryQuantities(HashMap<Product, Integer> replacementProducts, Seller seller) {
        ArrayList<Product> inventory = seller.getProducts();
        for (Map.Entry<Product, Integer> replacementProduct : replacementProducts.entrySet()) {
            // find product in seller inventory
            for (Product product : inventory) {
                if (Objects.equals(product, replacementProduct.getKey())) {
                    int index = inventory.indexOf(product);
                    database.getSellers().get(database.getSellers().indexOf(seller)).getProducts().get(index).setQuantity(product.getQuantity()
                            - replacementProduct.getValue());
                }
            }
        }
    }

    // Puts back product quantities to seller's inventory
    private void addInventoryQuantities(HashMap<Product, Integer> replacementProducts, Seller seller) {
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

    // exchange/return utilities --------------------------------------------------------------------------------------

    // Gets Product object with product title from order products
    private Product getProductFromOrder(Order order, String title) {
        HashMap<Product, Integer> products = order.getProducts();
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
                case "return" ->
                        System.out.println("Enter the name of the product you would like to return. The product must be listed above.");
                case "exchange" ->
                        System.out.println("Enter the name of the product you would like to exchange. The product must be listed above.");
            }
            String productTitle = InputManager.getInstance().nextLine();

            // validate product title
            while (Objects.equals(productTitle, "") || !orderProducts.contains(productTitle)) {
                System.out.println("Please enter a valid product!");
                productTitle = InputManager.getInstance().nextLine();
            }

            // get Product object
            Product product = getProductFromOrder(order, productTitle);

            // check if product is already in return product list
            if (selectedProducts.containsKey(product) && selectedProducts.get(product) >= order.getProducts().get(product)) {
                switch (returnOrExchange) {
                    case "return":
                        System.out.println("A maximum of " + order.getProducts().get(product) + " " + product.getTitle()
                                + " can be returned.");
                        System.out.println("Would you like to return another item? (y/n)");
                    case "exchange":
                        System.out.println("A maximum of " + order.getProducts().get(product) + " " + product.getTitle()
                                + " can be exchanged.");
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
                        case "return" -> System.out.println("Please enter a valid quantity! You can only return up to "
                                + order.getProducts().get(product) + " units.");
                        case "exchange" -> System.out.println("Please enter a valid quantity! You can only exchange up to "
                                + order.getProducts().get(product) + " units.");
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
                break;
            case "exchange" :
                System.out.println("3. Not satisfied with the product(s)");
                System.out.println("4. Other");
                break;
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
        System.out.println("      " + user.getAddress().getCity() + ", " + user.getAddress().getProvince() + ", "
                + user.getAddress().getCountry());
        System.out.println("      " + user.getAddress().getPostalCode());
        System.out.println();
        System.out.println("TO:   " + seller.getId());
        System.out.println("      " + seller.getAddress().getAddressLine());
        System.out.println("      " + seller.getAddress().getCity() + ", " + seller.getAddress().getProvince() + ", "
                + seller.getAddress().getCountry());
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
        long diffInDays = 0;

        try {
            Date request = sdf.parse(query.getRequestDate());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate today = LocalDate.now();
            Date reception = sdf.parse(today.format(formatter));

            long diffInMs = Math.abs(request.getTime() - reception.getTime());
            diffInDays = TimeUnit.DAYS.convert(diffInMs, TimeUnit.MILLISECONDS);
        } catch (ParseException e) {
            System.out.println("something went wrong");
        }

        if (diffInDays > 30) {
            return false;
        }

        return true;
    }

    // issues ---------------------------------------------------------------------------------------------------------

    // Reports a problem
    private void reportProblem(Order order) {
        System.out.println();
        System.out.println("Reporting a problem...");
        user.getOrderHistory().get(user.getOrderHistory().indexOf(order)).setIssue(new IssueQuery("problem with a product"));
        database.createTicket(order.getIssue());
        for (Product product : order.getProducts().keySet()){
            product.getSeller().addNotification(new Notification("There is a problem with a product",
                    user.getFirstName() +" reported a problem with a " + product.getTitle()));
        }
    }

    // Accepts a solution for an issue
    private boolean acceptSolution(IssueQuery issue) {
        System.out.println();
        System.out.println("Accepting solution...");
        System.out.println();
        System.out.println("The seller has proposed:  " + issue.getSolutionDescription());
        System.out.println("1. Yes");
        System.out.println("2. No");
        int choice;
        do {
            System.out.println("Please enter a valid choice");
            choice = uiUtilities.getUserInputAsInteger();
        } while (choice != 1 && choice != 2);

        switch (choice){
            case 1:
                issue.setReplacementTrackingNum(database.makeReshipmentTrackingNum());
                System.out.println("Thanks for accepting the solution!");
                return true;
            case 2:
                System.out.println("Sorry :( ");
                return false;
        }
        return true;
    }

    // Confirms the solution for an issue
    private void confirmSolution(Order order) {
        System.out.println();
        if (order.getIssue() == null){
            System.out.println("You haven't reported a problem yet");
        }else{

            if (acceptSolution(order.getIssue())) {
                for (Product product : order.getProducts().keySet()){
                    product.getSeller().addNotification(new Notification("Response to proposed solution",
                            user.getFirstName() + " has accepted your solution"));
                }
                if (Objects.equals(order.getIssue().getSolutionDescription(), "Reshipment of a replacement product")) {
                    System.out.println("Do you want to be reshipped the same product ?");

                    System.out.println("1. Yes");
                    System.out.println("2. No");
                    int choiceRepProduct;
                    do {
                        System.out.println("Please enter a valid choice");
                        choiceRepProduct = uiUtilities.getUserInputAsInteger();
                    } while (choiceRepProduct != 1 && choiceRepProduct != 2);
                    switch (choiceRepProduct){
                        case 1 :
                            order.getIssue().setReplacementProducts(order.getProducts());
                            user.getOrderHistory().get(user.getOrderHistory().indexOf(order)).setStatus(OrderState.PENDING);

                            System.out.println("the seller will be notified and send you the product as soon as posible");
                            for (Product product : order.getProducts().keySet()){
                                product.getSeller().addNotification(new Notification(user.getId() + "Accepted the reshipment",
                                        "The buyer accepted to be reshipped a new " + product.getTitle()));
                            }
                            break;
                        case 2 :
                            user.getOrderHistory().get(user.getOrderHistory().indexOf(order)).setStatus(OrderState.REJECTED);
                            System.out.println("we're sorry you don't like the option, try getting the product fixed");
                            break;
                    }

                } else {
                    System.out.println("We'll repair it for you lol");
                }
            }
        }
    }

    // order reception ------------------------------------------------------------------------------------------------

    // Confirms order reception
    private void confirmOrderReception(Order order) {
        System.out.println();
        System.out.println("Confirming order reception...");
        // can only confirm when status is 'in delivery' or 'replacement in delivery'
        if (order.getStatus().equals(OrderState.IN_DELIVERY) || order.getStatus().equals(OrderState.REPLACEMENT_IN_DELIVERY)) {

            // update status
            user.getOrderHistory().get(user.getOrderHistory().indexOf(order)).setStatus(OrderState.DELIVERED);

            // send notification to buyer
            notificationSender.sendBuyerNotification(user, "Order status changed",
                    "your order " + order.getId() + " is now " + order.getStatus().toString().toLowerCase() + "!");

            // add points
            int buyPointsWon = 0;
            for (Product product : order.getProducts().keySet()) {
                buyPointsWon += (int) (product.getPrice() * order.getProducts().get(product));
            }
            user.getMetrics().addBuyPoints(buyPointsWon);
            user.getMetrics().addExpPoints(10);
            System.out.println();
            System.out.println("You have won " + buyPointsWon + " buy points and 10 experience points by confirming this order!");

            // confirm order reception
            System.out.println();
            System.out.println("Order confirmed");
            return;
        }

        System.out.println();
        System.out.println("WARNING : Cannot confirm this order!");
        if (order.getStatus() == OrderState.DELIVERED || order.getStatus() == OrderState.REPLACEMENT_DELIVERED)
            System.out.println("Your order has already been confirmed.");
        if (order.getStatus() == OrderState.IN_PRODUCTION || order.getStatus() == OrderState.REPLACEMENT_IN_PRODUCTION)
            System.out.println("Your order has not been shipped yet.");
        if (order.getStatus() == OrderState.CANCELLED)
            System.out.println("Your order is cancelled.");
        if (order.getStatus() == OrderState.RESHIPMENT_CANCELLED
                || order.getStatus() == OrderState.RESHIPMENT_IN_DELIVERY
                || order.getStatus() == OrderState.RESHIPMENT_DELIVERED)
            System.out.println("You cannot confirm a reshipment.");
    }

    // verification ---------------------------------------------------------------------------------------------------

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

        return diffInDays <= 30;
    }
}
