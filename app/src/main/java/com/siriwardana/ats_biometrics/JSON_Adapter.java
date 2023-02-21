package com.siriwardana.ats_biometrics;

import android.util.Log;

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

    /**
     * Constructor: Empty
     */
    public JSON_Adapter(){

    }

    /**
     * Makes the employee JSON Object given all the details required.
     * @param id
     * @param fName
     * @param lName
     * @param stAddress
     * @param city
     * @param state
     * @param zipCode
     * @param dob
     * @param age
     * @param sex
     * @param dept
     * @param role
     * @return
     * @throws JSONException
     */
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

    /**
     * Returns the Employee ID
     * @param object
     * @return
     * @throws JSONException
     */
    public static int getID(JSONObject object) throws JSONException {
        return object.getInt(EID);
    }

    /**
     * Returns the Employee First Name
     * @param object
     * @return
     * @throws JSONException
     */
    public static String getFirstName(JSONObject object) throws JSONException {
        return object.getString(FIRST_NAME);
    }

    /**
     * Returns the Employee Last Name
     * @param object
     * @return
     * @throws JSONException
     */
    public static String getLastName(JSONObject object) throws JSONException {
        return object.getString(LAST_NAME);
    }

    /**
     * Returns the Employee Street Address
     * @param object
     * @return
     * @throws JSONException
     */
    public static String getStreetAddress(JSONObject object) throws JSONException {
        return object.getString(STREET_ADDRESS);
    }

    /**
     * Returns the Employee City
     * @param object
     * @return
     * @throws JSONException
     */
    public static String getCity(JSONObject object) throws JSONException {
        return object.getString(CITY);
    }

    /**
     * Returns the Employee State
     * @param object
     * @return
     * @throws JSONException
     */
    public static String getState(JSONObject object) throws JSONException {
        return object.getString(STATE);
    }

    /**
     * Returns the Employee Zip Code
     * @param object
     * @return
     * @throws JSONException
     */
    public static String getZipCode(JSONObject object) throws JSONException {
        return object.getString(ZIP_CODE);
    }

    /**
     * Returns the Employee Date of Birth
     * @param object
     * @return
     * @throws JSONException
     */
    public static String getDOB(JSONObject object) throws JSONException {
        return object.getString(DOB);
    }

    /**
     * Returns the Employee Age
     * @param object
     * @return
     * @throws JSONException
     */
    public static String getAge(JSONObject object) throws JSONException {
        return object.getString(AGE);
    }

    /**
     * Returns the Employee Sex
     * @param object
     * @return
     * @throws JSONException
     */
    public static String getSex(JSONObject object) throws JSONException {
        return object.getString(SEX);
    }

    /**
     * Returns the Employee Department
     * @param object
     * @return
     * @throws JSONException
     */
    public static String getDept(JSONObject object) throws JSONException {
        return object.getString(DEPT);
    }

    /**
     * Returns the Employee Roles
     * @param object
     * @return
     * @throws JSONException
     */
    public static String getRole(JSONObject object) throws JSONException {
        return object.getString(ROLE);
    }

}
