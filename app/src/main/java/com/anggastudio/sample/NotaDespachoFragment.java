package com.anggastudio.sample;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class NotaDespachoFragment extends DialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nota_despacho, container, false);

        Button closeButton = view.findViewById(R.id.btncancelarndespacho);
        Button guardar = view.findViewById(R.id.btnagregarndespacho);
        Button buscarcliente = view.findViewById(R.id.btnbuscarcliente);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();

            }
        });
        buscarcliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClientesFragment dialog = new ClientesFragment();
                dialog.show(getFragmentManager(), "Tipo de Pago efectivo");

            }
        });

        return view;
    }
}