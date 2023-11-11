package UIs;

import LoginUtility.DataBase;
import Users.User;

public abstract class Menu {
    protected User user;
    protected DataBase database;

    public Menu(User user) {
        this.user = user;
    }
    public abstract boolean displayMenu();
    public abstract boolean displayProfile();
    public abstract boolean displayOrderHistory();
    public abstract boolean displayNotifications();
    public abstract void displayMetrics();
    public abstract void modifyProfile();
    public abstract void modifyPersonalInfos();
    public abstract void modifyPassword();


    protected static void line() {
        System.out.println("--------------------------------------------------");
    }
}
