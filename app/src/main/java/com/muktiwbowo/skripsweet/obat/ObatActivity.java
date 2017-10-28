package com.muktiwbowo.skripsweet.obat;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.muktiwbowo.skripsweet.R;
import com.muktiwbowo.skripsweet.kasus.Kasus;
import com.muktiwbowo.skripsweet.kasus.KasusActivity;
import com.muktiwbowo.skripsweet.kasus.KasusAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ObatActivity extends AppCompatActivity {

    private static final String tag = ObatActivity.class.getSimpleName();
    private static final String url = "http://192.168.58.1/nyoba/getobat.php";
    private List<Obat> list = new ArrayList<Obat>();
    private ListView listView;
    private ObatAdapter obatAdapter;
    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obat);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        requestQueue = Volley.newRequestQueue(this);
        listView = (ListView) findViewById(R.id.list_obat);
        obatAdapter = new ObatAdapter(this,list);
        listView.setAdapter(obatAdapter);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject obj = response.getJSONObject(i);
                                Obat o = new Obat();
                                o.setfObat(obj.getString("nama_obat"));
                                list.add(o);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        obatAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AlertDialog.Builder add = new AlertDialog.Builder(ObatActivity.this);
                add.setMessage(error.getMessage()).setCancelable(true);
                AlertDialog alert = add.create();
                alert.setTitle("Error!!!");
                alert.show();
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}