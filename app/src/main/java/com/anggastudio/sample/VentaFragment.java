package com.anggastudio.sample;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.anggastudio.printama.Printama;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class VentaFragment extends Fragment {
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
        Bitmap logo = Printama.getBitmapFromVector(getContext(), R.drawable.robles_sinfondo);
        //Fecha y Hora
        Calendar cal          = Calendar.getInstance(TimeZone.getTimeZone("America/Lima"));
        SimpleDateFormat sdf  = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        String FechaHora      = sdf.format(cal.getTime());

        //Texto de Cara
        TextView textcaras    = (TextView) getView().findViewById(R.id.textcara);
        String cara           = textcaras.getText().toString();

        //Texto de Manguera
        TextView textmanguera = (TextView) getView().findViewById(R.id.textmanguera);
        String manguera       = textmanguera.getText().toString();
        String precio         = null;
        switch (manguera) {
            case "DIESEL":
                precio = "18.90";
                break;
            case "GAS 90":
                precio = "16.69";
                break;
            case "GAS 95":
                precio = "18.39";
                break;
            case "GAS 97":
                precio = "19.79";
                break;
            default:
                Log.d("Error", "NULL");
        }
        String finalPrecio = precio;
        double totalprecio = Double.parseDouble(finalPrecio);

        //Texto Importe
        TextView txtimporte   = (TextView) getView().findViewById(R.id.txtimporte);
        String importe        =  txtimporte.getText().toString();
        double totalimporte   = Double.parseDouble(importe);

        //Convertir decimal y Operacion de Cantidad por GALONES
        DecimalFormat df          = new DecimalFormat("#.###");
        double resultados         = Double.parseDouble(df.format(totalimporte/totalprecio ));
        String resultadoscantidad = String.valueOf(resultados);

        //Convertir numero a letras
        Numero_Letras numToWord = new Numero_Letras();
        String letraimporte     = numToWord.Convertir(importe,true);

        String exoneradas = " 0.00";
        String nota       = "Some Text";

        Printama.with(getContext()).connect(printama -> {
            printama.setSmallText();
            printama.printImage(logo, 200);
            printama.addNewLine(1);
            printama.printText("GRIFO ROBLES S.A.C\n", Printama.CENTER);
            printama.printTextln("PRINCIPAL: AV.SAN BORJA SUR NRO.810\n"+
                    "DTO.402 LIMA-LIMA-SAN BORJA", Printama.CENTER);
            printama.printTextln("SUCURSAL: CAR. CENTRAL MARGEN NRO.S/N\n" +
                    "JUNIN - HUANCAYO - PILCOMAYO", Printama.CENTER);
            printama.printTextln("RUC: 20602130259", Printama.CENTER);
            printama.printTextln("BOLETA DE VENTA ELECTRONICA", Printama.CENTER);
            printama.printText("B006-0142546\n",Printama.CENTER);
            printama.setNormalText();
            printama.printDoubleDashedLine();
            printama.setSmallText();
            printama.printTextln("Fecha-Hora:"+ FechaHora + "   Turno:02",Printama.LEFT);
            printama.printTextln("Cajero: FABIOLA MARIBEL HERRERA HUERTA", Printama.LEFT);
            printama.printTextln("Lado:"+cara, Printama.LEFT);
            printama.setNormalText();
            printama.printDoubleDashedLine();
            printama.setSmallText();
            printama.printTextJustify("PRODUCTO","U/MED.","PRECIO","CANT","IMPORTE\n");
            printama.printTextJustify(manguera,"GLL" , finalPrecio, resultadoscantidad, importe+"\n");
            printama.setNormalText();
            printama.printDoubleDashedLine();
            printama.setSmallText();
            printama.addNewLine(1);
            printama.printTextln("OP. GRAVADAS S/: "+ importe, Printama.RIGHT);
            printama.printTextln("OP. EXONERADAS S/: "+exoneradas , Printama.RIGHT);
            printama.printTextln("OP. EXONERADAS S/: "+importe, Printama.RIGHT);
            printama.printTextln("TOTAL VENTA S/: "+ importe, Printama.RIGHT);
            printama.setNormalText();
            printama.printDoubleDashedLine();
            printama.setSmallText();
            printama.printTextln("CONDICION DE PAGO:", Printama.LEFT);
            printama.printTextln("CONTADO S/: "+ importe, Printama.RIGHT);
            printama.printTextln("SON: "+letraimporte, Printama.LEFT);
            printama.setNormalText();
            printama.printDoubleDashedLine();
            printama.feedPaper();
            printama.close();
        });

    }


}