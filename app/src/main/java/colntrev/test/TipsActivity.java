package colntrev.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class TipsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips);

        TextView textView_tip = (TextView) findViewById(R.id.textView_tip);
        String str = "Are you having problem sleeping? Techniques to help you fall asleep:\n\n" +
                "+ Focus on slow breathing while in bed: inhale slowly, hold your breath for a few seconds, exhale by mouth very slowly repeat.\n\n" +
                "+ Keep your room slightly cooler. \n\n" +
                "+ Warm your feet using warm heatpacks or other ways.\n\n" +
                "+ Drink warm water. Avoid sweet or caffeinated drinks/food.\n\n" +
                "+ Warm bath.\n\n\n\n" +
                "What to do during daytime to sleep better:\n\n" +
                "+ Rigorous exercise, preferably early and not soon before bedtime.\n\n" +
                "+ Wake up early regularly.\n\n" +
                "+ Mindful meditation then set your intention to stop doing activities that kept you from proper bedtime.\n\n";
        textView_tip.setText(str);
    }
}
