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

// Train class that implements TransportMode interface representing a train (factory pattern)
public class Train implements TransportMode {
    private String trainID; // train ID
    private List<Passenger> passengers; // list of passengers on board
    private List<String> trainRoute; // list of locations on train's route (in order)
    private String currentLocation; // current location of transport
    private TransportState transportState; // transport's state (cancelled, delayed or on time)
    private final Double farePerStop = 20.00; // fare per stop
    private final Integer maxTimeBetweenTwoStops = 5000; // max time to travel between two stops (in milliseconds)
    private List<PassengerObserver> observers; // list of observers (passengers)
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss"); // date time formatter
    private static final Logger LOGGER = Logger.getLogger(Train.class.getName()); // logger

    public Train(String transportID, List<String> transportRoute, String currentLocation, TransportState transportState) {
        this.trainID = transportID;
        this.trainRoute = transportRoute;
        this.currentLocation = currentLocation;
        this.transportState = transportState;
        this.passengers = new ArrayList<>();
        this.observers = new ArrayList<>();
    }

    // train implementation of departure method
    @Override
    public void depart() {

        if (this.transportState.getClass().equals(CancelledState.class)) { // if train is cancelled
            System.out.println(this.trainID + ": Train Cancelled");
        } else if (this.transportState.getClass().equals(DelayedState.class)) { // if train is delayed
            try {
                System.out.println(this.trainID + ": Train Delayed");
                Thread.sleep(2000); // sleep for 2 seconds to simulate delay
                travel(); // start travelling
            } catch (InterruptedException e) {
                LOGGER.log(Level.SEVERE, "Error occurred while trying to simulate train delay", e);
            }
        } else { // if train is not cancelled or delayed
            travel(); // start travelling
        }
    }

    // travel method
    private void travel() {
        System.out.println(this.trainID + ": Train Departed");
        int i = 0; // index of current location in transport route

        // while there are still stops in the transport route
        while (this.trainRoute.size() > i) {

            this.currentLocation = this.trainRoute.get(i); // set current location

            try {
                Thread.sleep(maxTimeBetweenTwoStops); // sleep to simulate travel
            } catch (InterruptedException e) {
                LOGGER.log(Level.SEVERE, "Error occurred while trying to simulate train travel", e);
            }

            // print current location and number of passengers on board
            System.out.println(this.trainID + ": Current Location: " + this.currentLocation + " Passengers on board: " + this.passengers.size());

            notifyObservers(); // notify observers (passengers)

            i++; // index set to next stop
        }
        System.out.println(this.trainID + ": Train Arrived to the last station"); // print train arrived to last station
    }

    // train implementation of passenger boarding method
    @Override
    public void board(Passenger passenger) {

        // if passenger is waiting at the current location and the transport route contains the passenger's destination and the passenger is in waiting state
        if (passenger.getSource().equals(this.currentLocation) && this.trainRoute.contains(passenger.getDestination()) && passenger.getPassengerState().getClass().equals(WaitingState.class)) {

            Double payableAmount = calcPayment(passenger); // calculate trip amount
            LocalTime getOnTime = LocalTime.now(); // save time of boarding

            if (passenger.getAccount().tryToPay(payableAmount).getClass().equals(GoodStandingState.class)) { // if passenger has sufficient funds to travel
                System.out.println(
                        passenger.getPassengerID() + ": Card swiped: " + dateTimeFormatter.format(getOnTime) + "\n" +
                                passenger.getPassengerID() + ": Has sufficient funds to travel\n" +
                                passenger.getPassengerID() + ": Getting on train " + this.trainID + " at " + this.currentLocation);

                this.passengers.add(passenger); // add passenger to list of passengers on board
                passenger.getAccount().setHold(payableAmount); // set hold amount
                passenger.getPassengerState().updateToTravelling(passenger); // update passenger state to travelling

            } else if (passenger.getAccount().tryToPay(payableAmount).getClass().equals(DebtState.class)) { // if passenger can travel on debt
                System.out.println(
                        passenger.getPassengerID() + ": Card swiped: " + dateTimeFormatter.format(getOnTime) + "\n" +
                                passenger.getPassengerID() + ": Travelling on debt\n" +
                                passenger.getPassengerID() + ": Getting on train " + this.trainID + " at " + this.currentLocation);

                this.passengers.add(passenger); // add passenger to list of passengers on board
                passenger.getAccount().setHold(payableAmount); // set hold amount
                passenger.getPassengerState().updateToTravelling(passenger); // update passenger state to travelling

            } else { // if passenger does not have sufficient funds to travel
                System.out.println(
                        passenger.getPassengerID() + ": Card swiped: " + dateTimeFormatter.format(getOnTime) + "\n" +
                                passenger.getPassengerID() + ": Cannot travel due to insufficient funds");
            }
        }
    }

    // train implementation of passenger alighting method
    @Override
    public void alight(Passenger passenger) {

        // if passenger's destination is the current location and the passenger is on board and the passenger is in travelling state
        if (passenger.getDestination().equals(this.currentLocation) && this.passengers.contains(passenger) && passenger.getPassengerState().getClass().equals(TravellingState.class)) {

            LocalTime getOffTime = LocalTime.now(); // save time of alighting
            System.out.println(
                    passenger.getPassengerID() + ": Card swiped: " + dateTimeFormatter.format(getOffTime) + "\n" +
                            passenger.getPassengerID() + ": Getting off train " + this.trainID + " at " + this.currentLocation);

            Double payableAmount = passenger.getAccount().getHold(); // get hold amount
            passenger.pay(payableAmount); // pay for trip
            this.passengers.remove(passenger); // remove passenger from list of passengers on board
            passenger.printAccountDetails(); // print account details
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

    // method to calculate trip amount
    public Double calcPayment(Passenger passenger) {

        String source = passenger.getSource(); // get passenger's source
        String destination = passenger.getDestination(); // get passenger's destination

        int sourceIndex = this.trainRoute.indexOf(source); // get index of source in transport route
        int destinationIndex = this.trainRoute.indexOf(destination); // get index of destination in transport route

        int distance = Math.abs(destinationIndex - sourceIndex); // calculate distance between source and destination

        return distance * farePerStop; // calculate trip amount and return
    }
}
