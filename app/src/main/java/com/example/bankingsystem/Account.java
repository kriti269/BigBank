package com.example.bankingsystem;

import java.util.ArrayList;
import java.util.List;

// POJO Class to store account information
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

//Class for account related operations
class AccountOperations
{
    // Returns account types from list of all accounts
    public static String[] getAccountsNames(List<Account> accounts) {
        int arraySize = accounts.size();
        String[] accountNames = new String[arraySize+1];
        //add default option
        accountNames[0] = "Select Account";
        for (int i=1; i<=arraySize; i++) {
            accountNames[i] = accounts.get(i-1).getAccountType();
        }
        return accountNames;
    }

    // Returns account information from selected account type
    public static Account getAccount(String accountType){
        for(Account account: MainActivity.userAccounts){
            if(accountType.equalsIgnoreCase(account.getAccountType()))
                return account;
        }
        return null;
    }

    // Returns status string after deducting amount from the provided account type
    public static String withdrawAmount(String amountString, String AccountType) {
        double amount = Double.parseDouble(amountString);
        Account account = getAccount(AccountType);
        if(account == null) {
            return "Please select valid account for withdrawal";
        }
        if(account == null || account.getBalance() < amount){
            return "Balance too low!";
        }
        else{
            account.setBalance(account.getBalance()-amount);
        }
        return "";
    }

    // Returns status after adding amount to the provided account type
    public static String depositAmount(String amountString, String accountType) {
        double amount = Double.parseDouble(amountString);
        Account account = getAccount(accountType);
        if(account == null) {
            return "Please select valid account to deposit";
        } else {
            account.setBalance(account.getBalance() + amount);
        }
        return "";
    }

    // Returns true if receiver name is valid and different from logged in user
    public static boolean checkUser(String name){
        for(Account account: MainActivity.accountsList){
            if(!MainActivity.loggedInUser.getName().equalsIgnoreCase(name)
                    && name.equalsIgnoreCase(account.getUser().getName()))
                return true;
        }
        return false;
    }

    // Returns true if receiver account is valid and different from logged in user
    public static boolean checkAccount(String accountNumber){
        for(Account account: MainActivity.accountsList){
            if(!MainActivity.loggedInUser.getName().equalsIgnoreCase(account.getUser().getName())
                    && accountNumber.equalsIgnoreCase(account.getAccountId()))
                return true;
        }
        return false;
    }

    // Returns true if receiver name and account is valid and different from logged in user
    public static boolean checkUserAccount(String user, String accountId){
        for(Account account: MainActivity.accountsList){
            if(!MainActivity.loggedInUser.getName().equalsIgnoreCase(account.getUser().getName())
                    && accountId.equalsIgnoreCase(account.getAccountId()) && user.equalsIgnoreCase(account.getUser().getName()))
                return true;
        }
        return false;
    }

    // Returns status string after depositing amount to receiver's account
    public static String depositUserAmount(String amountString, String accountId) {
        double amount = Double.parseDouble(amountString);
        Account account = getUserAccount(accountId);
        if(account == null) {
            return "Please select valid account to deposit";
        } else {
            account.setBalance(account.getBalance() + amount);
        }
        return "";
    }

    // Returns account information from an account id
    public static Account getUserAccount(String accountId) {
        for(Account account: MainActivity.accountsList){
            if(accountId.equalsIgnoreCase(account.getAccountId()))
                return account;
        }
        return null;
    }
}
