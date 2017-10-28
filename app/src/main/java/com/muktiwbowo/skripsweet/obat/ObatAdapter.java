package com.muktiwbowo.skripsweet.obat;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.muktiwbowo.skripsweet.R;

import java.util.List;

/**
 * Created by muktiwbowo on 29/08/17.
 */

public class ObatAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    List<Obat> listObat;

    public ObatAdapter(Activity activity, List<Obat> listObat) {
        this.activity = activity;
        this.listObat = listObat;
    }

    @Override
    public int getCount() {
        return listObat.size();
    }

    @Override
    public Object getItem(int position) {
        return listObat.get(position);
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
            convertView = inflater.inflate(R.layout.list_obat_item,null);
        }
        TextView namaObat = (TextView) convertView.findViewById(R.id.txt_obat);
        Obat o = listObat.get(position);
        namaObat.setText(o.getfObat());
        return convertView;
    }
}
