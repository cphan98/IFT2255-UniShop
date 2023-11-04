package UIs;

import Users.Buyer;

public class BuyerMenu extends Menu {
    private Buyer user;

    public BuyerMenu(Buyer user) {
        super(user);
        this.user = user;
    }

    public boolean displayMenu() {
        boolean continueLoop = true;
        InputManager inputManager = InputManager.getInstance();

        while(continueLoop) {
            System.out.println("WELCOME DEAR BUYERRRRRRRRR");
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
}
