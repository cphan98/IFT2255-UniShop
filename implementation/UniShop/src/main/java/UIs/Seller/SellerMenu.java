package UIs.Seller;

import BackEndUtility.DataBase;
import BackEndUtility.InputManager;
import UIs.Menu;
import Users.Buyer;
import UtilityObjects.Address;
import Users.Seller;
import UtilityObjects.Notification;
import productClasses.Usages.Order;
import BackEndUtility.OrderState;
import productClasses.*;
import productClasses.Inheritances.*;

import java.util.*;

import static java.lang.Float.parseFloat;

public class SellerMenu extends Menu {
    // ATTRIBUTE

    private final Seller user;

    // CONSTRUCTOR

    public SellerMenu(Seller user, DataBase database) {
        super(user, database);
        this.user = user;
    }

    // OPERATIONS

    // MAIN MENU

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
            System.out.println("6. Display Followers");
            System.out.println("7. Display Your Customers");
            System.out.println("8. Log out");
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
                    displayNotifications();
                    break;
                case 5:
                    displayMetrics();
                    break;
                case 6:
                    continueLoop = displayFollowers();
                    break;
                case 7:
                    continueLoop = displayCustomers();
                    break;
                case 8:
                    return false;  // Add this to handle log out
                default:
                    System.out.println("Invalid selection. Please try again.");
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
                    System.out.println();
                    System.out.println("Modifying profile...");
                    modifyProfile();
                    break;
                case 2:
                    System.out.println();
                    System.out.println("Returning to menu...");
                    continueLoop = false;
                    break;
                case 3:
                    System.out.println();
                    System.out.println("Deleting account...");
                    uiUtilities.deleteAccount();
                    return false;
                default:
                    System.out.println();
                    System.out.println("Invalid selection. Please try again.");
                    return false;  // continue the loop
            }
        }
        return true;  // continue the loop
    }

    public boolean displayCustomers() {
        System.out.println("Your customers: ");
        int i = 0;
        for (Buyer customer : user.getCustomers()) {
            System.out.println(i + ". " + customer.getId());
        }
        System.out.println("You have " + user.getCustomers().size() + " customers.");
        return true;
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

                order.getBuyer().addNotification(new Notification(user + "Has send a solution to the problem", "the seller thinks its pertinent to: " + order.getIssue().getSolutionDescription()));

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
            sendBuyerNotification(order.getBuyer(), "Order status changed", "your order " + order.getId() + " is now " + order.getStatus().toString().toLowerCase() + "!");

            // confirm order preparation
            System.out.println();
            System.out.println("Your order is ready to be shipped!");
        } else {
            System.out.println();
            System.out.println("WARNING : Cannot prepare order");
        }
    }

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
        order.getBuyer().getOrderHistory().get(order.getBuyer().getOrderHistory().indexOf(order)).setStatus(OrderState.IN_DELIVERY);
        order.getBuyer().getOrderHistory().get(order.getBuyer().getOrderHistory().indexOf(order)).getIssue().setReshipmentReceived(true);

        // send notification to buyer
        sendBuyerNotification(order.getBuyer(), "Order status changed", "Your order " + order.getId() + " is now " + order.getStatus().toString().toLowerCase() + "!");

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
    }

    public void printLabel(Order order) {
        System.out.println();
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

            // put back points to buyer's points
            database.getBuyers().get(database.getBuyers().indexOf(order.getBuyer())).addPoints(sum);

            // send notification to buyer
            sendBuyerNotification(order.getBuyer(), "You've received a refund", "You've received a refund of " + sum + " from your return request " + order.getIssue().getId() + ".");
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

    // Prepares replacement order
    private void prepareReplacementOrder(Order replacementOrder) {
        System.out.println();
        System.out.println("The buyer requested an exchange");
        System.out.println();
        System.out.println("Preparing replacement order...");

        // change replacement order status
        replacementOrder.getBuyer().getOrderHistory().get(replacementOrder.getBuyer().getOrderHistory().indexOf(replacementOrder)).setStatus(OrderState.REPLACEMENT_IN_PRODUCTION);

        // send notification to buyer
        sendBuyerNotification(replacementOrder.getBuyer(), "Order status changed", "your order " + replacementOrder.getId() + " is now " + replacementOrder.getStatus().toString().toLowerCase() + "!");

        // display replacement products
        System.out.println();
        System.out.println("Available replacement products:");
        replacementOrder.productsToString();

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
        replacementOrder.getBuyer().getOrderHistory().get(replacementOrder.getBuyer().getOrderHistory().indexOf(replacementOrder)).setShippingNumber(number);

        // change replacement order status
        replacementOrder.getBuyer().getOrderHistory().get(replacementOrder.getBuyer().getOrderHistory().indexOf(replacementOrder)).setStatus(OrderState.REPLACEMENT_IN_DELIVERY);

        // send notification to buyer
        sendBuyerNotification(replacementOrder.getBuyer(), "Order status changed", "your order " + replacementOrder.getId() + " is now " + replacementOrder.getStatus().toString().toLowerCase() + "!");

        // confirm replacement order prepared
        System.out.println();
        System.out.println("Your order is ready to be shipped!");
    }

    // ISSUES

    public void displayIssue(Order order) {
        System.out.println("Issue ID: " + order.getIssue().getId());
        System.out.println("Description: " + order.getIssue().getIssueDescription());

        if (order.getIssue().getSolutionDescription() == null) {
            System.out.println("Solution: waiting for a solution...");
            order.getIssue().proposeSolution();
            order.getBuyer().addNotification(new Notification("Seller: " +  user+ " has proposed a solution", user + "thinks it's pertinent to: " + order.getIssue().getSolutionDescription()));
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

    // INVENTORY

    public boolean displayInventory() {
        boolean continueLoop = true;
        while (continueLoop) {
            System.out.println();
            System.out.println("INVENTORY");

            // display products
            System.out.println();
            user.getProducts().forEach(product -> System.out.println(product.toString()));

            // select option
            System.out.println("Please select an option:");
            System.out.println("1. Add item(s)");
            System.out.println("2. Remove item(s)");
            System.out.println("3. Change item quantity");
            System.out.println("4. Add promotion");
            System.out.println("5. Return to menu");
            int choice = uiUtilities.getUserInputAsInteger();

            // process selection
            switch (choice) {
                // add item(s)
                case 1:
                    System.out.println();
                    System.out.println("Adding item(s)...");
                    addProduct();
                    break;

                // remove item(s)
                case 2:
                    System.out.println();
                    System.out.println("Removing item(s)...");
                    removeProduct();
                    break;

                // change item quantity
                case 3:
                    System.out.println();
                    System.out.println("Changing item quantity...");
                    changeProductQty();
                    break;

                // add promotion
                case 4:
                    System.out.println();
                    System.out.println("Adding promotion...");
                    continueLoop = addPromotion();
                    break;

                // return to menu
                case 5:
                    continueLoop = false; // Exit the inventory submenu
                    break;

                default:
                    System.out.println();
                    System.out.println("Invalid selection. Please try again.");
            }
        }

        // continue loop
        return true;
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

    // Adds new product to database
    public void addProduct() {
        InputManager inputManager = InputManager.getInstance();

        // ask title
        System.out.println();
        System.out.println("Please enter the title of the product:");
        String title = inputManager.nextLine();

        // ask description
        System.out.println("Please enter the description of the product:");
        String description = inputManager.nextLine();

        // ask price
        String priceText = "a";
        while (!priceText.matches("\\d+(\\.\\d+)?")) {
            System.out.println("Please enter the price of the product:");
            priceText = inputManager.nextLine();
        }
        float price = parseFloat(priceText);

        // ask bonus points
        System.out.println("Please enter the bonus points of the product:");
        int bonusPoints = uiUtilities.getUserInputAsInteger();
        if (bonusPoints < 0) {
            bonusPoints = 0;
        }
        if (Math.floor(price)*20 < Math.floor(price) + bonusPoints) {
            bonusPoints = (int) Math.floor(price)*19;
        }

        // ask quantity
        System.out.println("Please enter the quantity of the product:");
        int quantity = uiUtilities.getUserInputAsInteger();

        // ask sell date
        System.out.println("Please enter the sell date of the product:");
        String sellDate = inputManager.nextLine();

        // ask details according to category
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

        // check if product already exists
        if (database.verifyNewProduct(product)) {
            // add product to database
            database.addProduct(product);

            // send notification to followers
            sendNewProductNotification(product);
        } else {
            System.out.println("Product already exists");
        }
    }

    // Sends notification to followers when new product added
    private void sendNewProductNotification(Product newProduct) {
        ArrayList<Buyer> followers = user.getFollowers();
        if (!followers.isEmpty())
            for (Buyer follower : followers)
                sendBuyerNotification(follower, "New Product", "New product added by " + user.getId() + ": " + newProduct.getTitle());
    }

    // Adds product promotion
    private boolean addPromotion(){
        // ask promotion type
        System.out.println();
        System.out.println("Please select the promotion type:");
        System.out.println("1. Price reduction");
        System.out.println("2. Bonus points");
        System.out.println("3. Return to menu");
        int promoChoice = uiUtilities.getUserInputAsInteger();

        // process selection
        switch (promoChoice) {
            // price reduction
            case 1:
                addPriceReduction();
                break;

            // bonus points
            case 2:
                addBonusPoints();
                break;

            // return to menu
            case 3:
                return false;

            // invalid input
            default:
                System.out.println();
                System.out.println("Invalid selection. Please try again.");
                promoChoice = uiUtilities.getUserInputAsInteger();
        }

        return true;
    }

    // Adds price reduction
    private void addPriceReduction() {
        // ask product title and get Product object
        Product product = null;
        while (product == null) {
            System.out.println();
            System.out.println("Please enter the title of the product:");
            String title = InputManager.getInstance().nextLine();
            product = user.findProductByTitle(title);
        }

        // ask reduced price
        System.out.println();
        System.out.println("Please enter the price of the product:");
        InputManager im = InputManager.getInstance();
        String priceText = "a";
        while (!priceText.matches("\\d+(\\.\\d+)?")) {
            priceText = im.nextLine();

            // validate price
            if (parseFloat(priceText) < 0) {
                System.out.println();
                System.out.println("WARNING : The price cannot be less than 0.00$");
                continue;
            }
            if (parseFloat(priceText) >= product.getPrice()) {
                System.out.println();
                System.out.println("WARNING : The price has to be less than " + product.getPrice() + "$");
            }
        }
        float reducedPrice = parseFloat(priceText);

        // set reduced price to product
        product.setPrice(reducedPrice);

        // update info in inventory
        ArrayList<Product> newInventory = new ArrayList<>();
        Product oldProduct = null;
        for (Product sellerProduct : user.getProducts()) {
            // replace old product with new product with same title
            if (Objects.equals(sellerProduct.getTitle(), product.getTitle())) {
                newInventory.add(product);
                oldProduct = sellerProduct;
            }

            // add product to list
            newInventory.add(sellerProduct);
        }
        user.setProducts(newInventory);

        // send notifications to buyers
        sendPromoNotificationToLikes(oldProduct, "price reduction");
        sendPromoNotificationToFollowers(oldProduct, "price reduction");
        sendPromoNotificationToBuyer(oldProduct, "price reduction");
    }

    // Adds bonus points to product
    private void addBonusPoints() {
        // ask product title and get Product object
        Product product = null;
        while (product == null) {
            System.out.println();
            System.out.println("Please enter the title of the product:");
            String title = InputManager.getInstance().nextLine();
            product = user.findProductByTitle(title);
        }

        // reset base points
        product.setBasePoints((int) Math.floor(product.getPrice()));

        // asks bonus points to add to base points
        System.out.println();
        System.out.println("Please enter the number of bonus points:");
        int bonusPoints = uiUtilities.getUserInputAsInteger();
        // cannot add bonus points > 20x base points
        if (bonusPoints > Math.floor(product.getPrice())*19)
            bonusPoints = (int) Math.floor(product.getPrice())*19;

        // set new points to product
        product.setBasePoints(product.getBasePoints() + bonusPoints);

        // update info in inventory
        ArrayList<Product> newInventory = new ArrayList<>();
        Product oldProduct = null;
        for (Product sellerProduct : user.getProducts()) {
            // replace old product with new product with same title
            if (Objects.equals(sellerProduct.getTitle(), product.getTitle())) {
                newInventory.add(product);
                oldProduct = sellerProduct;
            }

            // add product to list
            newInventory.add(sellerProduct);
        }
        user.setProducts(newInventory);

        // send notification to buyers
        sendPromoNotificationToLikes(oldProduct, "bonus points");
        sendPromoNotificationToFollowers(oldProduct, "bonus points");
        sendPromoNotificationToBuyer(oldProduct, "bonus points");
    }

    // Sends notification to buyers who liked the product
    private void sendPromoNotificationToLikes(Product oldProduct, String bonusPointsOrPriceReduction) {
        for (Buyer buyer : database.getBuyers())
            if (buyer.getWishList().contains(oldProduct))
                switch (bonusPointsOrPriceReduction) {
                    case "bonus points":
                        sendBuyerNotification(buyer, "New Promotion", user.getId() + " added bonus points to " + oldProduct.getTitle());
                        break;
                    case "price reduction":
                        sendBuyerNotification(buyer, "New Promotion", user.getId() + " reduced the price of " + oldProduct.getTitle());
                        break;
                }

    }

    // Sends notification to followers
    private void sendPromoNotificationToFollowers(Product oldProduct, String bonusPointsOrPriceReduction) {
        for (Buyer buyer : user.getFollowers())
            switch (bonusPointsOrPriceReduction) {
                case "bonus points":
                    sendBuyerNotification(buyer, "New Promotion", user.getId() + " added bonus points to " + oldProduct.getTitle());
                    break;
                case "price reduction":
                    sendBuyerNotification(buyer, "New Promotion", user.getId() + " reduced the price of " + oldProduct.getTitle());
                    break;
            }
    }

    // Sends notification to buyer whose followers liked the product
    private void sendPromoNotificationToBuyer(Product oldProduct, String bonusPointsOrPriceReduction) {
        for (Buyer buyer : database.getBuyers()) {
            boolean likedByFollowers = false;
            for (Buyer follower : buyer.getFollowers()) {
                if (follower.getWishList().contains(oldProduct)) {
                    likedByFollowers = true;
                    break;
                }
            }
            if (likedByFollowers)
                switch (bonusPointsOrPriceReduction) {
                    case "bonus points":
                        sendBuyerNotification(buyer, "New Promotion", user.getId() + " added bonus points to a product one of your followers liked: " +oldProduct.getTitle());
                        break;
                    case "price reduction":
                        sendBuyerNotification(buyer, "New Promotion", user.getId() + " reduced the price of " + oldProduct.getTitle());
                        break;
                }
        }
    }

    // METRICS

    public void displayProfileMetrics() {
        if(! user.getMetrics().getSelectedMetrics().isEmpty()){
            System.out.println(user.getMetrics().getSelectedMetrics().get(0));
            System.out.println(user.getMetrics().getSelectedMetrics().get(1));
            System.out.println(user.getMetrics().getSelectedMetrics().get(2));
        }
    }

    public void  displayMetrics() {
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
}
