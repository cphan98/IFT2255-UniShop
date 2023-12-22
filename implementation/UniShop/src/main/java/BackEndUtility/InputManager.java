package BackEndUtility;

import java.util.Scanner;

/**
 * Gets a user's input in the command line.
 *
 * This class is used multiple times in the project to avoid repetitive instantiation of the object Scanner.
 */
public class InputManager implements java.io.Serializable {

    // ATTRIBUTES

    private static InputManager instance = null;
    private Scanner scanner;

    // CONSTRUCTOR

    private InputManager() {
        scanner = new Scanner(System.in);
    }

    /**
     * Returns an instance of InputManager. If the instance is null, getInstance will create a new instance of InputManger.
     *
     * @return  instance of InputManager
     */
    public static InputManager getInstance() {
        if (instance == null) {
            instance = new InputManager();
        }
        return instance;
    }

    // UTILITIES

    /**
     * Returns the user's input when it is not empty. Otherwise, nextLine will ask the user to enter a non-empty input.
     *
     * @return  String, the user's input in the command line
     */
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

