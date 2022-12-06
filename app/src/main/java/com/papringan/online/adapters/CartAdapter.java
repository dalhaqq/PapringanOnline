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
import com.papringan.online.models.CartItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    ArrayList<CartItem> cartItems;
    Context context;

    public CartAdapter(ArrayList<CartItem> cartItems, Context context) {
        this.cartItems = cartItems;
        this.context = context;
    }

    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, int position) {
        CartItem cartItem = cartItems.get(position);
        Locale localeID = new Locale("in", "ID");
        String price = String.format(localeID, "Rp%,d", cartItem.getProduct().getPrice());
        holder.tvName.setText(cartItem.getProduct().getName());
        holder.tvPrice.setText(price);
        holder.tvQuantity.setText(String.valueOf(cartItem.getQuantity()));
        Glide.with(context).load(cartItem.getProduct().getImage()).into(holder.ivImage);
        holder.checkBox.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                cartItem.setChecked(true);
            } else {
                cartItem.setChecked(false);
            }
        });
        holder.btnIncrement.setOnClickListener(view -> {
            int quantity = cartItem.getQuantity();
            cartItem.setQuantity(++quantity);
            holder.tvQuantity.setText(String.valueOf(quantity));
            ((CartActivity) context).updateCart(cartItem.getProduct().getId(), quantity);
        });
        holder.btnDecrement.setOnClickListener(view -> {
            int quantity = cartItem.getQuantity();
            cartItem.setQuantity(--quantity);
            holder.tvQuantity.setText(String.valueOf(quantity));
            ((CartActivity) context).updateCart(cartItem.getProduct().getId(), quantity);
            if (quantity == 0) {
                cartItems.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, cartItems.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice, tvQuantity;
        ImageView ivImage;
        CheckBox checkBox;
        Button btnIncrement, btnDecrement;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.cart_tvCartItemName);
            tvPrice = itemView.findViewById(R.id.cart_tvCartItemPrice);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            ivImage = itemView.findViewById(R.id.cart_ivCartItemImage);
            checkBox = itemView.findViewById(R.id.cart_cbCartItem);
            btnIncrement = itemView.findViewById(R.id.btnIncrement);
            btnDecrement = itemView.findViewById(R.id.btnDecrement);
        }
    }
}
