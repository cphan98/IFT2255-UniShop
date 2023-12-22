package UtilityObjects;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Class representing a notification a user gets. A notification includes the title, summary of the notification, date
 * the notification was sent, and a boolean determining if the notification has been read.
 *
 * This class implements java.io.Serializable.
 */
public class Notification implements java.io.Serializable {

    // ATTRIBUTES

    private String title;
    private String summary;
    private String date;
    private boolean read;

    // CONSTRUCTOR

    /**
     * Constructs an instance of Notification with given title and summary.
     *
     * @param title     String, title of the notification
     * @param summary   String, summary of the notification
     */
    public Notification(String title, String summary) {
        this.title = title;
        this.summary = summary;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        this.date = formatter.format(LocalDateTime.now());
        this.read = false;
    }

    // GETTERS

    /**
     * Returns the notification's title.
     *
     * @return  String, title
     */
    public String getTitle() { return title; }

    /**
     * Returns the notification's summary.
     *
     * @return  String, summary
     */
    public String getSummary() { return summary; }

    /**
     * Returns the date the notification was sent.
     *
     * @return  String, date notification was sent
     */
    public String getDate() { return date; }

    // SETTERS

    /**
     * Sets the notification's title.
     *
     * @param title String, title
     */
    public void setTitle(String title) { this.title = title; }

    /**
     * Sets the notification's summary.
     *
     * @param summary   String, summary
     */
    public void setSummary(String summary) { this.summary = summary; }

    /**
     * Sets the date the notification was sent.
     *
     * @param date  String, date notification was sent
     */
    public void setDate(String date) { this.date = date; }

    /**
     * Sets the true if the user has read the notification.
     *
     * @param read  Boolean, true if the user has read the notification
     */
    public void setRead(boolean read) { this.read = read; }

    /**
     * Returns true if the user has read the notification.
     *
     * @return  Boolean, true if the notification was read by the user
     */
    public boolean isRead() { return read; }

    // UTILITIES

    // to string ------------------------------------------------------------------------------------------------------

    /**
     * Returns a string of the notification, including the title, summary and the date the notification was sent. The
     * string will include '(new)' if the user has not read the notification yet.
     *
     * @return  String, notification containing the title, summary, date the notification was sent, and '(new)' if the
     *          notification was not read yet
     */
    public String toString() {
        return "Title: " + title + "\nSummary: " + summary + "\nDate: " + date + (!read ? "\t\t(new)" : "");
    }
}
