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
        buscarcliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textidcliente    = idcliente.getText().toString().trim();
                if(textidcliente.isEmpty()){
                    alertidcliente.setError("El campo placa es obligatorio");
                }else if(!textidcliente.equals("11111111")){
                    alertidcliente.setError("El dato no es correcto");
                }else {
                    alertidcliente.setErrorEnabled(false);
                    ruc.setText("111111111111");
                    nombre.setText("CLIENTE VARIOS");
                    direccion.setText("JR. UNIÓN");
                    ClientesFragment dialog = new ClientesFragment();
                    dialog.show(getFragmentManager(), "Buscar Cliente");
                }
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
                    kilometraje.setText("0000000000");
                    idcliente.setText("11111111");
                    ruc.setText("111111111111");
                    nombre.setText("CLIENTE VARIOS");
                    direccion.setText("JR. UNIÓN");
                    Toast.makeText(getContext(), "SE AGREGO DATO", Toast.LENGTH_SHORT).show();
                }
            }
        });
        agregardespacho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView txtoperacion = (TextView) getActivity().findViewById(R.id.txtoperacion);
                String bd = "99";
                txtoperacion.setText(bd.toString());
                String textnplaca      = placa.getText().toString().trim();
                String textkilometraje = kilometraje.getText().toString().trim();
                String textidcliente   = idcliente.getText().toString().trim();
                String textruc         = ruc.getText().toString().trim();
                String textrazonsocial = nombre.getText().toString().trim();
                String textdireccion   = direccion.getText().toString().trim();
                String textobservacion = observacion.getText().toString().trim();

                if (textnplaca.isEmpty()){
                    alertplaca.setError("El campo placa es obligatorio");
                }else if (textkilometraje.isEmpty()){
                    alertkilometraje.setError("El campo kilometraje es obligatorio");
                }else if (textidcliente.isEmpty()){
                    alertidcliente.setError("El campo Id Cliente es obligatorio");
                }else if (textruc.isEmpty()){
                    alertruc.setError("El campo RUC es obligatorio");
                }else if (textrazonsocial.isEmpty()){
                    alertnombre.setError("El campo Razon Social es obligatorio");
                }else if (textdireccion.isEmpty()){
                    alertdireccion.setError("El campo dirección es obligatorio");
                }else if (textobservacion.isEmpty()){
                    alertobservacion.setError("El campo observación es obligatorio");
                }else{
                    alertplaca.setErrorEnabled(false);
                    alertkilometraje.setErrorEnabled(false);
                    alertidcliente.setErrorEnabled(false);
                    alertruc.setErrorEnabled(false);
                    alertnombre.setErrorEnabled(false);
                    alertdireccion.setErrorEnabled(false);
                    alertobservacion.setErrorEnabled(false);
                    Toast.makeText(getContext(), "SE AGREGO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                    dismiss();
                }
            }
        });
        return view;
    }
}