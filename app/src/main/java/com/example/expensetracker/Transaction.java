package com.example.expensetracker;

import android.util.Log;

public class Transaction {
    private String name;
    private String amount;
    private String type;
    private String tag;
    private String note;
    private String date;
    public Transaction(String name,String amount,String type,String tag,String date,String note){
        this.name = name;
        this.amount = amount;
        this.date = date;
        this.tag = tag;
        this.note = note;
        this.type = type;
        Log.d("ExpenseTracker", "Transaction: { Name : "+name+", Amount : "+amount+", Type : "+type+", Tag : "+tag+", Date"+date+", Note : "+note+"}");
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
