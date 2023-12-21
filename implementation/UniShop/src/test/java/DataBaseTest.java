import BackEndUtility.Category;
import BackEndUtility.DataBase;
import Users.*;
import UtilityObjects.Address;
import UtilityObjects.CreditCard;
import UtilityObjects.Notification;
import org.junit.jupiter.api.AfterEach;
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

    @AfterEach
    void tearDown() {
        database = null;
        seller = null;
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
    void test_searchBuyerById() {
        // initialize buyers
        Address zoroAddress = new Address("19 King St.", "Brazil", "East Blue", "Foosha Village", "L1F1F1");
        Buyer zoro = new Buyer("Roronoa", "Zoro", "pirate_hunter_strawhat", "santoryu",
                "zoro@google.maps", "0123456789", zoroAddress);
        Address namiAddress = new Address("19 King St.", "Brazil", "East Blue", "Foosha Village", "L1F1F1");
        Buyer nami = new Buyer("Nami", "Cat Burglar", "cat_burglar_strawhat", "clima_tact",
                "nami@swaan.com", "0123456789", namiAddress);
        Address usoppAddress = new Address("19 King St.", "Brazil", "East Blue", "Foosha Village", "L1F1F1");
        Buyer usopp = new Buyer("Usopp", "Sogeking", "sogeking_strahwat", "kabuto",
                "usopp@liar.com", "0123456789", usoppAddress);
        Address sanjiAddress = new Address("19 King St.", "Brazil", "East Blue", "Foosha Village", "L1F1F1");
        Buyer sanji = new Buyer("Sanji", "Black Leg", "black_leg_strawhat", "diable_jambe",
                "sanji@cook.com", "0123456789", sanjiAddress);

        // add buyers to database
        database.addUser(zoro);
        database.addUser(nami);
        database.addUser(usopp);
        database.addUser(sanji);

        // assert searchBuyerById
        ArrayList<Buyer> actualBuyers = database.searchBuyerById("strawhat");
        ArrayList<Buyer> expectedBuyers = new ArrayList<>();
        expectedBuyers.add(zoro);
        expectedBuyers.add(nami);
        expectedBuyers.add(sanji);

        ArrayList<Buyer> actualBuyers2 = database.searchBuyerById("sogeking");
        ArrayList<Buyer> expectedBuyers2 = new ArrayList<>();
        expectedBuyers2.add(usopp);

        ArrayList<Buyer> actualBuyers3 = database.searchBuyerById("luffy");

        ArrayList<Buyer> actualBuyers4 = database.searchBuyerById("");
        ArrayList<Buyer> expectedBuyers4 = new ArrayList<>();
        expectedBuyers4.add(zoro);
        expectedBuyers4.add(nami);
        expectedBuyers4.add(usopp);
        expectedBuyers4.add(sanji);

        assertAll("Check all functionalities of searchBuyerById",
                () -> assertEquals(expectedBuyers, actualBuyers, "The search results are correct when many buyers are found"),
                () -> assertEquals(expectedBuyers2, actualBuyers2, "The search results are correct when only one buyer is found"),
                () -> assertEquals(0, actualBuyers3.size(), "The search results are correct when no buyer is found"),
                () -> assertEquals(expectedBuyers4, actualBuyers4, "The search returns all buyers when input is empty")
        );
    }
}