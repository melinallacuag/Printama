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

public class FacturaFragment extends DialogFragment {

    Button cerrarfactura,tipoPago,agregarfactura,buscarplaca,buscarruc;
    TextInputEditText placa,ruc,nombre,direccion;
    TextInputLayout alertplaca,alertruc, alertnombre, alertdireccion;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_factura, container, false);

        cerrarfactura  = view.findViewById(R.id.btncancelarfacturacion);
        tipoPago       = view.findViewById(R.id.btntipopago);
        agregarfactura = view.findViewById(R.id.btnagregarfacturacion);
        buscarplaca    = view.findViewById(R.id.btntarjeta);
        buscarruc      = view.findViewById(R.id.btnsunat);

        placa     = view.findViewById(R.id.inputnplaca);
        ruc       = view.findViewById(R.id.inputruc);
        nombre    = view.findViewById(R.id.inputrazonsocial);
        direccion = view.findViewById(R.id.inputdireccion);

        alertplaca     = view.findViewById(R.id.textnplaca);
        alertruc       = view.findViewById(R.id.textruc);
        alertnombre    = view.findViewById(R.id.textrazonsocial);
        alertdireccion = view.findViewById(R.id.textdireccion);

        cerrarfactura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        buscarruc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String textruc    = ruc.getText().toString().trim();
                if (textruc.isEmpty()){
                    alertruc.setError("Ingresar RUC");
                }else{
                    nombre.setText("JHON PINO");
                    direccion.setText("JR. UNIÓN");
                    alertruc.setErrorEnabled(false);
                    Toast.makeText(getContext(), "SE AGREGO ", Toast.LENGTH_SHORT).show();
                }
            }
        });
        buscarplaca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String textnplaca    = placa.getText().toString().trim();
                if (textnplaca.isEmpty()){
                    alertplaca.setError("Ingresar placa");
                }else {
                    ruc.setText("104578141578");
                    nombre.setText("JHON PINO");
                    direccion.setText("JR. UNIÓN");
                    alertplaca.setErrorEnabled(false);
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
        agregarfactura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textnplaca      = placa.getText().toString().trim();
                String textruc         = ruc.getText().toString().trim();
                String textrazonsocial = nombre.getText().toString().trim();
                String textdireccion   = direccion.getText().toString().trim();

                if (textnplaca.isEmpty()){
                    alertplaca.setError("Ingresar placa");
                }else{
                    alertplaca.setErrorEnabled(false);
                }
                if (textruc.isEmpty()){
                    alertruc.setError("Ingresar RUC");
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
                    Toast.makeText(getContext(), "SE AGREGO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                    dismiss();
                }

            }
        });

        return view;
    }
}