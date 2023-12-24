package UIs.Seller;

import BackEndUtility.DataBase;
import UIs.Menu;
import UIs.Seller.Controllers.InventoryController;
import UIs.Seller.Controllers.OrderController;
import UIs.Seller.Controllers.ProfileController;
import Users.Buyer;
import Users.Seller;
import productClasses.Usages.Order;
/**
 * The SellerMenu class represents the menu and user interface for a Seller in UniShop.
 * It extends the Menu class and provides specific functionality related to sellers,
 * such as displaying the profile, order history, inventory, notifications, metrics, and managing customers.
 */
public class SellerMenu extends Menu {

    // ATTRIBUTES

    private final Seller user;
    private final ProfileController profileController;
    private final OrderController orderController;
    private final InventoryController inventoryController;

    // CONSTRUCTOR
    /**
     * Constructs a new SellerMenu object.
     *
     * @param user     The Seller user for whom the menu is created.
     * @param database The database to be used for data retrieval and storage.
     */
    public SellerMenu(Seller user, DataBase database) {
        super(user, database);
        this.user = user;
        this.profileController = new ProfileController(user, database);
        this.orderController = new OrderController(user, database);
        this.inventoryController = new InventoryController(user, database);
    }

    // UTILITIES

    // main menu ------------------------------------------------------------------------------------------------------
    /**
     * Displays the main menu for the Seller, providing various options, as, display their profile, order history,
     * the inventory of products, their notifications, metrics, followers, customers, and log out
     * by handling user input.
     *
     * @return True if the menu should continue looping, false if the user chooses to log out.
     */
    @Override
    public boolean displayMenu() {
        boolean continueLoop = true;

        while (continueLoop) {
            line();
            System.out.println("Welcome to UniShop, " + user.getId() + "!");
            line();
            System.out.println();
            System.out.println("Please select an option:");

            System.out.println("1. Display Profile");
            System.out.println("2. Display Order History");
            System.out.println("3. Display Inventory");
            int unreadNotifications = user.getNotifications().stream().filter(notification -> !notification.isRead()).toArray().length;
            System.out.println("4. Display Notifications" + (unreadNotifications == 0 ? "" : " (" + unreadNotifications  + " new)"));
            System.out.println("5. Display Metrics");
            System.out.println("6. Display Followers");
            System.out.println("7. Display Your Customers");
            System.out.println("8. Log out");
            int choice = uiUtilities.getUserInputAsInteger();

            switch (choice) {
                case 1:
                    continueLoop = profileController.displayProfile();
                    break;
                case 2:
                    continueLoop = orderController.displayOrderHistory();
                    break;
                case 3:
                    continueLoop = inventoryController.displayInventory();
                    break;
                case 4:
                    displayNotifications();
                    break;
                case 5:
                    profileController.displayMetrics();
                    break;
                case 6:
                    continueLoop = displayFollowers();
                    break;
                case 7:
                    continueLoop = displayCustomers();
                    break;
                case 8:
                    return false;  // Add this to handle log out
                default:
                    System.out.println();
                    System.out.println("Invalid selection. Please try again.");
            }
        }
        return true;
    }

    // profile page ---------------------------------------------------------------------------------------------------
    /**
     * Displays the profile page for the seller.
     *
     * @return false.
     */
    @Override
    public boolean displayProfile() {
        return false;
    }

    // order history page ---------------------------------------------------------------------------------------------
    /**
     * Displays the order history page for the seller.
     *
     * @return false.
     */
    @Override
    public boolean displayOrderHistory() {
        return false;
    }

    // order page -----------------------------------------------------------------------------------------------------
    /**
     * Interacts with a specific order, providing options for the seller.
     *
     * @param order The order to interact with.
     */
    @Override
    public void interactWithOrder(Order order) {

    }

    // metrics --------------------------------------------------------------------------------------------------------
    /**
     * Displays metrics related to the seller.
     */
    @Override
    public void displayMetrics() {

    }

    // profile modification -------------------------------------------------------------------------------------------
    /**
     * Modifies the user's profile.
     */
    @Override
    public void modifyProfile() {

    }

    // customers ------------------------------------------------------------------------------------------------------
    /**
     * Displays a list of customers associated with the Seller.
     *
     * @return True, indicating that the operation was successful.
     */
    public boolean displayCustomers() {
        System.out.println("Your customers: ");
        int i = 0;
        for (Buyer customer : user.getCustomers()) {
            System.out.println(i + ". " + customer.getId());
        }
        System.out.println("You have " + user.getCustomers().size() + " customers.");
        return true;
    }
}
