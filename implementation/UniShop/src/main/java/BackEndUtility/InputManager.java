package BackEndUtility;

import java.util.Scanner;

public class InputManager implements java.io.Serializable {

    // ATTRIBUTES

    private static InputManager instance = null;
    private Scanner scanner;

    // CONSTRUCTOR

    private InputManager() {
        scanner = new Scanner(System.in);
    }

    public static InputManager getInstance() {
        if (instance == null) {
            instance = new InputManager();
        }
        return instance;
    }

    // UTILITIES

    public String nextLine() {
        String nextLine = scanner.nextLine();
        while (nextLine.isEmpty()) {
            System.out.println("Please enter a non-empty input");
            nextLine = scanner.nextLine();
        }
        return nextLine;
    }

    // Add other input methods as needed
}

