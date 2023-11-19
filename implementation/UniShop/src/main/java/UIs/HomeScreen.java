package UIs;

import BackEndUtility.DataBase;
import BackEndUtility.InputManager;
import UIs.Buyer.BuyerMenu;
import UIs.Buyer.BuyerSignUp;
import UIs.Seller.SellerMenu;
import UIs.Seller.SellerSignUp;
import Users.Buyer;
import Users.Seller;
import Users.User;

public class HomeScreen {

    private DataBase database;


    public HomeScreen(DataBase database) {
        this.database = database;
    }

    public void initialize() {
        boolean continueLoop = true;
        while (continueLoop) {
            line();
            System.out.println("HEY!");
            System.out.println("Welcome to UniShop!");
            System.out.println("For further actions, unless prompted to do otherwise, please enter the number corresponding to your choice.");
            System.out.println("For example, if the option is '1. Login', please enter '1' to login.");
            line();
            System.out.println();
            System.out.println("1. Login");
            System.out.println("2. Signup as a buyer");
            System.out.println("3. Signup as a seller");
            System.out.println("4. Exit");

            String choice = readInput();
            switch (choice) {
                case "1":
                    System.out.println("Login:");
                    boolean successfulLogin = redirectToLoginScreen();
                    if (successfulLogin) {
                        continue; // Instead of breaking out, just continue to show the HomeScreen again
                    }
                    break;
                case "2":
                    if (redirectToSignupScreen(false)) {
                        continueLoop = false; // End the loop if user was added successfully
                    }
                    break;
                case "3":
                    if (redirectToSignupScreen(true)) {
                        continueLoop = false; // End the loop if user was added successfully
                    }
                    break;
                case "4":
                    continueLoop = false; // end the loop
                    break;
                default:
                    System.out.println("Invalid choice");
                    break;
            }
        }
    }

    public String readInput() {
        InputManager inputManager = InputManager.getInstance();
        return inputManager.nextLine();
    }

    public boolean redirectToLoginScreen() {
        LoginScreen loginScreen = new LoginScreen();
        String[] credentials = loginScreen.askCredentials();
        User user = loginScreen.loginUser(credentials[0], credentials[1], database);
        if (user == null) {
            System.out.println("The username or password is incorrect.");
            return false;
        }
        Menu menu = user instanceof Buyer ? new BuyerMenu((Buyer) user, database) : new SellerMenu((Seller) user, database);
        boolean continueLoop = menu.displayMenu();
        return !continueLoop; // indicates successful login
    }
    public boolean redirectToSignupScreen(boolean isSeller) {
        SignUpScreen signUpScreen = isSeller ? new SellerSignUp(database) : new BuyerSignUp(database);
        signUpScreen.getCredentialsAndSignUp();

        return false; // Continue loop anyway
    }

    private static void line() {
        System.out.println("--------------------------------------------------");
    }

}
