package edu.curtin.app.states.account;

import edu.curtin.app.entity.Account;

// implementation of CancelledState for the AccountState (State Pattern)
public class CancelledState implements AccountState{

    @Override
    public void updateAccount(Account account, Double balance) {
        if (balance > 0) {
            account.setState(new GoodStandingState()); // if balance is greater than 0, change state to GoodStandingState
        } else if (balance > -1000.0) {
            account.setState(new DebtState()); // if balance is greater than -1000, change state to DebtState
        }
    }
}
