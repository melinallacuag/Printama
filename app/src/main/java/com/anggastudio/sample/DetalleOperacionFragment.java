package com.anggastudio.sample;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.anggastudio.printama.Printama;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;


public class DetalleOperacionFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_detalle_operacion, container, false);

        TextView fechaemision = (TextView) view.findViewById(R.id.fecha);
        TextView cara     = view.findViewById(R.id.lado);
        TextView producto = view.findViewById(R.id.producto);
        TextView cantidad = view.findViewById(R.id.cantidad);
        TextView letras = view.findViewById(R.id.letras);
        TextView precioproducto = view.findViewById(R.id.precioproducto);
        TextView importetotal = view.findViewById(R.id.totalimporte);
        TextView opgravadas = view.findViewById(R.id.opgravadas);
        TextView opexoneradas = view.findViewById(R.id.opexoneradas);
        TextView totalventa = view.findViewById(R.id.totalventa);
        TextView totalcontado = view.findViewById(R.id.totalcontado);

        //Fecha y Hora del Comprobante
        Calendar fechahora    = Calendar.getInstance(TimeZone.getTimeZone("America/Lima"));
        SimpleDateFormat sdf  = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        String FechaHora      = sdf.format(fechahora.getTime());
        fechaemision.setText(FechaHora);

        //Detalle de la Operación
        Bundle  bundle = this.getArguments();
        String datocara         = bundle.getString("cara");
        String datoproducto     = bundle.getString("producto");
        String datoimportetotal = bundle.getString("importetotal");
        cara.setText(datocara);
        producto.setText(datoproducto);
        importetotal.setText(datoimportetotal);
        opgravadas.setText(datoimportetotal);
        opexoneradas.setText(datoimportetotal);
        totalventa.setText(datoimportetotal);
        totalcontado.setText(datoimportetotal);

        //Operracion de producto y el valor de precio
        String manguera       = producto.getText().toString();
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
        precioproducto.setText(precio);

       //Elaboracion de opeacion cantidad
        String finalPrecio = precio;
        double totalprecio = Double.parseDouble(finalPrecio);
        String importe        =  importetotal.getText().toString();
        double totalimporte   = Double.parseDouble(importe);

        //Convertir decimal y Operacion de Cantidad por GALONES
        DecimalFormat df          = new DecimalFormat("#.###");
        double resultados         = Double.parseDouble(df.format(totalimporte/totalprecio ));
        String resultadoscantidad = String.valueOf(resultados);
        cantidad.setText(resultadoscantidad);

        //Convertir numero a letras
        Numero_Letras numToWord = new Numero_Letras();
        String letraimporte     = numToWord.Convertir(importe,true);
        letras.setText(letraimporte);

        //Generar Codigo QR
        String rucempresa ="20603939949";
        String boleta ="B001-0000004";
        String igv ="3.05";
        String fecha ="2022/12/21";
        String ruccliente ="20554542728";

        //Fecha y Hora del Comprobante
        SimpleDateFormat sdf2  = new SimpleDateFormat("yyyy/MM/dd");
        String FechaHora2      = sdf2.format(fechahora.getTime());

        StringBuilder sb = new StringBuilder();
        sb.append(rucempresa.toString());
        sb.append(boleta.toString());
        sb.append(igv.toString());
        sb.append(importe.toString());
        sb.append(FechaHora2.toString());

        String digitogenerado = sb.toString();

        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(digitogenerado, BarcodeFormat.QR_CODE, 200, 200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            ImageView imageViewQrCode = view.findViewById(R.id.codigoqr);
            imageViewQrCode.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        view.findViewById(R.id.btneditar).setOnClickListener(v -> detalle());
        return view;
    }

    private void detalle() {
        View view = getView().findViewById(R.id.detalle);
        Printama.with(getContext()).connect(printama -> {
            printama.printFromView(view);
            new Handler().postDelayed(printama::close, 3000);
        }, this::showToast);
    }
    private void showToast(String message) {
        Toast.makeText(getContext(), "Conectar Bluetooth", Toast.LENGTH_SHORT).show();
    }
}