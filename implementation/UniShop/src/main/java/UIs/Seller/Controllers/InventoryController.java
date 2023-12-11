package UIs.Seller.Controllers;

import BackEndUtility.DataBase;
import BackEndUtility.InputManager;
import UIs.Seller.SellerMenu;
import Users.Buyer;
import Users.Seller;
import productClasses.Inheritances.*;
import productClasses.Product;

import java.util.ArrayList;
import java.util.Objects;

import static java.lang.Float.parseFloat;

public class InventoryController extends SellerMenu {

    // ATTRIBUTES

    private Seller user;
    private DataBase database;

    // CONTROLLER

    public InventoryController(Seller user, DataBase database) {
        super(user, database);
    }

    // UTILITIES

    // inventory page -------------------------------------------------------------------------------------------------

    public boolean displayInventory() {
        boolean continueLoop = true;
        while (continueLoop) {
            System.out.println();
            System.out.println("INVENTORY");

            // display products
            System.out.println();
            user.getProducts().forEach(product -> System.out.println(product.toString()));

            // select option
            System.out.println("Please select an option:");
            System.out.println("1. Add item(s)");
            System.out.println("2. Remove item(s)");
            System.out.println("3. Change item quantity");
            System.out.println("4. Add promotion");
            System.out.println("5. Return to menu");
            int choice = uiUtilities.getUserInputAsInteger();

            // process selection
            switch (choice) {
                // add item(s)
                case 1:
                    System.out.println();
                    System.out.println("Adding item(s)...");
                    addProduct();
                    break;

                // remove item(s)
                case 2:
                    System.out.println();
                    System.out.println("Removing item(s)...");
                    removeProduct();
                    break;

                // change item quantity
                case 3:
                    System.out.println();
                    System.out.println("Changing item quantity...");
                    changeProductQty();
                    break;

                // add promotion
                case 4:
                    System.out.println();
                    System.out.println("Adding promotion...");
                    continueLoop = addPromotion();
                    break;

                // return to menu
                case 5:
                    continueLoop = false; // Exit the inventory submenu
                    break;

                default:
                    System.out.println();
                    System.out.println("Invalid selection. Please try again.");
            }
        }

        // continue loop
        return true;
    }

    // add products ---------------------------------------------------------------------------------------------------

    // Adds new product to database
    public void addProduct() {
        InputManager inputManager = InputManager.getInstance();

        // ask title
        System.out.println();
        System.out.println("Please enter the title of the product:");
        String title = inputManager.nextLine();

        // ask description
        System.out.println("Please enter the description of the product:");
        String description = inputManager.nextLine();

        // ask price
        String priceText = "a";
        while (!priceText.matches("\\d+(\\.\\d+)?")) {
            System.out.println("Please enter the price of the product:");
            priceText = inputManager.nextLine();
        }
        float price = parseFloat(priceText);

        // ask bonus points
        System.out.println("Please enter the bonus points of the product:");
        int bonusPoints = uiUtilities.getUserInputAsInteger();
        if (bonusPoints < 0) {
            bonusPoints = 0;
        }
        if (Math.floor(price)*20 < Math.floor(price) + bonusPoints) {
            bonusPoints = (int) Math.floor(price)*19;
        }

        // ask quantity
        System.out.println("Please enter the quantity of the product:");
        int quantity = uiUtilities.getUserInputAsInteger();

        // ask sell date
        System.out.println("Please enter the sell date of the product:");
        String sellDate = inputManager.nextLine();

        // ask details according to category
        Product product = null;
        String author, releaseDate, brand, model, subCategory;
        int ISBN, edition;
        switch (user.getCategory()) {
            case BOOKS:
                System.out.println("Please enter the author of the book:");
                author = inputManager.nextLine();
                System.out.println("Please enter the publisher of the book:");
                String publisher = inputManager.nextLine();
                System.out.println("Please enter the ISBN of the book:");
                ISBN = uiUtilities.getUserInputAsInteger();
                System.out.println("Please enter the genre of the book:");
                String genre = inputManager.nextLine();
                System.out.println("Please enter the release date of the book:");
                releaseDate = inputManager.nextLine();
                System.out.println("Please enter the edition of the book:");
                edition = uiUtilities.getUserInputAsInteger();
                System.out.println("Please enter the volume of the book:");
                int volume = uiUtilities.getUserInputAsInteger();
                product = new Book(title, description, price, (int) Math.floor(price) + bonusPoints , user,
                        quantity, ISBN, author, publisher, genre, releaseDate, sellDate, edition, volume);
                break;
            case LEARNING_RESOURCES:
                System.out.println("Please enter the author of the learning resource:");
                author = inputManager.nextLine();
                System.out.println("Please enter the organization of the learning resource:");
                String organization = inputManager.nextLine();
                System.out.println("Please enter the ISBN of the learning resource:");
                ISBN = uiUtilities.getUserInputAsInteger();
                System.out.println("Please enter the release date of the learning resource:");
                releaseDate = inputManager.nextLine();
                System.out.println("Please enter the type of the learning resource:");
                String type = inputManager.nextLine();
                System.out.println("Please enter the edition of the learning resource:");
                edition = uiUtilities.getUserInputAsInteger();
                product = new LearningResource(title, description, price, (int) Math.floor(price) + bonusPoints,
                        user, quantity, ISBN, author, organization, releaseDate, sellDate, type, edition);
                break;
            case STATIONERY:
                System.out.println("Please enter the brand of the stationery:");
                brand = inputManager.nextLine();
                System.out.println("Please enter the model of the stationery:");
                model = inputManager.nextLine();
                System.out.println("Please enter the subcategory of the stationery:");
                subCategory = inputManager.nextLine();
                System.out.println("Please enter the release date of the stationery:");
                releaseDate = inputManager.nextLine();
                product = new Stationery(title, description, price, (int) Math.floor(price) + bonusPoints, user,
                        quantity, brand, model, subCategory, releaseDate, sellDate);
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
                product = new Hardware(title, description, price,(int) Math.floor(price) + bonusPoints , user,
                        quantity, brand, model, releaseDate, subCategory, sellDate);
                break;
            case DESKTOP_ACCESSORIES:
                System.out.println("Please enter the brand of the desktop accessory:");
                brand = inputManager.nextLine();
                System.out.println("Please enter the model of the desktop accessory:");
                model = inputManager.nextLine();
                System.out.println("Please enter the subcategory of the desktop accessory:");
                subCategory = inputManager.nextLine();
                product = new OfficeEquipment(title, description, price, (int) Math.floor(price) + bonusPoints,
                        user, quantity, brand, model, subCategory, sellDate);
                break;
        }

        // check if product already exists
        if (database.verifyNewProduct(product)) {
            // add product to database
            database.addProduct(product);

            // send notification to followers
            sendNewProductNotification(product);
        } else {
            System.out.println("Product already exists");
        }
    }

