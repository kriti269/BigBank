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
    boolean triggered = true;

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
        spUtilityType.setOnItemSelectedListener(new SpinnerItemSelectedListener());
        spAccount.setOnItemSelectedListener(new SpinnerItemSelectedListener());
        subscriptionNo.setOnFocusChangeListener(new EditTextFocusChangeListener());
        amount.setOnFocusChangeListener(new EditTextFocusChangeListener());

        //set pay bill button on click listener
        btnPayBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amountString = String.valueOf(amount.getText());
                String selectedAccount = spAccount.getSelectedItem().toString();
                payBillError.setText("");
                if (spUtilityType.getSelectedItem().toString().equals("Select Utility Type")) {
                    payBillError.setText("Please select utility type to Pay Bill!");
                    return;
                }
                if (subscriptionNo.getText().toString().isEmpty()) {
                    payBillError.setText("Please enter Subscription Number to Pay Bill!");
                    return;
                }
                if (selectedAccount.equals("Select Account Type")) {
                    payBillError.setText("Please select Account Type to Pay Bill From!");
                    return;
                }
                if (amountString.equals("")) {
                    payBillError.setText("Please enter Subscription Number to Pay Bill!");
                    return;
                }
                Bill bill = new Bill(AccountOperations.getAccount(selectedAccount),
                        subscriptionNo.getText().toString(), Double.parseDouble(amountString));
                MainActivity.userBills.add(bill);
                String result = AccountOperations.withdrawAmount(amountString, selectedAccount);
                if (result.isEmpty()) {
                    amount.setText("");
                    subscriptionNo.setText("");
                    triggered = false;
                    spUtilityType.setSelection(0);
                    triggered = false;
                    spAccount.setSelection(0);
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
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (triggered) {
                subscriptionNo.clearFocus();
                amount.clearFocus();
                payBillError.setText("");
                payBillSuccess.setText("");
            } else {
                triggered = true;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
}