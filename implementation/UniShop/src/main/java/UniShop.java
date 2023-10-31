import LoginUtility.DataBase;
import UIs.HomeScreen;
import Users.Buyer;
import Users.Seller;
import Users.User;

import java.util.ArrayList;

public class UniShop {
    private static DataBase database;
    public static void main(String[] args) {
        ArrayList<User> users = new ArrayList<>();
        users.add(new Buyer("pd","lol","abc", "1234", "abc@def", 2003, "1267 av."));
        users.add(new Seller("def", "1234", "def@abc", 2004, "1268 av.", "Computers"));
        users.add(new Buyer("montcuq","flemme","ghi", "1234", "ghi@jkl", 2005, "1269 av."));
        users.add(new Seller("jkl", "1234", "jkl@ghi", 2006, "1270 av.", "Books"));
        database = new DataBase(users);
        HomeScreen homeScreen = new HomeScreen(database);
        homeScreen.initialize();
    }
}