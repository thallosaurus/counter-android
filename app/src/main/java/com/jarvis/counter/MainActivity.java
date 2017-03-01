package com.jarvis.counter;

import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity {


    SharedPreferences preferences;
    public static final String MY_PREFS_NAME = "MyPrefsFile";

    List<Integer> listItems = null;
    ArrayAdapter adapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        preferences = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
        String restoredText = preferences.getString("key", null);
        if (restoredText == null) {
            listItems = new ArrayList<Integer>();
        } else {
            JSONArray m = null;
            ArrayList<Integer> newAL = new ArrayList<Integer>();
            try {
                m = new JSONArray(restoredText);
                if (m != null) {
                    for (int i=0;i<m.length();i++){
                        newAL.add(m.getInt(i));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            listItems = newAL;
            //adapter.notifyDataSetChanged();
        }

        ListView lv = (ListView) findViewById(R.id.listview);
        adapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_list_item_1, listItems);
        lv.setAdapter(adapter);
        adapter.setNotifyOnChange(true);
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //On Remove:
                showDialog(parent, position);
                return false;
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int i = Integer.parseInt(parent.getItemAtPosition(position).toString());
                i++;
                listItems.set(position, i);
                adapter.notifyDataSetChanged();
            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageResource(R.drawable.ic_add_white_24dp);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listItems.add(0);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            //System.out.println("Pressed Overflow");
            showCredits();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showDialog(final AdapterView<?> parent, final int position) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_title)
                .setMessage(R.string.dialog_message)
                .setPositiveButton(R.string.dialog_yes,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                System.out.println(parent.getItemAtPosition(position).toString());
                                adapter.remove(parent.getItemAtPosition(position));
                                adapter.notifyDataSetChanged();

                            }
                        })
                .setNegativeButton(
                        R.string.dialog_no,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                //Do Something Here
                            }
                        }).show();
    }

    public void showCredits() {
        Toast.makeText(this, R.string.about, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = preferences.edit();

        JSONArray mJSONArray = new JSONArray(listItems);
        editor.putString("key", mJSONArray.toString());

        editor.commit();
        System.out.println("Wrote to space");
        System.out.println(listItems.toString());
    }
}
