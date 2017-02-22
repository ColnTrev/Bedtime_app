package colntrev.test;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
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
        items.add("Bluetooth Games");
        items.add("Waiting on my food to be delivered");
        items.add("Trying to donate food");
        items.add("Playing Robert's shape game");
        adapter = new ArrayAdapter<String>(this, R.layout.items, items);
        ListView list = (ListView) findViewById(R.id.logList);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = ((TextView)view).getText().toString();
                EditText et = (EditText) findViewById(R.id.newItemAdd);
                et.setText(item);
            }
        });
    }

    public void addNewItem(View view) {
        EditText newItem = (EditText) findViewById(R.id.newItemAdd);
        String text = newItem.getText().toString();
        adapter.add(text); // adding new item to list view
    }
}
