package edu.curtin.app.entity.factories;

import edu.curtin.app.entity.Passenger;
import edu.curtin.app.observer.PassengerObserver;
import edu.curtin.app.states.transport.TransportState;

// interface for transport mode factory (factory pattern)
public interface TransportMode {

    // methods for departure, boarding, alighting, updating transport state, adding, removing and notifying observers
    void depart();
    void board(Passenger passenger);
    void alight(Passenger passenger);
    void updateTransportState(TransportState transportState);
    void addObserver(PassengerObserver observer);
    void removeObserver(PassengerObserver observer);
    void notifyObservers();

}
