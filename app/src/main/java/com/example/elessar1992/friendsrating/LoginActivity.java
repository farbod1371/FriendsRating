package com.example.elessar1992.friendsrating;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

public class LoginActivity extends AppCompatActivity
{
    private EditText textInputEditTextEmail;
    private EditText textInputEditTextPassword;
    private TextView notHaveAccount;
    private Button   appCompatButtonLogin;
    private TextView RecoverPassword;


    private FirebaseAuth mAuth;


    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        textInputEditTextEmail = findViewById(R.id.textInputEditTextEmail);
        textInputEditTextPassword = findViewById(R.id.textInputEditTextPassword);
        appCompatButtonLogin = findViewById(R.id.appCompatButtonLogin);
        notHaveAccount = findViewById(R.id.userAccount);
        RecoverPassword = findViewById(R.id.recoverAccount);

        mAuth = FirebaseAuth.getInstance();


        appCompatButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                String email = textInputEditTextEmail.getText().toString();
                String password = textInputEditTextPassword.getText().toString();
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {
                    textInputEditTextEmail.setError("Email is Invalid");
                    textInputEditTextEmail.setFocusable(true);
                }
                else
                {
                    loginUser(email,password);
                }
            }
        });

        notHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                finish();

            }
        });

        RecoverPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showingRecoveringPasswordDialog();
            }
        });
    }

    private void loginUser(String email, String password)
    {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            startActivity(new Intent(LoginActivity.this,ProfileActivity.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, ""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showingRecoveringPasswordDialog()
    {
        //AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this,R.style.AlertDialogCustom));
        builder.setTitle("Recover Password");

        LinearLayout linearLayout = new LinearLayout(LoginActivity.this);
        final EditText emailText = new EditText(LoginActivity.this);
        emailText.setHint("Email");
        emailText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        linearLayout.addView(emailText);
        linearLayout.setPadding(10,10,10,10);
        builder.setView(linearLayout);
        emailText.setMinEms(16);



        //for recovering
        builder.setPositiveButton("Recover", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String email = emailText.getText().toString().trim();
                startingRecovery(email);
            }
        });
        //for canceling
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void startingRecovery(String email)
    {
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
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

                    FirebaseDatabase database = FirebaseDatabase.getInstance();

                    DatabaseReference reference = database.getReference("Users");
                    reference.child(userID).setValue(hashMap);

                    Toast.makeText(LoginActivity.this, ""+user.getEmail(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, ProfileActivity.class));
                    finish();
                }
                else
                {
                    Toast.makeText(LoginActivity.this, "Failed...", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}
