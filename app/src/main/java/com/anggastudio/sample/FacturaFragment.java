package com.anggastudio.sample;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
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
        tipoPago.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TipoPagoFragment tipoPagoFragment = new TipoPagoFragment();
                tipoPagoFragment.show(getFragmentManager(), "Tipo de Pago");
                tipoPagoFragment.setCancelable(false);
            }
        });
        buscarruc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String textruc    = ruc.getText().toString();
                if(textruc.isEmpty()){
                    alertruc.setError("El campo RUC es obligatorio");
                }else if(!textruc.equals("11111111111")){
                    alertruc.setError("El dato no es correcto");
                }else{
                    alertruc.setErrorEnabled(false);
                    nombre.setText("JHON PINO");
                    Toast.makeText(getContext(), "SE AGREGO DATO", Toast.LENGTH_SHORT).show();
                }
            }
        });
        buscarplaca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String textnplaca    = placa.getText().toString();
                if(textnplaca.isEmpty()){
                    alertplaca.setError("El campo placa es obligatorio");
                }else if(!textnplaca.equals("000-0000")){
                    alertplaca.setError("El dato no es correcto");
                }else {
                    alertplaca.setErrorEnabled(false);
                    ruc.setText("11111111111");
                    nombre.setText("JHON PINO");
                    Toast.makeText(getContext(), "SE AGREGO DATO", Toast.LENGTH_SHORT).show();
                }
            }
        });
        agregarfactura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView txtoperacion = (TextView) getActivity().findViewById(R.id.txtoperacion);
                txtoperacion.setText("01");

                String textnplaca      = placa.getText().toString();
                String textruc         = ruc.getText().toString();
                String textrazonsocial = nombre.getText().toString();
                String textdireccion   = direccion.getText().toString();

                if (textnplaca.isEmpty()){
                    alertplaca.setError("El campo placa es obligatorio");
                }else if (textruc.isEmpty()){
                    alertruc.setError("El campo RUC es obligatorio");
                }else if (textrazonsocial.isEmpty()){
                    alertnombre.setError("El campo nombre es obligatorio");
                }else{
                    alertplaca.setErrorEnabled(false);
                    alertruc.setErrorEnabled(false);
                    alertnombre.setErrorEnabled(false);
                    Toast.makeText(getContext(), "SE AGREGO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                    dismiss();
                }
            }
        });
        return view;
    }
}