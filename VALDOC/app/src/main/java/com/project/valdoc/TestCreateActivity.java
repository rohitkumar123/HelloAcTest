package com.project.valdoc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.project.valdoc.db.ValdocDatabaseHandler;
import com.project.valdoc.intity.Ahu;
import com.project.valdoc.intity.ApplicableTestEquipment;
import com.project.valdoc.intity.ApplicableTestRoom;
import com.project.valdoc.intity.ClientInstrument;
import com.project.valdoc.intity.Equipment;
import com.project.valdoc.intity.PartnerInstrument;
import com.project.valdoc.intity.Room;
import com.project.valdoc.intity.RoomFilter;
import com.project.valdoc.intity.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TestCreateActivity extends Activity {
    private Spinner instrumentSpiner, equipmentOrAhuSpinner, equipmentSpinner, ahuSpinner, roomSpinner, testSpinner;
    private List<String> instrumentList, equipmentOrAhuTestList, ahuTestList, equipmentTestList, roomTestList, applicableTestRoomList, applicableTestEquipmentList;
    private ArrayAdapter<String> instrumentAdapter, equipmentOrAhuAdapter, equipmentadApter, ahuAdapter, roomAdapter, applicableTestRoomAdapter, applicableTestequipmentAdapter;
    private ArrayList<ClientInstrument> clientInstrumentArrayList = null;
    private ArrayList<PartnerInstrument> partnerInstrumentArrayList = null;
    private ArrayList<Ahu> mAhuArrayList = null;
    private ArrayList<Equipment> mEquipmentArrayList = null;
    private ArrayList<Room> mRoomArrayList = null;
    private ArrayList<ApplicableTestRoom> mApplicableTestRoomArrayList = null;
    private ArrayList<ApplicableTestEquipment> mApplicableTestEquipmentArrayList = null;

    private Button submit;
    private TextView user_name;
    private EditText witnessFirst;
    private EditText witnessSecond;
    private EditText witnessThird;
    private String userName = "";
    private String loginUserType = "";
    private String loginUserPartnerId = "";
    private int appUserId;
    private ValdocDatabaseHandler mValdocDatabaseHandler = new ValdocDatabaseHandler(TestCreateActivity.this);

    //spiner data storage
    private String spinerInstrument;
    private String spinerAhuOrEquipment;
    private int spinerAhu;
    private String spinerRoom;
    private String spinerTestType;
    private int spinerEquipment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        //get user name from login screen
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            userName = null;
            loginUserType = null;
        } else {
            userName = extras.getString("USERNAME");
            loginUserType = extras.getString("USERTYPE");
            appUserId = extras.getInt("APPUSERID");
        }
        //table creation
//        insertDataInTable();
        //instrument spinner creation
        getInstrumentList(loginUserType);
