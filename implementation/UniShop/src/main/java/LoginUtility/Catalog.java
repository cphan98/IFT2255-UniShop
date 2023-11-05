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
    public Catalog()
    {
        this.books_catalog = new HashSet<>();
        this.learningRes_catalog = new HashSet<>();
        this.stationery_catalog = new HashSet<>();
        this.electronics_catalog = new HashSet<>();
        this.desktopAcc_catalog = new HashSet<>();
        sellers_list = new HashSet<>();
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
        System.out.println("Books: " + books_catalog);
        System.out.println("Learning Ressources: " + learningRes_catalog);
        System.out.println("Stationery: " + stationery_catalog);
        System.out.println("Electronics: " + electronics_catalog);
        System.out.println("Desktop Accessories: " + desktopAcc_catalog);
    }

    public void displaySellers()
    {
        //create a method to add sellers in list when sign up
        System.out.println("List of Sellers: " + sellers_list);
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
    public HashSet<Seller> getSellers_list() {
        return sellers_list;
    }
    public void addSellers(Seller seller)
    {
        sellers_list.add(seller);
    }
}
