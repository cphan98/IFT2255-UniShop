package UIs;

import BackEndUtility.DataBase;
import BackEndUtility.InputManager;
import Users.Buyer;
import Users.Seller;
import UtilityObjects.Address;
import UtilityObjects.Notification;
import Users.User;
import productClasses.Usages.Order;

public abstract class Menu {
    protected User user;
    protected DataBase database;
    protected final UIUtilities uiUtilities;

    public Menu(User user, DataBase database) {
        this.user = user;
        this.database = database;
        this.uiUtilities = new UIUtilities(database, user);
    }
    public UIUtilities getUiUtilities() {
        return uiUtilities;
    }
    public abstract boolean displayMenu();
    public abstract boolean displayProfile();
    // ORDER HISTORY
    public boolean displayOrderHistory() {
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
            order = user.getOrderHistory().get(choice - 1);
            if (order == null) {
                System.out.println("Invalid selection. Please try again.");
                choice = uiUtilities.getUserInputAsInteger();
            }
        }
        interactWithOrder(order);

        return true;  // continue the loop
    }
    public abstract void interactWithOrder(Order order);
    // NOTIFICATIONS
    public void displayNotifications() {
        System.out.println("NOTIFICATIONS");
        System.out.println(user.notificationsToString());
        System.out.println("1. Return to menu");
        int choice = uiUtilities.getUserInputAsInteger();
        while (choice != 1) {
            System.out.println("Invalid selection. Please try again.");
            choice = uiUtilities.getUserInputAsInteger();
        }
        for (Notification notification : user.getNotifications()) {
            notification.setRead(true);
        }
        System.out.println("Returning to menu...");
    }


    public void sendBuyerNotification(Buyer buyer, String title, String summary) {
        buyer.addNotification((new Notification(title, summary)));
    }

    public void sendSellerNotification(Seller seller, String title, String summary) {
        seller.addNotification(new Notification(title, summary));
    }

    public abstract void displayMetrics();

    public abstract void modifyProfile();

    protected static void line() {
        System.out.println("--------------------------------------------------");
    }
}
