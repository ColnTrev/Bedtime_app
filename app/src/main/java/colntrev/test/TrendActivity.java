package colntrev.test;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class TrendActivity extends AppCompatActivity {
    LineGraphSeries<DataPoint> series, series2;
    static final int DEFAULT_SLEEP_MAX = 14;
    static final int DEFAULT_SLEEP_MIN = 0;
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
        GridLabelRenderer labels = graph.getGridLabelRenderer();
        series = new LineGraphSeries<>();
        series2 = new LineGraphSeries<>();
        series.setColor(Color.RED);
        series2.setColor(Color.GREEN);
        // Todo iterate over arraylist and graph both lines
        for(int i = 1; i < 8; i++){
            series.appendData(new DataPoint(i,8), true, 10);
        }
        for(int i = 1; i < 8; i++) {
            if(i == 5){
                y = 4;
            } else if(i > 5) {
                y = i + 2;
            } else {
                y = i + 1;
            }
            series2.appendData(new DataPoint(i,y), true,10);
        }
        graph.addSeries(series);
        graph.addSeries(series2);
        graph.getViewport().setMinY(DEFAULT_SLEEP_MIN);
        graph.getViewport().setMaxY(DEFAULT_SLEEP_MAX);
        graph.getViewport().setYAxisBoundsManual(true);
        //graph.getViewport().setScrollable(true); DO IN BETA
        series.setTitle("Expected Sleep");
        series2.setTitle("Actual Sleep");
        series.setDrawDataPoints(true);
        series2.setDrawDataPoints(true);
        labels.setHorizontalAxisTitle("Number of Days");
        labels.setVerticalAxisTitle("Hours of Sleep");
        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_trend, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
