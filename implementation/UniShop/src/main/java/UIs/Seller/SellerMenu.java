package UIs.Seller;

import BackEndUtility.DataBase;
import BackEndUtility.InputManager;
import UIs.Menu;
import Users.Seller;
import productClasses.Usages.Order;
import BackEndUtility.OrderState;
import productClasses.*;
import productClasses.Inheritances.*;

import java.util.*;

import static java.lang.Float.parseFloat;

public class SellerMenu extends Menu {
    private final Seller user;
    // MENU
    public SellerMenu(Seller user, DataBase database) {
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
            System.out.println("3. Display Inventory");
            int unreadNotifications = user.getNotifications().stream().filter(notification -> !notification.isRead()).toArray().length;
            System.out.println("4. Display Notifications" + (unreadNotifications == 0 ? "" : " (" + unreadNotifications  + " new)"));
            System.out.println("5. Display Metrics");
            System.out.println("6. Log out");
            int choice = uiUtilities.getUserInputAsInteger();


            switch (choice) {
                case 1:
                    continueLoop = displayProfile();
                    break;
                case 2:
                    continueLoop = displayOrderHistory();
                    break;
                case 3:
                    continueLoop = displayInventory();
                    break;
                case 4:
                    continueLoop = displayNotifications();
                    break;
                case 5:
                    continueLoop = displayMetrics();
                    break;
                case 6:
                    return false;  // Add this to handle log out
                default:
                    System.out.println("Invalid selection. Please try again.");
                    break;
            }
        }
        return true;
    }

    // PROFILE

    public boolean displayProfile() {
        boolean continueLoop = true;
        while (continueLoop) {
            System.out.println("PROFILE");
            System.out.println("Name: " + user.getId());
            System.out.println("Email: " + user.getEmail());
            System.out.println();
            System.out.println("METRICS");
            displayProfileMetrics();
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
        System.out.println("2. Modify address");
        System.out.println("3. Modify password");
        System.out.println("4. Return to menu");
        int choice = uiUtilities.getUserInputAsInteger();
        switch (choice) {
            case 1 -> uiUtilities.modifyPersonalInfo(user);
            case 2 -> uiUtilities.modifyAddress();
            case 3 -> uiUtilities.modifyPassword();
            case 4 -> System.out.println("Returning to menu...");
            default -> System.out.println("Invalid selection. Please try again.");

        }
    }

    // ORDERS

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
                if (order.getStatus() == OrderState.IN_DELIVERY) {
                    System.out.println("This order has already been prepared.");
                    break;
                } else if (order.getStatus() == OrderState.IN_PRODUCTION) {
                    printLabel(user, order);
                    InputManager im = InputManager.getInstance();
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

                    order.changeStatus(OrderState.IN_DELIVERY);
                    sendBuyerNotification(order.getBuyer(), "Order status changed", "your order " + order.getId() + " is now " + order.getStatus().toString().toLowerCase() + "!");
                    System.out.println("Your order is ready to be shipped!");
                    break;
                } else {
                    System.out.println("You cannot prepare this order.");
                }

            // handle problem
            case 2:
                if (order.getIssue() == null) {
                    System.out.println("No query in process.");
                    break;
                }

                displayIssue(order);

                break;

            // confirm reshipment reception
            case 3:
                // if no issues, nothing to do
                if (order.getIssue() == null) {
                    System.out.println("No query in process.");
                    break;
                }

                // reshipment reception can only be confirmed when status is 'reshipment in delivery'
                if (order.getStatus() != OrderState.RESHIPMENT_IN_DELIVERY) {
                    System.out.println("You cannot confirm the reshipment reception.");
                    break;
                }

                // update status
                order.setStatus(OrderState.RESHIPMENT_DELIVERED);
                order.getIssue().setReshipmentReceived(true);

                // send notification to buyer
                sendBuyerNotification(order.getBuyer(), "Order status changed", "Your order " + order.getId() + " is now " + order.getStatus().toString().toLowerCase() + "!");

                // if issue's solution description is 'return', refund buyer
                if (Objects.equals(order.getIssue().getSolutionDescription(), "Return")) refund(order);

                // put back reshipment products quantities in seller's inventory and database
                addDatabaseProductQuantities(order.getIssue().getReshipmentProducts());
                addInventoryQuantities(order.getIssue().getReshipmentProducts());

                // confirm reshipment
                System.out.println("Reshipment confirmed!");

                break;

            // return to order history
            case 4:
                System.out.println("Returning to order history...");
                break;

            // invalid input
            default:
                System.out.println("Invalid selection. Please try again.");
                break;
        }
    }

    public void printLabel(Seller user, Order order) {
        System.out.println("Printing label...");
        System.out.println();
        System.out.println("--------------------------------------------------");
        System.out.println();
        System.out.println("FROM: " + user.getId());
        System.out.println("      " + user.getAddress().getAddressLine());
        System.out.println("      " + user.getAddress().getCity() + ", " + user.getAddress().getProvince() + ", " + user.getAddress().getCountry());
        System.out.println("      " + user.getAddress().getPostalCode());
        System.out.println();
        System.out.println("TO:   " + order.getBuyer().getFirstName() + order.getBuyer().getLastName());
        System.out.println("      " + order.getBuyer().getAddress().getAddressLine());
        System.out.println("      " + order.getBuyer().getAddress().getCity() + ", " + order.getBuyer().getAddress().getProvince() + ", " + order.getBuyer().getAddress().getCountry());
        System.out.println("      " + order.getBuyer().getAddress().getPostalCode());
        System.out.println();
        System.out.println("--------------------------------------------------");
        System.out.println();
        System.out.println("Label printed!");
        System.out.println();
    }

    // Refund buyer
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
            sendBuyerNotification(order.getBuyer(), "You've received a refund", "You've received a refund of " + sum + " from your return request " + order.getIssue().getId() + ".");
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

            // send notification to buyer
            sendBuyerNotification(order.getBuyer(), "You've received a refund", "You've received a refund of " + sum + " from your return request " + order.getIssue().getId() + ".");
        }

        // update product quantities in database and seller's inventory
        addDatabaseProductQuantities(returnProducts);
        addInventoryQuantities(returnProducts);

        // confirm refund
        System.out.println("Refund completed!");
    }

    // Adds product quantities from database
    private void addDatabaseProductQuantities(HashMap<Product, Integer> returnProducts) {
        ArrayList<Product> databaseProducts = database.getProducts();
        for (Map.Entry<Product, Integer> returnProduct : returnProducts.entrySet()) {
            // find product in seller inventory
            for (Product product : databaseProducts) {
                if (Objects.equals(product, returnProduct.getKey())) {
                    int index = databaseProducts.indexOf(product);
                    database.getProducts().get(index).setQuantity(product.getQuantity() + returnProduct.getValue());
                }
            }
        }
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

    // ISSUES

    public void displayIssue(Order order) {
        System.out.println("Issue ID: " + order.getIssue().getId());
        System.out.println("Description: " + order.getIssue().getIssueDescription());

        if (order.getIssue().getSolutionDescription().isEmpty()) {
            System.out.println("Solution: waiting for a solution...");
        } else {
            System.out.println("Solution: " + order.getIssue().getSolutionDescription());
        }

        if (order.getIssue().getReshipmentTrackingNum() == null) {
            System.out.println("Reshipment tracking #: N/A");
        } else {
            System.out.println("Reshipment tracking #: " + order.getIssue().getReshipmentTrackingNum());
        }

        if (!order.getIssue().getReshipmentReceived()) {
            System.out.println("Reshipment received: N/A"); // TODO: change status according to order status
        } else {
            System.out.println("Reshipment received: yes");
        }

        if (order.getIssue().getReplacementProduct() == null) {
            System.out.println("Replacement product: N/A");
        } else {
            System.out.println("Replacement product: " + order.getIssue().getReplacementProduct().getTitle());
        }

        if (order.getIssue().getReplacementTrackingNum() == null) {
            System.out.println("Replacement tracking #: N/A");
        } else {
            System.out.println("Replacement tracking #: " + order.getIssue().getReplacementTrackingNum());
        }

        if (!order.getIssue().getReplacementReceived()) {
            System.out.println("Replacement received: N/A"); // TODO: change status according to order status
        } else {
            System.out.println("Replacement received: yes");
        }
    }

    // INVENTORY

    public boolean displayInventory() {
        boolean continueLoop = true;

        while (continueLoop) {
            System.out.println("INVENTORY");
            System.out.println();
            user.getProducts().forEach(product -> System.out.println(product.toString()));
            System.out.println();
            System.out.println("Please select an option:");
            System.out.println("1. Add item(s)");
            System.out.println("2. Remove item(s)");
            System.out.println("3. Change item quantity");
            System.out.println("4. Modify additional points of product");
            System.out.println("5. Return to menu");

            int choice = uiUtilities.getUserInputAsInteger();

            switch (choice) {
                case 1:
                    System.out.println("Adding item(s)...");
                    addProduct();
                    break;
                case 2:
                    System.out.println("Removing item(s)...");
                    removeProduct();
                    break;
                case 3:
                    System.out.println("Changing item quantity...");
                    changeProductQty();
                    break;
                case 4:
                    System.out.println("Modifying additional points of product...");
                    modifyAdditionalPoints();
                    break;
                case 5:
                    continueLoop = false; // Exit the inventory submenu
                    break;
            }
        }
        return true;
    }

    public void modifyAdditionalPoints() {
        Product product = null;
        while (product == null) {
            System.out.println("Please enter the title of the product:");
            String title = InputManager.getInstance().nextLine();
            product = user.findProductByTitle(title);
        }
        product.setBasePoints((int) Math.floor(product.getPrice()));
        System.out.println("Please enter the additional points of the product:");
        int additionalPoints = uiUtilities.getUserInputAsInteger();
        if (additionalPoints > Math.floor(product.getPrice())*19) {
            additionalPoints = (int) Math.floor(product.getPrice())*19;
        }
        product.setBasePoints(product.getBasePoints()+additionalPoints);
    }

    public void changeProductQty() {
        Product product = null;
        while (product == null) {
            System.out.println("Please enter the title of the product:");
            String title = InputManager.getInstance().nextLine();
            product = user.findProductByTitle(title);
        }

        System.out.println("Please enter the quantity of the product:");
        int quantity = uiUtilities.getUserInputAsInteger();
        if (quantity == 0) {
            database.removeProduct(product);
        }
        user.changeProductQuantity(product, quantity);
    }

    public void removeProduct() {
        Product product = null;
        while (product == null) {
            System.out.println("Please enter the title of the product:");
            String title = InputManager.getInstance().nextLine();
            product = user.findProductByTitle(title);
        }
        System.out.println("Are you sure? (y/n)");
        String choice = InputManager.getInstance().nextLine();
        if (choice.equals("y")) {database.removeProduct(product);}
        else {System.out.println("Product not removed");}
    }

    public void addProduct() {
        InputManager inputManager = InputManager.getInstance();
        System.out.println("Please enter the title of the product:");
        String title = inputManager.nextLine();
        System.out.println("Please enter the description of the product:");
        String description = inputManager.nextLine();
        String priceText = "a";
        while (!priceText.matches("\\d+(\\.\\d+)?")) {
            System.out.println("Please enter the price of the product:");
            priceText = inputManager.nextLine();
        }
        float price = parseFloat(priceText);
        System.out.println("Please enter the bonus points of the product:");
        int bonusPoints = uiUtilities.getUserInputAsInteger();
        if (bonusPoints < 0) {
            bonusPoints = 0;
        }
        if (Math.floor(price)*20 < Math.floor(price) + bonusPoints) {
            bonusPoints = (int) Math.floor(price)*19;
        }
        System.out.println("Please enter the quantity of the product:");
        int quantity = uiUtilities.getUserInputAsInteger();
        System.out.println("Please enter the sell date of the product:");
        String sellDate = inputManager.nextLine();
        Product product = null;
        String author, releaseDate, brand, model, subCategory;
        int ISBN, edition;
        switch (user.getCategory()) {
            case BOOKS:
                System.out.println("Please enter the author of the book:");
                author = inputManager.nextLine();
                System.out.println("Please enter the publisher of the book:");
                String publisher = inputManager.nextLine();
                System.out.println("Please enter the ISBN of the book:");
                ISBN = uiUtilities.getUserInputAsInteger();
                System.out.println("Please enter the genre of the book:");
                String genre = inputManager.nextLine();
                System.out.println("Please enter the release date of the book:");
                releaseDate = inputManager.nextLine();
                System.out.println("Please enter the edition of the book:");
                edition = uiUtilities.getUserInputAsInteger();
                System.out.println("Please enter the volume of the book:");
                int volume = uiUtilities.getUserInputAsInteger();
                product = new Book(title, description, price, (int) Math.floor(price) + bonusPoints , user, quantity, ISBN, author, publisher, genre, releaseDate, sellDate, edition, volume);
                break;
            case LEARNING_RESOURCES:
                System.out.println("Please enter the author of the learning resource:");
                author = inputManager.nextLine();
                System.out.println("Please enter the organization of the learning resource:");
                String organization = inputManager.nextLine();
                System.out.println("Please enter the ISBN of the learning resource:");
                ISBN = uiUtilities.getUserInputAsInteger();
                System.out.println("Please enter the release date of the learning resource:");
                releaseDate = inputManager.nextLine();
                System.out.println("Please enter the type of the learning resource:");
                String type = inputManager.nextLine();
                System.out.println("Please enter the edition of the learning resource:");
                edition = uiUtilities.getUserInputAsInteger();
                product = new LearningResource(title, description, price, (int) Math.floor(price) + bonusPoints, user, quantity, ISBN, author, organization, releaseDate, sellDate, type, edition);
                break;
            case STATIONERY:
                System.out.println("Please enter the brand of the stationery:");
                brand = inputManager.nextLine();
                System.out.println("Please enter the model of the stationery:");
                model = inputManager.nextLine();
                System.out.println("Please enter the subcategory of the stationery:");
                subCategory = inputManager.nextLine();
                System.out.println("Please enter the release date of the stationery:");
                releaseDate = inputManager.nextLine();
                product = new Stationery(title, description, price, (int) Math.floor(price) + bonusPoints, user, quantity, brand, model, subCategory, releaseDate, sellDate);
                break;
            case ELECTRONICS:
                System.out.println("Please enter the brand of the electronics:");
                brand = inputManager.nextLine();
                System.out.println("Please enter the model of the electronics:");
                model = inputManager.nextLine();
                System.out.println("Please enter the subcategory of the electronics:");
                subCategory = inputManager.nextLine();
                System.out.println("Please enter the release date of the electronics:");
                releaseDate = inputManager.nextLine();
                product = new Hardware(title, description, price,(int) Math.floor(price) + bonusPoints , user, quantity, brand, model, releaseDate, subCategory, sellDate);
                break;
            case DESKTOP_ACCESSORIES:
                System.out.println("Please enter the brand of the desktop accessory:");
                brand = inputManager.nextLine();
                System.out.println("Please enter the model of the desktop accessory:");
                model = inputManager.nextLine();
                System.out.println("Please enter the subcategory of the desktop accessory:");
                subCategory = inputManager.nextLine();
                product = new OfficeEquipment(title, description, price, (int) Math.floor(price) + bonusPoints, user, quantity, brand, model, subCategory, sellDate);
                break;
        }
        if (database.verifyNewProduct(product)) {
            database.addProduct(product);
        } else {
            System.out.println("Product already exists");
        }
    }

    // METRICS

    public void displayProfileMetrics() {
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
}
