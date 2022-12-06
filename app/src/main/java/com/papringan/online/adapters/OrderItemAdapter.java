package com.papringan.online.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.papringan.online.CartActivity;
import com.papringan.online.R;
import com.papringan.online.models.OrderItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.ViewHolder> {
    ArrayList<OrderItem> orderItems;
    Context context;

    public OrderItemAdapter(ArrayList<OrderItem> orderItems, Context context) {
        this.orderItems = orderItems;
        this.context = context;
    }

    @NonNull
    @Override
    public OrderItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemAdapter.ViewHolder holder, int position) {
        OrderItem orderItem = orderItems.get(position);
        Locale localeID = new Locale("in", "ID");
        String price = String.format(localeID, "Rp%,d", orderItem.getProduct().getPrice());
        holder.tvName.setText(orderItem.getProduct().getName());
        holder.tvPrice.setText(price);
        holder.tvQuantity.setText(String.valueOf(orderItem.getQuantity()) + " buah");
        Glide.with(context).load(orderItem.getProduct().getImage()).into(holder.ivImage);
    }

    @Override
    public int getItemCount() {
        return orderItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice, tvQuantity;
        ImageView ivImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvOrderItemName);
            tvPrice = itemView.findViewById(R.id.tvOrderItemPrice);
            tvQuantity = itemView.findViewById(R.id.tvOrderItemQuantity);
            ivImage = itemView.findViewById(R.id.ivOrderItemImage);
        }
    }
}
