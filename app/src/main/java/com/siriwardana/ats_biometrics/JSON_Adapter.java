package com.siriwardana.ats_biometrics;

import org.json.JSONException;
import org.json.JSONObject;

public class JSON_Adapter {

    private static final String EID = "EID";
    private static final String FIRST_NAME = "FNAME";
    private static final String LAST_NAME = "LNAME";
    private static final String STREET_ADDRESS = "STADDR";
    private static final String CITY = "CITY";
    private static final String STATE = "STATE";
    private static final String ZIP_CODE = "ZIPCODE";
    private static final String DOB = "DOB";
    private static final String AGE = "AGE";
    private static final String SEX = "SEX";
    private static final String DEPT = "DEPT";
    private static final String ROLE = "ROLE";

    public static JSONObject makeJSONObject(int id, String fName, String lName, String stAddress,
                                            String city, String state, String zipCode, String dob,
                                            String age, String sex, String dept, String role) throws JSONException {
        JSONObject object = new JSONObject();

        object.put(EID, id);
        object.put(FIRST_NAME, fName);
        object.put(LAST_NAME, lName);
        object.put(STREET_ADDRESS, stAddress);
        object.put(CITY, city);
        object.put(STATE, state);
        object.put(ZIP_CODE, zipCode);
        object.put(DOB, dob);
        object.put(AGE, age);
        object.put(SEX, sex);
        object.put(DEPT, dept);
        object.put(ROLE, role);

        return object;
    }

    public static int getID(JSONObject object) throws JSONException {
        return object.getInt(EID);
    }

    public static String getFirstName(JSONObject object) throws JSONException {
        return object.getString(FIRST_NAME);
    }

    public static String getLastName(JSONObject object) throws JSONException {
        return object.getString(LAST_NAME);
    }

    public static int getStreetAddress(JSONObject object) throws JSONException {
        return object.getInt(STREET_ADDRESS);
    }

    public static int getCity(JSONObject object) throws JSONException {
        return object.getInt(CITY);
    }

    public static int getState(JSONObject object) throws JSONException {
        return object.getInt(STATE);
    }

    public static int getZipCode(JSONObject object) throws JSONException {
        return object.getInt(ZIP_CODE);
    }

    public static int getDOB(JSONObject object) throws JSONException {
        return object.getInt(DOB);
    }

    public static int getAge(JSONObject object) throws JSONException {
        return object.getInt(AGE);
    }

    public static int getSex(JSONObject object) throws JSONException {
        return object.getInt(SEX);
    }

    public static int getDept(JSONObject object) throws JSONException {
        return object.getInt(DEPT);
    }

    public static int getRole(JSONObject object) throws JSONException {
        return object.getInt(ROLE);
    }

}
