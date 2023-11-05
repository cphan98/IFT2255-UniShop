import LoginUtility.DataBase;
import UIs.HomeScreen;
import Users.Address;
import Users.Buyer;
import Users.Seller;
import Users.User;
import otherUtility.Category;

import java.util.ArrayList;

public class UniShop {
    private static DataBase database;
    public static void main(String[] args) {
        ArrayList<User> users = new ArrayList<>();
        users.add(new Buyer("pd","lol","abc", "1234", "abc@def", "2003", new Address("1111 Av. Random Road", "Canada", "Quebec", "Montreal", "A1A1A1")));
        users.add(new Seller("def", "1234", "def@abc", "2004", new Address("1111 Av. Random Road", "Canada", "Quebec", "Montreal", "A1A1A1"), Category.ELECTRONICS));
        users.add(new Buyer("montcuq","flemme","ghi", "1234", "ghi@jkl", "2005", new Address("1111 Av. Random Road", "Canada", "Quebec", "Montreal", "A1A1A1")));
        users.add(new Seller("jkl", "1234", "jkl@ghi", "2006", new Address("1111 Av. Random Road", "Canada", "Quebec", "Montreal", "A1A1A1"), Category.BOOKS));
        database = new DataBase(users);
        HomeScreen homeScreen = new HomeScreen(database);
        homeScreen.initialize();
    }
}