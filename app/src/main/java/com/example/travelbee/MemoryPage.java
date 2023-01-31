package com.example.travelbee;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class MemoryPage extends AppCompatActivity {

    TextView titleName,locationName,dateView,descriptionView;
    ImageView imageView;
    Button update,delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_page);

//        getSupportActionBar().hide();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getIntent().getStringExtra("title"));

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);


        titleName = findViewById(R.id.displayTitle);
        locationName = findViewById(R.id.displayLocation);
        dateView = findViewById(R.id.displayDate);
        descriptionView = findViewById(R.id.displayDescription);
        imageView = findViewById(R.id.displayImage);

        Intent i = getIntent();
        String title = i.getStringExtra("title");
        titleName.setText(title);

        String location = i.getStringExtra("location");
        locationName.setText(location);

        String date = i.getStringExtra("date");
        dateView.setText(date);

        String description = i.getStringExtra("description");
        descriptionView.setText(description);

        String image_url = i.getStringExtra("image");
        Glide.with(imageView.getContext()).load(image_url).into(imageView);

    }

    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();//go previous activity
        return super.onSupportNavigateUp();
    }

}