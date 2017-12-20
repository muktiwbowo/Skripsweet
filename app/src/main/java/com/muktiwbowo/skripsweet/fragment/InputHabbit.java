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
public class InputHabbit extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private List<InHabbit> hList = new ArrayList<InHabbit>();
    private InHabbitAdapter inHabbitAdapter;
    RequestQueue hRequestQueue, inHabbitReq;
    ListView listView;
    FloatingActionButton fab;
    public EditText frHabbit;
    SwipeRefreshLayout refreshHabbit;

    public InputHabbit() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_input_habbit, container, false);
        getActivity().setTitle("Habbit");
        hRequestQueue = Volley.newRequestQueue(getActivity());
        inHabbitReq = Volley.newRequestQueue(getActivity());
        listView = (ListView) v.findViewById(R.id.listh);
        fab = (FloatingActionButton) v.findViewById(R.id.fabh);
        refreshHabbit = (SwipeRefreshLayout) v.findViewById(R.id.refreshh);
        inHabbitAdapter= new InHabbitAdapter(getActivity(), hList);
        listView.setAdapter(inHabbitAdapter);
        refreshHabbit.setOnRefreshListener(this);
        refreshHabbit.post(new Runnable() {
            @Override
            public void run() {
                refreshHabbit.setRefreshing(true);
                getHabbit();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFormHabbit();
            }
        });
        return v;
    }

    private void getHabbit() {
        refreshHabbit.setRefreshing(true);
        StringRequest requestHabbit = new StringRequest(Request.Method.GET, Url.urlHabbit, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.length() > 0) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("habbit");
                        for (int i=0; i<jsonArray.length(); i++){
                            JSONObject obj = jsonArray.getJSONObject(i);
                            InHabbit ih = new InHabbit();
                            ih.setIdHabbit(obj.getString("id_habbit"));
                            ih.setJenisHabbit(obj.getString("jenis_habbit"));
                            hList.add(ih);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    inHabbitAdapter.notifyDataSetChanged();
                }
                refreshHabbit.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
                refreshHabbit.setRefreshing(false);
            }
        });
        hRequestQueue.add(requestHabbit);
    }

    private void showFormHabbit() {
        final AlertDialog.Builder alertB = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.form_habbit, null);
        alertB.setView(dialogView);
        frHabbit = (EditText) dialogView.findViewById(R.id.fr_habbit);
        alertB.setTitle("Input Habbit");
        alertB.setPositiveButton("Kirim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                inputHabbit();
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

    private void inputHabbit() {
        StringRequest inHabbit = new StringRequest(Request.Method.POST, Url.insertHabbit, new Response.Listener<String>() {
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
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("jenis_habbit",frHabbit.getText().toString());
                return params;
            }
        };
        inHabbitReq.add(inHabbit);
    }

    @Override
    public void onRefresh() {
        hList.clear();
        inHabbitAdapter.notifyDataSetChanged();
        getHabbit();
    }
}
