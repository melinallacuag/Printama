package com.anggastudio.sample;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.print.PrintHelper;

import android.os.Environment;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class VentaFragment extends Fragment{

    ImageView imageQR;
    TextView totalmonto, cliente, operacion;
    @SuppressLint("WrongThread")
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_venta, container, false);

        imageQR = view.findViewById(R.id.image_qr);
        totalmonto  =  view.findViewById(R.id.txtimporte);
        operacion   = view.findViewById(R.id.op);
        cliente     = view.findViewById(R.id.idcliente);


        String rucempresa ="20603939949";
        String factura ="F001-0000004";
        String igv ="3.05";
        String fecha ="2022/12/21";
        String ruccliente ="20554542728";

        StringBuilder sb = new StringBuilder();
        sb.append(rucempresa.toString());
        sb.append(factura.toString());
        sb.append(igv.toString());
        sb.append(fecha.toString());
        sb.append(ruccliente.toString());

        String digitogenerado = sb.toString();


        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(digitogenerado, BarcodeFormat.QR_CODE, 200, 200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            ImageView imageViewQrCode = view.findViewById(R.id.image_qr);
            imageViewQrCode.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }


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

        view.findViewById(R.id.btnimprimir).setOnClickListener(v -> boleta());
        return view;
    }



    private  void boleta() {

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
        operacion   = getView().findViewById(R.id.op);
        cliente     = getView().findViewById(R.id.idcliente);

        StringBuilder sb = new StringBuilder();
        sb.append(cliente.getText().toString());
        sb.append(operacion.getText().toString());

        String digitogenerado = sb.toString();

        Printama.with(getContext()).connect(printama -> {
            printama.setSmallText();
            printama.printImage(logo, 200);
            printama.addNewLine(1);
            printama.printText("GRIFO ROBLES S.A.C\n", Printama.CENTER);
           /* printama.printTextln("PRINCIPAL: AV.SAN BORJA SUR NRO.810\n"+
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
            printama.printTextln("IGV S/: "+importe, Printama.RIGHT);
            printama.printTextln("TOTAL VENTA S/: "+ importe, Printama.RIGHT);
            printama.setNormalText();
            printama.printDoubleDashedLine();*/
            printama.setSmallText();
            printama.printTextln("CONDICION DE PAGO:", Printama.LEFT);
            printama.printTextln("CONTADO S/: "+ importe, Printama.RIGHT);
            printama.printTextln("SON: "+letraimporte, Printama.LEFT);
            printama.addNewLine();

        /*    ImageView imageViewQrCode = getView().findViewById(R.id.image_qr);
           Bitmap bitMatrix = ((BitmapDrawable) imageViewQrCode.getDrawable()).getBitmap();
            printama.printImage( bitMatrix);*/

 /*
            PrintHelper printHelper = new PrintHelper(getContext());
            printHelper.setScaleMode(PrintHelper.SCALE_MODE_FIT);
            Bitmap bitmap = ((BitmapDrawable) imageQR.getDrawable()).getBitmap();
              printama.printImage(bitmap, 200);
*/
            /*setImageBitmap*/

            Bitmap bitmap = ((BitmapDrawable) imageQR.getDrawable()).getBitmap();
            printama.printImage(bitmap, 200);

           /* Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.id.image_qr);
            printama.printImage(bitmap, 200);*/
           // printHelper.printBitmap("Print Bitmap", bitmap);


            printama.feedPaper();
            printama.close();
        }, this::showToast);
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), "Conectar Bluetooth", Toast.LENGTH_SHORT).show();
    }


}