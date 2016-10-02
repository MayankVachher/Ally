package com.mayank13059.ally;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class FiddleWithFiles extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fiddle_with_files);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();

        final String fname = intent.getStringExtra("fname");
        final String type = intent.getStringExtra("type");
        final int pos = intent.getIntExtra("pos", -1);

        TextView filename = (TextView) findViewById(R.id.content_fileName);
        TextView filetype = (TextView) findViewById(R.id.content_filetype);

        filename.setText(fname);
        filetype.setText(type);


        Button delete = (Button) findViewById(R.id.button_delete_file);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SqlDB dbEntry = new SqlDB(getApplicationContext());
                dbEntry.open();
                dbEntry.deleteEntry(fname);
                dbEntry.close();
                finish();

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        Button rename = (Button) findViewById(R.id.button_rename_file);
        rename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
                Intent intent = new Intent(getApplicationContext(), RenameFile.class);
                intent.putExtra("fname", fname);
                startActivity(intent);

            }
        });
    }

}
