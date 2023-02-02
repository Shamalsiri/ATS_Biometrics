package com.siriwardana.ats_biometrics;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class DB_Adapter {

    private static final String TAG = "SHAMMY";
    private DB_Helper db_helper;
    private Context context;

    public DB_Adapter(Context context){
        this.context = context;
        db_helper = new DB_Helper(context);
    }

    public void createTable(){
        SQLiteDatabase db = db_helper.getWritableDatabase();
        Log.d(TAG, "Creating DB Table");
        try{
            db.execSQL(DB_Helper.CREATE_TABLE);
        } catch (SQLException e){
            Log.d(TAG, e.toString());
            Message.message(context, "Failed to Create Table");
        }
    }

    public void deleteTable(){
        SQLiteDatabase db = db_helper.getWritableDatabase();
        Log.d(TAG, "Deleting DB Table");
        try{
            db.execSQL(DB_Helper.DROP_TABLE);
        }catch (SQLException e){
            Log.d(TAG, e.toString());
            Message.message(context,"Failed to Delete Table");

        }
    }

    public long insertEntry(JSONObject empData) throws JSONException {
        int id = empData.getInt(DB_Helper.EID);
        String fName = empData.getString(DB_Helper.FIRST_NAME);
        String lName = empData.getString(DB_Helper.LAST_NAME);
        String stAddress = empData.getString(DB_Helper.STREET_ADDRESS);
        String city = empData.getString(DB_Helper.CITY);
        String state = empData.getString(DB_Helper.STATE);
        String zipCode = empData.getString(DB_Helper.ZIP_CODE);
        String dob = empData.getString(DB_Helper.DOB);
        String age = empData.getString(DB_Helper.AGE);
        String sex = empData.getString(DB_Helper.SEX);
        String dept = empData.getString(DB_Helper.DEPT);
        String role = empData.getString(DB_Helper.ROLE);

        ContentValues  contentValues = new ContentValues();
        contentValues.put(DB_Helper.EID, id);
        contentValues.put(DB_Helper.FIRST_NAME, fName);
        contentValues.put(DB_Helper.LAST_NAME, lName);
        contentValues.put(DB_Helper.STREET_ADDRESS, stAddress);
        contentValues.put(DB_Helper.CITY, city);
        contentValues.put(DB_Helper.STATE, state);
        contentValues.put(DB_Helper.ZIP_CODE, zipCode);
        contentValues.put(DB_Helper.DOB, dob);
        contentValues.put(DB_Helper.AGE, age);
        contentValues.put(DB_Helper.SEX, sex);
        contentValues.put(DB_Helper.DEPT, dept);
        contentValues.put(DB_Helper.ROLE, role);

        SQLiteDatabase db = db_helper.getWritableDatabase();

        Log.d(TAG, "Inserting new entry into the database");
        long result = db.insert(DB_Helper.TABLE_NAME, null, contentValues);

        if(result > 0){
            Log.d(TAG, "Successfully inserted new entry to database");
            Message.message(context, "Successfully inserted new entry to database");
        }else{
            Log.d(TAG, "Failed to insert new entry");
            Message.message(context, "Failed to insert new entry");
        }

        return result;
    }

    public int editEntry(String eID, JSONObject empData) throws JSONException {

        int id = empData.getInt(DB_Helper.EID);
        String fName = empData.getString(DB_Helper.FIRST_NAME);
        String lName = empData.getString(DB_Helper.LAST_NAME);
        String stAddress = empData.getString(DB_Helper.STREET_ADDRESS);
        String city = empData.getString(DB_Helper.CITY);
        String state = empData.getString(DB_Helper.STATE);
        String zipCode = empData.getString(DB_Helper.ZIP_CODE);
        String dob = empData.getString(DB_Helper.DOB);
        String age = empData.getString(DB_Helper.AGE);
        String sex = empData.getString(DB_Helper.SEX);
        String dept = empData.getString(DB_Helper.DEPT);
        String role = empData.getString(DB_Helper.ROLE);

        ContentValues  contentValues = new ContentValues();
        contentValues.put(DB_Helper.EID, id);
        contentValues.put(DB_Helper.FIRST_NAME, fName);
        contentValues.put(DB_Helper.LAST_NAME, lName);
        contentValues.put(DB_Helper.STREET_ADDRESS, stAddress);
        contentValues.put(DB_Helper.CITY, city);
        contentValues.put(DB_Helper.STATE, state);
        contentValues.put(DB_Helper.ZIP_CODE, zipCode);
        contentValues.put(DB_Helper.DOB, dob);
        contentValues.put(DB_Helper.AGE, age);
        contentValues.put(DB_Helper.SEX, sex);
        contentValues.put(DB_Helper.DEPT, dept);
        contentValues.put(DB_Helper.ROLE, role);

        SQLiteDatabase db = db_helper.getWritableDatabase();
        int result = db.update(db_helper.TABLE_NAME, contentValues, db_helper.EID+" ="+eID, null);

        if(result == 0){
            Log.d(TAG, eID+" Failed to update");
            Message.message(context, eID+" Failed to update");
        }else{
            Log.d(TAG, eID+" successfully updated");
            Message.message(context, eID+" successfully updated");
        }

        return result;
    }

    public void deleteEntry(String eID){
        SQLiteDatabase db = db_helper.getWritableDatabase();

        int result = db.delete(DB_Helper.TABLE_NAME, DB_Helper.EID+" = "+eID, null);

        if(result == 0){
            Log.d(TAG,"Failed to delete "+eID+" from the database");
            Message.message(context,"Failed to delete "+eID+" from the database");
        }else{
            Log.d(TAG,eID+" deleted from the database");
            Message.message(context,eID+" deleted from the database");
        }
    }

    public JSONObject getEmployeeData(String eID) throws JSONException {
        SQLiteDatabase db = db_helper.getWritableDatabase();

        String[] columns = {DB_Helper.EID, DB_Helper.FIRST_NAME, DB_Helper.LAST_NAME,
                            DB_Helper.STREET_ADDRESS, DB_Helper.CITY, DB_Helper.STATE,
                            DB_Helper.ZIP_CODE, DB_Helper.DOB, DB_Helper.AGE, DB_Helper.SEX,
                            DB_Helper.DEPT, DB_Helper.ROLE};

        Cursor cursor = db.query(DB_Helper.TABLE_NAME, columns,DB_Helper.EID+" = '"+eID+"'",
                null, null, null, null);

        StringBuffer details = new StringBuffer();
        JSONObject detailObject = new JSONObject();

        while(cursor.moveToNext()){
            int idIndex = cursor.getColumnIndex(DB_Helper.EID);
            int fNameIndex = cursor.getColumnIndex(DB_Helper.FIRST_NAME);
            int lNameIndex = cursor.getColumnIndex(DB_Helper.LAST_NAME);
            int stAddressIndex = cursor.getColumnIndex(DB_Helper.STREET_ADDRESS);
            int cityIndex = cursor.getColumnIndex(DB_Helper.CITY);
            int stateIndex = cursor.getColumnIndex(DB_Helper.STATE);
            int zipIndex = cursor.getColumnIndex(DB_Helper.ZIP_CODE);
            int dobIndex = cursor.getColumnIndex(DB_Helper.DOB);
            int ageIndex = cursor.getColumnIndex(DB_Helper.AGE);
            int sexIndex = cursor.getColumnIndex(DB_Helper.SEX);
            int deptIndex = cursor.getColumnIndex(DB_Helper.DEPT);
            int roleIndex = cursor.getColumnIndex(DB_Helper.ROLE);

            int id = cursor.getInt(idIndex);
            String fName = cursor.getString(fNameIndex);
            String lName = cursor.getString(lNameIndex);
            String stAddr = cursor.getString(stAddressIndex);
            String city = cursor.getString(cityIndex);
            String state = cursor.getString(stateIndex);
            String zip = cursor.getString(zipIndex);
            String dob = cursor.getString(dobIndex);
            String age = cursor.getString(ageIndex);
            String sex = cursor.getString(sexIndex);
            String dept = cursor.getString(deptIndex);
            String role = cursor.getString(roleIndex);

            detailObject.put(DB_Helper.EID, id);
            detailObject.put(DB_Helper.FIRST_NAME, fName);
            detailObject.put(DB_Helper.LAST_NAME, lName);
            detailObject.put(DB_Helper.STREET_ADDRESS, stAddr);
            detailObject.put(DB_Helper.CITY, city);
            detailObject.put(DB_Helper.STATE, state);
            detailObject.put(DB_Helper.ZIP_CODE, zip);
            detailObject.put(DB_Helper.DOB, dob);
            detailObject.put(DB_Helper.AGE, age);
            detailObject.put(DB_Helper.SEX, sex);
            detailObject.put(DB_Helper.DEPT, dept);
            detailObject.put(DB_Helper.ROLE, role);
        }

        return detailObject;
    }

    public int getNumEntries(){
        String query = "SELECT * FROM "+DB_Helper.TABLE_NAME;

        SQLiteDatabase db = db_helper.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Log.d(TAG, "Number of database entries: "+cursor.getCount());

        return cursor.getCount();
    }

    public boolean doesEntryExist(String eID){
        String query = "SELECT * FROM "+DB_Helper.TABLE_NAME+" WHERE "+DB_Helper.EID+" = "+eID;

        SQLiteDatabase db = db_helper.getWritableDatabase();

        Log.d(TAG, "Checking for "+eID+" in the database");
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.getCount() > 0){
            Log.d(TAG, "ID: "+eID+" exists in the database");
            return true;
        }else{
            Log.d(TAG, "ID: "+eID+" doesn't exist in the database");
            return false;
        }
    }

    public class DB_Helper extends SQLiteOpenHelper {

        // DB information
        private static final String DB_NAME = "EMPLOYEE_DB";
        private static final String TABLE_NAME = "EMPLOYEE_TABLE";
        private static final int DB_VERSION = 0;

        // DB Fields
        private static final String EID = "eID";
        private static final String FIRST_NAME = "fName";
        private static final String LAST_NAME = "lName";
        private static final String STREET_ADDRESS = "stAddress";
        private static final String CITY = "city";
        private static final String STATE = "state";
        private static final String ZIP_CODE = "zipCode";
        private static final String DOB = "dob";
        private static final String AGE = "age";
        private static final String SEX = "sex";
        private static final String DEPT = "dept";
        private static final String ROLE = "role";

        // Queries
        private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "+TABLE_NAME+"("+
                EID+" INTEGER PRIMARY KEY, "+
                FIRST_NAME+" VARCHAR(255), "+
                LAST_NAME+" VARCHAR(255), "+
                STREET_ADDRESS+" VARCHAR(255), "+
                CITY+" VARCHAR(255), "+
                STATE+" VARCHAR(255), "+
                ZIP_CODE+" VARCHAR(255), "+
                DOB+" VARCHAR(255), "+
                AGE+" VARCHAR(255), "+
                SEX+" VARCHAR(255), "+
                DEPT+" VARCHAR(255), "+
                ROLE+" VARCHAR(255) "+
                ");";
        private static final String DROP_TABLE = "DROP TABLE IF EXISTS "+TABLE_NAME;


        private final Context context;

        public DB_Helper(Context context){
            super(context, DB_NAME, null, DB_VERSION);
            this.context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try{
                db.execSQL(CREATE_TABLE);
            }catch (SQLException e){
                Log.d(TAG, e.toString());
                Message.message(context, "Failed to Create Database");
            }

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try{
                db.execSQL(CREATE_TABLE);
                onCreate(db);
            }catch (SQLException e){
                Log.d(TAG, e.toString());
                Message.message(context, "Failed to Upgrade Database");
            }
        }
    }

}
