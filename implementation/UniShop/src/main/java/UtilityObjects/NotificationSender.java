package UtilityObjects;

import Users.Buyer;
import Users.Seller;

/**
 * Class that represents the notification sender, which allows the platform to send notifications to the users.
 * Makes it easier to send notifications to the users
 */
public class NotificationSender {

    /**
     * Method that sends a notification to the buyer
     * @param buyer the buyer that will receive the notification
     * @param title the title of the notification
     * @param summary the summary of the notification
     */
    public void sendBuyerNotification(Buyer buyer, String title, String summary) {
        buyer.addNotification((new Notification(title, summary)));
    }

    /**
     * Method that sends a notification to the seller
     * @param seller the seller that will receive the notification
     * @param title the title of the notification
     * @param summary the summary of the notification
     */
    public void sendSellerNotification(Seller seller, String title, String summary) {
        seller.addNotification(new Notification(title, summary));
    }
}
