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
import android.widget.Toast;

import java.util.List;
import java.util.stream.Collectors;

// Activity class to deposit amount to user accounts
public class DepositActivity extends AppCompatActivity {
    EditText dpAmount;
    Spinner dpAccount;
    Button dpBack, dpDeposit;
    TextView txvSuccess;
    TextView txvError;
    boolean triggered = true;

    //added this string to use streams to filter data
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit);

        // initialize view widgets
        dpAmount = findViewById(R.id.extDpAmount);
        dpAccount = findViewById(R.id.spDpAccount);
        dpBack = findViewById(R.id.btnDpBack);
        dpDeposit = findViewById(R.id.btnDpDeposit);
        txvSuccess = findViewById(R.id.txvDpSuccess);
        txvError = findViewById(R.id.txvDpError);

        // set adapter for list view
        String[] accountTypes = AccountOperations.getAccountsNames(MainActivity.userAccounts);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, accountTypes);
        dpAccount.setAdapter(arrayAdapter);

        // Clear success or errors on amount focus change
        dpAmount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                txvError.setText("");
                txvSuccess.setText("");
            }
        });

        // clear success or error messages on selected item change
        dpAccount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(triggered) {
                    //clear the details when selected account is changed
                    dpAmount.clearFocus();
                    txvError.setText("");
                    txvSuccess.setText("");
                } else {
                    //stopping the trigger when done manually from backend
                    triggered = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Deposit amount on button click
        dpDeposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //clear error and success messages
                dpAmount.clearFocus();
                txvError.setText("");
                txvSuccess.setText("");
                //get amount entered by user
                String amountString = dpAmount.getText().toString();
                if(amountString.isEmpty())
                    //set error message in case of amount not provided
                    txvError.setText("Please enter an amount to be deposited!");
                else{
                    //perform deposit amount with entered values
                    String result = AccountOperations.depositAmount(amountString,
                            dpAccount.getSelectedItem().toString());
                    //validate that deposit operation is performed result
                    //setup success and error messages
                    if(result.isEmpty()) {
                        dpAmount.setText("");
                        txvSuccess.setText("Amount successfully deposited!");
                        triggered = false;
                        dpAccount.setSelection(0);
                    }
                    txvError.setText(result);
                }
            }
        });

        dpBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //back button on click listener
                //move to main activity on button click
                Intent intent = new Intent(getBaseContext(),HomeActivity.class);
                startActivity(intent);
            }
        });

    }
}