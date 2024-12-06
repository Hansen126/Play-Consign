package com.example.playconsign;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_pass);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        ImageView forgotPassBackButton = findViewById(R.id.forgotPassBackButton);
        EditText forgotPassEmailET = findViewById(R.id.forgotPassEmailET);
        Button forgotPassSubmitButton = findViewById(R.id.forgotPassSubmitButton);

        forgotPassBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        forgotPassSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = forgotPassEmailET.getText().toString();
                if(email.isEmpty()) {
                    forgotPassEmailET.setError("Email cannot be empty");
                } else {
                    firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(ForgotPassActivity.this, task -> {
                        if(task.isSuccessful()) {
                            Toast.makeText(ForgotPassActivity.this, "a link has been sent to your email", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(ForgotPassActivity.this, "Enter a valid email", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }
}