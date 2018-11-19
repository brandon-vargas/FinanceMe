package apps.brandon.finance;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "BillInfo.db";

    public static final String CREATE_TABLE_BILLS = "CREATE TABLE billtable "+
            "(id INTEGER PRIMARY KEY, name TEXT, day TEXT, description TEXT, amount TEXT)";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DAY = "day";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_AMOUNT = "amount";
    public static final String TABLE_NAME_BILL = "billtable";
    public static final String DROP_TABLE_BILLS = "DROP TABLE IF EXISTS billtable";

    public static final String CREATE_TABLE_CHECKDATES = "CREATE TABLE checkdatestable"+
            "(id INTEGER PRIMARY KEY, date TEXT)";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_ID_CHECKS = "id";
    public static final String TABLE_NAME_CHECKDATES = "checkdatestable";
    public static final String DROP_TABLE_CHECKDATES = "DROP TABLE IF EXISTS checkdatestable";


    public DBHelper(Context context){
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_BILLS);
        db.execSQL(CREATE_TABLE_CHECKDATES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE_BILLS);
        db.execSQL(DROP_TABLE_CHECKDATES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){
        onUpgrade(db, oldVersion, newVersion);
    }

    //Bill table functions
    //
    //
    public void insertBillRecord(BillData bill, int id, int size){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_ID, id);
        values.put(COLUMN_NAME, bill.getName());
        values.put(COLUMN_DAY, bill.getDay());
        values.put(COLUMN_AMOUNT, bill.getAmount());
        values.put(COLUMN_DESCRIPTION, bill.getDescription());

        long insertId = db.insert(TABLE_NAME_BILL, null, values);
        //could potentially return id if needed
    }

    //TODO: sort cards based on pay iteration
    public ArrayList<BillData> getAllBillRecords(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME_BILL, null, null, null, null, null, null);
        ArrayList<BillData> billList = new ArrayList<>();
        BillData bill;

        if(cursor.getCount() > 0){
            for( int i = 0; i < cursor.getCount(); i++){
                cursor.moveToNext();
                bill = new BillData();
                //cursor.getString(0) is ignored because that contains the id. we currently dont need it.
                bill.setName(cursor.getString(1));
                bill.setDay(cursor.getString(2));
                bill.setDescription(cursor.getString(3));
                bill.setAmount(cursor.getString(4));
                billList.add(bill);
            }
        }
        cursor.close();
        Collections.sort(billList, BillData.BillDayComparator );
        return billList;
    }

    public int getCountOfBillRecords(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME_BILL, null, null, null, null, null, null);
        return cursor.getCount();
    }

    //CheckDates table functions
    //
    //
    public void insertCheckDate(String date, int id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_DATE, date);
        values.put(COLUMN_ID_CHECKS, id);

        long insertId = db.insert(TABLE_NAME_CHECKDATES, null, values);
        //could potentially return id if needed
    }

    public int getCountOfCheckDates(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME_CHECKDATES, null, null, null, null, null, null);
        return cursor.getCount();
    }

    public ArrayList<String> getAllCheckDates(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME_CHECKDATES, null, null, null, null, null, null);
        ArrayList<String> checkDatesList = new ArrayList<>();
        String date;

        if(cursor.getCount() > 0){
            for( int i = 0; i < cursor.getCount(); i++){
                cursor.moveToNext();
                date = cursor.getString(1);
                checkDatesList.add(date);
            }
        }
        cursor.close();

        return checkDatesList;
    }

    public int clearCheckDateTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_NAME_CHECKDATES,"1", null);
        return rowsDeleted;
    }

}
