package com.example.travelbee;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.travelbee.models.ToDo;

import java.util.HashMap;


public class MainTODOActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_todo);


       final  EditText edit_title =findViewById(R.id.edit_title);
       final EditText edit_subtitle =findViewById(R.id.edit_subtitle);
       final  EditText edit_desc =findViewById(R.id.edit_desc);

            Button  btn_submit = findViewById(R.id.btn_submit);
            Button btn_open = findViewById(R.id.btn_open);
            btn_open.setOnClickListener(v->{
            Intent intent = new Intent(MainTODOActivity.this,RVActivity.class);
            startActivity(intent);
        });
        DAOTask dao = new DAOTask();

       ToDo todo_edit=(ToDo) getIntent().getSerializableExtra("EDIT");
       if(todo_edit != null){

           btn_submit.setText("Update");
           edit_title.setText(todo_edit.getTitle());
           edit_subtitle.setText(todo_edit.getSubtitle());
           edit_desc.setText(todo_edit.getDesc());
           btn_open.setVisibility(View.GONE);
       }else {
           btn_submit.setVisibility(View.VISIBLE);
       }
        btn_submit.setOnClickListener(v->{

            if (TextUtils.isEmpty(edit_title.getText().toString())) {
                edit_title.setError("title is required");
            }
            if (TextUtils.isEmpty(edit_desc.getText().toString())) {
                edit_desc.setError("Description is required");
            }
            if (TextUtils.isEmpty(edit_subtitle.getText().toString())) {
                edit_subtitle.setError("subtitle  is required");
            } else {

                ToDo todo = new ToDo(edit_title.getText().toString(), edit_subtitle.getText().toString(), edit_desc.getText().toString());

                if (todo_edit == null) {
                    dao.add(todo).addOnSuccessListener(suc -> {

                        Toast.makeText(this, "created successfully", Toast.LENGTH_SHORT).show();

                    }).addOnFailureListener(er -> {

                        Toast.makeText(this, "" + er.getMessage(), Toast.LENGTH_SHORT).show();
                    });

                } else {

                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("title", edit_title.getText().toString());
                    hashMap.put("subtitle", edit_subtitle.getText().toString());
                    hashMap.put("desc", edit_desc.getText().toString());

                    dao.update(todo_edit.getKey(), hashMap).addOnSuccessListener(suc -> {
                        Toast.makeText(this, "update Successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }).addOnFailureListener(er -> {
                        Toast.makeText(this, "" + er.getMessage(), Toast.LENGTH_SHORT).show();
                    });

                }
            }
            //
        });



    }


}
