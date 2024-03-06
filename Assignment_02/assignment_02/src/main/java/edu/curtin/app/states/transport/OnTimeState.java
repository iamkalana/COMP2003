package edu.curtin.app.states.transport;

import edu.curtin.app.entity.factories.TransportMode;

import java.util.logging.Level;
import java.util.logging.Logger;

// implementation of OnTimeState for the transport state (state pattern)
public class OnTimeState implements TransportState{
    private static final Logger LOGGER = Logger.getLogger(OnTimeState.class.getName()); // logger
    @Override
    public void updateToOnTime(TransportMode transportMode) { // ignore the update to on time
        if (LOGGER.isLoggable(Level.WARNING)) {
            LOGGER.log(Level.WARNING, "Transport is already on time"); // log that the transport is already on time
        }
    }

    @Override
    public void updateToDelayed(TransportMode transportMode) { // update the state to delayed
        transportMode.updateTransportState(new DelayedState());
    }

    @Override
    public void updateToCancelled(TransportMode transportMode) { // update the state to cancelled
        transportMode.updateTransportState(new CancelledState());
    }
}
