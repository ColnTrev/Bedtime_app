package colntrev.test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class AnalyzeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyze);
    }

    public void backToMain(View view) {
        Intent homeScreen = new Intent(this, MainActivity.class);
        final int result = 1;
        startActivity(homeScreen);
    }
}
