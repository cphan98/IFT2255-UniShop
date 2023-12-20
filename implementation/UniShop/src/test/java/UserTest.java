import BackEndUtility.Category;
import BackEndUtility.DataBase;
import Users.Buyer;
import Users.Seller;
import Users.User;
import UtilityObjects.Address;
import UtilityObjects.CreditCard;
import UtilityObjects.Notification;
import productClasses.Inheritances.Stationery;
import productClasses.Product;
import productClasses.Usages.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    private Buyer user;
    private DataBase database;
    @BeforeEach
    void setUp() {
        //create buyer/seller
        Address address = new Address("19 King St.", "Brazil", "East Blue", "Foosha Village",
                "L1F1F1");

        user = new Buyer("Tony Tony", "Chopper", "dr_chopper", "cottoncandy", "dr_chopper@onepice.com", "0123456789", address);
        user.setCard(new CreditCard("1234567890123456", "Tony Tony", "Chopper", "12/23"));

        // create list of users
        ArrayList<User> users = new ArrayList<>();
        users.add(user);

        // initialize database with list of users
        database = new DataBase(users);
    }

    @Test
    void test_addNotification() {
        // create notification
        Notification newNotification = new Notification("Greetings", "Welcome to UniShop!");

        // add notification to user
        user.addNotification(newNotification);

        // make expected result
        Queue<Notification> testNotifications = new LinkedList<>();
        testNotifications.add(newNotification);

        // assert addNotification
        assertEquals(testNotifications, user.getNotifications(), "The new notification is in the user's notifications list");
    }

    @Test
    void test_addOrder() {
        // create seller
        Address address = new Address("19 King St.", "Brazil", "East Blue", "Foosha Village",
                "L1F1F1");
        Seller seller = new Seller("Monkey D. Luffy", "ilovemeat", "king_of_pirates@onepiece.com",
                "0123456789", address, Category.LEARNING_RESOURCES);
        database.addUser(seller);

        Product product = new Stationery("Sticky notes", "A sticky note", 1.00F, 1, seller,
                100, "The Straw Hats", "3000", "Paper", "1999-10-20", "1999-10-20");
        database.addProduct(product);

        //create products
        HashMap<Product, Integer> products = new HashMap<>();
        products.put(product, 10);

        //create order
        Order order = new Order(user, "credit card", products);

        //add order to user
        user.addOrder(order);

        //make expected result
        ArrayList<Order> testOrders = user.getOrderHistory();
        testOrders.add(order);

        //assert addOrder
        assertEquals(testOrders, user.getOrderHistory(), "The new order is in the user's order history");
    }
}
