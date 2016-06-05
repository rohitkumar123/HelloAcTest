package com.project.valdoc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.project.valdoc.db.ValdocDatabaseHandler;
import com.project.valdoc.intity.TestDetails;
import com.project.valdoc.utility.Utilityies;

import java.util.ArrayList;

public class SyncSelectedDataActivity extends AppCompatActivity {
    private static final String TAG = "SyncSelectedData";
    TableLayout table_layout1, table_layout2, table_layout3, table_layout4, table_layout5,
            table_layout6, table_layout7, table_layout8, table_layout9, table_layout10;
    int rows =5, colos =5;
    private String userName = "",testType = "";
    SharedPreferences sharedpreferences;
    private ValdocDatabaseHandler mValdocDatabaseHandler;
    private ArrayList<TestDetails> testDetailList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_selected_data);

        mValdocDatabaseHandler = new ValdocDatabaseHandler(SyncSelectedDataActivity.this);
        sharedpreferences = getSharedPreferences("valdoc", Context.MODE_PRIVATE);
        userName = sharedpreferences.getString("USERNAME", "");
        testDetailList = new ArrayList<TestDetails>();

        if(getIntent().hasExtra("rows")){
           rows = getIntent().getIntExtra("rows",6);
            colos = getIntent().getIntExtra("cols",6);
        }
        testType = getIntent().getStringExtra("TestType");
        Log.d(TAG, " Code TestType : " + testType);

        //Custom Action Bar
        ActionBar mActionBar = getSupportActionBar();
        if (mActionBar != null)
            Utilityies.setCustomActionBar(SyncSelectedDataActivity.this, mActionBar, userName);


        initRes();
        if(getTestDataByTestCode(testType).size()>0){
            BuildSyncDataTable(getTestDataByTestCode(testType).size(),colos);
        }else{
            findViewById(R.id.no_data_found_tv).setVisibility(View.VISIBLE);
        }
    }

    private ArrayList<TestDetails> getTestDataByTestCode(String testType) {
        ArrayList<TestDetails>testDetailsArrayList = new ArrayList<TestDetails>();
        if(testType.contains("ACPH_AV")){
            testDetailsArrayList = mValdocDatabaseHandler.getTestDetailByTestCode("ACPH_AV");
        }else if(testType.contains("ACPH_H")){
            testDetailsArrayList =  mValdocDatabaseHandler.getTestDetailByTestCode("ACPH_H");
        }else if(testType.contains("FIT")){
            testDetailsArrayList = mValdocDatabaseHandler.getTestDetailByTestCode("FIT");
        }else if(testType.contains("PCT")){
            testDetailsArrayList = mValdocDatabaseHandler.getTestDetailByTestCode("PCT");
        }else if(testType.contains("RCT")){
            testDetailsArrayList = mValdocDatabaseHandler.getTestDetailByTestCode("RCT");
        }else if(testType.contains("ARD_AF_AHU")){
            testDetailsArrayList = mValdocDatabaseHandler.getTestDetailByTestCode("ARD_AF_AHU");
        }else if(testType.contains("ARD_FIT_AHU")){
            testDetailsArrayList =  mValdocDatabaseHandler.getTestDetailByTestCode("ARD_FIT_AHU");
        }else if(testType.contains("ERD_AV")){
            testDetailsArrayList = mValdocDatabaseHandler.getTestDetailByTestCode("ERD_AV");
        }else if(testType.contains("ERD_FIT")){
            testDetailsArrayList =  mValdocDatabaseHandler.getTestDetailByTestCode("ERD_FIT");
        }else if(testType.contains("ERD_PCT")){
            testDetailsArrayList = mValdocDatabaseHandler.getTestDetailByTestCode("ERD_PCT");
        }else if(testType.contains("ERD_RCT")){
            testDetailsArrayList = mValdocDatabaseHandler.getTestDetailByTestCode("ERD_RCT");
        }
        return testDetailsArrayList;
    }

    private void BuildSyncDataTable(int rows, int cols) {
        //first section
        // outer for loop
        for (int i = 1; i <= rows; i++) {
            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));

            // inner for loop
            for (int j = 1; j <= 1; j++) {
                if (i == 1 && j == 1) {
                    row.addView(addTextView("SI # "));
                } else {
                    row.addView(addTextView(i-1+""));
                }

            }

            table_layout1.addView(row);
        }

        //Second section
        // outer for loop
        for (int i = 1; i <= rows; i++) {
            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            // inner for loop
            for (int j = 1; j <= 1; j++) {
                if (i == 1 && j == 1) {
                    row.addView(addTextView("Select "));
                } else {
                    row.addView(addCheckBox(1+i));
                }

            }
            table_layout2.addView(row);
        }
        //Third section
        // outer for loop
        for (int i = 1; i <= rows; i++) {
            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));

            // inner for loop
            for (int j = 1; j <= 1; j++) {
                if (i == 1 && j == 1) {
                    row.addView(addTextView("    RD NO     "));
                } else {
                    row.addView(addTextView(""+testDetailList.get(i-2).getRawDataNo()));
                }

            }

            table_layout3.addView(row);
        }
        //Fourth section
        // outer for loop
        for (int i = 1; i <= rows; i++) {
            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));

            // inner for loop
            for (int j = 1; j <= 1; j++) {
                if (i == 1 && j == 1) {
                    row.addView(addTextView("   Date       "));
                } else {
                    row.addView(addTextView(""+testDetailList.get(i-2).getDateOfTest()));
                }

            }

            table_layout4.addView(row);
        }
        //Fifth section
        // outer for loop
        for (int i = 1; i <= rows; i++) {
            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));

            // inner for loop
            for (int j = 1; j <= 1; j++) {
                if (i == 1 && j == 1) {
                    row.addView(addTextView("   Area Name    "));
                } else {
                    row.addView(addTextView(""+testDetailList.get(i-2).getTestArea()));
                }

            }

            table_layout5.addView(row);
        }
        //Sixth section
        // outer for loop
        for (int i = 1; i <= rows; i++) {
            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));

            // inner for loop
            for (int j = 1; j <= 1; j++) {
                if (i == 1 && j == 1) {
                    row.addView(addTextView("   AHU No   "));
                } else {
                    row.addView(addTextView(""+testDetailList.get(i-2).getAhuNo()));
                }

            }

            table_layout6.addView(row);
        }
        //Seventh section
        // outer for loop
        for (int i = 1; i <= rows; i++) {
            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));

            // inner for loop
            for (int j = 1; j <= 1; j++) {
                if (i == 1 && j == 1) {
                    row.addView(addTextView("   Room Name    "));
                } else {
                    row.addView(addTextView(""+testDetailList.get(i-2).getRoomName()));
                }

            }

            table_layout7.addView(row);
        }
        //Eight section
        // outer for loop
        for (int i = 1; i <= rows; i++) {
            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));

            // inner for loop
            for (int j = 1; j <= 1; j++) {
                if (i == 1 && j == 1) {
                    row.addView(addTextView("   Room No   "));
                } else {
                    row.addView(addTextView(""+testDetailList.get(i-2).getRoomNo()));
                }

            }

            table_layout8.addView(row);
        }
        //Ninth section
        // outer for loop
        for (int i = 1; i <= rows; i++) {
            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));

            // inner for loop
            for (int j = 1; j <= 1; j++) {
                if (i == 1 && j == 1) {
                    row.addView(addTextView("  Results  "));
                } else {
                    row.addView(addTextView(""));
                }

            }

            table_layout9.addView(row);
        }
        //Tenth section
        // outer for loop
        for (int i = 1; i <= rows; i++) {
            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));

            // inner for loop
            for (int j = 1; j <= 1; j++) {
                if (i == 1 && j == 1) {
                    row.addView(addTextView("  Action   "));
                } else {
                    row.addView(addActionTextView("View", i));
                }

            }

            table_layout10.addView(row);
        }



    }

    private void initRes() {
        table_layout1 = (TableLayout) findViewById(R.id.syncData_tableLayout1);
        table_layout2 = (TableLayout) findViewById(R.id.syncData_tableLayout2);
        table_layout3 = (TableLayout) findViewById(R.id.syncData_tableLayout3);
        table_layout4 = (TableLayout) findViewById(R.id.syncData_tableLayout4);
        table_layout5 = (TableLayout) findViewById(R.id.syncData_tableLayout5);
        table_layout6 = (TableLayout) findViewById(R.id.syncData_tableLayout6);
        table_layout7 = (TableLayout) findViewById(R.id.syncData_tableLayout7);
        table_layout8 = (TableLayout) findViewById(R.id.syncData_tableLayout8);
        table_layout9 = (TableLayout) findViewById(R.id.syncData_tableLayout9);
        table_layout10 = (TableLayout) findViewById(R.id.syncData_tableLayout10);

    }
    private TextView addTextView(String textValue) {
        TextView tv = new TextView(this);
        tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT));
        tv.setBackgroundResource(R.drawable.border1);
        tv.setGravity(Gravity.CENTER);
        //tv.setPadding(5,5,5,5);
        tv.setHeight(70);
        tv.setTextColor(getResources().getColor(R.color.black));
        //tv.setTextSize(getResources().getDimension(R.dimen.normal_text_size1));
        tv.setSingleLine(false);
        tv.setMaxLines(3);
        tv.setEllipsize(TextUtils.TruncateAt.END);
        tv.setText(textValue);
        return tv;
    }
    private TextView addActionTextView(String view, int rowId) {
        TextView tv = new TextView(this);
        tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT));
        tv.setBackgroundResource(R.drawable.border1);
        tv.setGravity(Gravity.CENTER);
        tv.setTextColor(getResources().getColor(R.color.black));
        tv.setSingleLine(false);
        tv.setHeight(70);
        tv.setMaxLines(3);
        tv.setEllipsize(TextUtils.TruncateAt.END);
        tv.setOnClickListener(new ViewActionValidator(SyncSelectedDataActivity.this,rowId));
        tv.setText(view);
        return tv;
    }
    private CheckBox  addCheckBox(int row){
        CheckBox cb = new CheckBox(this);
        cb.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT));
        cb.setBackgroundResource(R.drawable.border1);
        cb.setHeight(70);
        cb.setGravity(Gravity.CENTER);
        cb.setId(row);
        cb.setTag(row+1);
        cb.setOnCheckedChangeListener(new CheckedValidator(SyncSelectedDataActivity.this,row));
        return  cb;
    }

    private class CheckedValidator implements CompoundButton.OnCheckedChangeListener {
        int checkBpxId;
        Context mContext;

        public CheckedValidator(Context context, int row) {
            this.checkBpxId = row;
            this.mContext = context;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked)
                Toast.makeText(mContext, "CheckBox : "+checkBpxId+" Checked", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(mContext, "CheckBox : "+checkBpxId+" Unchecked", Toast.LENGTH_SHORT).show();

        }
    }

    private class ViewActionValidator implements View.OnClickListener {
        int viewTextId;
        Context mContext;

        public ViewActionValidator(Context context, int rowId) {
            this.mContext = context;
            this.viewTextId = rowId;

        }

        @Override
        public void onClick(View v) {
            Toast.makeText(mContext, testType+"View Row : "+viewTextId+" Test", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SyncSelectedDataActivity.this,CommonTestViewActivity.class);
            intent.putExtra("TestType",testType);
            intent.putExtra("rows",6);
            intent.putExtra("cols",5);
            startActivity(intent);
        }
    }
}
