package com.anggastudio.sample;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class FacturaFragment extends DialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_factura, container, false);

        Button closeButton = view.findViewById(R.id.btncancelarfacturacion);
        Button tipoPago = view.findViewById(R.id.btntipopago);
        Button guardar = view.findViewById(R.id.btnagregarfacturacion);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        tipoPago.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TipoPagoFragment dialog = new TipoPagoFragment();
                dialog.show(getFragmentManager(), "Tipo de Pago");
            }
        });
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();

            }
        });

        return view;
    }
}