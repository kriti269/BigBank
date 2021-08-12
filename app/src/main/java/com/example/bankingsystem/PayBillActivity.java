package com.example.bankingsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class PayBillActivity extends AppCompatActivity {
    Spinner spUtilityType, spAccount;
    EditText subscriptionNo, amount;
    Button btnPayBill;
    TextView payBillError, payBillSuccess;
    //initializing utility types for spinner
    String[] utilityTypes = {"Select Utility Type", "Hydro",
            "Water", "Gas", "Phone"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_bill);
        spUtilityType = findViewById(R.id.spUtilityType);
        spAccount = findViewById(R.id.spAccount);
        subscriptionNo = findViewById(R.id.extSubscriptionNo);
        amount = findViewById(R.id.extAmount);
        btnPayBill = findViewById(R.id.btnPayUtilityBill);
        payBillError = findViewById(R.id.txvPayBillError);
        payBillSuccess = findViewById(R.id.txvPayBillSuccess);

        //initialize spinner for Utility types
        ArrayAdapter aaUtilityType = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, utilityTypes);
        spUtilityType.setAdapter(aaUtilityType);

        //initializing account from logged in user accounts
        String[] userAccountTypes = AccountOperations.getAccountsNames(MainActivity.userAccounts);
        ArrayAdapter aaAccountType = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item,
                userAccountTypes);
        spAccount.setAdapter(aaAccountType);

        //set spinner on click listeners
        spUtilityType.setOnClickListener(new PayBillClickEvents());
        spAccount.setOnClickListener(new PayBillClickEvents());
        subscriptionNo.setOnClickListener(new PayBillClickEvents());
        amount.setOnClickListener(new PayBillClickEvents());

        //set pay bill button on click listener
        btnPayBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amountString = String.valueOf(amount.getText());
                String selectedAccount = spAccount.getSelectedItem().toString();
                payBillError.setText("");
                if(spUtilityType.getSelectedItem().toString().equals("Select Utility Type")) {
                    payBillError.setText("Please select utility type to Pay Bill.");
                    return;
                }
                if(String.valueOf(subscriptionNo.getText()).equals("")) {
                    payBillError.setText("Please enter Subscription Number to Pay Bill.");
                    return;
                }
                if(selectedAccount.equals("Select Account Type")) {
                    payBillError.setText("Please select Account Type to Pay Bill From.");
                    return;
                }
                if(amountString.equals("")) {
                    payBillError.setText("Please enter Subscription Number to Pay Bill.");
                    return;
                }
                String result = AccountOperations.withdrawAmount(amountString, selectedAccount);
                if(result.isEmpty()) {
                    payBillSuccess.setText("Bill successfully paid!");
                }
                payBillError.setText(result);
            }
        });
    }

    //edit text and spinner click event listener
    private class PayBillClickEvents implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            payBillError.setText("");
        }
    }
}