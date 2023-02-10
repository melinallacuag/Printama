package com.anggastudio.sample;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.widget.LinearLayout;
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
    TextView totalmonto, cliente;

    public void setclick(CardView gria, TextView lado, String texto) {
        gria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lado.setText(texto);
            }
        });
    }

    @SuppressLint("WrongThread")
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_venta, container, false);

        totalmonto  =  view.findViewById(R.id.txtimporte);


        CardView grias        = view.findViewById(R.id.card);
        TextView producto     = view.findViewById(R.id.textmanguera);
        TextView cara         = view.findViewById(R.id.textcara);
        TextView importetotal = view.findViewById(R.id.txtimporte);
        TextView operacion    = view.findViewById(R.id.txtoperacion);
        String operaciones    = operacion.getText().toString();


        grias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ("03".equals(operaciones)) {
                    Bundle bundle = new Bundle();
                    bundle.putString("producto", producto.getText().toString());
                    bundle.putString("lado", cara.getText().toString());
                    bundle.putString("importe", importetotal.getText().toString());
                    PrintBoletaFragment printBoletaFragment = new PrintBoletaFragment();
                    printBoletaFragment.setArguments(bundle);
                    printBoletaFragment.show(getActivity().getSupportFragmentManager(), "Boleta");
                    printBoletaFragment.setCancelable(false);
                }else if ("01".equals(operaciones)) {
                        Bundle bundle = new Bundle();
                        bundle.putString("producto",producto.getText().toString());
                        bundle.putString("lado",cara.getText().toString());
                        bundle.putString("importe",importetotal.getText().toString());
                        PrintFacturaFragment printFacturaFragment = new PrintFacturaFragment();
                        printFacturaFragment.setArguments(bundle);
                        printFacturaFragment.show(getActivity().getSupportFragmentManager(), "Factura");
                        printFacturaFragment.setCancelable(false);
                }else if ("99".equals(operaciones)) {
                        Bundle bundle = new Bundle();
                        bundle.putString("producto",producto.getText().toString());
                        bundle.putString("lado",cara.getText().toString());
                        bundle.putString("importe",importetotal.getText().toString());
                        PrintNotaDespachoFragment printNotaDespachoFragment = new PrintNotaDespachoFragment();
                        printNotaDespachoFragment.setArguments(bundle);
                        printNotaDespachoFragment.show(getActivity().getSupportFragmentManager(), "Nota de Despacho");
                        printNotaDespachoFragment.setCancelable(false);
                }else{
                        Log.d("Error", "NULL");
                }
            }
        });

        //Selección de Cara
        CardView Cara17         = (CardView) view.findViewById(R.id.cara17);
        CardView Cara18         = (CardView) view.findViewById(R.id.cara18);
        final TextView textcara = (TextView) view.findViewById(R.id.textcara);
      //  final TextView textcara2 = (TextView) view.findViewById(R.id.textcara2);

        setclick(Cara17, textcara, "17");
        setclick(Cara18, textcara, "18");

        //Selección de Manguera
        CardView diesel = (CardView) view.findViewById(R.id.diesel);
        CardView gas90  = (CardView) view.findViewById(R.id.gas90);
        CardView gas95  = (CardView) view.findViewById(R.id.gas95);
        CardView gas97  = (CardView) view.findViewById(R.id.gas97);
        CardView glp    = (CardView) view.findViewById(R.id.glp);
        final TextView textmanguera = (TextView) view.findViewById(R.id.textmanguera);

        setclick(diesel,textmanguera, "DIESEL");
        setclick(gas90, textmanguera, "GAS 90");
        setclick(gas95, textmanguera, "GAS 95");
        setclick(gas97, textmanguera, "GAS 97");
        setclick(glp,   textmanguera, "GLP");

       /* diesel.setOnClickListener(new View.OnClickListener() {
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
        });*/


        Button btnlibre        = view.findViewById(R.id.btnlibre);
        Button btnsoles        = view.findViewById(R.id.btnsoles);
        Button btngalones      = view.findViewById(R.id.btngalones);
        Button btnboleta       = view.findViewById(R.id.btnboleta);
        Button btnfactura      = view.findViewById(R.id.btnfactura);
        Button btnnotadespacho = view.findViewById(R.id.btnnotadespacho);
        Button btnserafin      = view.findViewById(R.id.btnserafin);
        Button btnpuntos       = view.findViewById(R.id.btnpuntos);
        ImageButton regreso    = view.findViewById(R.id.volverdasboard);

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
                Bundle bundle = new Bundle();
                bundle.putString("producto",producto.getText().toString());
                bundle.putString("lado",cara.getText().toString());
                bundle.putString("importe",importetotal.getText().toString());
                SerafinFragment serafinFragment = new SerafinFragment();
                serafinFragment.setArguments(bundle);
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
        view.findViewById(R.id.btnimprimir).setOnClickListener(v -> facturacion(turno,cajero,kilometraje,placa,ruc,clientes,direccion,umed));
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

    private  void boletasin(int turno ,String cajero, String umed) {

        //LOGO DE LA EMPRESA
        Bitmap logo = Printama.getBitmapFromVector(getContext(), R.drawable.logorobles);

        //FECHA-HORA
        Calendar cal          = Calendar.getInstance(TimeZone.getTimeZone("America/Lima"));
        SimpleDateFormat sdf  = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String FechaHora      = sdf.format(cal.getTime());

        //LADO
        TextView textlado     = (TextView) getView().findViewById(R.id.textcara);
        String lado           = textlado.getText().toString();

        //PRODUCTO
        TextView textproducto = (TextView) getView().findViewById(R.id.textmanguera);
        String manguera       = textproducto.getText().toString();
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
        String productos   = producto;
        double totalprecio = Double.parseDouble(finalPrecio);

        //IMPORTE
        TextView txtimporte   = (TextView) getView().findViewById(R.id.txtimporte);
        String importe        =  txtimporte.getText().toString();
        double totalimporte   = Double.parseDouble(importe);

        //OP. GRAVADAS
        double subtotal     = (totalimporte/1.18);
        double roundOff     = Math.round(subtotal*100.0)/100.0;
        String valorventa   = String.valueOf(roundOff);

        //IGV
        double impuesto     = (totalimporte-roundOff);
        double impuestoOff  = Math.round(impuesto*100.0)/100.0;
        String igv          = String.valueOf(impuestoOff);

        //Convertir decimal y Operacion de Cantidad por GALONES
        double resultados       = totalimporte/totalprecio ;
        double decimal          = Math.round(resultados*1000.0)/1000.0;
        String cantidadgalones  = String.valueOf(decimal);
        String galon            = cantidadgalones.replace(",",".");

        //Convertir numero a letras
        Numero_Letras numToWord = new Numero_Letras();
        String letraimporte     = numToWord.Convertir(importe,true);

        String exoneradas = " 0.00";

        //Generar Codigo QR
        String rucempresa       ="20602130259";
        String tipodocumento    ="03";
        String boleta           ="B006-0142546";
        String fecha            = FechaHora.substring(6,10) + "-" + FechaHora.substring(3,5) + "-" + FechaHora.substring(0,2);
        String tipodni          ="01";
        String dni              ="11111111";

        /* DNI: 01
           RUC: 06
        */
      /*  01 factura
          03 boleta
          99 nota de desoacho
          98 serafin
      */
        //GENERAR CODIGO QR BOLETA
        StringBuilder QRBoleta = new StringBuilder();
        QRBoleta.append(rucempresa + "|".toString());
        QRBoleta.append(tipodocumento+ "|".toString());
        QRBoleta.append(boleta+ "|".toString());
        QRBoleta.append(igv+ "|".toString());
        QRBoleta.append(importe+ "|".toString());
        QRBoleta.append(fecha+ "|".toString());
        QRBoleta.append(tipodni+ "|".toString());
        QRBoleta.append(dni+ "|".toString());

        String Qrboleta = QRBoleta.toString();

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
            printama.printTextlnBold("RUC: " + rucempresa, Printama.CENTER);
            printama.printTextlnBold("BOLETA DE VENTA ELECTRONICA", Printama.CENTER);
            printama.printTextlnBold(boleta,Printama.CENTER);
            printama.setSmallText();
            printama.printDoubleDashedLine();
            printama.addNewLine(1);
            printama.setSmallText();
            printama.printTextln("Fecha - Hora : "+ FechaHora + "   Turno:"+turno,Printama.LEFT);
            printama.printTextln("Cajero : "+cajero, Printama.LEFT);
            printama.printTextln("Lado   : "+lado, Printama.LEFT);
            printama.setSmallText();
            printama.printDoubleDashedLine();
            printama.addNewLine(1);
            printama.setSmallText();
            printama.printTextlnBold("PRODUCTO      "+"U/MED   "+"PRECIO   "+"CANTIDAD  "+"IMPORTE",Printama.RIGHT);
            printama.setSmallText();
            printama.printTextln(productos,Printama.LEFT);
            printama.printTextln(umed+"    " + finalPrecio+"     " + galon +"    "+ importe,Printama.RIGHT);
            printama.setSmallText();
            printama.printDoubleDashedLine();
            printama.addNewLine(1);
            printama.setSmallText();
            printama.printTextln("OP. GRAVADAS: S/ "+ valorventa, Printama.RIGHT);
            printama.printTextln("OP. EXONERADAS: S/   "+ exoneradas , Printama.RIGHT);
            printama.printTextln("I.G.V. 18%: S/  "+ igv, Printama.RIGHT);
            printama.printTextlnBold("TOTAL VENTA: S/ "+ importe, Printama.RIGHT);
            printama.setSmallText();
            printama.printDoubleDashedLine();
            printama.addNewLine(1);
            printama.setSmallText();
            printama.printTextlnBold("CONDICION DE PAGO:", Printama.LEFT);
            printama.printTextlnBold("CONTADO: S/ " + importe, Printama.RIGHT);
            printama.setSmallText();
            printama.printTextln("SON: " + letraimporte, Printama.LEFT);
            printama.setSmallText();
            printama.printTextln("                 ", Printama.CENTER);
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix bitMatrix;
            try {
                bitMatrix = writer.encode(Qrboleta, BarcodeFormat.QR_CODE, 200, 200);
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

        //LOGO DE  LA EMPRESA
        Bitmap logo = Printama.getBitmapFromVector(getContext(), R.drawable.logorobles);

        //Fecha y Hora
        Calendar cal          = Calendar.getInstance(TimeZone.getTimeZone("America/Lima"));
        SimpleDateFormat sdf  = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String FechaHora      = sdf.format(cal.getTime());

        //LADO
        TextView textLado    = (TextView) getView().findViewById(R.id.textcara);
        String lado           = textLado.getText().toString();

        //PRODUCTO
        TextView textproducto = (TextView) getView().findViewById(R.id.textmanguera);
        String manguera       = textproducto.getText().toString();
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
        TextView txtimporte   = (TextView) getView().findViewById(R.id.txtimporte);
        String importe        =  txtimporte.getText().toString();
        double totalimporte   = Double.parseDouble(importe);

        double subtotal = (totalimporte/1.18);
        double roundOff = Math.round(subtotal*100.0)/100.0;
        String valorventa  = String.valueOf(roundOff);

        double impuesto = (totalimporte-roundOff);
        double impuestoOff = Math.round(impuesto*100.0)/100.0;
        String igv = String.valueOf(impuestoOff);

        //OPERACION DE VALOR DE CANTIDAD POR GALONES
        double resultados      = totalimporte/totalprecio ;
        double decimal = Math.round(resultados*1000.0)/1000.0;
        String cantidadgalones = String.valueOf(decimal);
        String galon           = cantidadgalones.replace(",",".");

        //CONVERTIR IMPORTE A TEXTO
        Numero_Letras numToWord = new Numero_Letras();
        String letraimporte     = numToWord.Convertir(importe,true);

        String exoneradas = " 0.00";

        //GENERAR QR
        String rucempresa     ="20602130259";
        String tipodocumento  = "01";
        String factura        ="F001-0000004";
        String fecha          = FechaHora.substring(6,10) + "-" + FechaHora.substring(3,5) + "-" + FechaHora.substring(0,2);
        String tiporuc        ="01";
        String ruccliente     ="11111111111";

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
            printama.printTextlnBold("RUC: " +rucempresa, Printama.CENTER);
            printama.printTextlnBold("FACTURA DE VENTA ELECTRONICA", Printama.CENTER);
            printama.printTextlnBold(factura,Printama.CENTER);
            printama.setSmallText();
            printama.printDoubleDashedLine();
            printama.setSmallText();
            printama.addNewLine(1);
            printama.printTextln("Fecha - Hora : "+FechaHora + "   Turno : "+turno,Printama.LEFT);
            printama.printTextln("Cajero : "+cajero, Printama.LEFT);
            printama.printTextln("Lado   : "+lado + "         Kilometraje : "+kilometraje, Printama.LEFT);
            printama.printTextln("Placa  : "+placa, Printama.LEFT);
            printama.printTextln("R.U.C.    : "+ruccliente, Printama.LEFT);
            printama.printTextln("Cliente   : "+clientes, Printama.LEFT);
            printama.printTextln("Dirección : "+direccion, Printama.LEFT);
            printama.setSmallText();
            printama.printDoubleDashedLine();
            printama.setSmallText();
            printama.addNewLine(1);
            printama.printTextlnBold("PRODUCTO      "+"U/MED   "+"PRECIO   "+"CANTIDAD  "+"IMPORTE",Printama.RIGHT);
            printama.setSmallText();
            printama.printTextln(productos,Printama.LEFT);
            printama.printTextln(umed+"    " + finalPrecio+"     " + galon +"    "+ importe,Printama.RIGHT);
            printama.setSmallText();
            printama.printDoubleDashedLine();
            printama.setSmallText();
            printama.addNewLine(1);
            printama.printTextln("OP. GRAVADAS: S/ "+ valorventa, Printama.RIGHT);
            printama.printTextln("OP. EXONERADAS: S/   "+ exoneradas , Printama.RIGHT);
            printama.printTextln("I.G.V. 18%: S/  "+ igv, Printama.RIGHT);
            printama.printTextlnBold("TOTAL VENTA: S/ "+ importe, Printama.RIGHT);
            printama.setSmallText();
            printama.printDoubleDashedLine();
            printama.setSmallText();
            printama.addNewLine(1);
            printama.printTextlnBold("CONDICION DE PAGO:", Printama.LEFT);
            printama.printTextlnBold("CONTADO: S/ " + importe, Printama.RIGHT);
            printama.setSmallText();
            printama.printTextln("SON: " + letraimporte, Printama.LEFT);
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix bitMatrix;
            StringBuilder QRFactura = new StringBuilder();
            QRFactura.append(rucempresa + "|".toString());
            QRFactura.append(tipodocumento+ "|".toString());
            QRFactura.append(factura+ "|".toString());
            QRFactura.append(igv+ "|".toString());
            QRFactura.append(importe+ "|".toString());
            QRFactura.append(fecha+ "|".toString());
            QRFactura.append(tiporuc+ "|".toString());
            QRFactura.append(ruccliente+ "|".toString());
            String Qrfactura = QRFactura.toString();
            printama.printTextln("         ", Printama.CENTER);
            try {
                bitMatrix = writer.encode(Qrfactura, BarcodeFormat.QR_CODE, 200, 200);
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
            printama.printTextln("Autorizado mediante resolucion de Superintendencia Nro. 203-2015 SUNAT. Representacion impresa de la factura de venta electronica. Consulte desde\n"+ "http://4-fact.com/sven/auth/consulta");
            printama.addNewLine();
            printama.feedPaper();
            printama.close();
        }, this::showToast);
    }
    private  void notadespacho(int turno ,String cajero,long kilometraje,String placa,long dni,String clientes,long tarjeta,String umed) {

        //LOGO
        Bitmap logo = Printama.getBitmapFromVector(getContext(), R.drawable.logorobles);

        //FECHAHORA
        Calendar cal          = Calendar.getInstance(TimeZone.getTimeZone("America/Lima"));
        SimpleDateFormat sdf  = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String FechaHora      = sdf.format(cal.getTime());

        //LADO
        TextView textlado     = (TextView) getView().findViewById(R.id.textcara);
        String lado           = textlado.getText().toString();

        //PRODUCTO
        TextView textproducto = (TextView) getView().findViewById(R.id.textmanguera);
        String manguera       = textproducto.getText().toString();
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
        TextView txtimporte   = (TextView) getView().findViewById(R.id.txtimporte);
        String importe        =  txtimporte.getText().toString();
        double totalimporte   = Double.parseDouble(importe);

        //OPERACION DE VALOR DE CANTIDAD POR GALONES
        double resultados      = totalimporte/totalprecio ;
        double decimal = Math.round(resultados*1000.0)/1000.0;
        String cantidadgalones = String.valueOf(decimal);
        String galon           = cantidadgalones.replace(",",".");

        String exoneradas = " 0.00";

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
            printama.printTextlnBold("NOTA DE DESPACHO", Printama.CENTER);
            printama.printTextlnBold("015-0017680",Printama.CENTER);
            printama.setSmallText();
            printama.printDoubleDashedLine();
            printama.addNewLine(1);
            printama.setSmallText();
            printama.printTextln("Fecha - Hora : "+ FechaHora + "   Turno: "+turno,Printama.LEFT);
            printama.printTextln("Cajero : "+cajero, Printama.LEFT);
            printama.printTextln("Lado   : "+lado + "         Kilometraje: "+kilometraje, Printama.LEFT);
            printama.printTextln("Placa  : "+placa, Printama.LEFT);
            printama.printTextln("RUC / DNI : "+dni, Printama.LEFT);
            printama.printTextln("Cliente   : "+clientes, Printama.LEFT);
            printama.printTextln("#Tarjeta  : "+tarjeta + "  Chofer : ",Printama.LEFT);
            printama.setSmallText();
            printama.printDoubleDashedLine();
            printama.addNewLine(1);
            printama.setSmallText();
            printama.printTextlnBold("PRODUCTO      "+"U/MED   "+"PRECIO   "+"CANTIDAD  "+"IMPORTE",Printama.RIGHT);
            printama.setSmallText();
            printama.printTextln(productos,Printama.LEFT);
            printama.printTextln(umed+"    " + finalPrecio+"     " + galon +"    "+ importe,Printama.RIGHT);
            printama.setSmallText();
            printama.printDoubleDashedLine();
            printama.setSmallText();
            printama.addNewLine(1);
            printama.printTextlnBold("TOTAL VENTA: S/ "+ importe, Printama.RIGHT);
            printama.setSmallText();
            printama.printDoubleDashedLine();
            printama.addNewLine(1);
            printama.setSmallText();
            printama.printTextln("NOMBRE:", Printama.LEFT);
            printama.printTextln("DNI:", Printama.LEFT);
            printama.printTextln("FIRMA:", Printama.LEFT);
            printama.feedPaper();
            printama.close();
        }, this::showToast);
    }
    private  void serafin(int turno ,String cajero,long kilometraje, String umed) {

        //LODO DE LA EMPRESA
        Bitmap logo = Printama.getBitmapFromVector(getContext(), R.drawable.logorobles);

        //FECHAHORA
        Calendar cal         = Calendar.getInstance(TimeZone.getTimeZone("America/Lima"));
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String FechaHora     = sdf.format(cal.getTime());

        //LADO
        TextView textlado = (TextView) getView().findViewById(R.id.textcara);
        String lado       = textlado.getText().toString();

        //PRODUCTO
        TextView textproducto = (TextView) getView().findViewById(R.id.textmanguera);
        String manguera       = textproducto.getText().toString();
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
        TextView txtimporte = (TextView) getView().findViewById(R.id.txtimporte);
        String importe = txtimporte.getText().toString();
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
        }, this::showToast);
    }
    private void showToast(String message) {
        Toast.makeText(getContext(), "Conectar Bluetooth", Toast.LENGTH_SHORT).show();
    }


}