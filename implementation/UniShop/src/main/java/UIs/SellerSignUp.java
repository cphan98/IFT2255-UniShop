package UIs;

import Users.Seller;
import Users.User;

import static java.lang.Integer.parseInt;

public class SellerSignUp implements SignUpScreen {
    public User getCredentials() {
        System.out.println("Welcome, new seller!");
        InputManager inputManager = InputManager.getInstance();
        System.out.println("Please enter your username:");
        String username = inputManager.nextLine();
        System.out.println("Please enter your password:");
        String password = inputManager.nextLine();
        System.out.println("Please enter your email:");
        String email = inputManager.nextLine();
        System.out.println("Please enter your phone number:");
        int phoneNumber = parseInt(inputManager.nextLine());
        System.out.println("Please enter your address:");
        String address = inputManager.nextLine();
        System.out.println("Please enter your category:");
        String category = inputManager.nextLine();
        return new Seller(username, password, email, phoneNumber, address, category);
    }
}
