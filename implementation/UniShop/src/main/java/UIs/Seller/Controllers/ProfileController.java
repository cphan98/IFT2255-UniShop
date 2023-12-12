package UIs.Seller.Controllers;

import BackEndUtility.DataBase;
import UIs.Seller.SellerMenu;
import Users.Seller;

public class ProfileController extends SellerMenu {

    // ATTRIBUTES

    private Seller user;
    private DataBase database;

    // CONSTRUCTOR

    public ProfileController(Seller user, DataBase database) {
        super(user, database);
    }

    // UTILITIES

    // profile page ---------------------------------------------------------------------------------------------------

    @Override
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

    @Override
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

    @Override
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
