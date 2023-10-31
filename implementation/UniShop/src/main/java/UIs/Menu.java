package UIs;

import Users.User;

import java.util.Scanner;

public class Menu {
    private User user;
    public Menu(User user) {
        this.user = user;
    }

    public void displayMainMenu() {
        System.out.println("Welcome to UniShop");
        System.out.println("1. Profile");
        System.out.println("2. Order History");
        System.out.println("3. Users.Cart");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                displayProfile();
                break;
            case 2:
                displayOrderHistory();
                break;
            case 3:
                displayCart();
                break;
            default:
                System.out.println("Invalid choice");
                break;
        }
    }
    public void displayProfile() {
        //TODO
        System.out.println(user.getId() + " " + user.getPassword());
    }
    public void displayOrderHistory() {
        //TODO
        System.out.println("Order History");
    }
    public void displayCart() {
        //TODO
        System.out.println("Users.Cart");
    }

}
