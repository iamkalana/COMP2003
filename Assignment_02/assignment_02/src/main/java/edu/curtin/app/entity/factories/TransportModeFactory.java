package edu.curtin.app.entity.factories;

import edu.curtin.app.states.transport.TransportState;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

// factory class for creating transport modes (Factory Pattern)
public class TransportModeFactory {
    private static final Logger LOGGER = Logger.getLogger(TransportModeFactory.class.getName()); // logger

    // method for creating transport modes
    public TransportMode createTransportMode(String transportType, String transportID, List<String> transportRoute, String currentLocation, TransportState transportState) {
        if (transportType.equalsIgnoreCase("b")) { // if transport type is bus then create bus object

            Bus newBus = new Bus(transportID, transportRoute, currentLocation, transportState);

            if (LOGGER.isLoggable(Level.INFO)) {
                LOGGER.log(Level.INFO, "Bus created: " + transportID); // log bus creation
            }
            return newBus; // return bus object

        } else if (transportType.equalsIgnoreCase("t")) { // if transport type is train then create train object

            Train newTrain = new Train(transportID, transportRoute, currentLocation, transportState);

            if (LOGGER.isLoggable(Level.INFO)) {
                LOGGER.log(Level.INFO, "Train created: " + transportID); // log train creation
            }
            return newTrain; // return train object

        } else { // if transport type is not bus or train then log and return null

            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.log(Level.WARNING, "Invalid transport type: " + transportType); // log invalid transport type
            }
            return null;
        }
    }
}
