package edu.curtin.app.states.passenger;

import edu.curtin.app.entity.Passenger;

import java.util.logging.Level;
import java.util.logging.Logger;

// implementation of ArrivedState for the PassengerState (State Pattern)
public class ArrivedState implements PassengerState{
    private static final Logger LOGGER = Logger.getLogger(ArrivedState.class.getName()); // logger

    @Override
    public void updateToWaiting(Passenger passenger) {
        passenger.setPassengerState(new WaitingState()); // update the arrived state to 'waiting'
    }

    @Override
    public void updateToTravelling(Passenger passenger) { // ignore the update request as the passenger is already in the 'arrived' state
        if (LOGGER.isLoggable(Level.WARNING)) {
            LOGGER.log(Level.WARNING, "Passenger has already arrived");
        }
    }

    @Override
    public void updateToArrived(Passenger passenger) { // ignore the update request as the passenger is already in the 'arrived' state
        if (LOGGER.isLoggable(Level.WARNING)) {
            LOGGER.log(Level.WARNING, "Passenger has already arrived");
        }
    }
}
