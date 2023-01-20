package com.anggastudio.sample;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class SerafinFragment extends DialogFragment {

    Button cerraraserafin, imprimirserafin;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_serafin, container, false);

        cerraraserafin   = view.findViewById(R.id.btncancelarserafin);
        imprimirserafin  = view.findViewById(R.id.btnagregarserafin);
        cerraraserafin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        imprimirserafin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "SE IMPRIMIO SERAFIN", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
        return view;
    }
}