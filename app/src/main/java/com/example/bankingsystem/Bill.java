package com.example.bankingsystem;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

// POJO to store bill information
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

// Class for bill related operations
class BillOperations {
    //get all bills belonging to logged in user
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static List<Bill> getUserBills(List<Bill> bills) {
        //filter bill paid by logged in user
        bills = bills.stream()
                .filter(bill -> bill.getAccount().getUser().getAccessCardNumber() == MainActivity.loggedInUser.getAccessCardNumber())
                .collect(Collectors.toList());
        Collections.sort(bills, new Comparator<Bill>() {
            public int compare(Bill o1, Bill o2) {
                if (o1.getBillDate() == null || o2.getBillDate() == null)
                    return 0;
                return o2.getBillDate().compareTo(o1.getBillDate());
            }
        });

        return bills;
    }

    // get saved bills for a utility type
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static List<Bill> getPreviousBills(List<Bill> bills, String utilityType) {
        //filter bill paid by logged in user
        bills = bills.stream()
                .filter(bill -> bill.getUseForFuture() == true &&
                        bill.getAccount().getUser().getAccessCardNumber() == MainActivity.loggedInUser.getAccessCardNumber())
                .collect(Collectors.toList());
        Collections.sort(bills, new Comparator<Bill>() {
            public int compare(Bill o1, Bill o2) {
                if (o1.getBillDate() == null || o2.getBillDate() == null)
                    return 0;
                return o2.getBillDate().compareTo(o1.getBillDate());
            }
        });
        List<Bill> previousBills = new ArrayList<Bill>();
        for(int i=0;i<bills.size();i++) {
            Bill bill = bills.get(i);
            if(bill.getUtilityType().equals(utilityType)) {
                boolean exists = false;
                for(int j=0; j<previousBills.size();j++) {
                    if(previousBills.get(j).getSubscriptionNo().equals(bill.getSubscriptionNo())) {
                        exists = true;
                    }
                }
                if(!exists) {
                    previousBills.add(bill);
                }
            }
        }
        return previousBills;
    }

    // Get subscription numbers list from previously saved bills
    public static String[] getPreviousBillSubsNo(List<Bill> bills) {
        int arraySize = bills.size();
        String[] billSubsName = new String[arraySize+1];
        //add default option
        billSubsName[0] = "Select Subs Number";
        for (int i=1; i<=arraySize; i++) {
            billSubsName[i] = bills.get(i-1).getSubscriptionNo();
        }
        return billSubsName;
    }
}