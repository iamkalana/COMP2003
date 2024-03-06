package edu.curtin.app.states.account;

import edu.curtin.app.entity.Account;

// interface for the AccountState (State Pattern)
public interface AccountState {
    void updateAccount(Account account, Double balance); // update the account state based on the balance
}
