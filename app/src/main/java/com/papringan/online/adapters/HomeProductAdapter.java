package com.papringan.online.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.papringan.online.ProductDetailActivity;
import com.papringan.online.R;
import com.papringan.online.models.Product;

import java.util.ArrayList;
import java.util.Locale;

public class HomeProductAdapter extends RecyclerView.Adapter<HomeProductAdapter.ViewHolder> {
    ArrayList<Product> products;
    Context context;

    public HomeProductAdapter(ArrayList<Product> products, Context context) {
        this.products = products;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = products.get(position);
        Locale localeID = new Locale("in", "ID");
        String price = String.format(localeID, "Rp%,d", product.getPrice());
        holder.tvName.setText(product.getName());
        holder.tvPrice.setText(price);
        Glide.with(context).load(product.getImage()).into(holder.ivProduct);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProductDetailActivity.class);
                intent.putExtra("product", (Parcelable) product);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName, tvPrice;
        private ImageView ivProduct;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.product_tvName);
            tvPrice = itemView.findViewById(R.id.product_tvPrice);
            ivProduct = itemView.findViewById(R.id.product_ivImage);
        }
    }
}