package edu.curtin.app.states.transport;

import edu.curtin.app.entity.factories.TransportMode;

// interface for the transport state (state pattern)
public interface TransportState {
    void updateToOnTime(TransportMode transportMode); // update the state to on time
    void updateToDelayed(TransportMode transportMode); // update the state to delayed
    void updateToCancelled(TransportMode transportMode); // update the state to cancelled
}
