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
    Calendar calendar;
    EditText bedtime;
    TextView bed_AMPM;
    EditText waketime;
    TextView wake_AMPM;
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
