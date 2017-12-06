package com.muktiwbowo.skripsweet.konsul;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.guna.libmultispinner.MultiSelectionSpinner;
import com.muktiwbowo.skripsweet.R;
import com.muktiwbowo.skripsweet.Url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KonsulActivity extends AppCompatActivity implements MultiSelectionSpinner.OnMultipleItemsSelectedListener {


    private static final String url = "https://dabudabu.000webhostapp.com/farnotifphp/ceksimilarity.php";
    private static final String urlObat = "https://dabudabu.000webhostapp.com/farnotifphp/getobat.php";
    private static final String urlGejala = "https://dabudabu.000webhostapp.com/farnotifphp/getgejala.php";
    RequestQueue requestQueue, requestQueueObat, requestQueueGejala;
    EditText edtPenyakit;
    Button btnCheck;
    private TextView temp;
    String tempgejala, tempidgejala;
    MultiSelectionSpinner spinnerGejala, spinnerObat;
    ArrayList<String> konsulObats, konsulGejala;
    JSONArray resultObat, resultGejala;
    List<KonsulGejala> namaNamaGejala;
    List<String> idGejalaDipilih;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konsul);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        requestQueue = Volley.newRequestQueue(this);
        requestQueueObat = Volley.newRequestQueue(this);
        requestQueueGejala = Volley.newRequestQueue(this);

        konsulObats = new ArrayList<String>();
        konsulGejala = new ArrayList<String>();

        temp = (TextView) findViewById(R.id.temp);
        spinnerGejala = (MultiSelectionSpinner) findViewById(R.id.spinerGejala);
        spinnerObat = (MultiSelectionSpinner) findViewById(R.id.spinerObat);
        edtPenyakit = (EditText) findViewById(R.id.edt_penyakit);
        btnCheck = (Button) findViewById(R.id.btn_check);
        getSpinnerObat();
        getSpinnerGejala();
        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showResult();
                //showDialog();;
            }
        });
    }

    public void showResult(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url.urlRekomendasi, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(KonsulActivity.this, response, Toast.LENGTH_SHORT).show();
                //Log.d("Response", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(KonsulActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
               /* for (int i=0; i<tempgejala.length(); i++){
                    params.put("id_gejala ["+i+"]", tempidgejala);
                }*/
                for(int i = 0;i<idGejalaDipilih.size();i++){
                    params.put("id_gejala["+i+"]",idGejalaDipilih.get(i));
                }
                //params.put("id_gejala", tempidgejala);
                //params.put("kode_obat", spinnerObat.getSelectedItemsAsString());
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void getSpinnerGejala() {
        StringRequest requestGejala = new StringRequest(urlGejala, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    resultGejala = object.getJSONArray("gejala");
                    getDataGejala(resultGejala);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueueGejala.add(requestGejala);
    }

    private void getDataGejala(JSONArray resultGejala) {
        namaNamaGejala = new ArrayList<>();
        for (int i=0; i<resultGejala.length(); i++){
            try {
                JSONObject obj = resultGejala.getJSONObject(i);
                konsulGejala.add(obj.getString("nama_gejala"));
                KonsulGejala gejala = new KonsulGejala();
                gejala.setIdGejala(obj.getString("id_gejala"));
                gejala.setNamaGejala(obj.getString("nama_gejala"));
                //konsulGejala.add(obj.getString("id_gejala"));
                namaNamaGejala.add(gejala);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        spinnerGejala.setItems(konsulGejala);
        spinnerGejala.setSelection(new int[]{0});
        spinnerGejala.setListener(this);
    }

    public void getSpinnerObat(){
        StringRequest requestObat = new StringRequest(urlObat, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                    //JSONObject jsonObject = null;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    resultObat = jsonObject.getJSONArray("obat");
                    getDataObat(resultObat);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueueObat.add(requestObat);
    }

    private void getDataObat(JSONArray resultObat) {
        for(int i=0; i< resultObat.length(); i++){
            try {
                JSONObject obj = resultObat.getJSONObject(i);
                konsulObats.add(obj.getString("nama_obat"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        spinnerObat.setItems(konsulObats);
        spinnerObat.setSelection(new int[]{0});
        spinnerObat.setListener(this);
    }



    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public void selectedIndices(List<Integer> indices) {
        //tempgejala = indices.toString();
        //temp.setText(tempgejala);

    }

    @Override
    public void selectedStrings(List<String> strings) {
        tempidgejala = strings.toString();
        idGejalaDipilih = new ArrayList<>();
        for(int i =0;i<namaNamaGejala.size();i++){
            for (String s : strings){
                if(s.equals(namaNamaGejala.get(i).getNamaGejala())){
                    idGejalaDipilih.add(namaNamaGejala.get(i).getIdGejala());
                    break;
                }
            }

        }
         //Toast.makeText(this, String.valueOf(strings.size()), Toast.LENGTH_SHORT).show();
        //temp.setText(tempidgejala);
    }

     private void showDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // Setting Dialog Title
        alertDialogBuilder.setTitle("Rekomendasi");

        // Setting Dialog Message
        alertDialogBuilder.setMessage("Kebiasaan membeli fenilbutason dihentikan, konsul ke dokter untuk mendapat obat dispepsia atau memberi obat dispepsia yang termasuk OWA misal omeprazol atau famotidin, memberi informasi cara minum antasid dan obat dispepsia yang benar");

        // Setting Icon to Dialog

        // Setting OK Button

        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        // Showing Alert Message
        alertDialog.show();
    }
}