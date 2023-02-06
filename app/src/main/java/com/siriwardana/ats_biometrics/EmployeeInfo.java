package com.siriwardana.ats_biometrics;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class EmployeeInfo extends AppCompatActivity {

    private Button btnEditInfo, btnReEnrollBio, btnDone, btnDelete;
    private TextView tvID, tvFirstName, tvLastName, tvStreetAddress, tvCity, tvState, tvZipCode,
                    tvAge, tvSex, tvDepartment, tvRole;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_info);

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
    }
}