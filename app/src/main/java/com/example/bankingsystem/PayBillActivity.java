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
        previousBill.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    spPreviousBills.setSelection(0);
                    spPreviousBills.setVisibility(View.VISIBLE);
                    txvSelectSubsNo.setVisibility(View.VISIBLE);
                    subscriptionNo.setVisibility(View.INVISIBLE);
                    txvSubsNo.setVisibility(View.INVISIBLE);
                } else {
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
                Intent intent = new Intent(getBaseContext(),HomeActivity.class);
                startActivity(intent);
            }
        });

        //set pay bill button on click listener
        btnPayBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payBillError.setText("");
                payBillSuccess.setText("");
                String amountString = String.valueOf(amount.getText());
                String selectedAccount = spAccount.getSelectedItem().toString();
                payBillError.setText("");
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
                Bill bill = new Bill(spUtilityType.getSelectedItem().toString(),
                        AccountOperations.getAccount(selectedAccount),
                        subscriptionNo.getText().toString(), Double.parseDouble(amountString),
                        new Date(), saveForFuture.isChecked());
                MainActivity.userBills.add(bill);
                String result = AccountOperations.withdrawAmount(amountString, selectedAccount);
                if (result.isEmpty()) {
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
                    MainActivity.userBills.remove(bill);
                }
                payBillError.setText(result);
            }
        });
    }

    //edit text focus changed event listener
    private class EditTextFocusChangeListener implements View.OnFocusChangeListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            payBillError.setText("");
            payBillSuccess.setText("");
        }
    }

    //spinner item select event listener
    private class SpinnerItemSelectedListener implements AdapterView.OnItemSelectedListener {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (triggered) {
                if(parent.getId() != spPreviousBills.getId()) {
                    subscriptionNo.clearFocus();
                    amount.clearFocus();
                    payBillError.setText("");
                    payBillSuccess.setText("");
                }
                if(parent.getId() == spUtilityType.getId()) {
                    amount.setText("");
                    subscriptionNo.setText("");
                    spAccount.setSelection(0);
                    previousBill.setChecked(false);
                    previousBill.setVisibility(View.INVISIBLE);
                    String selectedUtility = spUtilityType.getSelectedItem().toString();
                    if(selectedUtility.equals("Select Utility")) {
                        userPreviousBills = new ArrayList<Bill>();
                        spPreviousBills.setAdapter(null);
                        previousBill.setVisibility(View.INVISIBLE);
                    } else {
                        userPreviousBills = BillOperations.getPreviousBills(MainActivity.userBills, selectedUtility);
                        if(userPreviousBills.size() == 0) {
                            ArrayAdapter aaPreviousBills = new ArrayAdapter(context,
                                    R.layout.support_simple_spinner_dropdown_item,
                                    new ArrayList<String>());
                            spPreviousBills.setAdapter(aaPreviousBills);
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
                    //remove bill default selections on changes
                    if(billSelected == "Select Subs Number") {
                        subscriptionNo.setText("");
                        amount.setText("");
                        spAccount.setSelection(0);
                    } else {
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