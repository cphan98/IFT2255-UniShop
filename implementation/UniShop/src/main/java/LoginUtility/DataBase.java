package LoginUtility;

import Users.Seller;
import Users.User;
import products.Order;
import products.Product;

import java.util.ArrayList;

public class DataBase {
    private ArrayList<User> users;
    private ArrayList<Product> products = new ArrayList<>();
    private ArrayList<Order> orders = new ArrayList<>();

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
    public ArrayList<User> getUsers() {
        return users;
    }
    public ArrayList<Product> getProducts() {
        return products;
    }
    public ArrayList<Order> getOrders() {
        return orders;
    }
    public void addOrder(Order order) {
        orders.add(order);
    }
    public void addProduct(Product product) {
        products.add(product);
        // add into the seller's product list as well
        Seller seller = getSeller(product.getSeller());
        seller.getProducts().add(product);
    }
    public void removeProduct(Product product) {
        products.remove(product);
    }
    public void removeOrder(Order order) {
        orders.remove(order);
    }
    public Seller getSeller(Seller seller) {
        for (User person : users) {
            if (person.equals(seller))
                return seller;
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

    public void updateOrderIDCounts() {
        for (int i = 0; i<orders.size(); i++) {
            orders.get(i).setId(orders.get(i).makeId(i+1));
        }
    }
    @Override
    public String toString() {
        return "DataBase{" +
                "users=" + users +
                '}';
    }
}
