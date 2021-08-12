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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
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
    boolean triggered = true;
    EditText receiverName;
    EditText receiverAccount;
    RadioButton rdbSelf;
    RadioButton rdbOthers;
    TextView txvTrToAccount;
    TextView txvTrRvName;
    TextView txvTrRvAccount;

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
        receiverName = findViewById(R.id.extTrRvName);
        receiverAccount = findViewById(R.id.extTrRvAccount);
        rdbSelf = findViewById(R.id.rdbTrSelf);
        rdbOthers = findViewById(R.id.rdbTrOthers);
        txvTrToAccount = findViewById(R.id.txvTrToAccount);
        txvTrRvAccount = findViewById(R.id.txvTrRvAccount);
        txvTrRvName = findViewById(R.id.txvTrRvName);

        rdbSelf.setOnCheckedChangeListener(new RadioButtonEvent());
        rdbOthers.setOnCheckedChangeListener(new RadioButtonEvent());

        String[] accountTypes = AccountOperations.getAccountsNames(MainActivity.userAccounts);
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
                else if(trFromAccount.getSelectedItemPosition()==0)
                    txvTrError.setText("Please select a transfer 'From Account'!");
                if(rdbSelf.isChecked()){
                    if(trToAccount.getSelectedItemPosition()==0)
                        txvTrError.setText("Please select a transfer 'To Account'!");
                    else if(trToAccount.getSelectedItem()==trFromAccount.getSelectedItem())
                        txvTrError.setText("From and To accounts should be different!");
                    else{
                        String result = AccountOperations.withdrawAmount(amountString, trFromAccount.getSelectedItem().toString());
                        if(result.isEmpty()) {
                            result = AccountOperations.depositAmount(amountString, trToAccount.getSelectedItem().toString());
                            if(result.isEmpty()) {
                                 trAmount.setText("");
                                triggered = false;
                                trFromAccount.setSelection(0);
                                trToAccount.setSelection(0);
                                txvTrSuccess.setText("Amount successfully transferred!");
                            }
                        }
                        txvTrError.setText(result);
                    }
                }
                else{
                    String receiverAccountStr = receiverAccount.toString();
                    String receiverNameStr = receiverName.toString();
                    if(receiverNameStr.isEmpty()){
                        txvTrError.setText("Please enter receiver's name!");
                    }
                    else if(receiverAccountStr.isEmpty()){
                        txvTrError.setText("Please enter receiver's account!");
                    }
                    else{
                        
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
                if(triggered) {
                    trAmount.clearFocus();
                    txvTrSuccess.setText("");
                    txvTrError.setText("");
                    if(trToAccount.getSelectedItemPosition()!=0 &&
                            trToAccount.getSelectedItem() == trFromAccount.getSelectedItem()){
                        txvTrError.setText("From and To accounts should be different!");
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        trToAccount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (triggered) {
                    trAmount.clearFocus();
                    txvTrSuccess.setText("");
                    txvTrError.setText("");
                    if(trToAccount.getSelectedItemPosition()!=0
                        && trFromAccount.getSelectedItem() == trToAccount.getSelectedItem()){
                        txvTrError.setText("From and To accounts should be different!");
                    }
                } else {
                    triggered = true;
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

    public class RadioButtonEvent implements CompoundButton.OnCheckedChangeListener{

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            txvTrSuccess.setText("");
            txvTrError.setText("");
            trAmount.setText("");
            trFromAccount.setSelection(0);
            trToAccount.setSelection(0);
            receiverName.setText("");
            receiverAccount.setText("");
            if(buttonView.getId()==R.id.rdbTrSelf){
                if(isChecked){
                    receiverName.setVisibility(View.INVISIBLE);
                    receiverAccount.setVisibility(View.INVISIBLE);
                    txvTrRvName.setVisibility(View.INVISIBLE);
                    txvTrRvAccount.setVisibility(View.INVISIBLE);
                }
                else{
                    receiverName.setVisibility(View.VISIBLE);
                    receiverAccount.setVisibility(View.VISIBLE);
                    txvTrRvName.setVisibility(View.VISIBLE);
                    txvTrRvAccount.setVisibility(View.VISIBLE);
                }
            }
            else if(buttonView.getId()==R.id.rdbTrOthers){
                if(isChecked){
                    txvTrToAccount.setVisibility(View.INVISIBLE);
                    trToAccount.setVisibility(View.INVISIBLE);
                }
                else{
                    txvTrToAccount.setVisibility(View.VISIBLE);
                    trToAccount.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}