package com.anggastudio.sample;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class VentaFragment extends Fragment  implements SolesFragment.Custom_DialogInterface{
    TextView totalmonto;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_venta, container, false);

        totalmonto =  view.findViewById(R.id.txtimporte);

        //cara
        CardView Cara17         = (CardView) view.findViewById(R.id.cara17);
        CardView Cara18         = (CardView) view.findViewById(R.id.cara18);
        final TextView textcara = (TextView) view.findViewById(R.id.textcara);
        Cara17.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textcara.setText("17");
            }
        });
        Cara18.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textcara.setText("18");
            }
        });

        //manguera
        CardView diesel = (CardView) view.findViewById(R.id.diesel);
        CardView gas90  = (CardView) view.findViewById(R.id.gas90);
        CardView gas95  = (CardView) view.findViewById(R.id.gas95);
        CardView gas97  = (CardView) view.findViewById(R.id.gas97);
        final TextView textmanguera = (TextView) view.findViewById(R.id.textmanguera);

        diesel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textmanguera.setText("DIESEL");
            }
        });
        gas90.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textmanguera.setText("GAS 90");
            }
        });
        gas95.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textmanguera.setText("GAS 95");
            }
        });
        gas97.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textmanguera.setText("GAS 97");
            }
        });



        Button btnlibre = view.findViewById(R.id.btnlibre);
        Button btnsoles = view.findViewById(R.id.btnsoles);
        Button btngalones = view.findViewById(R.id.btngalones);
        Button btnfactura = view.findViewById(R.id.btnfactura);
        Button btnnotadespacho = view.findViewById(R.id.btnnotadespacho);
        Button btnserafin = view.findViewById(R.id.btnserafin);
        Button btnpuntos = view.findViewById(R.id.btnpuntos);
        btnlibre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LibreFragment libreFragment = new LibreFragment();
                libreFragment.show(getActivity().getSupportFragmentManager(), "Libre");

            }
        });
        btnsoles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SolesFragment solesFragment = new SolesFragment();
                solesFragment.show(getActivity().getSupportFragmentManager(), "Soles");

            }
        });
        btngalones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GalonesFragment galonesFragment = new GalonesFragment();
                galonesFragment.show(getActivity().getSupportFragmentManager(), "Galones");

            }
        });
        btnfactura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FacturaFragment facturaFragment = new FacturaFragment();
                facturaFragment.show(getActivity().getSupportFragmentManager(), "Factura");

            }
        });
        btnnotadespacho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotaDespachoFragment notaDespachoFragment = new NotaDespachoFragment();
                notaDespachoFragment.show(getActivity().getSupportFragmentManager(), "Nota de Despacho");

            }
        });
        btnserafin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SerafinFragment serafinFragment = new SerafinFragment();
                serafinFragment.show(getActivity().getSupportFragmentManager(), "Serafin");

            }
        });
        btnpuntos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PuntosFragment puntosFragment = new PuntosFragment();
                puntosFragment.show(getActivity().getSupportFragmentManager(), "Puntos");

            }
        });

        view.findViewById(R.id.btnboleta).setOnClickListener(v -> boleta());
        return view;

    }
    private  void boleta() {

    }

    @Override
    public void applyTexts(String textsol) {

        totalmonto.setText(textsol+".00");
    }
}