//Spinner Initialization
        listItemCreation();
        spinerInitialization();
        spinnerCreation();
        initButton();
    }

    private void initButton() {
        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validationSpiner()) {
                    if ("RD_AV_5".equals(spinerTestType = testSpinner.getSelectedItem().toString())) {
                        rdAv5Test();
                    } else if ("RD_ACPH_AV".equals(spinerTestType = testSpinner.getSelectedItem().toString())) {
                        rdAcphAv();
                    } else if ("RD_ACPH_H".equals(spinerTestType = testSpinner.getSelectedItem().toString())) {
                        rdAcphH();
                    } else if ("RD_FIT".equals(spinerTestType = testSpinner.getSelectedItem().toString())) {
                        rdFit();
                    } else if ("RD_PC_3".equals(spinerTestType = testSpinner.getSelectedItem().toString())) {
                        rdPc3();
                    } else if ("RD_RCT".equals(spinerTestType = testSpinner.getSelectedItem().toString())) {
                        rdRct();
                    }
                } else {
                    Toast.makeText(TestCreateActivity.this, "Please select the test to be performed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void rdAcphAv() {
        Intent intent = new Intent(TestCreateActivity.this, DynamicTableActivity.class);
        intent.putExtra("USERTYPE", loginUserType);
        intent.putExtra("USERNAME", userName);
        intent.putExtra("testType", "RD_ACPH_AV");
        Log.d("valdoc", "TestCreateActivity loginUserType=" + loginUserType);
        Log.d("valdoc", "TestCreateActivity userName=" + userName);

        Log.d("valdoc", "TestCreateActivity witness=" + witnessFirst.getText());
        intent.putExtra("WITNESSFIRST", witnessFirst.getText().toString());
        intent.putExtra("WITNESSSECOND", witnessSecond.getText().toString());
        intent.putExtra("WITNESSTHIRD", witnessThird.getText().toString());

        if (loginUserType.equals("CLIENT")) {
            intent.putExtra("ClientInstrument", clientInstrumentArrayList.get(instrumentSpiner.getSelectedItemPosition() - 1));
        } else {
            intent.putExtra("PartnerInstrument", partnerInstrumentArrayList.get(instrumentSpiner.getSelectedItemPosition() - 1));
        }

        //get room name,roomNo,and area id
        Log.d("valdoc", "TestCreateActivity :equipment:=" + roomSpinner.getSelectedItemPosition());
        Room room = mRoomArrayList.get(roomSpinner.getSelectedItemPosition() - 1);
        intent.putExtra("Room", room);
        intent.putExtra("AhuNumber", ahuSpinner.getSelectedItem().toString());
        //get area based on room area id
        String areaName = mValdocDatabaseHandler.getAreaByRoomAreaId("" + room.getAreaId());
        Log.d("valdoc", "TestCreateActivity areaName=" + areaName);
        intent.putExtra("AREANAME", areaName);

        ApplicableTestRoom applicableTestRoom = mApplicableTestRoomArrayList.get(testSpinner.getSelectedItemPosition() - 1);
        intent.putExtra("LOCATION", applicableTestRoom.getLocation());

        //get filter list from equipment filter
        Log.d("valdoc", "TestCreateActivity :equipment id equipment1:=" + room.getRoomId());
        ArrayList<HashMap<String, String>> grillAndSizeFromGrill = mValdocDatabaseHandler.getGrillAndSizeFromGrill(room.getRoomId());
        intent.putExtra("GRILLIST", grillAndSizeFromGrill);
        Log.d("valdoc", "TestCreateActivity :grill size:=" + grillAndSizeFromGrill.size());
        startActivity(intent);
    }

    private void rdAcphH() {
        Intent intent = new Intent(TestCreateActivity.this, DynamicTableActivity.class);
        intent.putExtra("USERTYPE", loginUserType);
        intent.putExtra("USERNAME", userName);
        intent.putExtra("testType", "RD_ACPH_H");
        Log.d("valdoc", "TestCreateActivity witness=" + witnessFirst.getText());
        intent.putExtra("WITNESSFIRST", witnessFirst.getText().toString());
        intent.putExtra("WITNESSSECOND", witnessSecond.getText().toString());
        intent.putExtra("WITNESSTHIRD", witnessThird.getText().toString());

        if (loginUserType.equals("CLIENT")) {
            intent.putExtra("ClientInstrument", clientInstrumentArrayList.get(instrumentSpiner.getSelectedItemPosition() - 1));
        } else {
            intent.putExtra("PartnerInstrument", partnerInstrumentArrayList.get(instrumentSpiner.getSelectedItemPosition() - 1));
        }

        //get room name,roomNo,and area id
        Log.d("valdoc", "TestCreateActivity :equipment:=" + roomSpinner.getSelectedItemPosition());
        Room room = mRoomArrayList.get(roomSpinner.getSelectedItemPosition() - 1);
        intent.putExtra("Room", room);
        intent.putExtra("AhuNumber", ahuSpinner.getSelectedItem().toString());
        //get area based on room area id
        String areaName = mValdocDatabaseHandler.getAreaByRoomAreaId("" + room.getAreaId());
        Log.d("valdoc", "TestCreateActivity areaName=" + areaName);
        intent.putExtra("AREANAME", areaName);

//            ApplicableTestRoom applicableTestRoom = mApplicableTestRoomArrayList.get(testSpinner.getSelectedItemPosition() - 1);
//            intent.putExtra("LOCATION", applicableTestRoom.getLocation());

        //get filter list from equipment filter
        Log.d("valdoc", "TestCreateActivity :equipment id equipment1:=" + room.getRoomId());
        ArrayList<HashMap<String, String>> grillAndSizeFromGrill = mValdocDatabaseHandler.getGrillAndSizeFromGrill(room.getRoomId());
        intent.putExtra("GRILLIST", grillAndSizeFromGrill);
        Log.d("valdoc", "TestCreateActivity :grill size:=" + grillAndSizeFromGrill.size());
        startActivity(intent);
    }

    private void rdFit() {
        Intent intent = new Intent(TestCreateActivity.this, DynamicTableActivity.class);
        intent.putExtra("USERTYPE", loginUserType);
        intent.putExtra("USERNAME", userName);
        intent.putExtra("testType", "RD_FIT");
        Log.d("valdoc", "TestCreateActivity witness=" + witnessFirst.getText());
        intent.putExtra("WITNESSFIRST", witnessFirst.getText().toString());
        intent.putExtra("WITNESSSECOND", witnessSecond.getText().toString());
        intent.putExtra("WITNESSTHIRD", witnessThird.getText().toString());

        if (loginUserType.equals("CLIENT")) {
            intent.putExtra("ClientInstrument", clientInstrumentArrayList.get(instrumentSpiner.getSelectedItemPosition() - 1));
        } else {
            intent.putExtra("PartnerInstrument", partnerInstrumentArrayList.get(instrumentSpiner.getSelectedItemPosition() - 1));
        }

        //get room name,roomNo,and area id
        Log.d("valdoc", "TestCreateActivity :equipment:=" + roomSpinner.getSelectedItemPosition());
        Room room = mRoomArrayList.get(roomSpinner.getSelectedItemPosition() - 1);
        intent.putExtra("Room", room);

        //take test specification from room filter
        ArrayList<RoomFilter> filterArrayList = mValdocDatabaseHandler.getFromRoomFilter(room.getRoomId());
        Log.d("valdoc", "TestCreateActivity filterArrayList=" + filterArrayList.size());
        intent.putExtra("RoomFilterList", filterArrayList);
        //TO Do testspesification will be shown from room filter spesification
        intent.putExtra("AhuNumber", ahuSpinner.getSelectedItem().toString());
        //get area based on room area id
        String areaName = mValdocDatabaseHandler.getAreaByRoomAreaId("" + room.getAreaId());
        Log.d("valdoc", "TestCreateActivity areaName=" + areaName);
        intent.putExtra("AREANAME", areaName);
        // location will be the size off rommfilter list
        startActivity(intent);
    }

    private void rdPc3() {
        Intent intent = new Intent(TestCreateActivity.this, DynamicTableActivity.class);
        intent.putExtra("USERTYPE", loginUserType);
        intent.putExtra("USERNAME", userName);
        intent.putExtra("testType", "RD_PC_3");
        Log.d("valdoc", "TestCreateActivity witness=" + witnessFirst.getText());
        intent.putExtra("WITNESSFIRST", witnessFirst.getText().toString());
        intent.putExtra("WITNESSSECOND", witnessSecond.getText().toString());
        intent.putExtra("WITNESSTHIRD", witnessThird.getText().toString());

        if (loginUserType.equals("CLIENT")) {
            intent.putExtra("ClientInstrument", clientInstrumentArrayList.get(instrumentSpiner.getSelectedItemPosition() - 1));
        } else {
            intent.putExtra("PartnerInstrument", partnerInstrumentArrayList.get(instrumentSpiner.getSelectedItemPosition() - 1));
        }

        //get room name,roomNo,and area id
        Log.d("valdoc", "TestCreateActivity :equipment:=" + roomSpinner.getSelectedItemPosition());
        Room room = mRoomArrayList.get(roomSpinner.getSelectedItemPosition() - 1);
        intent.putExtra("Room", room);
        intent.putExtra("AhuNumber", ahuSpinner.getSelectedItem().toString());

        ApplicableTestRoom applicableTestRoom = mApplicableTestRoomArrayList.get(testSpinner.getSelectedItemPosition() - 1);
        intent.putExtra("LOCATION", applicableTestRoom.getLocation());
        intent.putExtra("NOOFCYCLE", applicableTestRoom.getNoOfCycle());

        //get area based on room area id
        String areaName = mValdocDatabaseHandler.getAreaByRoomAreaId("" + room.getAreaId());
        Log.d("valdoc", "TestCreateActivity areaName=" + areaName);
        intent.putExtra("AREANAME", areaName);
        // location will be the size off rommfilter list
        startActivity(intent);
    }

    private void rdRct() {
        Intent intent = new Intent(TestCreateActivity.this, DynamicTableActivity.class);
        intent.putExtra("USERTYPE", loginUserType);
        intent.putExtra("USERNAME", userName);
        intent.putExtra("testType", "RD_RCT");
        Log.d("valdoc", "TestCreateActivity witness=" + witnessFirst.getText());
        intent.putExtra("WITNESSFIRST", witnessFirst.getText().toString());
        intent.putExtra("WITNESSSECOND", witnessSecond.getText().toString());
        intent.putExtra("WITNESSTHIRD", witnessThird.getText().toString());

        if (loginUserType.equals("CLIENT")) {
            intent.putExtra("ClientInstrument", clientInstrumentArrayList.get(instrumentSpiner.getSelectedItemPosition() - 1));
        } else {
            intent.putExtra("PartnerInstrument", partnerInstrumentArrayList.get(instrumentSpiner.getSelectedItemPosition() - 1));
        }

        //get room name,roomNo,and area id
        Log.d("valdoc", "TestCreateActivity :equipment:=" + roomSpinner.getSelectedItemPosition());
        Room room = mRoomArrayList.get(roomSpinner.getSelectedItemPosition() - 1);
        intent.putExtra("Room", room);
        intent.putExtra("AhuNumber", ahuSpinner.getSelectedItem().toString());

        ApplicableTestRoom applicableTestRoom = mApplicableTestRoomArrayList.get(testSpinner.getSelectedItemPosition() - 1);
        intent.putExtra("LOCATION", applicableTestRoom.getLocation());
        intent.putExtra("NOOFCYCLE", applicableTestRoom.getNoOfCycle());

        //get area based on room area id
        String areaName = mValdocDatabaseHandler.getAreaByRoomAreaId("" + room.getAreaId());
        Log.d("valdoc", "TestCreateActivity areaName=" + areaName);
        intent.putExtra("AREANAME", areaName);
        // location will be the size off rommfilter list
        startActivity(intent);
    }

    private void rdAv5Test() {
        Intent intent = new Intent(TestCreateActivity.this, DynamicTableActivity.class);
        intent.putExtra("USERTYPE", loginUserType);
        intent.putExtra("USERNAME", userName);
        intent.putExtra("testType", "RD_AV_5");
        if (loginUserType.equals("CLIENT")) {
            intent.putExtra("ClientInstrument", clientInstrumentArrayList.get(instrumentSpiner.getSelectedItemPosition() - 1));
        } else {
            intent.putExtra("PartnerInstrument", partnerInstrumentArrayList.get(instrumentSpiner.getSelectedItemPosition() - 1));
        }

        //get room name,roomNo,and area id
        Log.d("valdoc", "TestCreateActivity :equipment:=" + equipmentSpinner.getSelectedItemPosition());
        Equipment equipment = mEquipmentArrayList.get(equipmentSpinner.getSelectedItemPosition() - 1);
        String[] roomDetails = mValdocDatabaseHandler.getRoomByEquipment(equipment.getRoomId());
        intent.putExtra("RoomDetails", roomDetails);
        intent.putExtra("Equipment", equipment);
        //get filter list from equipment filter
        Log.d("valdoc", "TestCreateActivity :equipment id name equipment1:=" + equipment.getEquipmentName());
        Log.d("valdoc", "TestCreateActivity :equipment id equipment1:=" + equipment.getTestReference());
        String[] filterList = mValdocDatabaseHandler.getFilterFromEquipmentFilter(equipment.getEquipmentId());
        intent.putExtra("FILTERLIST", filterList);
        Log.d("valdoc", "TestCreateActivity :equipment1:=" + filterList.length);
        //get area based on room area id
        String areaName = mValdocDatabaseHandler.getAreaByRoomAreaId(roomDetails[2]);
        Log.d("valdoc", "TestCreateActivity areaName=" + areaName);
        intent.putExtra("AREANAME", areaName);
        Log.d("valdoc", "TestCreateActivity witness=" + witnessFirst.getText());
        intent.putExtra("WITNESSFIRST", witnessFirst.getText().toString());
        intent.putExtra("WITNESSSECOND", witnessSecond.getText().toString());
        intent.putExtra("WITNESSTHIRD", witnessThird.getText().toString());
        ApplicableTestEquipment applicableTestEquipment = mApplicableTestEquipmentArrayList.get(testSpinner.getSelectedItemPosition() - 1);
        intent.putExtra("LOCATION", applicableTestEquipment.getLocation());
        startActivity(intent);
    }

    //spiner data validation
    private boolean validationSpiner() {
        if (instrumentSpiner.getSelectedItemPosition() > 0) {
            if (equipmentOrAhuSpinner.getSelectedItem().toString().equals("AHU")) {
                if (ahuSpinner.getSelectedItemPosition() > 0 && roomSpinner.getSelectedItemPosition() > 0 && testSpinner.getSelectedItemPosition() > 0 && witnessFirst.getText().length() > 0)
                    return true;
            } else if (equipmentOrAhuSpinner.getSelectedItem().toString().equals("EQUIPMENT")) {
                if (equipmentSpinner.getSelectedItemPosition() > 0 && testSpinner.getSelectedItemPosition() > 0 && witnessFirst.getText().length() > 0)
                    return true;
            } else {
                return false;
            }
            return false;
        } else {
            return false;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void spinerInitialization() {
        user_name = (TextView) findViewById(R.id.user_name);
        user_name.setText(userName);
        instrumentSpiner = (Spinner) findViewById(R.id.instrumentspiner);
        equipmentOrAhuSpinner = (Spinner) findViewById(R.id.equipmentahuspinner);
        equipmentSpinner = (Spinner) findViewById(R.id.equipmentspinner);
        ahuSpinner = (Spinner) findViewById(R.id.ahuspinner);
        roomSpinner = (Spinner) findViewById(R.id.roomspinner);
        testSpinner = (Spinner) findViewById(R.id.testspinner);
        witnessFirst = (EditText) findViewById(R.id.witnessfirst);
        witnessSecond = (EditText) findViewById(R.id.witnesssecond);
        witnessThird = (EditText) findViewById(R.id.witnessthird);

    }

    public void listItemCreation() {

        equipmentOrAhuTestList = new ArrayList<String>();
        equipmentOrAhuTestList.add("Select Ahu or Equipment");
        equipmentOrAhuTestList.add("EQUIPMENT");
        equipmentOrAhuTestList.add("AHU");
//create ahu list
        createAhuList();
        createEquipmentList();
        roomTestList = new ArrayList<String>();
        roomTestList.add("Slect room");

        applicableTestRoomList = new ArrayList<String>();
        applicableTestRoomList.add("Select Test Type");
        applicableTestEquipmentList = new ArrayList<String>();
    }

    private void getInstrumentList(String userType) {
//        if(instrumentList.)
//        instrumentList.clear();
//        clientInstrumentArrayList.clear();
        instrumentList = new ArrayList<String>();
        instrumentList.add("Select Instrument");
        if (userType.equals("CLIENT")) {
            clientInstrumentArrayList = mValdocDatabaseHandler.getClientInstrumentInfo();

            for (ClientInstrument clientInstrument : clientInstrumentArrayList) {
                instrumentList.add(clientInstrument.getcInstrumentName());
                Log.d("valdoc", "TestCreateActivity : client" + clientInstrument.getcInstrumentName());
            }

        } else {
            Log.d("valdoc", "TestCreateActivity :vendor" + appUserId);
            loginUserPartnerId = mValdocDatabaseHandler.getPartnerIdFromPartnerUser(appUserId);
            Log.d("valdoc", "TestCreateActivity :vendor loginUserPartnerId=" + loginUserPartnerId);
            partnerInstrumentArrayList = mValdocDatabaseHandler.getPartnerInstrumentInfo(loginUserPartnerId);
            for (PartnerInstrument partnerInstrument : partnerInstrumentArrayList) {
                instrumentList.add(partnerInstrument.getpInstrumentName());
                Log.d("valdoc", "TestCreateActivity :vendor" + partnerInstrument.getpInstrumentName());
            }
        }


    }

    public void spinnerCreation() {

        instrumentAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, instrumentList);
        equipmentOrAhuAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, equipmentOrAhuTestList);
        equipmentadApter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, equipmentTestList);
        roomAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, roomTestList);
        applicableTestRoomAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, applicableTestRoomList);
        applicableTestequipmentAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, applicableTestEquipmentList);

        instrumentSpiner.setAdapter(instrumentAdapter);
        equipmentOrAhuSpinner.setAdapter(equipmentOrAhuAdapter);
        equipmentSpinner.setAdapter(equipmentadApter);
        ahuSpinner.setAdapter(ahuAdapter);
        roomSpinner.setAdapter(roomAdapter);
        testSpinner.setAdapter(applicableTestRoomAdapter);

        instrumentSpiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                if (instrumentSpiner.getSelectedItemPosition() > 1)
                    spinerInstrument = instrumentList.get(instrumentSpiner.getSelectedItemPosition() - 1);
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        equipmentOrAhuSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                ahuSpinner.setVisibility(View.GONE);
                equipmentSpinner.setVisibility(View.GONE);
                // TODO Auto-generated method stub
                if (equipmentOrAhuSpinner.getSelectedItem().toString().equals("AHU")) {
//                    createAhuList();
                    ahuSpinner.setVisibility(View.VISIBLE);
                    equipmentSpinner.setVisibility(View.GONE);
                    roomSpinner.setVisibility(View.VISIBLE);
//                    roomTestList.add("Select Room");
                    roomSpinner.setSelection(0);
                    ahuSpinner.setSelection(0);
                    testSpinner.setSelection(0);
                    spinerAhuOrEquipment = "AHU";
                    Log.d("TestCreateActivity", "spiner :");
                } else if (equipmentOrAhuSpinner.getSelectedItem().toString().equals("EQUIPMENT")) {
                    ahuSpinner.setVisibility(View.GONE);
                    equipmentSpinner.setVisibility(View.VISIBLE);
                    roomSpinner.setVisibility(View.GONE);
                    testSpinner.setSelection(0);
                    spinerAhuOrEquipment = "EQUIPMENT";
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });


        equipmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                Log.d("TestCreateActivity", "equipment :index= " + equipmentSpinner.getSelectedItemPosition() + "mEquipmentArrayList size=" + mEquipmentArrayList.size());
                if (equipmentSpinner.getSelectedItemPosition() > 0) {
                    Equipment equipment = mEquipmentArrayList.get(equipmentSpinner.getSelectedItemPosition() - 1);
                    createApplicableTestEquipmentList(equipment.getEquipmentId());
                    spinerEquipment = equipment.getEquipmentId();
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        ahuSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                roomSpinner.setVisibility(View.VISIBLE);
                roomSpinner.setSelection(0);
                testSpinner.setSelection(0);
                Log.d("TestCreateActivity", "ahuspiner :position= " + ahuSpinner.getSelectedItemPosition());

                if (ahuSpinner.getSelectedItemPosition() > 0) {
                    Ahu ahu = mAhuArrayList.get(ahuSpinner.getSelectedItemPosition() - 1);
                    Log.d("TestCreateActivity", "ahuspiner : ahu.getAhuId()=" + ahu.getAhuId());
                    createRoomList(ahu.getAhuId());
                    spinerAhu = ahu.getAhuId();
//                    roomSpinner.setVisibility(View.VISIBLE);
                }
//                Toast.makeText(getBaseContext(), ahuTestList.get(arg2),
//                        Toast.LENGTH_SHORT).show();
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
        roomSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                Log.d("TestCreateActivity", "equipment :index= " + roomSpinner.getSelectedItemPosition());
                if (roomSpinner.getSelectedItemPosition() > 0) {
                    Room room = mRoomArrayList.get(roomSpinner.getSelectedItemPosition() - 1);
                    createApplicableTestRoomList(room.getRoomId());
                    spinerRoom = room.getRoomName();
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        testSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                spinerTestType = testSpinner.getSelectedItem().toString();
//                Toast.makeText(getBaseContext(), TestList.get(arg2),
//                        Toast.LENGTH_SHORT).show();
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });


    }


    //create ahu list
    private void createAhuList() {
        ahuTestList = new ArrayList<String>();
        ahuTestList.add("Select ahu");
        mAhuArrayList = mValdocDatabaseHandler.getAhuInfo();
        Log.d("valdoc", "TestCreateActivity : ahu size" + mAhuArrayList.size());
        for (Ahu ahu : mAhuArrayList) {
            ahuTestList.add(ahu.getAhuId() + "");
            Log.d("valdoc", "TestCreateActivity : ahu" + ahu.getAhuType());
        }
        ahuAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, ahuTestList);
