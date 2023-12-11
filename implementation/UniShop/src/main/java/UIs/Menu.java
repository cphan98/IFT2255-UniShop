package UIs;

import BackEndUtility.DataBase;
import BackEndUtility.InputManager;
import Users.*;
import UtilityObjects.Notification;
import productClasses.Usages.Order;

public abstract class Menu {

    // ATTRIBUTES

    protected User user;
    protected DataBase database;
    protected final UIUtilities uiUtilities;

    // CONSTRUCTOR

    public Menu(User user, DataBase database) {
        this.user = user;
        this.database = database;
        this.uiUtilities = new UIUtilities(database, user);
    }

    // GETTER

    public UIUtilities getUiUtilities() {
        return uiUtilities;
    }

    // UTILITIES

    // main page ------------------------------------------------------------------------------------------------------

    public abstract boolean displayMenu();

    // profile page ---------------------------------------------------------------------------------------------------

    public abstract boolean displayProfile();

    // followers page -------------------------------------------------------------------------------------------------

    public boolean displayFollowers() {
        int i = 0;
        for (Buyer buyer : user.getFollowers()) {
            i++;
            System.out.println(i + " " + buyer.getId());
        }
        System.out.println("You have " + user.getFollowers().size() + " followers: ");
        System.out.println("1. Get the follower");
        System.out.println("2. Return to menu");
        int choice = uiUtilities.getUserInputAsInteger();
        switch (choice) {
            case 1 -> searchFollower();
            case 2 -> System.out.println("Returning to menu...");
        }
        return true;
    }

    // follower page --------------------------------------------------------------------------------------------------

    public void searchFollower() {
        System.out.println("Enter the id of the follower you want to view:");
        String id = InputManager.getInstance().nextLine();
        Buyer pointedFollower = (Buyer) user.getFollower(id);
        if (pointedFollower == null) {
            System.out.println("Follower not found.");
        } else {
            System.out.println(pointedFollower);
            interactWithFollower(pointedFollower);
        }
    }

    public void interactWithFollower(Buyer pointedFollower) {
        System.out.println("1. Remove this follower");
        System.out.println("2. Return to menu");
        int choice = uiUtilities.getUserInputAsInteger();
        switch (choice) {
            case 1 -> user.removeFollower(pointedFollower);
            case 2 -> System.out.println("Returning to menu...");
            default -> System.out.println("Invalid selection. Please try again.");
        }
    }

    // order history page ---------------------------------------------------------------------------------------------

    public abstract boolean displayOrderHistory();

    // order page -----------------------------------------------------------------------------------------------------

    public abstract void interactWithOrder(Order order);

    // notifications --------------------------------------------------------------------------------------------------

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

    // metrics --------------------------------------------------------------------------------------------------------

    public abstract void displayMetrics();

    // profile modification -------------------------------------------------------------------------------------------

    public abstract void modifyProfile();

    // other ----------------------------------------------------------------------------------------------------------

    public static void line() {
        System.out.println("--------------------------------------------------");
    }
}
