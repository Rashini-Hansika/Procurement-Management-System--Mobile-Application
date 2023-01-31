package com.example.travelbee;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ToDoVH extends RecyclerView.ViewHolder {

public TextView txt_title,txt_subtitle,txt_desc,txt_option;
    public ToDoVH(@NonNull View itemView) {
        super(itemView);
        txt_title =itemView.findViewById(R.id.txt_title);
        txt_subtitle =itemView.findViewById(R.id.txt_subtitle);
        txt_desc=itemView.findViewById(R.id.txt_desc);
        txt_option=itemView.findViewById(R.id.txt_option);
    }
}
