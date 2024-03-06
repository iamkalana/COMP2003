package edu.curtin.app.entity;

import java.util.ArrayList;
import java.util.List;

public class CompositeDirectory implements Directory {
    private final String dName;
    private int level;
    private final List<Directory> content = new ArrayList<>();
    private int matchingCount = 0;

    public CompositeDirectory(String dName, int level) {
        this.dName = dName;
        this.level = level;
    }

    // Adding subdirectories and other files
    public void addContent(Directory directory) {
        content.add(directory);
    }

    // Searching matching lines
    @Override
    public void findMatchingLines(List<String> criteria){
        for (Directory directory : content) {
            directory.findMatchingLines(criteria);
        }
    }

    // Returning matching line count
    @Override
    public int getMatchingCount() {
        reset();
        for (Directory directory : content) {
            matchingCount += directory.getMatchingCount();
        }
        return matchingCount;
    }

    // Printing matching line count
    @Override
    public void printCount() {
        System.out.println(genTabSpaces(level) + dName + ": " + getMatchingCount());
        for (Directory directory : content) {
            directory.printCount();
        }
    }

    // Printing matching lines
    @Override
    public void printLines() {
        System.out.println(genTabSpaces(level) + dName + ": ");
        for (Directory directory : content) {
            directory.printLines();
        }
    }

    // Generating indentation
    private static String genTabSpaces(int level) {
        StringBuilder tabSpaces = new StringBuilder();
        for (int i = 0; i < level; i++) {
            tabSpaces.append("\t");
        }
        return tabSpaces.toString();
    }

    // Resetting the matching count
    private void reset() {
        matchingCount = 0;
    }

}
