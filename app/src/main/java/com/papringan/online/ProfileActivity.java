package com.papringan.online;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.papringan.online.utils.ApiURL;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
    EditText tvName, tvEmail, tvUsername, tvPhone, tvAddress;
    Button btnSave, btnChangePassword;
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        queue = Volley.newRequestQueue(this);
        tvName = findViewById(R.id.profile_tvName);
        tvEmail = findViewById(R.id.profile_tvEmail);
        tvUsername = findViewById(R.id.profile_tvUsername);
        tvPhone = findViewById(R.id.profile_tvPhoneNumber);
        tvAddress = findViewById(R.id.profile_tvAddress);

        btnSave = findViewById(R.id.profile_btnSave);
        btnChangePassword = findViewById(R.id.profile_btnChangePassword);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, ApiURL.BASE_URL + "profile", null, response -> {
            try {
                if (response.getString("status").equals("success")) {
                    JSONObject data = response.getJSONObject("data");
                    tvName.setText(data.getString("name"));
                    tvEmail.setText(data.getString("email"));
                    tvUsername.setText(data.getString("username"));
                    tvPhone.setText(data.getString("phone"));
                    tvAddress.setText(data.getString("address"));
                } else {
                    Toast.makeText(this, response.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            Toast.makeText(this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer " + getApplicationContext().getSharedPreferences("data", MODE_PRIVATE).getString("token", ""));
                return params;
            }
        };

        queue.add(jsonObjectRequest);

        btnSave.setOnClickListener(v -> {
            Map<String, String> params = new HashMap<>();
            params.put("name", tvName.getText().toString());
            params.put("email", tvEmail.getText().toString());
            params.put("username", tvUsername.getText().toString());
            params.put("phone", tvPhone.getText().toString());
            params.put("address", tvAddress.getText().toString());
            JSONObject jsonObject = new JSONObject(params);
            JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(Request.Method.POST, ApiURL.BASE_URL + "profile", jsonObject, response -> {
                try {
                    if (response.getString("status").equals("success")) {
                        JSONObject data = response.getJSONObject("data");
                        tvName.setText(data.getString("name"));
                        tvEmail.setText(data.getString("email"));
                        tvUsername.setText(data.getString("username"));
                        tvPhone.setText(data.getString("phone"));
                        tvAddress.setText(data.getString("address"));
                        Toast.makeText(this, "Berhasil mengubah data", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, response.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }, error -> {
                Toast.makeText(this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
            }) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> params = new HashMap<>();
                    params.put("Authorization", "Bearer " + getApplicationContext().getSharedPreferences("data", MODE_PRIVATE).getString("token", ""));
                    return params;
                }
            };
            queue.add(jsonObjectRequest1);
        });
    }
}