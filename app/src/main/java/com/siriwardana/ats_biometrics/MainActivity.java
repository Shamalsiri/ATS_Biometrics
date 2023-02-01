/*
* Engineer  : Shamal Siriwardana
* Date      : 02-01-2023
*/

package com.siriwardana.ats_biometrics;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.Manifest.permission_group.CAMERA;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity {

    private static final int APP_PERMISSION_CODE = 5000;
    private static final String TAG = "SHAMMY";

    private Button btnEmployee, btnDBSize, btnDeleteDB, btnDeleteID;

    private TextInputLayout tilID;

    private EditText etID;
//    private PermissionsListener permissionsListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Full Screen Flags
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        //checks for permission
        checkUserPermission();
    }

    private void checkUserPermission() {
//        this.permissionsListener = permissionsListener;
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
            ActivityCompat.requestPermissions(this, new String[] {WRITE_EXTERNAL_STORAGE,
            READ_EXTERNAL_STORAGE}, APP_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        final int writeExtPermissions = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        final int readExtPermissions  = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        final int cameraPermissions   = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);

        Log.d(TAG, "Write: "+ writeExtPermissions);
        Log.d(TAG, "Read: "+ readExtPermissions);
        Log.d(TAG, "Cam: "+ cameraPermissions);
        Log.d(TAG, "Req: "+ requestCode);

        if(requestCode == APP_PERMISSION_CODE){
            if(writeExtPermissions == PERMISSION_GRANTED &&
                    readExtPermissions == PERMISSION_GRANTED &&
                    cameraPermissions == PERMISSION_GRANTED){
                Log.d(TAG, "Permission Granted");
                Message.message(this, "Permission Granted");
            }else {
                Log.d(TAG, "Permission Denied");
                Message.message(this, "Permission Denied, please grant permission for the app to run");
                //Disable Employee Button
////                btnEmployee.setEnabled(false);
            }
        }
    }
}