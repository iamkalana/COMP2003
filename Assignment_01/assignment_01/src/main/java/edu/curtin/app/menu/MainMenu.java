package edu.curtin.app.menu;

import edu.curtin.app.entity.Directory;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainMenu extends Menu {

    private Directory rootDirectory;
    private static final Logger LOGGER = Logger.getLogger(MainMenu.class.getName());
    private final String[] menuOptions = {
            "\n======================\n======== MENU ========\n======================",
            "1 -> Set Criteria",
            "2 -> Set Output Format",
            "3 -> Report",
            "4 -> Quit"
    };

    private List<String> criteria;
    private String outputFormat = "count";
    private Scanner subScanner = new Scanner(System.in);

    public MainMenu(Directory rootDirectory) {
        this.rootDirectory = rootDirectory;
    }

    @Override
    public void loadRepeatingMenu(Scanner scanner) {
        super.loadRepeatingMenu(scanner);
        subScanner.close();
    }

    @Override
    protected void printMenu() {
        for (String option : menuOptions) {
            System.out.println(option);
        }
    }

    @Override
    protected void selection(int choice) {
        switch (choice) {
            case 1 -> setCriteria();
            case 2 -> setOutputFormat();
            case 3 -> report();
            case 4 -> quit();
            default -> System.out.println("Invalid input");
        }
    }

    @Override
    protected int changeExitValue() {
        return 4;
    } // Changing the menu exit value to 4

    // Get the criteria from the user
    public void setCriteria() {
        criteria = new ArrayList<>();
        try {
            String singleCriteria;
            System.out.println("Enter your criteria:");
            do {
                singleCriteria = subScanner.nextLine();
                if (singleCriteria.isBlank()) {
                    if (criteria.isEmpty()) {
                        System.out.println("No Criteria Selected!");
                    }
                } else {
                    if (validateCriteria(singleCriteria)) {
                        criteria.add(singleCriteria);
                    } else {
                        System.out.println("Invalid Criteria Found!");
                    }
                }
            } while (!singleCriteria.isBlank());
        } catch (InputMismatchException e) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "Invalid Input: " + e.getMessage());
            }
            System.out.println("Invalid Input");
        }
    }

    // Get the output format from the user
    public void setOutputFormat() {
        OutputFormatMenu outputFormatMenu = new OutputFormatMenu();
        outputFormatMenu.loadOneTimeMenu(subScanner);
        outputFormat = outputFormatMenu.getOutputFormat();
    }

    // Printing the matching lines or their count
    public void report() {
        if (criteria == null || criteria.isEmpty()) {
            System.out.println("No criteria selected");
        } else {
            rootDirectory.findMatchingLines(criteria);

            if (outputFormat.equals("show")) {
                rootDirectory.printLines();
            } else {
                rootDirectory.printCount();
            }
        }
    }

    public void quit() {
        System.out.println("\n===== THANK YOU ======\n");
    }

    private boolean validateCriteria(String singleCriteria) {
        String[] splitArr = singleCriteria.split("\\s", 3);
        if (splitArr.length == 3) {
            if (splitArr[0].equals("i") || splitArr[0].equals("e")) {
                if (splitArr[1].equals("t") || splitArr[1].equals("r")) {
                    return true;
                }
            }
        }
        return false;
    }

    public Directory getRootDirectory() {
        return rootDirectory;
    }

    public void setRootDirectory(Directory rootDirectory) {
        this.rootDirectory = rootDirectory;
    }

}
