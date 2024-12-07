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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");

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

        EditText registerNameET = findViewById(R.id.registerNameET);
        EditText registerEmailET = findViewById(R.id.registerEmailET);
        EditText registerPasswordET = findViewById(R.id.registerPasswordET);
        EditText registerPhoneET = findViewById(R.id.registerPhoneET);
        EditText registerAddressET = findViewById(R.id.registerAddressET);
        CheckBox registerTnCCB = findViewById(R.id.registerTnCCB);
        TextView registerTnCTV = findViewById(R.id.registerTnCTV);
        Button registerButton = findViewById(R.id.registerSubmitButton);
        ImageView registerBackButton = findViewById(R.id.registerBackButton);



        registerBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        registerTnCTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tncIntent = new Intent(RegisterActivity.this, TnCActivity.class);
                startActivity(tncIntent);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = registerNameET.getText().toString();
                String email = registerEmailET.getText().toString();
                String password = registerPasswordET.getText().toString();
                String phone = registerPhoneET.getText().toString();
                String address = registerAddressET.getText().toString();

                if(name.isEmpty()) {
                    registerNameET.setError("Name cannot be empty");
                } else if(name.length() < 4) {
                    registerNameET.setError("Name must be atleast 4 characters");
                } else if(email.isEmpty()) {
                    registerEmailET.setError("Email cannot be empty");
                } else if(password.isEmpty()) {
                    registerPasswordET.setError("Password cannot be empty");
                } else if(password.length() < 6) {
                    registerPasswordET.setError("Password must be atleast 6 characters");
                } else if(phone.isEmpty()) {
                    registerPhoneET.setError("Phone cannot be empty");
                } else if(address.isEmpty()) {
                    registerAddressET.setError("Address cannot be empty");
                } else if(!registerTnCCB.isChecked()) {
                    registerTnCCB.setError("You must agree to the terms and conditions");
                } else {
                    signUpUser(name, email, phone, address, password);
                    Toast.makeText(RegisterActivity.this, "Signup Success", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });

    }
    public void signUpUser(String name, String email, String phone, String address, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, task -> {
            if (task.isSuccessful()) {
                Log.d("register", "signup success");
                firebaseAuth.signInWithEmailAndPassword(email, password);
                addUserToDatabase(name, email, phone, address);
            } else {
                EditText registerEmailET = findViewById(R.id.registerEmailET);
                registerEmailET.setError("Enter a valid email format (example@domain.com)");
            }
        });
    }

    public void addUserToDatabase(String name, String email, String phone, String address) {
        if(firebaseAuth.getCurrentUser() != null) {
            String userId = firebaseAuth.getCurrentUser().getUid();
            User newUser = new User(name, email, phone, address);
            databaseReference.child(userId).setValue(newUser);
        } else {
            Log.d("register", "addUserToDatabase: currentuser is null");
        }


    }
}