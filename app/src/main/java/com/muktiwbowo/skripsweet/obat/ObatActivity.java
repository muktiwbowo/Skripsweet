package com.muktiwbowo.skripsweet.obat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.muktiwbowo.skripsweet.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ObatActivity extends AppCompatActivity {

    private static final String tag = ObatActivity.class.getSimpleName();
    private static final String url = "https://dabudabu.000webhostapp.com/farnotifphp/getobat.php";
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
        obatAdapter = new ObatAdapter(this, list);
        listView.setAdapter(obatAdapter);

        getObat();
    }

    public void getObat() {

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
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
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

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
}