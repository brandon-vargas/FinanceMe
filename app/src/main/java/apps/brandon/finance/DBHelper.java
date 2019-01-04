package apps.brandon.finance;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "BillInfo.db";

    public static final String CREATE_TABLE_BILLS = "CREATE TABLE billtable "+
            "(id TEXT PRIMARY KEY, name TEXT, day TEXT, description TEXT, amount TEXT, idplus TEXT)";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DAY = "day";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_AMOUNT = "amount";
    public static final String COLUMN_ID_BILL = "idplus";
    public static final String TABLE_NAME_BILL = "billtable";
    public static final String DROP_TABLE_BILLS = "DROP TABLE IF EXISTS billtable";

    public static final String CREATE_TABLE_CHECKDATES = "CREATE TABLE checkdatestable"+
            "(id INTEGER PRIMARY KEY, date TEXT, used INTEGER)";
    public static final String COLUMN_DATE_CHECKS = "date";
    public static final String COLUMN_ID_CHECKS = "id";
    public static final String COLUMN_USED_CHECKS = "used";
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
    public void insertBillRecord(BillData bill){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_NAME, bill.getName());
        values.put(COLUMN_DAY, bill.getDay());
        values.put(COLUMN_AMOUNT, bill.getAmount());
        values.put(COLUMN_DESCRIPTION, bill.getDescription());
        values.put(COLUMN_ID_BILL, bill.getId());

        long insertId = db.insert(TABLE_NAME_BILL, null, values);
        Log.i("inserted id ", String.valueOf(insertId));
        //could potentially return id if needed
    }

    public int updateBillRecord(String id, String name, String day, String amount, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        String selection = "idplus=?";
        String[] selectionArgs = { id };
        Log.i("values = ", id + " " + name + " " + day + " " + amount + " " + description);
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_DAY, day);
        values.put(COLUMN_AMOUNT, amount);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_ID_BILL, id);

        Log.i("id desired for update", id);
        //TODO: this update is not working.
        int rowsAffected = db.update(TABLE_NAME_BILL, values, selection, selectionArgs);
        Log.i("updated? ", String.valueOf(rowsAffected));


        return rowsAffected;
    }

    public ArrayList<BillData> getAllBillRecords(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME_BILL, null, null, null, null, null, null);
        ArrayList<BillData> billList = new ArrayList<>();
        BillData bill;

        if(cursor.getCount() > 0){
            for( int i = 0; i < cursor.getCount(); i++){
                cursor.moveToNext();
//                for(int j = 0; j < 6; j++)
//                    Log.i("cursor info", cursor.getString(j));
                bill = new BillData();
                //cursor.getString(0) is ignored because that contains the id. we currently dont need it.
                bill.setName(cursor.getString(1));
                Log.i("Column : ", cursor.getColumnName(1));
                Log.i("Column n: ", cursor.getString(1));
                bill.setDay(cursor.getString(2));
                Log.i("Column : ", cursor.getColumnName(2));
                Log.i("Column n: ", cursor.getString(2));

                bill.setDescription(cursor.getString(3));
                Log.i("Column : ", cursor.getColumnName(3));
                Log.i("Column n: ", cursor.getString(3));
                bill.setAmount(cursor.getString(4));
                Log.i("Column : ", cursor.getColumnName(4));
                Log.i("Column n: ", cursor.getString(4));
                bill.setId(cursor.getString(5));
                Log.i("Column : ", cursor.getColumnName(5));
                Log.i("Column n: ", cursor.getString(5));
                billList.add(bill);
            }
        }
        Log.i("this is what i pull", billList.toString());
        cursor.close();
        //sorts based on bill day
//        Collections.sort(billList, BillData.BillDayComparator );
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
    public void insertCheckDate(String date, int id, int used){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_ID_CHECKS, id);
        values.put(COLUMN_DATE_CHECKS, date);
        values.put(COLUMN_USED_CHECKS, used);

        long insertId = db.insert(TABLE_NAME_CHECKDATES, null, values);
        //could potentially return id if needed
    }

    public int getCountOfCheckDates(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME_CHECKDATES, null, null, null, null, null, null);
        return cursor.getCount();
    }

    public ArrayList<CheckData> getAllCheckDates(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME_CHECKDATES, null, null, null, null, null, null);
        ArrayList<CheckData> checkDatesList = new ArrayList<>();
        String date;
        int used;

        if(cursor.getCount() > 0){
            for( int i = 0; i < cursor.getCount(); i++){
                cursor.moveToNext();
                date = cursor.getString(1);
                used = cursor.getInt(2);
                CheckData checkData = new CheckData(date, used);
                checkDatesList.add(checkData);
            }
        }
        cursor.close();

        return checkDatesList;
    }

    //TODO: make sure this works
    public int clearCheckDateTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_NAME_CHECKDATES,"1", null);
        return rowsDeleted;
    }

}
