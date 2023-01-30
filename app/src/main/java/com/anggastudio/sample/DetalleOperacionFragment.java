package com.anggastudio.sample;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.anggastudio.printama.Printama;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;


public class DetalleOperacionFragment extends Fragment {
    TextView importe, cliente, operacion,textmanguera,lado;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detalle_operacion, container, false);
        lado  =  view.findViewById(R.id.ladodd);
        //Fecha y Hora
        Calendar cal          = Calendar.getInstance(TimeZone.getTimeZone("America/Lima"));
        SimpleDateFormat sdf  = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        String FechaHora      = sdf.format(cal.getTime());
        final TextView hora = (TextView) view.findViewById(R.id.fecha);
        hora.setText(FechaHora);


        //Cara
    /*   TextView textsol = (TextView) getActivity().findViewById(R.id.textcara);
        textmanguera = (TextView) view.findViewById(R.id.lado);
        textmanguera.setText(textsol.getText().toString());*/


        //importe  =  view.findViewById(R.id.importe);
        view.findViewById(R.id.btneditar).setOnClickListener(v -> detalle());
        return view;
    }

    private void detalle() {
        View view = getView().findViewById(R.id.detalle);
        Printama.with(getContext()).connect(printama -> {
            printama.printFromView(view);
            new Handler().postDelayed(printama::close, 2000);
        }, this::showToast);
    }
    private void showToast(String message) {
        Toast.makeText(getContext(), "Conectar Bluetooth", Toast.LENGTH_SHORT).show();
    }
}