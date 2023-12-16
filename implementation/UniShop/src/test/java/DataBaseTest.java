import BackEndUtility.Category;
import BackEndUtility.DataBase;
import Users.Seller;
import Users.User;
import UtilityObjects.Address;
import productClasses.Inheritances.Stationery;
import productClasses.Product;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class DataBaseTest {

    @Test
    void testAddProduct() {
        // create seller
        Address address = new Address("19 King St.", "Brazil", "East Blue", "Foosha Village",
                "L1F1F1");
        Seller seller = new Seller("Monkey D. Luffy", "ilovemeat", "king_of_pirates@onepiece.com",
                "0123456789", address, Category.LEARNING_RESOURCES);

        // create list of users
        ArrayList<User> users = new ArrayList<>();
        users.add(seller);

        // initialize database with list of users
        DataBase database = new DataBase(users);

        // create product
        Product product = new Stationery("Sticky notes", "A sticky note", 1.00F, 1, seller,
                100, "The Straw Hats", "3000", "Paper", "1999-10-20", "1999-10-20");

        // add product to database
        database.addProduct(product);

        // make expected result
        ArrayList<Product> testProducts = new ArrayList<>();
        testProducts.add(product);

        // assert addProduct
        assertEquals(testProducts, database.getProducts());
    }
}