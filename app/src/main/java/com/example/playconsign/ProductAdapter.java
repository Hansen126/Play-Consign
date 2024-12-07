package com.example.playconsign;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList;
    Context context;

    public ProductAdapter(List<Product> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View layoutView = inflater.inflate(R.layout.search_item_layout, parent, false);
        ProductViewHolder holder = new ProductViewHolder(layoutView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ProductViewHolder holder, int position) {
        String productName = productList.get(position).getProductName();
        String productImage = productList.get(position).getProductImage();
        int productPrice = productList.get(position).getProductPrice();
        String productCategory = productList.get(position).getProductCategory();
        String productCondition = productList.get(position).getProductCondition();
        String productDesc = productList.get(position).getProductDesc();
        String productSeller = productList.get(position).getProductSeller();
        String productSellerAddress = productList.get(position).getProductSellerAddress();

        String newProductPrice = NumberFormat.getNumberInstance(new Locale("id", "ID")).format(productPrice);

        holder.productName.setText(productName);
        holder.productPrice.setText("Rp " + newProductPrice);
        holder.productCategory.setText(productCategory);
        holder.productCondition.setText(productCondition);
        holder.productSeller.setText(productSeller);
        Log.d("ProductAdapter", "Image URL: " + productImage);
        Glide.with(context)
                .load(productImage)
                .placeholder(R.drawable.ic_image)
                .into(holder.productImage);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ProductViewHolder extends  RecyclerView.ViewHolder {
        TextView productName;
        ImageView productImage;
        TextView productPrice;
        TextView productCategory;
        TextView productCondition;
        TextView productDesc;
        TextView productSeller;
        TextView productSellerAddress;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.searchItemLayout_productName);
            productImage = itemView.findViewById(R.id.searchItemLayout_productImage);
            productPrice = itemView.findViewById(R.id.searchItemLayout_productPrice);
            productCategory = itemView.findViewById(R.id.searchItemLayout_productCategory);
            productCondition = itemView.findViewById(R.id.searchItemLayout_productCondition);
            productSeller = itemView.findViewById(R.id.searchItemLayout_productSeller);
        }
    }
}
