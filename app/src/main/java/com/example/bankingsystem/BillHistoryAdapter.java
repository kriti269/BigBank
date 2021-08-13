package com.example.bankingsystem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class BillHistoryAdapter extends BaseAdapter {
    List<Bill> billsHistoryList = new ArrayList<Bill>();
    LayoutInflater layoutInflater;

    public BillHistoryAdapter(Context context, List<Bill> billsHistoryList) {
        this.billsHistoryList = billsHistoryList;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return billsHistoryList.size();
    }

    @Override
    public Object getItem(int position) {
        return billsHistoryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null || convertView.getTag() == null) {
            convertView = layoutInflater.inflate(R.layout.bill_history, null);
            holder = new ViewHolder();
            //setting up attributes for the view
            holder.type = convertView.findViewById(R.id.txvBhType);
            holder.subscription = convertView.findViewById(R.id.txvBhSub);
            holder.account = convertView.findViewById(R.id.txvBhAccount);
            holder.amount = convertView.findViewById(R.id.txvBhAmount);
            holder.date = convertView.findViewById(R.id.txvBhDate);
            System.out.println(holder.account);
            convertView.setTag(holder);

        } else
            //setting up view holder
            holder = (ViewHolder) convertView.getTag();


        Bill bill = billsHistoryList.get(position);
        System.out.println(bill.getUtilityType());
        holder.type.setText(bill.getUtilityType());
        holder.subscription.setText(bill.getSubscriptionNo());
        holder.account.setText(bill.getAccount().getAccountType());
        holder.amount.setText("$" +String.valueOf(bill.getAmount()));
        holder.date.setText(new SimpleDateFormat("dd-MM-yy HH:mm").format(bill.getBillDate()));
        return convertView;
    }

    static class ViewHolder{
        TextView type;
        TextView subscription;
        TextView account;
        TextView amount;
        TextView date;
    }
}
