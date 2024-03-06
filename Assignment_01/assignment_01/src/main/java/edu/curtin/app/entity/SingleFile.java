package edu.curtin.app.entity;

import edu.curtin.app.find.FindExclude;
import edu.curtin.app.find.FindInclude;
import edu.curtin.app.find.FindStrategy;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class SingleFile implements Directory {

    private final String sfName;
    private final File file;
    private int level;
    private int matchingCount = 0;
    private List<String> matchingLines = new ArrayList<>();

    public SingleFile(String sfName, File file, int level) {
        this.sfName = sfName;
        this.file = file;
        this.level = level;
    }

    // Returning matching line count
    @Override
    public int getMatchingCount() {
        return matchingCount;
    }

    // Searching matching lines
    @Override
    public void findMatchingLines(List<String> criteria){
        List<String> includeList = new ArrayList<>();
        List<String> excludeList = new ArrayList<>();
        List<String> matchingList;

        for (String searchWord : criteria) {
            String[] splitArr = searchWord.split("\\s", 3);
            if (splitArr[0].equals("i")) {
                FindStrategy findStrategy = new FindInclude();
                List<String> foundList = findStrategy.find(splitArr[1], splitArr[2], file);
                for (String line : foundList) {
                    if (!includeList.contains(line)) {
                        includeList.add(line);
                    }
                }
            } else if (splitArr[0].equals("e")) {
                FindStrategy findStrategy = new FindExclude();
                List<String> foundList = findStrategy.find(splitArr[1], splitArr[2], file);
                for (String line : foundList) {
                    if (!excludeList.contains(line)) {
                        excludeList.add(line);
                    }
                }
            }
        }

        if (includeList.isEmpty()) {
            matchingList = excludeList;
        } else if (excludeList.isEmpty()) {
            matchingList = includeList;
        } else {
            matchingList = includeList.stream().filter(excludeList::contains).collect(toList());
        }

        reset();

        matchingCount = matchingList.size();
        matchingLines = matchingList;

    }

    // Printing matching line count
    @Override
    public void printCount() {
        System.out.println(genTabSpaces(level) + sfName + ": " + getMatchingCount());
    }

    // Printing matching lines
    @Override
    public void printLines() {
        System.out.println(genTabSpaces(level) + sfName);
        for (String line : matchingLines) {
            System.out.println(genTabSpaces(level + 1) + line);
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

    private void reset() {
        matchingCount = 0;
        matchingLines = new ArrayList<>();
    }

}
