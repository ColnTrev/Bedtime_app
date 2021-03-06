// RecordSleepActivity.java
// Thy Vu

package colntrev.test;

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
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;

/**
 * Created by Thy Vu on 2/20/17.
 */

public class RecordSleepActivity extends AppCompatActivity {
    private SleepRecordDataSource datasource;

    private Calendar calendar;
    private EditText editText_bedTime;
    private TextView textView_bedAMPM;
    private EditText editText_wakeTime;
    private TextView textView_wakeAMPM;

    private boolean okToSave_sleepT, okToSave_wakeT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_sleep);
        calendar = Calendar.getInstance();
        datasource = new SleepRecordDataSource(this);
        datasource.open();

        editText_bedTime = (EditText) findViewById(R.id.editText_bedT);
        textView_bedAMPM = (TextView) findViewById(R.id.textView_bedAMPM);
        editText_wakeTime = (EditText) findViewById(R.id.editText_wakeT);
        textView_wakeAMPM = (TextView) findViewById(R.id.textView_wakeAMPM);

        okToSave_sleepT = false;
        okToSave_wakeT= false;
    }

    @Override
    protected void onStart() {
        super.onStart();

        editText_bedTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            // Display error message for bad user's time input
            @Override
            public void afterTextChanged(Editable editable) {
                String timeInput = editText_bedTime.getText().toString();
                if (timeInput.length() > 0 && timeInput.charAt(0) != ':') {
                    String[] hrMin = timeInput.split(":");
                    int hour = Integer.parseInt(hrMin[0]);
                    int minute = 0;
                    if (hrMin.length > 1)
                        minute = Integer.parseInt(hrMin[1]);
                    if (hour > 12 || minute > 59) {
                        okToSave_sleepT=false;
                        editText_bedTime.setError("00:00 to 12:59 only");
                    }else{
                        okToSave_sleepT = true;
                    }
                }else{
                    okToSave_sleepT = false;
                }

            }
        });

        editText_wakeTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            // Display error message for bad user's time input
            @Override
            public void afterTextChanged(Editable editable) {
                String timeInput = editText_wakeTime.getText().toString();
                if (timeInput.length() > 0 && timeInput.charAt(0) != ':') {
                    String[] hrMin = timeInput.split(":");
                    int hour = Integer.parseInt(hrMin[0]);
                    int minute = 0;
                    if (hrMin.length > 1)
                        minute = Integer.parseInt(hrMin[1]);
                    if (hour > 12 || minute > 59) {
                        okToSave_wakeT = false;
                        editText_wakeTime.setError("00:00 to 12:59 only");
                    }else{
                        okToSave_wakeT = true;
                    }
                }else{
                    okToSave_wakeT = false;
                }
            }
        });
    }

    @Override
    protected void onResume() {
        datasource.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        datasource.close();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        datasource.close();
        super.onDestroy();
    }

    // Fill wakeTime EditText with current time
    public void setWakeTimeToNow(View view) {
        EditText wakeTime = (EditText) findViewById(R.id.editText_wakeT);

        int hour = calendar.get(Calendar.HOUR); // HOUR for AMPM format... but gotta check AM,PM
        int min = calendar.get(Calendar.MINUTE);
        if (min < 10) {
            wakeTime.setText("" + hour + ":0" + min);
        }else{
            wakeTime.setText("" + hour + ":" + min);
        }

        int hourMilitary = calendar.get(Calendar.HOUR_OF_DAY);
        if (hourMilitary > 12 || (hourMilitary == 12 && min > 0)){
            textView_wakeAMPM.setText("PM");
        }
    }



    // Record today's sleep statistics when user click "SAVE"
    public void recordSleepTime(View view) {
        Button btn = (Button) view;
        String btnText = btn.getText().toString().toUpperCase();

        if (btnText.equals("SAVE")) {
            if (okToSave_wakeT && okToSave_sleepT) {
                // Load SharedPreference for wantedSleep/Wake stats
                // DB: use auto increment key because of case where 2 sleeps occur within same date
                // eg: sleep AM wake up AM, go to sleep PM but wake up still PM same date
                // Use calendar to get today's date


                // get date
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int date = calendar.get(Calendar.DATE);
                String fullDate = "" + year + "/" + month + "/" + date;

                // get actual wake up / sleep stat
                String bedTime = editText_bedTime.getText().toString();
                String bedAMPM = textView_bedAMPM.getText().toString();
                String wakeTime = editText_wakeTime.getText().toString();
                String wakeAMPM = textView_wakeAMPM.getText().toString();
                double duration = getDurationInHr(bedAMPM, bedTime, wakeAMPM, wakeTime);
                if (duration <= 0) {
                    Toast.makeText(this, "Invalid time input", Toast.LENGTH_LONG).show();
                    btn.setError("Invalid time input");


                } else {

                    // get desired wake up / reminder stat preferences
                    SharedPreferences preferences = getSharedPreferences(SetupActivity.PREFS_NAME, 0);
                    String wantedWakeTime = preferences.getString(SetupActivity.PREF_KEY_WANTED_WAKE_TIME, "6:00");
                    String wantedWakeAMPM = preferences.getString(SetupActivity.PREF_KEY_WANTED_WAKE_AMPM, "AM");
                    String wantedSleepTime = preferences.getString(SetupActivity.PREF_KEY_REMIND_TIME, "10:00");
                    String wantedSleepAMPM = preferences.getString(SetupActivity.PREF_KEY_REMIND_AMPM, "PM");
                    double wantedDuration = getDurationInHr(wantedSleepAMPM, wantedSleepTime, wantedWakeAMPM, wantedWakeTime);

                    long rowID = datasource.addSleepEntry(fullDate, duration, wantedDuration, "");

                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putLong(SetupActivity.PREF_KEY_ROWID, rowID);
                    editor.commit();


                    Log.d("katsu", "added to db @ row " + rowID);
                    btn.setText("RECORD NIGHT ACTIVITY");
                }
            }else{
                Toast.makeText(this, "ERROR: Please use 00:00 to 12:59 time", Toast.LENGTH_LONG).show();
            }

        }else{
            Intent intent = new Intent(this, UpdateActivity.class);
            startActivity(intent);

        }

    }

    // Return sleep duration in hour unit
    private double getDurationInHr(String sleepAMPM, String sleepTime, String wakeAMPM, String wakeTime){
        double duration=0;

        // Extract hr and min from sleepTime String
        int sleepHr =0;
        int sleepMin=0;
        if (sleepTime.contains(":")) {
            sleepHr = Integer.parseInt(sleepTime.substring(0, sleepTime.indexOf(":")));
            if (sleepTime.length()>3) {
                sleepMin = Integer.parseInt(sleepTime.substring(sleepTime.indexOf(":") + 1));
            }
        }else{
            sleepHr = Integer.parseInt(sleepTime);
        }
        Log.d("katsu", "b correct" + sleepHr);
        if(sleepHr == 12 && sleepAMPM.equals("AM")){
            sleepHr = 0;

        }
        Log.d("katsu", "corrected" + sleepHr);

        // Extract hr and min from wakeTime String
        int wakeHr=0;
        int wakeMin=0;
        if (wakeTime.contains(":")) {
            wakeHr = Integer.parseInt(wakeTime.substring(0, wakeTime.indexOf(":")));
            if (wakeTime.length()>3) {
                wakeMin = Integer.parseInt(wakeTime.substring(wakeTime.indexOf(":") + 1));
            }
        }else{
            wakeHr = Integer.parseInt(wakeTime);
        }

        if(wakeHr == 12 && wakeAMPM.equals("AM")){
            wakeHr = 0;
        }

        // calculate duration
        // case not covered: went to sleep before noon and wake up afternoon SAME DAY
        if(sleepAMPM.equals(wakeAMPM)){
            // case1: went to sleep past midnight and wake up before noon next day
            // case2: went to sleep before midnight and wake up before midnight
            duration = (double)((wakeHr*60+wakeMin) - (sleepHr*60+sleepMin)) / 60;
        }else if (sleepAMPM.equals("PM") && wakeAMPM.equals("AM")){
            // case3: went to sleep at evening/night before 12 and wake up before noon next day
            duration = (double)(((12-sleepHr)*60 - sleepMin) + wakeHr*60+wakeMin) / 60;
        }else if (sleepAMPM.equals("AM") && wakeAMPM.equals("PM")){
            // case4: went to sleep past midnight and wake up in the afternoon

            duration = (double)(((wakeHr+12)*60+wakeMin) - (sleepHr*60+sleepMin)) / 60;
        }


        return duration;
    }

    // Toggle AM/PM text for TextView view
    public void toggleAMPM(View view) {
        TextView tvAMPM = (TextView) view;
        String currentText = tvAMPM.getText().toString();
        if(currentText.equals("AM")){
            tvAMPM.setText("PM");
        }else{
            tvAMPM.setText("AM");
        }
    }

    // Clicking view will show hours of sleep based on bed time and wake time
    public void showDuration(View view) {
        if (okToSave_sleepT && okToSave_wakeT) {
            double duration = getDurationInHr(textView_bedAMPM.getText().toString(), editText_bedTime.getText().toString(), textView_wakeAMPM.getText().toString(), editText_wakeTime.getText().toString());
            ((TextView) findViewById(R.id.textView_duration)).setText(String.format("%.2f", duration));
        }
    }
}
