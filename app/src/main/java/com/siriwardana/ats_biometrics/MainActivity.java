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

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity {

    private static final int APP_PERMISSION_CODE = 5000;
    private static final String TAG = "SHAMMY";

    private Button btnEmployee, btnDBSize, btnDeleteDB, btnDeleteID;

    private EditText etID;
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

        etID = (EditText) findViewById(R.id.et_eid);

        llManagerOptions = (LinearLayout) findViewById(R.id.ll_manager_options);

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