package edu.curtin.app.logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class LogInitializer {
    private static final Logger LOGGER = Logger.getLogger(LogInitializer.class.getName());

    public void init() {
        // Configuring the Logger
        try (InputStream configFile = new FileInputStream("logging.properties");) {
            LogManager.getLogManager().readConfiguration(configFile); // read configuration from file
            if (LOGGER.isLoggable(Level.INFO)) {
                LOGGER.info("Logger Initialized.");
            }
        } catch (IOException e) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.warning("Error while log initializing: " + e.getMessage());
            }
        }
    }
}
