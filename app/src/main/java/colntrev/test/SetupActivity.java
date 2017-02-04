// SetupActivity.java
// Thy Vu

package colntrev.test;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;

public class SetupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);






    }


    @Override
    protected void onStart() {
        super.onStart();

        final EditText time1 = (EditText) findViewById(R.id.editText_time1);
        time1.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                String timeInput = time1.getText().toString();

                // to do : check for correct time input & in 00 or 00:00 format
                if(timeInput.length() == 2 || timeInput.length()== 4 || timeInput.length()==5) {

                    Log.d("katsudon", "time input:"+timeInput);

                    //Log.d("katsudon", timeInput.substring(0,timeInput.indexOf(":")));

                    // MUST FIX: currently only takes 00, 00:, or 0: . Minute is ignored
                    // wake or sleep time
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

                    // dummy times: works for wake time only
                    int cycleTime = 90;
                    int sleepTime = (wakeHr+12) * 60 + wakeMin - 15 - cycleTime*5;
                    int hr = sleepTime/60;
                    int min = sleepTime%60;
                    if (hr > 12 || hr==12){
                        hr-= 12;
                    }
                    if (min > 10) {
                        recTime1.setText("" + hr + ":" + min);
                    }else{
                        recTime1.setText("" + hr + ":0" + min);
                    }

                    sleepTime+= cycleTime;
                    hr = sleepTime/60;
                    min = sleepTime%60;
                    if (hr > 12 || hr==12){
                        hr-= 12;
                    }
                    if (min > 10) {
                        recTime2.setText("" + hr + ":" + min);
                    }else{
                        recTime2.setText("" + hr + ":0" + min);
                    }

                    sleepTime+= cycleTime;
                    hr = sleepTime/60;
                    min = sleepTime%60;
                    if (hr > 12 || hr==12){
                        hr-= 12;
                    }
                    if (min > 10) {
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

    public void setReminder(View view)  {
        Log.d("nete", "setting reminder");

        Intent reminderIntent = new Intent(this, ReminderBroadcastReceiver.class);
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getService(this,0,reminderIntent,0);

        // alarm in 5 seconds
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+ 1000*5, pendingIntent);
        Log.d("nete", "set reminder");


    }

    public void saveDefaultReminder(View view) throws IOException {
        String filename = "defaultReminder";

        EditText textView = (EditText) findViewById(R.id.editText_setTime);
        String defaultReminder = textView.getText().toString();

        FileOutputStream fos = openFileOutput(filename, Context.MODE_PRIVATE);

        fos.write(defaultReminder.getBytes());
        fos.close();

    }

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
        TextView textView = (TextView) findViewById(R.id.textView_test);
        textView.setText("Default reminder is: " + sb.toString());

        // set wakeup time
        EditText setTime = (EditText) findViewById(R.id.editText_setTime);
        setTime.setText(sb.toString());

    }
}
