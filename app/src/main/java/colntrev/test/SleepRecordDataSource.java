package colntrev.test;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Thy Vu on 2/20/17.
 */

public class SleepRecordDataSource {
    private final static String TAG = "katsu";
    // database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = {MySQLiteHelper.COL_ID, MySQLiteHelper.COL_DATE,
            MySQLiteHelper.COL_REAL_DURATION, MySQLiteHelper.COL_WANTED_DURATION, MySQLiteHelper.COL_WANTED_DURATION};


    public SleepRecordDataSource(Context context){
        dbHelper = new MySQLiteHelper(context);

    }

    // open database
    public void open() throws SQLException{
        database = dbHelper.getWritableDatabase();
    }

    // close database
    public void close(){
        dbHelper.close();
    }

    // Insert new sleep entry into database.
    // Return db table's row ID
    public long addSleepEntry(String date, double realDuration, double wantedDuration, String activity){
        ContentValues values = new ContentValues();
        values.put(dbHelper.COL_DATE, date);
        values.put(dbHelper.COL_REAL_DURATION, realDuration);
        values.put(dbHelper.COL_WANTED_DURATION, wantedDuration);
        values.put(dbHelper.COL_ACTIVITY, activity);
        long newRowId = database.insert(dbHelper.TABLE_NAME, null, values);

        Log.d(TAG, "createSleepEntry returned "+ newRowId);
        return newRowId;


    }

    // return number of rows affected, should be 1
    public int updateSleepEntry(long rowId, String activity){
        ContentValues values = new ContentValues();
        values.put(dbHelper.COL_ACTIVITY, activity);
        return database.update(dbHelper.TABLE_NAME, values, dbHelper.COL_ID + "="+ rowId, null);


    }

    public void getAllActivities(ArrayList<String> activityList){


        String query = "SELECT count(*), "+ dbHelper.COL_ACTIVITY +" FROM "+ dbHelper.TABLE_NAME+" GROUP BY "+dbHelper.COL_ACTIVITY
                + " ORDER BY count(*) DESC;";
        String[] selectionArg = new String[0];
        Cursor cursor = database.rawQuery(query,selectionArg);
        if(cursor.moveToFirst()){
            while(!cursor.isAfterLast()){
                String entry = ""+ cursor.getInt(0) +" "+ cursor.getString(1);
                activityList.add(entry);
                Log.d(TAG, "cursor at entry: " + entry);
                cursor.moveToNext();
            }
        }else{
            Log.d(TAG, "getAllActivities failed (SleepRecordDataSource.java)");
        }




    }

    public void getAllSleepEntries(ArrayList<SleepEntry> activityList){
        String query = "SELECT * FROM "+ dbHelper.TABLE_NAME;
        String[] selectionArg = new String[0];
        Cursor cursor = database.rawQuery(query,selectionArg);
        if(cursor.moveToFirst()){
            while(!cursor.isAfterLast()){
                SleepEntry entry = new SleepEntry(cursor.getLong(0), cursor.getString(1), cursor.getDouble(2),
                        cursor.getDouble(3), cursor.getString(4));

                //String entry = ""+ cursor.getInt(0) +" "+ cursor.getString(1);
                activityList.add(entry);

                Log.d(TAG, "Making sleep entry: " +cursor.getLong(0)+ " "+ cursor.getString(1) + " " + cursor.getDouble(2)
                        + " " +cursor.getDouble(3)+ " " + cursor.getString(4));
                cursor.moveToNext();
            }
        }else{
            Log.d(TAG, "making sleep entries failed (SleepRecordDataSource.java)");
        }
    }


    public void getTopActivities(ArrayList<String> activityList){
        // get number of entries
        int numOfEntries = 1;
        String query = "SELECT COUNT(*) FROM " + dbHelper.TABLE_NAME;
        String[] selectionArg = new String[0];
        Cursor cursor = database.rawQuery(query,selectionArg);
        if(cursor.moveToFirst()){
            numOfEntries = cursor.getInt(0);
            Log.d(TAG, "OK:"+numOfEntries);
        }else{
            Log.d(TAG, "get total number of entries (SleepRecordDataSource.java)");
        }

        // get percentages
        String queryPercent = "SELECT count(*), " +  dbHelper.COL_ACTIVITY +" FROM "+ dbHelper.TABLE_NAME+ " WHERE "+
                dbHelper.COL_REAL_DURATION + "< 8 "+" GROUP BY "+dbHelper.COL_ACTIVITY
                + " ORDER BY count(*) DESC;";
        Cursor cursor2 = database.rawQuery(queryPercent,selectionArg);

        if(cursor2.moveToFirst()){
            Log.d(TAG, "NIKIFOR");
            while(!cursor2.isAfterLast()){


                String entry = ""+ ((double)(cursor2.getInt(0))/numOfEntries)*100 +"% "+ cursor2.getString(1);
                activityList.add(entry);

                Log.d(TAG, "Top activities: " +entry);
                cursor2.moveToNext();
            }
        }else{
            Log.d(TAG, "get top activities failed (SleepRecordDataSource.java)");
        }


    }



    public void clearAllEntries(){

        database.execSQL("DROP TABLE IF EXISTS "+ dbHelper.TABLE_NAME);
    }

}
