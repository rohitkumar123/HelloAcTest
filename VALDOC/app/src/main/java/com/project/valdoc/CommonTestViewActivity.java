package com.project.valdoc;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.project.valdoc.db.ValdocDatabaseHandler;
import com.project.valdoc.intity.RoomFilter;
import com.project.valdoc.intity.TestDetails;
import com.project.valdoc.intity.TestReading;
import com.project.valdoc.utility.Utilityies;

import java.util.ArrayList;
import java.util.HashMap;

public class CommonTestViewActivity extends AppCompatActivity {
    private static final String TAG = "CommonTestViewActivity";
    //Test 1 View...
    TableLayout table_layout, table_layout2, table_layout3, table_layout4;
    //Test 2 View ...
    TableLayout test2_table_layout, test2_table_layout2, test2_table_layout3, test2_table_layout4,
            test2_table_layout5, test2_table_layout6, test2_table_layout7, test2_table_layout8;
    //Test 3 View ...
    TableLayout test3_table_layout, test3_table_layout2, test3_table_layout3, test3_table_layout4,
            test3_table_layout5;
    //Test 4 View ...
    TableLayout test4_table_layout, test4_table_layout2, test4_table_layout3, test4_table_layout4,
            test4_table_layout5, test4_table_layout6, test4_table_layout7, test4_table_layout8;
    //Test 5 View ...
    TableLayout test5_table_layout, test5_table_layout2, test5_table_layout2_1, test5_tableLayout2_2,
            test5_table_layout3, test5_table_layout4, test5_tableLayout4_2, test5_table_layout4_1,
            test5_table_layout5, test5_table_layout5_1, test5_table_layout3_1;
    //Test 6 View ...
    TableLayout test6A_table_layout, test6A_table_layout2, test6A_table_layout3,
            test6A_table_layout4;
    TextView finalReadingTv;
    EditText finalReadingValueTv;
    ArrayList<TextView> txtViewList;
    ArrayList<TextView> txtPassFailList;
    ArrayList<TextView> resultTextViewList;
    ArrayList<TextView> gridTextList;
    TestDetails mTestDetails;
    ProgressDialog pr;
    private String userName = "";
    String testType = null;
    SharedPreferences sharedpreferences;
    private ValdocDatabaseHandler mValdocDatabaseHandler;
    private int rows, cols, testDetailId = 1;
    private String mTestBasedOn;
    private ArrayList<TestReading> testReadingList;
    //certificate view id creation
    private TextView instrumentUsed;
    private TextView equipmentName;
    private TextView equipmentNo;
    private TextView instrumentSerialNo;
    private TextView calibrationOn;
    private TextView calibrationDueOn;
    private TextView testSpecification;
    private TextView plantName;
    private TextView areaOfTest;
    private TextView roomName;
    private TextView roomNo;
    private TextView occupancyState;
    private TextView testRefrance;
    private TextView infarance;
    private TextView testCundoctor;
    private TextView testWitnessOrg;
    private TextView testCondoctorOrg;
    private TextView testWitness;
    private TextView dateTextView;
    private TextView customerName;
    private TextView certificateNo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_test_view);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        pr = ProgressDialog.show(CommonTestViewActivity.this, "Please Wait", "Loading...");
        pr.setCancelable(true);
        pr.setCanceledOnTouchOutside(true);

        mValdocDatabaseHandler = new ValdocDatabaseHandler(CommonTestViewActivity.this);
        testReadingList = new ArrayList<TestReading>();

        txtViewList = new ArrayList<TextView>();
        txtPassFailList = new ArrayList<TextView>();
        resultTextViewList = new ArrayList<TextView>();
        gridTextList = new ArrayList<TextView>();

        sharedpreferences = getSharedPreferences("valdoc", Context.MODE_PRIVATE);
        userName = sharedpreferences.getString("USERNAME", "");

        //Custom Action Bar
        ActionBar mActionBar = getSupportActionBar();
        if (mActionBar != null)
            Utilityies.setCustomActionBar(CommonTestViewActivity.this, mActionBar, userName);

        testType = getIntent().getStringExtra("TestType");
        testDetailId = getIntent().getIntExtra("testDetailId", 1);
        rows = getIntent().getIntExtra("rows", 6);
        cols = getIntent().getIntExtra("cols", 5);
        mTestBasedOn=getIntent().getStringExtra("TestBasedOn");
        Log.d(TAG, " TestType : " + testType + " testDetailId " + testDetailId);

        initRes();
        initTextView();
        String spiltValue[] = null;
        if (testType != null && testType.contains("AV")) {
            findViewById(R.id.test1_table_ll).setVisibility(View.VISIBLE);
            testReadingList = mValdocDatabaseHandler.getTestReadingDataById(testDetailId + "");
            spiltValue = testReadingList.get(0).getValue().split(",");
            Log.d(TAG, "CodeFlow : spiltValue length : " + spiltValue.length);
            BuildTable(testReadingList.size()+1,(spiltValue.length-2));
            //input Data
                int textId =0;
                for (int j = 0; j < testReadingList.size(); j++) {
                    Log.d(TAG, "CodeFlow : outerForLoop J: " + j);
                    //filter
                    gridTextList.get(j).setText(testReadingList.get(j).getEntityName());
                    //Average
                    resultTextViewList.get(j).setText(""+spiltValue[spiltValue.length-2]);
                    if("PASS".equalsIgnoreCase(spiltValue[spiltValue.length-1])){
                        resultTextViewList.get(j).setTextColor(ContextCompat.getColor(this, R.color.blue));
                    }else{
                        resultTextViewList.get(j).setTextColor(ContextCompat.getColor(this, R.color.red));
                    }
                    //Results pass/fail
                    //txtPassFailList.get(j).setText(""+spiltValue[spiltValue.length-1]);
                    //V1, V2, v3.. Value..
                    spiltValue =testReadingList.get(j).getValue().split(",");
                    for (int i = 0; i <spiltValue.length-2; i++) {
                        txtViewList.get(textId).setText(""+spiltValue[i]);
                        Log.d(TAG, "CodeFlow : InnerForLoop I: " + i+" textId "+textId);
                        textId++;

                    }

                }

        } else if (testType != null && testType.contains("AF")) {
            findViewById(R.id.test2_table_ll).setVisibility(View.VISIBLE);
            BuildTableTest2(rows, cols);
        } else if (testType != null && testType.contains("ACPH_H")) {
            findViewById(R.id.test3_table_ll).setVisibility(View.VISIBLE);
            BuildTableTest3(rows, cols);
        } else if (testType != null && testType.contains("FIT")) {
            findViewById(R.id.test4_table_ll).setVisibility(View.VISIBLE);
            BuildTableTest4(rows, cols);
        } else if (testType != null && testType.contains("PCT")) {
            findViewById(R.id.test5_table_ll).setVisibility(View.VISIBLE);
            BuildTableTest5(rows, cols);
        } else if (testType != null && testType.contains("RCT")) {
            Toast.makeText(CommonTestViewActivity.this, "Coming Soon...", Toast.LENGTH_SHORT).show();
        }
        mTestDetails = mValdocDatabaseHandler.getTestDetailById(testDetailId);

        //cancel button click
        findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        textViewValueAsignment();
    }

    private void textViewValueAsignment() {
        dateTextView.setText(""+mTestDetails.getDateOfTest());
        customerName.setText(""+mTestDetails.getCustomer());
        certificateNo.setText(""+mTestDetails.getRawDataNo());
        instrumentUsed.setText(""+mTestDetails.getInstrumentUsed());

//        make = (TextView) findViewById(R.id.make);
//        model = (TextView) findViewById(R.id.modle);
        instrumentSerialNo.setText(""+mTestDetails.getInstrumentNo());
        calibrationOn.setText(""+mTestDetails.getCalibratedOn());
        calibrationDueOn.setText(""+mTestDetails.getCalibratedDueOn());
        testSpecification.setText(""+mTestDetails.getTestSpecification());
        plantName.setText(""+mTestDetails.getBlockName());
        areaOfTest.setText(""+mTestDetails.getTestArea());

//        roomNameLable = (TextView) findViewById(R.id.ahu_no_lable);
//        roomNameLable.setText(getResources().getString(R.string.room_name));
        roomName.setText(""+mTestDetails.getRoomName());
        roomNo.setText(""+mTestDetails.getRoomNo());
        equipmentName.setText(""+mTestDetails.getEquipmentName());
        equipmentNo.setText(""+mTestDetails.getEquipmentNo());
        occupancyState.setText(""+mTestDetails.getOccupencyState());
        testRefrance.setText(""+mTestDetails.getTestReference());
        infarance.setText("");
        testCundoctor.setText(""+mTestDetails.getTesterName());
        testCondoctorOrg.setText(""+mTestDetails.getTestCondoctorOrg());
        testWitnessOrg.setText(""+mTestDetails.getTestWitnessOrg());
        testWitness.setText(""+mTestDetails.getWitnessName());

        String clientOrg = sharedpreferences.getString("CLIENTORG", "");
        String prtnerOrg = sharedpreferences.getString("PARTNERORG", "");
        if (sharedpreferences.getString("USERTYPE", "").equalsIgnoreCase("CLIENT")) {
            testCondoctorOrg.setText("(" + clientOrg + ")");
            testWitnessOrg.setText("(" + clientOrg + ")");
            customerName.setText(""+clientOrg);
        } else {
            testCondoctorOrg.setText("(" + prtnerOrg + ")");
            testWitnessOrg.setText("(" + clientOrg + ")");
            customerName.setText(""+prtnerOrg);
        }
        plantName.setText("from cofig screen");
    }

    private void initTextView() {
        dateTextView = (TextView) findViewById(R.id.datetextview);
        customerName = (TextView) findViewById(R.id.customer_name);
        certificateNo = (TextView) findViewById(R.id.trd_no);
        instrumentUsed = (TextView) findViewById(R.id.instrumentused);

//        make = (TextView) findViewById(R.id.make);
//        model = (TextView) findViewById(R.id.modle);
        instrumentSerialNo = (TextView) findViewById(R.id.instrumentserialno);
        calibrationOn = (TextView) findViewById(R.id.calibratedon);
        calibrationDueOn = (TextView) findViewById(R.id.calibrationdueon);
        testSpecification = (TextView) findViewById(R.id.testspecification);
        plantName = (TextView) findViewById(R.id.plantname);
        areaOfTest = (TextView) findViewById(R.id.areaoftest);

//        roomNameLable = (TextView) findViewById(R.id.ahu_no_lable);
//        roomNameLable.setText(getResources().getString(R.string.room_name));
        roomName = (TextView) findViewById(R.id.room_name);
        roomNo = (TextView) findViewById(R.id.room_no);
        equipmentName = (TextView) findViewById(R.id.equiment_name);
        equipmentNo = (TextView) findViewById(R.id.equiment_no);
        occupancyState = (TextView) findViewById(R.id.ocupancystate);
        testRefrance = (TextView) findViewById(R.id.testrefrence);
        infarance = (TextView) findViewById(R.id.infarance);
        testCundoctor = (TextView) findViewById(R.id.testcunducter);
        testCondoctorOrg = (TextView) findViewById(R.id.test_condoctor_org);
        testWitnessOrg = (TextView) findViewById(R.id.testwitness_org);
        testWitness = (TextView) findViewById(R.id.testwitness);

    }

    private void BuildTable(int rows, int cols) {
        //first section
        // outer for loop
        for (int i = 1; i <= rows; i++) {
            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));

            // inner for loop
            for (int j = 1; j <= 1; j++) {
                if (i == 1 && j == 1) {
                    row.addView(addTextView("Grille / Filter ID"));
                } else {
                    row.addView(addGridTextView(""));
                }

            }

            table_layout.addView(row);
        }

        //Second section
        // outer for loop
        for (int i = 1; i <= rows; i++) {

            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));

            // inner for loop
            for (int j = 1; j <= cols; j++) {
                if (i == 1 && j <= cols) {
                    row.addView(addTextView(" V " + j));
                } else {
                    //text view for user input
                    row.addView(addInputDataTextView());
                }
            }
            table_layout2.addView(row);
        }


        //third section
        // outer for loop
        for (int i = 1; i <= rows; i++) {
            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            // inner for loop
            for (int j = 1; j <= 1; j++) {
                if (i == 1 && j == 1) {
                    row.addView(addTextView(" Average(V) "));
                } else {
                    //result data  set
                    row.addView(addResultTextView(i));
                }
            }
            table_layout3.addView(row);

        }
        //fourth section
        // outer for loop
        for (int i = 1; i <= rows; i++) {
            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            // inner for loop
            for (int j = 1; j <= 1; j++) {
                if (i == 1 && j == 1) {
                    row.addView(addTextView(" Result "));
                } else {
                    //row.addView(addTextView(" PASS "));
                    row.addView(addTextPassFail(" ", i));
                }
            }
            table_layout4.addView(row);
        }

        //dismiss progressbar
        if (pr.isShowing())
            pr.dismiss();

    }

    private void BuildTableTest2(int rows, int cols) {
        //first section
        // outer for loop
        for (int i = 1; i <= rows; i++) {
            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            // inner for loop
            for (int j = 1; j <= 1; j++) {
                if (i == 1 && j == 1) {
                    row.addView(addTextView(" Grille / Filter ID\n "));
                } else {
                    row.addView(addTextView("grillAndSizeFromGrill"));
//                    row.addView(addTextView("AHU 2031/0.3MICRON/" + i));
                }

            }
            test2_table_layout.addView(row);

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
                    row.addView(addTextView(" Grill/Filter Size\n in ft2(A)"));
                } else {
                    row.addView(addResultTextView(i));
                }
            }
            test2_table_layout2.addView(row);

        }

        //Third section
        // outer for loop
        for (int i = 1; i <= rows; i++) {

            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));

            // inner for loop
            for (int j = 1; j <= cols; j++) {
                if (i == 1 && j <= cols) {
                    row.addView(addTextView(" V " + j + "\n "));
                } else {
                    //row.addView(addEditTextView());
                    row.addView(addInputDataTextView());
                }
            }
            test2_table_layout3.addView(row);
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
                    row.addView(addTextView(" Avg Velocity in\n fpm(AV)"));
                } else {
                    //result data  set
                    row.addView(addResultTextView(i));
                }
            }
            test2_table_layout4.addView(row);

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
                    row.addView(addTextView(" Air Flow Rate\n in cfm(AxAv)"));
                } else {
                    row.addView(addTextView("490"));
                }
            }
            test2_table_layout5.addView(row);

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
                    row.addView(addTextView(" Total Air Flow Rate\n in cfm (TFR)"));
                } else {
                    row.addView(addTextViewWithoutBorder("490"));
                }
            }
            test2_table_layout6.addView(row);
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
                    row.addView(addTextView(" Room Volume in\n ft3(RV)"));
                } else {
                    row.addView(addTextViewWithoutBorder("490"));
                }
            }
            test2_table_layout7.addView(row);
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
                    row.addView(addTextView("No. of Air Changes/Hr\n ((TFR/RV)x60))"));
                } else {
                    row.addView(addTextViewWithoutBorder("490"));
                }
            }
            test2_table_layout8.addView(row);
        }

        //dismiss progressbar
        if (pr.isShowing())
            pr.dismiss();

    }

    private void BuildTableTest3(int rows, int cols) {
        //first section
        // outer for loop
        for (int i = 1; i <= rows; i++) {
            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            // inner for loop
            for (int j = 1; j <= 1; j++) {
                if (i == 1 && j == 1) {
                    row.addView(addTextView(" Grille/Filter ID No\n "));
                } else {
                    row.addView(addTextView("grillAndSizeFromGrill"));
//                    row.addView(addTextView(" Filter No " + i));
                }
            }
            test3_table_layout.addView(row);
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
                    row.addView(addTextView(" Measured Supply Air\n Velocity in cfm (in cfm) "));
                } else {
                    //row.addView(addTextView(" "));
                    row.addView(addInputDataTextView());
                }
            }
            test3_table_layout2.addView(row);
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
                    row.addView(addTextView(" Total Air Flow Rate\n in cfm (TFR)"));
                } else {
                    row.addView(addTextViewWithoutBorder("0"));
                }
            }
            test3_table_layout3.addView(row);
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
                    row.addView(addTextView(" Room Volume in\n ft3(RV)"));
                } else {
                    row.addView(addTextViewWithoutBorder("" + 678));
                }
            }
            test3_table_layout4.addView(row);
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
                    row.addView(addTextView("No. of Air Changes/Hr\n ((TFR/RV)x60))"));
                } else {
                    row.addView(addTextViewWithoutBorder("490"));
                }
            }
            test3_table_layout5.addView(row);
        }

        //dismiss progressbar
        if (pr.isShowing())
            pr.dismiss();


    }

    private void BuildTableTest4(int rows, int cols) {
        //first section
        // outer for loop
        for (int i = 1; i <= rows; i++) {
            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            // inner for loop
            for (int j = 1; j <= 1; j++) {
                if (i == 1 && j == 1) {
                    row.addView(addTextView(" Filter No \n         "));
                } else {
                    row.addView(addTextView("HF -00" + i));
                }

            }
            test4_table_layout.addView(row);
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
                    row.addView(addTextView("Average before Scanning\n Concentration (µg/liter) "));
                } else {
                    row.addView(addTextView(30 + i + ""));
                }

            }
            test4_table_layout2.addView(row);
        }

        //Third section section
        // outer for loop
        for (int i = 1; i <= rows; i++) {
            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            // inner for loop
            for (int j = 1; j <= 1; j++) {
                if (i == 1 && j == 1) {
                    row.addView(addTextView(" Average After Scanning\nConcentration (µg/liter) "));
                } else {
                    row.addView(addTextView(20 + i + ""));
                }

            }
            test4_table_layout3.addView(row);
        }

        //fourth section
        // outer for loop
        for (int i = 1; i <= rows; i++) {
            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            // inner for loop
            for (int j = 1; j <= 1; j++) {
                if (i == 1 && j == 1) {
                    row.addView(addTextView(" Variation \nin Concentration*"));
                } else {
                    row.addView(addTextView(10 + i + "%"));
                }

            }
            test4_table_layout4.addView(row);
        }
        //fifth section
        // outer for loop
        for (int i = 1; i <= rows; i++) {
            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            // inner for loop
            for (int j = 1; j <= 1; j++) {
                if (i == 1 && j == 1) {
                    row.addView(addTextView(" Obtained Leakage \n(% Leakage)"));
                } else {
                    row.addView(addTextView(3 + i + ""));
                    //row.addView(addEditTextView(i));
                }

            }
            test4_table_layout5.addView(row);
        }

        //dismiss progressbar
        if (pr.isShowing())
            pr.dismiss();

    }

    private void BuildTableTest5(int rows, int cols) {
        //first section
        // outer for loop
        for (int i = 1; i <= rows; i++) {
            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            // inner for loop
            for (int j = 1; j <= 1; j++) {
                if (i == 1 && j == 1) {

                    TextView textView = addTextView(" Location ");
                    ViewGroup.LayoutParams params = textView.getLayoutParams();
                    params.height = getResources().getDimensionPixelSize(R.dimen.text_view_height);
                    textView.setLayoutParams(params);
                    row.addView(textView);
                } else {
                    int position = i - 1;
                    row.addView(addTextView(" " + position));
                }

            }
            test5_table_layout.addView(row);
        }
        for (int sk = 0; sk < 3; sk++) {
            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            row.addView(addTextView("    "));
            test5_table_layout.addView(row);
        }

// adding no of partical text
//        if(i==1) {
        TableRow row1 = new TableRow(this);
        row1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.MATCH_PARENT));
        TextView tvs = addTextView(" No. of Particles >= 5 µm/m³  ");
        tvs.setEms(12);
        row1.addView(tvs);
        test5_tableLayout2_2.addView(row1);
