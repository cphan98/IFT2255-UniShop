import BackEndUtility.Category;
import BackEndUtility.DataBase;
import Users.*;
import UtilityObjects.Address;
import UtilityObjects.CreditCard;
import UtilityObjects.Notification;
import org.junit.jupiter.api.BeforeEach;
import productClasses.Inheritances.LearningResource;
import productClasses.Inheritances.Stationery;
import productClasses.Product;
import productClasses.Usages.Order;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.*;

class DataBaseTest {

    private DataBase database;
    private Seller seller;

    @BeforeEach
    void setUp() {
        // create seller
        Address address = new Address("19 King St.", "Brazil", "East Blue", "Foosha Village", "L1F1F1");
        seller = new Seller("Monkey D. Luffy", "ilovemeat", "king_of_pirates@onepiece.com",
                "0123456789", address, Category.LEARNING_RESOURCES);

        // create list of users
        ArrayList<User> users = new ArrayList<>();
        users.add(seller);

        // initialize database with list of users
        database = new DataBase(users);
    }

    @Test
    void testAddProduct() {
        // create product
        Product product = new Stationery("Sticky notes", "A sticky note", 1.00F, 1, seller,
                100, "The Straw Hats", "3000", "Paper", "1999-10-20", "1999-10-20");

        // add follower to seller
        Address address = new Address("19 King St.", "Brazil", "East Blue", "Foosha Village", "L1F1F1");
        Buyer testBuyer = new Buyer("Roronoa", "Zoro", "pirate_hunter", "santoryu",
                "zoro@google.maps", "0123456789", address);
        seller.addFollower(testBuyer);

        // add product to database
        database.addProduct(product);

        // make expected result
        // product
        ArrayList<Product> testProducts = new ArrayList<>();
        testProducts.add(product);
        // notification
        Queue<Notification> testNotifications = new LinkedList<>();
        Notification notification = new Notification("New product just added!", "This Monkey D. Luffy just added a new product!");
        testNotifications.add(notification);

        // assert addProduct
        assertAll("Check all functionalities of addProduct",
                () -> assertEquals(testProducts, database.getProducts(), "The new product is in the database"),
                () -> assertEquals(testProducts, seller.getProducts(), "The new product is in the seller's products list"),
                () -> assertEquals(testNotifications, testBuyer.getNotifications(), "The new product is in the following buyer's notifications list")
        );
    }

    @Test
    void test_VerifyNewProduct_forExistingProduct() {
        // create product
        Product product = new Stationery("Sticky notes", "A sticky note", 1.00F, 1, seller,
                100, "The Straw Hats", "3000", "Paper", "1999-10-20", "1999-10-20");

        // add product to database
        database.addProduct(product);

        // assert verifyNewProduct
        assertFalse(database.verifyNewProduct(product), "Product already exists in database");
    }

    @Test
    void test_VerifyNewProduct_forNewProduct() {
        // create product
        Product newProduct = new Stationery("Sticky notes", "A sticky note", 1.00F, 1, seller,
                100, "The Straw Hats", "3000", "Paper", "1999-10-20", "1999-10-20");

        // we don't add it

        // assert verifyNewProduct
        assertTrue(database.verifyNewProduct(newProduct), "Product does not exist in database");
    }

    @Test
    void test_addOrder() {
        // create products
        Product product1 = new Stationery("Sticky notes", "A sticky note", 1.00F, 1, seller,
                 100, "The Straw Hats", "3000", "Paper", "1999-10-20", "1999-10-20");
        Seller seller2 = new Seller("pirate_hunter", "santoryu", "zoro@google.maps", "0123456789",
                 new Address("19 King St.", "Brazil", "East Blue", "Foosha Village",
                "L1F1F1"), Category.LEARNING_RESOURCES);
        database.addUser(seller2);
        Product product2 = new LearningResource("How to be a pirate", "A guide to being a pirate",
                2.00F, 1, seller2, 100, 52589849, "3000", "Paper", "1999-10-20", "1999-10-20", "English", 2022);

        // add products to database
        database.addProduct(product1);
        database.addProduct(product2);

        // initialize buyer
        Address address = new Address("17 Doctor Dr.", "Canada", "Grand Line", "Drum Island",
                "C0P1P1");
        Buyer buyer = new Buyer("Tony Tony", "Chopper", "dr_chopper", "cottoncandy", "dr_chopper@onepice.com", "0123456789", address);
        buyer.setCard(new CreditCard("1234567890123456", "Tony Tony", "Chopper", "2022-10-20"));
        database.addUser(buyer);

        // create orders
        HashMap<Product, Integer> products1 = new HashMap<>();
        products1.put(product1, 10);
        Order order1 = new Order(buyer, "credit card", products1);
        HashMap<Product, Integer> products2 = new HashMap<>();
        products2.put(product2, 15);
        Order order2 = new Order(buyer, "credit card", products2);

        // add orders to database
        database.addOrder(order1);
        database.addOrder(order2);

        // make expected result
        ArrayList<Order> expectedBuyerOrderHistory = new ArrayList<>();
        expectedBuyerOrderHistory.add(order1);
        expectedBuyerOrderHistory.add(order2);

        ArrayList<Order> expectedSeller1OrderHistory = new ArrayList<>();
        expectedSeller1OrderHistory.add(order1);

        ArrayList<Order> expectedSeller2OrderHistory = new ArrayList<>();
        expectedSeller2OrderHistory.add(order2);

        // assert addOrder
        assertAll("Test addOrder",
                () -> assertEquals(expectedBuyerOrderHistory, buyer.getOrderHistory(), "The buyer's order history is updated"),
                () -> assertEquals(expectedSeller1OrderHistory, seller.getOrderHistory(), "The seller's order history is updated"),
                () -> assertEquals(expectedSeller2OrderHistory, seller2.getOrderHistory(), "The seller's order history is updated"),
                () -> assertEquals(2, buyer.getMetrics().getOrdersMade(), "The buyer's orders made are updated"),
                () -> assertEquals(25, buyer.getMetrics().getProductsBought(), "The buyer's products bought are updated"),
                () -> assertEquals(10, seller.getMetrics().getProductsSold(), "The seller's products sold are updated"),
                () -> assertEquals(15, seller2.getMetrics().getProductsSold(), "The seller's products sold are updated"),
                () -> assertEquals(10, seller.getMetrics().getRevenue(), "The seller's revenue is updated"),
                () -> assertEquals(30, seller2.getMetrics().getRevenue(), "The seller's revenue is updated")

        );
    }
}