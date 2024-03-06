package edu.curtin.app.states.passenger;

import edu.curtin.app.entity.Passenger;

import java.util.logging.Level;
import java.util.logging.Logger;

// implementation of WaitingState for the PassengerState (State Pattern)
public class WaitingState implements PassengerState{
    private static final Logger LOGGER = Logger.getLogger(WaitingState.class.getName()); // logger
    @Override
    public void updateToWaiting(Passenger passenger) { // ignore the update request as the passenger is already in the 'waiting' state
        if (LOGGER.isLoggable(Level.WARNING)) {
            LOGGER.log(Level.WARNING, "Passenger is already waiting" + passenger.getPassengerID());
        }
    }
    @Override
    public void updateToTravelling(Passenger passenger) {
        passenger.setPassengerState(new TravellingState()); // update the waiting state to 'travelling'
    }

    @Override
    public void updateToArrived(Passenger passenger) { // ignore the update request as the passenger is not in the 'travelling' state
        if (LOGGER.isLoggable(Level.WARNING)) {
            LOGGER.log(Level.WARNING, "Passenger has not started travelling yet" + passenger.getPassengerID());
        }
    }
}
