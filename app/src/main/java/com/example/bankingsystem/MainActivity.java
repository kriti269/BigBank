package com.example.bankingsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

// Activity class to show login page to user for
// validating the credentials entered by user
public class MainActivity extends AppCompatActivity {
    EditText accessCard;
    EditText pin;
    TextView loginError;
    Button login;
    static List<Account> accountsList;
    static User loggedInUser;
    static List<Account> userAccounts = new ArrayList<Account>();
    static List<Bill> userBills = new ArrayList<Bill>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        accessCard = findViewById(R.id.extAccessCard);
        pin = findViewById(R.id.extPin);
        loginError = findViewById(R.id.txvError);
        login = findViewById(R.id.btnLogin);

        //initialize default bank user data
        initializeBankData();
        //setup login button on click listener
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //read the access card and pin entered by user
                long accessCardNumber = Long.parseLong(String.valueOf(accessCard.getText()));
                long pinNumber = Long.parseLong(String.valueOf(pin.getText()));
                boolean isValid = false;
                //clear previous entries from static memory if exists
                loggedInUser = null;
                userAccounts.clear();
                //matching logged in user credentials with
                //existing users in the records
                for(Account account:accountsList){
                    User user = account.getUser();
                    if(user.getAccessCardNumber()==accessCardNumber && user.getPinNumber()==pinNumber) {
                        //save the details on successful login
                        isValid = true;
                        loggedInUser = loggedInUser==null?user:loggedInUser;
                        userAccounts.add(account);
                    }
                }
                if(!isValid)
                    //show error message to user
                    Toast.makeText(getBaseContext(),"Invalid credentials!",Toast.LENGTH_LONG).show();
                else{
                    //move the user to home page activity
                    Intent intent = new Intent(getBaseContext(),HomeActivity.class);
                    startActivity(intent);
                }
            }
        });


    }

    //initializing bank user data on application startup
    public static void initializeBankData(){
        accountsList = new ArrayList<Account>();
        User user = new User(98889991100L, 1234, "Kriti");
        Account account = new Account(user,"BB0000120","Savings", 7000.0);
        accountsList.add(account);
        account = new Account(user,"BB0000130","Current", 801.0);
        accountsList.add(account);
        account = new Account(user,"BB0000140","Checking", 1109.0);
        accountsList.add(account);
        user = new User(98889991111L, 1234, "Jatin Bhola");
        account = new Account(user,"BB0000121","Current", 2040.15);
        accountsList.add(account);
        account = new Account(user,"BB0000131","Checking", 20.91);
        accountsList.add(account);
        user = new User(98889991122L, 1234, "Sonia");
        account = new Account(user,"BB0000122","Checking", 8010.89);
        accountsList.add(account);
        user = new User(98889991133L, 1234, "Varun Reddy");
        account = new Account(user,"BB0000123","Current", 5500.95);
        accountsList.add(account);
    }
}