README
======

DESCRIPTION

This application is a simple console simulation of public transport system. This system mainly includes 3 types of objects: Passengers, Buses and
Trains. There are two important files ("passenger_schedule.csv" and "transport_schedule.csv") in this application. The application will read these files
and create those objects based on the information in the files. Each transport object runs in its own thread. those threads simulate departure, boarding,
and alighting of passengers. The application will also print the information of each object in the console, based on their status.

You can modify those two csv files to change the simulation. The format of those two files are as follows:

passenger_schedule.csv:
passenger_id,transport_type(b or t),boarding_stop,alighting_stop,account_balance
i.e., p001,b,bs1,bs3,100.00

transport_schedule.csv:
transport_type(b or t),transport_id,route,starting_stop,transport_status(on_time or delayed or cancelled)
i.e., b,b001,bs1->bs2->bs3->bs4,bs1,on_time

REQUIREMENTS

- JDK 11 or later
- Gradle 8.0 or later (optional, but recommended)

HOW TO RUN

- If you have Gradle installed, you can run the application by typing "gradle run" in the terminal.
- If you don't have Gradle installed, you can run the application by typing "gradlew run" in the terminal.
(On Linux based systems, you may need to add "./" to the start of the command; i.e., "./gradlew run" or "./gradlew check".)

HOW TO CHECK PMD RULES

- If you have Gradle installed, you can check the PMD rules by typing "gradle check" in the terminal.
- If you don't have Gradle installed, you can check the PMD rules by typing "gradlew check" in the terminal.
(On Linux based systems, you may need to add "./" to the start of the command; i.e., "./gradlew run" or "./gradlew check".)
