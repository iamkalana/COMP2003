package edu.curtin.app;

import edu.curtin.app.entity.CompositeDirectory;
import edu.curtin.app.entity.Directory;
import edu.curtin.app.entity.SingleFile;
import edu.curtin.app.logger.AppLogger;
import edu.curtin.app.menu.MainMenu;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private String path;
    private Directory rootDirectory;
    private Scanner scanner = new Scanner(System.in);
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
    private static final AppLogger APP_LOGGER = new AppLogger();

    public Main(String path) {
        this.path = path;
        this.createRootDirectory();
    }

    public void run() {
        APP_LOGGER.init(); // Create logger
        if (rootDirectory != null) {
            MainMenu mainMenu = new MainMenu(rootDirectory);
            mainMenu.loadRepeatingMenu(scanner);
            scanner.close();
        }
    }

    // Initializing the root directory
    private void createRootDirectory() {
        try {
            File file = new File(path);
            if (file.exists()) {
                rootDirectory = new CompositeDirectory(file.getName(), 0);
                traverse(file, (CompositeDirectory) rootDirectory, 0);
            } else {
                throw new FileNotFoundException();
            }
        } catch (FileNotFoundException e) {
            System.out.println("Given Directory path does not exists!");
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.log(Level.WARNING, "Directory does not exists: " + e.getMessage());
            }
        }
    }

    // Traversing through the root directory
    private void traverse(File file, CompositeDirectory directory, int level) {
        if (file.isFile()) {
            String sfName = file.getName();
            SingleFile singleFile = new SingleFile(sfName, file, level);
            directory.addContent(singleFile);

        } else {
            String dName = file.getName();
            File[] files = file.listFiles();

            if (level == 0) {
                for (File file1 : files) {
                    traverse(file1, directory, level + 1);
                }
            } else {
                CompositeDirectory compositeDirectory = new CompositeDirectory(dName, level);
                directory.addContent(compositeDirectory);

                for (File file1 : files) {
                    traverse(file1, compositeDirectory, level + 1);
                }
            }
        }
    }
}
