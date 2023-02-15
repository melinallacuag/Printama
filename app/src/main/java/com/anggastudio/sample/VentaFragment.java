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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.anggastudio.sample.Adapter.Cara;
import com.anggastudio.sample.Adapter.CaraAdapter;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class VentaFragment extends Fragment{

    private static final int REQUEST_CODE_PERMISSION = 1;
    TextView totalmonto;

    RecyclerView recyclerCara;
    CaraAdapter caraAdapter;

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

        grias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView operacion   = view.findViewById(R.id.txtoperacion);
                String op            = operacion.getText().toString().trim();
                if (op.equals("03")) {
                    Bundle bundle = new Bundle();
                    bundle.putString("producto", producto.getText().toString());
                    bundle.putString("lado", cara.getText().toString());
                    bundle.putString("importe", importetotal.getText().toString());
                    PrintBoletaFragment printBoletaFragment = new PrintBoletaFragment();
                    printBoletaFragment.setArguments(bundle);
                    printBoletaFragment.show(getActivity().getSupportFragmentManager(), "Boleta");
                    printBoletaFragment.setCancelable(false);
                }else if (op.equals("01")) {
                        Bundle bundle = new Bundle();
                        bundle.putString("producto",producto.getText().toString());
                        bundle.putString("lado",cara.getText().toString());
                        bundle.putString("importe",importetotal.getText().toString());
                        PrintFacturaFragment printFacturaFragment = new PrintFacturaFragment();
                        printFacturaFragment.setArguments(bundle);
                        printFacturaFragment.show(getActivity().getSupportFragmentManager(), "Factura");
                        printFacturaFragment.setCancelable(false);

                }else if (op.equals("99")) {
                        Bundle bundle = new Bundle();
                        bundle.putString("producto",producto.getText().toString());
                        bundle.putString("lado",cara.getText().toString());
                        bundle.putString("importe",importetotal.getText().toString());
                        PrintNotaDespachoFragment printNotaDespachoFragment = new PrintNotaDespachoFragment();
                        printNotaDespachoFragment.setArguments(bundle);
                        printNotaDespachoFragment.show(getActivity().getSupportFragmentManager(), "Nota de Despacho");
                        printNotaDespachoFragment.setCancelable(false);
                }
            }
        });

        //Selección de Cara
        CardView Cara17         = (CardView) view.findViewById(R.id.cara17);
        CardView Cara18         = (CardView) view.findViewById(R.id.cara18);
        final TextView textcara = (TextView) view.findViewById(R.id.textcara);

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
        //Seleccion de Caras
        recyclerCara = view.findViewById(R.id.recycler);
        recyclerCara.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        List<Cara> caraList = new ArrayList<>();

        for (int i=0; i<1; i++){
            caraList.add(new Cara(i,"18",+i));
            caraList.add(new Cara(i,"19",+i));
            caraList.add(new Cara(i,"20",+i));
        }

        caraAdapter = new CaraAdapter(caraList,getContext());
        recyclerCara.setAdapter(caraAdapter);

        return view;
    }
}