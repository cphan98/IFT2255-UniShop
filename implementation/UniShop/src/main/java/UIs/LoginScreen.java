package UIs;

import LoginUtility.DataBase;
import Users.Buyer;
import Users.User;

public class LoginScreen {

    public String[] askCredentials() {

        InputManager inputManager = InputManager.getInstance();
        System.out.println("Enter your id:");
        String id = inputManager.nextLine();
        System.out.println("Enter your password:");
        String password = inputManager.nextLine();
        return new String[]{id, password};
    }
    public User loginUser(String id, String password, DataBase database) {
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
                System.out.println("Database : \n" + database);
                return null;
            }
            return user;
        }
        return null;
    }
}
