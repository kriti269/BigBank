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

public class DepositActivity extends AppCompatActivity {
    EditText dpAmount;
    Spinner dpAccount;
    Button dpBack, dpDeposit;
    TextView txvSuccess;
    TextView txvError;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit);

        dpAmount = findViewById(R.id.extDpAmount);
        dpAccount = findViewById(R.id.spDpAccount);
        dpBack = findViewById(R.id.btnDpBack);
        dpDeposit = findViewById(R.id.btnDpDeposit);
        txvSuccess = findViewById(R.id.txvDpSuccess);
        txvError = findViewById(R.id.txvDpError);

        String[] accountTypes = AccountOperations.getAccountsNames(MainActivity.userAccounts);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, accountTypes);
        dpAccount.setAdapter(arrayAdapter);

        dpAmount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                txvError.setText("");
                txvSuccess.setText("");
            }
        });

        dpAccount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dpAmount.clearFocus();
                txvError.setText("");
                txvSuccess.setText("");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        dpDeposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dpAmount.clearFocus();
                txvError.setText("");
                txvSuccess.setText("");
                String amountString = dpAmount.getText().toString();
                if(amountString.isEmpty())
                    txvError.setText("Please enter an amount to be deposited!");
                else{
                    double amount = Double.parseDouble(amountString);
                    Account account = getAccount(dpAccount.getSelectedItem().toString());
                    account.setBalance(account.getBalance()+amount);
                    txvError.setText("");
                    txvSuccess.setText("Amount successfully deposited!");
                }

            }
        });

        dpBack.setOnClickListener(new View.OnClickListener() {
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