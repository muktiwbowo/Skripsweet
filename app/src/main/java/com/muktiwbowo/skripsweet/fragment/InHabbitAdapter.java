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
 * Created by muktiwbowo on 19/12/17.
 */

public class InHabbitAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<InHabbit> listHabbit;

    public InHabbitAdapter(Activity activity, List<InHabbit> listHabbit) {
        this.activity = activity;
        this.listHabbit = listHabbit;
    }

    @Override
    public int getCount() {
        return listHabbit.size();
    }

    @Override
    public Object getItem(int position) {
        return listHabbit.get(position);
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
            convertView = inflater.inflate(R.layout.list_habbit, null);
        }
        TextView idHabbit = (TextView) convertView.findViewById(R.id.id_habbit);
        TextView jenisHabiit = (TextView) convertView.findViewById(R.id.txt_habbit);
        InHabbit ih = listHabbit.get(position);
        idHabbit.setText(ih.getIdHabbit());
        jenisHabiit.setText(ih.getJenisHabbit());
        return convertView;
    }
}
