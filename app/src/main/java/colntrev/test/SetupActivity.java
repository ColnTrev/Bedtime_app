// SetupActivity.java
// Thy Vu

package colntrev.test;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;

public class SetupActivity extends AppCompatActivity {
    static final String PREFS_NAME = "Preferences";
    static final String PREF_KEY_WANTED_WAKE_TIME = "wantedWakeTime";
    static final String PREF_KEY_WANTED_WAKE_AMPM = "wantedWakeTimeAMPM";
    static final String PREF_KEY_REMIND_TIME = "remindTime";
    static final String PREF_KEY_REMIND_AMPM = "remindTimeAMPM";
    static final String PREF_KEY_REMIND_IS_ON ="remindIsOn";
    static final String PREF_KEY_ROWID="rowId";

    private final static int REMINDER_REQUEST_CODE = 54321;
    private EditText editText_wantedWakeTime, editText_remindTime;
    private TextView textView_wantedWakeAMPM, textView_remindAMPM;
    private AlarmManager alarmManager;
    private Button button_remind;
    private SharedPreferences preferences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);


        editText_remindTime = (EditText) findViewById(R.id.editText_setTime);
        textView_remindAMPM = (TextView) findViewById(R.id.textView_AMPM2);

        editText_wantedWakeTime = (EditText) findViewById(R.id.editText_time1);
        textView_wantedWakeAMPM = (TextView) findViewById(R.id.textView_AMPM);

        button_remind = (Button) findViewById(R.id.button_setReminder);
        alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);


        // restore reminder setting from predefined preferences
        preferences = getSharedPreferences(PREFS_NAME, 0);




    }


    @Override
    protected void onStart() {
        super.onStart();
/*
        editText_wantedWakeTime.setText(preferences.getString(PREF_KEY_WANTED_WAKE_TIME, "6:00"));
        textView_wantedWakeAMPM.setText(preferences.getString(PREF_KEY_WANTED_WAKE_AMPM, "WA"));
        editText_remindTime.setText(preferences.getString(PREF_KEY_REMIND_TIME, "10:00"));
        textView_remindAMPM.setText(preferences.getString(PREF_KEY_REMIND_AMPM, "PM"));
*/

        Button recTime1 = (Button) findViewById(R.id.button_rec1);
        recTime1.setText("10:15");
        Button recTime2 = (Button) findViewById(R.id.button_rec2);
        recTime2.setText("11:45");
        Button recTime3 = (Button) findViewById(R.id.button_rec3);
        recTime3.setText("1:15");


        Log.d("katsu", preferences.getString(PREF_KEY_WANTED_WAKE_TIME, "6:00"));
        editText_wantedWakeTime.setText(preferences.getString(PREF_KEY_WANTED_WAKE_TIME, "6:00"));
        textView_wantedWakeAMPM.setText(preferences.getString(PREF_KEY_WANTED_WAKE_AMPM, "AM"));
        editText_remindTime.setText(preferences.getString(PREF_KEY_REMIND_TIME, "10:30"));
        textView_remindAMPM.setText(preferences.getString(PREF_KEY_REMIND_AMPM, "PM"));

        if (preferences.getBoolean(PREF_KEY_REMIND_IS_ON, false)){
            button_remind.setText("REMINDER: "+preferences.getString(PREF_KEY_REMIND_TIME, "10:30") +" "+
                    preferences.getString(PREF_KEY_REMIND_AMPM, "PM"));
        }


        editText_wantedWakeTime.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                String timeInput = editText_wantedWakeTime.getText().toString();

                // to do : check for correct time input & in 00 or 00:00 format
                if(timeInput.length() == 2 || timeInput.length()== 4 || timeInput.length()==5) {

                    Log.d("katsudon", "time input:"+timeInput);

                    //Log.d("katsudon", timeInput.substring(0,timeInput.indexOf(":")));


                    // get user-desired wake time
                    int wakeHr;
                    int wakeMin=0;
                    if (timeInput.contains(":")) {
                        wakeHr = Integer.parseInt(timeInput.substring(0, timeInput.indexOf(":")));
                        if (timeInput.length()>3) {
                            wakeMin = Integer.parseInt(timeInput.substring(timeInput.indexOf(":") + 1));
                        }
                    }else{
                        wakeHr = Integer.parseInt(timeInput);
                    }


                    Button recTime1 = (Button) findViewById(R.id.button_rec1);
                    Button recTime2 = (Button) findViewById(R.id.button_rec2);
                    Button recTime3 = (Button) findViewById(R.id.button_rec3);

                    // Note: algo works for suggested sleep times only
                    // Calculate and display suggested sleep time #1
                    int cycleTime = 90;
                    int sleepTime = (wakeHr+12) * 60 + wakeMin - 15 - cycleTime*5;
                    int hr = sleepTime/60;
                    int min = sleepTime%60;
                    if (hr > 12 || hr==12){
                        hr-= 12;
                    }
                    if (min >= 10) {
                        recTime1.setText("" + hr + ":" + min);
                    }else{
                        recTime1.setText("" + hr + ":0" + min);
                    }

                    // Calculate and display suggested sleep time #2
                    sleepTime+= cycleTime;
                    hr = sleepTime/60;
                    min = sleepTime%60;
                    if (hr > 12 || hr==12){
                        hr-= 12;
                    }
                    if (min >= 10) {
                        recTime2.setText("" + hr + ":" + min);
                    }else{
                        recTime2.setText("" + hr + ":0" + min);
                    }

                    // Calculate and display suggested sleep time #3
                    sleepTime+= cycleTime;
                    hr = sleepTime/60;
                    min = sleepTime%60;
                    if (hr > 12 || hr==12){
                        hr-= 12;
                    }
                    if (min >= 10) {
                        recTime3.setText("" + hr + ":" + min);
                    }else{
                        recTime3.setText("" + hr + ":0" + min);
                    }
                }

            }



            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
            }

        });


    }

    // Set reminder based on current user's input in "Set time: --:--"
    private void setReminder(View view)  {
        Log.d("nete", "setting reminder");
        // new version: in progress

        String timeInput = editText_remindTime.getText().toString();

        // Set alarm to user's input of reminder time

        // Get user's reminder time input
        int remindHr;
        int remindMin=0;
        if (timeInput.contains(":")) {
            remindHr = Integer.parseInt(timeInput.substring(0, timeInput.indexOf(":")));
            if (timeInput.length()>3) {
                remindMin = Integer.parseInt(timeInput.substring(timeInput.indexOf(":") + 1));
            }
        }else{
            remindHr = Integer.parseInt(timeInput);
        }


        // Set alarm to user's reminder time
        if (textView_remindAMPM.getText().toString().equals("PM")&& remindHr<12){
            remindHr += 12;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, remindHr);
        calendar.set(Calendar.MINUTE, remindMin);
        Intent reminderIntent = new Intent(this, ReminderService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this,REMINDER_REQUEST_CODE,reminderIntent,0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                1000*60*60*24, pendingIntent);



        // PUT SHAREDPREFERENCE HERE
        // key-value: wantedWakeTime - hh:mm, wantedWakeTimeAMPM - AM, wantedDuration - double
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PREF_KEY_WANTED_WAKE_TIME, editText_wantedWakeTime.getText().toString());
        editor.putString(PREF_KEY_WANTED_WAKE_AMPM, textView_wantedWakeAMPM.getText().toString());
        editor.putString(PREF_KEY_REMIND_TIME, editText_remindTime.getText().toString());
        editor.putString(PREF_KEY_REMIND_AMPM, textView_remindAMPM.getText().toString());
        editor.putBoolean(PREF_KEY_REMIND_IS_ON,true);
        editor.commit();


        // Reminder button's text includes reminder time
        ((Button)view).setText("reminder: "+timeInput+ " " + textView_remindAMPM.getText().toString());



        Log.d("nete", "set reminder:"+remindHr+":"+remindMin);


    }

    // Save default reminder time
    public void saveDefaultReminder(View view) throws IOException {


        // OLD
        String filename = "defaultReminder";

        String defaultReminder = editText_remindTime.getText().toString();

        FileOutputStream fos = openFileOutput(filename, Context.MODE_PRIVATE);

        fos.write(defaultReminder.getBytes());
        fos.close();

        // END OLD

    }

    // Load default reminder time
    public void displayDefaultSetting(View view) {

        String filename = "defaultReminder";
        StringBuilder sb = new StringBuilder();
        String content;
        //Context context = getApplicationContext();
        try {
            FileInputStream fis = openFileInput(filename);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            String line;
            while ((line = bufferedReader.readLine()) != null){
                sb.append(line);

            }
            fis.close();
            isr.close();
            bufferedReader.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // for debug
        //TextView textView = (TextView) findViewById(R.id.textView_test);
        //textView.setText("Default reminder is: " + sb.toString());

        // set wakeup time
        EditText setTime = (EditText) findViewById(R.id.editText_setTime);
        setTime.setText(sb.toString());

    }


    // Set reminder time input to user-selected suggested time
    public void onSuggestedTimeClick(View view) {

        String chosenTime = ((RadioButton)view).getText().toString();
        if (!chosenTime.equals("--:--")) {
            editText_remindTime.setText(chosenTime);
            int hr = Integer.parseInt(chosenTime.substring(0,chosenTime.indexOf(":")));
            // Just simplifying things for user. Not best solution.
            if(hr < 6){
                textView_remindAMPM.setText("AM");
            }else{
                textView_remindAMPM.setText("PM");
            }
        }


    }

    // If reminder is OFF, turn reminder ON.
    // If reminder is ON, turn reminder OFF.
    public void onClickRemindButton(View view) {

        if(button_remind.getText().toString().toLowerCase().equals("reminder: off")){
            // turn reminder ON
            setReminder(view);
        }else{
            // turn reminder OFF
            if (alarmManager!= null) {
                Intent reminderIntent = new Intent(this, ReminderService.class);
                PendingIntent pendingIntent = PendingIntent.getService(this, REMINDER_REQUEST_CODE, reminderIntent, 0);
                alarmManager.cancel(pendingIntent);
            }
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(PREF_KEY_REMIND_IS_ON, false);
            editor.commit();


            ((Button)view).setText("reminder: off");
        }

    }


    public void testDb(View view) {
        SleepRecordDataSource datasource = new SleepRecordDataSource(this);
        datasource.open();

        ArrayList<SleepEntry> sleepEntries = new ArrayList<>();
        datasource.getAllSleepEntries(sleepEntries);

        ArrayList<String> topAct = new ArrayList<>();
        datasource.getTopActivities(topAct);


        datasource.close();


    }

    public void toggleAMPM(View view) {
        TextView tvAMPM = (TextView) view;
        String currentText = tvAMPM.getText().toString();
        if(currentText.equals("AM")){
            tvAMPM.setText("PM");
        }else{
            tvAMPM.setText("AM");
        }
    }
}
