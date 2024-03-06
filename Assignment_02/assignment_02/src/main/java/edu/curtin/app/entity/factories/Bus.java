package edu.curtin.app.entity.factories;

import edu.curtin.app.entity.Passenger;
import edu.curtin.app.observer.PassengerObserver;
import edu.curtin.app.states.account.DebtState;
import edu.curtin.app.states.account.GoodStandingState;
import edu.curtin.app.states.passenger.TravellingState;
import edu.curtin.app.states.passenger.WaitingState;
import edu.curtin.app.states.transport.CancelledState;
import edu.curtin.app.states.transport.DelayedState;
import edu.curtin.app.states.transport.TransportState;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

// Bus class that implements TransportMode interface representing a bus (factory pattern)
public class Bus implements TransportMode {
    private String busID; // bus ID
    private List<Passenger> passengers; // list of passengers on board
    private List<String> busRoute; // list of locations on bus's route (in order)
    private String currentLocation; // current location of transport
    private TransportState transportState; // transport's state (cancelled, delayed or on time)
    private final Double farePerStop = 50.00; // fare per stop
    private final Integer maxTimeBetweenTwoStops = 3000; // max time to travel between two stops (in milliseconds)
    private List<PassengerObserver> observers; // list of observers (passengers)
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss"); // date time formatter
    private static final Logger LOGGER = Logger.getLogger(Bus.class.getName()); // logger

    public Bus(String transportID, List<String> transportRoute, String currentLocation, TransportState transportState) {
        this.busID = transportID;
        this.busRoute = transportRoute;
        this.currentLocation = currentLocation;
        this.transportState = transportState;
        this.passengers = new ArrayList<>();
        this.observers = new ArrayList<>();
    }

    // bus implementation of departure method
    @Override
    public void depart() {

        if (this.transportState.getClass().equals(CancelledState.class)) { // if bus is cancelled
            System.out.println(this.busID + ": Bus Cancelled");
        } else if (this.transportState.getClass().equals(DelayedState.class)) { // if bus is delayed
            try {
                System.out.println(this.busID + ": Bus Delayed");
                Thread.sleep(2000); // sleep for 2 seconds to simulate delay
                travel(); // start travelling
            } catch (InterruptedException e) {
                LOGGER.log(Level.SEVERE, "Error occurred while trying to simulate bus delay", e);
            }
        } else { // if bus is not cancelled or delayed
            travel(); // start travelling
        }
    }

    // travel method
    private void travel() {
        System.out.println(this.busID + ": Bus Departed");
        int i = 0; // index of current location in the route

        // while there are still stops to travel to in the route
        while (this.busRoute.size() > i) {

            this.currentLocation = this.busRoute.get(i); // set current location

            try {
                Thread.sleep(maxTimeBetweenTwoStops); // sleep to simulate travel time
            } catch (InterruptedException e) {
                LOGGER.log(Level.SEVERE, "Error occurred while trying to simulate bus travel", e);
            }

            // print current location and number of passengers on board
            System.out.println(this.busID + ": Current Location: " + this.currentLocation + " Passengers on board: " + this.passengers.size());

            notifyObservers(); // notify observers (passengers) of current location

            i++; // index set to next stop
        }

        System.out.println(this.busID + ": Bus Arrived to the last stop"); // print bus arrived to last stop
    }

    // bus implementation of passenger boarding method
    @Override
    public void board(Passenger passenger) {

        // if passenger is waiting at the current location and the bus route contains the passenger's destination and the passenger is in waiting state
        if (passenger.getSource().equals(this.currentLocation) && this.busRoute.contains(passenger.getDestination()) && passenger.getPassengerState().getClass().equals(WaitingState.class)) {

            Double payableAmount = calcPayment(passenger); // calculate trip payment
            LocalTime getOnTime = LocalTime.now(); // save time of boarding

            // if passenger has sufficient funds to travel
            if (passenger.getAccount().tryToPay(payableAmount).getClass().equals(GoodStandingState.class)) {
                System.out.println(
                        passenger.getPassengerID() + ": Card swiped: " + dateTimeFormatter.format(getOnTime) + "\n" +
                                passenger.getPassengerID() + ": Has sufficient funds to travel\n" +
                                passenger.getPassengerID() + ": Getting on bus " + this.busID + " at " + this.currentLocation);

                this.passengers.add(passenger); // board passenger
                passenger.getAccount().setHold(payableAmount); // set hold amount
                passenger.getPassengerState().updateToTravelling(passenger); // update passenger state to travelling

            } else if (passenger.getAccount().tryToPay(payableAmount).getClass().equals(DebtState.class)) { // if passenger can pay with debt
                System.out.println(
                        passenger.getPassengerID() + ": Card swiped: " + dateTimeFormatter.format(getOnTime) + "\n" +
                                passenger.getPassengerID() + ": Travelling on debt\n" +
                                passenger.getPassengerID() + ": Getting on bus " + this.busID + " at " + this.currentLocation);

                this.passengers.add(passenger); // board passenger
                passenger.getAccount().setHold(payableAmount); // set hold amount
                passenger.getPassengerState().updateToTravelling(passenger); // update passenger state to travelling

            } else { // if passenger does not have sufficient funds to travel
                System.out.println(
                        passenger.getPassengerID() + ": Card swiped: " + dateTimeFormatter.format(getOnTime) + "\n" +
                                passenger.getPassengerID() + ": Cannot travel due to insufficient funds");
            }
        }
    }

    // bus implementation of passenger alighting method
    @Override
    public void alight(Passenger passenger) {

        // if passenger's destination is the current location and the passenger is on current bus and the passenger is in travelling state
        if (passenger.getDestination().equals(this.currentLocation) && this.passengers.contains(passenger) && passenger.getPassengerState().getClass().equals(TravellingState.class)) {

            LocalTime getOffTime = LocalTime.now(); // save time of alighting
            System.out.println(
                    passenger.getPassengerID() + ": Card swiped: " + dateTimeFormatter.format(getOffTime) + "\n" +
                            passenger.getPassengerID() + ": Getting off bus " + this.busID + " at " + this.currentLocation);

            Double payableAmount = passenger.getAccount().getHold(); // get hold amount
            passenger.pay(payableAmount); // pay for trip
            this.passengers.remove(passenger); // remove passenger from bus
            passenger.printAccountDetails(); // print passenger account details
            passenger.getPassengerState().updateToArrived(passenger); // update passenger state to arrived
        }
    }

    // implementations of addObserver, removeObserver and notifyObservers methods
    @Override
    public void addObserver(PassengerObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(PassengerObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        // notify each observer(passengers) about current location
        for (PassengerObserver observer : observers) {
            observer.updateLocation(this); // trigger updateLocation method in passenger
        }
    }

    // bus implementation of updateTransportState method
    @Override
    public void updateTransportState(TransportState transportState) {
        this.transportState = transportState;
    }

    // calculate trip payment method
    public Double calcPayment(Passenger passenger) {

        String source = passenger.getSource(); // get passenger source
        String destination = passenger.getDestination(); // get passenger destination

        int sourceIndex = this.busRoute.indexOf(source); // get index of source
        int destinationIndex = this.busRoute.indexOf(destination); // get index of destination

        int distance = Math.abs(destinationIndex - sourceIndex); // calculate distance between source and destination

        return distance * farePerStop; // calculate trip amount and return
    }
}
