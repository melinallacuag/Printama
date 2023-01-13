package com.anggastudio.sample;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class TipoPagoFragment extends DialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tipo_pago, container, false);
        Button btncancelar = view.findViewById(R.id.btncancelartipopago);
        Button efectivo = view.findViewById(R.id.radioEfectivo);
        Button tarjeta = view.findViewById(R.id.radioTarjeta);
        Button credito = view.findViewById(R.id.radioCredito);
        btncancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        efectivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PagoEfectivoFragment dialog = new PagoEfectivoFragment();
                dialog.show(getFragmentManager(), "Tipo de Pago efectivo");
            }
        });
        credito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PagoCreditoFragment dialog = new PagoCreditoFragment();
                dialog.show(getFragmentManager(), "Tipo de Pago");
            }
        });
        tarjeta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PagoTarjetaFragment dialog = new PagoTarjetaFragment();
                dialog.show(getFragmentManager(), "Tipo de Pago");
            }
        });

        return view;
    }
}