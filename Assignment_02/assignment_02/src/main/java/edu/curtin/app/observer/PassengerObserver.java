package edu.curtin.app.observer;

import edu.curtin.app.entity.factories.TransportMode;

// Observer interface for the passenger class (observer pattern)
public interface PassengerObserver {
    void updateLocation(TransportMode transportMode); // triggers when the transport mode notifies its current location
}
