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
import androidx.recyclerview.widget.GridLayoutManager;
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
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference("products");
    ProductAdapter productAdapter;

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
        String category = getIntent.getStringExtra("category");
        if (category == null) {
            category = "All";
        }
        CharSequence query = getIntent.getCharSequenceExtra("query");
        if (query == null) {
            query = "";
        }

        SearchView searchSearchView = findViewById(R.id.searchSearchView);
        TextView searchCategoryTV = findViewById(R.id.searchCategoryTV);
        TextView searchResultTV = findViewById(R.id.searchResultTV);
        searchSearchView.setQuery(query, true);

        searchCategoryTV.setText("Category: " + category);
        if(searchSearchView.getQuery().equals("") || query.equals("")) {
            searchResultTV.setText("");
        } else if(searchSearchView.getQuery().equals("") || searchSearchView.getQuery() != null){
            searchResultTV.setText("You Search for " + (CharSequence) searchSearchView.getQuery());
        } else if(!query.equals("") || query != null) {
            searchResultTV.setText("You Search for " + query);
        }

        String result = searchSearchView.getQuery().toString().toLowerCase();
        fetchProductData(category, result);

        searchSearchView.setSubmitButtonEnabled(true);
        String finalCategory = category;
        searchSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent searchIntent = new Intent(SearchActivity.this, SearchActivity.class);
                searchIntent.putExtra("query", query);
                startActivity(searchIntent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                fetchProductData(finalCategory, newText);
                return false;
            }
        });


        RecyclerView productRV = findViewById(R.id.searchItemRV);
        productAdapter = new ProductAdapter(productList);
        productRV.setLayoutManager(new GridLayoutManager(this, 2));
        productRV.setAdapter(productAdapter);

    }

    private void fetchProductData(String category, String result) {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productList.clear();
                if(category.equals("All") && (result.equals(null) || result.equals(""))) {
                    for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                        Product product = new Product();
                        product.setProductName(productSnapshot.child("productName").getValue(String.class));
                        product.setProductPrice(productSnapshot.child("productPrice").getValue(Integer.class));
                        product.setProductCategory(productSnapshot.child("productCategory").getValue(String.class));
                        product.setProductCondition(productSnapshot.child("productCondition").getValue(String.class));
                        product.setProductDesc(productSnapshot.child("productDescription").getValue(String.class));
                        product.setProductImage(productSnapshot.child("productImage").getValue(String.class));
                        product.setProductSellerUID(productSnapshot.child("productSellerUID").getValue(String.class));
                        if (product != null) {
                            productList.add(product);
                        }
                    }
                } else if(!category.equals("All")){
                    for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                        Product product = productSnapshot.getValue(Product.class);
                        if (product != null && product.getProductCategory().equals(category)) {
                            productList.add(product);
                        }
                    }
                } else if(!(result.equals(null) || result.equals(""))) {
                    for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                        Product product = productSnapshot.getValue(Product.class);
                        if (product != null && product.getProductName().toLowerCase().contains(result)) {
                            productList.add(product);
                        }
                    }
                }
                productAdapter.notifyDataSetChanged();
                if(productList.isEmpty()) {
                    Log.e("ProductListActivity", "Product is null");
                    ImageView searchNotfoundIV = findViewById(R.id.searchNotfoundIV);
                    TextView searchNotfoundTV = findViewById(R.id.searchNotfoundTV);
                    searchNotfoundIV.setVisibility(View.VISIBLE);
                    searchNotfoundTV.setVisibility(View.VISIBLE);
                }
            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ProductListActivity", "Failed to read data", error.toException());
            }
        });
    }
}