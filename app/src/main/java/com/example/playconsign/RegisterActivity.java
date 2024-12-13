package com.example.playconsign;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        EditText registerNameET = findViewById(R.id.registerNameET);
        EditText registerEmailET = findViewById(R.id.registerEmailET);
        EditText registerPasswordET = findViewById(R.id.registerPasswordET);
        EditText registerConfirmPasswordET = findViewById(R.id.registerConfirmPasswordET);
        EditText registerPhoneET = findViewById(R.id.registerPhoneET);
        EditText registerAddressET = findViewById(R.id.registerAddressET);
        CheckBox registerTnCCB = findViewById(R.id.registerTnCCB);
        TextView registerTnCTV = findViewById(R.id.registerTnCTV);
        Button registerButton = findViewById(R.id.registerSubmitButton);
        ImageView registerBackButton = findViewById(R.id.registerBackButton);

        registerBackButton.setOnClickListener(v -> finish());

        registerTnCTV.setOnClickListener(v -> {
            Intent tncIntent = new Intent(RegisterActivity.this, TnCActivity.class);
            startActivity(tncIntent);
        });

        registerButton.setOnClickListener(v -> {
            String name = registerNameET.getText().toString().trim();
            String email = registerEmailET.getText().toString().trim();
            String password = registerPasswordET.getText().toString().trim();
            String confirmPassword = registerConfirmPasswordET.getText().toString().trim();
            String phone = registerPhoneET.getText().toString().trim();
            String address = registerAddressET.getText().toString().trim();

            if (name.isEmpty()) {
                registerNameET.setError("Name cannot be empty");
                registerNameET.requestFocus();
            } else if (name.length() < 4) {
                registerNameET.setError("Name must be at least 4 characters");
                registerNameET.requestFocus();
            } else if (email.isEmpty()) {
                registerEmailET.setError("Email cannot be empty");
                registerEmailET.requestFocus();
            } else if (password.isEmpty()) {
                registerPasswordET.setError("Password cannot be empty");
                registerPasswordET.requestFocus();
            } else if (password.length() < 6) {
                registerPasswordET.setError("Password must be at least 6 characters");
                registerPasswordET.requestFocus();
            } else if (phone.isEmpty()) {
                registerPhoneET.setError("Phone cannot be empty");
                registerPhoneET.requestFocus();
            } else if (address.isEmpty()) {
                registerAddressET.setError("Address cannot be empty");
                registerAddressET.requestFocus();
            } else if (!registerTnCCB.isChecked()) {
                registerTnCCB.setError("You must agree to the terms and conditions");
                registerTnCCB.requestFocus();
            } else if (!password.equals(confirmPassword)) {
                registerConfirmPasswordET.setError("Passwords do not match");
                registerConfirmPasswordET.requestFocus();
            } else {
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
                signUpUser(name, email, phone, address, password);
            }
        });
    }

    private void signUpUser(String name, String email, String phone, String address, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(RegisterActivity.this, task -> {
                    if (task.isSuccessful()) {
                        Log.d("register", "Signup successful");
                        addUserToDatabase(name, email, phone, address);
                    } else {
                        Log.e("register", "Signup failed", task.getException());
                        EditText registerEmailET = findViewById(R.id.registerEmailET);
                        registerEmailET.setError("Enter a valid email format (example@domain.com)");
                        registerEmailET.requestFocus();
                    }
                });
    }

    private void addUserToDatabase(String name, String email, String phone, String address) {
        if (firebaseAuth.getCurrentUser() != null) {
            String userId = firebaseAuth.getCurrentUser().getUid();
            User newUser = new User(name, email, phone, address);

            databaseReference.child(userId).setValue(newUser)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d("register", "User data stored successfully");
                            Toast.makeText(RegisterActivity.this, "Signup Success", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e("register", "Failed to store user data", task.getException());
                            Toast.makeText(RegisterActivity.this, "Failed to store user data. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Log.d("register", "Current user is null");
            Toast.makeText(RegisterActivity.this, "Authentication error. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }
}
