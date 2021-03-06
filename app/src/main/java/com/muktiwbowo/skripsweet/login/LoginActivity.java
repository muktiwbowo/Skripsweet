package com.muktiwbowo.skripsweet.login;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.muktiwbowo.skripsweet.other.MainActivity;
import com.muktiwbowo.skripsweet.R;
import com.muktiwbowo.skripsweet.Url;
import com.muktiwbowo.skripsweet.apoteker.AdminActivity;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private Button btnLogin;
    private EditText edtUser;
    private EditText edtPass;
    private SharedPreferences sharedPreferences;
    private RequestQueue requestQueue;
    private ProgressDialog progressDialog;
    Button btnRegister;
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnRegister = (Button) findViewById(R.id.btn_register);
        edtUser = (EditText) findViewById(R.id.edt_username);
        edtPass = (EditText) findViewById(R.id.edt_password);
        sharedPreferences = getSharedPreferences("users", Context.MODE_APPEND);
        requestQueue = Volley.newRequestQueue(this);
        progressDialog = new ProgressDialog(this);
        Session session = new Session(this);
        if (session.isLoggedIn()==true){
            if (session.getRole().equals("admin")){
                startActivity(new Intent(this,AdminActivity.class));
            }else {
                startActivity(new Intent(this,MainActivity.class));
            }
            finish();
        }
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLogin();
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }
    private void checkLogin() {
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url.uLogin,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("hasil").equals("sukses")) {
                                String role = jsonObject.getJSONObject("data").getString("status");
                                String user = jsonObject.getJSONObject("data").getString("username");
                                if (role.equals("pasien")) {
                                    Session session = new Session(getApplicationContext());
                                    session.storeData(jsonObject.getJSONObject("data").toString()
                                            , role);
                                    session.storeUsername(jsonObject.getJSONObject("data").toString(), user);

                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Session session = new Session(getApplicationContext());
                                    session.storeData(jsonObject.getJSONObject("data").toString(),
                                            role);
                                    session.storeUsername(jsonObject.getJSONObject("data").toString(), user);

                                    Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            } else {
                                Toast.makeText(LoginActivity.this, "login failed", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(LoginActivity.this, "username dan password cannot be empty " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> stringMap = new HashMap<String, String>();
                stringMap.put("username",edtUser.getText().toString());
                stringMap.put("password",edtPass.getText().toString());
                return stringMap;
            }
        };
        requestQueue.add(stringRequest);
    }
}
