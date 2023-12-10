package UIs.Buyer.Controllers;

import BackEndUtility.DataBase;
import BackEndUtility.InputManager;
import UIs.Buyer.BuyerMenu;
import Users.Buyer;
import UtilityObjects.CreditCard;

import java.util.ArrayList;

public class ProfileController extends BuyerMenu {

    // ATTRIBUTES

    private Buyer user;
    private DataBase dataBase;

    // CONSTRUCTOR

    public ProfileController(Buyer user, DataBase database) {
        super(user, database);
    }

    // UTILITIES

    // profile page ---------------------------------------------------------------------------------------------------

    public boolean displayProfile() {
        boolean continueLoop = true;
        while (continueLoop) {
            line();
            System.out.println("PROFILE");
            System.out.println("Name: " + user.getId());
            System.out.println("Email: " + user.getEmail());
            System.out.println("Buying points: " + user.getMetrics().getBuyPoints());
            System.out.println();
            System.out.println("METRICS");
            displayMetricsProfil();

            line();
            System.out.println();
            System.out.println("1. Modify profile");
            System.out.println("2. Return to menu");
            System.out.println("3. Delete account");
            int choice = uiUtilities.getUserInputAsInteger();

            switch (choice) {
                // modify profile
                case 1:
                    System.out.println();
                    System.out.println("Modifying profile...");
                    modifyProfile();
                    break;

                // return to menu
                case 2:
                    System.out.println();
                    System.out.println("Returning to menu...");
                    continueLoop = false;
                    break;

                // delete account
                case 3:
                    System.out.println();
                    System.out.println("Deleting account...");
                    uiUtilities.deleteAccount();
                    return false;

                // invalid input
                default:
                    System.out.println();
                    System.out.println("Invalid selection. Please try again.");
                    return false;  // continue the loop
            }
        }
        return true;  // continue the loop
    }

    // ranks ----------------------------------------------------------------------------------------------------------

    public void displayRanks() {
        System.out.println("Your rank is the " + user.getYourRank());
        int i = 0;
        for (Buyer buyer : user.getTop5ExpBuyers())
        {
            i++;
            System.out.println(i + ")" + "\t\t" + buyer.getId() + ": " + buyer.getMetrics().getExpPoints());
        }
    }

    // buyers ---------------------------------------------------------------------------------------------------------

    public boolean displayBuyers() {
        line();
        System.out.println("BUYERS");
        line();
        ArrayList<Buyer> buyers = database.getBuyers();
        for (Buyer buyer : buyers) {
            if (user.getBuyersFollowed().contains(buyer)) {
                System.out.println("You are following this buyer!");
            }
            System.out.println("ID: " + buyer.getId());
            System.out.println("Name: " + buyer.getFirstName());
            System.out.println("Points: " + buyer.getMetrics().getExpPoints());
        }
        line();

        boolean continueLoop = true;
        while (continueLoop) {
            System.out.println("1. Get a buyer's profile");
            System.out.println("2. Filter buyers");
            System.out.println("3. Display your followers");
            System.out.println("4. Return to menu");
            int choice = uiUtilities.getUserInputAsInteger();

            switch (choice) {
                case 1 -> continueLoop = searchBuyer();
                case 2 -> continueLoop = filterBuyers();
                case 3 -> continueLoop = displayFollowers();
                case 4 -> {
                    System.out.println("Returning to menu...");
                    continueLoop = false;
                }
                default -> System.out.println("Invalid selection. Please try again.");
            }
        }
        return true;
    }

    private boolean searchBuyer() {
        line();
        ArrayList<Buyer> pointedBuyer = null;
        boolean continueLoop = true;
        while (continueLoop) {
            System.out.println("1. Search by ID");
            System.out.println("2. Search by name");
            System.out.println("3. Return to menu");
            int choice = uiUtilities.getUserInputAsInteger();

            switch (choice) {
                case 1 -> {
                    System.out.println("Enter the id of the buyer you want to view:");
                    String id = InputManager.getInstance().nextLine();
                    pointedBuyer = database.searchBuyerById(id);
                    if (pointedBuyer == null) {
                        System.out.println("Buyer not found.");
                    } else {
                        System.out.println(pointedBuyer);
                        while (true) {
                            System.out.println("Enter the desired buyer to view their profile:");
                            int i = 0;
                            for (Buyer buyer : pointedBuyer) {
                                i++;
                                System.out.println(i + ") " + buyer.getId());
                            }
                            int index = uiUtilities.getUserInputAsInteger();
                            if (index > 0 && index <= pointedBuyer.size()) {
                                interactWithBuyer(pointedBuyer.get(index-1));
                                break;
                            } else {
                                System.out.println("Invalid selection. Please try again.");
                            }
                        }

                    }
                }

                case 2 -> {
                    System.out.println("Enter the name of the buyer you want to view:");
                    String name = InputManager.getInstance().nextLine();
                    pointedBuyer = database.searchBuyerByName(name);
                    if (pointedBuyer == null) {
                        System.out.println("Buyer not found.");
                    } else {
                        while (true) {
                            System.out.println("Enter the desired buyer to view their profile:");
                            int i = 0;
                            for (Buyer buyer : pointedBuyer) {
                                i++;
                                System.out.println(i + ") " + buyer.getId());
                            }
                            int index = uiUtilities.getUserInputAsInteger();
                            if (index > 0 && index <= pointedBuyer.size()) {
                                interactWithBuyer(pointedBuyer.get(index-1));
                                break;
                            } else {
                                System.out.println("Invalid selection. Please try again.");
                            }
                        }
                    }
                }
                case 3 -> {
                    System.out.println("Returning to menu...");
                    continueLoop = false;
                }
                default -> System.out.println("Invalid selection. Please try again.");
            }
        }
        return true;
    }

