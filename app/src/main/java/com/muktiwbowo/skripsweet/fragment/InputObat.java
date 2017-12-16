package com.muktiwbowo.skripsweet.fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.muktiwbowo.skripsweet.R;
import com.muktiwbowo.skripsweet.Url;
import com.muktiwbowo.skripsweet.obat.Obat;
import com.muktiwbowo.skripsweet.obat.ObatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class InputObat extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private List<InObat> obatList = new ArrayList<InObat>();
    private ListView listView;
    InObatAdapter obatAdapter;
    FloatingActionButton fabo;
    private EditText frObat;
    RequestQueue reqObat, reqInObat;
    SwipeRefreshLayout refreshLayout;

    public InputObat() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_input_obat, container, false);
        getActivity().setTitle("Obat");
        reqObat = Volley.newRequestQueue(getActivity());
        reqInObat = Volley.newRequestQueue(getActivity());
        listView = (ListView) v.findViewById(R.id.listo);
        refreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.refresh);
        obatAdapter = new InObatAdapter(getActivity(), obatList);
        listView.setAdapter(obatAdapter);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
                getFrObat();
            }
        });

        fabo = (FloatingActionButton) v.findViewById(R.id.fabo);
        fabo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFormObat();
            }
        });

        //getFrObat();
        return v;
    }


    private void showFormObat() {
        final AlertDialog.Builder alertB = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.form_obat, null);
        alertB.setView(dialogView);
        frObat = (EditText) dialogView.findViewById(R.id.fr_obat);
        alertB.setTitle("Input Obat");
        alertB.setPositiveButton("Kirim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                inputObat();
            }
        }).setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = alertB.create();
        alertDialog.show();
    }

    private void inputObat() {
        StringRequest obatRequest = new StringRequest(Request.Method.POST, Url.insertObat, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    String hasil = object.getString("hasil");
                    if (hasil.equals("sukses")) {
                        Toast.makeText(getActivity(), object.getString("pesan"), Toast.LENGTH_SHORT).show();
                        onRefresh();
                    } else {
                        Toast.makeText(getActivity(), object.getString("pesan"), Toast.LENGTH_SHORT).show();
                        onRefresh();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("nama_obat", frObat.getText().toString());
                return params;
            }
        };
        reqInObat.add(obatRequest);
    }

    private void getFrObat() {
        refreshLayout.setRefreshing(true);
        JsonObjectRequest obatObjReq = new JsonObjectRequest(Request.Method.GET, Url.urlObat,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response.length() > 0) {
                    try {
                        JSONArray jsonArray = response.getJSONArray("obat");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            InObat io = new InObat();
                            io.setIdObat(object.getString("kode_obat"));
                            io.setNamaObat(object.getString("nama_obat"));
                            //io.setJenis(object.getString("jenis"));
                            obatList.add(io);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    obatAdapter.notifyDataSetChanged();
                }
                refreshLayout.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                refreshLayout.setRefreshing(false);
            }
        });
        reqObat.add(obatObjReq);
    }

    @Override
    public void onRefresh() {
        obatList.clear();
        obatAdapter.notifyDataSetChanged();
        getFrObat();
    }
}
