package UIs;

import Users.Seller;
import Users.User;
import otherUtility.Category;

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
        System.out.println("1. Books");
        System.out.println("2. Learning resources");
        System.out.println("3. Stationery");
        System.out.println("4. Electronics");
        System.out.println("5. Desktop accessories");
        String choice = inputManager.nextLine();
        Category category = null;
        while (category == null) {
            switch (choice) {
                case "1":
                    category = Category.BOOKS;
                    break;
                case "2":
                    category = Category.LEARNING_RESOURCES;
                    break;
                case "3":
                    category = Category.STATIONERY;
                    break;
                case "4":
                    category = Category.ELECTRONICS;
                    break;
                case "5":
                    category = Category.DESKTOP_ACCESSORIES;
                    break;
                default:
                    System.out.println("Invalid choice, try again");
            }
        }

        return new Seller(username, password, email, phoneNumber, address, category);
    }
}
