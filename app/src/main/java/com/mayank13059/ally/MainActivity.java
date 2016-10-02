package com.mayank13059.ally;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private StableArrayAdapter adapter;
    private List<String> values;
    private List<String> values2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }

        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("masterPassword", Context.MODE_PRIVATE);

        String masterPassword = sharedPref.getString("password", "");

        if(masterPassword.equals("")) {
            Log.e("MAIN","Set MasterPassword");
            Intent intent = new Intent(this, SetMasterPassword.class);
            startActivity(intent);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ElementSelectionActivity.class);
                finish();
                startActivity(intent);
            }
        });


        ListView listview = (ListView) findViewById(R.id.listview);

        populateAdapter();

        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                Intent intent = new Intent(getApplicationContext(), FiddleWithFiles.class);
                if(values2.get(position).equals("Password")) {
                    intent = new Intent(getApplicationContext(), passwordCheck.class);
                }
                intent.putExtra("pos", position);
                intent.putExtra("fname", values.get(position));
                intent.putExtra("type", values2.get(position));
                finish();
                startActivity(intent);
            }

        });
    }

    private class StableArrayAdapter extends ArrayAdapter<String> {

        private final Context context;
        private final List<String> values;
        private final List<String> values2;

        public StableArrayAdapter(Context context, List<String> values, List<String> values2) {
            super(context, -1, values);
            this.context = context;
            this.values = values;
            this.values2 = values2;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View rowView = inflater.inflate(R.layout.list_single_item, parent, false);
            TextView textView = (TextView) rowView.findViewById(R.id.list_item_filename);
            textView.setText(values.get(position));

            TextView textView2 = (TextView) rowView.findViewById(R.id.list_item_type);
            textView2.setText(values2.get(position));

            return rowView;
        }

    }

    private void populateAdapter() {

        SqlDB dbEntry = new SqlDB(getApplicationContext());
        dbEntry.open();
        List<DBEntry> values_db = dbEntry.readAll();

        values = new ArrayList<String>();
        values2 = new ArrayList<String>();

        for(int x = 0; x < values_db.size(); x++) {
            values.add(values_db.get(x).FILENAME);
            values2.add(values_db.get(x).TYPE);
        }

        adapter = new StableArrayAdapter(this, values, values2);

    }
}
