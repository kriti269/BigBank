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

// Activity class for transfer amount within
// different types of accounts
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

    //used stream to populate account spinners
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

        //setup radio button on checked listener
        rdbSelf.setOnCheckedChangeListener(new RadioButtonEvent());
        rdbOthers.setOnCheckedChangeListener(new RadioButtonEvent());

        //get user account to get from and to accounts for transfer
        String[] accountTypes = AccountOperations.getAccountsNames(MainActivity.userAccounts);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, accountTypes);
        trToAccount.setAdapter(arrayAdapter);
        trFromAccount.setAdapter(arrayAdapter);

        //set transfer button on click listener
        trTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //remove error and success message if exists
                trAmount.clearFocus();
                txvTrError.setText("");
                txvTrSuccess.setText("");
                String amountString = trAmount.getText().toString();
                //validate amount and from account values input by user
                if(amountString.isEmpty())
                    txvTrError.setText("Please enter an amount to be transferred!");
                else if(trFromAccount.getSelectedItemPosition()==0)
                    txvTrError.setText("Please select a transfer 'From Account'!");
                else{
                    //validate that transfer is performed to user's other account
                    //or to other user's account
                    if(rdbSelf.isChecked()){
                        //validate transfer to account by user
                        //if sending to user's account validate account selected
                        //validate that user is not transferring to same account
                        if(trToAccount.getSelectedItemPosition()==0)
                            txvTrError.setText("Please select a transfer 'To Account'!");
                        else if(trToAccount.getSelectedItem()==trFromAccount.getSelectedItem())
                            txvTrError.setText("From and To accounts should be different!");
                        else{
                            //withdraw amount from the from account selected by user
                            String result = AccountOperations.withdrawAmount(amountString, trFromAccount.getSelectedItem().toString());
                            //if success validation
                            if(result.isEmpty()) {
                                //deposit amount into user selected to account
                                result = AccountOperations.depositAmount(amountString, trToAccount.getSelectedItem().toString());
                                if(result.isEmpty()) {
                                    //remove all entered values
                                    //show success message to user
                                    trAmount.setText("");
                                    triggered = false;
                                    trFromAccount.setSelection(0);
                                    trToAccount.setSelection(0);
                                    txvTrSuccess.setText("Amount successfully transferred!");
                                }
                            }
                            //show error message to user in case of error
                            txvTrError.setText(result);
                        }
                    }
                    else {
                        //validate transfer to account details in case
                        //amount is transfer to another account
                        String receiverAccountStr = receiverAccount.getText().toString();
                        String receiverNameStr = receiverName.getText().toString();
                        if (receiverNameStr.isEmpty()) {
                            txvTrError.setText("Please enter receiver's name!");
                        } else if (receiverAccountStr.isEmpty()) {
                            txvTrError.setText("Please enter receiver's account!");
                        } else {
                            //validate if receiver name and account exists or not
                            if (!AccountOperations.checkUser(receiverNameStr)) {
                                txvTrError.setText("Receiver does not exist!");
                            } else if (!AccountOperations.checkAccount(receiverAccountStr)) {
                                txvTrError.setText("Receiver account does not exist!");
                            } else if (!AccountOperations.checkUserAccount(receiverNameStr, receiverAccountStr)) {
                                txvTrError.setText("Receiver name and account does not exist!");
                            } else {
                                //withdraw account from user from account selected
                                String result = AccountOperations.withdrawAmount(amountString, trFromAccount.getSelectedItem().toString());
                                if (result.isEmpty()) {
                                    //deposit amount to another account
                                    result = AccountOperations.depositUserAmount(amountString, receiverAccountStr);
                                    if (result.isEmpty()) {
                                        //set default values in case of success
                                        trAmount.setText("");
                                        triggered = false;
                                        trFromAccount.setSelection(0);
                                        trToAccount.setSelection(0);
                                        receiverAccount.setText("");
                                        receiverName.setText("");
                                        //setup success message
                                        txvTrSuccess.setText("Amount successfully transferred!");
                                    }
                                }
                                //set error message in case of error
                                txvTrError.setText(result);
                            }
                        }
                    }
                }
            }
        });

        //setup focus change listener to remove error and success messages
        trAmount.setOnFocusChangeListener(new InputFocusChangeEvents());
        receiverAccount.setOnFocusChangeListener(new InputFocusChangeEvents());
        receiverName.setOnFocusChangeListener(new InputFocusChangeEvents());

        //setup from account item selected listener
        trFromAccount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //if automatically triggered selection from activity
                if(triggered) {
                    //empty details if from account selection is changed
                    trAmount.clearFocus();
                    txvTrSuccess.setText("");
                    txvTrError.setText("");
                    if(trToAccount.getSelectedItemPosition()!=0 &&
                            trToAccount.getSelectedItem() == trFromAccount.getSelectedItem()){
                        //show error message in case to and from account selection is same
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

        //setup back button click listener
        trBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //move to home page is user click on back button
                Intent intent = new Intent(getBaseContext(),HomeActivity.class);
                startActivity(intent);
            }
        });
    }

    //setup on input focus change listener
    private class InputFocusChangeEvents implements View.OnFocusChangeListener {

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            //remove error message if shown to user
            txvTrError.setText("");
            txvTrSuccess.setText("");
        }
    }

    //setup radio button on checked change listener
    private class RadioButtonEvent implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            //remove success or error messages
            txvTrSuccess.setText("");
            txvTrError.setText("");
            trAmount.setText("");
            trFromAccount.setSelection(0);
            trToAccount.setSelection(0);
            receiverName.setText("");
            receiverAccount.setText("");

            // change receiver amount and account visibility on radio button click
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