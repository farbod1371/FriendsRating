package com.example.elessar1992.friendsrating;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button RegisterButton;
    Button LoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RegisterButton = findViewById(R.id.register_btn);
        LoginButton = findViewById(R.id.login_btm);

        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
               startActivity(new Intent(MainActivity.this,RegisterActivity.class));
            }
        });

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,LoginActivity.class));

            }
        });

    }
}
