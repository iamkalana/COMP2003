package edu.curtin.app.menu;

import java.util.Scanner;

public class OutputFormatMenu extends Menu {

    private String outputFormat = "count";
    private final String[] menuOptions = {
            "\nSelect the output format: ",
            "1 -> Count",
            "2 -> Show",
            "3 -> Back"
    };

    @Override
    public void loadOneTimeMenu(Scanner scanner) {
        super.loadOneTimeMenu(scanner);
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
            case 1 -> setToCount();
            case 2 -> setToShow();
            case 3 -> back();
            default -> System.out.println("Invalid input\nDefault output format is count");
        }
    }

    @Override
    protected int changeExitValue() {
        return 3;
    } // Changing the menu exit value to 3

    // Set output format to count
    public void setToCount() {
        outputFormat = "count";
        System.out.println("Output format set to count");
    }

    // Set output format to show
    public void setToShow() {
        outputFormat = "show";
        System.out.println("Output format set to show");
    }

    public void back() {
        System.out.println("Default output format is count");
    }

    // Return the output format
    public String getOutputFormat() {
        return outputFormat;
    }
}
