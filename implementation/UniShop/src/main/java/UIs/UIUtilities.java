package UIs;

import BackEndUtility.DataBase;
import BackEndUtility.InputManager;
import Users.*;
import UtilityObjects.Address;
import UtilityObjects.Notification;
import productClasses.Product;
import productClasses.Usages.Evaluation;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Class that contains some methods utilities for the UIs to allow the user to interact with the platform
 */
public class UIUtilities {

    // ATTRIBUTES

    private final DataBase database;
    private final User user;

    // CONSTRUCTOR

    /**
     * Constructor of the class UIUtilities
     *
     * @param database the database of the platform
     * @param user    the user that is logged in
     */
    public UIUtilities(DataBase database, User user) {
        this.database = database;
        this.user = user;
    }

    // UTILITIES

    // toggles --------------------------------------------------------------------------------------------------------

    /**
     * Method that allows the user to like the evaluation made by another user, unless the user is the author of the evaluation.
     * Also gives experience points to the author of the evaluation if he gets his first like, and updates the metrics of both users.
     * @param user the user that made the evaluation
     * @param evaluation the evaluation that the user wants to like
     */
    public void toggleEvaluationLike(Buyer user, Evaluation evaluation) {
        if (evaluation.getAuthor() == user) {
            System.out.println("You cannot like your own evaluation!");
            return;
        }
        if (!user.getEvaluationsLiked().contains(evaluation)) {
            user.getEvaluationsLiked().add(evaluation);
            evaluation.setLikes(evaluation.getLikes() + 1);
            user.getMetrics().setLikesGiven(user.getMetrics().getLikesGiven() + 1);
            evaluation.getAuthor().getMetrics().setLikesReceived(evaluation.getAuthor().getMetrics().getLikesReceived() + 1);
            System.out.println("You liked " + evaluation.getAuthor().getId() + "'s evaluation!");
            if (evaluation.getLikes() == 1) {
                evaluation.getAuthor().addNotification(new Notification("New like on evaluation!",
                        "You have gotten the first like on one of your evaluations!"));
                evaluation.getAuthor().getMetrics().addExpPoints(10);
            }
        } else {
            user.getEvaluationsLiked().remove(evaluation);
            evaluation.setLikes(evaluation.getLikes() - 1);
            user.getMetrics().setLikesGiven(user.getMetrics().getLikesGiven() - 1);
            evaluation.getAuthor().getMetrics().setLikesReceived(evaluation.getAuthor().getMetrics().getLikesReceived() + 1);
            System.out.println("You unliked " + evaluation.getAuthor().getId() + "'s evaluation!");
            if (evaluation.getLikes() == 0) {
                evaluation.getAuthor().getMetrics().removeExpPoints(10);
            }
        }
    }

    /**
     * Method that allows the user to follow/unfollow another buyer and update both their metrics, unless the buyer is himself.
     * Also gives experience points to both users if they follow each other.
     * @param user  the user that wants to follow another buyer
     * @param buyer the buyer that the user wants to follow
     */
    public void toggleBuyerToFollowing(Buyer user, Buyer buyer) {
        if (buyer == user) {
            System.out.println("You cannot follow yourself!");
            return;
        }
        if (!user.getBuyersFollowed().contains(buyer)) {
            user.getBuyersFollowed().add(buyer);
            buyer.getFollowers().add(user);
            user.getMetrics().setLikesGiven(user.getMetrics().getLikesGiven() + 1);
            String title = "You have a new follower!";
            String summary = user.getId() + " is now following you !";
            buyer.addNotification(new Notification(title, summary));
            System.out.println("You are now following " + buyer.getId() + "!");
            if (buyer.getBuyersFollowed().contains(user)) {
                System.out.println("You are now following each other! You both earned 10 experience points!");
                user.getMetrics().addExpPoints(10);
                buyer.getMetrics().addExpPoints(10);
            }
        } else {
            user.getBuyersFollowed().remove(buyer);
            buyer.getFollowers().remove(user);
            user.getMetrics().setLikesGiven(user.getMetrics().getLikesGiven() - 1);
            System.out.println("You are no longer following " + buyer.getId() + "!");
            if (buyer.getBuyersFollowed().contains(user)) {
                System.out.println("You are no longer following each other! You both lost 10 experience points!");
                user.getMetrics().removeExpPoints(10);
                buyer.getMetrics().removeExpPoints(10);
            }
        }
    }

    /**
     * Method that allows the user to like/unlike a product and update the metrics of both the user and the product.
     * @param user   the user that wants to like/unlike the product
     * @param product the product that the user wants to like/unlike
     */
    public void toggleProductToWishList(Buyer user, Product product) {
        System.out.println();
        System.out.println("Removing product(s) from cart...");

        ArrayList<Product> wishList = user.getWishList();
        if (wishList.contains(product)) {
            wishList.remove(product);
            product.setLikes(product.getLikes() - 1);
            user.getMetrics().setLikesGiven(user.getMetrics().getLikesGiven() - 1);
            System.out.println();
            System.out.println("Product removed from wish list!");
        } else {
            wishList.add(product);
            product.setLikes(product.getLikes() + 1);
            user.getMetrics().setLikesGiven(user.getMetrics().getLikesGiven() + 1);
            System.out.println();
            System.out.println("Product added to wish list!");
        }
    }

