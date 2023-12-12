package BackEndUtility;

import productClasses.Usages.Evaluation;
import productClasses.Usages.IssueQuery;
import productClasses.Usages.Order;
import productClasses.Product;
import Users.*;
import UtilityObjects.*;

import java.util.*;
import java.util.stream.Stream;

public class DataBase implements java.io.Serializable {

    // ATTRIBUTES

    private final ArrayList<User> users;
    private final ArrayList<Product> products = new ArrayList<>();
    private final ArrayList<Order> orders = new ArrayList<>();
    private final ArrayList<Buyer> top5 = new ArrayList<>();
    private int issueID = 0;
    private int reshipmentCount = 0;

    // CONSTRUCTORS

    public DataBase(ArrayList<User> users) {
        this.users = users;
    }
    
    public DataBase() {
        this.users = new ArrayList<>();
    }

    // GETTERS

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

    // UTILITIES

    // cart -----------------------------------------------------------------------------------------------------------

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
                seller.addCustomers(user);
            }
        }
        return splitCart;
    }

    // orders ---------------------------------------------------------------------------------------------------------

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

    public void updateOrderIDCounts() {
        for (int i = 0; i<orders.size(); i++) {
            orders.get(i).setId(orders.get(i).makeId(i+1));
        }
    }

    private void assignOrders() {
        getUsers().forEach(user -> user.setOrderHistory(new ArrayList<>()));
        for (Order order : orders) {
            order.getBuyer().addOrder(order);
            Iterator<Product> it = order.getProducts().keySet().iterator();
            Product thisOne = it.next();
            thisOne.getSeller().addOrder(order);
        }
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

    public String makeReshipmentTrackingNum() {
        int zeros = 12 - Integer.toString(++reshipmentCount).length();
        return ("0".repeat(zeros) + reshipmentCount);
    }

    // products -------------------------------------------------------------------------------------------------------

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

    // buyers ---------------------------------------------------------------------------------------------------------

    public ArrayList<Buyer> searchBuyerById(String id) {
        ArrayList<Buyer> listOfBuyers = new ArrayList<>();
        for (Buyer buyer : getBuyers()) {
            if (buyer.getId().contains(id)) {
                listOfBuyers.add(buyer);
            }
        }
        return listOfBuyers;
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
                        System.out.println("Buyer : " + entry.getKey().getId() + " has " + entry.getValue() + " followers\n")
                );

                break;
            case "xp":
                System.out.println("Buyers ordered by number of points:");
                sortBuyersByXp(ascending).forEach(entry ->
                        System.out.println("Buyer : " + entry.getKey().getId() + " has " + entry.getValue() + " points\n")
                );
                break;
        }
    }

    private Stream<Map.Entry<Buyer, String>> sortBuyersByName(boolean ascending) {
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

    private Stream<Map.Entry<Buyer, String>> sortBuyersById(boolean ascending) {
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

    private Stream<Map.Entry<Buyer, Integer>> sortBuyersByFollowers(boolean ascending) {
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

    private Stream<Map.Entry<Buyer, Integer>> sortBuyersByXp(boolean ascending) {
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

    private void top5Buyers(boolean ascending) {
        sortBuyersByXp(ascending).limit(5).forEach(entry -> {
            top5.add(entry.getKey());
        });
    }

    // sellers --------------------------------------------------------------------------------------------------------

    public ArrayList<Seller> searchSellerById(String id) {
        ArrayList<Seller> listOfSellers = new ArrayList<>();
        for (Seller seller : getSellers()) {
            if (seller.getId().contains(id)) {
                listOfSellers.add(seller);
            }
        }
        return listOfSellers;
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

    // users ----------------------------------------------------------------------------------------------------------

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

    // evaluations ----------------------------------------------------------------------------------------------------

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

    // issue queries --------------------------------------------------------------------------------------------------

    public void createTicket(IssueQuery issue) {
        System.out.println("What is the problem with the order?");
        System.out.println("1. Wrong item received");
        System.out.println("2. Defective product");
        System.out.println("3. Missing product ");
        System.out.println("4. Wrong model of the product ordered ");
        System.out.println("5. Order arrived too late");
        System.out.println("6. No longer need the product");
        System.out.println("7. The product does not match the description");
        System.out.println("8. The product does not meet expectations");
        System.out.println("9. Found a better price elsewhere");
        System.out.println("10. Fraudulent purchase");
        System.out.println("11. Return");
        int choice = getUserInputAsInteger();

        switch (choice) {
            case 1 -> issue.setIssueDescription("Wrong item received");
            case 2 -> issue.setIssueDescription("Defective product");
            case 3 -> issue.setIssueDescription("Missing product");
            case 4 -> issue.setIssueDescription("Wrong model of the product ordered");
            case 5 -> issue.setIssueDescription("Order arrived too late");
            case 6 -> issue.setIssueDescription("No longer need the product");
            case 7 -> issue.setIssueDescription("The product does not match the description");
            case 8 -> issue.setIssueDescription("The product does not meet expectations");
            case 9 -> issue.setIssueDescription("Found a better price elsewhere");
            case 10 -> issue.setIssueDescription("Fraudulent purchase");
            case 11 -> System.out.println("Returning to menu...");
            default -> System.out.println("Invalid selection. Please try again.");
        }
        if(choice < 11) {
            issue.setId(issue.makeId(++issueID));
            System.out.println("the seller will be notified shortly");
        }
    }

    // inputs ---------------------------------------------------------------------------------------------------------

    private int getUserInputAsInteger() {
        while (true) {
            try {
                int returned = Integer.parseInt(InputManager.getInstance().nextLine());
                if (returned < 0) {
                    System.out.println("Invalid input. Please enter a positive number.");
                } else {
                    return returned;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }

        }
    }

    // to string ------------------------------------------------------------------------------------------------------

    @Override
    public String toString() {
        return "DataBase{" +
                "users=" + users +
                '}';
    }
}
