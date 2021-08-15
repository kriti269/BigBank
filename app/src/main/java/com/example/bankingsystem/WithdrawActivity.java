package com.example.bankingsystem;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;
import java.util.stream.Collectors;

// Activity class to withdraw money
public class WithdrawActivity extends AppCompatActivity {
    // declare all widgets
    EditText wdAmount;
    Spinner wdAccount;
    Button wdBack, wdWithdraw;
    TextView txvWdSuccess, txvWdError;
    boolean triggered = true;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);

        // initialize widgets
        wdAmount = findViewById(R.id.extWdAmount);
        wdAccount = findViewById(R.id.spWdAccount);
        wdBack = findViewById(R.id.btnWdBack);
        wdWithdraw = findViewById(R.id.btnWdWithdraw);
        txvWdSuccess = findViewById(R.id.txvWdSuccess);
        txvWdError = findViewById(R.id.txvWdError);

        // get all accounts of a user and set it in a spinner
        String[] accountTypes = AccountOperations.getAccountsNames(MainActivity.userAccounts);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, accountTypes);
        wdAccount.setAdapter(arrayAdapter);

        // clear success and error messages on changing amount values
        wdAmount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                txvWdError.setText("");
                txvWdSuccess.setText("");
            }
        });

        // clear success and error messages on changing account values
        wdAccount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(triggered) {
                    wdAmount.clearFocus();
                    txvWdError.setText("");
                    txvWdSuccess.setText("");
                } else {
                    triggered = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // withdraw amount on button click
        wdWithdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wdAmount.clearFocus();
                txvWdError.setText("");
                txvWdSuccess.setText("");
                String amountString = wdAmount.getText().toString();
                if(amountString.isEmpty())
                    txvWdError.setText("Please enter an amount to be withdrawn!");
                else{
                    String result = AccountOperations.withdrawAmount(amountString, wdAccount.getSelectedItem().toString());
                    if(result.isEmpty()) {
                        wdAmount.setText("");
                        triggered = false;
                        wdAccount.setSelection(0);
                        txvWdSuccess.setText("Amount successfully withdrawn!");
                    }
                    txvWdError.setText(result);
                }
            }
        });

        // return to home on back button click
        wdBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(),HomeActivity.class);
                startActivity(intent);
            }
        });
    }
}