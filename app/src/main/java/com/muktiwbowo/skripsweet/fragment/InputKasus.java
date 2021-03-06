package com.muktiwbowo.skripsweet.fragment;


import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.muktiwbowo.skripsweet.R;
import com.muktiwbowo.skripsweet.Url;
import com.muktiwbowo.skripsweet.apoteker.Admin;
import com.muktiwbowo.skripsweet.apoteker.AdminActivity;
import com.muktiwbowo.skripsweet.apoteker.AdminAdapter;
import com.muktiwbowo.skripsweet.apoteker.UpdateDetailActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class InputKasus extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private List<Admin> list = new ArrayList<>();
    private ListView listView;
    private AdminAdapter adminAdapter;
    RequestQueue requestQueue;
    SwipeRefreshLayout refreshKasus;

    public InputKasus() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_input_kasus, container, false);

        requestQueue = Volley.newRequestQueue(getActivity());
        listView = (ListView) v.findViewById(R.id.list);
        refreshKasus = (SwipeRefreshLayout) v.findViewById(R.id.refreshk);
        adminAdapter = new AdminAdapter(getActivity(),list);
        listView.setAdapter(adminAdapter);
        refreshKasus.setOnRefreshListener(this);
        refreshKasus.post(new Runnable() {
            @Override
            public void run() {
                refreshKasus.setRefreshing(true);
                getKasus();
            }
        });
        getActivity().setTitle("Kasus");
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Admin a = (Admin) adapterView.getAdapter().getItem(i);
                Intent intent = new Intent(getActivity(), UpdateDetailActivity.class);
                Bundle extras = new Bundle();
                extras.putString("idKasus", a.getKasusId());
                extras.putString("namaKasus", a.getKasusNama());
                extras.putString("namaPasien", a.getPasienNama());
                extras.putString("gender", a.getGendePasien());
                extras.putString("usia", a.getUsiaPasien());
                extras.putString("beratBadan", a.getBadanBerat());
                extras.putString("namaGejala", a.getGejalaNama());
                extras.putString("namaObat", a.getObatNama());
                extras.putString("namaPenyakit", a.getPenyakitNama());
                extras.putString("jenisHabbit",a.getJenisHabbit());
                extras.putString("rekomendasi", a.getSolusi());
                intent.putExtras(extras);
                startActivity(intent);
            }
        });

        return v;
    }

    private void getKasus(){
        refreshKasus.setRefreshing(true);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Url.urlKasus, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response.length() > 0) {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);
                            Admin a = new Admin();
                            a.setKasusId(obj.getString("kode_kasus"));
                            a.setKasusNama(obj.getString("nama_kasus"));
                            a.setPasienNama(obj.getString("nama_pasien"));
                            a.setUsiaPasien(obj.getString("usia"));
                            a.setGendePasien(obj.getString("gender"));
                            a.setBadanBerat(obj.getString("berat_badan"));
                            a.setGejalaNama(obj.getString("nama_gejala"));
                            a.setObatNama(obj.getString("nama_obat"));
                            a.setPenyakitNama(obj.getString("nama_penyakit"));
                            a.setJenisHabbit(obj.getString("jenis_habbit"));
                            a.setSolusi(obj.getString("rekomendasi"));
                            list.add(a);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    adminAdapter.notifyDataSetChanged();
                }
                refreshKasus.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                refreshKasus.setRefreshing(false);
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public void onRefresh() {
        list.clear();
        adminAdapter.notifyDataSetChanged();
        getKasus();
    }
}
