// SetupActivity.java
// Thy Vu

package colntrev.test;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
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
import java.util.Calendar;

public class SetupActivity extends AppCompatActivity {
    final static int REMINDER_REQUEST_CODE = 54321;
    private EditText editText_reminderTime;
    private TextView textView_remindAMPM;
    private AlarmManager alarmManager;
    private Button button_remind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);


        editText_reminderTime = (EditText) findViewById(R.id.editText_setTime);
        textView_remindAMPM = (TextView) findViewById(R.id.textView_AMPM2);
        button_remind = (Button) findViewById(R.id.button_setReminder);
        alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);



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
                    if (min >= 10) {
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
                    if (min >= 10) {
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

    private void setReminder(View view)  {
        Log.d("nete", "setting reminder");
        // new version: in progress




        String timeInput = editText_reminderTime.getText().toString();
        ((Button)view).setText("reminder: "+textView_remindAMPM.getText().toString()+" "+timeInput);

        // Set alarm to user's input of reminder time
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



        Log.d("nete", "set reminder:"+remindHr+":"+remindMin);


    }

    public void saveDefaultReminder(View view) throws IOException {
        String filename = "defaultReminder";

        String defaultReminder = editText_reminderTime.getText().toString();

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
        //TextView textView = (TextView) findViewById(R.id.textView_test);
        //textView.setText("Default reminder is: " + sb.toString());

        // set wakeup time
        EditText setTime = (EditText) findViewById(R.id.editText_setTime);
        setTime.setText(sb.toString());

    }



    public void onSuggestedTimeClick(View view) {

        String chosenTime = ((RadioButton)view).getText().toString();
        if (!chosenTime.equals("--:--"))
            editText_reminderTime.setText(chosenTime);

    }

    public void onClickRemindButton(View view) {

        if(button_remind.getText().toString().toLowerCase().equals("reminder: off")){
            setReminder(view);
        }else{
            ((Button)view).setText("reminder: off");
            if (alarmManager!= null) {
                Intent reminderIntent = new Intent(this, ReminderService.class);
                PendingIntent pendingIntent = PendingIntent.getService(this, REMINDER_REQUEST_CODE, reminderIntent, 0);
                alarmManager.cancel(pendingIntent);
            }
        }

    }
}