//        test5_table_layout2_1.addView(row1);
//        }


        //Second section
        // outer for loop
        for (int i = 1; i <= rows; i++) {
            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));

            // inner for loop 1
            for (int j = 1; j <= cols; j++) {
                if (i == 1 && j <= cols) {
                    row.addView(addTextView(" R " + j));
                } else {
                    //row.addView(addTextView(" 4434 | 3434 | 1341 "));
                    row.addView(addInputDataTextView());
                }
            }
            test5_table_layout2_1.addView(row);
        }
        for (int sk = 0; sk < 3; sk++) {
            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            if (sk == 0) {
                row.addView(addStretchedTextView(" Mean Average "));
            }
            if (sk == 1) {
                row.addView(addStretchedTextView(" Standard Deviation "));
            }
            if (sk == 2) {
                row.addView(addStretchedTextView(" 95% UCL "));
            }
            test5_table_layout2.addView(row);
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
                    TextView textView = addTextView(" Average ");
                    ViewGroup.LayoutParams params = textView.getLayoutParams();
                    params.height = getResources().getDimensionPixelSize(R.dimen.text_view_height);
                    textView.setLayoutParams(params);
                    row.addView(textView);
                } else {
                    row.addView(addResultTextView(i));
                }

            }
            test5_table_layout3.addView(row);
        }
        //Footer Rows....
        for (int sk = 0; sk < 3; sk++) {
            TableRow rowFooter = new TableRow(this);
            rowFooter.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            rowFooter.addView(addTextView("fuygfyu"));
            test5_table_layout3_1.addView(rowFooter);
        }


        TableRow row2 = new TableRow(this);
        row2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.MATCH_PARENT));
        TextView tvs1 = addTextView(" No. of Particles >= 5 µm/m³  ");
        tvs1.setEms(12);
        row2.addView(tvs1);
        test5_tableLayout4_2.addView(row2);

        //Fourth section
        // outer for loop
        for (int i = 1; i <= rows; i++) {
            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            // inner for loop 1
            for (int j = 1; j <= cols; j++) {
                if (i == 1 && j <= cols) {
                    row.addView(addTextView(" R " + j));
                } else {
                    row.addView(addInputDataTextView());
                }

            }
            test5_table_layout4_1.addView(row);

        }
        for (int sk = 0; sk < 3; sk++) {
            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            if (sk == 0) {
                row.addView(addStretchedTextView(" Mean Average "));
            }
            if (sk == 1) {
                row.addView(addStretchedTextView(" Standard Deviation "));
            }
            if (sk == 2) {
                row.addView(addStretchedTextView(" 95% UCL "));
            }
//            row.addView(addStretchedTextView(" Mean Average  "));
            test5_table_layout4.addView(row);
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
                    TextView textView = addTextView(" Average ");
                    ViewGroup.LayoutParams params = textView.getLayoutParams();
                    params.height = getResources().getDimensionPixelSize(R.dimen.text_view_height);
                    textView.setLayoutParams(params);
                    row.addView(textView);
                } else {
                    row.addView(addResultTextView(rows + i));
                }

            }
            test5_table_layout5.addView(row);
        }
        //Footer Rows....
        for (int sk = 0; sk < 3; sk++) {
            TableRow rowFooter = new TableRow(this);
            rowFooter.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            rowFooter.addView(addTextView("cdsswwe232"));
            test5_table_layout5_1.addView(rowFooter);
        }

    }


    private TextView addTextViewWithoutBorder(String s) {
        TextView tv = new TextView(this);
        tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT));
        //tv.setBackgroundResource(R.drawable.border);
        tv.setPadding(5, 5, 5, 5);
        tv.setTextColor(getResources().getColor(R.color.black));
        tv.setTextSize(getResources().getDimension(R.dimen.normal_text_size));
        //tv.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
        tv.setSingleLine(true);
        tv.setEllipsize(TextUtils.TruncateAt.END);
        txtViewList.add(tv);
        tv.setText(s);
        return tv;
    }

    private TextView addTextView(String textValue) {
        TextView tv = new TextView(this);
        tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT));
        tv.setBackgroundResource(R.drawable.border1);
        tv.setGravity(Gravity.CENTER);
        //tv.setPadding(5, 5, 5, 5);
        tv.setTextColor(getResources().getColor(R.color.black));
        tv.setTextSize(getResources().getDimension(R.dimen.normal_text_size));
        tv.setGravity(Gravity.CENTER);
        //tv.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
        tv.setSingleLine(false);
        tv.setMaxLines(3);
        tv.setEllipsize(TextUtils.TruncateAt.END);
        tv.setText(textValue);
        return tv;
    }

    private TextView addGridTextView(String textValue) {
        TextView tv = new TextView(this);
        tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT));
        tv.setBackgroundResource(R.drawable.border1);
        tv.setGravity(Gravity.CENTER);
        //tv.setPadding(5, 5, 5, 5);
        tv.setTextColor(getResources().getColor(R.color.black));
        tv.setTextSize(getResources().getDimension(R.dimen.normal_text_size));
        tv.setGravity(Gravity.CENTER);
        //tv.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
        tv.setSingleLine(false);
        tv.setMaxLines(3);
        tv.setEllipsize(TextUtils.TruncateAt.END);
        gridTextList.add(tv);
        tv.setText(textValue);
        return tv;
    }

    int idCountEtv = 200;

    private TextView addInputDataTextView() {
        TextView tv = new TextView(this);
        tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT));
        tv.setBackgroundResource(R.drawable.border1);
        //tv.setPadding(5, 5, 5, 5);
        tv.setTextColor(getResources().getColor(R.color.black));
        tv.setTextSize(getResources().getDimension(R.dimen.normal_text_size));
        tv.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        //tv.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
        tv.setId(idCountEtv);
        tv.setGravity(Gravity.CENTER);
        tv.setSingleLine(false);
        tv.setMaxLines(3);
        tv.setEms(4);
        tv.setEllipsize(TextUtils.TruncateAt.END);
        idCountEtv++;
        txtViewList.add(tv);
        Log.d(TAG, "CodeFlow idCountEtv " + idCountEtv);
        return tv;
    }

    int idPassFailTv = 300;

    private TextView addTextPassFail(String textValue, int tagRows) {
        TextView tv = new TextView(this);
        tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT));
        tv.setBackgroundResource(R.drawable.border1);
        //tv.setPadding(5, 5, 5, 5);
        tv.setTextColor(getResources().getColor(R.color.black));
        tv.setTextSize(getResources().getDimension(R.dimen.normal_text_size));
        //tv.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
        tv.setSingleLine(false);
        tv.setTag(tagRows);
        tv.setId(idPassFailTv);
        tv.setMaxLines(3);
        tv.setEllipsize(TextUtils.TruncateAt.END);
        tv.setText(textValue);
        tv.setGravity(Gravity.CENTER);
        idPassFailTv++;
        txtPassFailList.add(tv);
        return tv;
    }

    int idCountTv = 1;

    private TextView addResultTextView(int rowsNo) {
        TextView tv = new TextView(this);
        tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT));
        tv.setBackgroundResource(R.drawable.border);
        tv.setPadding(5, 6, 5, 6);
        tv.setTextColor(getResources().getColor(R.color.black));
        tv.setTextSize(getResources().getDimension(R.dimen.normal_text_size));
        tv.setGravity(Gravity.CENTER);
        tv.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        //tv.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
        tv.setEms(4);
        tv.setSingleLine(true);
        tv.setEllipsize(TextUtils.TruncateAt.END);
        Log.d(TAG, " idCountTv " + idCountTv);
        tv.setId(idCountTv);
        tv.setTag(rowsNo);
        idCountTv++;
        resultTextViewList.add(tv);
        return tv;
    }

    private TextView addStretchedTextView(String textValue) {
        TextView tv = new TextView(this);
        tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT));
        tv.setBackgroundResource(R.drawable.border1);
        //tv.setPadding(5, 5, 5, 5);
        tv.setTextColor(getResources().getColor(R.color.black));
        tv.setTextSize(getResources().getDimension(R.dimen.normal_text_size));
        //tv.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
        tv.setSingleLine(false);
        if (rows != 0)
            tv.setEms(4 * rows);
        else
            tv.setEms(12);
        tv.setMaxLines(3);
        tv.setEllipsize(TextUtils.TruncateAt.END);
        tv.setText(textValue);
        return tv;
    }

    private void initRes() {
        findViewById(R.id.submit).setVisibility(View.GONE);
        findViewById(R.id.clear).setVisibility(View.GONE);
        //Test 1
        table_layout = (TableLayout) findViewById(R.id.tableLayout1);
        table_layout2 = (TableLayout) findViewById(R.id.tableLayout2);
        table_layout3 = (TableLayout) findViewById(R.id.tableLayout3);
        table_layout4 = (TableLayout) findViewById(R.id.tableLayout4);
        table_layout4.setVisibility(View.GONE);
        if (testType != null && (testType.contains("AV") || testType.contains("AF"))) {
            findViewById(R.id.test_table_1_header_l_ll).setVisibility(View.GONE);
            TextView TestHeader = (TextView) findViewById(R.id.common_header_tv);
            findViewById(R.id.test_interference).setVisibility(View.GONE);
            findViewById(R.id.test1_reading_header).setVisibility(View.VISIBLE);
            findViewById(R.id.common_header_test1).setVisibility(View.GONE);
        }
        TextView TestHeader = (TextView) findViewById(R.id.common_header_tv);
        TextView TestHeader2 = (TextView) findViewById(R.id.common_header_2_tv);
        TestHeader.setText("TEST RAW DATA EQUIPMENT");
        TestHeader2.setVisibility(View.VISIBLE);
        TestHeader2.setText("(Air Flow Velocity, Volume Testing and Determination of Air Changes per Hour Rates)");
        if(testType != null && testType.contains("AF")){
            findViewById(R.id.acph_av_final_calc_ll).setVisibility(View.VISIBLE);
            findViewById(R.id.acph_av_final_calc_ll).setVisibility(View.GONE);
            findViewById(R.id.common_certificate_header_ll).setVisibility(View.VISIBLE);
            TestHeader.setText(" TEST RAW DATA AHU/EQUIPMENT ");
            TestHeader2.setText("(Air Flow Velocity/ Volume Testing)");
        }else if(testType != null && testType.contains("AV")){
            TestHeader.setText("TEST RAW DATA ");
            findViewById(R.id.acph_av_final_calc_ll).setVisibility(View.VISIBLE);
            findViewById(R.id.common_certificate_header_ll).setVisibility(View.VISIBLE);
            findViewById(R.id.ahu_no_lable).setVisibility(View.VISIBLE);
            findViewById(R.id.room_volume_table).setVisibility(View.VISIBLE);
        }

        //Test 2
        test2_table_layout = (TableLayout) findViewById(R.id.test2_tableLayout1);
        test2_table_layout2 = (TableLayout) findViewById(R.id.test2_tableLayout2);
        test2_table_layout3 = (TableLayout) findViewById(R.id.test2_tableLayout3);
        test2_table_layout4 = (TableLayout) findViewById(R.id.test2_tableLayout4);
        test2_table_layout5 = (TableLayout) findViewById(R.id.test2_tableLayout5);
        test2_table_layout6 = (TableLayout) findViewById(R.id.test2_tableLayout6);
        test2_table_layout7 = (TableLayout) findViewById(R.id.test2_tableLayout7);
        test2_table_layout8 = (TableLayout) findViewById(R.id.test2_tableLayout8);
        //Test3
        test3_table_layout = (TableLayout) findViewById(R.id.test3_tableLayout1);
        test3_table_layout2 = (TableLayout) findViewById(R.id.test3_tableLayout2);
        test3_table_layout3 = (TableLayout) findViewById(R.id.test3_tableLayout3);
        test3_table_layout4 = (TableLayout) findViewById(R.id.test3_tableLayout4);
        test3_table_layout5 = (TableLayout) findViewById(R.id.test3_tableLayout5);
        //Test4
        test4_table_layout = (TableLayout) findViewById(R.id.test4_tableLayout1);
        test4_table_layout2 = (TableLayout) findViewById(R.id.test4_tableLayout2);
        test4_table_layout3 = (TableLayout) findViewById(R.id.test4_tableLayout3);
        test4_table_layout4 = (TableLayout) findViewById(R.id.test4_tableLayout4);
        test4_table_layout5 = (TableLayout) findViewById(R.id.test4_tableLayout5);
        test4_table_layout6 = (TableLayout) findViewById(R.id.test4_tableLayout6);
        test4_table_layout7 = (TableLayout) findViewById(R.id.test4_tableLayout7);
        test4_table_layout8 = (TableLayout) findViewById(R.id.test4_tableLayout8);
        //Test5
        test5_table_layout = (TableLayout) findViewById(R.id.test5_tableLayout1);
        test5_table_layout2 = (TableLayout) findViewById(R.id.test5_tableLayout2);
        test5_table_layout2_1 = (TableLayout) findViewById(R.id.test5_tableLayout2_1);
        test5_tableLayout2_2 = (TableLayout) findViewById(R.id.test5_tableLayout2_2);
        test5_table_layout3 = (TableLayout) findViewById(R.id.test5_tableLayout3);
        test5_table_layout3_1 = (TableLayout) findViewById(R.id.test5_tableLayout3_1);
        test5_table_layout4 = (TableLayout) findViewById(R.id.test5_tableLayout4);
        test5_tableLayout4_2 = (TableLayout) findViewById(R.id.test5_tableLayout4_2);
        test5_table_layout4_1 = (TableLayout) findViewById(R.id.test5_tableLayout4_1);
        test5_table_layout5 = (TableLayout) findViewById(R.id.test5_tableLayout5);
        test5_table_layout5_1 = (TableLayout) findViewById(R.id.test5_tableLayout5_1);
        //Test6
        test6A_table_layout = (TableLayout) findViewById(R.id.test6A_tableLayout1);
        test6A_table_layout2 = (TableLayout) findViewById(R.id.test6A_tableLayout2);
        test6A_table_layout3 = (TableLayout) findViewById(R.id.test6A_tableLayout3);
        test6A_table_layout4 = (TableLayout) findViewById(R.id.test6A_tableLayout4);
        finalReadingTv = (TextView) findViewById(R.id.test6_final_reading_tv);
        finalReadingValueTv = (EditText) findViewById(R.id.test6_final_reading_value_tv);

    }

}
