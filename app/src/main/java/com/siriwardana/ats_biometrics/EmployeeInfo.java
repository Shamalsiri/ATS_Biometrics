package com.siriwardana.ats_biometrics;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.accutime.biometrics.BiometricError;
import com.accutime.biometrics.BiometricView;
import com.accutime.biometrics.InitializationListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EmployeeInfo extends AppCompatActivity {

    private final String TAG = "SHAMMY";
    private Button btnEditInfo, btnReEnrollBio, btnDone, btnDelete;
    private TextView tvID, tvFirstName, tvLastName, tvStreetAddress, tvCity, tvState, tvZipCode,
                    tvAge, tvSex, tvDepartment, tvRole;
    private String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_info);

        id = getIntent().getStringExtra("ID");

        btnEditInfo= (Button) findViewById(R.id.btn_edit_info);
        btnReEnrollBio = (Button) findViewById(R.id.btn_re_enroll_bio);
        btnDone = (Button) findViewById(R.id.btn_done);
        btnDelete = (Button) findViewById(R.id.btn_delete);

        tvID = (TextView) findViewById(R.id.tv_id);
        tvFirstName = (TextView) findViewById(R.id.tv_first_name);
        tvLastName = (TextView) findViewById(R.id.tv_last_name);
        tvStreetAddress = (TextView) findViewById(R.id.tv_street_address);
        tvCity = (TextView) findViewById(R.id.tv_city);
        tvState = (TextView) findViewById(R.id.tv_state);
        tvZipCode = (TextView) findViewById(R.id.tv_zip_code);
        tvAge = (TextView) findViewById(R.id.tv_age);
        tvSex = (TextView) findViewById(R.id.tv_sex);
        tvDepartment = (TextView) findViewById(R.id.tv_department);
        tvRole = (TextView)findViewById(R.id.tv_role);


        try {
            getEmployeeDetails();
        } catch (JSONException e) {
            Log.d(TAG, e.getMessage());
        }

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backHome();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteEmployee();
                backHome();
            }
        });

        btnReEnrollBio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reEnrollBio();
            }
        });

        btnEditInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Edit popup
            }
        });
    }

    /*
    * Get Employee details from the database
    * Show the retrieved employee info in the activity textview.
    * */
    private void getEmployeeDetails() throws JSONException {

        DB_Adapter db_adapter = new DB_Adapter(this);
        JSONObject employeeDetails = db_adapter.getEmployeeData(id);

        tvID.setText("ID Number: "+id);

        tvFirstName.setText(JSON_Adapter.getFirstName(employeeDetails));

        tvLastName.setText(JSON_Adapter.getLastName(employeeDetails));

        tvStreetAddress.setText(JSON_Adapter.getStreetAddress(employeeDetails));

        tvCity.setText(JSON_Adapter.getCity(employeeDetails));

        tvState.setText(JSON_Adapter.getState(employeeDetails));

        tvZipCode.setText(JSON_Adapter.getState(employeeDetails));

        tvAge.setText(JSON_Adapter.getAge(employeeDetails));

        tvSex.setText(JSON_Adapter.getSex(employeeDetails));

        tvDepartment.setText(JSON_Adapter.getDept(employeeDetails));

        tvRole.setText(JSON_Adapter.getRole(employeeDetails));

    }

    /* Starts the bio view activity in re-enroll mode
    *  id and mode are sent as extras
    * */
    private void reEnrollBio() {
        Intent intent = new Intent(this, BioViewActivity.class);
        intent.putExtra("MODE", "REENROLL");
        intent.putExtra("ID", id);
        startActivity(intent);
    }

    private void deleteEmployee() {
        // Delete from SQLite db
        DB_Adapter db_adapter = new DB_Adapter(this);
        db_adapter.deleteEntry(id);

        BiometricView bioView = new BiometricView(this);
        bioView.initialize(new InitializationListener() {
            @Override
            public void initializationStart() {

            }

            @Override
            public void initializationProgress(float v) {

            }

            @Override
            public void initializationComplete() {
                Log.d(TAG, "Bioview Initialize complete");

                List<String> idList = new ArrayList<>(1);
                idList.add(id);
                boolean deleted;

                Log.d(TAG,"Deleting "+id+" from Biometric Database");
                try{
                    deleted = bioView.deleteEnrollments(idList);
                    if(deleted){
                        Message.message(getApplicationContext(),  id+" deleted from bio db");
                        Log.d(TAG,id+" Deleted from biometric database");
                    }else{
                        Message.message(getApplicationContext(),  "Failed to  delete "+id+" from bio db");
                        Log.d(TAG,"Failed to  delete "+id+" from bio db");
                    }
                }catch (BiometricError e){
                    Log.d(TAG, e.getMessage());
                    e.printStackTrace();
                }
            }

            @Override
            public void initializationError(BiometricError biometricError) {

            }

            @Override
            public void initializationStatusMessage(String s) {

            }
        });
    }

    /*
    * Must be replace with Back trace
    * */
    private void backHome(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}