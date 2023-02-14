package com.anggastudio.sample;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.anggastudio.printama.Printama;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class SerafinFragment extends DialogFragment {

    Button cerraraserafin, imprimirserafin;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_serafin, container, false);

        cerraraserafin   = view.findViewById(R.id.btncancelarserafin);
        cerraraserafin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
      /*  imprimirserafin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "SE GENERO SERAFIN", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });*/
        view.findViewById(R.id.btnimprimirserafin).setOnClickListener(v -> serafin(turno,cajero,kilometraje,umed));
        return view;
    }
    int turno           = 1;
    String cajero       = "RUBEN ESCOBAR";
    long kilometraje    = Long.parseLong("00000000000");
    String placa        = "BKC-926";
    long dni            = Long.parseLong("20653232");
    long ruc            = Long.parseLong("20600064062");
    String clientes     = "CONNEXA DISTRIBUICIONES SAC";
    long tarjeta        = Long.parseLong("7020130000000309");
    String direccion    = "AV FERROCARRIL N 590 HUANCAYO";
    String umed         = "GLL";
    private  void serafin(int turno ,String cajero,long kilometraje, String umed) {
        Bundle bundle         = this.getArguments();
        //LODO DE LA EMPRESA
        Bitmap logo = Printama.getBitmapFromVector(getContext(), R.drawable.logorobles);

        //FECHAHORA
        Calendar cal         = Calendar.getInstance(TimeZone.getTimeZone("America/Lima"));
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String FechaHora     = sdf.format(cal.getTime());

        //LADO
        String lado       = bundle.getString("lado");

        //PRODUCTO
        String manguera        = bundle.getString("producto");
        String precio         = null;
        String producto       = null;

        switch (manguera) {
            case "DIESEL":
                precio = "18.90";
                producto = "DIESEL B5 S50";
                break;
            case "GAS 90":
                precio = "16.69";
                producto = "GASOHOL 90";
                break;
            case "GAS 95":
                precio = "18.39";
                producto = "GASOHOL 95";
                break;
            case "GAS 97":
                precio = "19.79";
                producto = "GASOHOL 97";
                break;
            case "GLP":
                precio = "8.00";
                producto = "GLP";
                break;
            default:
                Log.d("Error", "NULL");
        }
        String finalPrecio = precio;
        String productos = producto;
        double totalprecio = Double.parseDouble(finalPrecio);

        //IMPORTE
        String importe    = bundle.getString("importe");
        double totalimporte = Double.parseDouble(importe);

        //OPERACION DE VALOR DE CANTIDAD POR GALONES
        double resultados      = totalimporte/totalprecio ;
        double decimal = Math.round(resultados*1000.0)/1000.0;
        String cantidadgalones = String.valueOf(decimal);
        String galon           = cantidadgalones.replace(",",".");

        Printama.with(getContext()).connect(printama -> {
            printama.setNormalText();
            printama.printTextln("                 ", Printama.CENTER);
            printama.printImage(logo, 200);
            printama.setSmallText();
            printama.printTextlnBold("GRIFO ROBLES S.A.C", Printama.CENTER);
            printama.printTextlnBold("PRINCIPAL: AV.SAN BORJA SUR NRO.810", Printama.CENTER);
            printama.printTextlnBold("LIMA-LIMA-SAN BORJA", Printama.CENTER);
            printama.printTextlnBold("SUCURSAL: CAR. CENTRAL MARGEN NRO.S/N", Printama.CENTER);
            printama.printTextlnBold("JUNIN - HUANCAYO - PILCOMAYO", Printama.CENTER);
            printama.printTextlnBold("TICKET SERAFIN", Printama.CENTER);
            printama.printTextlnBold("001-0000003", Printama.CENTER);
            printama.setSmallText();
            printama.printDoubleDashedLine();
            printama.addNewLine(1);
            printama.setSmallText();
            printama.printTextln("Fecha - Hora : " + FechaHora + "   Turno : "+turno, Printama.LEFT);
            printama.printTextln("Cajero : "+cajero, Printama.LEFT);
            printama.printTextln("Lado   : " + lado + "   Kilometraje : " +kilometraje, Printama.LEFT);
            printama.printDoubleDashedLine();
            printama.addNewLine(1);
            printama.setSmallText();
            printama.printTextlnBold("PRODUCTO      "+"U/MED   "+"PRECIO   "+"CANTIDAD  "+"IMPORTE",Printama.RIGHT);
            printama.setSmallText();
            printama.printTextln(productos,Printama.LEFT);
            printama.printTextln(umed+"    " + finalPrecio+"     " + galon +"    "+ importe,Printama.RIGHT);
            printama.printDoubleDashedLine();
            printama.addNewLine(1);
            printama.setSmallText();
            printama.printTextBold("TOTAL VENTA: S/    " + importe, Printama.RIGHT);
            printama.addNewLine();
            printama.feedPaper();
            printama.close();
            Toast.makeText(getContext(), "SE GENERO SERAFIN", Toast.LENGTH_SHORT).show();
            dismiss();
        }, this::showToast);
    }
    private void showToast(String message) {
        Toast.makeText(getContext(), "Conectar Bluetooth", Toast.LENGTH_SHORT).show();
    }
}