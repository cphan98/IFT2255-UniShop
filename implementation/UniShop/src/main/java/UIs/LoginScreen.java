package UIs;

import BackEndUtility.DataBase;
import BackEndUtility.InputManager;
import Users.Buyer;
import Users.User;

/**
 * Class that represents the login screen of the platform, which allows the user to login
 */
public class LoginScreen {

    // UTILITIES

    // credentials ----------------------------------------------------------------------------------------------------

    /**
     * Method that redirects the user to the login screen, which allows him to login
     * @return an array containing the id and the password that the user entered
     */
    protected String[] askCredentials() {
        InputManager inputManager = InputManager.getInstance();
        System.out.println("Enter your id:");
        String id = inputManager.nextLine();
        System.out.println("Enter your password:");
        String password = inputManager.nextLine();
        return new String[]{id, password};
    }

    // login page -----------------------------------------------------------------------------------------------------

    /**
     * Method that logs the user in if the credentials are correct and the 24h time limit has not been passed
     *
     * @param id the id of the user
     * @param password the password of the user
     * @param database the database of the platform
     * @return the user that logged in, null if the credentials are incorrect or the 24h time limit has been passed
     */
    protected User loginUser(String id, String password, DataBase database) {
        User user = database.getUser(id, password);
        if (user != null) {
            if (database.check24H(user)) {
                if (user instanceof Buyer) {
                    System.out.println("Buyer logged in successfully");
                } else {
                    System.out.println("Seller logged in successfully");
                }
            } else {
                System.out.println("Time limit has been passed, your signup has been cancelled. Please signup again.");
                database.removeUser(user);
                return null;
            }
            return user;
        }
        return null;
    }
}
