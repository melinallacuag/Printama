package com.anggastudio.sample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.anggastudio.printama.Printama;

public class Ventas extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ventas);
        findViewById(R.id.btnimpresion).setOnClickListener(v -> factura());
    }
    private void factura() {
        Bitmap logo = Printama.getBitmapFromVector(this, R.drawable.robles_sinfondo);
        Printama.with(this).connect(printama -> {
            printama.setSmallText();
            printama.printImage(logo, 300);
            printama.printTextln("GRIFO ROBLES S.A.C", Printama.CENTER);
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
            printama.printTextln("Fecha-Hora: 11/01/2023 09:58:23", Printama.LEFT);
            printama.printTextln("Turno: 3", Printama.LEFT);
            printama.printTextln("Cajero: FABIOLA MARIBEL HERRERA HUERTA", Printama.LEFT);
            printama.printTextln("Lado: 17 ", Printama.LEFT);
            printama.setNormalText();
            printama.printDoubleDashedLine();
            printama.setSmallText();
            printama.printTextJustify("PRODUCTO","U/MED.","PRECIO","CANTIDAD\n");
            printama.printTextJustify("GASOHOL 90","GLL.","16.69","39.96\n");
            printama.setNormalText();
            printama.printDoubleDashedLine();
            printama.setSmallText();
            printama.printTextln("TOTAL VENTA: S/ 39.96", Printama.RIGHT);
            printama.setNormalText();
            printama.printDoubleDashedLine();
            printama.setSmallText();
            printama.printTextln("CONDICION DE PAGO:", Printama.LEFT);
            printama.printTextln("CONTADO: S/ 39.96", Printama.RIGHT);
            printama.printTextln("SON: VEINTE CON 00/100 SOLES", Printama.LEFT);
            printama.setNormalText();
            printama.printDoubleDashedLine();
            printama.feedPaper();
            printama.close();
        });
    }
}