//        ahuSpinner.setAdapter(ahuAdapter1);
        ahuAdapter.notifyDataSetChanged();
    }


    private void createEquipmentList() {
        equipmentTestList = new ArrayList<String>();
        if (null != equipmentTestList && equipmentTestList.size() > 0) {
            equipmentTestList.clear();
        }
        equipmentTestList.add("Select Equipment");
        mEquipmentArrayList = mValdocDatabaseHandler.getEquipmentInfo();

        for (Equipment equipment : mEquipmentArrayList) {
            equipmentTestList.add(equipment.getEquipmentNo());
            Log.d("valdoc", "TestCreateActivity : ahu" + equipment.getEquipmentName());
        }
        equipmentadApter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, equipmentTestList);
//        equipmentSpinner.setAdapter(equipmentadApter);
        equipmentadApter.notifyDataSetChanged();
    }


    private void createRoomList(int ahuNo) {
        roomSpinner.setVisibility(View.VISIBLE);
        Log.d("TestCreateActivity", "ahuspiner : create room list=");
        if (null != mRoomArrayList && mRoomArrayList.size() > 0)
            mRoomArrayList.clear();
        mRoomArrayList = mValdocDatabaseHandler.getRoomInfoByAhu(ahuNo);
        if (null != roomTestList && roomTestList.size() > 1) {
            Log.d("valdoc", "TestCreateActivity : room clear");
            roomTestList.clear();
            roomTestList.add("Select Room");
        }
        roomSpinner.setSelection(0);
        for (Room room : mRoomArrayList) {
            roomTestList.add(room.getRoomName());
            Log.d("valdoc", "TestCreateActivity : ahu" + room.getRoomName());
        }
        roomAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, roomTestList);
