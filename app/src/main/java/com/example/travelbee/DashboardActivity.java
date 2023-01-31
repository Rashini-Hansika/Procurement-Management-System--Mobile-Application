package com.example.travelbee;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class DashboardActivity extends AppCompatActivity {

    //firebase auth
    FirebaseAuth firebaseAuth;
    DrawerLayout drawerLayout;


    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //ActionBar and its title
         actionBar = getSupportActionBar();
        actionBar.setTitle("Profile");

        //init
        firebaseAuth = FirebaseAuth.getInstance();

        //Bottom navigation
        BottomNavigationView navigationView = findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(selectedListener);


        actionBar.setTitle("Home");
        HomeFragment fragment1 = new HomeFragment();
        FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
        ft1.replace(R.id.content, fragment1, "");
        ft1.commit();





    }



    private BottomNavigationView.OnNavigationItemSelectedListener selectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            //handle item clicks
            switch (menuItem.getItemId()){

                case R.id.nav_home:
                    actionBar.setTitle("Home");
                    HomeFragment fragment1 = new HomeFragment();
                    FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
                    ft1.replace(R.id.content, fragment1, "");
                    ft1.commit();
                    return true;
                case R.id.nav_profile:
                    actionBar.setTitle("Profile");
                    ProfileFragment fragment2 = new ProfileFragment();
                    FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
                    ft2.replace(R.id.content, fragment2, "");
                    ft2.commit();
                    return true;
                case R.id.nav_users:
                    actionBar.setTitle("Users");
                    UsersFragment fragment5 = new UsersFragment  ();
                    FragmentTransaction ft5 = getSupportFragmentManager().beginTransaction();
                    ft5.replace(R.id.content, fragment5, "");
                    ft5.commit();
                    return true;

            }
            return false;
        }
    };

    private void checkUserStatus(){
        //get current user
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user != null){
            //user is signed in stay here
            //set email of logged in user
            //mProfileTv.setText(user.getEmail());


        }else{
            //user not signed in, go to main activity
            startActivity(new Intent(DashboardActivity.this, MainActivity.class));
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onStart(){
        //check on start of app
        checkUserStatus();
        super.onStart();
    }
}



