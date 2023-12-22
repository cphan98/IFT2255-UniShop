import BackEndUtility.Category;
import Users.Seller;
import UtilityObjects.Address;
import productClasses.Inheritances.Hardware;
import productClasses.Inheritances.OfficeEquipment;
import productClasses.Product;
import productClasses.Usages.Cart;
import org.junit.Test;
import static org.junit.Assert.*;

public class CartTest {
    @Test
    public void testGetTotalPrice() {

        Cart cart = new Cart();


        Seller seller = new Seller("Laura", "1234", "jkl@ghi", "2006294850", new Address("1990 Av. Random Road", "Canada", "Quebec", "Montreal", "A1A1A7"), Category.BOOKS);
        Product Laptop = new Hardware("Laptop", "A laptop", 1000.0F, 1000, seller, 47, "MSI Gaming", "YTP-2099", "2017-08-03", "Laptops", "2022-01-01");
        Product Stapler = new OfficeEquipment("Stapler", "A stapler", 15.99F, 15, seller, 189, "Yamaha", "YTP-2098", "Staplers", "2021-02-04");

        cart.addProduct(Laptop, 3);
        cart.addProduct(Stapler, 5);

        float totalPrice = cart.getTotalPrice();

        assertEquals(3079.95f, totalPrice, 0.01);
    }
}
