package UIs.Buyer;

import BackEndUtility.DataBase;
import BackEndUtility.InputManager;
import UIs.SignUpScreen;
import UtilityObjects.Address;
import Users.Buyer;

/**
 * Class representing the buyer's sign up screen.
 *
 * This class manages a buyer's sign up, and extends SignUpScreen.
 */
public class BuyerSignUp implements SignUpScreen {

    // ATTRIBUTES

    private final DataBase database;

    // CONSTRUCTOR

    /**
     * Constructs an instance of BuyerSignUp with a given database.
     *
     * @param database  DataBase of UniShop, where the buyer will be added
     */
    public BuyerSignUp(DataBase database) {
        this.database = database;
    }

    // UTILITIES

    // credentials ----------------------------------------------------------------------------------------------------

    /**
     * Gets the necessary information from the user in order to sign them up as a buyer. Once the buyer is signed up,
     * they are required to sign in within 24h in order for their account to remain valid.
     */
    @Override
    public void getCredentialsAndSignUp() {
        InputManager inputManager = InputManager.getInstance();
        System.out.println("Please enter your first name:");
        String firstName = inputManager.nextLine();
        System.out.println("Please enter your last name:");
        String lastName = inputManager.nextLine();
        System.out.println("Please enter your username:");
        String username = inputManager.nextLine();
        System.out.println("Please enter your password:");
        String password = inputManager.nextLine();
        System.out.println("Please enter your address:");
        String address = inputManager.nextLine();
        System.out.println("Please enter your country:");
        String country = inputManager.nextLine();
        System.out.println("Please enter your province:");
        String province = inputManager.nextLine();
        System.out.println("Please enter your city:");
        String city = inputManager.nextLine();
        System.out.println("Please enter your postal code:");
        String postalCode = inputManager.nextLine();
        String email = "";
        while (!email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
            System.out.println("Please enter your email:");
            email = inputManager.nextLine();
        }
        String phoneNumber = "a";
        while (!phoneNumber.matches("[0-9]+")) {
            System.out.println("Enter your phone number:");
            phoneNumber = InputManager.getInstance().nextLine();
        }
        System.out.println("WARNING: You must connect within the next 24 hours or else the signup will be cancelled");
        database.addUser(new Buyer(firstName, lastName, username, password, email, phoneNumber,
                new Address(address, country, province, city, postalCode)));

    }
}
