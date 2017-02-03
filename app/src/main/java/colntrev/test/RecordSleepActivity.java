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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_sleep);
        calendar = Calendar.getInstance();

    }

    public void setWakeTimeToNow(View view) {
        EditText wakeTime = (EditText) findViewById(R.id.editText_wakeT);

        int hour = calendar.get(Calendar.HOUR); // HOUR for AMPM format... but gotta check AM,PM
        int min = calendar.get(Calendar.MINUTE);
        wakeTime.setText(""+hour+":"+min);



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
        EditText bedtime = (EditText) findViewById(R.id.editText_bedT);
        record = record + bedtime.getText().toString() + " ";
        EditText waketime = (EditText) findViewById(R.id.editText_wakeT);
        record = record + waketime.getText().toString()+ "\n";

        FileOutputStream fos = null;
        try {
            fos = openFileOutput(filename, Context.MODE_APPEND);
            fos.write(record.getBytes());
            fos.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }




    }
}
