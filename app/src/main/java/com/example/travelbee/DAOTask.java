package com.example.travelbee;

import com.example.travelbee.models.ToDo;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;

public class DAOTask {

    private DatabaseReference databaseReference ;

    public DAOTask() {

        FirebaseDatabase db =FirebaseDatabase.getInstance();
      //  databaseReference =db.getReference(ToDo.class.getSimpleName());
        String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference =db.getReference("Users").child(currentuser).child(ToDo.class.getSimpleName());
    }
    public Task<Void> add(ToDo todo){
        return databaseReference.push().setValue(todo);
    }

    public Task<Void> update(String key, HashMap<String,Object>hashMap)
    {
        return databaseReference.child(key).updateChildren(hashMap);
    }

    public Task<Void> remove(String key)
    {
        return databaseReference.child(key).removeValue();
    }

    public Query get( String key){
        if(key == null)
        {
            return databaseReference.orderByKey().limitToFirst(8);
        }
    return databaseReference.orderByKey().startAfter(key).limitToFirst(8);
}
    public Query get()
    {
        return databaseReference;
    }

}
