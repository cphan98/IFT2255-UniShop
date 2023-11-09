import LoginUtility.DataBase;
import UIs.HomeScreen;
import Users.Address;
import Users.Buyer;
import Users.Seller;
import Users.User;
import otherUtility.Category;
import products.*;

import java.util.ArrayList;
import java.util.Random;

public class UniShop {
    private static DataBase database;
    public static void main(String[] args) {
        makeFakeData();
        HomeScreen homeScreen = new HomeScreen(database);
        homeScreen.initialize();
    }

    private static void makeFakeData() {
        ArrayList<User> users = new ArrayList<>();
        putFakeUsers(users);
        database = new DataBase(users);
        database.getSellers().forEach(seller -> {
            Product product = putFakeProduct(seller);
            database.addProduct(product);
            product.setLikes(Math.round(new Random().nextFloat() * 1000));

        });
    }

    private static Product putFakeProduct(Seller seller) {
        return switch (seller.getCategory()) {
            case BOOKS -> new Book("Harry Potter", "Harry Potter and the Philosopher's Stone", 69.99F, 100, seller, 10, 123234, "J.K. Rowling", "Glenat", "Fantastic", "2021-01-01", "2021-01-01", 2022, 1);
            case LEARNING_RESOURCES -> new LearningResource("Math 101", "Math 101 textbook", 100.0F, 100, seller, 10, 102948, "John Doe", "McGraw Hill", "2022-02-02", "2021-01-01", "electronic", 2022);
            case STATIONERY -> new Stationery("Pencil", "A pencil", 6.99F, 10, seller, 17, "Staedtler", "2000", "Pencils", "2021-01-01", "2022-01-01");
            case ELECTRONICS -> new Hardware("Laptop", "A laptop", 1000.0F, 1000, seller, 68, "NewEgg", "YTP-2099", "2023-09-01", "Laptops", "2022-01-01");
            case DESKTOP_ACCESSORIES -> new OfficeEquipment("Pencil Sharpener", "A pencil sharpener", 15.99F, 100, seller, 100, "Yamaha", "YTP-2098", "Pencil Sharpeners", "2021-02-04");
        };
    }

    private static void putFakeUsers(ArrayList<User> users) {
        users.add(new Buyer("Nathan","Razaf","Asp3rity", "J2s3jAsd", "abc@def", "2003", new Address("1111 Av. Random Road", "Canada", "Quebec", "Montreal", "A1A1A1")));
        users.add(new Seller("Trinh", "1234", "def@abc", "2004", new Address("1111 Av. Random Road", "Canada", "Quebec", "Montreal", "A1A1A1"), Category.ELECTRONICS));
        users.add(new Buyer("Cynthia","Phan","cphan98", "1234", "ghi@jkl", "2005", new Address("1111 Av. Random Road", "Canada", "Quebec", "Montreal", "A1A1A1")));
        users.add(new Seller("Laura", "1234", "jkl@ghi", "2006", new Address("1111 Av. Random Road", "Canada", "Quebec", "Montreal", "A1A1A1"), Category.BOOKS));
        users.add(new Seller("mno", "1234", "mno@pqr", "2007", new Address("1111 Av. Random Road", "Canada", "Quebec", "Montreal", "A1A1A1"), Category.DESKTOP_ACCESSORIES));
        users.add(new Seller("pqr", "1234", "pqr@mno", "2008", new Address("1111 Av. Random Road", "Canada", "Quebec", "Montreal", "A1A1A1"), Category.LEARNING_RESOURCES));
        users.add(new Seller("stu", "1234", "stu@vwx", "2009", new Address("1111 Av. Random Road", "Canada", "Quebec", "Montreal", "A1A1A1"), Category.STATIONERY));
    }
}
