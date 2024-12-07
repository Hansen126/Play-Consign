package com.example.playconsign;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private List<Product> productList = new ArrayList<>();
    private DatabaseReference  databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private ProductAdapter productAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageView backButton = findViewById(R.id.searchBackArrow);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent homeIntent = new Intent(v.getContext(), MainActivity.class);
//                v.getContext().startActivity(homeIntent);
                finish();
            }
        });

        Intent getIntent = getIntent();
        CharSequence query = getIntent.getCharSequenceExtra("query");
        String category = getIntent.getStringExtra("category");

        SearchView searchSearchView = findViewById(R.id.searchSearchView);
        TextView searchCategoryTV = findViewById(R.id.searchCategoryTV);
        TextView searchResultTV = findViewById(R.id.searchResultTV);
        searchSearchView.setQuery(query, true);
        if(category == null) {
            category = "All";
        }
        searchCategoryTV.setText("Category: " + category);
        if(searchSearchView.getQuery() == "" || query == "") {
            searchResultTV.setText("");
        } else if(searchSearchView.getQuery() != "" || searchSearchView.getQuery() != null){
            searchResultTV.setText("You Search for " + (CharSequence) searchSearchView.getQuery());
        } else if(query != "" || query != null) {
            searchResultTV.setText("You Search for " + query);
        }


        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("products");

        fetchProductData();

        RecyclerView productRV = findViewById(R.id.searchItemRV);
        productAdapter = new ProductAdapter(productList);
        productRV.setAdapter(productAdapter);
    }

    private void fetchProductData() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productList.clear(); // Clear old list
                for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                    Product product = productSnapshot.getValue(Product.class);
                    if (product != null) {
                        productList.add(product);
                    }
                }
                productAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ProductListActivity", "Failed to read data", error.toException());
            }
        });
    }
}