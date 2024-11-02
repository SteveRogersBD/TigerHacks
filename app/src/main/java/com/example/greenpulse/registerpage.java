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

// Class declaration for the registration page
public class registerpage extends AppCompatActivity {

    // Declare UI components and Firebase authentication variables
    EditText editTextEmail, editTextPassword;
    Button btn;
    ProgressBar progressBar;
    Button btnback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registerpage); // Set the layout for the registration page
        editTextEmail = findViewById(R.id.username); // Initialize email EditText field
        editTextPassword = findViewById(R.id.pass); // Initialize password EditText field
        btn = findViewById(R.id.registerbtn); // Initialize registration button
        progressBar = findViewById(R.id.progressbar); // Initialize progress bar
        btnback = findViewById(R.id.backbtn);

        // Set click listener for the registration button
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(v.VISIBLE); // Show progress bar while registering
                String email, password;
                email = String.valueOf(editTextEmail.getText()); // Get entered email
                password = String.valueOf(editTextPassword.getText()); // Get entered password

                // Validate email and password fields
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(registerpage.this, "Enter email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(registerpage.this, "Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }
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
}
