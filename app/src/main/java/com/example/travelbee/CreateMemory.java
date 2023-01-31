package com.example.travelbee;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
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
import java.util.Random;

public class CreateMemory extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    ImageView image;
    TextInputEditText date, title, location, description;
    ImageView picker;
    Button upload, cancel;
    Integer memoryNum = new Random().nextInt();
    String keyMemory = Integer.toString(memoryNum);

    Uri imageUri;

    String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
    String storagePath = "Memories/";

    DatabaseReference db;
    StorageReference storage = FirebaseStorage.getInstance().getReference();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_memory);

//        getSupportActionBar().hide();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Add Enquiry");

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        image = findViewById(R.id.iv_upload);
        date = findViewById(R.id.et_date);
        picker = findViewById(R.id.img_datepicker);
        title = findViewById(R.id.et_current);
        location = findViewById(R.id.et_location);
        description = findViewById(R.id.et_description);
        upload = findViewById(R.id.btn_update);
        cancel = findViewById(R.id.btn_cancel);

        picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Please note that use your package name here
                com.example.travelbee.DatePicker mDatePickerDialogFragment;
                mDatePickerDialogFragment = new com.example.travelbee.DatePicker();
                mDatePickerDialogFragment.show(getSupportFragmentManager(), "DATE PICK");
            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGetContent.launch("image/*");

            }
        });


        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(date.getText().toString())){
                    date.setError("Enquiry Date is required");
                }
                if(TextUtils.isEmpty(description.getText().toString())){
                    description.setError("Message is required");
                }
                if(TextUtils.isEmpty(location.getText().toString())){
                    location.setError("Order Type is required");
                }
                if(TextUtils.isEmpty(title.getText().toString())){
                    title.setError("Order ID is required");
                }
                else {
                    InsertToDB();
                }
            }

            private void InsertToDB() {
                if(imageUri != null) {
                    StorageReference reference1 = storage.child(storagePath+ System.currentTimeMillis() + "." + getFileExtension(imageUri));
                    db = FirebaseDatabase.getInstance().getReference("Users").child(currentuser).child("Memories").child("Memory" + memoryNum);
                    reference1.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            reference1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    db.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                snapshot.getRef().child("date").setValue(date.getText().toString());
                                                snapshot.getRef().child("description").setValue(description.getText().toString());
                                                snapshot.getRef().child("imageUrl").setValue(uri.toString());
                                                snapshot.getRef().child("keyMemories").setValue(keyMemory);
                                                snapshot.getRef().child("location").setValue(location.getText().toString());
                                                snapshot.getRef().child("title").setValue(title.getText().toString());


                                                Intent a = new Intent(CreateMemory.this, DisplayMemories.class);
                                                Toast.makeText(CreateMemory.this, "Memory Created Successfully", Toast.LENGTH_SHORT).show();
                                                startActivity(a);
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
            private String getFileExtension(Uri imageUri) {
                ContentResolver cr = getContentResolver();
                MimeTypeMap mime = MimeTypeMap.getSingleton();
                return mime.getExtensionFromMimeType(cr.getType(imageUri));
            }
        });
    }

    public void onClick(View view){
        Intent intent = new Intent(CreateMemory.this, DisplayMemories.class);
        startActivity(intent);
    }

    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri result) {
                    if(result != null){
                        image.setImageURI(result);
                        imageUri = result;
                    }
                    else{
                        Toast.makeText(CreateMemory.this, "Please Upload Image", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();//go previous activity
        return super.onSupportNavigateUp();
    }


    @Override
    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
        Calendar mCalender = Calendar.getInstance();
        mCalender.set(Calendar.YEAR, year);
        mCalender.set(Calendar.MONTH, month);
        mCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String selectedDate = DateFormat.getDateInstance(DateFormat.MEDIUM).format(mCalender.getTime());
        date.setText(selectedDate);
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
    }
}