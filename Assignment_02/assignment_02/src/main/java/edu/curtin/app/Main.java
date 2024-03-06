package edu.curtin.app;

import edu.curtin.app.entity.Account;
import edu.curtin.app.entity.Passenger;
import edu.curtin.app.entity.factories.TransportMode;
import edu.curtin.app.entity.factories.TransportModeFactory;
import edu.curtin.app.logger.LogInitializer;
import edu.curtin.app.states.account.AccountState;
import edu.curtin.app.states.account.CancelledState;
import edu.curtin.app.states.account.DebtState;
import edu.curtin.app.states.account.GoodStandingState;
import edu.curtin.app.states.passenger.WaitingState;
import edu.curtin.app.states.transport.DelayedState;
import edu.curtin.app.states.transport.OnTimeState;
import edu.curtin.app.states.transport.TransportState;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    private static final Logger LOGGER = Logger.getLogger(Main.class.getName()); // logger
    private static final LogInitializer LOG_INITIALIZER = new LogInitializer(); // logger initializer

    // entry point method
    public void start() {

        LOG_INITIALIZER.init(); // initialize logger
        List<TransportMode> modes = readTransportModeFile(); // read transport mode file, create transport modes and store in list
        List<Passenger> passengers = readPassengerFile(); // read passenger file, create passengers and store in list

        // create thread pool and execute each transport mode in a separate thread
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        for (TransportMode tm : modes) {
            // add passengers as observers to each transport mode
            for (Passenger p : passengers) {
                tm.addObserver(p);
            }
            executorService.execute(new RunnableImpl(tm));  // execute transport mode in a separate thread
        }

        executorService.shutdown(); // shutdown thread pool
    }

    // Runnable implementation to execute transport mode in a separate thread
    private class RunnableImpl implements Runnable {

        private TransportMode transportMode;

        public RunnableImpl(TransportMode transportMode) {
            this.transportMode = transportMode;
        }

        @Override
        public void run() {
            transportMode.depart();
        }
    }

    // read transport mode file and create transport modes
    private static List<TransportMode> readTransportModeFile() {
        List<TransportMode> modes = new ArrayList<>(); // list to store transport modes

        try (Scanner scanner = new Scanner(new File("transport_schedule.csv"))) { // read file
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] data = line.split(","); // split line by comma
                String[] route = data[2].split("->"); // split route by arrow

                TransportState initialTransportState; // initial transport state

                if (data[4].equalsIgnoreCase("on_time")) {
                    initialTransportState = new OnTimeState(); // if transport state is on time then return on time state
                } else if (data[4].equalsIgnoreCase("delayed")) {
                    initialTransportState = new DelayedState(); // if transport state is delayed then return delayed state
                } else {
                    initialTransportState = new edu.curtin.app.states.transport.CancelledState(); // if transport state is cancelled then return cancelled state
                }

                // create transport mode using factory
                TransportMode newTransportMode = new TransportModeFactory().createTransportMode(
                        data[0], // transport mode
                        data[1], // transportID
                        Arrays.asList(route), // route
                        data[3], // start location
                        initialTransportState); // initial transport state (dependency injection)

                if (newTransportMode != null) { // if created transport mode is not null, add to list
                    modes.add(newTransportMode);
                }
            }
            LOGGER.log(Level.INFO, "Transport mode file read successfully");
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.SEVERE, "File not found", e);
            System.out.println("File not found");
        }
        return modes; // return list of transport modes
    }

    // read passenger file and create passengers
    private static List<Passenger> readPassengerFile() {
        List<Passenger> passengers = new ArrayList<>(); // list to store passengers

        try (Scanner scanner = new Scanner(new File("passenger_schedule.csv"))) { // read file
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] data = line.split(","); // split line by comma

                Double accountBalance = Double.valueOf(data[4]); // get account balance
                AccountState newAccountState;

                // create account state based on balance
                if (accountBalance > 0) {
                    newAccountState = new GoodStandingState();
                } else if (accountBalance <= -1000.0) {
                    newAccountState = new CancelledState();
                } else {
                    newAccountState = new DebtState();
                }

                // create passenger
                Passenger newPassenger = new Passenger(
                        data[0], // passengerID
                        data[1], // transport mode
                        data[2], // source location
                        data[3], // destination location
                        new Account(accountBalance, newAccountState), // dependency injection (account)
                        new WaitingState()); // dependency injection (state)

                passengers.add(newPassenger); // add passenger to list
            }
            LOGGER.log(Level.INFO, "Passenger file read successfully");
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.SEVERE, "File not found", e);
            System.out.println("File not found");
        }
        return passengers; // return list of passengers
    }
}
