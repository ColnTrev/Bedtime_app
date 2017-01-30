package colntrev.test;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.text.Editable;
import android.text.TextWatcher;
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

                // prototype 2: check for correct time input & in 00 or 00:00 format
                if(timeInput.length() == 2) {
                    Button recTime1 = (Button) findViewById(R.id.button_rec1);
                    Button recTime2 = (Button) findViewById(R.id.button_rec2);
                    Button recTime3 = (Button) findViewById(R.id.button_rec3);

                    recTime1.setText("10:30");
                    recTime2.setText("12:00");
                    recTime3.setText("1:30");
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

    public void setReminder(View view) throws IOException {
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
        Context context = getApplicationContext();
        try {
            FileInputStream fis = context.openFileInput(filename);
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
