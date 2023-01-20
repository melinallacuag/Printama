package com.anggastudio.sample;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;


public class NotaDespachoFragment extends DialogFragment {

    Button cerrardespacho,agregardespacho,buscarcliente,buscarplaca;
    TextInputEditText placa,kilometraje,idcliente,ruc,nombre,direccion,observacion;
    TextInputLayout alertplaca,alertkilometraje,alertidcliente,alertruc, alertnombre, alertdireccion, alertobservacion;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nota_despacho, container, false);

        cerrardespacho  = view.findViewById(R.id.btncancelarndespacho);
        agregardespacho = view.findViewById(R.id.btnagregarndespacho);
        buscarcliente   = view.findViewById(R.id.btnbuscarcliente);
        buscarplaca   = view.findViewById(R.id.btntarjeta);

        placa        = view.findViewById(R.id.inputnplaca);
        kilometraje  = view.findViewById(R.id.inputkilometraje);
        idcliente    = view.findViewById(R.id.inputidcliente);
        ruc          = view.findViewById(R.id.inputruc);
        nombre       = view.findViewById(R.id.inputrazonsocial);
        direccion    = view.findViewById(R.id.inputdireccion);
        observacion  = view.findViewById(R.id.inputobservacion);

        alertplaca       = view.findViewById(R.id.textnplaca);
        alertkilometraje = view.findViewById(R.id.textkilometraje);
        alertidcliente   = view.findViewById(R.id.textidcliente);
        alertruc         = view.findViewById(R.id.textruc);
        alertnombre      = view.findViewById(R.id.textrazonsocial);
        alertdireccion   = view.findViewById(R.id.textdireccion);
        alertobservacion = view.findViewById(R.id.textobservacion);

        cerrardespacho.setOnClickListener(new View.OnClickListener() {
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
                    alertplaca.setErrorEnabled(false);
                    Toast.makeText(getContext(), "SE AGREGO ", Toast.LENGTH_SHORT).show();
                    dismiss();
                }
            }
        });
        agregardespacho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textnplaca      = placa.getText().toString().trim();
                String textkilometraje = kilometraje.getText().toString().trim();
                String textidcliente   = idcliente.getText().toString().trim();
                String textruc         = ruc.getText().toString().trim();
                String textrazonsocial = nombre.getText().toString().trim();
                String textdireccion   = direccion.getText().toString().trim();
                String textobservacion = observacion.getText().toString().trim();

                if (textnplaca.isEmpty()){
                    alertplaca.setError("Ingresar placa");
                }else{
                    alertplaca.setErrorEnabled(false);
                }
                if (textkilometraje.isEmpty()){
                    alertkilometraje.setError("Ingresar kilometraje");
                }else{
                    alertkilometraje.setErrorEnabled(false);
                }
                if (textidcliente.isEmpty()){
                    alertidcliente.setError("Ingresar cliente");
                }else{
                    alertidcliente.setErrorEnabled(false);
                }
                if (textruc.isEmpty()){
                    alertruc.setError("Ingresar ruc");
                }else{
                    alertruc.setErrorEnabled(false);
                }
                if (textrazonsocial.isEmpty()){
                    alertnombre.setError("Ingresar Razón Social");
                }else{
                    alertnombre.setErrorEnabled(false);
                }
                if (textdireccion.isEmpty()){
                    alertdireccion.setError("Ingresar Dirección");
                }else{
                    alertdireccion.setErrorEnabled(false);
                }
                if (textobservacion.isEmpty()){
                    alertobservacion.setError("Ingresar observación");
                }else{
                    alertobservacion.setErrorEnabled(false);
                    Toast.makeText(getContext(), "SE AGREGO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                    dismiss();
                }

            }
        });
        buscarcliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textidcliente    = idcliente.getText().toString().trim();
                if (textidcliente.isEmpty()){
                    alertidcliente.setError("Ingresar Cliente");
                }else {
                    alertidcliente.setErrorEnabled(false);
                    ClientesFragment dialog = new ClientesFragment();
                    dialog.show(getFragmentManager(), "Buscar Cliente");
                }


            }
        });

        return view;
    }
}