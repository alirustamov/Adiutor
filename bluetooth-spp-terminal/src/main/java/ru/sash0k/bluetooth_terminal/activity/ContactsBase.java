package ru.sash0k.bluetooth_terminal.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ru.sash0k.bluetooth_terminal.R;


public class ContactsBase extends Activity {
    Button addContact;
    Activity activity;
    ListView listView;
    private String PREFS_NAME = "ALI_DATA";

    ArrayList<PhoneModel> phoneModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_base);
        activity = this;
        phoneModels = new ArrayList<PhoneModel>();

        listView = (ListView) findViewById(R.id.listJe);

        addContact = (Button) findViewById(R.id.addContact);
        addContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent contactsActivity = new Intent();
                contactsActivity.setClass(activity, Contacts.class);
                startActivity(contactsActivity);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String contactsDB = prefs.getString("contacts", "[]");
        try {
            JSONArray array = new JSONArray(contactsDB);
            for (int i = 0; i < array.length(); i++){
                PhoneModel phoneModel = new PhoneModel();
                JSONObject obj =array.getJSONObject(i);
                phoneModel.setName(obj.get("name").toString());
                phoneModel.setPhone(obj.get("phone").toString());
                phoneModels.add(phoneModel);
            }
            listView.setAdapter(new ContactListAdapter(getApplicationContext(), phoneModels));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contacts_base, menu);
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
