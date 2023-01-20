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


public class PagoTarjetaFragment extends DialogFragment {
    Button btncancelar,agregartarjeta;
    TextInputEditText soles;
    TextInputLayout alertsoles;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pago_tarjeta, container, false);

        btncancelar    = view.findViewById(R.id.btncancelarTajetatipo);
        agregartarjeta = view.findViewById(R.id.btnagregarTarjetatipo);

        soles        = view.findViewById(R.id.inputmontosoles);
        alertsoles   = view.findViewById(R.id.textsoles);

        btncancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        agregartarjeta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textsoles = soles.getText().toString().trim();
                if (textsoles.isEmpty()){
                    alertsoles.setError("Ingresar soles");
                }else {
                    alertsoles.setErrorEnabled(false);
                    Toast.makeText(getContext(), "SE AGREGO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                    dismiss();
                }
            }
        });

        return view;
    }
}