package apps.brandon.finance;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class DetailedActivity extends AppCompatActivity {

    public EditText eName;
    public EditText eAmount;
    public EditText eDay;
    public EditText eDescription;
    public ImageView image;
    public Button saveButton;
    public Button cancelButton;
    public int id;
    public int size;
    public DBHelper myDBHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        myDBHelper = new DBHelper(this);

        eName = findViewById(R.id.detailed_name);
        eAmount = findViewById(R.id.detailed_amount);
        eDay = findViewById(R.id.detailed_day);
        eDescription = findViewById(R.id.detailed_description);
//        image = findViewById(R.id.detailed_image);
        saveButton = findViewById(R.id.save_button);
        cancelButton = findViewById(R.id.cancel_button);
        id = 0;
        size = 0;

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            eName.setText(bundle.getString("title"));
//            image.setImageResource(bundle.getInt("image"));
            eAmount.setText(bundle.getString("amount"));
            eDay.setText(bundle.getString("day"));
            eDescription.setText(bundle.getString("description"));
            eDay.setText(bundle.getString("day"));
            id = bundle.getInt("id");
            size = bundle.getInt("size");
            Log.i("asdfasd", String.valueOf(id));
            Log.i("zxcvzcxv", String.valueOf(size));
        }
    }

    public void SaveButtonClicked(View view){
        Intent intent = new Intent(view.getContext(), MainActivity.class);
        //you could pass data between intents if you wanted to. BUT ITS NOT EFFICIENT.

        BillData bill = new BillData(eName.getText().toString(), eDay.getText().toString(), eDescription.getText().toString(), eAmount.getText().toString());
        myDBHelper.insertBillRecord(bill, id, size);

        startActivity(intent);
    }

    //upon hitting cancel button, the activity will be destroyed. as if it never were open.
    public void CancelButtonClicked(View view){
        finish();
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getBaseContext(), "Either save or cancel action", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy(){
        myDBHelper.close();
        super.onDestroy();
    }
}