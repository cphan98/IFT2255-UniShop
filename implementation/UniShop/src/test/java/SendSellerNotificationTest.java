import BackEndUtility.Category;
import Users.Seller;
import UtilityObjects.Address;
import UtilityObjects.Notification;
import UtilityObjects.NotificationSender;
import org.junit.jupiter.api.Test;

import java.util.Queue;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SendSellerNotificationTest {
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
