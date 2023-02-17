/*
* Engineer  : Shamal Siriwardana
* Date      : 02-01-2023
*/

package com.siriwardana.ats_biometrics;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.Manifest.permission.CAMERA;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.accutime.biometrics.BiometricError;
import com.accutime.biometrics.BiometricView;
import com.accutime.biometrics.InitializationListener;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int APP_PERMISSION_CODE = 5000;
    private static final String TAG = "SHAMMY";

    private Button btnEmployee, btnDBSize, btnDeleteDB, btnDeleteID;

    private TextInputEditText etID;
    private LinearLayout llManagerOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Full Screen Flags
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        //checks for permission
        checkUserPermission();

        btnEmployee = (Button) findViewById(R.id.btn_employee);
        btnDBSize = (Button) findViewById(R.id.btn_db_size);
        btnDeleteDB = (Button) findViewById(R.id.btn_deleteDb);
        btnDeleteID = (Button) findViewById(R.id.btn_deleteID);

        etID = (TextInputEditText) findViewById(R.id.et_eid);

        llManagerOptions = (LinearLayout) findViewById(R.id.ll_manager_options);

        btnEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, BioViewActivity.class);
                Intent intent = new Intent(MainActivity.this, BioViewActivity.class);
                intent.putExtra("BUTTON", "IDENTIFY");
                startActivity(intent);
            }
        });

        btnEmployee.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(llManagerOptions.getVisibility() == View.VISIBLE){
                    Log.d(TAG, "Setting Manager options to invisible");
                    llManagerOptions.setVisibility(View.INVISIBLE);
                } else{
                    llManagerOptions.setVisibility(View.VISIBLE);
                    Log.d(TAG, "Setting Manager options to visible");
                }
                return true;
            }
        });

        btnDeleteDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DB_Adapter db_adapter = new DB_Adapter(MainActivity.this);
                db_adapter.deleteTable();
                db_adapter.createTable();

                BiometricView bioView = new BiometricView(MainActivity.this);

                bioView.initialize(new InitializationListener() {
                    @Override
                    public void initializationStart() {

                    }

                    @Override
                    public void initializationProgress(float v) {

                    }

                    @Override
                    public void initializationComplete() {
                        Log.d(TAG, "Biometric View Initialized");
                        boolean deleted;
                        try {
                            Log.d(TAG, "Extracting list of IDs");
                            List<String> idList = bioView.getEnrollmentIds();
                            Log.d(TAG, "Deleting Database");
                            deleted = bioView.deleteEnrollments(idList);
                            if(deleted){
                                Log.d(TAG, "Database Deleted");
                                Message.message(getApplicationContext(), "Database Deleted");
                            }else{
                                Log.d(TAG,"Failed to delete database");
                                Message.message(getApplicationContext(), "Failed to delete database");
                            }
                        } catch (BiometricError e) {
                            Log.d(TAG, e.getMessage());

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
        });

        btnDBSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DB_Adapter db_adapter = new DB_Adapter(MainActivity.this);
                Message.message(MainActivity.this, "Database Size: "+db_adapter.getNumEntries());
                Log.d(TAG, "SQLite DB Size: "+db_adapter.getNumEntries());
            }
        });

        btnDeleteID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = etID.getText().toString().trim();

                // Delete from SQL db
                DB_Adapter db_adapter = new DB_Adapter(getApplicationContext());
                db_adapter.deleteEntry(id);

                // Initialize Bio

                BiometricView bioView = new BiometricView(getApplicationContext());
                bioView.initialize(new InitializationListener() {
                    @Override
                    public void initializationStart() {

                    }

                    @Override
                    public void initializationProgress(float v) {

                    }

                    @Override
                    public void initializationComplete() {
                        Log.d(TAG, "Adding id to List");
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
        });

    }

    private void checkUserPermission() {
        final int writeExtPermissions = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        final int readExtPermissions  = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        final int cameraPermissions   = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);

        Log.d(TAG, "Write: "+ writeExtPermissions);
        Log.d(TAG, "Read: "+ readExtPermissions);
        Log.d(TAG, "Cam: "+ cameraPermissions);

        if(writeExtPermissions == PERMISSION_GRANTED &&
            readExtPermissions == PERMISSION_GRANTED &&
            cameraPermissions == PERMISSION_GRANTED) {
            Log.d(TAG, "Permitted");
        }
        else {
            Log.d(TAG, "Requesting Permission");
            ActivityCompat.requestPermissions(this,
                    new String[] {WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE, CAMERA},
                    APP_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult( int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        final int writeExtPermissions = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        final int readExtPermissions  = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        final int cameraPermissions   = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);

        Log.d(TAG, "Write: "+ writeExtPermissions);
        Log.d(TAG, "Read: "+ readExtPermissions);
        Log.d(TAG, "Cam: "+ cameraPermissions);

        if(requestCode == APP_PERMISSION_CODE){
            if(writeExtPermissions == PERMISSION_GRANTED &&
                    readExtPermissions == PERMISSION_GRANTED &&
                    cameraPermissions == PERMISSION_GRANTED){
                Log.d(TAG, "Permission Granted");
                Message.message(this, "Permission Granted");
            }else {
                Log.d(TAG, "Permission Denied");
                Message.message(this, "Permission Denied, please grant permission for the app to run");
//                Disable Employee Button
                btnEmployee.setEnabled(false);
            }
        }
    }


}