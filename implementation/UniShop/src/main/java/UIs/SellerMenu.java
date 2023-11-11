package UIs;

import LoginUtility.DataBase;
import Users.Address;
import Users.Seller;
import products.*;

import java.util.Objects;

import static java.lang.Float.parseFloat;
import static java.lang.Integer.parseInt;

public class SellerMenu extends Menu {
    private Seller user;

    public SellerMenu(Seller user, DataBase dataBase) {
        super(user);
        this.user = user;
        this.database = dataBase;
    }

    public boolean displayMenu() {
        boolean continueLoop = true;
        InputManager inputManager = InputManager.getInstance();

        while (continueLoop) {
            System.out.println("WELCOME DEAR SELLERRRRRRRRR");
            System.out.println();
            System.out.println("Please select an option:");
            System.out.println("1. Display Profile");
            System.out.println("2. Display Order History");
            System.out.println("3. Modify Inventory");
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
                modifyPersonalInfos();
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

    public void modifyPersonalInfos(){
        System.out.println("Enter your new id:");
        String id = InputManager.getInstance().nextLine();
        System.out.println("Enter your email:");
        String email = InputManager.getInstance().nextLine();
        System.out.println("Enter your phone number:");
        String phoneNumber = InputManager.getInstance().nextLine();
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

    public boolean displayNotifications() {
        System.out.println("NOTIFICATIONS");
        return true;
    }

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
            System.out.println("3. Return to menu");

            String choice = inputManager.nextLine();

            switch (choice) {
                case "1":
                    System.out.println("Adding item(s)...");
                    addProduct();
                    break;
                case "2":
                    System.out.println("Removing item(s)...");
                    removeProduct();
                    break;
                case "3":
                    System.out.println("Changing item quantity...");
                    changeProductQty();
                    break;
                case "4":
                    continueLoop = false; // Exit the inventory submenu
                    break;
            }
        }
        return true;
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
        System.out.println("Please enter the price of the product:");
        float price = parseFloat(inputManager.nextLine());
        System.out.println("Please enter the base points of the product:");
        int basePoints = parseInt(inputManager.nextLine());
        System.out.println("Please enter the quantity of the product:");
        int quantity = parseInt(inputManager.nextLine());
        System.out.println("Please enter the sell date of the product:");
        String sellDate = inputManager.nextLine();
        Product product = null;
        switch (user.getCategory()) {
            case BOOKS:
                System.out.println("Please enter the author of the book:");
                String author = inputManager.nextLine();
                System.out.println("Please enter the publisher of the book:");
                String publisher = inputManager.nextLine();
                System.out.println("Please enter the ISBN of the book:");
                int ISBN = parseInt(inputManager.nextLine());
                System.out.println("Please enter the genre of the book:");
                String genre = inputManager.nextLine();
                System.out.println("Please enter the release date of the book:");
                String releaseDate = inputManager.nextLine();
                System.out.println("Please enter the edition of the book:");
                int edition = parseInt(inputManager.nextLine());
                System.out.println("Please enter the volume of the book:");
                int volume = parseInt(inputManager.nextLine());
                product = new Book(title, description, price, basePoints, user, quantity, ISBN, author, publisher, genre, releaseDate, sellDate, edition, volume);
                break;
            case LEARNING_RESOURCES:
                System.out.println("Please enter the author of the learning resource:");
                author = inputManager.nextLine();
                System.out.println("Please enter the organization of the learning resource:");
                String organization = inputManager.nextLine();
                System.out.println("Please enter the ISBN of the learning resource:");
                ISBN = parseInt(inputManager.nextLine());
                System.out.println("Please enter the release date of the learning resource:");
                releaseDate = inputManager.nextLine();
                System.out.println("Please enter the type of the learning resource:");
                String type = inputManager.nextLine();
                System.out.println("Please enter the edition of the learning resource:");
                edition = parseInt(inputManager.nextLine());
                product = new LearningResource(title, description, price, basePoints, user, quantity, ISBN, author, organization, releaseDate, sellDate, type, edition);
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
                product = new Stationery(title, description, price, basePoints, user, quantity, brand, model, subCategory, releaseDate, sellDate);
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
                product = new Hardware(title, description, price, basePoints, user, quantity, brand, model, releaseDate, subCategory, sellDate);
                break;
            case DESKTOP_ACCESSORIES:
                System.out.println("Please enter the brand of the desktop accessory:");
                brand = inputManager.nextLine();
                System.out.println("Please enter the model of the desktop accessory:");
                model = inputManager.nextLine();
                System.out.println("Please enter the subcategory of the desktop accessory:");
                subCategory = inputManager.nextLine();
                product = new OfficeEquipment(title, description, price, basePoints, user, quantity, brand, model, subCategory, sellDate);
                break;
        }
        if (database.verifyNewProduct(product)) {
            database.addProduct(product);
        } else {
            System.out.println("Product already exists");
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

    public void displayMetrics() {
        System.out.println(user.getMetrics().toString());
    }
}
