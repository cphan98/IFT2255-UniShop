package UtilityObjects;

import Users.Buyer;
import Users.Seller;

public class NotificationSender {
    public void sendBuyerNotification(Buyer buyer, String title, String summary) {
        buyer.addNotification((new Notification(title, summary)));
    }

    public void sendSellerNotification(Seller seller, String title, String summary) {
        seller.addNotification(new Notification(title, summary));
    }
}
