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

public class WithdrawActivity extends AppCompatActivity {
    EditText wdAmount;
    Spinner wdAccount;
    Button wdBack, wdWithdraw;
    TextView txvWdSuccess;
    TextView txvWdError;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);

        wdAmount = findViewById(R.id.extWdAmount);
        wdAccount = findViewById(R.id.spWdAccount);
        wdBack = findViewById(R.id.btnWdBack);
        wdWithdraw = findViewById(R.id.btnWdWithdraw);
        txvWdSuccess = findViewById(R.id.txvWdSuccess);
        txvWdError = findViewById(R.id.txvWdError);

        String[] accountTypes = AccountOperations.getAccountsNames(MainActivity.userAccounts);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, accountTypes);
        wdAccount.setAdapter(arrayAdapter);

        wdAmount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                txvWdError.setText("");
                txvWdSuccess.setText("");
            }
        });

        wdAccount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                wdAmount.clearFocus();
                txvWdError.setText("");
                txvWdSuccess.setText("");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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
                    double amount = Double.parseDouble(amountString);
                    Account account = getAccount(wdAccount.getSelectedItem().toString());
                    if(account.getBalance()<amount){
                        txvWdError.setText("Balance too low!");
                    }
                    else{
                        account.setBalance(account.getBalance()-amount);
                        txvWdError.setText("");
                        txvWdSuccess.setText("Amount successfully withdrawn!");
                    }
                }

            }
        });

        wdBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(),HomeActivity.class);
                startActivity(intent);
            }
        });
    }

    private static Account getAccount(String accountType){
        for(Account account: MainActivity.userAccounts){
            if(accountType==account.getAccountType())
                return account;
        }
        return null;
    }
}