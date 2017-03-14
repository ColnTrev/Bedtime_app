package colntrev.test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


// THIS CLASS IS UNUSED, GIT GAVE US TROUBLE DELETING IT SO IT'S HERE BUT IGNORE IT
// HAVE A GOOD DAY

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
    }

    public void goTrend(View view) {
        Intent trend = new Intent(this, TrendActivity.class);
        startActivity(trend);
    }

    public void goUpdate(View view) {
        Intent update = new Intent(this, UpdateActivity.class);
        startActivity(update);
    }

    public void goBack(View view) {
        Intent back = new Intent(this, MainActivity.class);
        startActivity(back);
    }

}
