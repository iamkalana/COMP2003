package edu.curtin.app.logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class AppLogger {
    private static final Logger LOGGER = Logger.getLogger(AppLogger.class.getName());

    public void init() {
        // Configuring the Logger
        try (InputStream configFile = new FileInputStream("logging.properties");) {
            LogManager.getLogManager().readConfiguration(configFile);
            if (LOGGER.isLoggable(Level.INFO)) {
                LOGGER.info("AppLogger Initialized.");
            }


        } catch (IOException e) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.warning("Error while logger initializing: " + e.getMessage());
            }
            System.out.println("Error while logger initializing: " + e.getMessage());
        }
    }

}
