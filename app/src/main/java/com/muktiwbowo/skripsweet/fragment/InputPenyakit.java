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
import com.android.volley.toolbox.RequestFuture;
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
public class InputPenyakit extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private List<InPenyakit> pList =new ArrayList<InPenyakit>();
    private ListView listViewp;
    private InPenyakitAdapter adapterPenyakit;
    RequestQueue reqPenyakit, reqInpPenyakit;
    private EditText frPenyakit;
    FloatingActionButton fabp;
    SwipeRefreshLayout refreshPenyakit;

    public InputPenyakit() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_input_penyakit, container, false);
        reqPenyakit = Volley.newRequestQueue(getActivity());
        reqInpPenyakit = Volley.newRequestQueue(getActivity());
        listViewp = (ListView) v.findViewById(R.id.listp);
        fabp = (FloatingActionButton) v.findViewById(R.id.fabp);
        refreshPenyakit = (SwipeRefreshLayout) v.findViewById(R.id.refreshp);
        adapterPenyakit = new InPenyakitAdapter(getActivity(), pList);
        listViewp.setAdapter(adapterPenyakit);
        refreshPenyakit.setOnRefreshListener(this);
        refreshPenyakit.post(new Runnable() {
            @Override
            public void run() {
                refreshPenyakit.setRefreshing(true);
                getFrPenyakit();
            }
        });
        getActivity().setTitle("Penyakit");
        fabp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFormPenyakit();
            }
        });
        //getFrPenyakit();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void showFormPenyakit() {
        final AlertDialog.Builder alertB = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.form_penyakit, null);
        alertB.setView(dialogView);
        frPenyakit = (EditText) dialogView.findViewById(R.id.fr_penyakit);
        alertB.setTitle("Input Penyakit");
        alertB.setPositiveButton("Kirim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                inputPenyakit();
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

    private void inputPenyakit() {
        StringRequest request = new StringRequest(Request.Method.POST, Url.insertPenyakit, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    String hasil = object.getString("hasil");
                    if (hasil.equals("sukses")){
                        Toast.makeText(getActivity(), object.getString("pesan"), Toast.LENGTH_SHORT).show();
                        onRefresh();
                    }else {
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
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("nama_penyakit", frPenyakit.getText().toString());
                return params;
            }
        };
        reqInpPenyakit.add(request);
    }

    private void getFrPenyakit() {
        refreshPenyakit.setRefreshing(true);
        JsonObjectRequest objectPenyakit = new JsonObjectRequest(Request.Method.GET, Url.urlPenyakit,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response.length() > 0) {
                    try {
                        JSONArray jsonArray = response.getJSONArray("penyakit");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            InPenyakit ip = new InPenyakit();
                            ip.setIdPenyakit(object.getString("kode_penyakit"));
                            ip.setNamaPenyakit(object.getString("nama_penyakit"));
                            pList.add(ip);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    adapterPenyakit.notifyDataSetChanged();
                }
                refreshPenyakit.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                refreshPenyakit.setRefreshing(false);
            }
        });
        reqPenyakit.add(objectPenyakit);
    }

    @Override
    public void onRefresh() {
        pList.clear();
        adapterPenyakit.notifyDataSetChanged();
        getFrPenyakit();
    }
}
