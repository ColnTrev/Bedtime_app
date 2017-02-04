package colntrev.test;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

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
    FileInputStream input;
    InputStreamReader ips;
    BufferedReader buff;
    FileOutputStream output;
    static final String filename = "ActivityLog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        items = new ArrayList<>();
        //readFile();
        populateLog();
    }

    /*@Override
    protected void onDestroy() {
        super.onDestroy();
        saveFile();
    }

    private void readFile(){
        try {
            input = openFileInput(filename);
            ips = new InputStreamReader(input);
            buff = new BufferedReader(ips);
            String line;
            while((line = buff.readLine()) != null){
                items.add(line);
            }
            input.close();
            ips.close();
            buff.close();
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    private void saveFile(){
       ArrayList<String> save = new ArrayList<>();
        for(int i = 0; i < adapter.getCount(); i++){
            save.add(adapter.getItem(i));
        }
        try{
            output = openFileOutput(filename, Context.MODE_APPEND);
            for(String item : save){
                output.write(item.getBytes());
            }
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    private void populateLog(){
        items.add("Bluetooth Games");
        items.add("Waiting on my food to be delivered");
        items.add("Trying to donate food");
        items.add("Playing Robert's shape game");
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
