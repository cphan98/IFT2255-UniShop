package UIs;

import BackEndUtility.DataBase;
import BackEndUtility.InputManager;
import Users.Buyer;
import UtilityObjects.Notification;
import Users.User;
import productClasses.Usages.Order;

public abstract class Menu {
    protected User user;
    protected DataBase database;

    public Menu(User user) {
        this.user = user;
    }
    public abstract boolean displayMenu();
    public abstract boolean displayProfile();
    public boolean displayFollowers() {
        int i = 0;
        for (Buyer buyer : user.getFollowers()) {
            i++;
            System.out.println(i + " " + buyer.getId());
        }
        System.out.println("You have " + user.getFollowers().size() + " followers: ");
        System.out.println("1. Get the follower");
        System.out.println("2. Return to menu");
        int choice = getUserInputAsInteger();
        switch (choice) {
            case 1 -> searchFollower();
            case 2 -> System.out.println("Returning to menu...");
        }
        return true;
    }
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
        int choice = getUserInputAsInteger();
        switch (choice) {
            case 1 -> user.removeFollower(pointedFollower);
            case 2 -> System.out.println("Returning to menu...");
            default -> System.out.println("Invalid selection. Please try again.");
        }
    }
    // ORDER HISTORY
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
    public abstract void interactWithOrder(Order order);
    private void setAllNotificationsAsSeen() {
        for (Notification notification : user.getNotifications()) {
            notification.setRead(true);
        }
    }
    public boolean displayNotifications() {
        System.out.println("NOTIFICATIONS");
        System.out.println(user.notificationsToString());
        System.out.println("1. Return to menu");
        int choice = getUserInputAsInteger();
        while (choice != 1) {
            System.out.println("Invalid selection. Please try again.");
            choice = getUserInputAsInteger();
        }
        setAllNotificationsAsSeen();
        System.out.println("Returning to menu...");
        return true;
    }
    public abstract void displayMetrics();
    public abstract void modifyProfile();
    public abstract void modifyPersonalInfo();
    public abstract void modifyPassword();
    protected int getUserInputAsInteger() {
        while (true) {
            try {
                int returned = Integer.parseInt(InputManager.getInstance().nextLine());
                if (returned < 0) {
                    System.out.println("Invalid input. Please enter a positive number.");
                } else {
                    return returned;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }

        }
    }
    protected static void line() {
        System.out.println("--------------------------------------------------");
    }
}
