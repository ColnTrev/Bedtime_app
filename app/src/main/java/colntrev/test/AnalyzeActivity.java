package colntrev.test;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class AnalyzeActivity extends AppCompatActivity {


    private SleepRecordDataSource datasource;
    private ArrayList<SleepEntry> allSleep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyze);
        datasource = new SleepRecordDataSource(this);
        datasource.open();
        ArrayList<String> topActivities = new ArrayList<>();
        datasource.getTopActivities(topActivities);

        allSleep = new ArrayList<>();
        datasource.getAllSleepEntries(allSleep);

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

    public void sendEmail(View view) {
        TextView analyze = (TextView) findViewById(R.id.displayText);
        String progress = analyze.getText().toString();

        String sleepRecords = "";
        for(SleepEntry entry : allSleep){
            sleepRecords += "Date:" + entry.getDate() + " Hours slept:"+
                    (double)Math.round(entry.getRealDuration()*100)/100
                    +" Activity before sleep:"+entry.getActivity()+"\n";
        }

        progress += "\n\n\nBelow is your activity record.\n" + sleepRecords;


        try {
            Intent gmail = new Intent(Intent.ACTION_SEND);
            gmail.setType("message/rfc822");
            gmail.putExtra(Intent.EXTRA_SUBJECT, "My progress");
            gmail.putExtra(Intent.EXTRA_TEXT, progress);
            startActivity( Intent.createChooser(gmail, "Email to..."));
        } catch(ActivityNotFoundException e){
            Log.d("sendEmail", e.toString());
        }
    }


}
