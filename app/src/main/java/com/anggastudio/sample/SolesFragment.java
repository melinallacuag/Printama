package com.anggastudio.sample;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputLayout;



public class SolesFragment extends DialogFragment {
    Button btncancelar, agregar;
    EditText montosoles;
    TextInputLayout alertsoles;

    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

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
                dialogInterface.applyTexts(textsol);
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


         //       dismiss();

            }
        });

        return view;
    }

    private  Custom_DialogInterface dialogInterface ;


    public interface Custom_DialogInterface {
        void applyTexts(String textsol);
    }
    @Override
    public void onDetach() {
        super.onDetach();
        dialogInterface = null;
    }


    @Override
    public void onAttach(@NonNull  Context context){
        super.onAttach(context);
        dialogInterface  = (Custom_DialogInterface)  getParentFragment();
    }

}