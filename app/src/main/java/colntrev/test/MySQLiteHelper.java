package colntrev.test;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by MaiThy on 2/20/17.
 */

public class MySQLiteHelper extends SQLiteOpenHelper {
    // If database schema is changed, database version must be incremented
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "SleepRecord.db";

    // Sleep Record Table
    public static final String TABLE_NAME = "SLEEP_RECORD";
    public static final String COL_ID = "_id";
    public static final String COL_DATE ="date";
    public static final String COL_REAL_DURATION = "realDuration";
    public static final String COL_WANTED_DURATION = "wantedDuration";
    public static final String COL_ACTIVITY = "activity";

    /*
    // future release
    public static final String COL_SLEEP_TIME = "sleepTime";
    public static final String COL_SLEEP_AMPM = "sleepAMPM";
    public static final String COL_WAKE_TIME = "wakeTime";
    public static final String COL_WAKE_AMPM = "wakeAMPM";
    */


    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +" ("
            + COL_ID + " integer primary key autoincrement, "
            + COL_DATE + " TEXT, "
            + COL_REAL_DURATION + " REAL, "
            + COL_WANTED_DURATION + " REAL, "
            + COL_ACTIVITY + " TEXT);";

    // constructor
    public MySQLiteHelper (Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);


    }


    // onCreate
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(db);

    }


}
