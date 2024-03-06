package edu.curtin.app.states.transport;

import edu.curtin.app.entity.factories.TransportMode;

import java.util.logging.Level;
import java.util.logging.Logger;

// implementation of DelayedState for the transport state (state pattern)
public class DelayedState implements TransportState{
    private static final Logger LOGGER = Logger.getLogger(DelayedState.class.getName()); // logger
    @Override
    public void updateToOnTime(TransportMode transportMode) { // update the state to on time
        transportMode.updateTransportState(new OnTimeState());
    }

    @Override
    public void updateToDelayed(TransportMode transportMode) { // ignore the update to delayed
        if (LOGGER.isLoggable(Level.WARNING)) {
            LOGGER.log(Level.WARNING, "Transport is already delayed"); // log that the transport is already delayed
        }
    }

    @Override
    public void updateToCancelled(TransportMode transportMode) { // update the state to cancelled
        transportMode.updateTransportState(new CancelledState());
    }
}
