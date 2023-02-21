package com.siriwardana.ats_biometrics;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
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
import android.widget.TextView;

import com.accutime.biometrics.BiometricError;
import com.accutime.biometrics.BiometricView;
import com.accutime.biometrics.InitializationListener;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

public class EmployeeInfoActivity extends AppCompatActivity {

    private final String TAG = "SHAMMY";
    private Button btnEditInfo, btnReEnrollBio, btnDone, btnDelete;
    private TextView tvGreeting, tvID, tvFirstName, tvLastName, tvStreetAddress, tvCity, tvState, tvZipCode,
                    tvAge, tvSex, tvDepartment, tvRole;
    private String id, firstName, lastName, streetAddress, city, state,
                    zipCode, dob, age, sex, department, role;

    // Edit Info Popup - UI elements
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private View editEmployeePopUp;
    private Button btnCancel, btnSave;
    private AutoCompleteTextView actvState;
    private TextInputEditText etFirstName, etLastName, etDOB, etAge,
            etStAddress, etCity, etZipCode, etDepartment, etRole;
    private RadioGroup rgSex;
    private RadioButton rbSex;
    private ArrayAdapter<String> arrayAdapter;

    /**
     * On Create
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

        setContentView(R.layout.activity_employee_info);

        id = getIntent().getStringExtra("ID");

        btnEditInfo= (Button) findViewById(R.id.btn_edit_info);
        btnReEnrollBio = (Button) findViewById(R.id.btn_re_enroll_bio);
        btnDone = (Button) findViewById(R.id.btn_done);
        btnDelete = (Button) findViewById(R.id.btn_delete);

        tvGreeting = (TextView) findViewById(R.id.tv_greeting);
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
        setGreeting();

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
                createNewEditDialog();
            }
        });
    }

    /**
     * Create the dialog box with the editText to edit the employee details
     */
    private void createNewEditDialog() {

        dialogBuilder = new AlertDialog.Builder(this);
        editEmployeePopUp = getLayoutInflater().inflate(R.layout.edit_details_popup, null);

        btnSave = (Button) editEmployeePopUp.findViewById(R.id.btn_save);
        btnCancel = (Button) editEmployeePopUp.findViewById(R.id.btn_cancel);

        tvID = (TextView) editEmployeePopUp.findViewById(R.id.tv_employee_id);

        etFirstName = (TextInputEditText) editEmployeePopUp.findViewById(R.id.et_first_name);
        etLastName = (TextInputEditText) editEmployeePopUp.findViewById(R.id.et_last_name);
        etAge = (TextInputEditText) editEmployeePopUp.findViewById(R.id.et_age);
        etStAddress = (TextInputEditText) editEmployeePopUp.findViewById(R.id.et_street_address);
        etCity = (TextInputEditText) editEmployeePopUp.findViewById(R.id.et_city);
        etZipCode = (TextInputEditText) editEmployeePopUp.findViewById(R.id.et_zip_code);
        etDepartment = (TextInputEditText) editEmployeePopUp.findViewById(R.id.et_department);
        etRole = (TextInputEditText) editEmployeePopUp.findViewById(R.id.et_role);
        etDOB = (TextInputEditText) editEmployeePopUp.findViewById(R.id.et_dob);

        rgSex = (RadioGroup) editEmployeePopUp.findViewById(R.id.rg_sex);
        rbSex = (RadioButton) editEmployeePopUp.findViewById(R.id.rb_male);

        actvState = (AutoCompleteTextView) editEmployeePopUp.findViewById(R.id.actv_state1);

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.states));
        actvState.setAdapter(arrayAdapter);

        dialogBuilder.setView(editEmployeePopUp);
        dialog = dialogBuilder.create();

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width =900;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);


        actvState.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(actvState.getWindowToken(), 0);
                    actvState.setShowSoftInputOnFocus(false);
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

        etDOB.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(etDOB.getWindowToken(), 0);
                    etDOB.setShowSoftInputOnFocus(false);
                    materialDatePicker.show(getSupportFragmentManager(),"DATE_PICKER");

                }
            }
        });

        etDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(etDOB.getWindowToken(), 0);
                    etDOB.setShowSoftInputOnFocus(false);
                    materialDatePicker.show(getSupportFragmentManager(),"DATE_PICKER");

            }
        });

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object epochTime) {
                etDOB.setText(materialDatePicker.getHeaderText());
                Log.d(TAG, ""+(long)materialDatePicker.getSelection());

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
                dialog.dismiss();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //define save here
                checkValues();
                dialog.dismiss();
                //last move post info
//                postInfo();
            }
        });
    }

    /**
     * Set the age textView from the age calculated with the dob
     * @param EAge
     */
    private void setAge(int EAge){
        etAge.setText(Integer.toString(EAge));
    }

    /**
     * Check to see if the user entered any changes to the details
     */
    private void checkValues() {

        //Check First Name for Changes
        if(etFirstName.getText().toString().trim().compareTo("") != 0){
            firstName = etFirstName.getText().toString().trim();
        }
        Log.d(TAG, "F Name: "+ firstName);

        //Check Last Name for Changes
        if(etLastName.getText().toString().trim().compareTo("") != 0){
            lastName = etLastName.getText().toString().trim();
        }
        Log.d(TAG, "L Name: "+ lastName);

        //Check Street Address for Changes
        if(etStAddress.getText().toString().trim().compareTo("") != 0){
            streetAddress = etStAddress.getText().toString().trim();
        }
        Log.d(TAG, "St Address: "+ streetAddress);

        //Check City for Changes
        if(etCity.getText().toString().trim().compareTo("") != 0){
            city = etCity.getText().toString().trim();
        }
        Log.d(TAG, "City: "+ city);
        actvState.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String temp = actvState.getText().toString().trim();
                if(temp.compareTo(state) != 0 && temp.compareTo("") != 0){
                    state = temp;
                }
            }
        });
        Log.d(TAG, "State: "+ state);

        //Check Zip Code for Changes
        if(etZipCode.getText().toString().trim().compareTo("") != 0){
            zipCode = etZipCode.getText().toString().trim();
        }
        Log.d(TAG, "Zip Code: "+ zipCode);

        //Check Date of Birth for Changes
        if(etDOB.getText().toString().trim().compareTo("") != 0){
            dob = etDOB.getText().toString().trim();
        }
        Log.d(TAG, "DOB: "+ dob);

        //Check Age for Changes
        if(etAge.getText().toString().trim().compareTo("") != 0){
            age = etAge.getText().toString().trim();
        }
        Log.d(TAG, "Age: "+ age);

        //Check Sex for Changes
        int radioId = rgSex.getCheckedRadioButtonId();
        if(radioId != -1){
            rbSex = (RadioButton) editEmployeePopUp.findViewById(radioId);

            if(rbSex.getText().toString().compareTo("") != 0){
                Log.d(TAG, rbSex.getText().toString().trim());
                sex = rbSex.getText().toString();
            }
        }
        Log.d(TAG, "SEX: "+ sex);

        //Check Department for Changes
        if(etDepartment.getText().toString().trim().compareTo("") != 0){
            department = etDepartment.getText().toString().trim();
        }
        Log.d(TAG, "Department: "+ department);

        //Check Role for Changes
        if(etRole.getText().toString().trim().compareTo("") != 0){
            role = etRole.getText().toString().trim();
        }
        Log.d(TAG, "Role: "+ role);

        try {
            updateDatabase();
            getEmployeeDetails();
        } catch (JSONException e) {
            Log.d(TAG, "Update Error");
            Log.d(TAG, e.toString());
        }
        setGreeting();

    }

    /**
     * Update the db record with the updated data
     * @throws JSONException
     */
    private void updateDatabase() throws JSONException {
        JSON_Adapter jAdapter = new JSON_Adapter();
        JSONObject empDetails = jAdapter.makeJSONObject(Integer.parseInt(id), firstName, lastName,
                        streetAddress, city, state, zipCode, dob, age, sex,department, role);

        DB_Adapter db = new DB_Adapter(this);
        db.editEntry(id, empDetails);
    }

    /**
     * Get Employee details from the database
     * Show the retrieved employee info in the activity textview.
     * @throws JSONException
     */
    private void getEmployeeDetails() throws JSONException {

        DB_Adapter db_adapter = new DB_Adapter(this);
        JSONObject employeeDetails = db_adapter.getEmployeeData(id);

        firstName = JSON_Adapter.getFirstName(employeeDetails);
        lastName = JSON_Adapter.getLastName(employeeDetails);
        streetAddress = JSON_Adapter.getStreetAddress(employeeDetails);
        city = JSON_Adapter.getCity(employeeDetails);
        state = JSON_Adapter.getState(employeeDetails);
        zipCode = JSON_Adapter.getZipCode(employeeDetails);
        dob = JSON_Adapter.getDOB(employeeDetails);
        age = JSON_Adapter.getAge(employeeDetails);
        sex = JSON_Adapter.getSex(employeeDetails);
        department = JSON_Adapter.getDept(employeeDetails);
        role = JSON_Adapter.getRole(employeeDetails);

        tvID.setText("ID Number: "+id);
        tvFirstName.setText(JSON_Adapter.getFirstName(employeeDetails));
        tvLastName.setText(JSON_Adapter.getLastName(employeeDetails));
        tvStreetAddress.setText(JSON_Adapter.getStreetAddress(employeeDetails));
        tvCity.setText(JSON_Adapter.getCity(employeeDetails));
        tvState.setText(JSON_Adapter.getState(employeeDetails));
        tvZipCode.setText(JSON_Adapter.getZipCode(employeeDetails));
        tvAge.setText(JSON_Adapter.getAge(employeeDetails));
        tvSex.setText(JSON_Adapter.getSex(employeeDetails));
        tvDepartment.setText(JSON_Adapter.getDept(employeeDetails));
        tvRole.setText(JSON_Adapter.getRole(employeeDetails));

    }

    /**
     * Starts the bio view activity in re-enroll mode
     * ID and BUTTON are sent as extras
     */
    private void reEnrollBio() {
        Intent intent = new Intent(this, BioViewActivity.class);
        intent.putExtra("BUTTON", "RE_ENROLL");
        intent.putExtra("ID", id);
        startActivity(intent);
    }

    /**
     * Delete the Employee from the SQLite DB
     *  Delete the Employee from the Biometrics Database
     */
    private void deleteEmployee() {
        // Delete from SQLite db
        DB_Adapter db_adapter = new DB_Adapter(this);
        db_adapter.deleteEntry(id);

        // Delete from the Bio db
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

    /**
     * Set the Greeting depending on the time of the day
     */
    private void setGreeting(){
        //Determine greeting
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
        String greeting ="";

        if(timeOfDay >= 0 && timeOfDay < 12){
            //Good morning
            greeting = "Good Morning ";
        }
        else if(timeOfDay >= 12 && timeOfDay < 16){
            // Good Afternoon
            greeting = "Good Afternoon ";
        }
        else if (timeOfDay >= 16 && timeOfDay < 24){
            //Good Evening
            greeting = "Good Evening ";
        }
        //post greeting
        tvGreeting.setText(greeting+firstName);
    }

    /**
     * Must be replace with Back trace
     */
    private void backHome(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}