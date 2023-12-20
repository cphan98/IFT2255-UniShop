
import Users.Buyer;
import UtilityObjects.Address;
import UtilityObjects.Notification;
import UtilityObjects.NotificationSender;
import org.junit.jupiter.api.Test;

import java.util.Queue;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SendBuyerNotificationTest {
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

}
