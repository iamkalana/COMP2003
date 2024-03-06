package edu.curtin.app.states.account;

import edu.curtin.app.entity.Account;

// implementation of DebtState for the AccountState (State Pattern)
public class DebtState implements AccountState{

    @Override
    public void updateAccount(Account account, Double balance) {
        if (balance > 0) {
            account.setState(new GoodStandingState()); // if balance is positive, change state to GoodStandingState
        } else if (balance <= -1000.0) {
            account.setState(new CancelledState()); // if balance is less than or equal to -1000, change state to CancelledState
        }
    }
}
