package UIs.Seller.Controllers;

import BackEndUtility.DataBase;
import BackEndUtility.InputManager;
import BackEndUtility.OrderState;
import UIs.Seller.SellerMenu;
import Users.Seller;
import UtilityObjects.Notification;
import productClasses.Product;
import productClasses.Usages.Order;

import java.util.*;

public class OrderController extends SellerMenu {

    // ATTRIBUTES
    private Seller user;
    private DataBase database;

    // CONSTRUCTOR

    public OrderController(Seller user, DataBase database) {
        super(user, database);
    }

    // UTILITIES

    // order history page ---------------------------------------------------------------------------------------------

    @Override
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

    @Override
    public void interactWithOrder(Order order) {
        System.out.println(order);
        System.out.println();
        System.out.println("1. Prepare order");
        System.out.println("2. Handle problem");
        System.out.println("3. Confirm reshipment reception");
        System.out.println("4. Return to order history");

        int choice = uiUtilities.getUserInputAsInteger();
        switch (choice) {
            // prepare order
            case 1:
                System.out.println();
                System.out.println("Preparing order...");
                prepareOrder(order);
                break;

            // handle problem
            case 2:
                if (order.getIssue() == null) {
                    System.out.println("No query in process.");
                    break;
                }

                displayIssue(order);

                order.getBuyer().addNotification(new Notification(user.getId() + "Has send a solution to the problem",
                        "the seller thinks its pertinent to: " + order.getIssue().getSolutionDescription()));

                break;

            // confirm reshipment reception
            case 3:
                System.out.println();
                System.out.println("Confirm reshipment reception...");
                confirmReshipmentReception(order);
                break;

            // return to order history
            case 4:
                System.out.println();
                System.out.println("Returning to order history...");
                break;

            // invalid input
            default:
                System.out.println();
                System.out.println("Invalid selection. Please try again.");
        }
    }

    // prepare order --------------------------------------------------------------------------------------------------

    // Prepares order
    private void prepareOrder(Order order) {
        // order can only be prepared when order status is 'in delivery' or 'replacement in delivery'
        if (order.getStatus() == OrderState.IN_DELIVERY || order.getStatus() == OrderState.REPLACEMENT_IN_DELIVERY) {
            System.out.println();
            System.out.println("WARNING : Cannot prepare order");
            System.out.println("This order has already been prepared.");
        } else if (order.getStatus() == OrderState.IN_PRODUCTION || order.getStatus() == OrderState.REPLACEMENT_IN_PRODUCTION) {
            // print label
            printLabel(order);

            // ask shipping information
            InputManager im = InputManager.getInstance();
            System.out.println();
            System.out.println("Please enter shipping information.");
            System.out.println("Shipping company:");
            String company = im.nextLine();
            order.setShippingCompany(company);
            String number = "a";
            while (!number.matches("\\d+")) {
                System.out.println("Shipping number:");
                number = im.nextLine();
            }
            order.setShippingNumber(number);

            // update status
            order.getBuyer().getOrderHistory().get(order.getBuyer().getOrderHistory().indexOf(order)).setStatus(OrderState.IN_DELIVERY);

            // send notification to buyer
            sendBuyerNotification(order.getBuyer(), "Order status changed",
                    "your order " + order.getId() + " is now " + order.getStatus().toString().toLowerCase() + "!");

            // confirm order preparation
            System.out.println();
            System.out.println("Your order is ready to be shipped!");
        } else {
            System.out.println();
            System.out.println("WARNING : Cannot prepare order");
        }
    }

    // prints label for order
    private void printLabel(Order order) {
        System.out.println();
        System.out.println("Printing label...");
        System.out.println();
        System.out.println("--------------------------------------------------");
        System.out.println();
        System.out.println("FROM: " + user.getId());
        System.out.println("      " + user.getAddress().getAddressLine());
        System.out.println("      " + user.getAddress().getCity() + ", " + user.getAddress().getProvince() + ", "
                + user.getAddress().getCountry());
        System.out.println("      " + user.getAddress().getPostalCode());
        System.out.println();
        System.out.println("TO:   " + order.getBuyer().getFirstName() + order.getBuyer().getLastName());
        System.out.println("      " + order.getBuyer().getAddress().getAddressLine());
        System.out.println("      " + order.getBuyer().getAddress().getCity() + ", " + order.getBuyer().getAddress().getProvince()
                + ", " + order.getBuyer().getAddress().getCountry());
        System.out.println("      " + order.getBuyer().getAddress().getPostalCode());
        System.out.println();
        System.out.println("--------------------------------------------------");
        System.out.println();
        System.out.println("Label printed!");
    }

