package com.example.travelbee.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.travelbee.DashboardActivity;
import com.example.travelbee.DisplayMemories;
import com.example.travelbee.MemoryPage;
import com.example.travelbee.R;
import com.example.travelbee.UpdateMemory;
import com.example.travelbee.models.Memories;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder>{

    Context context;
    ArrayList<Memories> myMemories;

    public Adapter(Context c, ArrayList<Memories> p){
        context = c;
        myMemories = p;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.textTitle.setText(myMemories.get(i).getTitle());
        myViewHolder.textDate.setText(myMemories.get(i).getDate());
        Glide.with(myViewHolder.dbImage.getContext()).load(myMemories.get(i).getImageUrl()).into(myViewHolder.dbImage);

        final String getTitle = myMemories.get(i).getTitle();
        final String getLocation = myMemories.get(i).getLocation();
        final String getDate = myMemories.get(i).getDate();
        final String getDescription = myMemories.get(i).getDescription();
        final String getImage = myMemories.get(i).getImageUrl();
        final String getKey = myMemories.get(i).getKeyMemories();

        myViewHolder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent aa = new Intent(context, UpdateMemory.class);
                aa.putExtra("title", getTitle);
                aa.putExtra("location", getLocation);
                aa.putExtra("date", getDate);
                aa.putExtra("description", getDescription);
                aa.putExtra("image", getImage);
                aa.putExtra("keyMemories", getKey);
                context.startActivity(aa);
            }
        });

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent aa = new Intent(context, MemoryPage.class);
                aa.putExtra("title", getTitle);
                aa.putExtra("location", getLocation);
                aa.putExtra("date", getDate);
                aa.putExtra("description", getDescription);
                aa.putExtra("image", getImage);
                aa.putExtra("keyMemories", getKey);
                context.startActivity(aa);
            }
        });

        myViewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference db;
                db = FirebaseDatabase.getInstance().getReference("Users").child(currentuser).child("Memories").child("Memory" + getKey);
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
                builder.setTitle("Delete");
                builder.setMessage("Are you sure you want to delete this memory?");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        db.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                                    StorageReference storageReference = firebaseStorage.getReferenceFromUrl(getImage);
                                    storageReference.delete();

                                    Intent a = new Intent(context, DashboardActivity.class);
                                    Toast.makeText(context.getApplicationContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
                                    context.startActivity(a);
                                }
                                Toast.makeText(context.getApplicationContext(), "Failure", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(context.getApplicationContext(), "Cancelled.", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return myMemories.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textTitle, textLocation, textDate, textDescription,keyMemories;
        ImageView dbImage;
        Button btn, delete;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);

            textTitle = itemView.findViewById(R.id.text_title_new);
            dbImage = itemView.findViewById(R.id.iv_uploaded_new);
            textDate = itemView.findViewById(R.id.text_date_new);
            btn = itemView.findViewById(R.id.update_btn);
            delete= itemView.findViewById(R.id.btn_cancel);

        }

    }

}
