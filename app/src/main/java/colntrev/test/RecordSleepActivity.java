// RecordSleepActivity.java
// Thy Vu

package colntrev.test;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;

public class RecordSleepActivity extends AppCompatActivity {
    private Calendar calendar;
    private EditText bedtime;
    private TextView bed_AMPM;
    private EditText waketime;
    private TextView wake_AMPM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_sleep);
        calendar = Calendar.getInstance();

        bedtime = (EditText) findViewById(R.id.editText_bedT);
        bed_AMPM = (TextView) findViewById(R.id.textView_bedAMPM);
        waketime = (EditText) findViewById(R.id.editText_wakeT);
        wake_AMPM = (TextView) findViewById(R.id.textView_wakeAMPM);

    }

    public void setWakeTimeToNow(View view) {
        EditText wakeTime = (EditText) findViewById(R.id.editText_wakeT);

        int hour = calendar.get(Calendar.HOUR); // HOUR for AMPM format... but gotta check AM,PM
        int min = calendar.get(Calendar.MINUTE);
        wakeTime.setText(""+hour+":"+min);

        int hourMilitary = calendar.get(Calendar.HOUR_OF_DAY);
        if (hourMilitary > 12 || (hourMilitary == 12 && min > 0)){
            wake_AMPM.setText("PM");
        }



    }

    public void loadSleepRecord(View view) {
        String filename = "SleepRecord";
        StringBuilder sb = new StringBuilder();
        try {
            FileInputStream fis = openFileInput(filename);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            String line;
            while ((line = bufferedReader.readLine()) != null){
                sb.append("\n"+line);

            }
            fis.close();
            isr.close();
            bufferedReader.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        TextView textView_test = (TextView) findViewById(R.id.textView_record);
        textView_test.setText(sb.toString());


    }

    // Record sleep statistics when user click "SAVE"
    public void recordSleepTime(View view) {
        String filename = "SleepRecord";
        String record = ""+ calendar.get(Calendar.YEAR) + " " + calendar.get(Calendar.MONTH) + " "
                + calendar.get(Calendar.DATE)+ " ";



        record = record + bedtime.getText().toString() + " " + bed_AMPM.getText().toString() + " "
                + waketime.getText().toString()+ " "+ wake_AMPM.getText().toString() + "\n";

        FileOutputStream fos = null;
        try {
            fos = openFileOutput(filename, Context.MODE_APPEND);
            fos.write(record.getBytes());
            fos.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
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

        // calculate duration
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

    public void showDuration(View view) {
        double duration = getDurationInHr(bed_AMPM.getText().toString(), bedtime.getText().toString(), wake_AMPM.getText().toString(),waketime.getText().toString());
        ((TextView)findViewById(R.id.textView_duration)).setText(String.format("%.2f", duration));
    }
}
