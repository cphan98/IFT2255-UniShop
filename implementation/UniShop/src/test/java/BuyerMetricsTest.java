import BackEndUtility.DataBase;
import Users.Buyer;
import Users.User;
import UtilityObjects.Address;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class BuyerMetricsTest {

    private DataBase database;
    private Buyer buyer;

    @BeforeEach
    void setUp() {
        // create buyer
        Address address = new Address("17 Doctor Dr.", "Canada", "Grand Line", "Drum Island",
                "C0P1P1");
        buyer = new Buyer("Tony Tony", "Chopper", "dr_chopper", "cottoncandy",
                "dr_chopper@onepice.com", "0123456789", address);

        // create list of users
        ArrayList<User> users = new ArrayList<>();
        users.add(buyer);

        // initialize database with list of users
        database = new DataBase(users);
    }

    @Test
    void test_AddExpPoints() {
        // add experience points to buyer
        int xp = 1999;
        database.getBuyer(buyer).getMetrics().addExpPoints(xp);

        // assert addExpPoints
        assertEquals(xp, database.getBuyer(buyer).getMetrics().getExpPoints());
    }

    @Test
    void test_AddBuyPoints() {
        // add buy points to buyer
        int points = 2023;
        database.getBuyer(buyer).getMetrics().addBuyPoints(points);

        // assert addBuyPoints
        assertEquals(points, database.getBuyer(buyer).getMetrics().getBuyPoints());
    }
}