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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;


/**
 * Created by Thy Vu on 2/20/17.
 */
public class SetupActivity extends AppCompatActivity {
    static final String PREFS_NAME = "Preferences";
    static final String PREF_KEY_WANTED_WAKE_TIME = "wantedWakeTime";
    static final String PREF_KEY_WANTED_WAKE_AMPM = "wantedWakeTimeAMPM";
    static final String PREF_KEY_REMIND_TIME = "remindTime";
    static final String PREF_KEY_REMIND_AMPM = "remindTimeAMPM";
    static final String PREF_KEY_REMIND_IS_ON ="remindIsOn";
    static final String PREF_KEY_WAKE_ALARM_IS_ON = "wakeAlarmIsOn";
    static final String PREF_KEY_ROWID="rowId";

    private final static int SLEEP_REMINDER_REQUEST_CODE = 54321;
    private final static int WAKE_ALARM_REQUEST_CODE = 12345;
    private final static String TAG = "katsu";

    private EditText editText_wantedWakeTime;
    private EditText editText_remindTime;
    private TextView textView_wantedWakeAMPM;
    private TextView textView_remindAMPM;
    private AlarmManager alarmManager;
    private SharedPreferences preferences;

    private Switch switch_wake, switch_sleep;

    private TimePicker timePicker_wake;
    private TimePicker timePicker_sleep;

    private PendingIntent pIntentSleep, pIntentWake;

