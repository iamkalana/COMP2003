package edu.curtin.app.entity;

import edu.curtin.app.states.account.AccountState;
import edu.curtin.app.states.account.CancelledState;
import edu.curtin.app.states.account.DebtState;
import edu.curtin.app.states.account.GoodStandingState;

// Account class represents account of passenger
public class Account {
    private Double balance; // account balance
    private Double hold; // amount of money on hold
    private AccountState state; // account state

    public Account(Double balance, AccountState accountState) {
        this.balance = balance;
        this.hold = 0.0;
        this.state = accountState;
    }

    // try to pay amount, return new account state what will be after payment
    public AccountState tryToPay(Double amount) {
        if(this.balance - amount > 0) {
            return new GoodStandingState();
        } else if (this.balance - amount > -1000.0) {
            return new DebtState();
        } else {
            return new CancelledState();
        }
    }

    // pay amount, update account state
    public void pay(Double amount) {
        this.balance -= amount;
        this.state.updateAccount(this, this.balance);
    }

    // top up account, update account state
    public void topUp(Double amount) {
        this.balance += amount;
        this.state.updateAccount(this, this.balance);
    }

    // getters and setters
    public AccountState getState() {
        return state;
    }

    public void setState(AccountState state) {
        this.state = state;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Double getHold() {
        return hold;
    }

    public void setHold(Double hold) {
        this.hold = hold;
    }
}
