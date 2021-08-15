package com.example.bankingsystem;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

// Activity class for users to pay utility bills
public class PayBillActivity extends AppCompatActivity {
    private static Context context;
    Spinner spUtilityType, spAccount, spPreviousBills;
    EditText subscriptionNo, amount;
    Button btnPayBill, btnBack;
    TextView payBillError, payBillSuccess, txvSelectSubsNo, txvSubsNo;
    CheckBox saveForFuture, previousBill;
    //initializing utility types for spinner
    String[] utilityTypes = {"Select Utility", "Hydro",
            "Water", "Gas", "Phone"};
    boolean triggered = true;
    ArrayAdapter aaAccountType;
    List<Bill> userPreviousBills = new ArrayList<Bill>();
    Bill selectedBill = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PayBillActivity.context = getApplicationContext();
        setContentView(R.layout.activity_pay_bill);
        spUtilityType = findViewById(R.id.spUtilityType);
        spAccount = findViewById(R.id.spAccount);
        subscriptionNo = findViewById(R.id.extSubscriptionNo);
        amount = findViewById(R.id.extAmount);
        btnPayBill = findViewById(R.id.btnPayUtilityBill);
        payBillError = findViewById(R.id.txvPayBillError);
        payBillSuccess = findViewById(R.id.txvPayBillSuccess);
        saveForFuture = findViewById(R.id.cbSaveForFuture);
        spPreviousBills = findViewById(R.id.spPreviousBills);
        previousBill = findViewById(R.id.chkPreviousBill);
        txvSelectSubsNo = findViewById(R.id.txvSelectSubsNo);
        txvSubsNo = findViewById(R.id.txvSubsNo);
        btnBack = findViewById(R.id.btnPbBack);

