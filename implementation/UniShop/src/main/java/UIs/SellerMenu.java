package UIs;

import LoginUtility.DataBase;
import Users.Address;
import Users.Seller;
import otherUtility.OrderState;
import products.*;

import java.util.Objects;

import static java.lang.Float.parseFloat;
import static java.lang.Integer.parseInt;

public class SellerMenu extends Menu {
    private Seller user;

    // MENU

    public SellerMenu(Seller user, DataBase dataBase) {
        super(user);
        this.user = user;
        this.database = dataBase;
    }

    public boolean displayMenu() {
        boolean continueLoop = true;
        InputManager inputManager = InputManager.getInstance();

        while (continueLoop) {
            line();
            System.out.println("Welcome to UniShop, " + user.getId() + "!");
            line();
            System.out.println();
            System.out.println("Please select an option:");

            System.out.println("1. Display Profile");
            System.out.println("2. Display Order History");
            System.out.println("3. Display Inventory");
            System.out.println("4. Log out");
            String choice = inputManager.nextLine();

            switch (choice) {
                case "1":
                    continueLoop = displayProfile();
                    break;
                case "2":
                    continueLoop = displayOrderHistory();
                    break;
                case "3":
                    continueLoop = displayInventory();
                    break;
                case "4":
                    return false;  // Add this to handle log out
                default:
                    System.out.println("RICKROLL");
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
            InputManager inputManager = InputManager.getInstance();
            System.out.println("Name: " + user.getId());
            System.out.println("Email: " + user.getEmail());
            System.out.println();
            System.out.println("METRICS");
            displayMetrics();
            System.out.println();
            System.out.println("1. Modify profile");
            System.out.println("2. Return to menu");
            System.out.println("3. Delete account");
            String choice = inputManager.nextLine();

            switch (choice) {
                case "1":
                    System.out.println("Modifying profile...");
                    modifyProfile();
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

    public void modifyProfile() {
        System.out.println("1. Modify personal infos");
        System.out.println("2. Modify address");
        System.out.println("3. Modify password");
        System.out.println("4. Return to menu");
        int choice = getUserInputAsInteger();
        switch (choice) {
            case 1:
                modifyPersonalInfo();
                break;
            case 2:
                modifyAddress();
                break;
            case 3:
                modifyPassword();
                break;
            case 4:
                System.out.println("Returning to menu...");
                break;
            default:
                System.out.println("Invalid selection. Please try again.");
                break;
        }
    }

    public void modifyPersonalInfo(){
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
        if (!database.validateNewUser(id, email)) {
            System.out.println("This id or email is already taken");
            System.out.println("Your other infos are changed but your id and email were not changed");
            return;
        }
        user.setId(id);
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);
        System.out.println("Personal infos modified");
    }

    public void modifyAddress(){
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

    // ORDERS

    // TODO: display one order and be able to interact with it

    public boolean displayOrderHistory() {
        System.out.println("ORDER HISTORY");
        System.out.println(user.ordersMadeToString());
        System.out.println("Write the number of the order you want to see the details of, or write 0 to return to menu");
        int choice = getUserInputAsInteger();
        if (choice == 0) {
            System.out.println("Returning to menu...");
            return true;  // continue the loop
        }
        Order order = null;
        while (order == null) {
            order = user.getOrderHistory().get(choice - 1);
            if (order == null) {
                System.out.println("Invalid selection. Please try again.");
                choice = getUserInputAsInteger();
            }
        }
        interactWithOrder(order);

        return true;  // continue the loop
    }

    public void interactWithOrder(Order order) {
        System.out.println(order);
        System.out.println();
        System.out.println("1. Prepare order");
        System.out.println("2. Handle problem");
        System.out.println("3. Confirm return receipt");
        System.out.println("4. Return to order history");

        int choice = getUserInputAsInteger();
        switch (choice) {
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
                    System.out.println("Your order is ready to be shipped!");
                    break;
                }
            case 2:
                if (order.getIssue() == null) {
                    System.out.println("No query in process.");
                    break;
                } else {
                    displayIssue(order);
                }
                break;
            case 3:
                // TODO
                break;
            case 4:
                System.out.println("Returning to order history...");
                break;
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

    // ISSUES

    public void displayIssue(Order order) {
        System.out.println("Issue ID: " + order.getIssue().getId());
        System.out.println("Description: " + order.getIssue().getIssueDescription());

        if (order.getIssue().getSolutionDescription().isEmpty()) {
            System.out.println("Solution: waiting for a solution...");
        } else {
            System.out.println("Solution: " + order.getIssue().getSolutionDescription());
        }

        if (order.getIssue().getReshipmentTrackingNum() == 0) {
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

        if (order.getIssue().getReplacementTrackingNum() == 0) {
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

    // NOTIFICATIONS

    public boolean displayNotifications() {
        System.out.println("NOTIFICATIONS");
        // TODO
        return true;
    }

    // INVENTORY

    public boolean displayInventory() {
        boolean continueLoop = true;
        InputManager inputManager = InputManager.getInstance();

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

            int choice = getUserInputAsInteger();

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
        int additionalPoints = -1;
        String additionalPointsText = "a";
        while (!additionalPointsText.matches("\\d+")) {
            System.out.println("Please enter the additional points of the product:");
            additionalPointsText = InputManager.getInstance().nextLine();
            additionalPoints = parseInt(additionalPointsText);

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
        int quantity = 0;
        while (quantity <= 0) {
            System.out.println("Please enter the new quantity of the product:");
            quantity = parseInt(InputManager.getInstance().nextLine());
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
        String bonusPointsText = "a";
        while (!bonusPointsText.matches("\\d+")) {
            System.out.println("Please enter the bonus points of the product:");
            bonusPointsText = inputManager.nextLine();
        }
        int bonusPoints = parseInt(bonusPointsText);
        if (bonusPoints < 0) {
            bonusPoints = 0;
        }
        if (Math.floor(price)*20 < Math.floor(price) + bonusPoints) {
            bonusPoints = (int) Math.floor(price)*19;
        }
        String quantityText = "a";
        while (!quantityText.matches("\\d+")) {
            System.out.println("Please enter the quantity of the product:");
            quantityText = inputManager.nextLine();
        }
        int quantity = parseInt(quantityText);
        System.out.println("Please enter the sell date of the product:");
        String sellDate = inputManager.nextLine();
        Product product = null;
        switch (user.getCategory()) {
            case BOOKS:
                System.out.println("Please enter the author of the book:");
                String author = inputManager.nextLine();
                System.out.println("Please enter the publisher of the book:");
                String publisher = inputManager.nextLine();
                String ISBNText = "a";
                while (!ISBNText.matches("\\d+")) {
                    System.out.println("Please enter the ISBN of the book:");
                    ISBNText = inputManager.nextLine();
                }
                int ISBN = parseInt(ISBNText);
                System.out.println("Please enter the genre of the book:");
                String genre = inputManager.nextLine();
                System.out.println("Please enter the release date of the book:");
                String releaseDate = inputManager.nextLine();
                String editionText = "a";
                while (!editionText.matches("\\d+")) {
                    System.out.println("Please enter the edition of the book:");
                    editionText = inputManager.nextLine();
                }
                int edition = parseInt(editionText);
                String volumeText = "a";
                while (!volumeText.matches("\\d+")) {
                    System.out.println("Please enter the volume of the book:");
                    volumeText = inputManager.nextLine();
                }
                int volume = parseInt(volumeText);
                product = new Book(title, description, price, (int) Math.floor(price) + bonusPoints , user, quantity, ISBN, author, publisher, genre, releaseDate, sellDate, edition, volume);
                break;
            case LEARNING_RESOURCES:
                System.out.println("Please enter the author of the learning resource:");
                author = inputManager.nextLine();
                System.out.println("Please enter the organization of the learning resource:");
                String organization = inputManager.nextLine();
                ISBNText = "a";
                while (!ISBNText.matches("\\d+")) {
                    System.out.println("Please enter the ISBN of the learning resource:");
                    ISBNText = inputManager.nextLine();
                }
                ISBN = parseInt(ISBNText);
                System.out.println("Please enter the release date of the learning resource:");
                releaseDate = inputManager.nextLine();
                System.out.println("Please enter the type of the learning resource:");
                String type = inputManager.nextLine();
                editionText = "a";
                while (!editionText.matches("\\d+")) {
                    System.out.println("Please enter the edition of the learning resource:");
                    editionText = inputManager.nextLine();
                }
                edition = parseInt(editionText);
                product = new LearningResource(title, description, price, (int) Math.floor(price) + bonusPoints, user, quantity, ISBN, author, organization, releaseDate, sellDate, type, edition);
                break;
            case STATIONERY:
                System.out.println("Please enter the brand of the stationery:");
                String brand = inputManager.nextLine();
                System.out.println("Please enter the model of the stationery:");
                String model = inputManager.nextLine();
                System.out.println("Please enter the subcategory of the stationery:");
                String subCategory = inputManager.nextLine();
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

    public void displayMetrics() {
        System.out.println(user.getMetrics().toString());
    }
}
