package colntrev.test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class AnalyzeActivity extends AppCompatActivity {

    private SleepRecordDataSource datasource;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyze);
        datasource = new SleepRecordDataSource(this);
        datasource.open();
        ArrayList<String> topActivities = new ArrayList<>();
        datasource.getTopActivities(topActivities);
        TextView textView = (TextView) findViewById(R.id.displayText);
        String displayStr = "The past nights where you got less than 8 hours of sleep you were:\n";
        for (String se : topActivities){
            displayStr += se + " \n" ;
        }

        textView.setText(displayStr);

    }

    public void backToMain(View view) {
        Intent homeScreen = new Intent(this, MainActivity.class);
        startActivity(homeScreen);
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
}
