package com.example.elessar1992.friendsrating;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by elessar1992 on 5/27/19.
 */

public class ProfileActivity extends AppCompatActivity
{
    FirebaseAuth firebaseAuth;
    //TextView myProfile;

    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //actionbar
        actionBar = getSupportActionBar();
        actionBar.setTitle("Profile");

        //auth
        firebaseAuth = FirebaseAuth.getInstance();

        //myProfile = findViewById(R.id.profiles);
        BottomNavigationView navigationView = findViewById(R.id.navigationButton);
        navigationView.setOnNavigationItemSelectedListener(selectedListener);

        actionBar.setTitle("Home");
        Home_Fragment fragment1 = new Home_Fragment();
        FragmentTransaction f1 = getSupportFragmentManager().beginTransaction();
        f1.replace(R.id.myContent,fragment1,"");
        f1.commit();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener selectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId())
                    {
                        case R.id.navigation:
                            actionBar.setTitle("Home");
                            Home_Fragment fragment1 = new Home_Fragment();
                            FragmentTransaction f1 = getSupportFragmentManager().beginTransaction();
                            f1.replace(R.id.myContent,fragment1,"");
                            f1.commit();
                            return true;

                        case R.id.profiles:
                            actionBar.setTitle("Profile");
                            ProfileFragment fragment2 = new ProfileFragment();
                            FragmentTransaction f2 = getSupportFragmentManager().beginTransaction();
                            f2.replace(R.id.myContent,fragment2,"");
                            f2.commit();
                            return true;

                        case R.id.users:
                            actionBar.setTitle("Users");
                            UserFragment fragment3 = new UserFragment();
                            FragmentTransaction f3 = getSupportFragmentManager().beginTransaction();
                            f3.replace(R.id.myContent,fragment3,"");
                            f3.commit();
                            return true;
                    }
                    return false;
                }
            };

    private void checkingUsers()
    {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user != null)
        {
            //myProfile.setText(user.getEmail());
        }
        else
        {
            startActivity(new Intent(ProfileActivity.this,MainActivity.class));
            finish();
        }
    }

    protected void onStart()
    {
        checkingUsers();
        super.onStart();
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if(id == R.id.action_logout)
        {
            firebaseAuth.signOut();
            checkingUsers();
        }
        return super.onOptionsItemSelected(item);
    }
}
