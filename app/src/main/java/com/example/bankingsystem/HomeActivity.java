package com.example.bankingsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

// Activity class to show home page to user with account related options
public class HomeActivity extends AppCompatActivity {
    TextView welcomeMessage;
    Button btnBalance, btnDeposit, btnWithdraw;
    Button btnTransfer, btnPayBill, btnViewBills;
    Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //initializing view items
        welcomeMessage = findViewById(R.id.txvWelcome);
        btnBalance = findViewById(R.id.btnCheckBalance);
        btnDeposit = findViewById(R.id.btnDeposit);
        btnWithdraw = findViewById(R.id.btnWithdraw);
        btnPayBill = findViewById(R.id.btnPayBill);
        btnTransfer = findViewById(R.id.btnTransfer);
        btnViewBills = findViewById(R.id.btnViewBills);
        btnLogout = findViewById(R.id.btnLogout);

        //set welcome message for user on top
        welcomeMessage.setText("Welcome " + MainActivity.loggedInUser.getName() + "!");

        //added respective click events on button click
        btnBalance.setOnClickListener(new ButtonEvents());
        btnDeposit.setOnClickListener(new ButtonEvents());
        btnTransfer.setOnClickListener(new ButtonEvents());
        btnWithdraw.setOnClickListener(new ButtonEvents());
        btnPayBill.setOnClickListener(new ButtonEvents());
        btnViewBills.setOnClickListener(new ButtonEvents());

        //setup logout button click listener to logout user
        //and move user to login screen
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //remove logged in user details from static variables
                MainActivity.loggedInUser = null;
                MainActivity.userAccounts.clear();
                Intent intent = new Intent(getBaseContext(),MainActivity.class);
                startActivity(intent);
            }
        });
    }

    class ButtonEvents implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent intent = null;
            //handle click event based on button id
            //forward the user to respective activity
            if(v.getId()==R.id.btnCheckBalance){
                intent = new Intent(getBaseContext(), CheckBalanceActivity.class);
            }
            else if(v.getId()==R.id.btnDeposit){
                intent = new Intent(getBaseContext(), DepositActivity.class);
            }
            else if(v.getId()==R.id.btnWithdraw){
                intent = new Intent(getBaseContext(), WithdrawActivity.class);
            }
            else if(v.getId()==R.id.btnTransfer){
                intent = new Intent(getBaseContext(), TransferActivity.class);
            }
            else if(v.getId()==R.id.btnPayBill){
                intent = new Intent(getBaseContext(), PayBillActivity.class);
            }
            else if(v.getId()==R.id.btnViewBills){
                intent = new Intent(getBaseContext(), ViewBillActivity.class);
            }
            //starting the next activity
            startActivity(intent);
        }
    }
}