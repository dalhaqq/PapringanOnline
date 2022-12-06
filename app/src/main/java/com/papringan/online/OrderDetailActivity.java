package com.papringan.online;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.papringan.online.adapters.OrderItemAdapter;
import com.papringan.online.models.OrderItem;
import com.papringan.online.models.PaymentMethod;
import com.papringan.online.models.Product;
import com.papringan.online.models.ShippingOption;
import com.papringan.online.utils.ApiURL;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class OrderDetailActivity extends AppCompatActivity {
    ArrayList<OrderItem> cartItemArrayList = new ArrayList<>();
    RecyclerView rvProduct;
    RequestQueue queue;
    OrderItemAdapter adapter;
    String token;
    TextView tvShippingEtd, tvShippingPrice, tvShippingService, tvTotalPurchasePrice, tvTotalShippingCostPrice, tvTotalPaymentPrice;
    ImageView btnBack;
    ArrayList<PaymentMethod> paymentMethodsArrayList = new ArrayList<>();
    ArrayList<ShippingOption> shippingOptionsArrayList = new ArrayList<>();

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'");
    NumberFormat formatter2 = NumberFormat.getCurrencyInstance(new Locale("in", "ID"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        int id = getIntent().getIntExtra("order_id", 0);

        btnBack = findViewById(R.id.orderDetail_btnBack);
        tvShippingEtd = findViewById(R.id.orderDetail_tvShippingEtd);
        tvShippingPrice = findViewById(R.id.orderDetail_tvShippingCost);
        tvShippingService = findViewById(R.id.orderDetail_tvShippingService);
        tvTotalPurchasePrice = findViewById(R.id.orderDetail_tvTotalPurchasePrice);
        tvTotalPaymentPrice = findViewById(R.id.orderDetail_tvTotalPaymentPrice);
        tvTotalShippingCostPrice = findViewById(R.id.orderDetail_tvTotalShippingCostPrice);

        queue = Volley.newRequestQueue(this);
        adapter = new OrderItemAdapter(cartItemArrayList, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvProduct = findViewById(R.id.checkout_rvProduct);
        rvProduct.setLayoutManager(layoutManager);
        rvProduct.setAdapter(adapter);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, ApiURL.BASE_URL + "order/" + String.valueOf(id), null, response -> {
            try {
                if (response.getString("status").equals("success")) {
                    JSONObject data = response.getJSONObject("data");

                    int total = 0;
                    JSONArray items = data.getJSONArray("items");
                    for (int i = 0; i < items.length(); i++) {
                        JSONObject item = items.getJSONObject(i);
                        JSONObject product = item.getJSONObject("product");
                        cartItemArrayList.add(new OrderItem(
                                item.getInt("id"),
                                item.getInt("order_id"),
                                new Product(
                                        product.getInt("id"),
                                        product.getString("name"),
                                        product.getInt("price"),
                                        product.getString("description"),
                                        product.getString("image"),
                                        product.getInt("stock"),
                                        product.getInt("weight"),
                                        product.getInt("length"),
                                        product.getInt("width"),
                                        product.getInt("height"),
                                        LocalDateTime.parse(product.getString("created_at"), formatter),
                                        LocalDateTime.parse(product.getString("updated_at"), formatter)
                                ),
                                item.getInt("quantity"),
                                LocalDateTime.parse(item.getString("created_at"), formatter),
                                LocalDateTime.parse(item.getString("updated_at"), formatter)));
                        total += item.getInt("quantity") * product.getInt("price");
                    }
                    adapter.notifyDataSetChanged();
                    tvShippingEtd.setText("akan diterima dalam " + data.getString("shipping_etd") + " hari");
                    tvShippingPrice.setText(formatter2.format(data.getInt("shipping_cost")));
                    tvShippingService.setText("JNE " + data.getString("shipping_service_name"));
                    tvTotalPurchasePrice.setText(formatter2.format(data.getInt("total")));
                    tvTotalPaymentPrice.setText(formatter2.format(total + data.getString("shipping_cost")));
                    tvTotalShippingCostPrice.setText(formatter2.format(data.getInt("shipping_cost")));
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
        request.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }
}