    // handle problem -------------------------------------------------------------------------------------------------

    private void displayIssue(Order order) {
        System.out.println("Issue ID: " + order.getIssue().getId());
        System.out.println("Description: " + order.getIssue().getIssueDescription());

        if (order.getIssue().getSolutionDescription() == null) {
            System.out.println("Solution: waiting for a solution...");
            order.getIssue().proposeSolution();
            order.getBuyer().addNotification(new Notification("Seller: " +  user.getId() + " has proposed a solution",
                    user.getId() + "thinks it's pertinent to: " + order.getIssue().getSolutionDescription()));
            return;
        } else {
            System.out.println("Solution: " + order.getIssue().getSolutionDescription());
        }

        if (order.getIssue().getReshipmentTrackingNum() == null) {
            System.out.println("Reshipment tracking #: N/A");
        } else {
            System.out.println("Reshipment tracking #: " + order.getIssue().getReshipmentTrackingNum());
        }

        if (!order.getIssue().getReshipmentReceived()) {
            System.out.println("Reshipment received: N/A");
        } else {
            System.out.println("Reshipment received: yes");
        }

        if (order.getIssue().getReplacementProduct() == null) {
            System.out.println("Replacement product: N/A");
        } else {
            System.out.println("Replacement product: " + order.getIssue().getReplacementProduct());
        }

        if (order.getIssue().getReplacementTrackingNum() == null) {
            System.out.println("Replacement tracking #: N/A");
        } else {
            System.out.println("Replacement tracking #: " + order.getIssue().getReplacementTrackingNum());
        }

        if (!order.getIssue().getReplacementReceived()) {
            System.out.println("Replacement received: N/A");
        } else {
            System.out.println("Replacement received: yes");
        }
    }

    // confirm reshipment reception -----------------------------------------------------------------------------------

    // Confirms reshipment reception
    private void confirmReshipmentReception(Order order) {
        // if no issues, nothing to do
        if (order.getIssue() == null) {
            System.out.println();
            System.out.println("No query in process.");
            return;
        }

        // reshipment reception can only be confirmed when status is 'reshipment in delivery'
        if (order.getStatus() != OrderState.RESHIPMENT_IN_DELIVERY) {
            System.out.println();
            System.out.println("You cannot confirm the reshipment reception.");
            return;
        }

        // update status
        order.getBuyer().getOrderHistory().get(order.getBuyer().getOrderHistory().indexOf(order)).setStatus(OrderState.RESHIPMENT_DELIVERED);
        order.getBuyer().getOrderHistory().get(order.getBuyer().getOrderHistory().indexOf(order)).getIssue().setReshipmentReceived(true);

        // send notification to buyer
        sendBuyerNotification(order.getBuyer(), "Order status changed",
                "Your order " + order.getId() + " is now " + order.getStatus().toString().toLowerCase() + "!");

        // if issue's solution description is 'return', refund buyer
        if (Objects.equals(order.getIssue().getSolutionDescription(), "Return")) refund(order);

        // put back reshipment products quantities in inventory
        addInventoryQuantities(order.getIssue().getReshipmentProducts());

        // confirm reshipment
        System.out.println("Reshipment confirmed!");

        // if issue's solution description is 'exchange', ask buyer to prepare order
        if (Objects.equals(order.getIssue().getSolutionDescription(), "Exchange")) {
            prepareReplacementOrder(order.getIssue().getReplacementOrder());
        }

        int buyPointsDeducted = 0;
        HashMap<Product, Integer> productsReturned = order.getIssue().getReshipmentProducts();
        for (Map.Entry<Product, Integer> product : productsReturned.entrySet()) {
            buyPointsDeducted += product.getKey().getBasePoints() * product.getValue();
        }
        order.getBuyer().getMetrics().removeBuyPoints(buyPointsDeducted);
        System.out.println("The buyer has been deducted " + buyPointsDeducted + " buy points.");
    }

