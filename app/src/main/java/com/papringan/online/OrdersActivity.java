package com.papringan.online;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.papringan.online.adapters.CartAdapter;
import com.papringan.online.models.CartItem;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class OrdersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
    }
}