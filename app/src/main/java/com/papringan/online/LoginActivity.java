package com.papringan.online;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.papringan.online.utils.ApiURL;

import org.json.JSONException;
import org.json.JSONObject;


public class LoginActivity extends AppCompatActivity {
    private EditText etUsername, etPassword;
    private Button btnLogin;

    TextView tvRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        RequestQueue queue = Volley.newRequestQueue(this);

        etUsername = findViewById(R.id.login_etUsername);
        etPassword = findViewById(R.id.login_etPassword);
        btnLogin = findViewById(R.id.login_btnLogin);

        tvRegister = findViewById(R.id.login_tvCreate);
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = ApiURL.BASE_URL + "auth/login";
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                JSONObject data = new JSONObject();
                try {
                    data.put("username", username);
                    data.put("password", password);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, data, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("status").equals("success")) {
                                SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("data", Context.MODE_PRIVATE);
                                SharedPreferences.Editor edit = sharedPref.edit();
                                edit.putString("token", response.getString("access_token"));
                                edit.apply();
                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                            } else {
                                Toast.makeText(getBaseContext(), "Login gagal", Toast.LENGTH_SHORT).show();
                                Log.d("error", response.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("error", e.getMessage());
                        }
                    }
                }, error -> {
                    Toast.makeText(getBaseContext(), error.toString(), Toast.LENGTH_SHORT).show();
                    error.printStackTrace();
                });
                queue.add(jsonObjectRequest);
            }
        });
    }
}