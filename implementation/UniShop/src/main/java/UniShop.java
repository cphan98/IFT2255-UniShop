import BackEndUtility.DataBase;
import UIs.Buyer.BuyerMenu;
import UIs.HomeScreen;
import Users.*;
import UtilityObjects.Address;
import UtilityObjects.CreditCard;
import BackEndUtility.Category;
import BackEndUtility.OrderState;
import productClasses.*;
import productClasses.Inheritances.*;
import productClasses.Usages.Evaluation;
import serializationUtil.SerializationUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class UniShop {
    // ATTRIBUTES

    private static final String DATA_FILE = "dataBase.ser";
    private static DataBase database;

    // MAIN

    public static void main(String[] args) {
        try {
            // Try to load existing data
            database = SerializationUtil.loadDataBase(DATA_FILE);
        } catch (IOException | ClassNotFoundException e) {
            // If no data is found or an error occurs, create new data
            makeFakeData();
            printBigVoidBefore();
        }

        HomeScreen homeScreen = new HomeScreen(database);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                SerializationUtil.saveDataBase(database, DATA_FILE);
                System.out.println("Data saved successfully.");
            } catch (IOException e) {
                System.out.println("Error saving data: " + e.getMessage());
            }
        }));
        homeScreen.initialize();

        // Save the data when exiting
        try {
            SerializationUtil.saveDataBase(database, DATA_FILE);
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    // OPERATIONS

    private static void printBigVoidBefore() {
        for (int i=0; i<100; i++) {
            System.out.println();
        }
    }

    // DATA INITIALIZATION

    private static void makeFakeData() {
        String[] prompts = {"terrible", "bad", "average", "good", "fantastic"};
        ArrayList<User> users = new ArrayList<>();
        putFakeUsers(users);
        database = new DataBase(users);
        putFakeProducts();
        database.getProducts().forEach(product -> {
           product.setLikes(Math.round(new Random().nextFloat() * 1000));
           float randomRating;
           for (int i=0; i<2; i++) {
               randomRating = (float) Math.round(Math.random()*50)/10;
                String comment = "This is a " + prompts[Math.max(0,Math.round(randomRating)-1)] + " " + product.getTitle().toLowerCase() + "!";
                int userIndex = Math.round((float) Math.random() * (database.getBuyers().size()-1))*i;
                database.addEvaluationToProduct(product, new Evaluation(comment, randomRating, (Buyer) users.get(userIndex)));
           }
        });
        simulateSomeActions();
    }

    private static void simulateSomeActions() {
        ArrayList<User> users = database.getUsers();
        ArrayList<Product> products = database.getProducts();
        Buyer cynthia = (Buyer) users.get(0);
        Buyer nathan = (Buyer) users.get(1);
        Buyer lucas = (Buyer) users.get(2);
        Buyer monpote20 = (Buyer) users.get(3);
        BuyerMenu buyerMenu = new BuyerMenu(cynthia, database);
        buyerMenu.getUiUtilities().toggleBuyerToFollowing(cynthia, (Buyer) users.get(1));
        buyerMenu.getUiUtilities().toggleBuyerToFollowing(cynthia, (Buyer) users.get(2));
        buyerMenu.getUiUtilities().toggleBuyerToFollowing(cynthia, (Buyer) users.get(3));
        buyerMenu.getUiUtilities().toggleBuyerToFollowing(cynthia, (Buyer) users.get(4));
        buyerMenu.getUiUtilities().toggleProductToWishList(((Buyer) users.get(4)), products.get(0));
        buyerMenu.getUiUtilities().toggleProductToWishList(((Buyer) users.get(4)), products.get(1));
        buyerMenu.getUiUtilities().toggleProductToWishList(((Buyer) users.get(3)), products.get(1));
        buyerMenu.getUiUtilities().toggleProductToWishList(((Buyer) users.get(3)), products.get(2));
        buyerMenu.getUiUtilities().toggleProductToWishList(((Buyer) users.get(2)), products.get(2));
        buyerMenu.getUiUtilities().toggleProductToWishList(((Buyer) users.get(2)), products.get(3));
        buyerMenu.getUiUtilities().toggleProductToWishList(((Buyer) users.get(1)), products.get(3));
        buyerMenu.getUiUtilities().toggleProductToWishList(((Buyer) users.get(1)), products.get(4));

        cynthia.setCard(new CreditCard("123456789012", "Cynthia", "Phan", "2022-01-01"));
        cynthia.getCart().addProduct(products.get(0), 12);
        cynthia.getCart().addProduct(products.get(4), 9);
        cynthia.getCart().addProduct(products.get(5), 7);
        database.generateAndAddOrders(cynthia, "credit card");
        cynthia.getCart().getProducts().clear();


        nathan.setCard(new CreditCard("123456789012", "Nathan", "Razaf", "2022-01-01"));
        nathan.getCart().addProduct(products.get(1), 5);
        nathan.getCart().addProduct(products.get(2), 3);
        nathan.getCart().addProduct(products.get(6), 8);
        nathan.getCart().addProduct(products.get(8), 2);
        nathan.getCart().addProduct(products.get(12), 1);
        database.generateAndAddOrders(nathan, "credit card");
        nathan.getCart().getProducts().clear();

        lucas.setCard(new CreditCard("123456789012", "Lucas", "Ranaivo", "2022-01-01"));
        lucas.getCart().addProduct(products.get(3), 6);
        lucas.getCart().addProduct(products.get(7), 8);
        lucas.getCart().addProduct(products.get(15), 3);
        lucas.getCart().addProduct(products.get(16), 3);
        database.generateAndAddOrders(lucas, "credit card");
        lucas.getCart().getProducts().clear();

        monpote20.setCard(new CreditCard("123456789012", "Nathan", "Rasami", "2022-01-01"));
        monpote20.getCart().addProduct(products.get(4), 2);
        monpote20.getCart().addProduct(products.get(5), 3);
        monpote20.getCart().addProduct(products.get(6), 6);
        monpote20.getCart().addProduct(products.get(7), 1);
        monpote20.getCart().addProduct(products.get(8), 3);
        monpote20.getCart().addProduct(products.get(9), 2);
        database.generateAndAddOrders(monpote20, "credit card");
        monpote20.getCart().getProducts().clear();


    }


    private static void putFakeProducts() {
        ArrayList<Seller> sellers = database.getSellers();
        database.addProduct(new Hardware("Laptop", "A laptop", 1000.0F, 1000, sellers.get(0), 47, "MSI Gaming", "YTP-2099", "2017-08-03", "Laptops", "2022-01-01"));
        database.addProduct(new Hardware("Desktop screen", "A desktop screen", 500.0F, 500, sellers.get(0), 82, "DELL", "FHD-999", "2019-02-02", "Desktop screens", "2022-01-01"));
        database.addProduct(new Hardware("Keyboard", "A keyboard", 100.0F, 100, sellers.get(0), 136, "Logitech", "K-100", "2020-01-01", "Keyboards", "2022-01-01"));
        database.addProduct(new Hardware("Graphics card", "A graphics card", 1000.0F, 1000, sellers.get(0), 184, "Nvidia", "GTX-1080", "2020-01-02", "Graphics cards", "2022-01-01"));
        database.addProduct(new Book("Harry Potter and the Philosopher's Stone", "Harry Potter and the Philosopher's Stone", 39.99F, 39, sellers.get(1), 32, 123234, "J.K. Rowling", "Glenat", "Fantastic", "2021-01-01", "2021-01-01", 2022, 1));
        database.addProduct(new Book("Harry Potter and the Chamber of Secrets", "Harry Potter and the Chamber of Secrets", 39.99F, 39, sellers.get(1), 32, 123238, "J.K. Rowling", "Glenat", "Fantastic", "2021-01-01", "2021-01-01", 2022, 2));
        database.addProduct(new Book("Harry Potter and the Prisoner of Azkaban", "Harry Potter and the Prisoner of Azkaban", 39.99F, 39, sellers.get(1), 32, 123239, "J.K. Rowling", "Glenat", "Fantastic", "2021-01-01", "2021-01-01", 2022, 3));
        database.addProduct(new Book("Harry Potter and the Goblet of Fire", "Harry Potter and the Goblet of Fire", 39.99F, 39, sellers.get(1), 32, 123240, "J.K. Rowling", "Glenat", "Fantastic", "2021-01-01", "2021-01-01", 2022, 4));
        database.addProduct(new OfficeEquipment("Pencil Sharpener", "A pencil sharpener", 15.99F, 15, sellers.get(2), 200, "Yamaha", "YTP-2098", "Pencil Sharpeners", "2021-02-04"));
        database.addProduct(new OfficeEquipment("Stapler", "A stapler", 15.99F, 15, sellers.get(2), 189, "Yamaha", "YTP-2098", "Staplers", "2021-02-04"));
        database.addProduct(new OfficeEquipment("Hole puncher", "A hole puncher", 15.99F, 15, sellers.get(2), 178, "Yamaha", "YTP-2098", "Hole punchers", "2021-02-04"));
        database.addProduct(new OfficeEquipment("Paper clip", "A paper clip", 15.99F, 15, sellers.get(2), 167, "Yamaha", "YTP-2098", "Paper clips", "2021-02-04"));
        database.addProduct(new LearningResource("Math 101", "Math 101 textbook", 59.99F, 59, sellers.get(3), 10, 102948, "John Doe", "McGraw Hill", "2022-02-02", "2021-01-01", "electronic", 2022));
        database.addProduct(new LearningResource("Physics 101", "Physics 101 textbook", 59.99F, 59, sellers.get(3), 10, 102949, "John Doe", "McGraw Hill", "2022-02-02", "2021-01-01", "physic", 2022));
        database.addProduct(new LearningResource("Chemistry 101", "Chemistry 101 textbook", 59.99F, 59, sellers.get(3), 10, 1029450, "John Doe", "McGraw Hill", "2022-02-02", "2021-01-01", "chemistry", 2022));
        database.addProduct(new LearningResource("Biology 101", "Biology 101 textbook", 59.99F, 59, sellers.get(3), 10, 1029451, "John Doe", "McGraw Hill", "2022-02-02", "2021-01-01", "biology", 2022));
        database.addProduct(new Stationery("Pencil", "A pencil", 6.99F, 6, sellers.get(4), 17, "Staedtler", "2000", "Pencils", "2021-01-01", "2022-01-01"));
        database.addProduct(new Stationery("Pen", "A pen", 6.99F, 6, sellers.get(4), 17, "Schneider", "2000", "Pens", "2021-01-01", "2022-01-01"));
        database.addProduct(new Stationery("Eraser", "An eraser", 6.99F, 6, sellers.get(4), 17, "Staedtler", "2000", "Erasers", "2021-01-01", "2022-01-01"));
        database.addProduct(new Stationery("Ruler", "A ruler", 6.99F, 6, sellers.get(4), 17, "RuleTheWorld", "2000", "Rulers", "2021-01-01", "2022-01-01"));
    }

    private static void putFakeUsers(ArrayList<User> users) {
        users.add(new Buyer("Cynthia","Phan","cphan98", "1234", "ghi@jkl", "2005748362", new Address("1567 Av. Random Road", "Canada", "Quebec", "Montreal", "A1A1A3")));
        users.add(new Buyer("Nathan","Razaf","Asp3rity", "J2s3jAsd", "abc@def", "2003950384", new Address("1234 Av. Random Road", "Canada", "Quebec", "Montreal", "A1A1A2")));
        users.add(new Buyer("Lucas","Ranaivo","sacul", "1234", "luc@g.com", "2005102948", new Address("1345 Av. Random Road", "Canada", "Quebec", "Montreal", "A1A1A1")));
        users.add(new Buyer("Nathan","Rasami","monpote20", "1234", "nat@g.com", "2005583927", new Address("1153 Av. Random Road", "Canada", "Quebec", "Montreal", "A1A1A4")));
        users.add(new Buyer("Aaron","Leong","zhuyi", "1234", "zhuyi@g.com", "2005406976", new Address("1166 Av. Random Road", "Canada", "Quebec", "Montreal", "A1A1A5")));
        users.add(new Buyer("Kevin","Tran","kevtran", "1234", "ktran@gmail.com", "2005406978", new Address("1167 Av. Random Road", "Canada", "Quebec", "Montreal", "A1A1A6")));
        users.add(new Buyer("Kenny","Tran","kentran", "1234", "levy@gm.fr", "2005406939", new Address("1168 Av. Random Road", "Canada", "Quebec", "Montreal", "A1A1A7")));
        users.add(new Buyer("Levy","Tran","levtran", "1234", "pat@a.com", "2005406970", new Address("1169 Av. Random Road", "Canada", "Quebec", "Montreal", "A1A1A8")));
        users.add(new Buyer("Patrick","Tran","pattran", "1234", "luc@mg.fr", "2005406971", new Address("1170 Av. Random Road", "Canada", "Quebec", "Montreal", "A1A1A9")));
        users.add(new Buyer("Luc","Tran","luctran", "1234", "abb@aj.di", "2005406972", new Address("1171 Av. Random Road", "Canada", "Quebec", "Montreal", "A1A1B1")));
        users.add(new Seller("Trinh", "1234", "def@abc", "2004456745", new Address("1156 Av. Random Road", "Canada", "Quebec", "Montreal", "A1A1A6"), Category.ELECTRONICS));
        users.add(new Seller("Laura", "1234", "jkl@ghi", "2006294850", new Address("1990 Av. Random Road", "Canada", "Quebec", "Montreal", "A1A1A7"), Category.BOOKS));
        users.add(new Seller("Randy", "1234", "mno@pqr", "2007103948", new Address("1432 Av. Random Road", "Canada", "Quebec", "Montreal", "A1A1A8"), Category.DESKTOP_ACCESSORIES));
        users.add(new Seller("Deraina", "1234", "pqr@mno", "2008103985", new Address("1845 Av. Random Road", "Canada", "Quebec", "Montreal", "A1A1A9"), Category.LEARNING_RESOURCES));
        users.add(new Seller("Anthony", "1234", "stu@vwx", "2009304958", new Address("1764 Av. Random Road", "Canada", "Quebec", "Montreal", "A1A1B1"), Category.STATIONERY));
    }
}
