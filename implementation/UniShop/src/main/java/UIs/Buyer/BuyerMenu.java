package UIs.Buyer;

import BackEndUtility.*;
import UIs.Menu;
import UIs.Buyer.Controllers.*;
import Users.*;
import productClasses.Usages.Order;

/**
 * Class representing the buyer's menu page.
 *
 * This class contains methods to be implemented by controllers, and extends Menu.
 */
public class BuyerMenu extends Menu {

    // ATTRIBUTES

    private final Buyer user;
    private final ProfileController profileController;
    private final OrderController orderController;
    private final CartController cartController;
    private final CatalogController catalogController;

    // CONSTRUCTOR

    /**
     * Constructs an instance of BuyerMenu with given user and databse.
     *
     * @param user      Buyer, the user is a buyer
     * @param database  DataBase of UniShop containing all necessary information about users, products and orders
     */
    public BuyerMenu(Buyer user, DataBase database) {
        super(user, database);
        this.user = user;
        this.profileController = new ProfileController(user, database);
        this.orderController = new OrderController(user, database);
        this.cartController = new CartController(user, database);
        this.catalogController = new CatalogController(user, database);
    }

    // UTILITIES

    // main menu ------------------------------------------------------------------------------------------------------

    /**
     * Displays options for the buyer to choose from: display profile, display order history, display cart, display
     * wishlist, display catalog, display buyers, display notifications, display metrics, and log out.
     *
     * Returns true in order for the calling method to continue the interaction loop.
     *
     * @return  Boolean, true in order for the calling method to continue the interaction loop
     */
    @Override
    public boolean displayMenu() {

        boolean continueLoop = true;

        while (continueLoop) {
            line();
            System.out.println("Welcome to UniShop, " + user.getId() + "!");
            line();
            System.out.println();
            profileController.displayRanks();
            System.out.println();
            line();
            System.out.println("Please select an option:");

            System.out.println("1. Display Profile");
            System.out.println("2. Display Order History");
            System.out.println("3. Display Cart");
            System.out.println("4. Display Wishlist");
            System.out.println("5. Display Catalog");
            System.out.println("6. Display Buyers");
            int unreadNotifications = user.getNotifications().stream().filter(notification -> !notification.isRead()).toArray().length;
            System.out.println("7. Display Notifications" + (unreadNotifications == 0 ? "" : " (" + unreadNotifications  + " new)"));
            System.out.println("8. Display Metrics");
            System.out.println("9. Log out");
            int choice = uiUtilities.getUserInputAsInteger();

            switch (choice) {
                case 1 -> continueLoop = profileController.displayProfile();
                case 2 -> continueLoop = orderController.displayOrderHistory();
                case 3 -> continueLoop = cartController.displayCart();
                case 4 -> continueLoop = displayWishList();
                case 5 -> continueLoop = catalogController.displayCatalog();
                case 6 -> continueLoop = profileController.displayBuyers();
                case 7 -> displayNotifications();
                case 8 -> profileController.displayMetrics();
                case 9 -> {
                    return false;  // Add this to handle log out
                }
                default -> {
                    System.out.println();
                    System.out.println("Invalid selection. Please try again.");
                }
            }
        }
        return true;
    }

    // profile page ---------------------------------------------------------------------------------------------------

    /**
     * Displays the buyer's profile and returns true in order for the calling method to continue the interaction loop.
     *
     * @return  Boolean, true in order for the calling method to continue the interaction loop
     */
    @Override
    public boolean displayProfile() {
        return false;
    }

    // order history page ---------------------------------------------------------------------------------------------

    /**
     * Displays the order history and returns true in order for the calling method to continue the interaction loop.
     *
     * @return  Boolean, true in order for the calling method to continue the interaction loop
     */
    @Override
    public boolean displayOrderHistory() {
        return false;
    }

    // order page -----------------------------------------------------------------------------------------------------

    /**
     * Displays options for the buyer to interact with a selected order.
     *
     * @param order Order to interact with
     */
    @Override
    public void interactWithOrder(Order order) {
      
    }

    // metrics --------------------------------------------------------------------------------------------------------

    /**
     * Displays the buyer's metrics.
     */
    @Override
    public void displayMetrics() {

    }

    // profile modification -------------------------------------------------------------------------------------------

    /**
     * Displays options for the buyer to modify their profile.
     */
    @Override
    public void modifyProfile() {

    }

    // wish list ------------------------------------------------------------------------------------------------------

    private boolean displayWishList() {
        System.out.println("WISHLIST");
        boolean continueLoop = true;
        while (continueLoop) {
            System.out.println(user.wishListToString());
            System.out.println("Type the number of the product you want to add to the cart, or 0 to return to menu:");
            int choice = uiUtilities.getUserInputAsInteger();
            if (choice < 0 || choice > user.getWishList().size()) {
                System.out.println("Invalid choice");
            }
            else if (choice == 0) {
                System.out.println("Returning to menu...");
                continueLoop = false;
            }
            else {
                catalogController.addProductToCart(user.getWishList().get(choice-1));
            }
        }
        return true;
    }
}
