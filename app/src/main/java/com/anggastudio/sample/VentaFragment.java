package com.anggastudio.sample;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.print.PrintHelper;

import android.os.Environment;
import android.os.Handler;
import android.print.PrintJob;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.anggastudio.printama.Printama;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import net.glxn.qrgen.android.MatrixToImageWriter;
import net.glxn.qrgen.android.QRCode;
import net.glxn.qrgen.core.image.ImageType;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class VentaFragment extends Fragment{

    private static final int REQUEST_CODE_PERMISSION = 1;
    ImageView imageQR;
    TextView totalmonto, cliente, operacion,cari,total,textsols;
    CardView gria;
    @SuppressLint("WrongThread")
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_venta, container, false);

        totalmonto  =  view.findViewById(R.id.txtimporte);
        CardView gria       = view.findViewById(R.id.card);
        //montosoles    = view.findViewById(R.id.t);


        TextView cara = view.findViewById(R.id.textcara);
        TextView producto = view.findViewById(R.id.textmanguera);
        TextView importetotal = view.findViewById(R.id.txtimporte);


        gria.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("cara",cara.getText().toString());
                bundle.putString("producto",producto.getText().toString());
                bundle.putString("importetotal",importetotal.getText().toString());
                DetalleOperacionFragment fragment = new DetalleOperacionFragment();
                fragment.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
            }
        });

        //Selección de Cara
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

        //Selección de Manguera
        CardView diesel = (CardView) view.findViewById(R.id.diesel);
        CardView gas90  = (CardView) view.findViewById(R.id.gas90);
        CardView gas95  = (CardView) view.findViewById(R.id.gas95);
        CardView gas97  = (CardView) view.findViewById(R.id.gas97);
        CardView glp    = (CardView) view.findViewById(R.id.glp);
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
        glp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textmanguera.setText("GLP");
            }
        });

        Button btnlibre        = view.findViewById(R.id.btnlibre);
        Button btnsoles        = view.findViewById(R.id.btnsoles);
        Button btngalones      = view.findViewById(R.id.btngalones);
        Button btnboleta       = view.findViewById(R.id.btnboleta);
        Button btnfactura      = view.findViewById(R.id.btnfactura);
        Button btnnotadespacho = view.findViewById(R.id.btnnotadespacho);
        Button btnserafin      = view.findViewById(R.id.btnserafin);
        Button btnpuntos       = view.findViewById(R.id.btnpuntos);

        ImageButton regreso       = view.findViewById(R.id.volverdasboard);

        regreso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DasboardFragment dasboardFragment  = new DasboardFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,dasboardFragment).commit();
            }
        });

        btnlibre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LibreFragment libreFragment = new LibreFragment();
                libreFragment.show(getActivity().getSupportFragmentManager(), "Libre");
                libreFragment.setCancelable(false);
            }
        });
        btnsoles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SolesFragment solesFragment = new SolesFragment();
                solesFragment.show(getActivity().getSupportFragmentManager(), "Soles");
                solesFragment.setCancelable(false);
            }
        });
        btngalones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GalonesFragment galonesFragment = new GalonesFragment();
                galonesFragment.show(getActivity().getSupportFragmentManager(), "Galones");
                galonesFragment.setCancelable(false);
            }
        });
        btnboleta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BoletaFragment boletaFragment = new BoletaFragment();
                boletaFragment.show(getActivity().getSupportFragmentManager(), "Boleta");
                boletaFragment.setCancelable(false);
            }
        });
        btnfactura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FacturaFragment facturaFragment = new FacturaFragment();
                facturaFragment.show(getActivity().getSupportFragmentManager(), "Factura");
                facturaFragment.setCancelable(false);
            }
        });
        btnnotadespacho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotaDespachoFragment notaDespachoFragment = new NotaDespachoFragment();
                notaDespachoFragment.show(getActivity().getSupportFragmentManager(), "Nota de Despacho");
                notaDespachoFragment.setCancelable(false);
            }
        });
       btnserafin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SerafinFragment serafinFragment = new SerafinFragment();
                serafinFragment.show(getActivity().getSupportFragmentManager(), "Serafin");
                serafinFragment.setCancelable(false);
            }
        });
        btnpuntos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PuntosFragment puntosFragment = new PuntosFragment();
                puntosFragment.show(getActivity().getSupportFragmentManager(), "Puntos");

            }
        });
        view.findViewById(R.id.btnimprimir).setOnClickListener(v -> boletasin(turno,cajero,umed));
        return view;
    }


    int turno = 01;
    String cajero = "RUBEN ESCOBAR";
    long kilometraje = Long.parseLong("00000000000");
    String placa = "BKC-926";
    long dni = Long.parseLong("20653232");
    long ruc = Long.parseLong("20600064062");
    String clientes = "CONNEXA DISTRIBUICIONES SAC";
    long tarjeta = Long.parseLong("7020130000000309");
    String direccion = "AV FERROCARRIL N 590 HUANCAYO";
    String umed = "GLL";

    private  void boletasin(int turno ,String cajero, String umed) {

        Bitmap logo = Printama.getBitmapFromVector(getContext(), R.drawable.logorobles);
        Bitmap qr = Printama.getBitmapFromVector(getContext(), R.drawable.qr);

        //Fecha y Hora
        Calendar cal          = Calendar.getInstance(TimeZone.getTimeZone("America/Lima"));
        SimpleDateFormat sdf  = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String FechaHora      = sdf.format(cal.getTime());

        //Texto de Cara
        TextView textcaras    = (TextView) getView().findViewById(R.id.textcara);
        String cara           = textcaras.getText().toString();

        //Texto de Manguera
        TextView textmanguera = (TextView) getView().findViewById(R.id.textmanguera);
        String manguera       = textmanguera.getText().toString();
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

        //Texto Importe
        TextView txtimporte   = (TextView) getView().findViewById(R.id.txtimporte);
        String importe        =  txtimporte.getText().toString();
        double totalimporte   = Double.parseDouble(importe);

        double subtotal = (totalimporte/1.18);
        double roundOff = Math.round(subtotal*100.0)/100.0;
        String valorventa  = String.valueOf(roundOff);

        double impuesto = (totalimporte-roundOff);
        double impuestoOff = Math.round(impuesto*100.0)/100.0;
        String igv = String.valueOf(impuestoOff);

       // double a = 123.13698;
       // double roundOff = Math.round(a*100)/100;


        //Convertir decimal y Operacion de Cantidad por GALONES
       /* DecimalFormat df          = new DecimalFormat("#.###");
        double resultados         = Double.parseDouble(df.format(totalimporte/totalprecio ));
        String resultadoscantidad = String.valueOf(resultados);*/

        double resultados      = totalimporte/totalprecio ;
        double decimal = Math.round(resultados*1000.0)/1000.0;
       // String decimal         = String.format("%.3f", (resultados));
        String cantidadgalones = String.valueOf(decimal);
        String galon           = cantidadgalones.replace(",",".");

        //Convertir numero a letras
        Numero_Letras numToWord = new Numero_Letras();
        String letraimporte     = numToWord.Convertir(importe,true);

        String exoneradas = " 0.00";

        totalmonto  = getView().findViewById(R.id.txtimporte);

        cliente     = getView().findViewById(R.id.idcliente);

        //Generar Codigo QR
        String rucempresa ="20603939949";
        String boleta ="B001-0000004";
        String igv5 ="3.05";
        String fecha ="2022/12/21";
        String ruccliente ="20554542728";
        //Fecha y Hora del Comprobante

        StringBuilder sb = new StringBuilder();
        sb.append(rucempresa.toString());
        sb.append(boleta.toString());
        sb.append(igv5.toString());
        sb.append(importe.toString());
        sb.append(fecha.toString());

        String digitogenerado = sb.toString();


        View view = getView().findViewById(R.id.cara18);
        Printama.with(getContext()).connect(printama -> {
            printama.setSmallText();
            printama.addNewLine();
            printama.printImage(logo,200, Printama.CENTER);
            printama.setSmallText();
            printama.printTextln("GRIFO ROBLES S.A.C", Printama.CENTER);
            printama.printTextln("PRINCIPAL: AV.SAN BORJA SUR NRO.810", Printama.CENTER);
            printama.printTextln("LIMA-LIMA-SAN BORJA", Printama.CENTER);
            printama.printTextln("SUCURSAL: CAR. CENTRAL MARGEN NRO.S/N", Printama.CENTER);
            printama.printTextln("JUNIN - HUANCAYO - PILCOMAYO", Printama.CENTER);
            printama.printTextln("RUC: 20602130259", Printama.CENTER);
            printama.printTextln("BOLETA DE VENTA ELECTRONICA", Printama.CENTER);
            printama.printTextln("B006-0142546",Printama.CENTER);
            printama.setNormalText();
            printama.printDoubleDashedLine();
            printama.addNewLine(1);
            printama.setSmallText();
            printama.printTextln("Fecha - Hora : "+ FechaHora + "   Turno:"+turno,Printama.LEFT);
            printama.printTextln("Cajero : "+cajero, Printama.LEFT);
            printama.printTextln("Lado   : "+cara, Printama.LEFT);
            printama.setNormalText();
            printama.printDoubleDashedLine();
            printama.addNewLine(1);
            printama.setSmallText();
            printama.printTextln("PRODUCTO      "+"U/MED   "+"PRECIO   "+"CANTIDAD  "+"IMPORTE",Printama.RIGHT);
            printama.printTextln(productos +"   " + umed+"    " + finalPrecio+"    " + galon +"    "+ importe,Printama.RIGHT);
            printama.setNormalText();
            printama.printDoubleDashedLine();
            printama.setSmallText();
            printama.printTextln("OP. GRAVADAS S/: "+ valorventa, Printama.RIGHT);
            printama.printTextln("OP. EXONERADAS S/: "+exoneradas , Printama.RIGHT);
            printama.printTextln("I.G.V. 18% S/: "+igv, Printama.RIGHT);
            printama.printTextln("TOTAL VENTA S/: "+ importe, Printama.RIGHT);
            printama.setNormalText();
            printama.printDoubleDashedLine();
            printama.setSmallText();
            printama.printTextln("CONDICION DE PAGO:", Printama.LEFT);
            printama.printTextln("CONTADO S/: " + importe, Printama.RIGHT);
            printama.printTextln("SON: " + letraimporte, Printama.LEFT);
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix bitMatrix;
            try {
                bitMatrix = writer.encode(digitogenerado, BarcodeFormat.QR_CODE, 200, 200);
                int width = bitMatrix.getWidth();
                int height = bitMatrix.getHeight();
                Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
                for (int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {
                        int color = Color.WHITE;
                        if (bitMatrix.get(x, y)) color = Color.BLACK;
                        bitmap.setPixel(x, y, color);
                    }
                }
                if (bitmap != null) {
                    printama.printImage(bitmap);
                }
            } catch (WriterException e) {
                e.printStackTrace();
            }
            printama.setSmallText();
            printama.printTextln("Autorizado mediante resolucion de Superintendencia Nro. 203-2015 SUNAT. Representacion impresa de la boleta de venta electronica. Consulte desde\n"+ "http://4-fact.com/sven/auth/consulta");
            printama.addNewLine();
            printama.feedPaper();
            printama.close();
        }, this::showToast);
    }
    private  void boleta(int turno ,String cajero,long kilometraje, long dni,String clientes, String umed) {

        Bitmap logo = Printama.getBitmapFromVector(getContext(), R.drawable.logorobles);
        Bitmap qr = Printama.getBitmapFromVector(getContext(), R.drawable.qr);

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
            case "GLP":
                precio = "8.00";
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

        totalmonto  = getView().findViewById(R.id.txtimporte);

        cliente     = getView().findViewById(R.id.idcliente);

        StringBuilder sb = new StringBuilder();
        sb.append(cliente.getText().toString());

        String digitogenerado = sb.toString();

        Printama.with(getContext()).connect(printama -> {
            printama.setSmallText();
            printama.printImage(logo, 200);
            printama.addNewLine(1);
            printama.setSmallText();
            printama.printTextln("GRIFO ROBLES S.A.C", Printama.CENTER);
            printama.printTextln("PRINCIPAL: AV.SAN BORJA SUR NRO.810", Printama.CENTER);
            printama.printTextln("LIMA-LIMA-SAN BORJA", Printama.CENTER);
            printama.printTextln("SUCURSAL: CAR. CENTRAL MARGEN NRO.S/N", Printama.CENTER);
            printama.printTextln("JUNIN - HUANCAYO - PILCOMAYO", Printama.CENTER);
            printama.printTextln("RUC: 20602130259", Printama.CENTER);
            printama.printTextln("BOLETA DE VENTA ELECTRONICA", Printama.CENTER);
            printama.printTextln("B006-0142546",Printama.CENTER);
            printama.setNormalText();
            printama.printDoubleDashedLine();
            printama.setSmallText();
            printama.printTextln("Fecha-Hora:"+ FechaHora + "   Turno:"+turno,Printama.LEFT);
            printama.printTextln("Cajero:"+cajero, Printama.LEFT);
            printama.printTextln("Lado:"+cara + "         Kilometraje: "+kilometraje, Printama.LEFT);
            printama.printTextln("DNI:"+dni, Printama.LEFT);
            printama.printTextln("Cliente:"+clientes, Printama.LEFT);
            printama.setNormalText();
            printama.printDoubleDashedLine();
            printama.setSmallText();
            printama.printTextJustify("PRODUCTO","U/MED.","PRECIO","CANT","IMPORTE\n");
            printama.printTextJustify(manguera,umed , finalPrecio, resultadoscantidad, importe+"\n");
            printama.setNormalText();
            printama.printDoubleDashedLine();
            printama.setSmallText();
            printama.addNewLine(1);
            printama.printTextln("OP. GRAVADAS S/: "+ importe, Printama.RIGHT);
            printama.printTextln("OP. EXONERADAS S/: "+exoneradas , Printama.RIGHT);
            printama.printTextln("I.G.V. 18% S/: "+importe, Printama.RIGHT);
            printama.printTextln("TOTAL VENTA S/: "+ importe, Printama.RIGHT);
            printama.setNormalText();
            printama.printDoubleDashedLine();
            printama.setSmallText();
            printama.printTextln("CONDICION DE PAGO:", Printama.LEFT);
            printama.printTextln("CONTADO S/: " + importe, Printama.RIGHT);
            printama.printTextln("SON: " + letraimporte, Printama.LEFT);
            printama.setNormalText();
            printama.printDoubleDashedLine();
            printama.setSmallText();
            printama.printTextln("Autorizado mediante resolucion de Superintendencia Nro. 203-2015 SUNAT. Representacion impresa de la boleta de venta electronica. Consulte desde\n"+ "http://4-fact.com/sven/auth/consulta");
            printama.addNewLine();
            printama.feedPaper();
            printama.close();
        }, this::showToast);
    }
    private  void facturacion(int turno ,String cajero,long kilometraje,String placa,long ruc,String clientes ,String direccion,String umed) {
        Bitmap logo = Printama.getBitmapFromVector(getContext(), R.drawable.logorobles);

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
            case "GLP":
                precio = "8.00";
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

        totalmonto  = getView().findViewById(R.id.txtimporte);
        cliente     = getView().findViewById(R.id.idcliente);

        StringBuilder sb = new StringBuilder();
        sb.append(cliente.getText().toString());

        String digitogenerado = sb.toString();

        Printama.with(getContext()).connect(printama -> {
            printama.setSmallText();
            printama.printImage(logo, 200);
            printama.addNewLine(1);
            printama.setSmallText();
            printama.printTextln("GRIFO ROBLES S.A.C", Printama.CENTER);
            printama.printTextln("PRINCIPAL: AV.SAN BORJA SUR NRO.810", Printama.CENTER);
            printama.printTextln("LIMA-LIMA-SAN BORJA", Printama.CENTER);
            printama.printTextln("SUCURSAL: CAR. CENTRAL MARGEN NRO.S/N", Printama.CENTER);
            printama.printTextln("JUNIN - HUANCAYO - PILCOMAYO", Printama.CENTER);
            printama.printTextln("RUC: 20602130259", Printama.CENTER);
            printama.printTextln("FACTURA DE VENTA ELECTRONICA", Printama.CENTER);
            printama.printTextln("F001-0000004",Printama.CENTER);
            printama.setNormalText();
            printama.printDoubleDashedLine();
            printama.setSmallText();
            printama.printTextln("Fecha-Hora:"+ FechaHora + "   Turno: "+turno,Printama.LEFT);
            printama.printTextln("Cajero: "+cajero, Printama.LEFT);
            printama.printTextln("Lado:"+cara + "         Kilometraje: "+kilometraje, Printama.LEFT);
            printama.printTextln("Placa:"+placa, Printama.LEFT);
            printama.printTextln("RUC:"+ruc, Printama.LEFT);
            printama.printTextln("Cliente:"+clientes, Printama.LEFT);
            printama.printTextln("Dirección:"+direccion, Printama.LEFT);
            printama.setNormalText();
            printama.printDoubleDashedLine();
            printama.setSmallText();
            printama.printTextJustify("PRODUCTO","U/MED.","PRECIO","CANT","IMPORTE\n");
            printama.printTextJustify(manguera,umed , finalPrecio, resultadoscantidad, importe+"\n");
            printama.setNormalText();
            printama.printDoubleDashedLine();
            printama.setSmallText();
            printama.addNewLine(1);
            printama.printTextln("OP. GRAVADAS S/: "+ importe, Printama.RIGHT);
            printama.printTextln("OP. EXONERADAS S/: "+exoneradas , Printama.RIGHT);
            printama.printTextln("I.G.V. 18% S/: "+importe, Printama.RIGHT);
            printama.printTextln("TOTAL VENTA S/: "+ importe, Printama.RIGHT);
            printama.setNormalText();
            printama.printDoubleDashedLine();
            printama.setSmallText();
            printama.printTextln("CONDICION DE PAGO:", Printama.LEFT);
            printama.printTextln("CONTADO S/: " + importe, Printama.RIGHT);
            printama.printTextln("SON: " + letraimporte, Printama.LEFT);
            printama.setSmallText();
            printama.printTextln("Autorizado mediante resolucion de Superintendencia Nro. 203-2015 SUNAT. Representacion impresa de la factura de venta electronica. Consulte desde\n"+ "http://4-fact.com/sven/auth/consulta");
            printama.addNewLine();
            printama.feedPaper();
            printama.close();
        }, this::showToast);
    }
    private  void notadespacho(int turno ,String cajero,int kilometraje,String placa,long dni,String clientes,long tarjeta,String umed) {
        Bitmap logo = Printama.getBitmapFromVector(getContext(), R.drawable.logorobles);

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
            case "GLP":
                precio = "8.00";
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

        totalmonto  = getView().findViewById(R.id.txtimporte);
        cliente     = getView().findViewById(R.id.idcliente);

        StringBuilder sb = new StringBuilder();
        sb.append(cliente.getText().toString());

        String digitogenerado = sb.toString();

        Printama.with(getContext()).connect(printama -> {
            printama.setSmallText();
            printama.printImage(logo, 200);
            printama.addNewLine(1);
            printama.setSmallText();
            printama.printTextln("GRIFO ROBLES S.A.C", Printama.CENTER);
            printama.printTextln("PRINCIPAL: AV.SAN BORJA SUR NRO.810", Printama.CENTER);
            printama.printTextln("LIMA-LIMA-SAN BORJA", Printama.CENTER);
            printama.printTextln("SUCURSAL: CAR. CENTRAL MARGEN NRO.S/N", Printama.CENTER);
            printama.printTextln("JUNIN - HUANCAYO - PILCOMAYO", Printama.CENTER);
            printama.printTextln("NOTA DE DESPACHO", Printama.CENTER);
            printama.printTextln("015-0017680",Printama.CENTER);
            printama.setNormalText();
            printama.printDoubleDashedLine();
            printama.setSmallText();
            printama.printTextln("Fecha-Hora:"+ FechaHora + "   Turno: "+turno,Printama.LEFT);
            printama.printTextln("Cajero: "+cajero, Printama.LEFT);
            printama.printTextln("Lado:"+cara + "         Kilometraje: "+kilometraje, Printama.LEFT);
            printama.printTextln("Placa:"+placa, Printama.LEFT);
            printama.printTextln("RUC / DNI:"+dni, Printama.LEFT);
            printama.printTextln("Cliente:"+clientes, Printama.LEFT);
            printama.printTextln("#Tarjeta:"+ tarjeta + "  Chofer:",Printama.LEFT);
            printama.setNormalText();
            printama.printDoubleDashedLine();
            printama.setSmallText();
            printama.printTextJustify("PRODUCTO","U/MED.","PRECIO","CANT","IMPORTE\n");
            printama.printTextJustify(manguera,umed, finalPrecio, resultadoscantidad, importe+"\n");
            printama.setNormalText();
            printama.printDoubleDashedLine();
            printama.setSmallText();
            printama.addNewLine(1);
            printama.printTextln("DESCUENTO S/: "+ importe, Printama.RIGHT);
            printama.printTextln("TOTAL VENTA S/: "+ importe, Printama.RIGHT);
            printama.setNormalText();
            printama.printDoubleDashedLine();
            printama.setSmallText();
            printama.printTextln("NOMBRE:", Printama.LEFT);
            printama.printTextln("DNI:", Printama.LEFT);
            printama.printTextln("FIRMA:", Printama.LEFT);
            printama.feedPaper();
            printama.close();
        }, this::showToast);
    }
    private  void serafin(int turno ,String cajero,long kilometraje, String umed) {
        Toast.makeText(getContext(), "SE GENERO SERAFIN", Toast.LENGTH_SHORT).show();

        Bitmap logo = Printama.getBitmapFromVector(getContext(), R.drawable.logorobles);

        //Fecha y Hora
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("America/Lima"));
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        String FechaHora = sdf.format(cal.getTime());
        //Texto de Cara
        TextView textcaras = (TextView) getView().findViewById(R.id.textcara);
        String cara = textcaras.getText().toString();
        //Texto de Manguera
        TextView textmanguera = (TextView) getView().findViewById(R.id.textmanguera);
        String manguera = textmanguera.getText().toString();
        String precio = null;
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
            case "GLP":
                precio = "8.00";
                break;
            default:
                Log.d("Error", "NULL");
        }
        String finalPrecio = precio;
        double totalprecio = Double.parseDouble(finalPrecio);

        //Texto Importe
        TextView txtimporte = (TextView) getView().findViewById(R.id.txtimporte);
        String importe = txtimporte.getText().toString();
        double totalimporte = Double.parseDouble(importe);

        //Convertir decimal y Operacion de Cantidad por GALONES
        DecimalFormat df = new DecimalFormat("#.###");
        double resultados = Double.parseDouble(df.format(totalimporte / totalprecio));
        String resultadoscantidad = String.valueOf(resultados);

        //Convertir numero a letras
        Numero_Letras numToWord = new Numero_Letras();
        String letraimporte = numToWord.Convertir(importe, true);

        String exoneradas = " 0.00";


        Printama.with(getContext()).connect(printama -> {
            printama.setSmallText();
            printama.printImage(logo, 200);
            printama.addNewLine(1);
            printama.setSmallText();
            printama.printTextln("GRIFO ROBLES S.A.C", Printama.CENTER);
            printama.printTextln("PRINCIPAL: AV.SAN BORJA SUR NRO.810", Printama.CENTER);
            printama.printTextln("LIMA-LIMA-SAN BORJA", Printama.CENTER);
            printama.printTextln("SUCURSAL: CAR. CENTRAL MARGEN NRO.S/N", Printama.CENTER);
            printama.printTextln("JUNIN - HUANCAYO - PILCOMAYO", Printama.CENTER);
            printama.printTextln("TICKET SERAFIN", Printama.CENTER);
            printama.printTextln("001-0000003", Printama.CENTER);
            printama.setNormalText();
            printama.printDoubleDashedLine();
            printama.setSmallText();
            printama.printTextln("Fecha-Hora:" + FechaHora + "   Turno: "+turno, Printama.LEFT);
            printama.printTextln("Cajero:"+cajero, Printama.LEFT);
            printama.printTextln("Lado:" + cara + "   Kilometraje: " +kilometraje, Printama.LEFT);
            printama.setNormalText();
            printama.printDoubleDashedLine();
            printama.setSmallText();
            printama.printTextJustify("PRODUCTO", "U/MED.", "PRECIO", "CANT", "IMPORTE\n");
            printama.printTextJustify(manguera, umed, finalPrecio, resultadoscantidad, importe + "\n");
            printama.setNormalText();
            printama.printDoubleDashedLine();
            printama.setSmallText();
            printama.printTextln("TOTAL VENTA: S/: " + importe, Printama.RIGHT);
            printama.addNewLine();
            printama.feedPaper();
            printama.close();
        }, this::showToast);
    }
    private void showToast(String message) {
        Toast.makeText(getContext(), "Conectar Bluetooth", Toast.LENGTH_SHORT).show();
    }


}