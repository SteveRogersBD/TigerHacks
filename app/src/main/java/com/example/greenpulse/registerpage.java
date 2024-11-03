package com.example.greenpulse;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.greenpulse.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

// Class declaration for the registration page
public class registerpage extends AppCompatActivity {

    // Declare UI components and Firebase authentication variables
    EditText editTextEmail, editTextPassword;
    Button btn;
    ProgressBar progressBar;
    Button btnback;
    FirebaseAuth mAuth;
    FirebaseDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registerpage); // Set the layout for the registration page
        editTextEmail = findViewById(R.id.username); // Initialize email EditText field
        editTextPassword = findViewById(R.id.pass); // Initialize password EditText field
        btn = findViewById(R.id.registerbtn); // Initialize registration button
        progressBar = findViewById(R.id.progressbar); // Initialize progress bar
        btnback = findViewById(R.id.backbtn);
        mAuth = FirebaseAuth.getInstance();
        // Set click listener for the registration button
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(v.VISIBLE); // Show progress bar while registering
                String email, password;
                email = String.valueOf(editTextEmail.getText()); // Get entered email
                password = String.valueOf(editTextPassword.getText()); // Get entered password
                db= FirebaseDatabase.getInstance();
                createUser(email,password);

            }
        });

        btnback.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(registerpage.this,loginPage.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void createUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if(task.isSuccessful())
                {
                    User user = new User(email,password,null,null);
                    String sEmail = email.replace('.','_');
                    db.getReference().child("users").child(sEmail).setValue(user);
                    startActivity(new Intent(registerpage.this,MapActivity.class));
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(registerpage.this, e.getLocalizedMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser()!=null){
            startActivity(new Intent(registerpage.this,MainActivity.class));
        }
    }
}
