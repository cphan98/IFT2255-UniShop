package LoginUtility;
import Users.Seller;
import otherUtility.Category;
import products.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Catalog
{
    private HashSet<Product> books_catalog, learningRes_catalog, stationery_catalog,
            electronics_catalog, desktopAcc_catalog;
    private HashSet<Seller> sellers_list;

    public Catalog(ArrayList<Seller> sellers) {
        this.books_catalog = new HashSet<>();
        this.learningRes_catalog = new HashSet<>();
        this.stationery_catalog = new HashSet<>();
        this.electronics_catalog = new HashSet<>();
        this.desktopAcc_catalog = new HashSet<>();
        sellers_list = new HashSet<>();
        // add seller to list of sellers
        sellers_list.addAll(sellers);
        for (Seller seller : sellers) { // add all sellers' products to catalog according to its category
            for (Product product : seller.getProducts()) {
                addProduct(product.getCategory(), product);
            }
        }
    }

    public HashSet<Product> getCatalogType(Category category)
    {
        return switch (category) {
            case BOOKS -> books_catalog;
            case LEARNING_RESOURCES -> learningRes_catalog;
            case STATIONERY -> stationery_catalog;
            case ELECTRONICS -> electronics_catalog;
            case DESKTOP_ACCESSORIES -> desktopAcc_catalog;
        };
    }

    public void addProduct(Category category, Product product)
    {
        getCatalogType(category).add(product);
    }

    public void removeProduct(Category category, Product product)
    {
        getCatalogType(category).remove(product);
    }

    public void displayCatalog()
    {
        System.out.println("Books: \n" + categoryCatalogToString(books_catalog) + "\n");
        System.out.println("Learning Ressources: \n" + categoryCatalogToString(learningRes_catalog) + "\n");
        System.out.println("Stationery: \n" + categoryCatalogToString(stationery_catalog) + "\n");
        System.out.println("Electronics: \n" + categoryCatalogToString(electronics_catalog) + "\n");
        System.out.println("Desktop Accessories: \n" + categoryCatalogToString(desktopAcc_catalog) + "\n");
        displaySellers();
    }

    public void displaySellers()
    {
        //create a method to add sellers in list when sign up
        System.out.println("List of Sellers: \n" + sellersToString() + "\n");
    }

    public Product searchProduct(Product product)
    {
        HashSet<Product> catalog = getCatalogType(product.getCategory());
        if (catalog.contains(product)) {
            for (Product obj : catalog) {
                if (obj.equals(product))
                    return product;
            }
        }
        return null;
    }
    public Seller searchSeller(Seller seller)
    {
        HashSet<Seller> listOfSellers = getSellers_list();
        if (listOfSellers.contains(seller)) {
            for (Seller person : listOfSellers) {
                if (person.equals(seller))
                    return seller;
            }
        }
        return null;
    }

    public Product searchProductByName(String title) {
        for (Product product : books_catalog) {
            if (product.getTitle().equals(title))
                return product;
        }
        for (Product product : learningRes_catalog) {
            if (product.getTitle().equals(title))
                return product;
        }
        for (Product product : stationery_catalog) {
            if (product.getTitle().equals(title))
                return product;
        }
        for (Product product : electronics_catalog) {
            if (product.getTitle().equals(title))
                return product;
        }
        for (Product product : desktopAcc_catalog) {
            if (product.getTitle().equals(title))
                return product;
        }
        return null;
    }

    public Seller searchSellerByName(String id) {
        for (Seller seller : sellers_list) {
            if (seller.getId().equals(id))
                return seller;
        }
        return null;
    }
    public HashSet<Seller> getSellers_list() {
        return sellers_list;
    }
    public void addSellers(Seller seller)
    {
        sellers_list.add(seller);
    }
    public String categoryCatalogToString(HashSet<Product> catalog) {
        StringBuilder result = new StringBuilder();
        for (Product product : catalog) {
            result.append(product.smallToString());
        }
        return result.toString();
    }

    public String sellersToString() {
        StringBuilder result = new StringBuilder();
        for (Seller seller : sellers_list) {
            result.append(seller.getId()).append(": ").append(seller.getCategory()).append("\n");
        }
        return result.toString();
    }
}
