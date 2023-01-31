package com.example.travelbee;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.util.Calendar;

public class UpdateMemory extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    EditText titleName,locationName,dateView,descriptionView;
    ImageView imageView, picker;
    Button update, delete;

    Uri imageUri;
    String storagePath = "Memories/";

    DatabaseReference db;
    String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_memory);

//        getSupportActionBar().hide();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Update Memory");

        //enable back button
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        titleName = findViewById(R.id.et_current);
        locationName = findViewById(R.id.et_location);
        picker = findViewById(R.id.img_datepicker);
        dateView = findViewById(R.id.et_date);
        descriptionView = findViewById(R.id.et_description);
        imageView = findViewById(R.id.iv_upload);
        update = findViewById(R.id.btn_update);
        delete = findViewById(R.id.btn_cancel);

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

        String keykeyMemories = i.getStringExtra("keyMemories");

        db = FirebaseDatabase.getInstance().getReference("Users").child(currentuser).child("Memories").child("Memory" + keykeyMemories);


        picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Please note that use your package name here
                com.example.travelbee.DatePicker mDatePickerDialogFragment;
                mDatePickerDialogFragment = new com.example.travelbee.DatePicker();
                mDatePickerDialogFragment.show(getSupportFragmentManager(), "DATE PICK");
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGetContent.launch("image/*");
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(dateView.getText().toString())){
                    dateView.setError("Date is required");
                }
                if(TextUtils.isEmpty(descriptionView.getText().toString())){
                    descriptionView.setError("Description is required");
                }
                if(TextUtils.isEmpty(locationName.getText().toString())){
                    locationName.setError("Location is required");
                }
                if(TextUtils.isEmpty(titleName.getText().toString())){
                    titleName.setError("Title is required");
                }
                else {
                    if (imageUri != null) {
                        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                        final StorageReference reference1 = firebaseStorage.getReferenceFromUrl(image_url);

                        reference1.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                reference1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        db.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                snapshot.getRef().child("date").setValue(dateView.getText().toString());
                                                snapshot.getRef().child("description").setValue(descriptionView.getText().toString());
                                                snapshot.getRef().child("imageUrl").setValue(uri.toString());
                                                snapshot.getRef().child("keyMemories").setValue(keykeyMemories);
                                                snapshot.getRef().child("location").setValue(locationName.getText().toString());
                                                snapshot.getRef().child("title").setValue(titleName.getText().toString());
                                                Intent i = new Intent(UpdateMemory.this, DisplayMemories.class);
                                                Toast.makeText(UpdateMemory.this, "Memory Updated Successfully", Toast.LENGTH_SHORT).show();
                                                startActivity(i);
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });

                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
                    }
                }
            }

            private String getFileExtension(Uri imageUri) {
                ContentResolver cr = getContentResolver();
                MimeTypeMap mime = MimeTypeMap.getSingleton();
                return mime.getExtensionFromMimeType(cr.getType(imageUri));
            }
        });
    }

    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri result) {
                    if(result != null){
                        imageView.setImageURI(result);
                        imageUri = result;
                    }
                }
            });

    @Override
    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
        Calendar mCalender = Calendar.getInstance();
        mCalender.set(Calendar.YEAR, year);
        mCalender.set(Calendar.MONTH, month);
        mCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String selectedDate = DateFormat.getDateInstance(DateFormat.MEDIUM).format(mCalender.getTime());
        dateView.setText(selectedDate);
    }

    public void onClickCancel(View view){
//        Intent intent = new Intent(UpdateMemory.this, DisplayMemories.class);
//        startActivity(intent);
        onBackPressed();//go previous activity
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
    }

    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();//go previous activity
        return super.onSupportNavigateUp();
    }
}