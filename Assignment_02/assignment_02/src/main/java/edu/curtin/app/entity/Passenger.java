package edu.curtin.app.entity;

import edu.curtin.app.entity.factories.TransportMode;
import edu.curtin.app.observer.PassengerObserver;
import edu.curtin.app.states.passenger.PassengerState;
import edu.curtin.app.states.passenger.WaitingState;

// Passenger class to represent a passenger (implements PassengerObserver interface)
public class Passenger implements PassengerObserver {
    private String passengerID; // passenger ID
    private String transportMode; // passenger's transport mode
    private String source; // passenger's get on location
    private String destination; // passenger's get off location
    private Account account; // passenger's account
    private PassengerState passengerState; // passenger's state (waiting, travelling or arrived)

    public Passenger(String passengerID, String transportMode, String source, String destination, Account account, PassengerState passengerState) {
        this.passengerID = passengerID;
        this.transportMode = transportMode;
        this.source = source;
        this.destination = destination;
        this.account = account;
        this.passengerState = passengerState;
    }

    // implementation of updateLocation method from PassengerObserver interface
    // board or alight passenger based on passenger's state
    @Override
    public void updateLocation(TransportMode transportMode) {
        if (this.getPassengerState().getClass().equals(WaitingState.class)) {
            transportMode.board(this); // if passenger is waiting, board passenger
        } else {
            transportMode.alight(this); // if passenger is travelling, alight passenger
        }
    }

    // method to pay for trip
    public void pay(double amount){
        account.pay(amount);
        System.out.println(getPassengerID() + ": Payment of " + amount + " made");
    }

    // method to top up account
    public void topUp(double amount){
        account.topUp(amount);
        System.out.println(getPassengerID() + ": Deposit of " + amount + " made");
    }

    // getters and setters
    public String getPassengerID() {
        return passengerID;
    }

    public void setPassengerID(String passengerID) {
        this.passengerID = passengerID;
    }

    public String getTransportMode() {
        return transportMode;
    }

    public void setTransportMode(String transportMode) {
        this.transportMode = transportMode;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public PassengerState getPassengerState() {
        return passengerState;
    }

    public void setPassengerState(PassengerState passengerState) {
        this.passengerState = passengerState;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    // method to print account details
    public void printAccountDetails() {
        System.out.println(getPassengerID() + ": Account Balance: " + account.getBalance());
        System.out.println(getPassengerID() + ": Account Status: " + account.getState().getClass().getSimpleName());
    }
}
