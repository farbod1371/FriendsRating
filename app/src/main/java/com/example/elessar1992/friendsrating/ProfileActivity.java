package com.example.elessar1992.friendsrating;

import android.content.Intent;
import android.os.Bundle;
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
    TextView myProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseAuth = FirebaseAuth.getInstance();
        myProfile = findViewById(R.id.profileID);

    }

    private void checkingUsers()
    {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user != null)
        {
            myProfile.setText(user.getEmail());
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
