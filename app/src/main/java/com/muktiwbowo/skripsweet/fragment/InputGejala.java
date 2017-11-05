package com.muktiwbowo.skripsweet.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.muktiwbowo.skripsweet.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class InputGejala extends Fragment {


    public InputGejala() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_input_gejala, container, false);
    }

}
