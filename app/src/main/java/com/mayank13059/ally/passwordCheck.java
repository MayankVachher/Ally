package com.mayank13059.ally;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class passwordCheck extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_check);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button button = (Button) findViewById(R.id.checkPass);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText pass = (EditText) findViewById(R.id.inputPass);
                SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("masterPassword", Context.MODE_PRIVATE);
                String masterPassword = sharedPref.getString("password", "");

                if(masterPassword.equals(pass.getText().toString())) {

                    Intent prevIntent = getIntent();
                    int pos = prevIntent.getIntExtra("pos", -1);
                    String fname = prevIntent.getStringExtra("fname");
                    String type = prevIntent.getStringExtra("type");

                    Intent intent = new Intent(getApplicationContext(), FiddleWithFiles.class);
                    intent.putExtra("pos", pos);
                    intent.putExtra("fname", fname);
                    intent.putExtra("type", type);
                    startActivity(intent);
                }
                else {
                    finish();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

}
