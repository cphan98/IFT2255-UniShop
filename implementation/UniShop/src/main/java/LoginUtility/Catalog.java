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

    public HashSet<Product> catalogType(Category category)
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
        catalogType(category).add(product);
    }

    public void removeProduct(Category category, Product product)
    {
        catalogType(category).remove(product);
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

    public void addSellers(Seller seller)
    {
        sellers_list.add(seller);
    }

    public void searchProduct(Product product)
    {
    }
}
