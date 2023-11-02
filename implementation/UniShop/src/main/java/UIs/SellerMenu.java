package UIs;

import Users.Seller;

public class SellerMenu extends Menu {
    private Seller user;

    public SellerMenu(Seller user) {
        super(user);
        this.user = user;
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
        System.out.println("ORDERS PROCESSED");
        return true;
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
            System.out.println("Please select an option:");
            System.out.println("1. Add item(s)");
            System.out.println("2. Remove item(s)");
            System.out.println("3. Return to menu");

            String choice = inputManager.nextLine();

            switch (choice) {
                case "1":
                    System.out.println("Adding item(s)...");
                    break;
                case "2":
                    System.out.println("Removing item(s)...");
                    break;
                case "3":
                    continueLoop = false; // Exit the inventory submenu
                    break;
            }
        }
        return true;
    }

    public void displayMetrics() {
        System.out.println(user.getMetrics().toString());
    }
}
