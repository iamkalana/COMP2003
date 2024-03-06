package edu.curtin.app.menu;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Menu {

    private static final Logger LOGGER = Logger.getLogger(Menu.class.getName());

    // Load the repeating menu
    public void loadRepeatingMenu(Scanner scanner) {
        int exitValue = changeExitValue();
        int choice = -1;
        do {
            choice = getChoice(scanner, choice);
        } while (choice != exitValue);
    }

    // Load the one time menu
    public void loadOneTimeMenu(Scanner scanner) {
        int choice = -1;
        do {
            choice = getChoice(scanner, choice);
        } while (choice == -1);
    }

    // Get the user input
    private int getChoice(Scanner scanner, int choice) {
        try {
            printMenu();
            System.out.println("Enter your choice: ");
            choice = scanner.nextInt();
            selection(choice);

        } catch (InputMismatchException e) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "Invalid Input: " + e.getMessage());
            }
            System.out.println("Invalid Input");
            scanner.nextLine();
        }
        return choice;
    }

    protected abstract void printMenu();

    protected abstract void selection(int choice);

    protected abstract int changeExitValue();

}
