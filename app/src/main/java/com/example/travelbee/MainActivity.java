package com.example.travelbee;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    //views
    Button mRegisterBtn, mLoginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //init views
        mRegisterBtn = (Button) findViewById(R.id.register_btn);
        mLoginBtn = (Button) findViewById(R.id.login_btn);

        //handle register button click
        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //start Register Activity
                startActivity(new Intent(MainActivity.this,RegisterActivity.class));

            }
        });
        //handle login button click

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //start Login Activity
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
            }
        });
    }

    public void onClick(View view) {
    }

    public void hideKeyboard(View view) {
    }
}