    private boolean okToSetAlarm, okToSetReminder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);



        editText_remindTime = (EditText) findViewById(R.id.editText_setTime);
        textView_remindAMPM = (TextView) findViewById(R.id.textView_AMPM2);

        editText_wantedWakeTime = (EditText) findViewById(R.id.editText_time1);
        textView_wantedWakeAMPM = (TextView) findViewById(R.id.textView_AMPM);

        alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

        switch_sleep = (Switch) findViewById(R.id.switch_sleep);
        switch_wake = (Switch) findViewById(R.id.switch_wake);


        // restore reminder setting from predefined preferences
        preferences = getSharedPreferences(PREFS_NAME, 0);




    }


    @Override
    protected void onStart() {
        super.onStart();


        // switch label change to indicate on/off
        switch_sleep.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isOn) {
                if(isOn){
                    switch_sleep.setText("on");
                }else{
                    switch_sleep.setText("off");
                }
            }
        });
        switch_wake.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isOn) {
                if(isOn){
                    switch_wake.setText("on");
                }else{
                    switch_wake.setText("off");
                }
            }
        });

        // Display default suggested bed times.
        Button recTime1 = (Button) findViewById(R.id.button_rec1);
        recTime1.setText("10:15 PM");
        Button recTime2 = (Button) findViewById(R.id.button_rec2);
        recTime2.setText("11:45 PM");
        Button recTime3 = (Button) findViewById(R.id.button_rec3);
        recTime3.setText("1:15 AM");



        // Display saved time preferences
        Log.d("katsu", preferences.getString(PREF_KEY_WANTED_WAKE_TIME, "6:00"));
        editText_wantedWakeTime.setText(preferences.getString(PREF_KEY_WANTED_WAKE_TIME, "6:00"));
        textView_wantedWakeAMPM.setText(preferences.getString(PREF_KEY_WANTED_WAKE_AMPM, "AM"));
        editText_remindTime.setText(preferences.getString(PREF_KEY_REMIND_TIME, "10:30"));
        textView_remindAMPM.setText(preferences.getString(PREF_KEY_REMIND_AMPM, "PM"));

        okToSetReminder = true;
        okToSetAlarm = true;



        // set switch on/off based on saved preference
        if (preferences.getBoolean(PREF_KEY_REMIND_IS_ON, false)){
            switch_sleep.setChecked(true);
        }
        if (preferences.getBoolean(PREF_KEY_WAKE_ALARM_IS_ON, false)){
            switch_wake.setChecked(true);
        }

        // editing wake time changes the suggested sleep times
        editText_wantedWakeTime.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                //boolean okToProceed = false;
                String timeInput = editText_wantedWakeTime.getText().toString();
                if (timeInput.length() > 0 && timeInput.charAt(0) != ':') {

                    String[] hrMin = timeInput.split(":");

                    int hour = Integer.parseInt(hrMin[0]);
                    int minute = 0;
                    if (hrMin.length > 1)
                        minute = Integer.parseInt(hrMin[1]);
                    if (hour > 12 || minute > 59) {
                        okToSetAlarm = false;
                        editText_wantedWakeTime.setError("00:00 to 12:59 only");
                    }else{
                        okToSetAlarm=true;
                    }
                }else{
                    okToSetAlarm = false;
                }

                // to do : check for correct time input & in 00 or 00:00 format
                if(okToSetAlarm && (timeInput.length() == 2 || timeInput.length()== 4 || timeInput.length()==5)) {

                    Log.d("katsudon", "time input:"+timeInput);


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

                    // account for 12:xx AM vs 00:xx AM
                    String userWakeAMPM = textView_wantedWakeAMPM.getText().toString();
                    if (userWakeAMPM.equals("AM") && wakeHr==12){
                        wakeHr=0;
                    }

                    Button recTime1 = (Button) findViewById(R.id.button_rec1);
                    Button recTime2 = (Button) findViewById(R.id.button_rec2);
                    Button recTime3 = (Button) findViewById(R.id.button_rec3);

                    // Calculate and display suggested sleep time #1
                    int cycleTime = 90;
                    int sleepTime= (wakeHr+12) * 60 + wakeMin - 15 - cycleTime*6;
                    int hr = sleepTime/60;
                    int min = sleepTime%60;
                    String amPm = "PM";
                    if (hr > 12 || hr==12){
                        hr-= 12;
                        amPm = "AM";
                    }
                    if (min >= 10) {
                        recTime1.setText("" + hr + ":" + min+ " "+amPm);
                    }else{
                        recTime1.setText("" + hr + ":0" + min+ " "+amPm);
                    }

                    // Calculate and display suggested sleep time #2
                    sleepTime+= cycleTime;
                    hr = sleepTime/60;
                    min = sleepTime%60;
                    if (hr > 12 || hr==12){
                        hr-= 12;
                        amPm = "AM";
                    }else{
                        amPm = "PM";
                    }
                    if (min >= 10) {
                        recTime2.setText("" + hr + ":" + min+ " "+amPm);
                    }else{
                        recTime2.setText("" + hr + ":0" + min+ " "+amPm);
                    }

                    // Calculate and display suggested sleep time #3
                    sleepTime+= cycleTime;
                    hr = sleepTime/60;
                    min = sleepTime%60;
                    if (hr > 12 || hr==12){
                        hr-= 12;
                        amPm = "AM";
                    }else{
                        amPm = "PM";
                    }
                    if (min >= 10) {
                        recTime3.setText("" + hr + ":" + min+ " "+amPm);
                    }else{
                        recTime3.setText("" + hr + ":0" + min+ " "+amPm);
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


        // Display warning for bad time inputs (not 00:00 to 12:59)
        editText_remindTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String timeInput = editText_remindTime.getText().toString();
                Log.d(TAG, timeInput);
                if (timeInput.length() > 0 && timeInput.charAt(0) != ':') {
                    String[] hrMin = timeInput.split(":");
                    int hour = Integer.parseInt(hrMin[0]);
                    int minute = 0;
                    if (hrMin.length > 1)
                        minute = Integer.parseInt(hrMin[1]);
                    if (hour > 12 || minute > 59) {
                        //okToProceed = false;
                        okToSetReminder=false;
                        editText_remindTime.setError("00:00 to 12:59 only");
                    }else{
                        okToSetReminder = true;
                        //okToProceed=true;
                    }
                }else{
                    okToSetReminder = false;
                }
            }
        });


    }

    // Set alarm to user's input of time
    private void setNotification(int requestCode, String timeInput, String amPm)  {
        Log.d("katsu", "setting reminder");


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


        // Set Notification Service
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR, remindHr);
        calendar.set(Calendar.MINUTE, remindMin);
        calendar.set(Calendar.SECOND, 0);
        //*** // FIX
        if (amPm.equals("AM")){
            calendar.set(Calendar.AM_PM, Calendar.AM);
            Log.d(TAG, "AM");
        }else{
            calendar.set(Calendar.AM_PM,Calendar.PM);
            Log.d(TAG, "PM");
        }

        // Create Pending Intent
        Intent intent;
        PendingIntent pendingIntent;
        if (requestCode == SLEEP_REMINDER_REQUEST_CODE) {
            intent = new Intent(this, ReminderService.class);
            pendingIntent = PendingIntent.getService(this, SLEEP_REMINDER_REQUEST_CODE, intent, 0);
            Log.d(TAG, "req code = sleep");
        }else{
            intent = new Intent(this, WakeAlarmService.class);
            pendingIntent = PendingIntent.getService(this, WAKE_ALARM_REQUEST_CODE, intent, 0);
            Log.d(TAG, "req code = wake");

        }

        // make notification repeat every day
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                1000*60*60*24, pendingIntent);



        // Update preferences
        // key-value: wantedWakeTime - hh:mm, wantedWakeTimeAMPM - AM, wantedDuration - double
        SharedPreferences.Editor editor = preferences.edit();
        if (requestCode == WAKE_ALARM_REQUEST_CODE) {
            editor.putString(PREF_KEY_WANTED_WAKE_TIME, timeInput);
            editor.putString(PREF_KEY_WANTED_WAKE_AMPM, amPm);
            editor.putBoolean(PREF_KEY_WAKE_ALARM_IS_ON, true);
            Log.d("katsu", "pref = wake on");

        }else if(requestCode == SLEEP_REMINDER_REQUEST_CODE){
            editor.putString(PREF_KEY_REMIND_TIME, timeInput);
            editor.putString(PREF_KEY_REMIND_AMPM, amPm);
            editor.putBoolean(PREF_KEY_REMIND_IS_ON, true);
            Log.d("katsu", "pref = sleep on");

        }
        editor.commit();


        Log.d(TAG, "finished setting reminder:"+remindHr+":"+remindMin);
    }





    // Set reminder time to user-selected radio button
    public void onSuggestedTimeClick(View view) {
        String chosenTime = ((RadioButton)view).getText().toString();
        if (!chosenTime.equals("--:--")) {
            // "00:00" format
            String[] timeAMPM = chosenTime.split(" ");
            editText_remindTime.setText(timeAMPM[0]);
            textView_remindAMPM.setText(timeAMPM[1]);
        }


    }


    // Clicking TextView alternates "AM" to "PM" and vice versa
    public void toggleAMPM(View view) {
        TextView tvAMPM = (TextView) view;
        String currentText = tvAMPM.getText().toString();
        if(currentText.equals("AM")){
            tvAMPM.setText("PM");
        }else{
            tvAMPM.setText("AM");
        }
    }

    // Returns true if editText has "00:00" to "12:59" format
    // Param: editText      EditText with time text
    private boolean isValidTime(EditText editText){
        boolean isValid = false;
        String timeInput = editText.getText().toString();
        if (timeInput.length() > 0 ) {
            String[] hrMin = timeInput.split(":");
            int hour = Integer.parseInt(hrMin[0]);
            int minute = 0;
            if (hrMin.length > 1)
                minute = Integer.parseInt(hrMin[1]);
            if (hour > 12 || minute > 59) {
                isValid = false;
                editText.setError("00:00 to 12:59 only");
            }else{
                isValid=true;
            }

        }
        return isValid;
    }

    // Create background Service that will generate a bedtime and/or wake-up Notification to user
    // Param: view      SAVE button
    public void onClickSave(View view) {
        // SET WAKE TIME ALARM IF NEEDED
        if(switch_wake.isChecked()){
            Log.d(TAG, "switch wake is on");
            if (!okToSetAlarm){
                Toast.makeText(this, "ERROR: Please use 00:00 to 12:59 time", Toast.LENGTH_LONG).show();
            }else {
                // turn reminder ON
                String prefWakeTime = preferences.getString(PREF_KEY_WANTED_WAKE_TIME, "-");
                String prefWakeAMPM = preferences.getString(PREF_KEY_WANTED_WAKE_AMPM, "-");
                String wantedWakeTime = editText_wantedWakeTime.getText().toString();
                String wantedWakeAMPM = textView_wantedWakeAMPM.getText().toString();
                if (!(prefWakeTime.equals(wantedWakeTime) && prefWakeAMPM.equals(wantedWakeAMPM))) {
                    // turn previous wake up alarm OFF
                    if (preferences.getBoolean(PREF_KEY_WAKE_ALARM_IS_ON, false) && alarmManager != null) {
                        Intent reminderIntent = new Intent(this, WakeAlarmService.class);
                        PendingIntent pendingIntent = PendingIntent.getService(this, WAKE_ALARM_REQUEST_CODE, reminderIntent, 0);
                        alarmManager.cancel(pendingIntent);
                        pendingIntent.cancel();
                        Log.d(TAG, "turn previous wake up alarm OFF");
                    }
                    // turn new wakeup alarm on
                    setNotification(WAKE_ALARM_REQUEST_CODE, wantedWakeTime, wantedWakeAMPM);
                }
            }
        }else{
            // turn wake up alarm OFF
            Log.d(TAG, "switch wake is off");

            if (alarmManager!= null) {
                Intent reminderIntent = new Intent(this, WakeAlarmService.class);
                PendingIntent pendingIntent = PendingIntent.getService(this, WAKE_ALARM_REQUEST_CODE, reminderIntent, 0);
                alarmManager.cancel(pendingIntent);
                pendingIntent.cancel();
                Log.d(TAG,"turn wake up alarm OFF");
            }
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(PREF_KEY_WAKE_ALARM_IS_ON, false);
            Log.d(TAG, "pref wake off");
            editor.commit();

        }


        // SET SLEEP TIME REMINDER IF NEEDED
        if(switch_sleep.isChecked()){
            Log.d(TAG, "switch sleep on");

            if(!okToSetReminder){
                Toast.makeText(this, "ERROR: Please use 00:00 to 12:59 time", Toast.LENGTH_LONG).show();
            }else {
                // turn reminder ON
                String prefSleepTime = preferences.getString(PREF_KEY_REMIND_TIME, "10:30");
                String prefSleepAMPM = preferences.getString(PREF_KEY_REMIND_AMPM, "PM");
                String wantedSleepTime = editText_remindTime.getText().toString();
                String wantedSleepAMPM = textView_remindAMPM.getText().toString();
                if (!(prefSleepTime.equals(wantedSleepTime) && prefSleepAMPM.equals(wantedSleepAMPM))) {
                    // turn previous sleep reminder OFF
                    if (alarmManager != null) {
                        Intent reminderIntent = new Intent(this, ReminderService.class);
                        PendingIntent pendingIntent = PendingIntent.getService(this, SLEEP_REMINDER_REQUEST_CODE, reminderIntent, 0);
                        alarmManager.cancel(pendingIntent);
                        Log.d(TAG, "turn previous sleep reminder OFF");
                        pendingIntent.cancel();
                    }
                    // turn sleep notification on
                    setNotification(SLEEP_REMINDER_REQUEST_CODE, wantedSleepTime, wantedSleepAMPM);
                }
            }
        }else{
            // turn sleep reminder OFF
            Log.d(TAG, "switch sleep is off");

            if (alarmManager!= null) {
                Intent reminderIntent = new Intent(this, ReminderService.class);
                PendingIntent pendingIntent = PendingIntent.getService(this, SLEEP_REMINDER_REQUEST_CODE, reminderIntent, 0);
                alarmManager.cancel(pendingIntent);
                pendingIntent.cancel();
                Log.d(TAG, "turn sleep reminder OFF");
            }
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(PREF_KEY_REMIND_IS_ON, false);
            editor.commit();
        }

    }
}
