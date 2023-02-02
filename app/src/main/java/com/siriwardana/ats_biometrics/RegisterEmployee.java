package com.siriwardana.ats_biometrics;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.google.android.material.textfield.TextInputLayout;

import java.lang.reflect.Array;

public class RegisterEmployee extends AppCompatActivity {

    private TextInputLayout tilState;
    private AutoCompleteTextView actvState;

    private ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_employee);

        actvState = (AutoCompleteTextView) findViewById(R.id.actv_state);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.states) );

        actvState.setAdapter(arrayAdapter);

        actvState.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(actvState.getWindowToken(), 0);
                }
            }
        });


        actvState.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                actvState.setShowSoftInputOnFocus(false);
                String item = parent.getItemAtPosition(position).toString();
                Message.message(RegisterEmployee.this,"Item: "+item);
            }
        });

    }
}