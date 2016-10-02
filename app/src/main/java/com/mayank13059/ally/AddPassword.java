package com.mayank13059.ally;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.FileOutputStream;

public class AddPassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button trigger = (Button) findViewById(R.id.addPasswordTrigger);
        trigger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText login, password;
                login = (EditText) findViewById(R.id.loginText);
                password = (EditText) findViewById(R.id.passwordText);
                writePasswords(login.getText().toString(), password.getText().toString());
                Intent intent = new Intent();
                setResult(1001, intent);
                finish();
            }
        });
    }

    private void writePasswords(String login, String password) {
        String fileName = login;
        String content = password;

        FileOutputStream outputStream = null;
        try {
            outputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStream.write(content.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        SqlDB dbEntry = new SqlDB(getApplicationContext());
        dbEntry.open();
        dbEntry.createEntry(fileName, "Password");
        dbEntry.close();
    }

}
