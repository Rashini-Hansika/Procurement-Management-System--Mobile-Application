package com.example.travelbee.adapters;

import static java.lang.Long.parseLong;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelbee.R;
import com.example.travelbee.models.ModelChat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class AdapterChat extends RecyclerView.Adapter<AdapterChat.MyHolder>{


    private static final int MSG_TYPE_LEFT = 0;
    private static final int MSG_TYPE_RIGHT = 1;
    Context context;
    List<ModelChat> chatList;
    String imageUrl;

    FirebaseUser fUser;

    public AdapterChat(Context context, List<ModelChat> chatList, String imageUrl) {
        this.context = context;
        this.chatList = chatList;
        this.imageUrl = imageUrl;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        //inflate layouts row_chat_left = receiver, row_chat_right = sender

        View view;
        if(i==MSG_TYPE_RIGHT){
            view = LayoutInflater.from(context).inflate(R.layout.row_chat_right, parent, false);
        }else{
            view = LayoutInflater.from(context).inflate(R.layout.row_chat_left, parent, false);
        }
        return new MyHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int i) {
        //get data
        String message = chatList.get(i).getMessage();
        String timeStamp = chatList.get(i).getTimestamp();
        String type = chatList.get(i).getType();
        
        //convert time stamp to dd/mm/yyyy hh:mm am/pm
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        try {
            cal.setTimeInMillis(Long.parseLong(timeStamp));
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        String dateTime = android.text.format.DateFormat.format("dd/MM/yyyy hh:mm aa", cal).toString();

        if(type.equals("text")){
            holder.messageTv.setVisibility(View.VISIBLE);
            holder.messageIv.setVisibility(View.GONE);

            holder.messageTv.setText(message);
        }else{
            holder.messageTv.setVisibility(View.GONE);
            holder.messageIv.setVisibility(View.VISIBLE);

            Picasso.get().load(message).placeholder(R.drawable.ic_image_black).into(holder.messageIv);
        }

        //set data
        holder.messageTv.setText(message);
        holder.timeTv.setText(dateTime);
        try{
            Picasso.get().load(imageUrl).into(holder.profileIv);
        }catch (Exception e){

        }

        //click to show delete dialog
        holder.messageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //show delete message confirm dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete");
                builder.setMessage("Are you sure to delete this message?");
                //delete button
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        deleteMessage(i);
                    }
                });
                //cancel delete button
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        //dismiss dialog
                        dialogInterface.dismiss();
                    }
                });

                //create and show dialog
                builder.create().show();

            }
        });

        //set seen/delivered status of message
        if(i==chatList.size()-1){
            if(chatList.get(i).isSeen()){
                holder.isSeenTv.setText("Seen");

            }else{
                holder.isSeenTv.setText("Delivered");

            }
        }else{
            holder.isSeenTv.setVisibility(View.GONE);
        }

    }

    private void deleteMessage(int position) {
        String myUID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        String timeStamp = chatList.get(position).getTimestamp();

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Chats");
        Query query = dbRef.orderByChild("timestamp").equalTo(timeStamp);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               for(DataSnapshot ds: snapshot.getChildren()){

                   if (ds.child("sender").getValue().equals(myUID)) {
                       HashMap<String, Object>hashMap = new HashMap<>();
                       hashMap.put("message", "This message was deleted...");
                       ds.getRef().updateChildren(hashMap);

                       Toast.makeText(context, "message deleted...", Toast.LENGTH_SHORT).show();
                   }else{
                       Toast.makeText(context, "You can delete only your messages...", Toast.LENGTH_SHORT).show();
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
        return chatList.size();
    }

    @Override
    public int getItemViewType(int position) {
        //get currently signed in user
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        if(chatList.get(position).getSender().equals(fUser.getUid())){
            return MSG_TYPE_RIGHT;
        }else{
            return MSG_TYPE_LEFT;
        }
    }

    class MyHolder extends RecyclerView.ViewHolder{

        //views
        ImageView profileIv, messageIv;
        TextView messageTv, timeTv, isSeenTv;
        LinearLayout messageLayout;//for click listner to show delete


        public MyHolder(@NonNull View itemView) {
            super(itemView);


            //init views
            profileIv = itemView.findViewById(R.id.profileIv);
            messageIv = itemView.findViewById(R.id.messageIv);
            messageTv = itemView.findViewById(R.id.messageTv);
            timeTv = itemView.findViewById(R.id.timeTv);
            isSeenTv = itemView.findViewById(R.id.isSeenTv);
            messageLayout = itemView.findViewById(R.id.messageLayout);


        }
    }

}
