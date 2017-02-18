package colntrev.test;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class TrendActivity extends AppCompatActivity {
    LineGraphSeries<DataPoint> series, series2;
    //ArrayList<Object> dbSet = new ArrayList<>();
    //DatabaseHelper db = new DatabaseHelper();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trend);
        //dbSet = new ArrayList<>();
        //db = new DatabaseHelper();
        graph();
    }

    public void graph(){
        double x, y;
        GraphView graph = (GraphView) findViewById(R.id.graph);
        series = new LineGraphSeries<>();
        series2 = new LineGraphSeries<>();
        series.setColor(Color.RED);
        series2.setColor(Color.GREEN);
        // Todo iterate over arraylist and graph both lines
        for(int i = 0; i < 10; i++){
            x = i;
           if(i > 5){
                y = i + 1;
            } else {
                y = i + 2;
            }
            series.appendData(new DataPoint(x,y), true, 10);
        }
        for(int i = 0; i < 10; i++){
            x = i;
            if(i == 5){
                y = 4;
            } else if(i > 5) {
                y = i + 2;
            } else {
                y = i + 1;
            }
            series2.appendData(new DataPoint(x,y), true,10);
        }
        graph.addSeries(series);
        graph.addSeries(series2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_trend, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
