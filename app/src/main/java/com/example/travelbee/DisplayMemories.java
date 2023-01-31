package com.example.travelbee;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelbee.adapters.Adapter;
import com.example.travelbee.models.Memories;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DisplayMemories extends AppCompatActivity {
    RecyclerView recyclerView;
    Adapter adapter;
    ArrayList<Memories> items;
    DatabaseReference reference;
    FloatingActionButton fab;

    String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_memories);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("My Enquiry");

        //enable back button
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        fab = findViewById(R.id.floating_action_button);
        items = new ArrayList<Memories>();

        reference = FirebaseDatabase.getInstance().getReference("Users").child(currentuser).child("Memories");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot1: snapshot.getChildren())
                {
                    Memories p = dataSnapshot1.getValue(Memories.class);
                    items.add(p);
                }
                adapter = new Adapter(DisplayMemories.this, items);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "No Data", Toast.LENGTH_SHORT).show();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DisplayMemories.this, CreateMemory.class);
                startActivity(intent);
            }
        });



    }
    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();//go previous activity
        return super.onSupportNavigateUp();
    }


//    public void onClick(View view){
//        Intent intent = new Intent(DisplayMemories.this, CreateMemory.class);
//        startActivity(intent);
//    }

}