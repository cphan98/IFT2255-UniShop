package UIs;

import LoginUtility.DataBase;
import Users.Seller;
import products.*;

import static java.lang.Float.parseFloat;
import static java.lang.Integer.parseInt;

public class SellerMenu extends Menu {
    private Seller user;
    private DataBase dataBase;

    public SellerMenu(Seller user, DataBase dataBase) {
        super(user);
        this.user = user;
        this.dataBase = dataBase;
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
                    // TODO
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
        // TODO
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
        if (verifyNewProduct(product)) {
            dataBase.addProduct(product);
        } else {
            System.out.println("Product already exists");
        }
    }

    public boolean verifyNewProduct(Product product) {
        boolean valid = true;
        for (Product p : dataBase.getProducts()) {
            if (p.getTitle().equals(product.getTitle())) {
                valid = false;
                break;
            }
        }
        return valid;
    }

    public void displayMetrics() {
        System.out.println(user.getMetrics().toString());
    }
}
