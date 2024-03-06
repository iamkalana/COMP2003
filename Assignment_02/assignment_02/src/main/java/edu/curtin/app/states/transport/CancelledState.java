package edu.curtin.app.states.transport;

import edu.curtin.app.entity.factories.TransportMode;

import java.util.logging.Level;
import java.util.logging.Logger;

// implementation of cancelled state for the transport state (state pattern)
public class CancelledState implements TransportState{
    private static final Logger LOGGER = Logger.getLogger(CancelledState.class.getName()); // logger
    @Override
    public void updateToOnTime(TransportMode transportMode) { // ignore the update to on time
        if (LOGGER.isLoggable(Level.WARNING)) {
            LOGGER.log(Level.WARNING, "Transport mode is cancelled. Cannot update to on time."); // log invalid state change
        }
    }

    @Override
    public void updateToDelayed(TransportMode transportMode) { // ignore the update to delayed
        if (LOGGER.isLoggable(Level.WARNING)) {
            LOGGER.log(Level.WARNING, "Transport mode is cancelled. Cannot update to delayed."); // log invalid state change
        }
    }

    @Override
    public void updateToCancelled(TransportMode transportMode) { // ignore the update to cancelled
        if (LOGGER.isLoggable(Level.WARNING)) {
            LOGGER.log(Level.WARNING, "Transport mode is already cancelled."); // log invalid state change
        }
    }
}
