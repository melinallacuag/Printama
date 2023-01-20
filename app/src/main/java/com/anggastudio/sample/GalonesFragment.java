package com.anggastudio.sample;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;


public class GalonesFragment extends DialogFragment {

    Button btncancelar,agregargalones;
    TextInputEditText galones;
    TextInputLayout alertgalones;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_galones, container, false);

        btncancelar    = view.findViewById(R.id.btncancelargalones);
        agregargalones = view.findViewById(R.id.btnagregargalones);
        galones        = view.findViewById(R.id.inputmontogalones);
        alertgalones   = view.findViewById(R.id.textgalones);

        btncancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        agregargalones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textgalones = galones.getText().toString().trim();
                if (textgalones.isEmpty()){
                    alertgalones.setError("Ingresar galones");
                }else {
                    alertgalones.setErrorEnabled(false);
                    Toast.makeText(getContext(), "SE AGREGO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                    dismiss();
                }
            }
        });
        return view;
    }
}