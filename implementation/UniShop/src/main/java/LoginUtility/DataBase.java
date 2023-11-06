package LoginUtility;

import Users.Seller;
import Users.User;
import products.Product;

import java.util.ArrayList;

public class DataBase {
    private ArrayList<User> users;

    public DataBase(ArrayList<User> users) {
        this.users = users;
    }

    public User getUser(String id, String password) {
        for (User user : users) {
            if (user.getId().equals(id) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }
    public ArrayList<Seller> getSellers() {
        ArrayList<Seller> sellers = new ArrayList<>();
        for (User user : users) {
            if (user instanceof Seller) {
                sellers.add((Seller) user);
            }
        }
        return sellers;
    }
    public boolean addUser(User user) {
        if (validateNewUser(user)) {
            users.add(user);
            return true;
        }
        return false;
    }
    public void removeUser(User user) {
        users.remove(user);
    }
    public void changePassword(User user, String newPassword) {
        user.setPassword(newPassword);
    }

    public boolean validateNewUser(User user) {
        for (User u : users) {
            if (u.getId().equals(user.getId())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return "DataBase{" +
                "users=" + users +
                '}';
    }
}
