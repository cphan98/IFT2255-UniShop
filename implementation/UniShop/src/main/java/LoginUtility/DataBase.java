package LoginUtility;

import Users.Seller;
import Users.User;
import products.Order;
import products.Product;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

public class DataBase {
    private final ArrayList<User> users;
    private final ArrayList<Product> products = new ArrayList<>();
    private final ArrayList<Order> orders = new ArrayList<>();

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
        assignOrders();
        updateOrderIDCounts();
    }
    public void addProduct(Product product) {
        products.add(product);
        // add into the seller's product list as well
        Seller seller = getSeller(product.getSeller());
        seller.getProducts().add(product);
    }
    public void removeProduct(Product product) {
        products.remove(product);
        // remove from the seller's product list as well
        Seller seller = getSeller(product.getSeller());
        seller.getProducts().remove(product);
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
        if ( validateNewUser(user.getId(), user.getEmail()) ) {
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

    public boolean validateNewUser(String id, String email) {
        for (User u : users) {
            if (u.getId().equals(id) || u.getEmail().equals(email)) {
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

    public void assignOrders() {
        getUsers().forEach(user -> {
            user.setOrderHistory(new ArrayList<>());
        });
        for (Order order : orders) {
            order.getBuyer().addOrder(order);
            Iterator<Product> it = order.getProducts().keySet().iterator();
            Product thisOne = it.next();
            thisOne.getSeller().addOrder(order);
        }
    }
    public boolean verifyNewProduct(Product product) {
        boolean valid = true;
        for (Product p : getProducts()) {
            if (p.getTitle().equals(product.getTitle())) {
                valid = false;
                break;
            }
        }
        return valid;
    }

    public boolean check24H(User user) {
        if (!user.getChecked24H()) {
            Date startTime = user.getStartTime();
            Date endTime = Calendar.getInstance().getTime();
            if (startTime != null) {
                long difference = endTime.getTime() - startTime.getTime();
                long diffInHrs = difference / 3600000;
                System.out.println(diffInHrs);
                if (diffInHrs >= 24) {
                    return false;
                }
                user.setChecked24H(true);
            }
        }
        return user.getChecked24H();
    }
    @Override
    public String toString() {
        return "DataBase{" +
                "users=" + users +
                '}';
    }
}
