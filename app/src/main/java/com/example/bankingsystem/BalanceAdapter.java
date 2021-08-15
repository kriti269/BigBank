package com.example.bankingsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Adapter to display account balances in list view
public class BalanceAdapter extends BaseAdapter {
    List<Account> accounts = new ArrayList<Account>();
    LayoutInflater inflater;
    //creating map for account types list and images id
    Map<String, Integer> accountTypeList;

    public BalanceAdapter(Context context, List<Account> accountList) {
        //initializing user account types list
        this.initializeAccountTypeList();
        //setting the accounts from the list of
        //account available for logged in user
        this.accounts = accountList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return accounts.size();
    }

    @Override
    public Object getItem(int position) {
        return accounts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        ViewHolder holder;
        if (view == null || view.getTag() == null) {
            view = inflater.inflate(R.layout.balance_adapter, null);
            holder = new ViewHolder();
            //setting up attributes for the view
            holder.accountTypeImg = view.findViewById(R.id.imvAccountType);
            holder.name = view.findViewById(R.id.txvAccountType);
            holder.price = view.findViewById(R.id.txvAccountBalance);
            view.setTag(holder);

        } else
            //setting up view holder
            holder = (ViewHolder) view.getTag();

        //getting account type details for view
        String accountType = accounts.get(i).getAccountType();
        holder.accountTypeImg.setImageResource(accountTypeList.get(accountType));
        holder.name.setText(accountType);
        holder.price.setText("$ " + String.valueOf(accounts.get(i).getBalance()));
        return view;
    }

    static class ViewHolder {
        ImageView accountTypeImg;
        TextView name;
        TextView price;
    }

    private void initializeAccountTypeList() {
        //initializing user account type with
        // account type names and relevant images
        accountTypeList = new HashMap<String, Integer>();
        accountTypeList.put("Savings", R.mipmap.saving_account);
        accountTypeList.put("Checking", R.mipmap.chequing_account);
        accountTypeList.put("Current", R.mipmap.current_account);
    }
}