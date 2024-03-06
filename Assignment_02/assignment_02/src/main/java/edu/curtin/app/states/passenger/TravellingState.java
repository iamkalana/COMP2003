package edu.curtin.app.states.passenger;

import edu.curtin.app.entity.Passenger;

import java.util.logging.Level;
import java.util.logging.Logger;

// implementation of TravellingState for the PassengerState (State Pattern)
public class TravellingState implements PassengerState{
    private static final Logger LOGGER = Logger.getLogger(TravellingState.class.getName()); // logger
    @Override
    public void updateToWaiting(Passenger passenger) { // ignore the update request as the passenger is already in the 'travelling' state
        if (LOGGER.isLoggable(Level.WARNING)) {
            LOGGER.log(Level.WARNING, "Passenger is already travelling" + passenger.getPassengerID());
        }

    }
    @Override
    public void updateToTravelling(Passenger passenger) { // ignore the update request as the passenger is already in the 'travelling' state
        if (LOGGER.isLoggable(Level.WARNING)) {
            LOGGER.log(Level.WARNING, "Passenger is already travelling" + passenger.getPassengerID());
        }
    }

    @Override
    public void updateToArrived(Passenger passenger) {
        passenger.setPassengerState(new ArrivedState()); // update the travelling state to 'arrived'
    }
}
