package colntrev.test;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by colntrev on 2/13/17.
 */

class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TABLE_ACTION = "action";
    private static final String KEY_COUNT = "count";
    private static final String KEY_ACTION = "act";
    private static final String KEY_POSITIVE = "positive";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "actionBase";
    private static final String CREATE_TABLE_ACTION = "CREATE TABLE " + TABLE_ACTION + '(' + KEY_ACTION
            + " TEXT PRIMARY KEY, " + KEY_COUNT + " INTEGER, " + KEY_POSITIVE + " INTEGER)";
    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_ACTION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP IF TABLE EXISTS" + TABLE_ACTION);
        onCreate(db);
    }

    public void createAction(Action action){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ACTION, action.getActivity());
        values.put(KEY_COUNT, action.getCount());
        values.put(KEY_POSITIVE, (action.getPositive()? 1 : 0));
    }

    public ArrayList<Action> getActions(){
        ArrayList<Action> actions = new ArrayList<>();
        String select = "SELECT * FROM " + TABLE_ACTION;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery(select, null);
        if(cur.moveToFirst()){
            do{
                Action act = new Action();
                act.setCount(cur.getInt(cur.getColumnIndex(KEY_COUNT)));
                act.setActivity(cur.getString(cur.getColumnIndex(KEY_ACTION)));

                int pos = cur.getInt(cur.getColumnIndex(KEY_POSITIVE));
                act.setPositive((pos != 0));

                actions.add(act);
            }while(cur.moveToNext());
        }
        return actions;
    }
}
