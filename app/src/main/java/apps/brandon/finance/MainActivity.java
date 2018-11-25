package apps.brandon.finance;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class MainActivity extends AppCompatActivity implements DatePickerFragment.DateDialogListener{
    private RecyclerView mRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private GridLayoutManager gridLayoutManager;
    public MyAdapter myAdapter;
    private List<BillData> myBillList;
    private List<BillData> currentBillList;
    private List<BillData> nextBillList;
    public DBHelper myDBHelper;
    private SectionedRecyclerViewAdapter sectionAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDBHelper = new DBHelper(this);
        mRecyclerView = findViewById(R.id.recycler);
        sectionAdapter = new SectionedRecyclerViewAdapter();

        //Will only run once when the app is opened for the first time.
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean previouslyStarted = prefs.getBoolean("isFirstRun", false);
        if(!previouslyStarted) {
//            Toast.makeText(getBaseContext(), "asdfasdfasdfasdf", Toast.LENGTH_SHORT).show();
            SharedPreferences.Editor edit = prefs.edit();
            edit.putBoolean("isFirstRun", true);
            edit.commit();
            try {

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        gridLayoutManager = new GridLayoutManager(MainActivity.this, 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (sectionAdapter.getSectionItemViewType(position)){
                    case SectionedRecyclerViewAdapter.VIEW_TYPE_HEADER:
                        return 2;
                    default:
                        return 1;
                }
            }
        });
        mRecyclerView.setLayoutManager(gridLayoutManager);

        //TODO: maybe if theres no record count, use default place holders?
        if(myDBHelper.getCountOfBillRecords() == 0) {
            myBillList = new ArrayList<>();
            currentBillList = new ArrayList<>();
            nextBillList = new ArrayList<>();
        }
        else{
            myBillList = myDBHelper.getAllBillRecords();
            try{
                currentBillList = getFilteredBillRecords(myBillList, 1);
                nextBillList = getFilteredBillRecords(myBillList, 2);
            }catch (Exception e){e.printStackTrace();}
        }

        Log.i("size of my bills", String.valueOf(myDBHelper.getCountOfBillRecords()));
        Log.i("currentBill", currentBillList.toString());
        Log.i("nextBill",  nextBillList.toString());

        sectionAdapter.addSection(new CurrentPaySection(MainActivity.this,"Current Pay Cycle", currentBillList));
        sectionAdapter.addSection(new CurrentPaySection(MainActivity.this,"Next Pay Cycle", nextBillList));

        myAdapter = new MyAdapter(MainActivity.this, myBillList);
        mRecyclerView.setAdapter(sectionAdapter);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    //brings my menu to life. allows me to put actions on it
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);
        return true;
    }

    //how to handle action items being clicked
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_add was selected
            case R.id.action_add:
                if(myDBHelper.getCountOfCheckDates() == 0){
                    Toast.makeText(this, "You must first set up your check dates in the menu", Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent = new Intent(MainActivity.this, DetailedActivity.class);
                    MainActivity.this.startActivity(intent);
                }
                break;
            // action with ID action_minus was selected
            case R.id.action_minus:
                Toast.makeText(this, "This feature is not yet implemented", Toast.LENGTH_SHORT)
                        .show();
                break;
            case R.id.action_details:
                DatePickerFragment dialog = new DatePickerFragment();
                dialog.show(getSupportFragmentManager(), "MainActivity.DateDialog");
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onDestroy(){
        myDBHelper.close();
        super.onDestroy();
    }

    @Override
    public void onFinishDialog(Date date) {
        //delete the table first, then repopulate the data
        myDBHelper.clearCheckDateTable();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String checkDate = sdf.format(date);
        Calendar calendar = Calendar.getInstance();
        try{
            calendar.setTime(sdf.parse(checkDate));
        }catch (ParseException e){e.printStackTrace();}

        //adds current pay iteration's date
        calendar.add(Calendar.DATE, -14);
        String previousCheckDate = sdf.format(calendar.getTime());
        myDBHelper.insertCheckDate(previousCheckDate, 0, 1);

        int counterId = 1;
        //will add 100 more dates the checks will fall on. roughly about 3 years
        for(int i = 0; i < 100; i++){
            calendar.add(Calendar.DATE, 14);
            String nextCheckDate = sdf.format(calendar.getTime());
            myDBHelper.insertCheckDate(nextCheckDate,counterId, 0);
            counterId++;
        }
        Toast.makeText(getBaseContext(), "Initializing future check dates complete", Toast.LENGTH_LONG).show();

//        ArrayList<CheckData> finalDateList = myDBHelper.getAllCheckDates();
//        Log.i("Final dates",finalDateList.toString());
    }


    //Helper functions
    //
    //
    public ArrayList<BillData> getFilteredBillRecords(List<BillData> unfilteredList, int type) throws ParseException{
        ArrayList<BillData> billDataArrayList = new ArrayList<>();
        ArrayList<CheckData> checkDataArrayList = myDBHelper.getAllCheckDates();
        String payIterationString = determineCurrentPayIteration(checkDataArrayList);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Date todaysDate = new Date();
        String today = sdf.format(todaysDate);
        Calendar calendarToday = Calendar.getInstance();
        Calendar payIterationCal = Calendar.getInstance();
        try{
            calendarToday.setTime(sdf.parse(today));
            payIterationCal.setTime(sdf.parse(payIterationString));
        }catch (ParseException e){e.printStackTrace();}


        //i couldve used enums but that required extra dependencies so NAH
        //type is searching for current pay iteration bills
        if(type == 1){

            payIterationCal.add(Calendar.DATE, 14);
            for(int i = 0; i < 14; i++){
                payIterationCal.add(Calendar.DATE, -1);
                String day = sdf.format( payIterationCal.getTime()).split("/")[2];

                for(BillData billData: unfilteredList){
                    Integer billDay = Integer.parseInt(billData.getDay());
                    Integer calendarDay = Integer.parseInt(day);
                    if(billDay == calendarDay){
                        billDataArrayList.add(billData);
                    }
                }
            }
        }
        //type is searching for next pay iteration bills
        else if(type == 2){

            payIterationCal.add(Calendar.DATE, 28);
            for(int i = 0; i < 14; i++){
                payIterationCal.add(Calendar.DATE, -1);
                String day = sdf.format( payIterationCal.getTime()).split("/")[2];

                for(BillData billData: unfilteredList){
                    Integer billDay = Integer.parseInt(billData.getDay());
                    Integer calendarDay = Integer.parseInt(day);
                    if(billDay == calendarDay){
                        billDataArrayList.add(billData);
                    }
                }
            }
        }

        return billDataArrayList;
    }

    public String determineCurrentPayIteration(ArrayList<CheckData> checkDataArrayList){

        String currentPayDate = "";
        for( int i = 0; i < checkDataArrayList.size(); i++){

            if( checkDataArrayList.get(i).isUsed() == 1){
//                    Log.i("fasdfasdf","asdfasdfads");
                currentPayDate = checkDataArrayList.get(i).getDate();
            }else{
                break;
            }
        }

//        Log.i("current Pay Date", currentPayDate.getTime().toString());
        return currentPayDate;
    }

    //TODO: REMEMEBR, bills may fall on the same day
}
