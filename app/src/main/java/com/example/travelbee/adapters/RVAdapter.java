package com.example.travelbee.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.example.travelbee.DAOTask;
import com.example.travelbee.MainTODOActivity;
import com.example.travelbee.ToDoVH;
import com.example.travelbee.models.ToDo;
import com.example.travelbee.R;
import java.util.ArrayList;


public class RVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {



    private Context context;

ArrayList<ToDo> list = new ArrayList<>();
public RVAdapter(Context ctx)

{
    this.context=ctx;
}
public void setItems(ArrayList<ToDo> todo)
{
    list.addAll(todo);
}
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =LayoutInflater.from(context).inflate(R.layout.layout_item,parent,false);
        return  new ToDoVH(view);
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
    {
        ToDo e = null;
        this.onBindViewHolder(holder,position,e);
    }

    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, ToDo e) {
        ToDoVH vh =(ToDoVH) holder ;
      ToDo todo = e==null? list.get(position):e;
       /*ToDo todo =list.get(position);*/
       vh.txt_title.setText(todo.getTitle());
        vh.txt_subtitle.setText(todo.getSubtitle());
        vh.txt_desc.setText(todo.getDesc());
        vh.txt_option.setOnClickListener(v->{

            PopupMenu popupMenu = new PopupMenu(context,vh.txt_option);
            popupMenu.inflate(R.menu.option_menu);
            popupMenu.setOnMenuItemClickListener(item->{


                switch (item.getItemId()){
                    case R.id.menu_edit:

                        Intent intent = new Intent(context, MainTODOActivity.class);

                        intent.putExtra("EDIT",todo);
                        context.startActivity(intent);
                        break;
                    case R.id.menu_remove:

                        DAOTask dao = new DAOTask();
                        dao.remove(todo.getKey()).addOnSuccessListener(suc->{
                            Toast.makeText(context,"Task is removed successfully",Toast.LENGTH_SHORT).show();
                            notifyItemRemoved(position);
                            list.remove(todo);
                        }).addOnFailureListener(er->{
                            Toast.makeText(context,""+er.getMessage(),Toast.LENGTH_SHORT).show();
                        });
                                break;
                }
                return false;
            });

        popupMenu.show();
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
