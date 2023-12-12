package UIs.Buyer;

import BackEndUtility.*;
import UIs.Menu;
import UIs.Buyer.Controllers.*;
import Users.*;
import productClasses.Usages.Order;

public class BuyerMenu extends Menu {

    // ATTRIBUTES

    private Buyer user;
    private final ProfileController profileController = new ProfileController(user, database);
    private final OrderController orderController = new OrderController(user, database);
    private final CartController cartController = new CartController(user, database);
    private final CatalogController catalogController = new CatalogController(user, database);

    // CONSTRUCTOR

    public BuyerMenu(Buyer user, DataBase database) {
        super(user, database);
        this.user = user;
    }

    // UTILITIES

    // main menu ------------------------------------------------------------------------------------------------------

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
            System.out.println("7. Display Notifications");
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

    @Override
    public boolean displayProfile() {
        return false;
    }

    // order history page ---------------------------------------------------------------------------------------------

    @Override
    public boolean displayOrderHistory() {
        return false;
    }

    // order page -----------------------------------------------------------------------------------------------------

    @Override
    public void interactWithOrder(Order order) {

    }

    // metrics --------------------------------------------------------------------------------------------------------

    @Override
    public void displayMetrics() {

    }

    // profile modification -------------------------------------------------------------------------------------------
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
