package com.example.greenpulse;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
public class ForgotPassword extends AppCompatActivity {

    Button btnreset, btnback;
    EditText editEmail;
    String strEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        btnback = findViewById(R.id.backbtn);
        btnreset = findViewById(R.id.resetpass);
        editEmail = findViewById(R.id.forgotemail);

        btnreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strEmail = editEmail.getText().toString().trim();
                if (!TextUtils.isEmpty(strEmail)) {
                    editEmail.setError("Email Field is Empty");
                } else {
                    editEmail.setError("Email Field is Empty");
                }
            }
        });

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotPassword.this, loginPage.class);
                startActivity(intent);
                finish();
            }
        });

    }
}