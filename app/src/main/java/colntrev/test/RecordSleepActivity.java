package colntrev.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.Calendar;

public class RecordSleepActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_sleep);
    }

    public void setWakeTimeToNow(View view) {
        EditText wakeTime = (EditText) findViewById(R.id.editText_wakeT);
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY); // HOUR for AMPM format... but gotta check AM,PM
        int min = c.get(Calendar.MINUTE);
        wakeTime.setText(""+hour+":"+min);

    }
}
