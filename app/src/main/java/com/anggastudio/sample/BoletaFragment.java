package com.anggastudio.sample;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.text.AllCapsTransformationMethod;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;


public class BoletaFragment extends DialogFragment {

    Button cerrarboleta,tipoPago,agregarboleta,generar,buscarplaca,buscardni;
    TextInputEditText placa,dni,nombre,direccion;
    TextInputLayout alertplaca,alertdni, alertnombre, alertdireccion;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_boleta, container, false);

        cerrarboleta  = view.findViewById(R.id.btncancelarboleta);
        tipoPago      = view.findViewById(R.id.btntipopago);
        agregarboleta = view.findViewById(R.id.btnagregarboleta);
        generar       = view.findViewById(R.id.btngenerarcliente);
        buscarplaca   = view.findViewById(R.id.btntarjeta);
        buscardni     = view.findViewById(R.id.btnrenic);



        placa     = view.findViewById(R.id.inputnplaca);
        dni       = view.findViewById(R.id.inputdni);
        nombre    = view.findViewById(R.id.inputnombre);
        direccion = view.findViewById(R.id.inputdireccion);

        alertplaca     = view.findViewById(R.id.textnplaca);
        alertdni       = view.findViewById(R.id.textdni);
        alertnombre    = view.findViewById(R.id.textnombre);
        alertdireccion = view.findViewById(R.id.textdireccion);


        cerrarboleta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        buscarplaca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String textnplaca    = placa.getText().toString().trim();
                if (textnplaca.isEmpty()){
                    alertplaca.setError("Ingresar placa");
                }else {
                    dni.setText("75994784");
                    nombre.setText("JHON PINO");
                    direccion.setText("JR. UNIÓN");
                    alertplaca.setErrorEnabled(false);
                    Toast.makeText(getContext(), "SE AGREGO ", Toast.LENGTH_SHORT).show();
                }
            }
        });
        buscardni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String textdni    = dni.getText().toString().trim();
                if (textdni.isEmpty()){
                    alertdni.setError("Ingresar DNI");
                }else {
                    nombre.setText("JHON PINO");
                    direccion.setText("JR. UNIÓN");
                    alertdni.setErrorEnabled(false);
                    Toast.makeText(getContext(), "SE AGREGO ", Toast.LENGTH_SHORT).show();
                }
            }
        });
        tipoPago.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TipoPagoFragment tipoPagoFragment = new TipoPagoFragment();
                tipoPagoFragment.show(getFragmentManager(), "Tipo de Pago");
                tipoPagoFragment.setCancelable(false);
            }
        });
        agregarboleta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textnplaca    = placa.getText().toString().trim();
                String textdni       = dni.getText().toString().trim();
                String textnombre    = nombre.getText().toString().trim();
                String textdireccion = direccion.getText().toString().trim();

                if (textnplaca.isEmpty()){
                    alertplaca.setError("Ingresar placa");
                }else {
                    alertplaca.setErrorEnabled(false);
                }
                if (textdni.isEmpty()){
                    alertdni.setError("Ingresar DNI");
                }else {
                    alertdni.setErrorEnabled(false);
                }
                if (textnombre.isEmpty()){
                    alertnombre.setError("Ingresar nombre");
                }else {
                    alertnombre.setErrorEnabled(false);
                }
                if (textdireccion.isEmpty()){
                    alertdireccion.setError("Ingresar dirección");
                }else {
                    alertdireccion.setErrorEnabled(false);
                    Toast.makeText(getContext(), "SE AGREGO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                    dismiss();
                }
            }
        });
        generar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placa.setText("7845D78D");
                dni.setText("75994784");
                nombre.setText("JHON PINO");
                direccion.setText("JR. UNIÓN");
            }
        });

        return view;
    }

}