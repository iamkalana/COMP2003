package edu.curtin.app.find;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FindInclude implements FindStrategy {

    private static final Logger LOGGER = Logger.getLogger(FindInclude.class.getName());

    // Searching for included key
    @Override
    public List<String> find(String type, String key, File file) {
        String line;
        List<String> includedList = new ArrayList<>();
        int lineNo = 1;

        // Read file
        try (FileReader fileReader = new FileReader(file);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            while ((line = bufferedReader.readLine()) != null) {
                if (type.equals("t")) { // searching for the given search text
                    if (line.contains(key)) {
                        includedList.add(lineNo + "\t" + line);
                    }
                } else if (type.equals("r")) {
                    Pattern pattern = Pattern.compile(key);
                    Matcher matcher = pattern.matcher(line);
                    if (matcher.find()) { // searching for the given search regex
                        includedList.add(lineNo + "\t" + line);
                    }
                }
                lineNo++;
            }
            if (LOGGER.isLoggable(Level.INFO)) {
                LOGGER.log(Level.INFO, "File read successfully: " + file.getName() + " find include");
            }
        } catch (IOException e) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.log(Level.WARNING, "Can not read file: " + e.getMessage());
            }
            System.out.println("Something went wrong while file reading!" + e.getMessage());
        }
        return includedList;
    }
}
