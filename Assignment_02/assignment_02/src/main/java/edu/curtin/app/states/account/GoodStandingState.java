package edu.curtin.app.states.account;

import edu.curtin.app.entity.Account;

// implementation of GoodStandingState for the AccountState (State Pattern)
public class GoodStandingState implements AccountState{

    @Override
    public void updateAccount(Account account, Double balance) {
        if (balance > -1000.0 && balance <= 0) {
            account.setState(new DebtState()); // if balance is between -1000 and 0, change state to DebtState
        } else if (balance <= -1000.0) {
            account.setState(new CancelledState()); // if balance is less than -1000, change state to CancelledState
        }
    }
}
