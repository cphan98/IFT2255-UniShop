import BackEndUtility.Category;
import BackEndUtility.DataBase;
import Users.Buyer;
import Users.Seller;
import Users.User;
import UtilityObjects.Address;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import productClasses.Inheritances.Stationery;
import productClasses.Product;
import productClasses.Usages.Evaluation;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class BuyerMetricsTest {

    private DataBase database;
    private Buyer buyer;
    private Seller seller;

    @BeforeEach
    void setUp() {
        // create buyer
        Address address = new Address("17 Doctor Dr.", "Canada", "Grand Line", "Drum Island",
                "C0P1P1");
        buyer = new Buyer("Tony Tony", "Chopper", "dr_chopper", "cottoncandy",
                "dr_chopper@onepice.com", "0123456789", address);

        // create seller
        Address addressSeller = new Address("19 King St.", "Brazil", "East Blue", "Foosha Village", "L1F1F1");
        seller = new Seller("Monkey D. Luffy", "ilovemeat", "king_of_pirates@onepiece.com",
                "0123456789", addressSeller, Category.LEARNING_RESOURCES);


        // create list of users
        ArrayList<User> users = new ArrayList<>();
        users.add(buyer);
        users.add(seller);

        // initialize database with list of users
        database = new DataBase(users);
    }

    @Test
    void testAddExpPoints() {
        // add experience points to buyer
        int xp = 1999;
        database.getBuyer(buyer).getMetrics().addExpPoints(xp);

        // assert addExpPoints
        assertEquals(xp, database.getBuyer(buyer).getMetrics().getExpPoints());
    }

    @Test
    void testAddBuyPoints() {
        // add buy points to buyer
        int points = 2023;
        database.getBuyer(buyer).getMetrics().addBuyPoints(points);

        // assert addBuyPoints
        assertEquals(points, database.getBuyer(buyer).getMetrics().getBuyPoints());
    }

    @Test
    void testUpdateAverageNoteGiven() {
        // create products
        Product product1 = new Stationery("DeathNote", "A notebook", 1.00F, 1, seller,
                100, "The Shinigamis", "4444", "Paper", "2006-10-04", "2006-10-04");
        Product product2 = new Stationery("Sticky notes", "A sticky note", 1.00F, 1, seller,
                100, "The Straw Hats", "3000", "Paper", "1999-10-20", "1999-10-20");

        // create another buyer
        Address address = new Address("19 King St.", "Brazil", "East Blue", "Foosha Village", "L1F1F1");
        Buyer buyer2 = new Buyer("Roronoa", "Zoro", "pirate_hunter", "santoryu",
                "zoro@google.maps", "0123456789", address);
        database.addUser(buyer2);

        // creat evaluation
        Evaluation evaluation1 = new Evaluation("Writing names don't do anything...", 3.3F, buyer);
        Evaluation evaluation2 = new Evaluation("Sturdy cover!", 9.8F, buyer2);
        Evaluation evaluation3 = new Evaluation("Notes are very sticky!", 9.0F, buyer);

        // average note given
        float note = (evaluation1.getRating() + evaluation3.getRating()) / 2;

        // adding evaluations to product to make result
        database.addEvaluationToProduct(product1, evaluation1);
        database.addEvaluationToProduct(product1, evaluation2);
        database.addEvaluationToProduct(product2, evaluation3);

        //assert updateAverageNoteGiven
        assertEquals(note, buyer.getMetrics().getAverageNoteGiven());
        assertEquals(evaluation2.getRating(), evaluation2.getAuthor().getMetrics().getAverageNoteGiven());
    }
}