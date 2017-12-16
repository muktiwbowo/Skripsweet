package com.muktiwbowo.skripsweet.fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
public class InputGejala extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private List<InGejala> gList = new ArrayList<InGejala>();
    private InGejalaAdapter inGejalaAdapter;
    RequestQueue gRequestQueue, inGejalaReq;
    ListView listView;
    FloatingActionButton fab;
    public EditText frGejala;
    SwipeRefreshLayout refreshGejala;

    public InputGejala() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_input_gejala, container, false);
        gRequestQueue = Volley.newRequestQueue(getActivity());
        inGejalaReq = Volley.newRequestQueue(getActivity());
        listView = (ListView) v.findViewById(R.id.listg);
        refreshGejala = (SwipeRefreshLayout) v.findViewById(R.id.refreshg);
        inGejalaAdapter = new InGejalaAdapter(getActivity(), gList);
        listView.setAdapter(inGejalaAdapter);
        refreshGejala.setOnRefreshListener(this);
        refreshGejala.post(new Runnable() {
            @Override
            public void run() {
                refreshGejala.setRefreshing(true);
                getGejala();
            }
        });
        getActivity().setTitle("Gejala");

        fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFormGejala();
            }
        });
        //getGejala();
        return v;
    }

    private void showFormGejala() {
        final AlertDialog.Builder alertB = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.form_gejala, null);
        alertB.setView(dialogView);
        frGejala = (EditText) dialogView.findViewById(R.id.fr_gejala);
        alertB.setTitle("Input Gejala");
        alertB.setPositiveButton("Kirim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                inputGejala();
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

    private void getGejala() {
        refreshGejala.setRefreshing(true);
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Url.urlGejala,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response.length() > 0) {
                    try {
                        JSONArray jsonArray = response.getJSONArray("gejala");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            InGejala ig = new InGejala();
                            ig.setIdGejala(object.getString("id_gejala"));
                            ig.setNamaGejala(object.getString("nama_gejala"));
                            gList.add(ig);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    inGejalaAdapter.notifyDataSetChanged();
                }
                refreshGejala.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                refreshGejala.setRefreshing(false);
            }
        });
        gRequestQueue.add(jsonObjectRequest);
    }

    private void inputGejala() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url.insertGejala, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    String hasil = object.getString("hasil");
                    if (hasil.equals("sukses")) {
                        Toast.makeText(getActivity(), object.getString("pesan"), Toast.LENGTH_LONG).show();
                        onRefresh();
                    } else {
                        Toast.makeText(getActivity(), object.getString("pesan"), Toast.LENGTH_LONG).show();
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
                params.put("nama_gejala", frGejala.getText().toString());
                return params;
            }
        };
        inGejalaReq.add(stringRequest);
    }

    @Override
    public void onRefresh() {
        gList.clear();
        inGejalaAdapter.notifyDataSetChanged();
        getGejala();
    }
}
