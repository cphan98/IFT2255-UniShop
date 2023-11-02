package UIs;

import Users.User;

public abstract class Menu {
    protected User user;
    public Menu(User user) {
        this.user = user;
    }
    public abstract boolean displayMenu();
    public abstract boolean displayProfile();
    public abstract boolean displayOrderHistory();
    public abstract boolean displayNotifications();
    public abstract void displayMetrics();
}
