package com.example.bankingsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class PayBillActivity extends AppCompatActivity {
    Spinner spUtilityType, spAccount;
    EditText subscriptionNo, amount;
    Button btnPayBill;
    TextView payBillError;
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

        //initialize spinner for Utility types
        ArrayAdapter aaUtilityType = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, utilityTypes);
        spUtilityType.setAdapter(aaUtilityType);

        //initializing account from logged in user accounts
        String[] userAccountTypes = AccountOperations.getAccountsNames(MainActivity.userAccounts);
        ArrayAdapter aaAccountType = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item,
                userAccountTypes);
        spAccount.setAdapter(aaAccountType);

        //set pay bill button on click listener
        btnPayBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payBillError.setText("");
            }
        });
    }
}