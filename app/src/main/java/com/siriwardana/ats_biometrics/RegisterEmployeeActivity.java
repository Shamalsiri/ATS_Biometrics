package com.siriwardana.ats_biometrics;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class RegisterEmployeeActivity extends AppCompatActivity {

    private final String TAG = "SHAMMY";

    private AutoCompleteTextView actvState, actvDOB;
    private TextInputLayout tilDOB;
    private TextInputEditText etEmployeeID, etFirstName, etLastName, etAge,
            etStAddress, etCity, etZipCode, etDepartment, etRole;
    private RadioGroup rgSex;
    private RadioButton rbSex;
    private Button btnCancel, btnEnroll;

    private String eId, fName, lName, dob, age, sex, stAddress,
            city, state, zipCode, department, role;

    private ArrayAdapter<String> arrayAdapter;

    /**
     * On Create
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Full Screen Flags
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register_employee);

        etEmployeeID = (TextInputEditText) findViewById(R.id.et_employee_id);
        etFirstName = (TextInputEditText) findViewById(R.id.et_first_name);
        etLastName = (TextInputEditText) findViewById(R.id.et_last_name);
        etAge = (TextInputEditText) findViewById(R.id.et_age);
        etStAddress = (TextInputEditText) findViewById(R.id.et_street_address);
        etCity = (TextInputEditText) findViewById(R.id.et_city);
        etZipCode = (TextInputEditText) findViewById(R.id.et_zip_code);
        etDepartment = (TextInputEditText) findViewById(R.id.et_department);
        etRole = (TextInputEditText) findViewById(R.id.et_role);

        rgSex = (RadioGroup) findViewById(R.id.rg_sex);
        rbSex = (RadioButton) findViewById(R.id.rb_male);

        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnEnroll = (Button) findViewById(R.id.btn_enroll);

        actvDOB = (AutoCompleteTextView)  findViewById(R.id.actv_dob);
        actvState = (AutoCompleteTextView) findViewById(R.id.actv_state);
        state = null;
        zipCode = null;

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.states) );
        actvState.setAdapter(arrayAdapter);

        // Hide Keyboard
        actvState.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(actvState.getWindowToken(), 0);
                }
            }
        });
        // Pick clicked item
        actvState.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                actvState.setShowSoftInputOnFocus(false);
                String item = parent.getItemAtPosition(position).toString();
                state = item;
            }
        });

        //Material Date Picker
        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Enter the Date of Birth");
        final MaterialDatePicker materialDatePicker = builder.build();

        actvDOB.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(actvDOB.getWindowToken(), 0);
                    actvDOB.setShowSoftInputOnFocus(false);
                    materialDatePicker.show(getSupportFragmentManager(),"DATE_PICKER");
                }
            }
        });

        actvDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(actvDOB.getWindowToken(), 0);
                actvDOB.setShowSoftInputOnFocus(false);
                materialDatePicker.show(getSupportFragmentManager(),"DATE_PICKER");
            }
        });

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object epochTime) {
                actvDOB.setText(materialDatePicker.getHeaderText());

                int offsetFromUTC = TimeZone.getDefault().getOffset(new Date().getTime()) * -1;
                Date date = new Date((long) epochTime + offsetFromUTC);
                Calendar c = new GregorianCalendar();

                c.setTime(date);
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH) +1;
                int day = c.get(Calendar.DAY_OF_MONTH);

                int empAge = 0;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    empAge = Period.between(LocalDate.of(year, month, day),
                            LocalDate.now()).getYears();
                }

                setAge(empAge);
            }
        });


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //back stack
                finish();
            }
        });

        btnEnroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInputs();
                Log.d(TAG,"DOB Text Size: "+actvDOB.getTextSize());
                validateInputs();
            }
        });
    }

    /**
     * Set the Age in the age Textview
     * @param EAge
     */
    private void setAge(int EAge){
        etAge.setText(Integer.toString(EAge));
    }

    /**
     * Checks if the Employee ID exists in the SQLite DB
     * Enroll the Employee to the SQLite DB
     */
    private void enrollToDB(){
        DB_Adapter db_adapter = new DB_Adapter(RegisterEmployeeActivity.this);
        if(db_adapter.doesEntryExist(eId)){
            Log.d(TAG, eId+" exists in the database");
            Message.message(this, "Employee ID already exists, please enter another Employee ID");
        }else {
            Log.d(TAG, eId+" doesn't exists in the database");
            try {
                insertEntryToDB();
            } catch (JSONException e) {
                Log.d(TAG, "Failed to insert entry to database");
                Log.d(TAG, "Catch Log: "+e.getMessage());
            }
            openEnrollBioView();
        }

    }

    /**
     * Calls the Bioview Activity with the ID and BUTTON set to ENROLL
     */
    private void openEnrollBioView() {
        Intent intent = new Intent(this, BioViewActivity.class);
        intent.putExtra("BUTTON", "ENROLL");
        intent.putExtra("ID", eId);

        startActivity(intent);
    }

    /**
     * Insert the JSON Object with Employee data to the SQLite DB
     * @throws JSONException
     */
    private void insertEntryToDB() throws JSONException {
        int id = Integer.parseInt(eId);
        JSONObject object = JSON_Adapter.makeJSONObject(id, fName, lName, stAddress, city, state,
                zipCode, dob, age, sex, department, role);
        DB_Adapter db_adapter = new DB_Adapter(this);
        db_adapter.insertEntry(object);
    }

    /**
     * Get the details the User Input
     */
    private void getInputs(){
        eId = etEmployeeID.getText().toString().trim();
        fName = etFirstName.getText().toString().trim();
        lName = etLastName.getText().toString().trim();

        age = etAge.getText().toString().trim();
        // Getting clicked radio button
        int radioID = rgSex.getCheckedRadioButtonId();
        rbSex = findViewById(radioID);
        // Sex is selected in the validateInput method
        stAddress = etStAddress.getText().toString().trim();
        city = etCity.getText().toString().trim();
        zipCode = etZipCode.getText().toString().trim();

        role = etRole.getText().toString().trim();

        dob = actvDOB.getText().toString().trim();

        if(etDepartment.getText().toString().trim() != null){
            department = etDepartment.getText().toString().trim();
        }
    }

    /**
     * Validate the User Inputs
     */
    private void validateInputs(){
        if(isEmptyOrNull(eId)){
            Message.message(this, "Enter a unique Employee ID#");
            Log.d(TAG, "Employee ID# is empty or null");
            return;
        }else{
            Log.d(TAG, "Employee ID entered: "+eId);
        }

        if(isEmptyOrNull(fName)){
            Message.message(this, "Enter a First Name");
            Log.d(TAG, "First Name is empty or null");
            return;
        }else{
            Log.d(TAG, "First Name entered: "+fName);
        }

        if(isEmptyOrNull(lName)){
            Message.message(this, "Enter a Last Name");
            Log.d(TAG, "Last Name is empty or null");
            return;
        }else{
            Log.d(TAG, "Last Name entered: "+lName);
        }

        if(isEmptyOrNull(stAddress)){
            Message.message(this, "Enter a Street Address");
            Log.d(TAG, "Street Address is empty or null");
            return;
        }else{
            Log.d(TAG, "Street Address entered: "+stAddress);
        }

        if(isEmptyOrNull(city)){
            Message.message(this, "Enter a City");
            Log.d(TAG, "City is empty or null");
            return;
        }else{
            Log.d(TAG, "Street Address entered: "+city);
        }

        Log.d(TAG,"State: "+state);
        if(state == null){
            Log.d(TAG,"Validate State: "+state);
            Message.message(this, "Enter a State");
            Log.d(TAG, "State is empty or null");
            return;
        }else{
            Log.d(TAG, "State entered: "+state);
        }

        if(isEmptyOrNull(zipCode)){
            Message.message(this, "Enter a Zip Code");
            Log.d(TAG, "Zip Code is empty or null");
            return;
        }else if (zipCode.length() != 5 ){
            Message.message(this, "Invalid zip Code, enter a valid zip Code");
            Log.d(TAG, "Zip Code is invalid");
            return;
        }else{
            Log.d(TAG, "Zip Code entered: "+zipCode);
        }

        if(isEmptyOrNull(dob)){
            Message.message(this, "Pick a Date of Birth");
            Log.d(TAG, "Date of Birth is empty or null");
            return;
        }else{
            Log.d(TAG, "Date of Birth entered: "+dob);
        }

        if(isEmptyOrNull(age)){
            Message.message(this, "Enter an Age");
            Log.d(TAG, "Age is empty or null");
            return;
        }else{
            Log.d(TAG, "Age entered: "+age);
        }

        if(rbSex != null){
            sex = rbSex.getText().toString().trim();
            Log.d(TAG, "Sex selected: "+sex);
        }else{
            Message.message(this, "Pick a Sex");
            Log.d(TAG, "Sex not selected");
            return;
        }

        if(isEmptyOrNull(department)){
            Message.message(this, "Enter a Department");
            Log.d(TAG, "Department is empty or null");
            return;
        }else{
            Log.d(TAG, "Department entered: "+department);
        }

        if(isEmptyOrNull(role)){
            Message.message(this, "Enter a Role");
            Log.d(TAG, "Role is empty or null");
            return;
        }else{
            Log.d(TAG, "Role entered: "+role);
        }

        enrollToDB();
    }

    /**
     * Checks if a string is empty or null
     * @param value
     * @return
     */
    public boolean isEmptyOrNull(String value){
        if(value.compareTo("") == 0 || value == null){
            return true;
        }
        return false;
    }
}