        //initialize spinner for Utility types
        ArrayAdapter aaUtilityType = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, utilityTypes);
        spUtilityType.setAdapter(aaUtilityType);

        //initializing account from logged in user accounts
        String[] userAccountTypes = AccountOperations.getAccountsNames(MainActivity.userAccounts);
        aaAccountType = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item,
                userAccountTypes);
        spAccount.setAdapter(aaAccountType);

        //set spinner on click listeners
        spUtilityType.setOnItemSelectedListener(new SpinnerItemSelectedListener());
        spAccount.setOnItemSelectedListener(new SpinnerItemSelectedListener());
        spPreviousBills.setOnItemSelectedListener(new SpinnerItemSelectedListener());
        subscriptionNo.setOnFocusChangeListener(new EditTextFocusChangeListener());
        amount.setOnFocusChangeListener(new EditTextFocusChangeListener());
        //setting up checked change listener to confirm
        //that user want to copy details of bill from previously paid bills
        previousBill.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            //used this code for using streams in Bill operations class
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    //setup user state for selection from previously paid bills
                    spPreviousBills.setSelection(0);
                    spPreviousBills.setVisibility(View.VISIBLE);
                    txvSelectSubsNo.setVisibility(View.VISIBLE);
                    subscriptionNo.setVisibility(View.INVISIBLE);
                    txvSubsNo.setVisibility(View.INVISIBLE);
                } else {
                    //setup default state for user to enter bill details
                    //in case of new bill paid
                    subscriptionNo.setText("");
                    spPreviousBills.setVisibility(View.INVISIBLE);
                    txvSelectSubsNo.setVisibility(View.INVISIBLE);
                    subscriptionNo.setVisibility(View.VISIBLE);
                    txvSubsNo.setVisibility(View.VISIBLE);
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //back button listener to move user
                // back to the home page
                Intent intent = new Intent(getBaseContext(),HomeActivity.class);
                startActivity(intent);
            }
        });

        //set pay bill button on click listener
        btnPayBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //remove previous error and success message if exists
                payBillError.setText("");
                payBillSuccess.setText("");
                String amountString = String.valueOf(amount.getText());
                String selectedAccount = spAccount.getSelectedItem().toString();
                payBillError.setText("");
                //validate that values input by user
                if (spUtilityType.getSelectedItem().toString().equals("Select Utility")) {
                    payBillError.setText("Please select utility to Pay Bill!");
                    return;
                }
                if (subscriptionNo.getText().toString().isEmpty()) {
                    payBillError.setText("Please enter Subscription Number to Pay Bill!");
                    return;
                }
                if (selectedAccount.equals("Select Account")) {
                    payBillError.setText("Please select Account to Pay Bill From!");
                    return;
                }
                if (amountString.equals("")) {
                    payBillError.setText("Please enter amount to Pay Bill!");
                    return;
                }
                //setup bill object to pay bill
                Bill bill = new Bill(spUtilityType.getSelectedItem().toString(),
                        AccountOperations.getAccount(selectedAccount),
                        subscriptionNo.getText().toString(), Double.parseDouble(amountString),
                        new Date(), saveForFuture.isChecked());
                //saved bill in bills paid static list
                MainActivity.userBills.add(bill);
                //withdraw amount from user selected account
                String result = AccountOperations.withdrawAmount(amountString, selectedAccount);
                //validate that amount is successfully withdrawn
                if (result.isEmpty()) {
                    //setup activity to default state on
                    //successful bill payment
                    subscriptionNo.setText("");
                    txvSelectSubsNo.setVisibility(View.INVISIBLE);
                    subscriptionNo.setVisibility(View.VISIBLE);
                    previousBill.setVisibility(View.INVISIBLE);
                    txvSubsNo.setVisibility(View.VISIBLE);
                    saveForFuture.setChecked(false);
                    previousBill.setChecked(false);
                    amount.setText("");
                    subscriptionNo.setText("");
                    triggered = false;
                    spAccount.setSelection(0);
                    spPreviousBills.setSelection(0);
                    spUtilityType.setSelection(0);
                    payBillSuccess.setText("Bill successfully paid!");
                } else {
                    // remove bill from static list if
                    // bill not paid successfully
                    MainActivity.userBills.remove(bill);
                }
                //setup error message
                payBillError.setText(result);
            }
        });
    }

    //edit text focus changed event listener
    private class EditTextFocusChangeListener implements View.OnFocusChangeListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            //on focus change remove existing error and success messages
            payBillError.setText("");
            payBillSuccess.setText("");
        }
    }

    //spinner item select event listener
    private class SpinnerItemSelectedListener implements AdapterView.OnItemSelectedListener {
        //added this code as used streams in bill operation class
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            //validation for handling cases if selection is changed
            // by user or manually from code
            if (triggered) {
                //clear messages in case previous bill are changed
                if(parent.getId() != spPreviousBills.getId()) {
                    subscriptionNo.clearFocus();
                    amount.clearFocus();
                    payBillError.setText("");
                    payBillSuccess.setText("");
                }
                if(parent.getId() == spUtilityType.getId()) {
                    //setup default values on utility type update
                    amount.setText("");
                    subscriptionNo.setText("");
                    spAccount.setSelection(0);
                    previousBill.setChecked(false);
                    previousBill.setVisibility(View.INVISIBLE);
                    String selectedUtility = spUtilityType.getSelectedItem().toString();
                    //validate that utility type value is selected or not
                    if(selectedUtility.equals("Select Utility")) {
                        userPreviousBills = new ArrayList<Bill>();
                        spPreviousBills.setAdapter(null);
                        previousBill.setVisibility(View.INVISIBLE);
                    } else {
                        //get previous bills paid by user for selected utility
                        userPreviousBills = BillOperations.getPreviousBills(MainActivity.userBills, selectedUtility);
                        //provide user option to select for previous bills
                        // paid in case it exists in static list
                        if(userPreviousBills.size() == 0) {
                            //setup list of previous bills
                            ArrayAdapter aaPreviousBills = new ArrayAdapter(context,
                                    R.layout.support_simple_spinner_dropdown_item,
                                    new ArrayList<String>());
                            spPreviousBills.setAdapter(aaPreviousBills);
                            //show previous bill spinner to user
                            previousBill.setVisibility(View.INVISIBLE);
                        } else {
                            //initializing previous Bills paid in case of previous bill saved for future
                            //get previous bill paid by user for a specific utility type
                            String[] previousBillSubsNo = BillOperations.getPreviousBillSubsNo(userPreviousBills);
                            ArrayAdapter aaPreviousBills = new ArrayAdapter(context,
                                    R.layout.support_simple_spinner_dropdown_item,
                                    previousBillSubsNo);
                            spPreviousBills.setAdapter(aaPreviousBills);
                            previousBill.setVisibility(View.VISIBLE);
                        }
                    }
                } else if(parent.getId() == spPreviousBills.getId()) {
                    String billSelected = spPreviousBills.getSelectedItem().toString();
                    //validate user selection for previous bill paid selection
                    if(billSelected == "Select Subs Number") {
                        //remove selected bill details in case
                        //no bills are selected
                        subscriptionNo.setText("");
                        amount.setText("");
                        spAccount.setSelection(0);
                    } else {
                        //get details of previous bill in case
                        //previously paid bill is selected by user
                        //for current bill payment
                        selectedBill = (userPreviousBills.stream()
                                .filter(previousBill -> previousBill.subscriptionNo == billSelected)
                                .collect(Collectors.toList())).get(0);
                        subscriptionNo.setText(selectedBill.getSubscriptionNo());
                        amount.setText(String.valueOf(selectedBill.getAmount()));
                        int aaPosition = aaAccountType.getPosition(selectedBill.getAccount().getAccountType());
                        spAccount.setSelection(aaPosition);
                    }
                }
            } else {
                if(parent.getId() == spAccount.getId()) {
                    //if account spinner selected manually
                    //update back to handle updates from user
                    triggered = true;
                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    public static Context getAppContext() {
        return PayBillActivity.context;
    }
}