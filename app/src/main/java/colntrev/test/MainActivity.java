package colntrev.test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Thy!
    }

    public void goAnalyze(View view) {
        Intent analyze = new Intent(this,AnalyzeActivity.class);
        startActivity(analyze);
    }

    public void goSettings(View view) {
        Intent settings = new Intent(this, SetupActivity.class);
        startActivity(settings);
    }

    public void goTrend(View view) {
        Intent trend = new Intent(this, TrendActivity.class);
        startActivity(trend);
    }

    public void goTips(View view) {
        Intent tips = new Intent(this, TipsActivity.class);
        startActivity(tips);
    }

    public void goUpdate(View view) {
        Intent update = new Intent(this, UpdateActivity.class);
        startActivity(update);
    }

    public void goRecordSleep(View view) {
        Intent record = new Intent(this, RecordSleepActivity.class);
        startActivity(record);
    }
}
