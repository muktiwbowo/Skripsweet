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
 * Created by muktiwbowo on 07/11/17.
 */

public class InObatAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<InObat> obatList;

    public InObatAdapter(Activity activity, List<InObat> obatList) {
        this.activity = activity;
        this.obatList = obatList;
    }

    @Override
    public int getCount() {
        return obatList.size();
    }

    @Override
    public Object getItem(int position) {
        return obatList.get(position);
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
            convertView = inflater.inflate(R.layout.list_obat,null);
        }
        TextView kdObat = (TextView) convertView.findViewById(R.id.kdobat);
        TextView namaObat = (TextView) convertView.findViewById(R.id.txtobat);
        InObat io = obatList.get(position);
        kdObat.setText(io.getIdObat());
        namaObat.setText(io.getNamaObat());
        return convertView;
    }
}
