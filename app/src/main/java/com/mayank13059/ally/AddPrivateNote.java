package com.mayank13059.ally;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class AddPrivateNote extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_private_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button trigger = (Button) findViewById(R.id.addPrivateNoteTrigger);
        trigger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText title, content;
                title = (EditText) findViewById(R.id.privateNoteTitle);
                content = (EditText) findViewById(R.id.privateNoteContent);
                writePrivateNote(title.getText().toString(), content.getText().toString());
                Intent intent = new Intent();
                setResult(1001, intent);
                finish();
            }
        });
    }

    private void writePrivateNote(String title, String content) {

        Boolean state = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);

        if(!state) {
            Toast.makeText(this, "External Storage Not Mounted!", Toast.LENGTH_SHORT).show();
            return;
        }

        Context context = getApplicationContext();
        File dir = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),"/Ally");

        if(!dir.exists()) {
            Boolean temp = dir.mkdirs();
        }

        File file;
        FileOutputStream outputStream;
        try {
            file = new File(dir, "ally_"+title+".txt");

            Log.e("NOTES", file.toString());

            outputStream = new FileOutputStream(file);

            outputStream.write(content.getBytes());
            outputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        SqlDB dbEntry = new SqlDB(getApplicationContext());
        dbEntry.open();
        dbEntry.createEntry(title, "Private");
        dbEntry.close();
    }

}
