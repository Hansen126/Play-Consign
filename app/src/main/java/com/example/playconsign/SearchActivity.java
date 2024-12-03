package com.example.playconsign;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SearchActivity extends AppCompatActivity {

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
        searchCategoryTV.setText("Category: " + category);
        searchResultTV.setText("You Search for " + (CharSequence) searchSearchView.getQuery());
    }
}