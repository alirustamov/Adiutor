package ru.sash0k.bluetooth_terminal.activity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import ru.sash0k.bluetooth_terminal.R;


public class Contacts extends Activity implements AbsListView.OnScrollListener {
    ListView lv;
    ListAdapter la;
    ListView 			  listView;
    ArrayList<PhoneModel> phoneModels;
    ArrayList<PhoneModel> phoneModelsSection;
    LinearLayout sideIndex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        listView = (ListView) findViewById(R.id.listView);
        phoneModels = new ArrayList<PhoneModel>();
        new getContactTask().execute((Void[])null);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contacts, menu);
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

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if(phoneModels.get(firstVisibleItem).isSection())
        {
            for(int i=0;i<sideIndex.getChildCount();i++)
            {
                if(((TextView)sideIndex.getChildAt(i)).getText().toString().equals(phoneModels.get(firstVisibleItem).getName()))
                    sideIndex.getChildAt(i).setBackgroundResource(R.drawable.abs__ic_clear_disabled);
                else
                    sideIndex.getChildAt(i).setBackgroundColor(Color.WHITE);
            }
        }
    }

    private class getContactTask extends AsyncTask<Void, Void, Void>
    {
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(Contacts.this);
            progressDialog.setIndeterminate(true);
            progressDialog.show();
        }

        @SuppressLint("DefaultLocale")
        @Override
        protected Void doInBackground(Void... params) {
            Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);
            while (phones.moveToNext())
            {
                PhoneModel phoneModel = new PhoneModel();
                phoneModel.setName(phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)).toUpperCase(Locale.ENGLISH));
                phoneModel.setPhone(phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                phoneModels.add(phoneModel);
            }
            phones.close();
            Collections.sort(phoneModels, new PhoneModel());
            /**
             * Get Sections
             */
            String section = "";
            phoneModelsSection = new ArrayList<PhoneModel>();
            for(PhoneModel model : phoneModels)
            {
                if(!section.equals(model.getName().substring(0, 1)))
                {
                    section = model.getName().substring(0, 1);
                    PhoneModel phoneModel = new PhoneModel();
                    phoneModel.setName(section);
                    phoneModel.setSection(true);
                    phoneModelsSection.add(phoneModel);
                }
            }
            phoneModels.addAll(phoneModelsSection);
            Collections.sort(phoneModels, new PhoneModel());
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            sideView(phoneModelsSection);
            progressDialog.dismiss();
            listView.setAdapter(new ContactListAdapter(getApplicationContext(), phoneModels));
        }

    }



    /**
     * Implement Side View
     * @param phoneModelsSection
     */
    @SuppressWarnings("deprecation")
    public void sideView(ArrayList<PhoneModel> phoneModelsSection)
    {
        sideIndex = (LinearLayout) findViewById(R.id.sideIndex);
        int sideIndexHeight = Contacts.this.getWindowManager().getDefaultDisplay().getHeight();
        sideIndex.removeAllViews();

        // TextView for every visible item
        TextView sectors = null;

        // maximal number of item, which could be displayed
        int indexMaxSize = (int) Math.floor(sideIndexHeight / phoneModelsSection.size());


        for (int i = 0; i < phoneModelsSection.size(); i++)
        {
            sectors = new TextView(this);
            sectors.setText(phoneModelsSection.get(i).getName());
            //sectors.setTextSize(indexMaxSize);
            sectors.setGravity(Gravity.CENTER);
            sectors.setTextColor(Color.BLACK);
            sectors.setOnClickListener(new SideSectorClick());
            ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, indexMaxSize, 1);
            sectors.setLayoutParams(params);
            sideIndex.addView(sectors);
        }

        listView.setOnScrollListener(this);
    }

    /**
     * Onlick Sector Class
     */

    private class SideSectorClick implements View.OnClickListener
    {

        @Override
        public void onClick(View v) {
            /**
             * Set Child back to unselected
             */
            for(int i=0;i<sideIndex.getChildCount();i++)
                sideIndex.getChildAt(i).setBackgroundColor(Color.WHITE);

            TextView textView = (TextView) v;
            for(int i=0; i<phoneModels.size(); i++)
            {
                if(phoneModels.get(i).isSection() && phoneModels.get(i).getName().startsWith(textView.getText().toString()))
                {
                    listView.setSelection(i);
                    textView.setBackgroundResource(R.drawable.abs__btn_cab_done_default_holo_dark);
                    break;
                }
            }
        }

    }


}
