package com.example.travelbee.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelbee.ChatActivity;
import com.example.travelbee.R;
import com.example.travelbee.models.ModelUsers;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

public class AdapterUser extends RecyclerView.Adapter<AdapterUser.MyHolder>{

    Context context;
    List<ModelUsers> userList;

    FirebaseAuth firebaseAuth;
    String myUid;


    public AdapterUser(Context context, List<ModelUsers> userList) {
        this.context = context;
        this.userList = userList;

        firebaseAuth = FirebaseAuth.getInstance();
        myUid = firebaseAuth.getUid();
    }

    @NonNull
    @Override
    public AdapterUser.MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(context).inflate(R.layout.row_users,viewGroup, false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterUser.MyHolder holder, int i) {

        //get data
        String hisUID = userList.get(i).getUid();

        String userImage = userList.get(i).getImage();
        String userName = userList.get(i).getName();
        String userEmail = userList.get(i).getEmail();

        holder.mNameTv.setText(userName);
        holder.mEmailTv.setText(userEmail);
        try{
            Picasso.get().load(userImage)
                    .placeholder(R.drawable.ic_default_img)
                    .into(holder.mAvatarTv);
        }catch (Exception e){

        }

        holder.blockIv.setImageResource(R.drawable.ic_unblocked_green);
        checkIsBlocked(hisUID, holder, i);

        holder.itemView.setOnClickListener(view -> {

            imBlockedORNot(hisUID);
        });


        holder.blockIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userList.get(i).isBlocked()){
                    unBlockUser(hisUID);
                }
                else{
                    blockUser(hisUID);
                }
            }
        });

    }

    private void imBlockedORNot (String hisUID){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(hisUID).child("BlockedUsers").orderByChild("uid").equalTo(myUid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot ds: snapshot.getChildren()){
                            if (ds.exists()){
                                Toast.makeText(context, "You're blocked by that user, can't send message", Toast.LENGTH_SHORT).show();

                                return;
                            }
                        }
                        Intent intent = new Intent(context, ChatActivity.class);
                        intent.putExtra("hisUid", hisUID);
                        context.startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void checkIsBlocked(String hisUID, MyHolder holder, int i) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(myUid).child("BlockedUsers").orderByChild("uid").equalTo(hisUID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot ds: snapshot.getChildren()){
                            if (ds.exists()){
                                holder.blockIv.setImageResource(R.drawable.ic_blocked_red);
                                userList.get(i).setBlocked(true);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void blockUser(String hisUID) {

        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("uid",hisUID);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(myUid).child("BlockedUsers").child(hisUID).setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, "Blocked Successfully...", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Failed: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void unBlockUser(String hisUID) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(myUid).child("BlockedUsers").orderByChild("uid").equalTo(hisUID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot ds: snapshot.getChildren()){
                            if (ds.exists()){
                                ds.getRef().removeValue()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(context, "Unblocked Successfully...", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(context, "Failed: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{

        ImageView mAvatarTv, blockIv;
        TextView mNameTv, mEmailTv;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            mAvatarTv = itemView.findViewById(R.id.avatarIv);
            blockIv = itemView.findViewById(R.id.blockIv);
            mNameTv = itemView.findViewById(R.id.nameTv);
            mEmailTv = itemView.findViewById(R.id.emailTv);

        }
    }
}
