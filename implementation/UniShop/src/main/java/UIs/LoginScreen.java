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
            if (user instanceof Buyer) {
                System.out.println("Buyer logged in successfully");
            } else {
                System.out.println("Seller logged in successfully");
            }
            return user;
        }
        return null;
    }
}
