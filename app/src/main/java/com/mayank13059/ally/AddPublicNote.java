package com.mayank13059.ally;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class AddPublicNote extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_public_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button trigger = (Button) findViewById(R.id.addPublicNoteTrigger);
        trigger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText title, content;
                title = (EditText) findViewById(R.id.publicNoteTitle);
                content = (EditText) findViewById(R.id.publicNoteContent);
                writePublicNote(title.getText().toString(), content.getText().toString());
                Intent intent = new Intent();
                setResult(1001, intent);
                finish();
            }
        });
    }

    private void writePublicNote(String title, String content) {

        Boolean state = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);

        if(!state) {
            Toast.makeText(this, "External Storage Not Mounted!", Toast.LENGTH_SHORT).show();
            return;
        }
        File dir = new File(Environment.getExternalStorageDirectory(),"/Ally");

        if(!dir.exists()) {
            Boolean temp = dir.mkdirs();
        }

        File dir2 = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS), "");

        if(!dir2.exists()) {
            Boolean temp = dir2.mkdirs();
        }

        File file, file2;
        FileOutputStream outputStream, outputStream2;
        try {
            file = new File(dir, "ally_"+title+".txt");
            file2 = new File(dir2,"ally_"+title+".txt");


            outputStream = new FileOutputStream(file);

            outputStream.write(content.getBytes());
            outputStream.close();

            outputStream2 = new FileOutputStream(file2);
            outputStream2.write(content.getBytes());
            outputStream2.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        SqlDB dbEntry = new SqlDB(getApplicationContext());
        dbEntry.open();
        dbEntry.createEntry(title, "Public");
        dbEntry.close();
    }

}
