package com.siriwardana.ats_biometrics;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import android.view.WindowManager;
import android.view.View;


import com.accutime.biometrics.BiometricError;
import com.accutime.biometrics.BiometricView;
import com.accutime.biometrics.Enrollment;
import com.accutime.biometrics.EnrollmentListener;
import com.accutime.biometrics.EnrollmentSession;
import com.accutime.biometrics.IdentificationListener;
import com.accutime.biometrics.InitializationListener;

import java.util.ArrayList;
import java.util.List;

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

                //Check DB size and open enroll dialog if size = 0
                int size;
                try {
                  size = bioView.getEnrollmentIds().size();
                    if(size == 0 ){
                        newEnrollDialog(true);
                        return;
                    }
                } catch (BiometricError e) {
                    Log.d(TAG, "Error attempting to get db size");
                    Log.d(TAG, e.toString());
                }


                if(BUTTON.compareTo("IDENTIFY")==0){
                    Log.d(TAG, "ID Mode");
                    identify();

                }else if(BUTTON.compareTo("ENROLL")==0){
                    Log.d(TAG,"Enroll Mode");
                    enroll();

                }else if(BUTTON.compareTo("RE_ENROLL")==0){
                    Log.d(TAG, "Re-Enroll Mode");
                    reEnroll();
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

    private void enroll() {
        bioView.setVisibility(View.VISIBLE);

        bioView.startEnrollment(ID, new EnrollmentListener() {
            @Override
            public void enrollmentStart() {

            }

            @Override
            public void enrollmentComplete(EnrollmentSession enrollmentSession) {
                if(enrollmentSession.hasSessionData()){
                    enrollmentSession.commit();
                }

                Log.d(TAG, "Enrollment Complete");
                Message.message(BioViewActivity.this, "Enrollment Complete");

                Intent intent = new Intent(BioViewActivity.this, RegisterEmployee.class);
                intent.putExtra("ID", ID);
                startActivity(intent);
            }

            @Override
            public void enrollmentTimeout(String s) {
                Message.message(BioViewActivity.this, "Enrollment Timed Out");
                Log.d(TAG, "Enrollment Timed out");

                deleteRecord();
                finish();

                //Can implement a re-try function here
            }

            @Override
            public void enrollmentStatus(String s) {

            }

            @Override
            public void enrollmentError(BiometricError biometricError) {
                Message.message(BioViewActivity.this, "Enrollment Error");
                Log.d(TAG, "Enrollment Error: "+biometricError.getMessage());

                deleteRecord();
                finish();

                //Can implement a re-try here
            }

            @Override
            public void enrollmentProgress(float v) {

            }

            @Override
            public void enrollmentTimeRemaining(long l) {

            }
        });
    }

    private void identify() {
        bioView.setVisibility(View.VISIBLE);

        bioView.startIdentification(new IdentificationListener() {
            @Override
            public void identificationStart() {

            }

            @Override
            public void identificationComplete(Enrollment enrollment) {
                String id;
                if(enrollment != null){
                    id = enrollment.getId();
                    Log.d(TAG, "Identified Id#: " + id);

                    Intent intent = new Intent(BioViewActivity.this, RegisterEmployee.class);
                    intent.putExtra("ID", id);
                    startActivity(intent);
                }
            }

            @Override
            public void identificationTimeout(String s) {
                Log.d(TAG, "ID timed out");
                Log.d(TAG, "ID time out message: "+ s);
                Message.message(BioViewActivity.this, "Identification Timed Out");

                newEnrollDialog(false);
            }

            @Override
            public void identificationStatus(String s) {

            }

            @Override
            public void identificationError(BiometricError biometricError) {
                Log.d(TAG, "Biometric Error: " + biometricError);
                Message.message(getApplicationContext(), "Biometric Error");
            }

            @Override
            public void identificationProgress(float v) {

            }

            @Override
            public void identificationTimeRemaining(long l) {

            }
        });
    }

    private void reEnroll(){
        bioView.setVisibility(View.VISIBLE);

        Enrollment existingEnrollment = null;
        try {
            existingEnrollment = bioView.getEnrollment(ID);
            if(existingEnrollment != null){
                boolean appendable;
                appendable = bioView.canAppend(ID);
                if(!appendable){
                    deleteBio(ID);
                }
                enroll();
            }
        } catch (BiometricError e) {
            Message.message(getApplicationContext(), "Biometric Error");
            Log.d(TAG, "Biometric Error: " + e.toString());
        }

    }

    private void deleteRecord(){
        DB_Adapter db = new DB_Adapter(this);
        db.deleteEntry(ID);
        deleteBio(ID);
    }

    private void deleteBio(String id){
        //Put id into a List of strings
        List<String> idList = new ArrayList<>(1);
        idList.add(id);
        boolean deleted;
        try{
            //delete ids in the list of strings
            deleted = bioView.deleteEnrollments(idList);
            if(deleted){
                Message.message(this,""+id+" deleted from the Biometric DB");
                Log.d("SHAMMY", ""+id+" deleted from Bio DB");
                //Delete record from the db;
            }else{
                Message.message(this,""+id+" not deleted from the Biometric DB");
                Log.d("SHAMMY", ""+id+" not deleted from the Biometric DB");
            }
        } catch (BiometricError e) {
            e.printStackTrace();
            Log.d("SHAMMY", ""+e.getMessage());
        }
    }

    private void newEnrollDialog(boolean b) {

    }
}