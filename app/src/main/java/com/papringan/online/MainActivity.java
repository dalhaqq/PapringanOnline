package com.papringan.online;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.papringan.online.utils.ApiURL;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = ApiURL.BASE_URL + "profile";

        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("data", Context.MODE_PRIVATE);
        String token = sharedPref.getString("token", "");
        if (token.equals("")) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("status").equals("success")) {
                        startActivity(new Intent(MainActivity.this, HomeActivity.class));
                    } else {
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, error -> {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
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
}