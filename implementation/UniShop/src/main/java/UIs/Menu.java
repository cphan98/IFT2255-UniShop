package UIs;

import BackEndUtility.DataBase;
import BackEndUtility.InputManager;
import Users.*;
import UtilityObjects.Notification;
import productClasses.Usages.Order;

/**
 * The Menu class is an abstract class that serves as the base for various user menus in UniShop.
 * It provides common attributes and methods for handling user interaction and menu navigation.
 */
public abstract class Menu {

    // ATTRIBUTES

    protected User user;
    protected DataBase database;
    protected final UIUtilities uiUtilities;

    // CONSTRUCTOR
    /**
     * Constructs a new Menu object.
     *
     * @param user     The user for whom the menu is created.
     * @param database The database to be used for data retrieval and storage.
     */
    public Menu(User user, DataBase database) {
        this.user = user;
        this.database = database;
        this.uiUtilities = new UIUtilities(database, user);
    }

    // GETTER
    /**
     * Gets the UIUtilities associated with this menu.
     *
     * @return The UIUtilities instance.
     */
    public UIUtilities getUiUtilities() {
        return uiUtilities;
    }

    // UTILITIES

    // main page ------------------------------------------------------------------------------------------------------
    /**
     * Displays the main menu for the user and handles user input.
     *
     * @return True if the menu should continue looping, false otherwise.
     */
    public abstract boolean displayMenu();

    // profile page ---------------------------------------------------------------------------------------------------
    /**
     * Displays the profile page for the user.
     *
     * @return True if the operation was successful, false otherwise.
     */
    public abstract boolean displayProfile();

    // followers page -------------------------------------------------------------------------------------------------
    /**
     * Displays the followers page for the user.
     *
     * @return True if the operation was successful, false otherwise.
     */
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
    /**
     * Searches for a follower based on the provided ID and interacts with the found follower.
     */
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
    /**
     * Interacts with a specific follower, providing options such as removal.
     *
     * @param pointedFollower The follower to interact with.
     */
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
    /**
     * Displays the order history page for the user.
     *
     * @return True if the operation was successful, false otherwise.
     */
    public abstract boolean displayOrderHistory();

    // order page -----------------------------------------------------------------------------------------------------
    /**
     * Interacts with a specific order, providing options for the user.
     *
     * @param order The order to interact with.
     */
    public abstract void interactWithOrder(Order order);

    // notifications --------------------------------------------------------------------------------------------------

    /**
     * Displays the notifications for the user and marks them as read upon user interaction.
     */
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


    // metrics --------------------------------------------------------------------------------------------------------
    /**
     * Displays metrics related to the user.
     */
    public abstract void displayMetrics();

    // profile modification -------------------------------------------------------------------------------------------
    /**
     * Modifies the user's profile.
     */
    public abstract void modifyProfile();

    // other ----------------------------------------------------------------------------------------------------------
    /**
     * Prints a line separator to the console.
     */
    public static void line() {
        System.out.println("--------------------------------------------------");
    }
}
