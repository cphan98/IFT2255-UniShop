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
        Buyer nathanRasami = (Buyer) users.get(3);
        Buyer aaron = (Buyer) users.get(4);
        Buyer kevin = (Buyer) users.get(5);
        Buyer lenny = (Buyer) users.get(6);
        Buyer levy = (Buyer) users.get(7);
        Buyer patrick = (Buyer) users.get(8);
        Buyer luc = (Buyer) users.get(9);
        Buyer laura = (Buyer) users.get(10);
        Buyer mimi = (Buyer) users.get(11);
        Buyer eren = (Buyer) users.get(12);


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

        BuyerMenu anotherBuyerMenu = new BuyerMenu(kevin, database);
        anotherBuyerMenu.getUiUtilities().toggleBuyerToFollowing(kevin, (Buyer) users.get(5));
        anotherBuyerMenu.getUiUtilities().toggleBuyerToFollowing(kevin, (Buyer) users.get(6));
        anotherBuyerMenu.getUiUtilities().toggleBuyerToFollowing(kevin, (Buyer) users.get(7));
        anotherBuyerMenu.getUiUtilities().toggleBuyerToFollowing(kevin, (Buyer) users.get(8));
        anotherBuyerMenu.getUiUtilities().toggleProductToWishList(((Buyer) users.get(9)), products.get(4));
        anotherBuyerMenu.getUiUtilities().toggleProductToWishList(((Buyer) users.get(9)), products.get(5));

        anotherBuyerMenu.getUiUtilities().toggleProductToWishList(((Buyer) users.get(6)), products.get(5));
        anotherBuyerMenu.getUiUtilities().toggleProductToWishList(((Buyer) users.get(6)), products.get(6));

        anotherBuyerMenu.getUiUtilities().toggleProductToWishList(((Buyer) users.get(7)), products.get(6));
        anotherBuyerMenu.getUiUtilities().toggleProductToWishList(((Buyer) users.get(7)), products.get(7));

        anotherBuyerMenu.getUiUtilities().toggleProductToWishList(((Buyer) users.get(8)), products.get(7));
        anotherBuyerMenu.getUiUtilities().toggleProductToWishList(((Buyer) users.get(8)), products.get(8));

        BuyerMenu thirdBuyerMenu = new BuyerMenu(luc, database);
        thirdBuyerMenu.getUiUtilities().toggleBuyerToFollowing(luc, (Buyer) users.get(10));
        thirdBuyerMenu.getUiUtilities().toggleBuyerToFollowing(luc, (Buyer) users.get(11));
        thirdBuyerMenu.getUiUtilities().toggleBuyerToFollowing(luc, (Buyer) users.get(12));
        thirdBuyerMenu.getUiUtilities().toggleProductToWishList(((Buyer) users.get(10)), products.get(4));
        thirdBuyerMenu.getUiUtilities().toggleProductToWishList(((Buyer) users.get(10)), products.get(5));

        thirdBuyerMenu.getUiUtilities().toggleProductToWishList(((Buyer) users.get(11)), products.get(5));
        thirdBuyerMenu.getUiUtilities().toggleProductToWishList(((Buyer) users.get(11)), products.get(6));

        thirdBuyerMenu.getUiUtilities().toggleProductToWishList(((Buyer) users.get(12)), products.get(6));
        thirdBuyerMenu.getUiUtilities().toggleProductToWishList(((Buyer) users.get(12)), products.get(7));


        cynthia.setCard(new CreditCard("123456789012", "Cynthia", "Phan", "2022-01-01"));
        cynthia.getCart().addProduct(products.get(0), 1);
        cynthia.getCart().addProduct(products.get(4), 1);
        cynthia.getCart().addProduct(products.get(5), 1);
        database.generateAndAddOrders(cynthia, "credit card");
        cynthia.getCart().getProducts().clear();

        nathan.setCard(new CreditCard("123456789012", "Nathan", "Razaf", "2022-01-01"));
        nathan.getCart().addProduct(products.get(1), 1);
        nathan.getCart().addProduct(products.get(2), 1);
        nathan.getCart().addProduct(products.get(6), 1);
        nathan.getCart().addProduct(products.get(8), 2);
        nathan.getCart().addProduct(products.get(12), 1);
        database.generateAndAddOrders(nathan, "credit card");
        nathan.getCart().getProducts().clear();

        lucas.setCard(new CreditCard("123456789012", "Lucas", "Ranaivo", "2022-01-01"));
        lucas.getCart().addProduct(products.get(3), 1);
        lucas.getCart().addProduct(products.get(7), 1);
        lucas.getCart().addProduct(products.get(15), 1);
        lucas.getCart().addProduct(products.get(16), 3);
        database.generateAndAddOrders(lucas, "credit card");
        lucas.getCart().getProducts().clear();

        nathanRasami.setCard(new CreditCard("123456789012", "Nathan", "Razaf", "2022-01-01"));
        nathanRasami.getCart().addProduct(products.get(1), 2);
        nathanRasami.getCart().addProduct(products.get(3), 1);
        nathanRasami.getCart().addProduct(products.get(5), 1);
        database.generateAndAddOrders(nathanRasami, "credit card");
        nathanRasami.getCart().getProducts().clear();

        aaron.setCard(new CreditCard("123456789012", "Nathan", "Razaf", "2022-01-01"));
        aaron.getCart().addProduct(products.get(2), 1);
        aaron.getCart().addProduct(products.get(3), 1);
        aaron.getCart().addProduct(products.get(7), 1);
        aaron.getCart().addProduct(products.get(13), 1);
        database.generateAndAddOrders(aaron, "credit card");
        aaron.getCart().getProducts().clear();

        kevin.setCard(new CreditCard("123456789012", "Nathan", "Razaf", "2022-01-01"));
        kevin.getCart().addProduct(products.get(1), 1);
        kevin.getCart().addProduct(products.get(4), 2);
        kevin.getCart().addProduct(products.get(8), 1);
        kevin.getCart().addProduct(products.get(11), 1);
        database.generateAndAddOrders(kevin, "credit card");
        kevin.getCart().getProducts().clear();

        lenny.setCard(new CreditCard("123456789012", "Nathan", "Razaf", "2022-01-01"));
        lenny.getCart().addProduct(products.get(0), 1);
        lenny.getCart().addProduct(products.get(5), 2);
        lenny.getCart().addProduct(products.get(9), 1);
        lenny.getCart().addProduct(products.get(14), 1);
        database.generateAndAddOrders(lenny, "credit card");
        lenny.getCart().getProducts().clear();

        levy.setCard(new CreditCard("123456789012", "Nathan", "Razaf", "2022-01-01"));
        levy.getCart().addProduct(products.get(0), 1);
        levy.getCart().addProduct(products.get(5), 3);
        levy.getCart().addProduct(products.get(4), 1);
        database.generateAndAddOrders(levy, "credit card");
        levy.getCart().getProducts().clear();

        patrick.setCard(new CreditCard("123456789012", "Nathan", "Razaf", "2022-01-01"));
        patrick.getCart().addProduct(products.get(1), 1);
        patrick.getCart().addProduct(products.get(15), 1);
        patrick.getCart().addProduct(products.get(8), 1);
        patrick.getCart().addProduct(products.get(2), 4);
        database.generateAndAddOrders(patrick, "credit card");
        patrick.getCart().getProducts().clear();

        luc.setCard(new CreditCard("123456789012", "Nathan", "Razaf", "2022-01-01"));
        luc.getCart().addProduct(products.get(9), 1);
        luc.getCart().addProduct(products.get(4), 1);
        luc.getCart().addProduct(products.get(10), 1);
        luc.getCart().addProduct(products.get(17), 4);
        database.generateAndAddOrders(luc, "credit card");
        luc.getCart().getProducts().clear();

        laura.setCard(new CreditCard("123456789012", "Nathan", "Razaf", "2022-01-01"));
        laura.getCart().addProduct(products.get(7), 1);
        laura.getCart().addProduct(products.get(19), 1);
        laura.getCart().addProduct(products.get(18), 2);
        database.generateAndAddOrders(laura, "credit card");
        laura.getCart().getProducts().clear();

        mimi.setCard(new CreditCard("123456789012", "Nathan", "Razaf", "2022-01-01"));
        mimi.getCart().addProduct(products.get(6), 1);
        mimi.getCart().addProduct(products.get(4), 3);
        mimi.getCart().addProduct(products.get(5), 1);
        database.generateAndAddOrders(mimi, "credit card");
        mimi.getCart().getProducts().clear();

        eren.setCard(new CreditCard("123456789012", "Nathan", "Razaf", "2022-01-01"));
        eren.getCart().addProduct(products.get(2), 2);
        eren.getCart().addProduct(products.get(3), 1);
        eren.getCart().addProduct(products.get(8), 1);
        database.generateAndAddOrders(eren, "credit card");
        eren.getCart().getProducts().clear();

        int i = 0;
        for (OrderState status : OrderState.values()) {
            users.get(i).getOrderHistory().get(0).setStatus(status);
            i++;
        }

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

        users.add(new Buyer("Laura","Tran","lautrab", "1234", "lau@tr.di", "2005406944", new Address("1177 Av. Random Road", "Canada", "Quebec", "Montreal", "A5G1Y1")));
        users.add(new Buyer("Mimi","Tran","mimitran", "1234", "mimi@tr.ca", "2005406969", new Address("1444 Av. Random Road", "Canada", "Quebec", "Montreal", "B1A7B1")));
        users.add(new Buyer("Eren","Yeager","edtran", "1234", "edd@tr.vr", "2005406999", new Address("6969 Av. Random Road", "Canada", "Quebec", "Montreal", "A4A1D1")));

        users.add(new Seller("Trinh", "1234", "def@abc", "2004456745", new Address("1156 Av. Random Road", "Canada", "Quebec", "Montreal", "A1A1A6"), Category.ELECTRONICS));
        users.add(new Seller("Laura", "1234", "jkl@ghi", "2006294850", new Address("1990 Av. Random Road", "Canada", "Quebec", "Montreal", "A1A1A7"), Category.BOOKS));
        users.add(new Seller("Randy", "1234", "mno@pqr", "2007103948", new Address("1432 Av. Random Road", "Canada", "Quebec", "Montreal", "A1A1A8"), Category.DESKTOP_ACCESSORIES));
        users.add(new Seller("Deraina", "1234", "pqr@mno", "2008103985", new Address("1845 Av. Random Road", "Canada", "Quebec", "Montreal", "A1A1A9"), Category.LEARNING_RESOURCES));
        users.add(new Seller("Anthony", "1234", "stu@vwx", "2009304958", new Address("1764 Av. Random Road", "Canada", "Quebec", "Montreal", "A1A1B1"), Category.STATIONERY));
    }
}
