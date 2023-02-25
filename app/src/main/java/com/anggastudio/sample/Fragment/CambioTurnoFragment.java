package com.anggastudio.sample.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.anggastudio.sample.Login;
import com.anggastudio.sample.R;

public class CambioTurnoFragment extends DialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cambio_turno, container, false);

        Button btncancelar = view.findViewById(R.id.btncancelarcambioturno);
        Button btncambio   = view.findViewById(R.id.btnagregarcambioturno);

        btncancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "SE CANCELO", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });

        btncambio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Toast.makeText(getContext(), "SE GENERO EL CAMBIO DE TURNO", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), Login.class);
                    startActivity(intent);
                    finalize();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });

        return view;
    }
}