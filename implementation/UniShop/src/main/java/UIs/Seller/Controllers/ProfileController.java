package UIs.Seller.Controllers;

import BackEndUtility.DataBase;
import UIs.Seller.SellerMenu;
import UIs.UIUtilities;
import Users.Seller;

/**
 * Class managing a seller's profile.
 *
 * In this class, a seller can display their metrics, modify their profile, or deleter their account.
 */
public class ProfileController {

    // ATTRIBUTES

    private final Seller user;
    private final DataBase database;
    private final UIUtilities uiUtilities;

    // CONSTRUCTOR

    /**
     * Constructs an instance of ProfileController with given user and database.
     *
     * @param user      Seller, user is a seller
     * @param database  DataBase of UniShop containing information about this seller
     */
    public ProfileController(Seller user, DataBase database) {
        this.database = database;
        this.user = user;
        this.uiUtilities = new UIUtilities(database, user);
    }

    // UTILITIES

    // profile page ---------------------------------------------------------------------------------------------------

    /**
     * Displays the seller's profile, including their metrics. The seller can modify their profile or deleter their
     * account.
     *
     * Returns true in order for the calling method to continue the interaction loop.
     *
     * @return  Boolean, true in order for the calling method to continue the interaction loop
     */
    public boolean displayProfile() {
        boolean continueLoop = true;
        while (continueLoop) {
            System.out.println("PROFILE");
            System.out.println("Name: " + user.getId());
            System.out.println("Email: " + user.getEmail());
            System.out.println();
            System.out.println("METRICS");
            displayProfileMetrics();
            System.out.println();
            System.out.println("1. Modify profile");
            System.out.println("2. Return to menu");
            System.out.println("3. Delete account");
            int choice = uiUtilities.getUserInputAsInteger();

            switch (choice) {
                case 1:
                    System.out.println();
                    System.out.println("Modifying profile...");
                    modifyProfile();
                    break;
                case 2:
                    System.out.println();
                    System.out.println("Returning to menu...");
                    continueLoop = false;
                    break;
                case 3:
                    System.out.println();
                    System.out.println("Deleting account...");
                    uiUtilities.deleteAccount();
                    return false;
                default:
                    System.out.println();
                    System.out.println("Invalid selection. Please try again.");
                    return false;  // continue the loop
            }
        }
        return true;  // continue the loop
    }

    // profile modifications ------------------------------------------------------------------------------------------

    /**
     * Modifies the seller's profile. The seller can modify their personal information, address, and/or password.
     */
    public void modifyProfile() {
        System.out.println("1. Modify personal info");
        System.out.println("2. Modify address");
        System.out.println("3. Modify password");
        System.out.println("4. Return to menu");
        int choice = uiUtilities.getUserInputAsInteger();
        switch (choice) {
            case 1 -> uiUtilities.modifyPersonalInfo(user);
            case 2 -> uiUtilities.modifyAddress();
            case 3 -> uiUtilities.modifyPassword();
            case 4 -> System.out.println("Returning to menu...");
            default -> System.out.println("Invalid selection. Please try again.");

        }
    }

    // metrics --------------------------------------------------------------------------------------------------------

    private void displayProfileMetrics() {
        if(! user.getMetrics().getSelectedMetrics().isEmpty()){
            System.out.println(user.getMetrics().getSelectedMetrics().get(0));
            System.out.println(user.getMetrics().getSelectedMetrics().get(1));
            System.out.println(user.getMetrics().getSelectedMetrics().get(2));
        }
    }

    /**
     * Displays the seller's metrics. The seller can also configure which metrics to display on their profile.
     */
    public void  displayMetrics() {
        boolean continueLoop = true;
        while (continueLoop){
            System.out.println("All metrics available for " + user.getId() + " : ");
            System.out.println();
            System.out.println(user.getMetrics().AlltoString());
            System.out.println();
            System.out.println("1. Configure metrics to display in profile (3 max.)");
            System.out.println("2. Return to menu ");
            int choice = uiUtilities.getUserInputAsInteger();

            switch (choice){
                case 1:
                    user.getMetrics().configureMetrics();
                    break;
                case 2: continueLoop = false;
            }
        }
    }
}
