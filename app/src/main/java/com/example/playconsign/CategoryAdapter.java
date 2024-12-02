package com.example.playconsign;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryHolder> {

    Context activityContext;
    List<Category> categoryList;

    public CategoryAdapter(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public CategoryAdapter.CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        activityContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(activityContext);
        View layoutView = inflater.inflate(R.layout.home_category_layout, parent, false);
        CategoryHolder holder = new CategoryHolder(layoutView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.CategoryHolder holder, int position) {
        String categoryName = categoryList.get(position).getCategoryName();
        int categoryImage = categoryList.get(position).getImage();

        holder.categoryName.setText(categoryName);
        holder.categoryImage.setImageResource(categoryImage);

        holder.categoryCL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activityContext, SearchActivity.class);
                activityContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class CategoryHolder extends RecyclerView.ViewHolder {
        ImageView categoryImage;
        TextView categoryName;
        ConstraintLayout categoryCL;

        public CategoryHolder(@NonNull View itemView) {
            super(itemView);
            categoryImage = itemView.findViewById(R.id.homeCategoryLayout_CategoryImage);
            categoryName = itemView.findViewById(R.id.homeCategoryLayout_CategoryName);
            categoryCL = itemView.findViewById(R.id.homeCategoryLayout_ConstraintLayout);
        }

    }
}