    // Sends notification to followers when new product added
    private void sendNewProductNotification(Product newProduct) {
        ArrayList<Buyer> followers = user.getFollowers();
        if (!followers.isEmpty())
            for (Buyer follower : followers)
                sendBuyerNotification(follower, "New Product",
                        "New product added by " + user.getId() + ": " + newProduct.getTitle());
    }

    // remove products ------------------------------------------------------------------------------------------------

    public void removeProduct() {
        Product product = null;
        while (product == null) {
            System.out.println("Please enter the title of the product:");
            String title = InputManager.getInstance().nextLine();
            product = user.findProductByTitle(title);
        }
        System.out.println("Are you sure? (y/n)");
        String choice = InputManager.getInstance().nextLine();
        if (choice.equals("y")) {database.removeProduct(product);}
        else {System.out.println("Product not removed");}
    }

    // change product quantities --------------------------------------------------------------------------------------

    public void changeProductQty() {
        Product product = null;
        while (product == null) {
            System.out.println("Please enter the title of the product:");
            String title = InputManager.getInstance().nextLine();
            product = user.findProductByTitle(title);
        }

        System.out.println("Please enter the quantity of the product:");
        int quantity = uiUtilities.getUserInputAsInteger();
        if (quantity == 0) {
            database.removeProduct(product);
        }
        user.changeProductQuantity(product, quantity);
    }

    // add promotion --------------------------------------------------------------------------------------------------

    // Adds product promotion
    private boolean addPromotion(){
        // ask promotion type
        System.out.println();
        System.out.println("Please select the promotion type:");
        System.out.println("1. Price reduction");
        System.out.println("2. Bonus points");
        System.out.println("3. Return to menu");
        int promoChoice = uiUtilities.getUserInputAsInteger();

        // process selection
        switch (promoChoice) {
            // price reduction
            case 1:
                addPriceReduction();
                break;

            // bonus points
            case 2:
                addBonusPoints();
                break;

            // return to menu
            case 3:
                return false;

            // invalid input
            default:
                System.out.println();
                System.out.println("Invalid selection. Please try again.");
                promoChoice = uiUtilities.getUserInputAsInteger();
        }

        return true;
    }

