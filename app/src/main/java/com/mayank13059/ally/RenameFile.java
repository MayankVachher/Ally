package com.mayank13059.ally;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RenameFile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rename_file);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        final String fileName = intent.getStringExtra("fname");

        EditText renameField = (EditText) findViewById(R.id.edit_rename);

        renameField.setHint("Rename "+fileName+" as ...");

        Button rename = (Button) findViewById(R.id.button_rename_go);
        rename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SqlDB dbEntry = new SqlDB(getApplicationContext());
                dbEntry.open();
                EditText renameField = (EditText) findViewById(R.id.edit_rename);
                dbEntry.renameEntry(fileName, renameField.getText().toString());
                dbEntry.close();
                finish();

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

}
