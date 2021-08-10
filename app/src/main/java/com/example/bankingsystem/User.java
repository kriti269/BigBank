package com.example.bankingsystem;

public class User {
    public long accessCardNumber;
    public long pinNumber;
    public String name;

    public User(long accessCardNumber, long pinNumber, String name) {
        this.accessCardNumber = accessCardNumber;
        this.pinNumber = pinNumber;
        this.name = name;
    }

    public long getAccessCardNumber() {
        return accessCardNumber;
    }

    public void setAccessCardNumber(long accessCardNumber) {
        this.accessCardNumber = accessCardNumber;
    }

    public long getPinNumber() {
        return pinNumber;
    }

    public void setPinNumber(long pinNumber) {
        this.pinNumber = pinNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