    // Adds price reduction
    private void addPriceReduction() {
        // ask product title and get Product object
        Product product = null;
        while (product == null) {
            System.out.println();
            System.out.println("Please enter the title of the product:");
            String title = InputManager.getInstance().nextLine();
            product = user.findProductByTitle(title);
        }

        // ask reduced price
        System.out.println();
        System.out.println("Please enter the price of the product:");
        InputManager im = InputManager.getInstance();
        String priceText = "a";
        while (!priceText.matches("\\d+(\\.\\d+)?")) {
            priceText = im.nextLine();

            // validate price
            if (parseFloat(priceText) < 0) {
                System.out.println();
                System.out.println("WARNING : The price cannot be less than 0.00$");
                continue;
            }
            if (parseFloat(priceText) >= product.getPrice()) {
                System.out.println();
                System.out.println("WARNING : The price has to be less than " + product.getPrice() + "$");
            }
        }
        float reducedPrice = parseFloat(priceText);

        // set reduced price to product
        product.setPrice(reducedPrice);

        // update info in inventory
        ArrayList<Product> newInventory = new ArrayList<>();
        Product oldProduct = null;
        for (Product sellerProduct : user.getProducts()) {
            // replace old product with new product with same title
            if (Objects.equals(sellerProduct.getTitle(), product.getTitle())) {
                newInventory.add(product);
                oldProduct = sellerProduct;
            }

            // add product to list
            newInventory.add(sellerProduct);
        }
        user.setProducts(newInventory);

        // send notifications to buyers
        sendPromoNotificationToLikes(oldProduct, "price reduction");
        sendPromoNotificationToFollowers(oldProduct, "price reduction");
        sendPromoNotificationToBuyer(oldProduct, "price reduction");
    }

    // Adds bonus points to product
    private void addBonusPoints() {
        // ask product title and get Product object
        Product product = null;
        while (product == null) {
            System.out.println();
            System.out.println("Please enter the title of the product:");
            String title = InputManager.getInstance().nextLine();
            product = user.findProductByTitle(title);
        }

        // reset base points
        product.setBasePoints((int) Math.floor(product.getPrice()));

        // asks bonus points to add to base points
        System.out.println();
        System.out.println("Please enter the number of bonus points:");
        int bonusPoints = uiUtilities.getUserInputAsInteger();
        // cannot add bonus points > 20x base points
        if (bonusPoints > Math.floor(product.getPrice())*19)
            bonusPoints = (int) Math.floor(product.getPrice())*19;

        // set new points to product
        product.setBasePoints(product.getBasePoints() + bonusPoints);

        // update info in inventory
        ArrayList<Product> newInventory = new ArrayList<>();
        Product oldProduct = null;
        for (Product sellerProduct : user.getProducts()) {
            // replace old product with new product with same title
            if (Objects.equals(sellerProduct.getTitle(), product.getTitle())) {
                newInventory.add(product);
                oldProduct = sellerProduct;
            }

            // add product to list
            newInventory.add(sellerProduct);
        }
        user.setProducts(newInventory);

        // send notification to buyers
        sendPromoNotificationToLikes(oldProduct, "bonus points");
        sendPromoNotificationToFollowers(oldProduct, "bonus points");
        sendPromoNotificationToBuyer(oldProduct, "bonus points");
    }

    // Sends notification to buyers who liked the product
    private void sendPromoNotificationToLikes(Product oldProduct, String bonusPointsOrPriceReduction) {
        for (Buyer buyer : database.getBuyers())
            if (buyer.getWishList().contains(oldProduct))
                switch (bonusPointsOrPriceReduction) {
                    case "bonus points":
                        sendBuyerNotification(buyer, "New Promotion",
                                user.getId() + " added bonus points to " + oldProduct.getTitle());
                        break;
                    case "price reduction":
                        sendBuyerNotification(buyer, "New Promotion",
                                user.getId() + " reduced the price of " + oldProduct.getTitle());
                        break;
                }

    }

    // Sends notification to followers
    private void sendPromoNotificationToFollowers(Product oldProduct, String bonusPointsOrPriceReduction) {
        for (Buyer buyer : user.getFollowers())
            switch (bonusPointsOrPriceReduction) {
                case "bonus points":
                    sendBuyerNotification(buyer, "New Promotion",
                            user.getId() + " added bonus points to " + oldProduct.getTitle());
                    break;
                case "price reduction":
                    sendBuyerNotification(buyer, "New Promotion",
                            user.getId() + " reduced the price of " + oldProduct.getTitle());
                    break;
            }
    }

    // Sends notification to buyer whose followers liked the product
    private void sendPromoNotificationToBuyer(Product oldProduct, String bonusPointsOrPriceReduction) {
        for (Buyer buyer : database.getBuyers()) {
            boolean likedByFollowers = false;
            for (Buyer follower : buyer.getFollowers()) {
                if (follower.getWishList().contains(oldProduct)) {
                    likedByFollowers = true;
                    break;
                }
            }
            if (likedByFollowers)
                switch (bonusPointsOrPriceReduction) {
                    case "bonus points":
                        sendBuyerNotification(buyer, "New Promotion",
                                user.getId() + " added bonus points to a product one of your followers liked: " +oldProduct.getTitle());
                        break;
                    case "price reduction":
                        sendBuyerNotification(buyer, "New Promotion",
                                user.getId() + " reduced the price of " + oldProduct.getTitle());
                        break;
                }
        }
    }
}
