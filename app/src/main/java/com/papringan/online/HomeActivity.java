package com.papringan.online;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.papringan.online.adapters.HomeProductAdapter;
import com.papringan.online.models.Product;
import com.papringan.online.utils.ApiURL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {
    ArrayList<Product> products = new ArrayList<>();
    RecyclerView recyclerView;
    HomeProductAdapter adapter;
    RequestQueue queue;
    String token;
    ImageView btnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        token = getApplicationContext().getSharedPreferences("data", MODE_PRIVATE).getString("token", "");
        recyclerView = findViewById(R.id.home_rvProducts);
        queue = Volley.newRequestQueue(this);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        adapter = new HomeProductAdapter(products, this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        getProducts("");

        ImageView ic_profile = findViewById(R.id.home_imgProfile);
        ic_profile.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
        });

        ImageView ic_orders = findViewById(R.id.home_imgOrders);
        ic_orders.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, OrdersActivity.class));
        });

        ImageView ic_cart = findViewById(R.id.home_imgCart);
        ic_cart.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, CartActivity.class));
        });

        ImageView ic_home = findViewById(R.id.home_imgHome);
        ic_home.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, HomeActivity.class));
        });

        btnSearch = findViewById(R.id.home_btnSearch);
        final boolean[] isSearch = {false};
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etSearch = findViewById(R.id.home_etSearch);
                if (isSearch[0]) {
                    btnSearch.setImageResource(R.drawable.ic_search);
                    isSearch[0] = false;
                    getProducts("");
                    etSearch.setText("");
                } else {
                    btnSearch.setImageResource(R.drawable.ic_close);
                    isSearch[0] = true;
                    getProducts(etSearch.getText().toString());
                }
            }
        });
    }

    private void getProducts(String keyword) {
        ProgressBar progressBar = new ProgressBar(this);
        progressBar.setVisibility(View.VISIBLE);
        String url = keyword == "" ? ApiURL.BASE_URL + "product" : ApiURL.BASE_URL + "product/search/" + keyword;
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("status").equals("success")) {
                        JSONArray data = response.getJSONArray("data");
                        products.clear();
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject product = data.getJSONObject(i);
                            String createdAt = product.getString("created_at");
                            String updatedAt = product.getString("updated_at");
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'");
                            products.add(new Product(
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
                                    LocalDateTime.parse(createdAt, formatter),
                                    LocalDateTime.parse(updatedAt, formatter)
                            ));
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(HomeActivity.this, "Gagal mengambil data", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(HomeActivity.this, "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
                Log.e("Volley", error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer " + token);
                return params;
            }
        };
        queue.add(jsonArrayRequest);
    }
}