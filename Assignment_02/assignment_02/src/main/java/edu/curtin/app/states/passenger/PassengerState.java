package edu.curtin.app.states.passenger;

import edu.curtin.app.entity.Passenger;

// interface for the PassengerState (State Pattern)
public interface PassengerState {
    void updateToWaiting(Passenger passenger); // update the passenger to waiting state
    void updateToTravelling(Passenger passenger); // update the passenger to travelling state
    void updateToArrived(Passenger passenger); // update the passenger to arrived state
}
