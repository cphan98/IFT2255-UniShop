package BackEndUtility;

import Users.Buyer;
import UtilityObjects.Address;
import UtilityObjects.CreditCard;
import UtilityObjects.Notification;
import Users.Seller;
import Users.User;
import productClasses.Usages.Evaluation;
import productClasses.Usages.Order;
import productClasses.Product;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DataBase implements java.io.Serializable {
    private final ArrayList<User> users;
    private final ArrayList<Product> products = new ArrayList<>();
    private final ArrayList<Order> orders = new ArrayList<>();
    private final ArrayList<Buyer> top5 = new ArrayList<>();

    public DataBase(ArrayList<User> users) {
        this.users = users;
    }
    
    public DataBase() {
        this.users = new ArrayList<>();
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
    public void addOrder(Order order) {
        orders.add(order);
        assignOrders();
        updateOrderIDCounts();

        order.getBuyer().getMetrics().setOrdersMade(order.getBuyer().getOrderHistory().size());
        int buyerProductsBought = 0;
        for (Order o : order.getBuyer().getOrderHistory()) {
            for (Product p : o.getProducts().keySet()) {
                buyerProductsBought += o.getProducts().get(p);
            }
        }
        order.getBuyer().getMetrics().setProductsBought(buyerProductsBought);

        Seller seller = order.getProducts().keySet().iterator().next().getSeller();
        int sellerProductsSold = 0;
        int sellerRevenue = 0;
        for (Order o: seller.getOrderHistory()) {
            for (Product p : o.getProducts().keySet()) {
                sellerProductsSold += o.getProducts().get(p);
                sellerRevenue += (int) (p.getPrice() * o.getProducts().get(p));
            }
        }
        seller.getMetrics().updateProductsSold(sellerProductsSold);
        seller.getMetrics().updateRevenue(sellerRevenue);

    }
    public void addProduct(Product product) {
        products.add(product);
        // add into the seller's product list as well
        Seller seller = getSeller(product.getSeller());
        seller.getProducts().add(product);
        String title = "New product just added!";
        String summary = "This " + seller.getId() + " just added a new product!";
        for (Buyer follower : seller.getFollowers()){
            follower.addNotification(new Notification(title, summary));
        }
    }
    public void removeProduct(Product product) {
        products.remove(product);
        // remove from the seller's product list as well
        Seller seller = getSeller(product.getSeller());
        seller.getProducts().remove(product);
    }
    public Buyer getBuyer(Buyer buyer) {
        for (User person : users) {
            if (person.equals(buyer))
                return buyer;
        }
        return null;
    }
    public ArrayList<Buyer> getBuyers() {
        ArrayList<Buyer> buyers = new ArrayList<>();
        for (User user : users) {
            if (user instanceof Buyer) {
                buyers.add((Buyer) user);
            }
        }
        return buyers;
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
    public ArrayList<Buyer> getTop5() {
        top5Buyers(false);
        return top5;
    }
    public Integer getYourRank(Buyer buyer) {
        List<Buyer> listOfBuyers = sortBuyersByXp(true).map(Map.Entry::getKey).toList();
        return listOfBuyers.indexOf(buyer);
    }
    public ArrayList<Buyer> searchBuyerById(String id) {
        ArrayList<Buyer> listOfBuyers = new ArrayList<>();
        for (Buyer buyer : getBuyers()) {
            if (buyer.getId().contains(id)) {
                listOfBuyers.add(buyer);
            }
        }
        return listOfBuyers;
    }
    public ArrayList<Seller> searchSellerById(String id) {
        ArrayList<Seller> listOfSellers = new ArrayList<>();
        for (Seller seller : getSellers()) {
            if (seller.getId().contains(id)) {
                listOfSellers.add(seller);
            }
        }
        return listOfSellers;
    }
    public ArrayList<Buyer> searchBuyerByName(String name) {
        ArrayList<Buyer> listOfBuyers = new ArrayList<>();
        for (Buyer buyer : getBuyers()) {
            if (buyer.getFirstName().contains(name)) {
                listOfBuyers.add(buyer);
            } else if (buyer.getLastName().contains(name)) {
                listOfBuyers.add(buyer);
            }
        }
        return listOfBuyers;
    }
    public ArrayList<Seller> searchSellerByAddress(String address) {
        ArrayList<Seller> listOfSellers = new ArrayList<>();
        for (Seller seller : getSellers()) {
            if (seller.getAddress().getAddressLine().contains(address)) {
                listOfSellers.add(seller);
            }
        }
        return listOfSellers;
    }
    public void sortBuyer(boolean ascending, String filter) {
        switch (filter) {
            case "name":
                System.out.println("Buyers ordered by name:");
                sortBuyersByName(ascending).forEach(entry ->
                        System.out.println("Name: " + entry.getValue() + "\n")
                );
                break;
            case "ID":
                System.out.println("Buyers ordered by id:");
                sortBuyersById(ascending).forEach(entry ->
                        System.out.println("ID: " + entry.getValue() + "\n")
                );

                break;
            case "followers":
                System.out.println("Buyers ordered by number of followers:");
                sortBuyersByFollowers(ascending).forEach(entry ->
                        System.out.println("Followers: " + entry.getValue() + "\n")
                );

                break;
            case "xp":
                System.out.println("Buyers ordered by number of points:");
                sortBuyersByXp(ascending).forEach(entry ->
                        System.out.println("Points: " + entry.getValue() + "\n")
                );
                break;
        }
    }
    public Stream<Map.Entry<Buyer, String>> sortBuyersByName(boolean ascending) {
        HashMap<Buyer, String> buyerName = new HashMap<>();
        for (Buyer buyer : getBuyers()) {
            buyerName.put(buyer, buyer.getFirstName());
        }

        Stream<Map.Entry<Buyer, String>> sortedStream = buyerName.entrySet().stream()
                .sorted(Map.Entry.comparingByValue());

        if (!ascending) {
            sortedStream = sortedStream.sorted(Collections.reverseOrder(Map.Entry.comparingByValue()));
            return sortedStream;
        }
        return sortedStream;
    }
    public Stream<Map.Entry<Buyer, String>> sortBuyersById(boolean ascending) {
        HashMap<Buyer, String> buyerId = new HashMap<>();
        for (Buyer buyer : getBuyers()) {
            buyerId.put(buyer, buyer.getId());
        }

        Stream<Map.Entry<Buyer, String>> sortedStream = buyerId.entrySet().stream()
                .sorted(Map.Entry.comparingByValue());

        if (!ascending) {
            sortedStream = sortedStream.sorted(Collections.reverseOrder(Map.Entry.comparingByValue()));
            return sortedStream;
        }
        return sortedStream;
    }
    public Stream<Map.Entry<Buyer, Integer>> sortBuyersByFollowers(boolean ascending) {
        HashMap<Buyer, Integer> buyerFollowers = new HashMap<>();
        for (Buyer buyer : getBuyers()) {
            buyerFollowers.put(buyer, buyer.getFollowers().size());
        }

        Stream<Map.Entry<Buyer, Integer>> sortedStream = buyerFollowers.entrySet().stream()
                .sorted(Map.Entry.comparingByValue());

        if (!ascending) {
            sortedStream = sortedStream.sorted(Collections.reverseOrder(Map.Entry.comparingByValue()));
            return sortedStream;
        }
        return sortedStream;
    }
    public Stream<Map.Entry<Buyer, Integer>> sortBuyersByXp(boolean ascending) {
        HashMap<Buyer, Integer> buyersXP = new HashMap<>();
        for (Buyer buyer : getBuyers()) {
            buyersXP.put(buyer, buyer.getMetrics().getExpPoints());
        }

        Stream<Map.Entry<Buyer, Integer>> sortedStream = buyersXP.entrySet().stream()
                .sorted(Map.Entry.comparingByValue());

        if (!ascending) {
            sortedStream = sortedStream.sorted(Collections.reverseOrder(Map.Entry.comparingByValue()));
            return sortedStream;
        }
        return sortedStream;
    }
    public void top5Buyers(boolean ascending) {
        sortBuyersByXp(ascending).limit(5).forEach(entry -> {
            top5.add(entry.getKey());
                });
    }
    public void addUser(User user) {
        if ( validateNewUser(user.getId(), user.getEmail()) ) {
            users.add(user);
            System.out.println("User added successfully");
            return;
        }
        System.out.println("User already exists");
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
        getUsers().forEach(user -> user.setOrderHistory(new ArrayList<>()));
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
                if (diffInHrs >= 24) {
                    return false;
                }
                user.setChecked24H(true);
            }
        }
        return user.getChecked24H();
    }

    public void generateAndAddOrders(Buyer user, String paymentType, Address shippingAddress, String phoneNumber) {
        HashMap<Seller, HashMap<Product, Integer>> splitCart = splitCartBeforeOrder(user);
        for (Seller seller : splitCart.keySet()) {
            HashMap<Product, Integer> sellerProducts = splitCart.get(seller);
            addOrder(new Order(user, paymentType, shippingAddress, phoneNumber, sellerProducts));
        }
    }
    public void generateAndAddOrders(Buyer user, CreditCard creditCard, Address shippingAddress, String phoneNumber) {
        HashMap<Seller, HashMap<Product, Integer>> splitCart = splitCartBeforeOrder(user);
        for (Seller seller : splitCart.keySet()) {
            HashMap<Product, Integer> sellerProducts = splitCart.get(seller);
            addOrder(new Order(user, "credit card", creditCard, shippingAddress, phoneNumber, sellerProducts));
        }
    }
    public void generateAndAddOrders(Buyer user) {
        HashMap<Seller, HashMap<Product, Integer>> splitCart = splitCartBeforeOrder(user);
        for (Seller seller : splitCart.keySet()) {
            HashMap<Product, Integer> products = splitCart.get(seller);
            addOrder(new Order(user, "credit card", user.getCard(), products));
        }
    }
    public void generateAndAddOrders(Buyer user, String paymentType) {
        HashMap<Seller, HashMap<Product, Integer>> splitCart = splitCartBeforeOrder(user);
        for (Seller seller : splitCart.keySet()) {
            HashMap<Product, Integer> products = splitCart.get(seller);

            for(Product product : user.getCart().getProducts().keySet()){
                getSeller(seller).sellProduct(product, splitCart.size());
            }

            addOrder(new Order(user, paymentType, products));
        }
    }

    private HashMap<Seller, HashMap<Product, Integer>> splitCartBeforeOrder(Buyer user) {
        HashMap<Seller, HashMap<Product, Integer>> splitCart = new HashMap<>();
        HashMap<Product, Integer> cartProducts = user.getCart().getProducts();
        for (Product product : cartProducts.keySet()) {
            Seller seller = product.getSeller();
            HashMap<Product, Integer> sellerProducts;
            if (splitCart.containsKey(seller)) {
                sellerProducts = splitCart.get(seller);
            } else {
                sellerProducts = new HashMap<>();
            }
            sellerProducts.put(product, cartProducts.get(product));
            splitCart.put(seller, sellerProducts);

        }
        for ( Seller seller : splitCart.keySet())
        {
            for (Product product : splitCart.get(seller).keySet())
            {
                String title = "New order!";
                String summary = user.getId() + " just bought your " + product.getTitle() + "!";
                seller.addNotification(new Notification(title, summary));
            }
        }
        return splitCart;
    }

    public void addEvaluationToProduct(Product product, Evaluation evaluation) {
        evaluation.getAuthor().getEvaluationsMade().put(product, evaluation);
        evaluation.getAuthor().getMetrics().setEvaluationsMade(evaluation.getAuthor().getMetrics().getEvaluationsMade() + 1);
        evaluation.getAuthor().getMetrics().updateAverageNoteGiven(evaluation.getRating());
        product.getEvaluations().add(evaluation);
        product.getSeller().getMetrics().updateAverageNoteReceived(evaluation.getRating());
        product.updateOverallRating();
    }

    public void removeEvaluationFromProduct(Product product, Evaluation evaluation) {
        evaluation.getAuthor().getEvaluationsMade().remove(product);
        evaluation.getAuthor().getMetrics().setEvaluationsMade(evaluation.getAuthor().getMetrics().getEvaluationsMade() - 1);
        evaluation.getAuthor().getMetrics().removeAverageNoteGiven(evaluation.getRating());
        product.getEvaluations().remove(evaluation);
        product.getSeller().getMetrics().removeAverageNoteReceived(evaluation.getRating());
        product.updateOverallRating();
    }
    @Override
    public String toString() {
        return "DataBase{" +
                "users=" + users +
                '}';
    }
}
