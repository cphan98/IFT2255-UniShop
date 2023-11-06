package UIs;

import LoginUtility.Catalog;
import LoginUtility.DataBase;
import Users.Buyer;

public class BuyerMenu extends Menu {
    private Buyer user;
    private DataBase database;

    public BuyerMenu(Buyer user, DataBase database) {
        super(user);
        this.user = user;
        this.database = database;
    }

    public boolean displayMenu() {
        boolean continueLoop = true;
        InputManager inputManager = InputManager.getInstance();

        while(continueLoop) {
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
            System.out.println("6. Log out");
            String choice = inputManager.nextLine();

            switch (choice) {
                case "1":
                    continueLoop = displayProfile();
                    break;
                case "2":
                    continueLoop = displayOrderHistory();
                    break;
                case "3":
                    continueLoop = displayCart();
                    break;
                case "5":
                    continueLoop = displayCatalog();
                    break;
                case "6":
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
            line();
            System.out.println("PROFILE");
            InputManager inputManager = InputManager.getInstance();
            System.out.println("Name: " + user.getId());
            System.out.println("Email: " + user.getEmail());
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
                case "1":
                    System.out.println("Modifying profile...");
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

    public boolean displayOrderHistory() {
        InputManager inputManager = InputManager.getInstance();
        String choice = "";
        while (!choice.equals("1")) {
            System.out.println("ORDER HISTORY");
            System.out.println("1. Return to menu");
            choice = inputManager.nextLine();
        }
        return true;  // continue the loop
    }

    public boolean displayCart() {
        InputManager inputManager = InputManager.getInstance();
        String choice = "";
        while (!choice.equals("1")) {
            System.out.println("CART");
            System.out.println("1. Return to menu");
            choice = inputManager.nextLine();
        }
        return true;  // continue the loop
    }

    public boolean displayNotifications() {
        //TODO
        return true;  // this can be changed depending on the outcome of the TODO
    }

    public void displayMetrics() {
        System.out.println(user.getMetrics().toString());
    }

    public boolean displayCatalog() {
        line();
        System.out.println("CATALOG");
        Catalog catalog = new Catalog(database.getSellers());
        catalog.displayCatalog();
        line();
        InputManager inputManager = InputManager.getInstance();
        boolean continueLoop = true;
        while (continueLoop)
        {
            System.out.println("1. Get a product");
            System.out.println("2. Get a seller's infos and products");
            System.out.println("3. Return to menu");
            String choice = inputManager.nextLine();
            switch (choice) {
                case "1":
                    System.out.println("Getting a product...");
                    System.out.println("Enter the name of the product you want to check out:");
                    String productName = inputManager.nextLine();
                    System.out.println(catalog.searchProductByName(productName) == null ? "Product not found" : catalog.searchProductByName(productName).toString());
                    break;
                case "2":
                    System.out.println("Getting a seller's infos and products...");
                    System.out.println("Enter the name of the seller you want to check out:");
                    String id = inputManager.nextLine();
                    System.out.println(catalog.searchSellerByName(id) == null ? "Seller not found" : catalog.searchSellerByName(id).toString());
                    break;
                case "3":
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









}
