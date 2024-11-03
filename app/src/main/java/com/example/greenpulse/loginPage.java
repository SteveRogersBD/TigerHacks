package com.example.greenpulse;
// Import statements

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;

// MainActivity class declaration
public class loginPage extends AppCompatActivity {
    // Declare UI components and Firebase authentication variables
    EditText editTextEmail, editTextPassword;
    Button butn;
    ProgressBar progressBar;
    TextView textview;
    FirebaseAuth mAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page); // Setting the layout

        // Initializing UI components and Firebase instances
        editTextEmail = findViewById(R.id.username);
        editTextPassword = findViewById(R.id.pass);
        butn = findViewById(R.id.loginbtn);
        progressBar = findViewById(R.id.progressbar);

        // Set click listener for the login button
        butn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(v.VISIBLE);
                String email, password;
                email =String.valueOf(editTextEmail.getText());
                password =String.valueOf(editTextPassword.getText());
                mAuth = FirebaseAuth.getInstance();
                verifyUser(email,password);

            }
        });

    }

    private void verifyUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {

            @Override
            public void onSuccess(AuthResult authResult) {
                progressBar.setVisibility(View.GONE);
                startActivity(new Intent(loginPage.this,MapActivity.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(loginPage.this, e.getLocalizedMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    // Method to handle click on register button
    public void registerclick(View v)
    {
        Intent intent = new Intent(loginPage.this,registerpage.class);
        startActivity(intent);
    }

    public void resetclick(View v)
    {
        Intent intent = new Intent(loginPage.this,ForgotPassword.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser()!=null)
        {
            startActivity(new Intent(loginPage.this,MapActivity.class));
        }
    }
}
