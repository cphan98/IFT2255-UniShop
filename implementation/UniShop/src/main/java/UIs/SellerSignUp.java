package UIs;

import LoginUtility.DataBase;
import Users.Address;
import Users.Seller;
import Users.User;
import otherUtility.Category;
import products.*;

import static java.lang.Float.parseFloat;
import static java.lang.Integer.parseInt;

public class SellerSignUp implements SignUpScreen {
    private DataBase database;

    public SellerSignUp(DataBase database) {
        this.database = database;
    }
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
        String phoneNumber = inputManager.nextLine();
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
        Seller seller = new Seller(username, password, email, phoneNumber, new Address(address, country, province, city, postalCode), category);
        addProductToSeller(seller);
        System.out.println("WARNING: You must connect within the next 24 hours or else the signup will be cancelled");
        return seller;
    }

    private void addProductToSeller(Seller seller) {
        InputManager inputManager = InputManager.getInstance();
        System.out.println("Please enter the title of the product:");
        String title = inputManager.nextLine();
        System.out.println("Please enter the description of the product:");
        String description = inputManager.nextLine();
        System.out.println("Please enter the price of the product:");
        float price = parseFloat(inputManager.nextLine());
        System.out.println("Please enter the base points of the product:");
        int basePoints = parseInt(inputManager.nextLine());
        System.out.println("Please enter the quantity of the product:");
        int quantity = parseInt(inputManager.nextLine());
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
                int ISBN = parseInt(inputManager.nextLine());
                System.out.println("Please enter the genre of the book:");
                String genre = inputManager.nextLine();
                System.out.println("Please enter the release date of the book:");
                String releaseDate = inputManager.nextLine();
                System.out.println("Please enter the edition of the book:");
                int edition = parseInt(inputManager.nextLine());
                System.out.println("Please enter the volume of the book:");
                int volume = parseInt(inputManager.nextLine());
                product = new Book(title, description, price, basePoints, seller, quantity, ISBN, author, publisher, genre, releaseDate, sellDate, edition, volume);
                break;
            case LEARNING_RESOURCES:
                System.out.println("Please enter the author of the learning resource:");
                author = inputManager.nextLine();
                System.out.println("Please enter the organization of the learning resource:");
                String organization = inputManager.nextLine();
                System.out.println("Please enter the ISBN of the learning resource:");
                ISBN = parseInt(inputManager.nextLine());
                System.out.println("Please enter the release date of the learning resource:");
                releaseDate = inputManager.nextLine();
                System.out.println("Please enter the type of the learning resource:");
                String type = inputManager.nextLine();
                System.out.println("Please enter the edition of the learning resource:");
                edition = parseInt(inputManager.nextLine());
                product = new LearningResource(title, description, price, basePoints, seller, quantity, ISBN, author, organization, releaseDate, sellDate, type, edition);
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
                product = new Stationery(title, description, price, basePoints, seller, quantity, brand, model, subCategory, releaseDate, sellDate);
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
                product = new Hardware(title, description, price, basePoints, seller, quantity, brand, model, releaseDate, subCategory, sellDate);
                break;
            case DESKTOP_ACCESSORIES:
                System.out.println("Please enter the brand of the desktop accessory:");
                brand = inputManager.nextLine();
                System.out.println("Please enter the model of the desktop accessory:");
                model = inputManager.nextLine();
                System.out.println("Please enter the subcategory of the desktop accessory:");
                subCategory = inputManager.nextLine();
                System.out.println("Please enter the release date of the desktop accessory:");
                releaseDate = inputManager.nextLine();
                product = new OfficeEquipment(title, description, price, basePoints, seller, quantity, brand, model, subCategory, sellDate);
                break;
        }
        database.addProduct(product);
    }
}
