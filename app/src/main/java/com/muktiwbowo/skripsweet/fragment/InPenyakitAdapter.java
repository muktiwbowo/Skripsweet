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

public class InPenyakitAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<InPenyakit> pList;

    public InPenyakitAdapter(Activity activity, List<InPenyakit> pList) {
        this.activity = activity;
        this.pList = pList;
    }

    @Override
    public int getCount() {
        return pList.size();
    }

    @Override
    public Object getItem(int position) {
        return pList.get(position);
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
            convertView = inflater.inflate(R.layout.list_penyakit, null);
        }
        TextView idPenyakit = (TextView) convertView.findViewById(R.id.id_penyakit);
        TextView namaPenyakit = (TextView) convertView.findViewById(R.id.txt_penyakit);
        InPenyakit ip = pList.get(position);
        idPenyakit.setText(ip.getIdPenyakit());
        namaPenyakit.setText(ip.getNamaPenyakit());
        return convertView;
    }
}
