package com.example.bankingsystem;

public class Account {
    public User user;
    public String accountId;
    public String accountType;
    public double balance;

    public Account(User user, String accountId, String accountType, double balance) {
        this.user = user;
        this.accountId = accountId;
        this.accountType = accountType;
        this.balance = balance;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
