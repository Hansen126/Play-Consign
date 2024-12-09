package com.example.playconsign;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.playconsign.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    boolean checkSeller = false;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("sellers");
        if(firebaseAuth.getCurrentUser() != null) {
            Log.e("Miantest", firebaseAuth.getCurrentUser().getUid() + checkSeller);
            String currentUID = firebaseAuth.getCurrentUser().getUid();
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.child(currentUID).exists()) {
                        checkSeller = true;
                    }
                    navBarClickAction();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            navBarClickAction();
        }
    }

    private void navBarClickAction() {
        binding.mainBottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int selectedId = item.getItemId();
                if(selectedId == R.id.mainHomeMenu){
                    switchFragment(new HomeFragment());
                } else if (selectedId == R.id.mainConsignMenu) {
                    if(firebaseAuth.getCurrentUser() == null) {
                        Intent homeIntent = new Intent(MainActivity.this, MainActivity.class);
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(homeIntent);
                        startActivity(intent);
                    } else if (checkSeller == false) {
                        Intent homeIntent = new Intent(MainActivity.this, MainActivity.class);
                        Intent registerSellerintent = new Intent(MainActivity.this, SellerRegisterActivity.class);
                        startActivity(homeIntent);
                        startActivity(registerSellerintent);
                    } else {
                        switchFragment(new ConsignFragment());
                    }
                } else if (selectedId == R.id.mainProfileMenu) {
                    if(firebaseAuth.getCurrentUser() == null) {
                        Intent homeIntent = new Intent(MainActivity.this, MainActivity.class);
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(homeIntent);
                        startActivity(intent);
                    } else {
                        switchFragment(new ProfileFragment());
                    }
                }
                return true;
            }
        });
    }
    private void switchFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.mainFCV, fragment);
        fragmentTransaction.commit();
    }
}