    // Prepares replacement order
    private void prepareReplacementOrder(Order replacementOrder) {
        System.out.println();
        System.out.println("The buyer requested an exchange");
        System.out.println();
        System.out.println("Preparing replacement order...");

        // change replacement order status
        replacementOrder.getBuyer().getOrderHistory().get(replacementOrder.getBuyer().getOrderHistory().indexOf(replacementOrder))
                .setStatus(OrderState.REPLACEMENT_IN_PRODUCTION);

        // send notification to buyer
        sendBuyerNotification(replacementOrder.getBuyer(), "Order status changed",
                "your order " + replacementOrder.getId() + " is now " + replacementOrder.getStatus().toString().toLowerCase() + "!");

        // display replacement products
        System.out.println();
        System.out.println("Replacement products:");
        System.out.println(replacementOrder.productsToString());

        // print label
        printLabel(replacementOrder);

        // enter shipping information
        InputManager im = InputManager.getInstance();
        System.out.println();
        System.out.println("Please enter shipping information");
        System.out.println("Shipping company:");
        String company = im.nextLine();
        replacementOrder.setShippingCompany(company);
        String number = "a";
        while (!number.matches("\\d+")) {
            System.out.println("Shipping number:");
            number = im.nextLine();
        }

        // add shipping number to replacement order
        replacementOrder.getBuyer().getOrderHistory().get(replacementOrder.getBuyer().getOrderHistory().indexOf(replacementOrder))
                .setShippingNumber(number);

        // change replacement order status
        replacementOrder.getBuyer().getOrderHistory().get(replacementOrder.getBuyer().getOrderHistory().indexOf(replacementOrder))
                .setStatus(OrderState.REPLACEMENT_IN_DELIVERY);

        // send notification to buyer
        sendBuyerNotification(replacementOrder.getBuyer(), "Order status changed",
                "your order " + replacementOrder.getId() + " is now " + replacementOrder.getStatus().toString().toLowerCase() + "!");

        // confirm replacement order prepared
        System.out.println();
        System.out.println("Your order is ready to be shipped!");
    }

    // Refunds buyer
    private void refund(Order order) {
        HashMap<Product, Integer> returnProducts = order.getIssue().getReshipmentProducts(); // list of products to return
        Set<Product> orderProducts = order.getProducts().keySet(); // list of products from the order

        // refund according to original payment type: credit card or points
        if (Objects.equals(order.getPaymentType(), "credit card")) {
            float sum = 0; // sum to refund

            // calculate sum to refund
            for (Map.Entry<Product, Integer> returnProduct : returnProducts.entrySet()) {
                int quantity = returnProduct.getValue();
                float price = 0;

                // find the corresponding product in the order
                for (Product orderProduct : orderProducts)
                    if (Objects.equals(orderProduct, returnProduct.getKey())) price = orderProduct.getPrice();

                // add cost of returning products to sum
                sum += quantity * price;
            }

            // send notification to buyer
            sendBuyerNotification(order.getBuyer(), "You've received a refund",
                    "You've received a refund of " + sum + " from your return request " + order.getIssue().getId() + ".");
        } else {
            int sum = 0; // sum to refund

            // calculate points to refund
            for (Map.Entry<Product, Integer> returnProduct : returnProducts.entrySet()) {
                int quantity = returnProduct.getValue();
                float points = 0;

                // find the corresponding product in the order
                for (Product orderProduct : orderProducts) {
                    if (Objects.equals(orderProduct, returnProduct.getKey())) points = orderProduct.getBasePoints();
                }

                // add cost of returning products to sum
                sum += quantity * points;
            }

            // put back points to buyer's points
            database.getBuyers().get(database.getBuyers().indexOf(order.getBuyer())).getMetrics().addBuyPoints(sum);

            // send notification to buyer
            sendBuyerNotification(order.getBuyer(), "You've received a refund",
                    "You've received a refund of " + sum + " from your return request " + order.getIssue().getId() + ".");
        }

        // update product quantities in inventory
        addInventoryQuantities(returnProducts);

        // confirm refund
        System.out.println();
        System.out.println("Refund completed!");
    }

    // Adds product quantities from seller's inventory
    private void addInventoryQuantities(HashMap<Product, Integer> returnProducts) {
        ArrayList<Product> inventory = user.getProducts();
        for (Map.Entry<Product, Integer> returnProduct : returnProducts.entrySet()) {
            // find product in seller inventory
            for (Product product : inventory) {
                if (Objects.equals(product, returnProduct.getKey())) {
                    int index = inventory.indexOf(product);
                    user.getProducts().get(index).setQuantity(product.getQuantity() + returnProduct.getValue());
                }
            }
        }
    }
}
