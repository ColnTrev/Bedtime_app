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

public class SetupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);






    }


    @Override
    protected void onStart() {
        super.onStart();



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
