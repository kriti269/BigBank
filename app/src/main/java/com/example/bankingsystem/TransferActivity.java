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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TransferActivity extends AppCompatActivity {
    EditText trAmount;
    Spinner trFromAccount, trToAccount;
    Button trBack, trTransfer;
    TextView txvTrSuccess;
    TextView txvTrError;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);

        trAmount = findViewById(R.id.extTrAmount);
        trFromAccount = findViewById(R.id.spTrFromAccount);
        trToAccount = findViewById(R.id.spTrToAccount);
        trBack = findViewById(R.id.btnTrBack);
        trTransfer = findViewById(R.id.btnTrTransfer);
        txvTrSuccess = findViewById(R.id.txvTrSuccess);
        txvTrError = findViewById(R.id.txvTrError);

        List<String> accountTypes = MainActivity.userAccounts.stream().map(Account::getAccountType).collect(Collectors.toList());
        accountTypes.add(0,"Select");
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, accountTypes);
        trToAccount.setAdapter(arrayAdapter);
        trFromAccount.setAdapter(arrayAdapter);

        trTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trAmount.clearFocus();
                txvTrError.setText("");
                txvTrSuccess.setText("");
                String amountString = trAmount.getText().toString();
                if(amountString.isEmpty())
                    txvTrError.setText("Please enter an amount to be transferred!");
                else if(trFromAccount.getSelectedItem()=="Select")
                    txvTrError.setText("Please select a transfer 'From Account'!");
                else if(trToAccount.getSelectedItem()=="Select")
                    txvTrError.setText("Please select a transfer 'To Account'!");
                else{
                    double amount = Double.parseDouble(amountString);
                    Account accountFrom = getAccount(trFromAccount.getSelectedItem().toString());
                    Account accountTo = getAccount(trToAccount.getSelectedItem().toString());
                    if(accountFrom.getBalance()<amount){
                        txvTrError.setText("Balance too low!");
                    }
                    else{
                        accountFrom.setBalance(accountFrom.getBalance()-amount);
                        accountTo.setBalance(accountTo.getBalance()+amount);
                        txvTrError.setText("");
                        txvTrSuccess.setText("Amount successfully transferred!");
                    }
                }

            }
        });

        trAmount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                txvTrError.setText("");
                txvTrSuccess.setText("");
            }
        });

        trFromAccount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                trAmount.clearFocus();
                txvTrSuccess.setText("");
                txvTrError.setText("");
                if(trToAccount.getSelectedItem() != "Select" &&
                        trToAccount.getSelectedItem() == trFromAccount.getSelectedItem()){
                    txvTrError.setText("From and To accounts should be different!");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        trToAccount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                trAmount.clearFocus();
                txvTrSuccess.setText("");
                txvTrError.setText("");
                if(trToAccount.getSelectedItem() != "Select"
                    && trFromAccount.getSelectedItem() == trToAccount.getSelectedItem()){
                    txvTrError.setText("From and To accounts should be different!");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        trBack.setOnClickListener(new View.OnClickListener() {
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