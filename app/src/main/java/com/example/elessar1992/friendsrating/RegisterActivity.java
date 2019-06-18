package com.example.elessar1992.friendsrating;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

/**
 * Created by elessar1992 on 5/19/19.
 */

public class RegisterActivity extends AppCompatActivity
{
    private FirebaseAuth mAuth;

    //private NestedScrollView nestedScrollView;

    private TextInputLayout textInputLayoutName;
    private TextInputLayout textInputLayoutLastname;
    private TextInputLayout textInputLayoutUsername;
    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;
    private TextInputLayout textInputLayoutConfirmPassword;


    private TextInputEditText textInputEditTextName;
    private TextInputEditText textInputEditTextLastname;


    private EditText textInputEditTextUsername;
    private EditText textInputEditTextEmail;
    private EditText textInputEditTextPassword;
    private TextView textInputEditTextAccount;
    //private EditText textInputEditTextConfirmPassword;

    private Button appCompatButtonRegister;
    //private AppCompatTextView appCompatTextViewLoginLink;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        getSupportActionBar().hide();

        initViews();
        initListener();




    }

    private void initViews()
    {
        //nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);

        //textInputLayoutName = (TextInputLayout) findViewById(R.id.textInputLayoutFirstName);
        //textInputLayoutLastname = (TextInputLayout) findViewById(R.id.textInputLayoutLastName);
        //textInputLayoutUsername = (TextInputLayout) findViewById(R.id.textInputLayoutUsername);
        //textInputLayoutEmail = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);
        //textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);
        //textInputLayoutConfirmPassword = (TextInputLayout) findViewById(R.id.textInputLayoutConfirmPassword);

        //textInputEditTextName = (TextInputEditText) findViewById(R.id.textInputEditTextFirstName);
        //textInputEditTextLastname = (TextInputEditText) findViewById(R.id.textInputEditTextLastName);
        //textInputEditTextUsername = (EditText) findViewById(R.id.textInputEditTextUsername);
        textInputEditTextEmail = (EditText) findViewById(R.id.textInputEditTextEmail);
        textInputEditTextPassword = (EditText) findViewById(R.id.textInputEditTextPassword);
        //textInputEditTextConfirmPassword = (EditText) findViewById(R.id.textInputEditTextConfirmPassword);

        appCompatButtonRegister = (Button) findViewById(R.id.appCompatButtonLogin);

        textInputEditTextAccount = (TextView) findViewById(R.id.userAccount);

        //appCompatTextViewLoginLink = (AppCompatTextView) findViewById(R.id.textViewLinkRegister);
        mAuth = FirebaseAuth.getInstance();

    }

    private void initListener()
    {
        appCompatButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String name =  textInputEditTextName.getText().toString().trim();
                //String lastname = textInputEditTextLastname.getText().toString().trim();
                //String username = textInputEditTextUsername.getText().toString().trim();
                String email = textInputEditTextEmail.getText().toString().trim();
                String password = textInputEditTextPassword.getText().toString().trim();
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {
                    textInputEditTextEmail.setError("Invalid Email");
                    textInputEditTextEmail.setFocusable(true);
                }
                else if (password.length() < 6)
                {
                    textInputEditTextPassword.setError("Passwrod Length is less than 6");
                    textInputEditTextPassword.setFocusable(true);
                }
                else
                {
                    registerUser(email,password);
                }
            }
        });

        textInputEditTextAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                finish();
            }
        });

    }

    private void registerUser(String email, String password)
    {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            String email = user.getEmail();
                            String userID = user.getUid();

                            HashMap<Object, String> hashMap = new HashMap<>();
                            hashMap.put("email", email);
                            hashMap.put("uid", userID);
                            hashMap.put("name", "");
                            hashMap.put("onlineStatus","online");
                            hashMap.put("typingTo","noOne");
                            hashMap.put("phone", "");
                            hashMap.put("image", "");
                            hashMap.put("cover", "");

                            FirebaseDatabase database = FirebaseDatabase.getInstance();

                            DatabaseReference reference = database.getReference("Users");
                            reference.child(userID).setValue(hashMap);

                            Toast.makeText(RegisterActivity.this, "Registration Completed\n"+user.getEmail(),Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(RegisterActivity.this, ProfileActivity.class));
                                    finish();

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterActivity.this, ""+e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

}
