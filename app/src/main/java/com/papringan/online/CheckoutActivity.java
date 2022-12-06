package com.papringan.online;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.icu.number.NumberFormatter;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.papringan.online.adapters.CartAdapter;
import com.papringan.online.adapters.OrderItemAdapter;
import com.papringan.online.models.CartItem;
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

public class CheckoutActivity extends AppCompatActivity {
    ArrayList<OrderItem> cartItemArrayList = new ArrayList<>();
    RecyclerView rvProduct;
    RequestQueue queue;
    OrderItemAdapter adapter;
    String token;
    TextView tvShippingEtd, tvShippingPrice, tvShippingService, tvTotalPurchasePrice, tvTotalShippingCostPrice, tvTotalPaymentPrice;
    ImageView btnBack;
    Button btnCheckout;
    ArrayList<PaymentMethod> paymentMethodsArrayList = new ArrayList<>();
    ArrayList<ShippingOption> shippingOptionsArrayList = new ArrayList<>();

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'");
    NumberFormat formatter2 = NumberFormat.getCurrencyInstance(new Locale("in", "ID"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        btnBack = findViewById(R.id.checkout_btnBack);
        btnCheckout = findViewById(R.id.checkout_btnCheckout);
        tvShippingEtd = findViewById(R.id.checkout_tvShippingEtd);
        tvShippingPrice = findViewById(R.id.checkout_tvShippingCost);
        tvShippingService = findViewById(R.id.checkout_tvShippingService);
        tvTotalPurchasePrice = findViewById(R.id.checkout_tvTotalPurchasePrice);
        tvTotalPaymentPrice = findViewById(R.id.checkout_tvTotalPaymentPrice);
        tvTotalShippingCostPrice = findViewById(R.id.checkout_tvTotalShippingCostPrice);

        queue = Volley.newRequestQueue(this);
        adapter = new OrderItemAdapter(cartItemArrayList, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvProduct = findViewById(R.id.checkout_rvProduct);
        rvProduct.setLayoutManager(layoutManager);
        rvProduct.setAdapter(adapter);

        token = getApplicationContext().getSharedPreferences("data", MODE_PRIVATE).getString("token", "");

        btnCheckout.setOnClickListener(v -> {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("shipping_option_id", 1);
                jsonObject.put("payment_method_id", 1);
                if (getIntent().getStringExtra("action").equals("checkout")) {
                    jsonObject.put("cart_items_id", new JSONArray((ArrayList) getIntent().getSerializableExtra("cart_items_id")));
                } else {
                    ArrayList<Integer> cartItemsId = new ArrayList<>();
                    cartItemsId.add(getIntent().getIntExtra("product_id", 0));
                    jsonObject.put("cart_items_id", new JSONArray(cartItemsId));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, ApiURL.BASE_URL + "checkout/confirm", jsonObject, response -> {
                try {
                    if (response.getString("status").equals("success")) {
                        startActivity(new Intent(this, OrderDetailActivity.class).putExtra("order_id", response.getInt("order_id")));
                        finish();
                    } else {
                        Toast.makeText(this, response.getString("message"), Toast.LENGTH_SHORT).show();
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
        });

        JSONObject req = new JSONObject();
        String action = getIntent().getStringExtra("action");
        String url = ApiURL.BASE_URL + "checkout";
        if (action.equals("checkout")) {
            ArrayList cartItemsId = (ArrayList) getIntent().getSerializableExtra("cart_items_id");
            try {
                req.put("cart_items_id", new JSONArray(cartItemsId));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else  {
            url += "/product";
            Product product = (Product) getIntent().getSerializableExtra("product");
            try {
                req.put("product_id", product.getId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, req, response -> {
            try {
                if (response.getString("status").equals("success")) {
                    JSONObject data = response.getJSONObject("data");
                    JSONArray cartItems = data.getJSONArray("cartItems");
                    for (int i = 0; i < cartItems.length(); i++) {
                        JSONObject cartItem = cartItems.getJSONObject(i);
                        JSONObject iProduct = cartItem.getJSONObject("product");
                        cartItemArrayList.add(new OrderItem(
                                cartItem.getInt("id"),
                                cartItem.getInt("customer_id"),
                                new Product(
                                        iProduct.getInt("id"),
                                        iProduct.getString("name"),
                                        iProduct.getInt("price"),
                                        iProduct.getString("description"),
                                        iProduct.getString("image"),
                                        iProduct.getInt("stock"),
                                        iProduct.getInt("weight"),
                                        iProduct.getInt("dimension_x"),
                                        iProduct.getInt("dimension_y"),
                                        iProduct.getInt("dimension_z"),
                                        LocalDateTime.parse(iProduct.getString("created_at"), formatter),
                                        LocalDateTime.parse(iProduct.getString("updated_at"), formatter)
                                ),
                                cartItem.getInt("quantity"),
                                LocalDateTime.parse(cartItem.getString("created_at"), formatter),
                                LocalDateTime.parse(cartItem.getString("updated_at"), formatter)
                        ));
                    }
                    adapter.notifyDataSetChanged();
                    JSONArray paymentMethods = data.getJSONArray("paymentMethods");
                    for (int i = 0; i < paymentMethods.length(); i++) {
                        JSONObject paymentMethod = paymentMethods.getJSONObject(i);
                        paymentMethodsArrayList.add(new PaymentMethod(
                                paymentMethod.getInt("id"),
                                paymentMethod.getString("name"),
                                paymentMethod.getString("instruction"),
                                LocalDateTime.parse(paymentMethod.getString("created_at"), formatter),
                                LocalDateTime.parse(paymentMethod.getString("updated_at"), formatter)
                        ));
                    }
                    JSONArray shippingOptions = data.getJSONArray("shippingOptions");
                    for (int i = 0; i < shippingOptions.length(); i++) {
                        JSONObject shippingOption = shippingOptions.getJSONObject(i);
                        shippingOptionsArrayList.add(new ShippingOption(
                                shippingOption.getInt("id"),
                                shippingOption.getString("code"),
                                shippingOption.getString("courier"),
                                shippingOption.getString("service"),
                                shippingOption.getString("description"),
                                shippingOption.getInt("cost"),
                                shippingOption.getString("etd")
                        ));
                    }
                    tvShippingEtd.setText("akan diterima dalam " + shippingOptionsArrayList.get(0).getEtd() + " hari");
                    tvShippingPrice.setText(formatter2.format(shippingOptionsArrayList.get(0).getCost()));
                    tvShippingService.setText("JNE " + shippingOptionsArrayList.get(0).getService());
                    tvTotalPurchasePrice.setText(formatter2.format(data.getInt("total")));
                    tvTotalPaymentPrice.setText(formatter2.format(data.getInt("total") + shippingOptionsArrayList.get(0).getCost()));
                    tvTotalShippingCostPrice.setText(formatter2.format(shippingOptionsArrayList.get(0).getCost()));
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