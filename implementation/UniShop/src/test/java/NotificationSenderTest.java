import static org.junit.Assert.*;

import BackEndUtility.Category;
import Users.Buyer;
import Users.Seller;
import UtilityObjects.Address;
import UtilityObjects.Notification;
import UtilityObjects.NotificationSender;
import org.junit.Test;

import java.util.Queue;
public class NotificationSenderTest {
    @Test
    public void testSendBuyerNotification() {
        Buyer buyer = new Buyer("Cynthia","Phan","cphan98", "1234", "ghi@jkl", "2005748362", new Address("1567 Av. Random Road", "Canada", "Quebec", "Montreal", "A1A1A3"));
        NotificationSender notificationSender = new NotificationSender();

        notificationSender.sendBuyerNotification(buyer, "New Promotion", " a new product has a promotion !! ");


        Queue<Notification> buyerNotifications = buyer.getNotifications();
        assertEquals(1, buyerNotifications.size());

        Notification notification = buyerNotifications.poll();
        assertEquals("New Promotion", notification.getTitle());
        assertEquals(" a new product has a promotion !! ", notification.getSummary());

    }
    @Test
    public void testSendSellerNotification() {
        Seller seller = new Seller("Laura", "1234", "jkl@ghi", "2006294850", new Address("1990 Av. Random Road", "Canada", "Quebec", "Montreal", "A1A1A7"), Category.BOOKS);
        NotificationSender notificationSender = new NotificationSender();

        notificationSender.sendSellerNotification(seller, "New problem reported!", "Someone has reported a problem with your product");

        Queue<Notification> sellerNotifications = seller.getNotifications();
        assertEquals(1, sellerNotifications.size());
        Notification notification = sellerNotifications.poll();
        assertEquals("New problem reported!", notification.getTitle());
        assertEquals("Someone has reported a problem with your product", notification.getSummary());
    }
}
