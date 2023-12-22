package UIs.Seller;

import BackEndUtility.DataBase;
import BackEndUtility.InputManager;
import UIs.SignUpScreen;
import UtilityObjects.Address;
import Users.Seller;
import BackEndUtility.Category;
import productClasses.*;
import productClasses.Inheritances.*;

import static java.lang.Float.parseFloat;

/**
 * Class managing a seller's sign up.
 *
 * This class implements SignUpScreen.
 */
public class SellerSignUp implements SignUpScreen {

    // ATTRIBUTE

    private final DataBase database;

    // CONSTRUCTOR

    /**
     * Constructs an instance of SellerSignUp with a given database.
     *
     * @param database  DataBase of UniShop, in which a new seller will be added
     */
    public SellerSignUp(DataBase database) {
        this.database = database;
    }

    // UTILITIES

    // credentials ----------------------------------------------------------------------------------------------------

    /**
     * Gets the necessary information for the user to sign up as a seller. Once the seller is signed up the user must
     * connect withing 24h in order for their account to remain valid.
     */
    @Override
    public void getCredentialsAndSignUp() {
        System.out.println("Welcome, new seller!");
        InputManager inputManager = InputManager.getInstance();
        System.out.println("Please enter your username:");
        String username = inputManager.nextLine();
        System.out.println("Please enter your password:");
        String password = inputManager.nextLine();
        String email = "";
        while (!email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
            System.out.println("Please enter your email:");
            email = inputManager.nextLine();
        }
        String phoneNumber = "a";
        while (!phoneNumber.matches("[0-9]+")) {
            System.out.println("Enter your phone number:");
            phoneNumber = InputManager.getInstance().nextLine();
        }
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
        Category category = null;
        while (category == null) {
            System.out.println("Please enter your category:");
            System.out.println("1. Books");
            System.out.println("2. Learning resources");
            System.out.println("3. Stationery");
            System.out.println("4. Electronics");
            System.out.println("5. Desktop accessories");
            String choice = inputManager.nextLine();
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
        Seller seller = new Seller(username, password, email, phoneNumber,
                new Address(address, country, province, city, postalCode), category);
        database.addUser(seller);
        addProductToSeller(seller);
        System.out.println("WARNING: You must connect within the next 24 hours or else the signup will be cancelled");
    }

    // products -------------------------------------------------------------------------------------------------------

    private void addProductToSeller(Seller seller) {
        InputManager inputManager = InputManager.getInstance();
        System.out.println("Please enter the title of the product:");
        String title = inputManager.nextLine();
        while (!database.verifyNewProduct(new Book(title, "", 0, 0, seller, 1,
                55, "a", "a", "a", "a", "a", 1, 1))) {
            System.out.println("Invalid input. Please enter a unique title.");
            title = inputManager.nextLine();
        }
        System.out.println("Please enter the description of the product:");
        String description = inputManager.nextLine();
        float price = -1F;
        while (price < 0) {
            System.out.println("Please enter the price of the product:");
            try {
                price = parseFloat(inputManager.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
        System.out.println("Please enter the additional points of the product:");
        int basePoints = getUserInputAsInteger();
        System.out.println("Please enter the quantity of the product:");
        int quantity = getUserInputAsInteger();
        System.out.println("Please enter the sell date of the product:");
        String sellDate = inputManager.nextLine();
        Product product = null;
        switch (seller.getCategory()) {
            case BOOKS:
                System.out.println("Please enter the author of the book:");
                String author = inputManager.nextLine();
                System.out.println("Please enter the publisher of the book:");
                String publisher = inputManager.nextLine();
                System.out.println("Please enter the ISBN of the book:");
                int ISBN = getUserInputAsInteger();
                System.out.println("Please enter the genre of the book:");
                String genre = inputManager.nextLine();
                System.out.println("Please enter the release date of the book:");
                String releaseDate = inputManager.nextLine();
                System.out.println("Please enter the edition of the book:");
                int edition = getUserInputAsInteger();
                System.out.println("Please enter the volume of the book:");
                int volume = getUserInputAsInteger();
                product = new Book(title, description, price, basePoints, seller, quantity, ISBN, author,
                        publisher, genre, releaseDate, sellDate, edition, volume);
                break;
            case LEARNING_RESOURCES:
                System.out.println("Please enter the author of the learning resource:");
                author = inputManager.nextLine();
                System.out.println("Please enter the organization of the learning resource:");
                String organization = inputManager.nextLine();
                System.out.println("Please enter the ISBN of the learning resource:");
                ISBN = getUserInputAsInteger();
                System.out.println("Please enter the release date of the learning resource:");
                releaseDate = inputManager.nextLine();
                System.out.println("Please enter the type of the learning resource:");
                String type = inputManager.nextLine();
                System.out.println("Please enter the edition of the learning resource:");
                edition = getUserInputAsInteger();
                product = new LearningResource(title, description, price, basePoints, seller, quantity, ISBN, author,
                        organization, releaseDate, sellDate, type, edition);
                break;
            case STATIONERY:
                System.out.println("Please enter the brand of the stationery:");
                String brand = inputManager.nextLine();
                System.out.println("Please enter the model of the stationery:");
                String model = inputManager.nextLine();
                System.out.println("Please enter the subcategory of the stationery:");
                String subCategory = inputManager.nextLine();
                System.out.println("Please enter the release date of the stationery:");
                releaseDate = inputManager.nextLine();
                product = new Stationery(title, description, price, basePoints, seller, quantity, brand, model,
                        subCategory, releaseDate, sellDate);
                break;
            case ELECTRONICS:
                System.out.println("Please enter the brand of the electronics:");
                brand = inputManager.nextLine();
                System.out.println("Please enter the model of the electronics:");
                model = inputManager.nextLine();
                System.out.println("Please enter the subcategory of the electronics:");
                subCategory = inputManager.nextLine();
                System.out.println("Please enter the release date of the electronics:");
                releaseDate = inputManager.nextLine();
                product = new Hardware(title, description, price, basePoints, seller, quantity, brand, model,
                        releaseDate, subCategory, sellDate);
                break;
            case DESKTOP_ACCESSORIES:
                System.out.println("Please enter the brand of the desktop accessory:");
                brand = inputManager.nextLine();
                System.out.println("Please enter the model of the desktop accessory:");
                model = inputManager.nextLine();
                System.out.println("Please enter the subcategory of the desktop accessory:");
                subCategory = inputManager.nextLine();
                product = new OfficeEquipment(title, description, price, basePoints, seller, quantity, brand, model,
                        subCategory, sellDate);
                break;
        }
        database.addProduct(product);
    }

    // inputs ---------------------------------------------------------------------------------------------------------

    private int getUserInputAsInteger() {
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