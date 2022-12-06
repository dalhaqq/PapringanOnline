package com.papringan.online;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.papringan.online.adapters.CartAdapter;
import com.papringan.online.models.CartItem;
import com.papringan.online.models.Product;
import com.papringan.online.utils.ApiURL;

import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class CartActivity extends AppCompatActivity {
    ArrayList<CartItem> cartItems = new ArrayList<>();
    RecyclerView rvCart;
    RequestQueue queue;
    CartAdapter adapter;
    String token;
    ImageView btnBack;
    Button btnShop, btnCheckout;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        btnBack = findViewById(R.id.cart_btnBack);
        btnShop = findViewById(R.id.cart_btnShop);
        btnCheckout = findViewById(R.id.cart_btnCheckout);

        queue = Volley.newRequestQueue(this);
        adapter = new CartAdapter(cartItems, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        rvCart = findViewById(R.id.cart_rvCart);
        rvCart.setLayoutManager(layoutManager);
        rvCart.setAdapter(adapter);

        token = getApplicationContext().getSharedPreferences("data", MODE_PRIVATE).getString("token", "");

        btnShop.setOnClickListener(v -> {
            finish();
        });

        btnBack.setOnClickListener(v -> {
            finish();
        });

        btnCheckout.setOnClickListener(v -> {
            if (!isCheckedExist()) {
                Toast.makeText(this, "Please select at least one item", Toast.LENGTH_SHORT).show();
                return;
            } else {
                Intent intent = new Intent(CartActivity.this, CheckoutActivity.class);
                intent.putExtra("action", "checkout");
                intent.putExtra("cart_items_id", getCheckedItemIds());
                startActivity(intent);
            }
        });

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, ApiURL.BASE_URL + "cart", null, response -> {
            try {
                if (response.getString("status").equals("success")) {
                    JSONArray data = response.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject item = data.getJSONObject(i);
                        JSONObject product = item.getJSONObject("product");

                        cartItems.add(new CartItem(
                                item.getInt("id"),
                                item.getInt("customer_id"),
                                new Product(
                                        product.getInt("id"),
                                        product.getString("name"),
                                        product.getInt("price"),
                                        product.getString("description"),
                                        product.getString("image"),
                                        product.getInt("stock"),
                                        product.getInt("weight"),
                                        product.getInt("dimension_x"),
                                        product.getInt("dimension_y"),
                                        product.getInt("dimension_z"),
                                        LocalDateTime.parse(product.getString("created_at"), formatter),
                                        LocalDateTime.parse(product.getString("updated_at"), formatter)
                                ),
                                item.getInt("quantity"),
                                LocalDateTime.parse(item.getString("created_at"), formatter),
                                LocalDateTime.parse(item.getString("updated_at"), formatter)
                        ));
                    }
                    adapter.notifyDataSetChanged();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, error -> {
            error.printStackTrace();
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer " + token);
                return params;
            }
        };
        queue.add(request);
    }

    private boolean isCheckedExist() {
        for (CartItem item : cartItems) {
            if (item.isChecked()) {
                return true;
            }
        }
        return false;
    }

    private ArrayList getCheckedItemIds() {
        ArrayList ids = new ArrayList();
        for (CartItem item : cartItems) {
            if (item.isChecked()) {
                ids.add(item.getId());
            }
        }
        return ids;
    }

    public void updateCart(int product_id, int quantity) {
        JSONObject data = new JSONObject();
        try {
            data.put("product_id", product_id);
            data.put("quantity", quantity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, ApiURL.BASE_URL + "cart/update", data, response -> {
            try {
                if (response.getString("status").equals("success")) {
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, error -> {
            error.printStackTrace();
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer " + token);
                return params;
            }
        };
        queue.add(jsonObjectRequest);
    }
}