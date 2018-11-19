package apps.brandon.finance;

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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DatePickerFragment.DateDialogListener{
    private RecyclerView mRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private GridLayoutManager gridLayoutManager;
    public MyAdapter myAdapter;
    private List<BillData> myBillList;
    private BillData billData;
    public DBHelper myDBHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDBHelper = new DBHelper(this);
        mRecyclerView = findViewById(R.id.recycler);

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
        mRecyclerView.setLayoutManager(gridLayoutManager);

        if(myDBHelper.getCountOfBillRecords() == 0) myBillList = new ArrayList<>();
        else myBillList = myDBHelper.getAllBillRecords();

        myAdapter = new MyAdapter(MainActivity.this, myBillList);
        mRecyclerView.setAdapter(myAdapter);
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
                //will add an empyt Bill object to the list
                //and notify the adapter listen to create new card
                myBillList.add(new BillData());
                if(myBillList.size() == 0) myAdapter.notifyItemInserted(0);
                else myAdapter.notifyItemInserted(myBillList.size()-1);

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
        myDBHelper.insertCheckDate(previousCheckDate, 0);

        int counterId = 1;
        //will add 100 more dates the checks will fall on. roughly about 3 years
        for(int i = 0; i < 100; i++){
            calendar.add(Calendar.DATE, 14);
            String nextCheckDate = sdf.format(calendar.getTime());
            myDBHelper.insertCheckDate(nextCheckDate,counterId);
            counterId++;
        }
        Toast.makeText(getBaseContext(), "Initializing future check dates complete", Toast.LENGTH_LONG).show();
        ArrayList<String> finalDateList = myDBHelper.getAllCheckDates();
        Log.i("Final dates",finalDateList.toString());
    }

}
