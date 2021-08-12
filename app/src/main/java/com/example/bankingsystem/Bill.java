package com.example.bankingsystem;

public class Bill {
    public Account account;
    public String subscriptionNo;
    public double amount;

    public Bill(Account account, String subscriptionNo, double amount) {
        this.account = account;
        this.subscriptionNo = subscriptionNo;
        this.amount = amount;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getSubscriptionNo() {
        return subscriptionNo;
    }

    public void setSubscriptionNo(String subscriptionNo) {
        this.subscriptionNo = subscriptionNo;
    }

    public double getAmount() { return amount; }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}