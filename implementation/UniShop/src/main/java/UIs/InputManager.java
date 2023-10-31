package UIs;

import java.util.Scanner;

public class InputManager {
    private static InputManager instance = null;
    private Scanner scanner;

    private InputManager() {
        scanner = new Scanner(System.in);
    }

    public static InputManager getInstance() {
        if (instance == null) {
            instance = new InputManager();
        }
        return instance;
    }

    public String nextLine() {
        return scanner.nextLine();
    }

    public int nextInt() {
        return scanner.nextInt();
    }

    // Add other input methods as needed
}

