package com.example.bankingsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class CheckBalanceActivity extends AppCompatActivity {
    ListView accountTypesListView;
    TextView txvBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_balance);
        accountTypesListView = findViewById(R.id.lvAccounts);
        txvBalance = findViewById(R.id.txvBalance);

        //initializing account type list view adapter with details
        accountTypesListView.setAdapter(new BalanceAdapter(this, MainActivity.userAccounts));
        setTotalBalance();
    }

    private void setTotalBalance() {
        List<Account> userAccountsList = MainActivity.userAccounts;
        double totalBalance = 0.0;
        //adding balance of all accounts to show total balance
        for(int i=0;i<userAccountsList.size();i++) {
            totalBalance += userAccountsList.get(i).getBalance();
        }
        txvBalance.setText("$ " + String.valueOf(totalBalance));
    }
}