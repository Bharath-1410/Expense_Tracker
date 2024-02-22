package com.example.expensetracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    public ArrayList<String> expenseAmount;
    public ArrayList<String> expenseDate;
    public ArrayList<String> expenseType;
    public ArrayList<String> expenseCustomName;
    public ArrayList<String> expenseNote;
    public ArrayList<String> expenseTag;
    private static final String DATABASE_NAME = "expenseTracker";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        expenseAmount = new ArrayList<>();
        expenseDate = new ArrayList<>();
        expenseType = new ArrayList<>();
        expenseCustomName = new ArrayList<>();
        expenseNote = new ArrayList<>();
        expenseTag = new ArrayList<>();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS transactions (sno INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, amount TEXT, type TEXT, tag TEXT, date TEXT, note TEXT)";
        db.execSQL(createTableQuery);
        Log.d("DBHelper", "Creation of transactions table is successful");
        Log.d("ExpenseTracker", "DBHelper onCreate: Successfully Created Table transactions");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public static void updateTransaction(int sno, Transaction transaction,Context context) {
        try{
            DBHelper dbHelper = new DBHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("name",transaction.getName());
            values.put("amount",transaction.getAmount());
            values.put("type",transaction.getType());
            values.put("tag",transaction.getTag());
            values.put("date",transaction.getDate());
            values.put("note",transaction.getNote());
            db.update("transactions", values, "sno = ?", new String[]{String.valueOf(sno)});
            db.close();
            Log.d("updated", "updateTransaction: Success "+sno);
            Log.i("ExpenseTracker","updateTransaction: Successfully updated Records at "+sno+" The Values are "+transaction.getName()+", "+transaction.getAmount()+", "+transaction.getType()+", "+transaction.getTag()+", "+transaction.getDate()+", "+transaction.getNote());
        }catch (Exception e){
            Log.e("ExpenseTracker", "updateTransaction: "+e.toString() );
        }
    }

    public long addTransaction(Transaction transaction){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name",transaction.getName());
        values.put("amount",transaction.getAmount());
        values.put("type",transaction.getType());
        values.put("tag",transaction.getTag());
        values.put("date",transaction.getDate());
        values.put("note",transaction.getNote());
        long newid = db.insert("transactions",null,values);
        db.close();
        Log.d("DBHelper", "addTransaction: {" + transaction.getName() + transaction.getAmount() + transaction.getType() + transaction.getTag() + transaction.getNote() + transaction.getDate() + "}" );
        Log.w("ExpenseTracker", "addTransaction: {" + transaction.getName() + transaction.getAmount() + transaction.getType() + transaction.getTag() + transaction.getNote() + transaction.getDate() + "}" );
        return newid;
    }

    public static ArrayList<ArrayList<String>> fetchData(Context context,String [] projection,String selection,String [] selectionArgs) {
        ArrayList<ArrayList<String>> dataList = new ArrayList<>();

        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                "transactions",
                projection,
                selection,
                selectionArgs,
                null,
                null,
                "sno DESC"
        );

        while (cursor.moveToNext()) {
            ArrayList<String> rowData = new ArrayList<>();
            for (String column : projection) {
                rowData.add(cursor.getString(cursor.getColumnIndexOrThrow(column)));
            }
            dataList.add(rowData);
        }
        cursor.close();
        db.close();
        Log.d("ExpenseTracker", "fetchData() returned: " + dataList);
        return dataList;
    }

    public static ArrayList<ArrayList<String>> fetchData(Context context, String[] projection) {
        Log.d("ExpenseTracker", "Class "+context.getClass()+" fetchData() returned: " + fetchData(context.getApplicationContext(), projection,null,null));
        return fetchData(context.getApplicationContext(), projection,null,null);
    }
    public static void deleteRecord(Context context,int id) {
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsAffected = db.delete("transactions", "sno=?", new String[]{String.valueOf(id)});
        db.close();

        if (rowsAffected > 0) {
            // Deletion successful
            Log.d("DBHelper", "Record deleted successfully");
            Log.i("ExpenseTracker", "Record deleted successfully");
        } else {
            // No records were deleted
            Log.d("DBHelper", "No records deleted");
            Log.e("ExpenseTracker", "No records deleted");
        }
    }
    public static int getTotalExpenses(Context context) {
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        int totalExpense = 0;
        try {
            String[] projection = {"SUM(amount)"};
            cursor = db.query(
                    "transactions",
                    projection,
                    "type = ?",
                    new String[]{"Expense"},
                    null,
                    null,
                    null
            );
            if (cursor != null && cursor.moveToFirst()) {
                totalExpense = cursor.getInt(0);
            }
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        Log.d("ExpenseTracker", context.getClass()+" getTotalExpenses() returned: " + totalExpense);
        return totalExpense;
    }
    public static int getTotalIncome(Context context) {
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        int totalExpense = 0;
        try {
            String[] projection = {"SUM(amount)"};
            cursor = db.query(
                    "transactions",
                    projection,
                    "type = ?",
                    new String[]{"Income"},
                    null,
                    null,
                    null
            );
            if (cursor != null && cursor.moveToFirst()) {
                totalExpense = cursor.getInt(0);
            }
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        Log.d("ExpenseTracker", context.getClass()+" getTotalIncome() returned: " + totalExpense);
        return totalExpense;
    }
    
}
