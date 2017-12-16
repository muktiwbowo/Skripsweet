package com.muktiwbowo.skripsweet.obat;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.muktiwbowo.skripsweet.R;
import com.muktiwbowo.skripsweet.Url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ObatActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private static final String tag = ObatActivity.class.getSimpleName();
    private List<Obat> list = new ArrayList<Obat>();
    private ListView listView;
    SwipeRefreshLayout refreshObat;
    private ObatAdapter obatAdapter;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obat);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        requestQueue = Volley.newRequestQueue(this);
        listView = (ListView) findViewById(R.id.list_obat);
        refreshObat = (SwipeRefreshLayout) findViewById(R.id.refreshObat);
        obatAdapter = new ObatAdapter(this, list);
        listView.setAdapter(obatAdapter);
        refreshObat.setOnRefreshListener(this);
        refreshObat.post(new Runnable() {
            @Override
            public void run() {
                refreshObat.setRefreshing(true);
                getObat();
            }
        });

        //getObat();
    }

    public void getObat() {
        refreshObat.setRefreshing(true);
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, Url.urlObat, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response.length() > 0) {
                            try {
                                JSONArray jsonArray = response.getJSONArray("obat");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    Obat o = new Obat();
                                    o.setIdObat(obj.getString("kode_obat"));
                                    o.setfObat(obj.getString("nama_obat"));
                                    list.add(o);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            obatAdapter.notifyDataSetChanged();
                        }
                        refreshObat.setRefreshing(false);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ObatActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                        refreshObat.setRefreshing(false);
                    }
                }
        );
        requestQueue.add(getRequest);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onRefresh() {
        list.clear();
        obatAdapter.notifyDataSetChanged();
        getObat();
    }
}