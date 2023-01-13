package com.anggastudio.sample;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class BoletaFragment extends DialogFragment {


    private static android.content.Context Context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_boleta, container, false);

        Button closeButton = view.findViewById(R.id.btncancelarboleta);
        Button tipoPago = view.findViewById(R.id.btntipopago);
        Button guardar = view.findViewById(R.id.btnagregarboleta);
        Button generador = view.findViewById(R.id.btngenerarcliente);

        TextView placa = view.findViewById(R.id.inputnplaca);
        TextView dni = view.findViewById(R.id.inputdni);
        TextView nombre = view.findViewById(R.id.inputnombre);
        TextView direccion = view.findViewById(R.id.inputdireccion);
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
        generador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placa.setText("7845d78d");
                nombre.setText("75994784");
                nombre.setText("Jhon Pino");
                nombre.setText("Jr. Unión");
            }
        });



        return view;
    }

}