package com.papringan.online;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.papringan.online.utils.ApiURL;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {
    EditText etName, etUsername, etPassword, etEmail, etPasswordConfirm;
    Button btnRegister;
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etName = findViewById(R.id.register_etName);
        etUsername = findViewById(R.id.register_etUsername);
        etEmail = findViewById(R.id.register_etEmail);
        etPassword = findViewById(R.id.register_etPassword);
        etPasswordConfirm = findViewById(R.id.register_etConfirmPassword);
        btnRegister = findViewById(R.id.register_btnRegister);

        queue = Volley.newRequestQueue(this);

        ((Button) findViewById(R.id.register_btnRegister)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                JSONObject data = new JSONObject();
                try {
                    data.put("name", etName.getText().toString());
                    data.put("username", etUsername.getText().toString());
                    data.put("email", etEmail.getText().toString());
                    data.put("password", etPassword.getText().toString());
                    data.put("password_confirmation", etPasswordConfirm.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, ApiURL.BASE_URL + "auth/register", data, response -> {

                    try {
                        if (response.getString("status").equals("success")) {
                        } else {
                            Toast.makeText(RegisterActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }, error -> {
                    Toast.makeText(RegisterActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                });
                queue.add(request);
            }
        });
    }
}