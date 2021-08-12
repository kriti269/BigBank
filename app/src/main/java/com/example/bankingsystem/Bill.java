package com.example.bankingsystem;

import java.util.Date;
import java.util.List;

public class Bill {
    public String utilityType;
    public Account account;
    public String subscriptionNo;
    public double amount;
    public Date billDate;
    public boolean useForFuture;

    public Bill(String utilityType, Account account, String subscriptionNo,
                double amount, Date billDate, boolean useForFuture) {
        this.utilityType = utilityType;
        this.account = account;
        this.subscriptionNo = subscriptionNo;
        this.amount = amount;
        this.billDate = billDate;
        this.useForFuture = useForFuture;
    }

    public String getUtilityType() { return utilityType; }

    public void SetUtilityType() { this.utilityType = utilityType; }

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

    public Date getBillDate() { return billDate; }

    public void setBillDate(Date billDate) { this.billDate = billDate; }

    public boolean getUseForFuture() { return useForFuture; }

    public void setUseForFuture(boolean useForFuture) { this.useForFuture = useForFuture; }
}

class BillOperations {
    public static List<Bill> getPreviousBills(List<Bill> bills, String utilityType) {

    }

    public static String[] getPreviousBillSubsNo(List<Bill> bills) {
        int arraySize = bills.size();
        String[] billSubsName = new String[arraySize+1];
        //add default option
        billSubsName[0] = "Select Subscription Number";
        for (int i=1; i<=arraySize; i++) {
            billSubsName[i] = bills.get(i-1).getSubscriptionNo();
        }
        return billSubsName;
    }
}