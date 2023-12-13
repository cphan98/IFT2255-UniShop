package UtilityObjects;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Notification implements java.io.Serializable {

    // ATTRIBUTES

    private String title;
    private String summary;
    private String date;
    private boolean read;

    // CONSTRUCTOR

    public Notification(String title, String summary) {
        this.title = title;
        this.summary = summary;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        this.date = formatter.format(LocalDateTime.now());
        this.read = false;
    }

    // GETTERS

    public String getTitle() { return title; }
    public String getSummary() { return summary; }
    public String getDate() { return date; }

    // SETTERS

    public void setTitle(String title) { this.title = title; }
    public void setSummary(String summary) { this.summary = summary; }
    public void setDate(String date) { this.date = date; }
    public void setRead(boolean read) { this.read = read; }
    public boolean isRead() { return read; }

    // UTILITIES

    // to string ------------------------------------------------------------------------------------------------------

    public String toString() {
        return "Title: " + title + "\nSummary: " + summary + "\nDate: " + date + (!read ? "\t\t(new)" : "");
    }
}