    /**
     * Method that allows a buyer to follow/unfollow a seller to get his notifications.
     * Also updates the metrics of both the buyer and the seller.
     * @param user  the buyer that wants to follow/unfollow the seller
     * @param seller the seller that the buyer wants to follow/unfollow
     */
    public void toggleSellerToFollowing(Buyer user, Seller seller) {
        ArrayList<Seller> sellersFollowed = user.getSellersFollowed();
        if (!sellersFollowed.contains(seller)) {
            sellersFollowed.add(seller);
            seller.getMetrics().updateLikes(seller.getMetrics().getLikes() + 1);
            user.getMetrics().setLikesGiven(user.getMetrics().getLikesGiven() + 1);
            seller.addFollower(user);   //seller has a new follower
            sellersFollowed.add(seller);        // keep track of buyer's following
            System.out.println("You are now following " + seller.getId() + "!");
        } else {
            sellersFollowed.remove(seller);
            seller.getMetrics().updateLikes(seller.getMetrics().getLikes() - 1);
            user.getMetrics().setLikesGiven(user.getMetrics().getLikesGiven() - 1);
            seller.removeFollower(user);
            System.out.println("You are no longer following " + seller.getId() + "!");
        }
    }

    // profile --------------------------------------------------------------------------------------------------------

    /**
     * Method that allows the user to delete his account from the platform
     */
    public void deleteAccount() {
        System.out.println("Are you sure you want to delete your account? (y/n)");
        String input = InputManager.getInstance().nextLine();
        boolean continueLoop2 = true;
        while (continueLoop2) {
            if (Objects.equals(input, "y")) {
                System.out.println("Deleting account...");
                database.removeUser(user);
                continueLoop2 = false;
            } else if (Objects.equals(input, "n")) {
                System.out.println("Returning to menu...");
                continueLoop2 = false;
            } else {
                System.out.println("Invalid input");
            }
        }
    }

    /**
     * Method that allows the user to modify his address
     */
    public void modifyAddress() {
        System.out.println("Enter your street name:");
        String street = InputManager.getInstance().nextLine();
        System.out.println("Enter your city:");
        String city = InputManager.getInstance().nextLine();
        System.out.println("Enter your province:");
        String province = InputManager.getInstance().nextLine();
        System.out.println("Enter your country:");
        String country = InputManager.getInstance().nextLine();
        System.out.println("Enter your postal code:");
        String postalCode = InputManager.getInstance().nextLine();
        Address shippingAddress = new Address(street, city, province, country, postalCode);
        user.setAddress(shippingAddress);
    }

    private String modifyEmail() {
        String email = "";
        while (!email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
            System.out.println("Please enter your email:");
            email = InputManager.getInstance().nextLine();
        }
        return email;
    }

    private String modifyPhoneNumber() {
        String phoneNumber = "a";
        while (!phoneNumber.matches("[0-9]+")) {
            System.out.println("Enter your phone number:");
            phoneNumber = InputManager.getInstance().nextLine();
        }
        return phoneNumber;
    }

    /**
     * Method that allows the buyer to modify his personal info. Doesn't modify his id or email if they are already taken.
     * @param user the buyer that wants to modify his personal info
     */
    public void modifyPersonalInfo(Buyer user) {
        System.out.println("Enter your first name:");
        String firstName = InputManager.getInstance().nextLine();
        System.out.println("Enter your last name:");
        String lastName = InputManager.getInstance().nextLine();
        System.out.println("Enter your new id:");
        String id = InputManager.getInstance().nextLine();
        String email = modifyEmail();
        String phoneNumber = modifyPhoneNumber();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        if (!database.validateNewUser(id, email)) {
            System.out.println("This id or email is already taken");
            System.out.println("Your other info are changed but your id and email were not changed");
            return;
        }
        user.setId(id);
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);
        System.out.println("Personal info modified");
    }

    /**
     * Method that allows the seller to modify his personal info. Doesn't modify his id or email if they are already taken.
     * @param user the seller that wants to modify his personal info
     */
    public void modifyPersonalInfo(Seller user) {
        System.out.println("Enter your new id:");
        String id = InputManager.getInstance().nextLine();
        String email = modifyEmail();
        String phoneNumber = modifyPhoneNumber();
        if (!database.validateNewUser(id, email)) {
            System.out.println("This id or email is already taken");
            System.out.println("Your other info are changed but your id and email were not changed");
            return;
        }
        user.setId(id);
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);
        System.out.println("Personal info modified");
    }

    /**
     * Method that allows the user to modify his password
     */
    public void modifyPassword() {
        while (true) {
            System.out.println("Enter your current password:");
            String currentPassword = InputManager.getInstance().nextLine();
            if (Objects.equals(currentPassword, user.getPassword())) {
                System.out.println("Enter your new password:");
                String newPassword = InputManager.getInstance().nextLine();
                user.setPassword(newPassword);
                database.changePassword(user, newPassword);
                System.out.println("Password modified");
                break;
            } else {
                System.out.println("Wrong password");
            }
        }
    }

    // inputs ---------------------------------------------------------------------------------------------------------

    /**
     * Method that takes an input from the user and returns it as an integer
     * @return the input of the user as an integer
     */
    public int getUserInputAsInteger() {
        while (true) {
            try {
                int returned = Integer.parseInt(InputManager.getInstance().nextLine());
                if (returned < 0) {
                    System.out.println("Invalid input. Please enter a positive number.");
                } else {
                    return returned;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }

        }
    }
}
