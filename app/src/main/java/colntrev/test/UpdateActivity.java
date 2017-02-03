package colntrev.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class UpdateActivity extends AppCompatActivity {
    ArrayList<String> items; // This list will get the previous list view contents and reload them
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        items = new ArrayList<>();
        populateLog();
    }

    private void populateLog(){
        items.add("Ice Cream");
        items.add("Pintrest");
        items.add("Wild Parties");
        items.add("Bluetooth Games");

        adapter = new ArrayAdapter<String>(this, R.layout.items, items);
        ListView list = (ListView) findViewById(R.id.logList);
        list.setAdapter(adapter);
    }

    public void addNewItem(View view) {
        EditText newItem = (EditText) findViewById(R.id.newItemAdd);
        String text = newItem.getText().toString();
        adapter.add(text); // adding new item to list view
    }
}
