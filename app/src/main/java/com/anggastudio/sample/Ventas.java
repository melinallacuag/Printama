package com.anggastudio.sample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.anggastudio.printama.Printama;
import com.anggastudio.sample.mock.Mock;
import com.anggastudio.sample.model.PrintBody;
import com.anggastudio.sample.model.PrintModel;

import java.sql.PreparedStatement;
import java.text.BreakIterator;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.TimeZone;

public class Ventas extends AppCompatActivity implements SolesFragment.Custom_DialogInterface{

    public void  libre(View view){
        LibreFragment libreFragment = new LibreFragment();
        libreFragment.show(getSupportFragmentManager(),"Libre");
        libreFragment.setCancelable(false);
    }
    public void  galones(View view){
        GalonesFragment galonesFragment = new GalonesFragment();
        galonesFragment.show(getSupportFragmentManager(),"Galones");
        galonesFragment.setCancelable(false);
    }
    public void  boletas(){
        BoletaFragment boletaFragment = new BoletaFragment();
        boletaFragment.show(getSupportFragmentManager(),"Boleta");
        boletaFragment.setCancelable(false);
    }
    public void  factura(View view){
        FacturaFragment facturaFragment = new FacturaFragment();
        facturaFragment.show(getSupportFragmentManager(),"Factura");
        facturaFragment.setCancelable(false);
    }
    public void  notadespacho(View view){
        NotaDespachoFragment notaDespachoFragment = new NotaDespachoFragment();
        notaDespachoFragment.show(getSupportFragmentManager(),"Nota Despacho");
        notaDespachoFragment.setCancelable(false);
    }
    public void  serafin(View view){
        SerafinFragment serafinFragment = new SerafinFragment();
        serafinFragment.show(getSupportFragmentManager(),"Serafin");
        serafinFragment.setCancelable(false);
    }
    public void  puntos(View view){
        PuntosFragment puntosFragment = new PuntosFragment();
        puntosFragment.show(getSupportFragmentManager(),"Puntos");
        puntosFragment.setCancelable(false);
    }
    public void  soles(View view){
        SolesFragment solesFragment = new SolesFragment();
        solesFragment.show(getSupportFragmentManager(),"Soles");
        solesFragment.setCancelable(false);
    }
    TextView totalmonto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ventas);
        totalmonto =  findViewById(R.id.txtimporte);
      //  generarventa.setOnClickListener(new View.OnClickListener() {
                              //              @Override
                                //            public void onClick(View view) {
                                  //              try {
                                      //              PreparedStatement stm = Conexion.conexionDB().prepareStatement(
                                         //                   "UPDATE PUMP_LADOS_MANGUERAS2 SET Valor_Ref = 20.00 WHERE Lado_SVEN = '"+ user.getText().toString() + "' AND Id_Articulo_SVEN = '"+ name.getText().toString() + "'");
                                       //             stm.executeUpdate();
                                       //             Toast.makeText(getApplicationContext(),"Process Successful", Toast.LENGTH_SHORT).show();
                                    //            }catch (Exception exception){
                                    //                Log.e("error",exception.getMessage());
                                  //              }
                                  //          }

                                 //       }

      //  );

        ImageButton configuracion = findViewById(R.id.btnconfiguracion);
        configuracion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    startActivity(new Intent(Ventas.this,MainActivity.class));
            }
        });

        CardView Cara17 = (CardView) findViewById(R.id.cara17);
        CardView Cara18 = (CardView) findViewById(R.id.cara18);
        final TextView textcara = (TextView) findViewById(R.id.textcara);
        Cara17.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    textcara.setText("17");
                }
            });
        Cara18.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    textcara.setText("18");
                }
            });

        CardView diesel = (CardView) findViewById(R.id.diesel);
        CardView gas90 = (CardView) findViewById(R.id.gas90);
        CardView gas95 = (CardView) findViewById(R.id.gas95);
        CardView gas97 = (CardView) findViewById(R.id.gas97);
        final TextView textmanguera = (TextView) findViewById(R.id.textmanguera);

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

        findViewById(R.id.btnboleta).setOnClickListener(v -> boleta());
    }


    private  void boleta(){

        Bitmap logo = Printama.getBitmapFromVector(this, R.drawable.robles_sinfondo);

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("America/Lima"));
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        String FechaHora = sdf.format(cal.getTime());



        TextView textcaras = (TextView) findViewById(R.id.textcara);
        String cara = textcaras.getText().toString();

        TextView textmanguera = (TextView) findViewById(R.id.textmanguera);
        String manguera = textmanguera.getText().toString();

        TextView txtimporte = (TextView) findViewById(R.id.txtimporte);
        String importe =  txtimporte.getText().toString();
        double totalimporte = Double.parseDouble(importe);


        Numero_Letras numToWord = new Numero_Letras();
        String letraimporte = numToWord.Convertir(importe,true);

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
            default:
                Log.d("MyApp", "NULL");
        }


        String finalPrecio = precio;
        double totalprecio = Double.parseDouble(finalPrecio);

        DecimalFormat df = new DecimalFormat("#.###");
        double resultados = Double.parseDouble(df.format(totalimporte/totalprecio ));
        String resultadoscantidad = String.valueOf(resultados);


        Printama.with(this).connect(printama -> {
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
            printama.printTextln("TOTAL VENTA S/: "+ importe, Printama.RIGHT);
            printama.setNormalText();
            printama.printDoubleDashedLine();
            printama.setSmallText();
            printama.printTextln("CONDICION DE PAGO:", Printama.LEFT);
            printama.printTextln("CONTADO S/: "+ importe, Printama.RIGHT);
            printama.printTextln("SON:"+letraimporte, Printama.LEFT);
            printama.setNormalText();
            printama.printDoubleDashedLine();
            printama.feedPaper();
            printama.close();
        });
    }


    @Override
    public void applyTexts(String textsol) {
        totalmonto.setText(textsol+".00");
    }
}