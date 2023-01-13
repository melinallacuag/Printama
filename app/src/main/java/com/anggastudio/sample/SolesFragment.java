package com.anggastudio.sample;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.BreakIterator;


public class SolesFragment extends DialogFragment {

    EditText edsol;
    Custom_DialogInterface dialogInterface;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_soles, container, false);

        Button btncancelar = view.findViewById(R.id.btncancelarsoles);
        Button agregar = view.findViewById(R.id.btnagregarsoles);
        EditText edsol = view.findViewById(R.id.inputmotosoles);
        btncancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();
            }
        });
        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textsol = edsol.getText().toString();
                dialogInterface.applyTexts(textsol);
                dismiss();
            }
        });

        return view;
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        dialogInterface = (Custom_DialogInterface) context;
    }

    public  interface  Custom_DialogInterface{
        void applyTexts(String textsol);
    }


}