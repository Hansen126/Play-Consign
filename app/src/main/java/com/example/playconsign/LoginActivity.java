package com.example.playconsign;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Context context = getApplicationContext();
        EditText loginEmailET = findViewById(R.id.loginEmailET);
        EditText loginPasswordET = findViewById(R.id.loginPasswordET);
        Button loginSubmitButton = findViewById(R.id.loginSubmitButton);
        TextView loginForgotPassTV = findViewById(R.id.loginForgotPassTV);
        TextView loginRegisterTV = findViewById(R.id.loginRegisterTV);
        ImageView loginBackButton = findViewById(R.id.loginBackButton);

        loginRegisterTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
            }
        });

        loginBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        loginForgotPassTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forgotPassIntent = new Intent(LoginActivity.this, ForgotPassActivity.class);
                startActivity(forgotPassIntent);
            }
        });

        loginSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = loginEmailET.getText().toString();
                String password = loginPasswordET.getText().toString();
                if(email.isEmpty()) {
                    loginEmailET.setError("Email cannot be empty");
                } else if(password.isEmpty()) {
                    loginPasswordET.setError("Password cannot be empty");
                } else {

                    firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, task -> {
                        if(task.isSuccessful()) {
                            Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(mainIntent);
                        } else {
                            loginEmailET.setError("Invalid email or password");
                        }
                    });
                }
            }
        });
    }
}