//        roomSpinner.setAdapter(roomAdapter);
        roomAdapter.notifyDataSetChanged();
    }


    private void createApplicableTestRoomList(int roomId) {
        Log.d("TestCreateActivity", "ahuspiner : create room list=");
        if (null != mApplicableTestRoomArrayList && mApplicableTestRoomArrayList.size() > 0)
            mApplicableTestRoomArrayList.clear();
        mApplicableTestRoomArrayList = mValdocDatabaseHandler.getApplicableTestRoomInfo(roomId);
        if (null != applicableTestRoomList && applicableTestRoomList.size() > 0) {
            applicableTestRoomList.clear();
            applicableTestRoomAdapter.notifyDataSetChanged();
        }
        applicableTestRoomList.add("Select Test Type");
        for (ApplicableTestRoom applicableTestRoom : mApplicableTestRoomArrayList) {
            applicableTestRoomList.add(applicableTestRoom.getTestName());
            Log.d("valdoc", "TestCreateActivity : ahu" + applicableTestRoom.getTestName());
        }
        applicableTestRoomAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, applicableTestRoomList);
        testSpinner.setAdapter(applicableTestRoomAdapter);
        applicableTestRoomAdapter.notifyDataSetChanged();
    }

    private void createApplicableTestEquipmentList(int equipmentId) {
        Log.d("TestCreateActivity", "ahuspiner : create room list=");
        if (null != mApplicableTestEquipmentArrayList && mApplicableTestEquipmentArrayList.size() > 0)
            mApplicableTestEquipmentArrayList.clear();
        mApplicableTestEquipmentArrayList = mValdocDatabaseHandler.getApplicableTestEquipmentInfo(equipmentId);
        if (null != applicableTestEquipmentList && applicableTestEquipmentList.size() > 0) {
            applicableTestEquipmentList.clear();
            applicableTestRoomAdapter.notifyDataSetChanged();
        }
        applicableTestEquipmentList.add("Select Test Type");
        for (ApplicableTestEquipment applicableTestEquipment : mApplicableTestEquipmentArrayList) {
            applicableTestEquipmentList.add(applicableTestEquipment.getTestName());
            Log.d("valdoc", "TestCreateActivity : applicableTestEquipment" + applicableTestEquipment.getTestName());
        }
        applicableTestequipmentAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, applicableTestEquipmentList);
        testSpinner.setAdapter(applicableTestequipmentAdapter);
        applicableTestequipmentAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        Intent afterLoginIntent = new Intent(TestCreateActivity.this, AfterLoginActivity.class);
        startActivity(afterLoginIntent);
        TestCreateActivity.this.finish();
    }
}
