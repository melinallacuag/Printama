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
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.BreakIterator;


public class SolesFragment extends DialogFragment {
    Button btncancelar, agregar;
    EditText montosoles;
    TextInputLayout alertsoles;
    Custom_DialogInterface dialogInterface;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_soles, container, false);

        btncancelar   = view.findViewById(R.id.btncancelarsoles);
        agregar       = view.findViewById(R.id.btnagregarsoles);
        montosoles    = view.findViewById(R.id.montosoles);
        alertsoles    = view.findViewById(R.id.textsoles);

        btncancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { dismiss();}
        });

        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textsol = montosoles.getText().toString().trim();
                int numsol   = Integer.parseInt(textsol);
                if (numsol < 5){
                    alertsoles.setError("El valor debe ser minimo 5 ");
                }else if(9999 < numsol){
                    alertsoles.setError("El valor debe ser maximo 9999");
                }else {
                    alertsoles.setErrorEnabled(false);
                    Toast.makeText(getContext(), "SE AGREGO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                    dismiss();
                }
               // if (!textsol.equals("")){
                //    dialogInterface.applyTexts(textsol);
                //}

            }
        });

        return view;
    }
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        dialogInterface  = (Custom_DialogInterface)  getTargetFragment();
    }

    public interface Custom_DialogInterface {
        void applyTexts(String textsol);
    }
}