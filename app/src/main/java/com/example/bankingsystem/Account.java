package com.example.bankingsystem;

import java.util.ArrayList;
import java.util.List;

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

//class for account related operations
class AccountOperations
{
    public static String[] getAccountsNames(List<Account> accounts) {
        int arraySize = accounts.size();
        String[] accountNames = new String[arraySize+1];
        //add default option
        accountNames[0] = "Select Account Type";
        for (int i=1; i<=arraySize; i++) {
            accountNames[i] = accounts.get(i-1).getAccountType();
        }
        return accountNames;
    }

    public static Account getAccount(String accountType){
        for(Account account: MainActivity.userAccounts){
            if(accountType==account.getAccountType())
                return account;
        }
        return null;
    }

    public static String withdrawAmount(String amountString, String AccountType) {
        double amount = Double.parseDouble(amountString);
        Account account = getAccount(AccountType);
        if(account == null) {
            return "Unable to find Account information";
        }
        if(account == null || account.getBalance() < amount){
            return "Balance too low!";
        }
        else{
            account.setBalance(account.getBalance()-amount);
        }
        return "";
    }

    public static String depositAmount(String amountString, String accountType) {
        double amount = Double.parseDouble(amountString);
        Account accountTo = getAccount(accountType);
        if(accountTo == null) {
            return "Unable to find Account information";
        } else {
            accountTo.setBalance(accountTo.getBalance() + amount);
        }
        return "";
    }
}
