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
import androidx.cardview.widget.CardView;
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
    TextInputEditText placa,dni,nombre,direccion,boleta;
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

        tipoPago.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TipoPagoFragment tipoPagoFragment = new TipoPagoFragment();
                tipoPagoFragment.show(getFragmentManager(), "Tipo de Pago");
                tipoPagoFragment.setCancelable(false);
            }
        });
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
                if(textnplaca.isEmpty()){
                    alertplaca.setError("El campo placa es obligatorio");
                }else if(!textnplaca.equals("000-0000")){
                    alertplaca.setError("El dato no es correcto");
                }else {
                    alertplaca.setErrorEnabled(false);
                    dni.setText("11111111");
                    nombre.setText("CLIENTE VARIOS");
                    Toast.makeText(getContext(), "SE AGREGO DATO ", Toast.LENGTH_SHORT).show();
                }
            }
        });
        buscardni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String textdni    = dni.getText().toString().trim();
                if(textdni.isEmpty()){
                    alertplaca.setError("El campo dni es obligatorio");
                }else if(!textdni.equals("000-0000")){
                    alertplaca.setError("El dato no es correcto");
                }else{
                    alertdni.setErrorEnabled(false);
                    nombre.setText("CLIENTE VARIOS");
                    Toast.makeText(getContext(), "SE AGREGO DATO", Toast.LENGTH_SHORT).show();
                }
            }
        });
        generar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placa.setText("000-0000");
                dni.setText("11111111");
                nombre.setText("CLIENTE VARIOS");
            }
        });
        agregarboleta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView txtoperacion = (TextView) getActivity().findViewById(R.id.txtoperacion);
                String bd = "03";
                txtoperacion.setText(bd.toString());

                String textnplaca    = placa.getText().toString();
                String textdni       = dni.getText().toString();
                String textnombre    = nombre.getText().toString();
                String textdireccion = direccion.getText().toString();

                if (textnplaca.isEmpty()){
                    alertplaca.setError("El campo placa es obligatorio");
                }else if (textdni.isEmpty()){
                    alertdni.setError("El campo DNI es obligatorio");
                }else if (textnombre.isEmpty()){
                    alertnombre.setError("El campo nombre es obligatorio");
                }else {
                    alertplaca.setErrorEnabled(false);
                    alertdni.setErrorEnabled(false);
                    alertnombre.setErrorEnabled(false);
                    Toast.makeText(getContext(), "SE GUARDO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                    dismiss();
                }
            }
        });

        return view;
    }
}