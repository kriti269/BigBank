package com.example.bankingsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {
    TextView welcomeMessage;
    Button btnBalance, btnDeposit, btnWithdraw;
    Button btnTransfer, btnPayBill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        welcomeMessage = findViewById(R.id.txvWelcome);
        btnBalance = findViewById(R.id.btnCheckBalance);
        btnDeposit = findViewById(R.id.btnDeposit);
        btnWithdraw = findViewById(R.id.btnWithdraw);
        btnPayBill = findViewById(R.id.btnPayBill);
        btnTransfer = findViewById(R.id.btnTransfer);

        welcomeMessage.setText("Welcome " + MainActivity.loggedInUser.getName() + "!");

        btnBalance.setOnClickListener(new ButtonEvents());
        btnDeposit.setOnClickListener(new ButtonEvents());
        btnTransfer.setOnClickListener(new ButtonEvents());
        btnWithdraw.setOnClickListener(new ButtonEvents());
        btnPayBill.setOnClickListener(new ButtonEvents());


    }

    class ButtonEvents implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent intent = null;
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
            startActivity(intent);
        }
    }
}