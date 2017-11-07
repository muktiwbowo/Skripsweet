package com.muktiwbowo.skripsweet.fragment;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.muktiwbowo.skripsweet.R;

import java.util.List;

/**
 * Created by muktiwbowo on 06/11/17.
 */

public class InGejalaAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<InGejala> listGejala;

    public InGejalaAdapter(Activity activity, List<InGejala> listGejala) {
        this.activity = activity;
        this.listGejala = listGejala;
    }

    @Override
    public int getCount() {
        return listGejala.size();
    }

    @Override
    public Object getItem(int position) {
        return listGejala.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null){
            inflater = (LayoutInflater) activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView == null){
            convertView = inflater.inflate(R.layout.list_gejala,null);
        }
        TextView idGejala = (TextView) convertView.findViewById(R.id.id_gejala);
        TextView namaGejala = (TextView) convertView.findViewById(R.id.txt_gejala);
        InGejala ig = listGejala.get(position);

        idGejala.setText(ig.getIdGejala());
        namaGejala.setText(ig.getNamaGejala());

        return convertView;
    }
}
