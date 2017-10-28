package com.muktiwbowo.skripsweet.kasus;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.muktiwbowo.skripsweet.R;

import java.util.List;

/**
 * Created by muktiwbowo on 27/10/17.
 */

public class KasusAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Kasus> dataList;

    public KasusAdapter(Activity activity, List<Kasus> dataList) {
        this.activity = activity;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null){
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView == null){
            convertView = inflater.inflate(R.layout.list_kasus,null);
        }

        TextView namaKasus = (TextView) convertView.findViewById(R.id.txt_kasus);
        TextView namaPenyakit = (TextView) convertView.findViewById(R.id.txt_penyakit);
        Kasus k = dataList.get(position);
        namaKasus.setText(k.getNamaKasus());
        namaPenyakit.setText(k.getNamaPenyakit());
        return convertView;
    }
}
