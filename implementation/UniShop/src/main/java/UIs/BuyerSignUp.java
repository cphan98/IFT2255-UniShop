package UIs;

import Users.Address;
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
        System.out.println("Please enter your email:");
        String email = inputManager.nextLine();
        System.out.println("Please enter your phone number:");
        String phoneNumber = inputManager.nextLine();
        System.out.println("WARNING: You must connect within the next 24 hours or else the signup will be cancelled");
        return new Buyer(firstName, lastName, username, password, email, phoneNumber, new Address(address, country, province, city, postalCode));

    }
}
