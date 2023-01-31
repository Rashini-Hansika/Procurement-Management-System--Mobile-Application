package com.example.travelbee;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.travelbee.adapters.AdapterUser;
import com.example.travelbee.models.ModelUsers;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 .
 */
public class UsersFragment extends Fragment {

    RecyclerView recyclerView;

    AdapterUser adapterUser;


    List<ModelUsers> usersList;

    //firebase auth
    FirebaseAuth firebaseAuth;


    public UsersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_users, container, false);

        firebaseAuth = FirebaseAuth.getInstance();


        recyclerView = view.findViewById(R.id.users_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        usersList = new ArrayList<>();



        getAllUsers();

        return view;
    }

    private void getAllUsers() {
        final FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersList.clear();
                for (DataSnapshot ds:snapshot.getChildren()){
                    ModelUsers modelUsers = ds.getValue(ModelUsers.class);

                    if(!modelUsers.getUid().equals(fUser.getUid())){
                        usersList.add(modelUsers);
                    }

                    adapterUser = new AdapterUser(getActivity(), usersList);

                    recyclerView.setAdapter(adapterUser);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void searhUsers(String s) {
        final FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersList.clear();
                for (DataSnapshot ds:snapshot.getChildren()){
                    ModelUsers modelUsers = ds.getValue(ModelUsers.class);

                    if(!modelUsers.getUid().equals(fUser.getUid())){

                        if(modelUsers.getName().toLowerCase().contains(s.toLowerCase()) ||
                            modelUsers.getEmail().toLowerCase().contains(s.toLowerCase())){
                            usersList.add(modelUsers);

                        }

                    }

                    adapterUser = new AdapterUser(getActivity(), usersList);
                    adapterUser.notifyDataSetChanged();

                    recyclerView.setAdapter(adapterUser);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void checkUserStatus(){
        //get current user
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user != null){
            //user is signed in stay here
            //set email of logged in user
            //mProfileTv.setText(user.getEmail());
        }else{
            //user not signed in, go to main activity
            startActivity(new Intent(getActivity(), MainActivity.class));
            getActivity().finish();
        }
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    /*inflate options menu*/
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //inflating menu
        inflater.inflate(R.menu.menu_main,menu);


        MenuItem item = menu.findItem(R.id.action_search);



        android.widget.SearchView searchView;

        searchView = (android.widget.SearchView) MenuItemCompat.getActionView(item);

        searchView.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(!TextUtils.isEmpty(query.trim())){
                    searhUsers(query);
                }else{
                    getAllUsers();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(!TextUtils.isEmpty(newText.trim())){
                    searhUsers(newText);
                }else{
                    getAllUsers();
                }
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }



    /*handle menu item clicks*/

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //get item id

        int id = item.getItemId();
        if(id == R.id.action_logout){
            firebaseAuth.signOut();
            checkUserStatus();
        }
        return super.onOptionsItemSelected(item);
    }
}

