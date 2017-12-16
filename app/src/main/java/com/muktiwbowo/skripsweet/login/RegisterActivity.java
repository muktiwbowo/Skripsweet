package com.muktiwbowo.skripsweet.login;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.muktiwbowo.skripsweet.R;
import com.muktiwbowo.skripsweet.Url;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText rUsername, rPassword, rePassword;
    private Button btnCreate;
    ProgressDialog progressDialog;
    RequestQueue regQuee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        rUsername = (EditText) findViewById(R.id.edt_rusername);
        rPassword = (EditText) findViewById(R.id.edt_rpassword);
        rePassword = (EditText) findViewById(R.id.edt_repassword);
        btnCreate = (Button) findViewById(R.id.btn_create);

        progressDialog = new ProgressDialog(this);
        regQuee = Volley.newRequestQueue(this);

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });
    }

    private void createAccount() {
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest requestRegister = new StringRequest(Request.Method.POST, Url.uRegister, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.cancel();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String hasil= jsonObject.getString("hasil");
                    if (hasil.equals("sukses")){
                        Toast.makeText(RegisterActivity.this, "Akun berhasil dibuat", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    }else {
                        Toast.makeText(RegisterActivity.this, jsonObject.getString("pesan"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.cancel();
                Toast.makeText(RegisterActivity.this,  "Create account failed", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> rparams = new HashMap<String, String>();
                rparams.put("username", rUsername.getText().toString());
                rparams.put("password", rPassword.getText().toString());
                rparams.put("repassword", rePassword.getText().toString());
                return rparams;
            }
        };
        regQuee.add(requestRegister);
    }

}
