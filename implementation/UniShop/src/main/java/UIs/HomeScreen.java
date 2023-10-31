package UIs;

import LoginUtility.DataBase;
import Users.User;

public class HomeScreen {

    private DataBase database;

    public HomeScreen(DataBase database) {
        this.database = database;
    }

    public void initialize() {
        boolean continueLoop = true;
        while (continueLoop) {
            System.out.println("HEY!");
            System.out.println("Welcome to UniShop!");
            System.out.println();
            System.out.println("1. Login");
            System.out.println("2. Signup as a buyer");
            System.out.println("3. Signup as a seller");
            System.out.println("4. Exit");

            String choice = readInput();
            InputManager inputManager = InputManager.getInstance();
            switch (choice) {
                case "1":
                    System.out.println("!!!Login!!!");
                    boolean successfulLogin = redirectToLoginScreen();
                    if (successfulLogin) {
                        continueLoop = false; // break out of the loop if login is successful
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
            System.out.println("User not found");
            return false;
        }
        Menu menu = new Menu(user);
        menu.displayMainMenu();
        return true; // indicates successful login
    }
    public boolean redirectToSignupScreen(boolean isSeller) {
        SignUpScreen signUpScreen = isSeller ? new SellerSignUp() : new BuyerSignUp();
        User user = signUpScreen.getCredentials();
        if (database.addUser(user)) {
            System.out.println("User added successfully");
            System.out.println(database.toString());
        } else {
            System.out.println("User not added");
        }
        return false; // Continue loop anyway
    }

}
