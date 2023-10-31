package UIs;

import Users.Buyer;
import Users.User;

import static java.lang.Integer.parseInt;

public class BuyerSignUp implements SignUpScreen {
    public User getCredentials() {
        InputManager inputManager = InputManager.getInstance();
        System.out.println("Please enter your first name:");
        String firstName = inputManager.nextLine();
        System.out.println("Please enter your last name:");
        String lastName = inputManager.nextLine();
        System.out.println("Please enter your username:");
        String username = inputManager.nextLine();
        System.out.println("Please enter your password:");
        String password = inputManager.nextLine();
        System.out.println("Please enter your shipping address:");
        String address = inputManager.nextLine();
        System.out.println("Please enter your email:");
        String email = inputManager.nextLine();
        System.out.println("Please enter your phone number:");
        int phoneNumber = parseInt(inputManager.nextLine());
        return new Buyer(firstName, lastName, username, password, email, phoneNumber, address);

    }
}
