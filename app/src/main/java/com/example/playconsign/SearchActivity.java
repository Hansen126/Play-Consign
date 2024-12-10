package com.example.playconsign;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
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

import com.google.firebase.auth.FirebaseAuth;
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
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    ProductAdapter productAdapter;
    String UID = null;
    String category = null;

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
                finish();
            }
        });

        Intent getIntent = getIntent();
        category = getIntent.getStringExtra("category");
        if (category == null) {
            category = "All";
        }
        CharSequence query = getIntent.getCharSequenceExtra("query");
        if (query == null) {
            query = "";
        }
        UID = getIntent.getStringExtra("SellerProduct");
        if(firebaseAuth.getCurrentUser() != null) {
            if(UID != null && UID.equals(firebaseAuth.getCurrentUser().getUid())) {

            }
        }


        SearchView searchSearchView = findViewById(R.id.searchSearchView);
        TextView searchCategoryTV = findViewById(R.id.searchCategoryTV);
        TextView searchResultTV = findViewById(R.id.searchResultTV);
        searchSearchView.setQuery(query, true);

        searchCategoryTV.setText("Category: " + category);
        if(searchSearchView.getQuery().equals("") || query.equals("")) {
            searchResultTV.setText("");
            searchResultTV.setVisibility(View.GONE);
        } else if(searchSearchView.getQuery().equals("") || searchSearchView.getQuery() != null){
            searchResultTV.setText("You Search for " + (CharSequence) searchSearchView.getQuery());
            searchResultTV.setVisibility(View.VISIBLE);
        } else if(!query.equals("") || query != null) {
            searchResultTV.setText("You Search for " + query);
            searchResultTV.setVisibility(View.VISIBLE);
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
                searchIntent.putExtra("category", category);
                searchIntent.putExtra("SellerProduct", UID);
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
        productRV.setLayoutManager(new GridLayoutManager(this, calculateSpanCount()));
        productRV.setAdapter(productAdapter);

    }

    private void fetchProductData(String category, String result) {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productList.clear();
                if(UID != null) {
                    if(category.equals("All") && (result.equals(null) || result.equals(""))) {
                        for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                            Product product = productSnapshot.getValue(Product.class);
                            if (product != null && UID.equals(product.getProductSellerUID())) {
                                productList.add(product);
                            }
                        }
                    } else if(!category.equals("All") && !(result.equals(null) || result.equals(""))){
                        for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                            Product product = productSnapshot.getValue(Product.class);
                            if (product != null && product.getProductCategory().equals(category) && product.getProductName().toLowerCase().contains(result) && UID.equals(product.getProductSellerUID())) {
                                productList.add(product);
                            }
                        }
                    } else if(!(result.equals(null) || result.equals(""))) {
                        for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                            Product product = productSnapshot.getValue(Product.class);
                            if (product != null && product.getProductName().toLowerCase().contains(result) && UID.equals(product.getProductSellerUID())) {
                                productList.add(product);
                            }
                        }
                    } else if(!category.equals("All")){
                        for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                            Product product = productSnapshot.getValue(Product.class);
                            if (product != null && product.getProductCategory().equals(category) && UID.equals(product.getProductSellerUID())) {
                                productList.add(product);
                            }
                        }
                    }
                } else {
                    if(category.equals("All") && (result.equals(null) || result.equals(""))) {
                        for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                            Product product = productSnapshot.getValue(Product.class);
                            if (product != null ) {

                                productList.add(product);
                            }
                        }
                    } else if(!category.equals("All") && !(result.equals(null) || result.equals(""))){
                        for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                            Product product = productSnapshot.getValue(Product.class);
                            if (product != null && product.getProductCategory().equals(category) && product.getProductName().toLowerCase().contains(result)) {
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
                    } else if(!category.equals("All")){
                        for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                            Product product = productSnapshot.getValue(Product.class);
                            if (product != null && product.getProductCategory().equals(category)) {
                                productList.add(product);
                            }
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

    private int calculateSpanCount() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float screenWidthDp = displayMetrics.widthPixels / displayMetrics.density;
        float itemWidthDp = 180;
        return Math.max(1, (int) (screenWidthDp / itemWidthDp));
    }
}