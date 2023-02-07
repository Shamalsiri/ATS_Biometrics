package com.siriwardana.ats_biometrics;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.accutime.biometrics.BiometricError;
import com.accutime.biometrics.BiometricView;
import com.accutime.biometrics.InitializationListener;

public class BioViewActivity extends AppCompatActivity {

    private static final String TAG = "SHAMMY";
    private BiometricView bioView;

    private static String BUTTON;
    private static String ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_bio_view);

        bioView = (BiometricView) findViewById(R.id.biometric_view);
        BUTTON = getIntent().getStringExtra("BUTTON");
        ID = getIntent().getStringExtra("ID");

        initializeBiometrics();

    }

    private void initializeBiometrics() {
        bioView.initialize(new InitializationListener() {
            @Override
            public void initializationStart() {

            }

            @Override
            public void initializationProgress(float v) {

            }

            @Override
            public void initializationComplete() {
                Message.message(BioViewActivity.this, "Initialization Complete");
                try {
                    Log.d(TAG,"Bio Version: "+ bioView.getVersion());
                }catch (BiometricError e){
                    Log.d(TAG, e.getMessage());
                }

                if(BUTTON.compareTo("IDENTIFY")==0){
                    Log.d(TAG, "ID Mode");

                }else if(BUTTON.compareTo("ENROLL")==0){
                    Log.d(TAG,"Enroll Mode");

                }else if(BUTTON.compareTo("RE_ENROLL")==0){
                    Log.d(TAG, "Re-Enroll Mode");
                }

            }

            @Override
            public void initializationError(BiometricError biometricError) {
                Message.message(BioViewActivity.this, "Failed to Initialize");
                Log.d(TAG, "Initialization Error");
                Log.d(TAG, biometricError.getMessage());
                finish();
                //Replace finish
            }

            @Override
            public void initializationStatusMessage(String s) {

            }
        });
    }
}