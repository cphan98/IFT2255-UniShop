package UIs.Buyer.Controllers;

import BackEndUtility.DataBase;
import BackEndUtility.InputManager;
import UIs.Buyer.BuyerMenu;
import UIs.UIUtilities;
import Users.Buyer;
import UtilityObjects.Address;
import UtilityObjects.CreditCard;
import productClasses.Product;

import java.util.Objects;

public class CartController {

    // ATTRIBUTES

    private final Buyer user;
    private final DataBase database;
    private final UIUtilities uiUtilities;

    // CONSTRUCTOR

    public CartController(Buyer user, DataBase database) {
        this.user = user;
        this.database = database;
        this.uiUtilities = new UIUtilities(database, user);
    }

    // UTILITIES

    // shopping cart page ---------------------------------------------------------------------------------------------

    public boolean displayCart() {
        System.out.println(user.getCart().toString());
        System.out.println();
        System.out.println("1. Proceed to checkout");
        System.out.println("2. Remove an item from the cart");
        System.out.println("3. Empty cart");
        System.out.println("4. Return to menu");
        int choice = uiUtilities.getUserInputAsInteger();
        switch (choice) {
            case 1 -> {
                System.out.println("Proceeding to checkout...");
                if (user.getCart().getProducts().isEmpty()) {
                    System.out.println("Your cart is empty!");
                    return true;
                }
                makeCheckout();
            }
            case 2 -> removeProductFromCart();
            case 3 -> emptyCart();
            case 4 -> System.out.println("Returning to menu...");
            default -> System.out.println("Invalid selection. Returning to menu...");
        }
        return true;  // continue the loop
    }

    // checkout -------------------------------------------------------------------------------------------------------

    private void makeCheckout() {
        InputManager im = InputManager.getInstance();
        System.out.println("You currently have " + user.getMetrics().getBuyPoints() + " points\n");
        System.out.println("Do you want to use your registered info for this order? (y/n)");
        String choice = "";
        while (!choice.matches("[yn]")) {
            choice = im.nextLine();
        }
        if (user.getCard() == null) {
            System.out.println("You don't have a credit card registered");
            System.out.println("Enter your credit card number:");
            String cardNumber = im.nextLine();
            System.out.println("Enter your credit card expiration date:");
            String expirationDate = im.nextLine();
            System.out.println("Enter your credit card owner's first name:");
            String ownerName = im.nextLine();
            System.out.println("Enter your credit card owner's last name:");
            String ownerLastName = im.nextLine();
            user.setCard(new CreditCard(cardNumber, ownerName, ownerLastName, expirationDate));
        }
        if (Objects.equals(choice, "y")) {
            System.out.println("Enter your payment type (credit card/points):");
            String paymentType = "";
            while (!paymentType.matches("credit card|points")) {
                paymentType = im.nextLine();
            }
            if (Objects.equals(paymentType, "credit card")) {
                database.generateAndAddOrders(user);
                System.out.println("Order successful!");
            }
            else if (Objects.equals(paymentType, "points")) { // 1 point for 2 cents
                if (user.getMetrics().getBuyPoints() < user.getCart().getTotalPrice() * 50) {
                    System.out.println("Not enough points. You need " + (user.getCart().getTotalPrice() * 50 - user.getMetrics().getBuyPoints()) + " more points to pay this order");
                    return;
                }
                user.getMetrics().removeBuyPoints((int) user.getCart().getTotalPrice() * 50);
                database.generateAndAddOrders(user, paymentType);
                System.out.println("Order successful!");
            }
        }
        else {
            System.out.println("Shipping address:");
            System.out.println("Enter your street:");
            String street = im.nextLine();
            System.out.println("Enter your city:");
            String city = im.nextLine();
            System.out.println("Enter your province:");
            String province = im.nextLine();
            System.out.println("Enter your country:");
            String country = im.nextLine();
            System.out.println("Enter your postal code:");
            String postalCode = im.nextLine();
            Address shippingAddress = new Address(street, city, province, country, postalCode);
            System.out.println("Enter your phone number:");
            String phoneNumber = im.nextLine();
            System.out.println("Enter your payment type (credit card/points):");
            String paymentType = im.nextLine();
            if (Objects.equals(paymentType, "credit card")) {
                System.out.println("Enter your credit card number:");
                String cardNumber = im.nextLine();
                System.out.println("Enter your credit card expiration date:");
                String expirationDate = im.nextLine();
                System.out.println("Enter your credit card owner's first name:");
                String ownerName = im.nextLine();
                System.out.println("Enter your credit card owner's last name:");
                String ownerLastName = im.nextLine();
                CreditCard creditCard = new CreditCard(cardNumber, ownerName, ownerLastName, expirationDate);
                database.generateAndAddOrders(user, creditCard, shippingAddress, phoneNumber);
                System.out.println("Order successful!");
            }
            else if (Objects.equals(paymentType, "points")) { // 1 point for 2 cents
                if (user.getMetrics().getBuyPoints() < user.getCart().getTotalPrice() * 50) {
                    System.out.println("Not enough points. You need " + Math.ceil((user.getCart().getTotalPrice() * 50 - user.getMetrics().getBuyPoints())) + " more points to pay this order");
                    return;
                }
                user.getMetrics().removeBuyPoints((int) user.getCart().getTotalPrice() * 50);
                database.generateAndAddOrders(user, paymentType, shippingAddress, phoneNumber);
                System.out.println("Order successful!");
            }

        }
        database.updateOrderIDCounts();
        user.getCart().getProducts().clear();
    }

    // remove product -------------------------------------------------------------------------------------------------

    private void removeProductFromCart() {
        InputManager inputManager = InputManager.getInstance();
        System.out.println("Enter the name of the product you want to remove:");
        String title = inputManager.nextLine();
        Product product = user.getCart().searchProductByName(title);
        if (product == null) {
            System.out.println("Product not found");
            return;
        }
        System.out.println("How many of it do you want to remove?");
        int quantity = uiUtilities.getUserInputAsInteger();
        if (quantity > product.getQuantity()) {
            System.out.println("Not enough products in cart");
            return;
        }
        user.getCart().removeProduct(product, quantity);
        product.setQuantity(product.getQuantity() + quantity);
        System.out.println("Product removed from cart");
    }

    // empty cart -----------------------------------------------------------------------------------------------------

    private void emptyCart() {
        InputManager im = InputManager.getInstance();
        System.out.println("Are you sure you want to empty the cart? (y/n)");
        String input = im.nextLine();
        boolean continueLoop = true;
        while (continueLoop) {
            if (Objects.equals(input, "y")) {
                user.getCart().getProducts().forEach((product, integer) -> product.setQuantity(product.getQuantity() + integer));
                user.getCart().getProducts().clear();
                System.out.println("Cart emptied");
                continueLoop = false;
            }
            else if (Objects.equals(input, "n")) {
                System.out.println("Returning to menu...");
                continueLoop = false;
            }
            else {
                System.out.println("Invalid input");
            }
        }
    }
}
