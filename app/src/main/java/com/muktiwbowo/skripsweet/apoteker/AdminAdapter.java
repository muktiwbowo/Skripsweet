package com.muktiwbowo.skripsweet.apoteker;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.muktiwbowo.skripsweet.R;
import com.muktiwbowo.skripsweet.kasus.Kasus;

import java.util.List;

/**
 * Created by muktiwbowo on 29/10/17.
 */

public class AdminAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<Admin> listAdmin;

    public AdminAdapter(Activity activity, List<Admin> listAdmin) {
        this.activity = activity;
        this.listAdmin = listAdmin;
    }

    @Override
    public int getCount() {
        return listAdmin.size();
    }

    @Override
    public Object getItem(int position) {
        return listAdmin.get(position);
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
            convertView = inflater.inflate(R.layout.list_admin,null);
        }

        TextView idKasus = (TextView) convertView.findViewById(R.id.id_kasus);
        TextView kasusNama = (TextView) convertView.findViewById(R.id.kasus_nama);
        TextView penyakitNama = (TextView) convertView.findViewById(R.id.penyakit_nama);
        TextView namaPasien = (TextView) convertView.findViewById(R.id.nama_pasien);
        Admin a = listAdmin.get(position);
        idKasus.setText(a.getKasusId());
        kasusNama.setText(a.getKasusNama());
        penyakitNama.setText(a.getPenyakitNama());
        namaPasien.setText(a.getPasienNama());
        return convertView;
    }
}
