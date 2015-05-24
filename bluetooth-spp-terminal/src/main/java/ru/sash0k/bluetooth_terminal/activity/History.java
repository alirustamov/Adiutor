package ru.sash0k.bluetooth_terminal.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

import ru.sash0k.bluetooth_terminal.R;


public class History extends Activity {

    private ListView lv;
    private ArrayAdapter<String> listAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        lv = (ListView)findViewById(R.id.listView2);
        String[] items = new String[] {"20.05.15 15:33", "22.05.15 10:25"};

        ArrayList<String> itemList = new ArrayList<String>();
        itemList.addAll(Arrays.asList(items));

        listAdapter = new ArrayAdapter<String> (this, R.layout.history_row, itemList);
        listAdapter.add("23.05.15 13:03");
        lv.setAdapter(listAdapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_history, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