    public void interactWithBuyer(Buyer pointedBuyer) {
        if (user.getBuyersFollowed().contains(pointedBuyer)) {
            System.out.println("1. Unfollow this buyer");
        } else {
            System.out.println("1. Follow this buyer");
        }
        System.out.println("2. View Profile");
        System.out.println("3. Return to menu");
        int choice = uiUtilities.getUserInputAsInteger();
        switch (choice) {
            case 1 -> uiUtilities.toggleBuyerToFollowing(user, pointedBuyer);
            case 2 -> displayBuyersProfile(pointedBuyer);
            case 3 -> {
                System.out.println("Returning to menu...");
            }
            default -> System.out.println("Invalid selection. Please try again.");
        }
    }

    public boolean filterBuyers() {
        System.out.println("1. Sort by name");
        System.out.println("2. Sort by ID");
        System.out.println("3. Sort by followers");
        System.out.println("4. Sort by experience points");
        System.out.println("5. Return to menu");
        int choice = uiUtilities.getUserInputAsInteger();
        switch (choice) {
            case 1:
                System.out.println("1. Ascending");
                System.out.println("2. Descending");
                System.out.println("3. Return to menu");
                int choice1 = uiUtilities.getUserInputAsInteger();
                switch (choice1) {
                    case 1 -> database.sortBuyer(true, "name");
                    case 2 -> database.sortBuyer(false, "name");
                    case 3 -> System.out.println("Returning to menu...");
                    default -> System.out.println("Invalid selection. Please try again.");
                }
                break;
            case 2:
                System.out.println("1. Ascending");
                System.out.println("2. Descending");
                System.out.println("3. Return to menu");
                int choice2 = uiUtilities.getUserInputAsInteger();
                switch (choice2) {
                    case 1 -> database.sortBuyer(true, "ID");
                    case 2 -> database.sortBuyer(false, "ID");
                    case 3 -> System.out.println("Returning to menu...");
                    default -> System.out.println("Invalid selection. Please try again.");
                }
                break;
            case 3:
                System.out.println("1. Ascending");
                System.out.println("2. Descending");
                System.out.println("3. Return to menu");
                int choice3 = uiUtilities.getUserInputAsInteger();
                switch (choice3) {
                    case 1 -> database.sortBuyer(true, "followers");
                    case 2 -> database.sortBuyer(false, "followers");
                    case 3 -> System.out.println("Returning to menu...");
                    default -> System.out.println("Invalid selection. Please try again.");
                }
                break;
            case 4:
                System.out.println("1. Ascending");
                System.out.println("2. Descending");
                System.out.println("3. Return to menu");
                int choice4 = uiUtilities.getUserInputAsInteger();
                switch (choice4) {
                    case 1 -> database.sortBuyer(true, "xp");
                    case 2 -> database.sortBuyer(false, "xp");
                    case 3 -> System.out.println("Returning to menu...");
                    default -> System.out.println("Invalid selection. Please try again.");
                }
                break;
        }
        return true;
    }

    private void displayBuyersProfile(Buyer buyer) {
        line();
        System.out.println("Buyer's name: " + buyer.getFirstName() + "\t" + buyer.getLastName());
        System.out.println("Buyer's ID: " + buyer.getId());
        System.out.println(buyer.getMetrics().toString());
    }

    // profile modifications ------------------------------------------------------------------------------------------

    public void modifyProfile() {
        System.out.println();
        System.out.println("1. Modify personal info");
        System.out.println("2. Modify shipping address");
        System.out.println("3. Modify password");
        System.out.println("4. Modify payment info");
        System.out.println("5. Return to menu");
        int choice = uiUtilities.getUserInputAsInteger();
        switch (choice) {
            case 1 -> uiUtilities.modifyPersonalInfo(user);
            case 2 -> uiUtilities.modifyAddress();
            case 3 -> uiUtilities.modifyPassword();
            case 4 -> modifyPaymentInfo();
            case 5 -> {
                System.out.println();
                System.out.println("Returning to menu...");
            }
            default -> {
                System.out.println();
                System.out.println("Invalid selection. Please try again.");
            }
        }
    }

    public void modifyPaymentInfo() {
        System.out.println("Enter your credit card number:");
        String cardNumber = InputManager.getInstance().nextLine();
        System.out.println("Enter your credit card expiration date:");
        String expirationDate = InputManager.getInstance().nextLine();
        System.out.println("Enter your credit card owner's first name:");
        String ownerName = InputManager.getInstance().nextLine();
        System.out.println("Enter your credit card owner's last name:");
        String ownerLastName = InputManager.getInstance().nextLine();
        user.setCard(new CreditCard(cardNumber, ownerName, ownerLastName, expirationDate));
        System.out.println("Payment info modified");
    }

    // metrics --------------------------------------------------------------------------------------------------------

    public void displayMetricsProfil() {
        if (user.getMetrics().getSelectedMetrics().isEmpty()) {
            System.out.println("No metrics selected");
            return;
        }
        for (String metrics : user.getMetrics().getSelectedMetrics()) {
            System.out.println(metrics);
        }
    }

    public void displayMetrics(){
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
