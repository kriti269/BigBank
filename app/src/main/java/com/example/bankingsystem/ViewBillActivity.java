package com.example.bankingsystem;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

// Activity class to display view all bill details
public class ViewBillActivity extends AppCompatActivity {
    // declare widgets
    ListView lvBills;
    Button back;
    TextView viewMessage;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_bill);

        // initialize widgets
        lvBills = findViewById(R.id.lvVbHistory);
        back = findViewById(R.id.btnVbBack);
        viewMessage = findViewById(R.id.txvVbMsg);

        // Get bill history and initialize bill history adaptor
        List<Bill> userBillsList = BillOperations.getUserBills(MainActivity.userBills);
        if(userBillsList.size()==0){
            viewMessage.setText("No bills history found!");
        }
        else{
            viewMessage.setText("");
            lvBills.setAdapter(new BillHistoryAdapter(this, userBillsList));
        }

        // return to home activity on back button
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(),HomeActivity.class);
                startActivity(intent);
            }
        });
    }
}