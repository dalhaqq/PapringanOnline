package com.papringan.online;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.papringan.online.models.CartItem;
import com.papringan.online.models.Product;
import com.papringan.online.utils.ApiURL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ProductDetailActivity extends AppCompatActivity {
    private Product product;
    private TextView tvName, tvPrice, tvDescription;
    private ImageView btnBack, ivImage;
    private Button btnBuy, btnCart;
    private RequestQueue queue;
    String token;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        Locale locale = new Locale("id", "ID");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);

        queue = Volley.newRequestQueue(this);

        token = getApplicationContext().getSharedPreferences("data", MODE_PRIVATE).getString("token", "");

        product = (Product) getIntent().getParcelableExtra("product");
        tvName = findViewById(R.id.productDetail_tvProductName);
        tvPrice = findViewById(R.id.productDetail_tvProductPrice);
        tvDescription = findViewById(R.id.productDetail_tvProductDescription);
        btnBack = findViewById(R.id.productDetail_btnBack);
        ivImage = findViewById(R.id.productDetail_ivProductImage);
        btnBuy = findViewById(R.id.productDetail_btnBuy);
        btnCart = findViewById(R.id.productDetail_btnCart);

        tvName.setText(product.getName());
        tvPrice.setText(numberFormat.format(product.getPrice()));
        tvDescription.setText(product.getDescription());

        btnBack.setOnClickListener(view -> finish());
        btnBuy.setOnClickListener(view -> {
            Intent intent = new Intent(this, CheckoutActivity.class);
            intent.putExtra("action", "buy");
            intent.putExtra("product", product);
            startActivity(intent);
        });
        JSONObject body = new JSONObject();
        try {
            body.put("product_id", product.getId());
            body.put("quantity", 1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        btnCart.setOnClickListener(view -> {
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, ApiURL.BASE_URL + "cart", body, response -> {
                try {
                    if (response.getString("status").equals("success")) {
                        Toast.makeText(this, "Berhasil menambahkan ke keranjang", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Gagal menambahkan ke keranjang", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }, error -> {
                Toast.makeText(this, "Gagal menambahkan ke keranjang", Toast.LENGTH_SHORT).show();
            }) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> params = new HashMap<>();
                    params.put("Authorization", "Bearer " + token);
                    return params;
                }
            };
            queue.add(request);
        });
    }
}