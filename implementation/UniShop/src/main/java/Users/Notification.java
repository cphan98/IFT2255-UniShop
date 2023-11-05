package Users;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Notification {
    // attributes
    private String title;
    private String summary;
    private String date;

    // getters
    public String getTitle() { return title; }
    public String getSummary() { return summary; }
    public String getDate() { return date; }

    // setters
    public void setTitle(String title) { this.title = title; }
    public void setSummary(String summary) { this.summary = summary; }
    public void setDate(String date) { this.date = date; }

    // constructor
    public Notification(String title, String summary) {
        this.title = title;
        this.summary = summary;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        this.date = formatter.format(LocalDateTime.now());
    }
}
