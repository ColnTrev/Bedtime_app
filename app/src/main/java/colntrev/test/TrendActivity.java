package colntrev.test;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;

public class TrendActivity extends AppCompatActivity {
    BarGraphSeries<DataPoint> series, series2;
    static final int DEFAULT_SLEEP_MAX = 14;
    static final int DEFAULT_SLEEP_MIN = 0;
    private SleepRecordDataSource datasource;
    ArrayList<SleepEntry> dbSet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trend);
        dbSet = new ArrayList<>();
        datasource = new SleepRecordDataSource(this);
        datasource.open();
        datasource.getAllSleepEntries(dbSet);
        graph();
    }

    public void graph(){
        double y;
        GraphView graph = (GraphView) findViewById(R.id.graph);
        GridLabelRenderer labels = graph.getGridLabelRenderer();
        series = new BarGraphSeries<>();
        series2 = new BarGraphSeries<>();
        series.setColor(Color.RED);
        series2.setColor(Color.GREEN);

        if(dbSet.isEmpty()){
            for(int i = 1; i < 8; i++){
                series.appendData(new DataPoint(i,8), true, 10);
            }
            for(int i = 1; i < 8; i++) {
                if (i == 5) {
                    y = 4;
                } else if (i > 5) {
                    y = i + 2;
                } else {
                    y = i + 1;
                }
                series2.appendData(new DataPoint(i, y), true, 10);
            }
        } else {
            for(SleepEntry se : dbSet){
                series.appendData(new DataPoint(new Long(se.getId()).doubleValue(),se.getWantedDuration()), true, 10);
                series2.appendData(new DataPoint(new Long(se.getId()).doubleValue(), se.getRealDuration()), true, 10);
            }
        }

        graph.addSeries(series);
        graph.addSeries(series2);
        graph.getViewport().setMinY(DEFAULT_SLEEP_MIN);
        graph.getViewport().setMaxY(DEFAULT_SLEEP_MAX);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinX(DEFAULT_SLEEP_MIN);
        graph.getViewport().setMaxX(DEFAULT_SLEEP_MAX);
        graph.getViewport().setXAxisBoundsManual(true);
        series.setTitle("Expected Sleep");
        series2.setTitle("Actual Sleep");
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
