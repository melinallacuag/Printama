package com.anggastudio.sample.Fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.anggastudio.printama.Printama;
import com.anggastudio.sample.Adapter.CaraAdapter;
import com.anggastudio.sample.Adapter.CardAdapter;
import com.anggastudio.sample.Adapter.DetalleVentaAdapter;
import com.anggastudio.sample.Adapter.MangueraAdapter;
import com.anggastudio.sample.Adapter.TipoTarjetaAdapter;
import com.anggastudio.sample.Login;
import com.anggastudio.sample.Menu;
import com.anggastudio.sample.R;
import com.anggastudio.sample.WebApiSVEN.Controllers.APIService;
import com.anggastudio.sample.WebApiSVEN.Models.Card;
import com.anggastudio.sample.WebApiSVEN.Models.Cliente;
import com.anggastudio.sample.WebApiSVEN.Models.Company;
import com.anggastudio.sample.WebApiSVEN.Models.DetalleVenta;
import com.anggastudio.sample.WebApiSVEN.Models.Lados;
import com.anggastudio.sample.WebApiSVEN.Models.Picos;
import com.anggastudio.sample.WebApiSVEN.Models.Placa;
import com.anggastudio.sample.WebApiSVEN.Models.Setting;
import com.anggastudio.sample.WebApiSVEN.Models.Tipotarjeta;
import com.anggastudio.sample.WebApiSVEN.Parameters.GlobalInfo;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VentaFragment extends Fragment{

    TextView  producto,cara,importetotal,textcara,textmanguera;
    CardView  grias;
    Button    btnlibre,btnsoles,btngalones,btnboleta,btnfactura,btnnotadespacho,btnserafin,btnpuntos,automatiStop;

    RecyclerView recyclerCara, recyclerManguera, recyclerDetalleVenta;
    CaraAdapter caraAdapter;
    MangueraAdapter mangueraAdapter;
    DetalleVentaAdapter detalleVentaAdapter;

    List<DetalleVenta> detalleVentaList;
    private APIService mAPIService;
    private String mCara;

    boolean mTimerRunning;
    Timer timer;
    TimerTask timerTask;
    final Handler handler = new Handler();

    /* Boleta-Factura*/
    Card cards = null;
    RadioGroup radioGroup;
    Spinner dropStatus;
    TextView modopagoefectivo;
    RadioButton cbefectivo,cbtarjeta,cbcredito,radioButton;
    TextInputEditText  textid,txtplaca,textrazsocial,textdni,textruc,textnombre,textdireccion,textkilometraje,textobservacion,textpagoefectivo,textNroOperacio;
    TextInputLayout alertpefectivo,alertoperacion,alertid,alertplaca,alertdni,alertruc, alertnombre,alertrazsocial,textdropStatus;
    Button btnagregar,btncancelar,btngenerar,buscarplaca,buscardni,buscarruc,buscarid;
    AlertDialog.Builder builder;
    AlertDialog alertDialog;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_venta, container, false);

        mAPIService  = GlobalInfo.getAPIService();

        producto     = view.findViewById(R.id.textmanguera);
        cara         = view.findViewById(R.id.textcara);
        importetotal = view.findViewById(R.id.txtimporte);
        grias        = view.findViewById(R.id.card);

        automatiStop = view.findViewById(R.id.automatiStop);

        automatiStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mTimerRunning) {
                    stoptimertask();
                } else {
                    startTimer();
                }
            }
        });

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

        btnlibre        = view.findViewById(R.id.btnlibre);
        btnsoles        = view.findViewById(R.id.btnsoles);
        btngalones      = view.findViewById(R.id.btngalones);
        btnboleta       = view.findViewById(R.id.btnboleta);
        btnfactura      = view.findViewById(R.id.btnfactura);
        btnnotadespacho = view.findViewById(R.id.btnnotadespacho);
        btnserafin      = view.findViewById(R.id.btnserafin);
        btnpuntos       = view.findViewById(R.id.btnpuntos);

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
                for(DetalleVenta detalleVenta : detalleVentaList){

                    String mnCara = detalleVenta.getCara().toString();

                    if(mnCara.equals(mCara)) {

                        //Abrir Modal de cada Operación
                        builder = new AlertDialog.Builder(getActivity());
                        LayoutInflater inflater = getActivity().getLayoutInflater();
                        View dialogView = inflater.inflate(R.layout.fragment_boleta, null);
                        builder.setView(dialogView);
                        abrirmodal();

                        //Alertas
                        alertplaca       = dialogView.findViewById(R.id.alertPlaca);
                        alertdni         = dialogView.findViewById(R.id.alertDNI);
                        alertnombre      = dialogView.findViewById(R.id.alertNombre);
                        alertpefectivo   = dialogView.findViewById(R.id.alertPEfectivo);
                        alertoperacion   = dialogView.findViewById(R.id.alertOperacion);

                        //Campos
                        txtplaca         = dialogView.findViewById(R.id.inputPlaca);
                        textdni          = dialogView.findViewById(R.id.inputDNI);
                        textnombre       = dialogView.findViewById(R.id.inputNombre);
                        textdireccion    = dialogView.findViewById(R.id.inputDireccion);
                        textkilometraje  = dialogView.findViewById(R.id.inputKilometraje);
                        textobservacion  = dialogView.findViewById(R.id.inputObservacion);
                        textNroOperacio  = dialogView.findViewById(R.id.inputOperacion);
                        textpagoefectivo = dialogView.findViewById(R.id.inputPEfectivo);
                        modopagoefectivo = dialogView.findViewById(R.id.modopagoefectivo);

                        //Radio Button
                        radioGroup       = dialogView.findViewById(R.id.radioformapago);
                        cbefectivo       = dialogView.findViewById(R.id.radioEfectivo);
                        cbtarjeta        = dialogView.findViewById(R.id.radioTarjeta);
                        cbcredito        = dialogView.findViewById(R.id.radioCredito);

                        //Button Spinner
                        dropStatus       = dialogView.findViewById(R.id.dropStatus);
                        textdropStatus   = dialogView.findViewById(R.id.textdropStatus);

                        //Button
                        btnagregar       = dialogView.findViewById(R.id.btnagregarboleta);
                        btncancelar      = dialogView.findViewById(R.id.btncancelar);
                        btngenerar       = dialogView.findViewById(R.id.btngenerarcliente);
                        buscardni        = dialogView.findViewById(R.id.btnrenic);
                        buscarplaca      = dialogView.findViewById(R.id.btnplaca);

                        //Array de los select
                        getCard();

                        //Radio Button de Visiluación de campos
                        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(RadioGroup group, int checkedId) {
                                radioButton = dialogView.findViewById(checkedId);
                                if (checkedId == cbefectivo.getId()){
                                    modopagoefectivo.setVisibility(View.VISIBLE);
                                    textdropStatus.setVisibility(View.GONE);
                                    alertoperacion.setVisibility(View.GONE);
                                    alertpefectivo.setVisibility(View.GONE);
                                } else if (checkedId == cbtarjeta.getId()){
                                    textdropStatus.setVisibility(View.VISIBLE);
                                    alertpefectivo.setVisibility(View.VISIBLE);
                                    alertoperacion.setVisibility(View.VISIBLE);
                                    modopagoefectivo.setVisibility(View.GONE);
                                } else if (checkedId == cbcredito.getId()){
                                    alertpefectivo.setVisibility(View.VISIBLE);
                                    textdropStatus.setVisibility(View.GONE);
                                    alertoperacion.setVisibility(View.GONE);
                                    modopagoefectivo.setVisibility(View.GONE);
                                }
                            }
                        });

                        dropStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                cards = (Card) dropStatus.getSelectedItem();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                            }
                        });

                        btncancelar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                alertDialog.dismiss();
                            }
                        });

                        buscarplaca.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                String getPlacaBoleta = txtplaca.getText().toString();

                                if (getPlacaBoleta.isEmpty()) {
                                    alertplaca.setError("* El campo Placa es obligatorio");
                                }else{
                                    alertplaca.setErrorEnabled(false);
                                    findPlaca(getPlacaBoleta,"03");
                                    textdni.setText("");
                                    textnombre.setText("");
                                    textdireccion.setText("");
                                }
                            }
                        });

                        buscardni.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {

                                String getClienteDni = textdni.getText().toString();

                                if (getClienteDni.isEmpty()) {
                                    alertdni.setError("* El campo DNI es obligatorio");
                                } else {
                                    alertdni.setErrorEnabled(false);
                                    findClienteDNI(getClienteDni);
                                    textnombre.setText("");
                                    textdireccion.setText("");
                                }

                            }
                        });

                        btngenerar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                txtplaca.setText(GlobalInfo.getsettingNroPlaca10);
                                textdni.setText(GlobalInfo.getsettingClienteID10);
                                textnombre.setText(GlobalInfo.getsettingClienteRZ10);
                            }
                        });

                        radioGroup.check(cbefectivo.getId());

                        btnagregar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                String textnplaca          = txtplaca.getText().toString();
                                String textndni            = textdni.getText().toString();
                                String textnnombre         = textnombre.getText().toString();
                                String textnNroOperacio    = textNroOperacio.getText().toString();
                                String textnpagoefectivo   = textpagoefectivo.getText().toString();

                                int checkedRadioButtonId   = radioGroup.getCheckedRadioButtonId();

                                if (textnplaca.isEmpty()) {
                                    alertplaca.setError("* El campo Placa es obligatorio");
                                    return;
                                } else if (textndni.isEmpty()) {
                                    alertdni.setError("* El campo DNI es obligatorio");
                                    return;
                                } else if (textnnombre.isEmpty()) {
                                    alertnombre.setError("* El campo Nombre es obligatorio");
                                    return;
                                } else if (radioGroup.getCheckedRadioButtonId() == -1) {
                                    Toast.makeText(getContext(), "Por favor, seleccione Tipo de Pago", Toast.LENGTH_SHORT).show();
                                    return;
                                } else if (checkedRadioButtonId == cbtarjeta.getId()) {
                                    if (textnNroOperacio.isEmpty()) {
                                        alertoperacion.setError("* El campo Nro Operación es obligatorio");
                                        return;
                                    } else if (textnNroOperacio.length() < 4) {
                                        alertoperacion.setError("* El  Nro Operación debe tener 4 dígitos");
                                        return;
                                    } else if (textnpagoefectivo.isEmpty()) {
                                        alertpefectivo.setError("* El campo Pago Efectivo es obligatorio");
                                        return;
                                    }
                                } else if (checkedRadioButtonId == cbcredito.getId()) {
                                     if (textnpagoefectivo.isEmpty()) {
                                         alertpefectivo.setError("* El campo Pago Efectivo es obligatorio");
                                        return;
                                     }

                                }

                                double value = Double.parseDouble(textnpagoefectivo);
                                DecimalFormat decimalFormat = new DecimalFormat("#.##");

                                alertplaca.setErrorEnabled(false);
                                alertdni.setErrorEnabled(false);
                                alertnombre.setErrorEnabled(false);
                                alertoperacion.setErrorEnabled(false);
                                alertpefectivo.setErrorEnabled(false);

                                detalleVenta.setNroPlaca(txtplaca.getText().toString());
                                detalleVenta.setClienteID(textdni.getText().toString());
                                detalleVenta.setClienteRUC("");
                                detalleVenta.setClienteRS(textnombre.getText().toString());
                                detalleVenta.setClienteDR(textdireccion.getText().toString());
                                detalleVenta.setKilometraje(textkilometraje.getText().toString());
                                detalleVenta.setObservacion(textobservacion.getText().toString());
                                detalleVenta.setTipoPago(radioButton.getText().toString().substring(0,1));

                                String datotipotarjeta =radioButton.getText().toString();

                                detalleVenta.setTarjetaCredito("");
                                detalleVenta.setOperacionREF("");
                                detalleVenta.setMontoSoles(Double.parseDouble(String.valueOf(0)));

                                if (datotipotarjeta.equals("Tarjeta")){
                                    detalleVenta.setTarjetaCredito(String.valueOf(Integer.valueOf(cards.getCardID())));
                                    detalleVenta.setOperacionREF(textNroOperacio.getText().toString());

                                    if( value != Double.parseDouble(decimalFormat.format(value))){
                                        alertpefectivo.setError("* Por favor ingrese un valor con 2 decimales");
                                        return;
                                    }else{
                                        detalleVenta.setMontoSoles(Double.parseDouble(textpagoefectivo.getText().toString()));
                                    }

                                }else if (datotipotarjeta.equals("Credito")) {

                                    if(value != Double.parseDouble(decimalFormat.format(value))){
                                        alertpefectivo.setError("* Por favor ingrese un valor con 2 decimales");
                                        return;
                                    }else{
                                        detalleVenta.setMontoSoles(Double.parseDouble(textpagoefectivo.getText().toString()));
                                    }
                                }

                                recyclerDetalleVenta.setAdapter(detalleVentaAdapter);
                                Toast.makeText(getContext(), "Se agrego correctamente", Toast.LENGTH_SHORT).show();
                                alertDialog.dismiss();

                            }
                        });
                    }
                }
            }
        });

        btnfactura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for(DetalleVenta detalleVenta : detalleVentaList){

                    String mnCara = detalleVenta.getCara().toString();

                    if(mnCara.equals(mCara) ) {

                        //Abrir Modal de cada Operación
                        builder = new AlertDialog.Builder(getActivity());
                        LayoutInflater inflater = getActivity().getLayoutInflater();
                        View dialogView = inflater.inflate(R.layout.fragment_factura, null);
                        builder.setView(dialogView);
                        abrirmodal();

                        //Alertas
                        alertplaca       = dialogView.findViewById(R.id.alertPlaca);
                        alertruc         = dialogView.findViewById(R.id.alertRUC);
                        alertrazsocial   = dialogView.findViewById(R.id.alertRazSocial);
                        alertpefectivo   = dialogView.findViewById(R.id.alertPEfectivo);
                        alertoperacion   = dialogView.findViewById(R.id.alertOperacion);

                        //Campos
                        txtplaca         = dialogView.findViewById(R.id.inputPlaca);
                        textruc          = dialogView.findViewById(R.id.inputRUC);
                        textrazsocial    = dialogView.findViewById(R.id.inputRazSocial);
                        textdireccion    = dialogView.findViewById(R.id.inputDireccion);
                        textkilometraje  = dialogView.findViewById(R.id.inputKilometraje);
                        textobservacion  = dialogView.findViewById(R.id.inputObservacion);
                        textNroOperacio  = dialogView.findViewById(R.id.inputOperacion);
                        textpagoefectivo = dialogView.findViewById(R.id.inputPEfectivo);
                        modopagoefectivo = dialogView.findViewById(R.id.modopagoefectivo);

                        //Radio Button
                        radioGroup     = dialogView.findViewById(R.id.radioformapago);
                        cbefectivo     = dialogView.findViewById(R.id.radioEfectivo);
                        cbtarjeta      = dialogView.findViewById(R.id.radioTarjeta);
                        cbcredito      = dialogView.findViewById(R.id.radioCredito);

                        //Button Spinner
                        dropStatus            = dialogView.findViewById(R.id.dropStatus);
                        textdropStatus        = dialogView.findViewById(R.id.textdropStatus);

                        //Button
                        btnagregar     = dialogView.findViewById(R.id.btnagregarboleta);
                        btncancelar    = dialogView.findViewById(R.id.btncancelar);
                        buscarruc      = dialogView.findViewById(R.id.btnsunat);
                        buscarplaca    = dialogView.findViewById(R.id.btnplaca);

                        //Array de los select
                        getCard();

                        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(RadioGroup group, int checkedId) {
                                radioButton = dialogView.findViewById(checkedId);

                                if (checkedId == cbefectivo.getId()){
                                    modopagoefectivo.setVisibility(View.VISIBLE);
                                    textdropStatus.setVisibility(View.GONE);
                                    alertoperacion.setVisibility(View.GONE);
                                    alertpefectivo.setVisibility(View.GONE);
                                } else if (checkedId == cbtarjeta.getId()){
                                    textdropStatus.setVisibility(View.VISIBLE);
                                    alertpefectivo.setVisibility(View.VISIBLE);
                                    alertoperacion.setVisibility(View.VISIBLE);
                                    modopagoefectivo.setVisibility(View.GONE);
                                } else if (checkedId == cbcredito.getId()){
                                    alertpefectivo.setVisibility(View.VISIBLE);
                                    textdropStatus.setVisibility(View.GONE);
                                    alertoperacion.setVisibility(View.GONE);
                                    modopagoefectivo.setVisibility(View.GONE);
                                }
                            }
                        });

                        dropStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                cards = (Card) dropStatus.getSelectedItem();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                            }
                        });

                        btncancelar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                alertDialog.dismiss();
                            }
                        });

                        buscarplaca.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                String getPlacaFactura = txtplaca.getText().toString();

                                if (getPlacaFactura.isEmpty()) {
                                    alertplaca.setError("* El campo Placa es obligatorio");
                                } else {
                                    alertplaca.setErrorEnabled(false);
                                    findPlaca(getPlacaFactura,"01");
                                    textruc.setText("");
                                    textrazsocial.setText("");
                                    textdireccion.setText("");
                                }

                            }
                        });

                        buscarruc.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                String getClienteRuc    = textruc.getText().toString().trim();

                                if(getClienteRuc.isEmpty()){
                                    alertruc.setError("* El campo RUC es obligatorio");
                                }else{
                                    alertruc.setErrorEnabled(false);
                                    findClienteRUC(getClienteRuc);
                                    textrazsocial.setText("");
                                    textdireccion.setText("");
                                }
                            }
                        });

                        radioGroup.check(cbefectivo.getId());

                        btnagregar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                String textnplaca         = txtplaca.getText().toString();
                                String textnruc           = textruc.getText().toString();
                                String textnnombre        = textrazsocial.getText().toString();
                                String textnNroOperacio   = textNroOperacio.getText().toString();
                                String textnpagoefectivo  = textpagoefectivo.getText().toString();
                                int checkedRadioButtonId  = radioGroup.getCheckedRadioButtonId();

                                if (textnplaca.isEmpty()) {
                                    alertplaca.setError("* El campo Placa es obligatorio");
                                    return;
                                } else if (textnruc.isEmpty()) {
                                    alertruc.setError("* El campo DNI es obligatorio");
                                    return;
                                } else if (textnnombre.isEmpty()) {
                                    alertrazsocial.setError("* El campo Nombre es obligatorio");
                                    return;
                                }else if (radioGroup.getCheckedRadioButtonId() == -1){
                                    Toast.makeText(getContext(), "Por favor, seleccione Tipo de Pago", Toast.LENGTH_SHORT).show();
                                    return;
                                } else if (textnpagoefectivo.isEmpty()){
                                    alertpefectivo.setError("* El campo Pago Efectivo es obligatorio");
                                    return;
                                } else if (checkedRadioButtonId == cbtarjeta.getId()) {
                                    if (textnNroOperacio.isEmpty()) {
                                        alertoperacion.setError("* El campo Nro Operación es obligatorio");
                                        return;
                                    } else if(textnNroOperacio.length() < 4){
                                        alertoperacion.setError("* El  Nro Operación debe tener 4 dígitos");
                                        return;
                                    } else if(textnpagoefectivo.isEmpty()) {
                                        alertpefectivo.setError("* El campo Pago Efectivo es obligatorio");
                                        return;
                                    }
                                }else if (checkedRadioButtonId == cbcredito.getId()) {
                                    if(textnpagoefectivo.isEmpty()) {
                                        alertpefectivo.setError("* El campo Pago Efectivo es obligatorio");
                                        return;
                                    }
                                }

                                double value = Double.parseDouble(textnpagoefectivo);
                                DecimalFormat decimalFormat = new DecimalFormat("#.##");

                                alertplaca.setErrorEnabled(false);
                                alertruc.setErrorEnabled(false);
                                alertrazsocial.setErrorEnabled(false);
                                alertoperacion.setErrorEnabled(false);
                                alertpefectivo.setErrorEnabled(false);

                                detalleVenta.setNroPlaca(txtplaca.getText().toString());
                                detalleVenta.setClienteID("");
                                detalleVenta.setClienteRUC(textruc.getText().toString());
                                detalleVenta.setClienteRS(textrazsocial.getText().toString());
                                detalleVenta.setClienteDR(textdireccion.getText().toString());
                                detalleVenta.setKilometraje(textkilometraje.getText().toString());
                                detalleVenta.setObservacion(textobservacion.getText().toString());
                                detalleVenta.setTipoPago(radioButton.getText().toString().substring(0,1));

                                String datotipotarjeta =radioButton.getText().toString();

                                detalleVenta.setTarjetaCredito("");
                                detalleVenta.setOperacionREF("");
                                detalleVenta.setMontoSoles(Double.parseDouble(String.valueOf(0)));

                                    if (datotipotarjeta.equals("Tarjeta")){

                                        detalleVenta.setTarjetaCredito(String.valueOf(Integer.valueOf(cards.getCardID())));
                                        detalleVenta.setOperacionREF(textNroOperacio.getText().toString());

                                        if(value != Double.parseDouble(decimalFormat.format(value))){
                                            alertpefectivo.setError("* Por favor ingrese un valor con dos decimales solamente");
                                            return;
                                        }else{
                                            detalleVenta.setMontoSoles(Double.valueOf(textpagoefectivo.getText().toString()));
                                        }

                                    }else if (datotipotarjeta.equals("Credito")) {

                                        if(value != Double.parseDouble(decimalFormat.format(value))){
                                            alertpefectivo.setError("* Por favor ingrese un valor con dos decimales solamente");
                                            return;
                                        }else{
                                            detalleVenta.setMontoSoles(Double.valueOf(textpagoefectivo.getText().toString()));
                                        }

                                    }

                                    recyclerDetalleVenta.setAdapter(detalleVentaAdapter);
                                    Toast.makeText(getContext(), "Se agrego correctamente", Toast.LENGTH_SHORT).show();
                                    alertDialog.dismiss();
                            }
                        });
                    }
                }
            }
        });

        btnnotadespacho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for (DetalleVenta detalleVenta : detalleVentaList){
                    String mnCara = detalleVenta.getCara().toString();
                    if(mnCara.equals(mCara) ) {

                        builder = new AlertDialog.Builder(getActivity());
                        LayoutInflater inflater = getActivity().getLayoutInflater();
                        View dialogView = inflater.inflate(R.layout.fragment_nota_despacho, null);
                        builder.setView(dialogView);
                        abrirmodal();

                        txtplaca        = dialogView.findViewById(R.id.inputnplaca);
                        textid          = dialogView.findViewById(R.id.inputid);
                        textruc         = dialogView.findViewById(R.id.inputruc);
                        textnombre      = dialogView.findViewById(R.id.inputnombre);
                        textdireccion   = dialogView.findViewById(R.id.inputdireccion);
                        textkilometraje = dialogView.findViewById(R.id.inputkilometraje);
                        textobservacion = dialogView.findViewById(R.id.inputobservacion);

                        btnagregar      = dialogView.findViewById(R.id.btnagregarboleta);
                        btncancelar     = dialogView.findViewById(R.id.btncancelarboleta);
                        buscarid        = dialogView.findViewById(R.id.btnsunat);
                        buscarplaca     = dialogView.findViewById(R.id.btnplaca);

                        alertplaca      = dialogView.findViewById(R.id.textnplaca);
                        alertid         = dialogView.findViewById(R.id.textid);
                        alertruc        = dialogView.findViewById(R.id.textruc);
                        alertnombre     = dialogView.findViewById(R.id.textnombre);

                        btncancelar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                alertDialog.dismiss();
                            }
                        });

                        buscarplaca.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                builder = new AlertDialog.Builder(getActivity());
                                LayoutInflater inflater = getActivity().getLayoutInflater();
                                View dialogView = inflater.inflate(R.layout.fragment_clientes, null);
                                builder.setView(dialogView);
                                abrirmodal();

                                btncancelar    = dialogView.findViewById(R.id.btncancelar);

                                btncancelar.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        alertDialog.dismiss();
                                    }
                                });

                            }
                        });

                        buscarid.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                String getClienteId    = textid.getText().toString().trim();
                                if(getClienteId.isEmpty()){
                                    alertid.setError("* El campo RUC es obligatorio");
                                }else{
                                    alertid.setErrorEnabled(false);
                                    findCliente(getClienteId,"99");
                                    textruc.setText("");
                                    textnombre.setText("");
                                    textdireccion.setText("");
                                }
                            }
                        });

                        btnagregar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String textnplaca     = txtplaca.getText().toString();
                                String textndni       = textid.getText().toString();
                                String textnruc       = textruc.getText().toString();
                                String textnnombre    = textnombre.getText().toString();

                                if (textnplaca.isEmpty()) {
                                    alertplaca.setError("* El campo Placa es obligatorio");
                                } else if (textndni.isEmpty()) {
                                    alertid.setError("* El campo DNI es obligatorio");
                                }else if (textnruc.isEmpty()) {
                                    alertruc.setError("* El campo RUC es obligatorio");
                                } else if (textnnombre.isEmpty()) {
                                    alertnombre.setError("* El campo Nombre es obligatorio");
                                } else {
                                    alertplaca.setErrorEnabled(false);
                                    alertid.setErrorEnabled(false);
                                    alertruc.setErrorEnabled(false);
                                    alertnombre.setErrorEnabled(false);

                                    detalleVenta.setNroPlaca(txtplaca.getText().toString());
                                    detalleVenta.setClienteID(textid.getText().toString());
                                    detalleVenta.setClienteRUC(textruc.getText().toString());
                                    detalleVenta.setClienteRS(textnombre.getText().toString());
                                    detalleVenta.setClienteDR(textdireccion.getText().toString());
                                    detalleVenta.setKilometraje(textkilometraje.getText().toString());
                                    detalleVenta.setObservacion(textobservacion.getText().toString());
                                    detalleVenta.setTipoPago("C");

                                    recyclerDetalleVenta.setAdapter(detalleVentaAdapter);
                                    Toast.makeText(getContext(), "Se agrego correctamente", Toast.LENGTH_SHORT).show();
                                    alertDialog.dismiss();
                                }
                            }
                        });
                    }
                }
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

        //Listado de Dettalles de Venta
        recyclerDetalleVenta = view.findViewById(R.id.recyclerdetalleventa);
        recyclerDetalleVenta.setLayoutManager(new LinearLayoutManager(getContext()));

        //Listado de Mangueras
        recyclerManguera = view.findViewById(R.id.recyclerlado);
        recyclerManguera.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        //Listado de Caras
        recyclerCara = view.findViewById(R.id.recycler);
        recyclerCara.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        findLados(GlobalInfo.getterminalImei10);
        findDetalleVenta(GlobalInfo.getterminalImei10);

        findSetting(GlobalInfo.getterminalCompanyID10);

        return view;
    }

    private void startTimer() {

        timer = new Timer();

        actualizarVista();

        timer.schedule(timerTask,  5000);
    }

    public void stoptimertask() {

        timer.cancel();
        mTimerRunning = false;
        automatiStop.setText("Stop");
    }

    private void actualizarVista() {

        Toast.makeText(getContext(), "hola", Toast.LENGTH_SHORT).show();

        timerTask = new TimerTask() {
            public void run() {

                handler.post(new Runnable() {
                    public void run() {

                        boletas(NameCompany,RUCCompany,AddressCompany,BranchCompany,TurnoTerminal,CajeroTerminal);

                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd:MMMM:yyyy HH:mm:ss a");
                        final String strDate = simpleDateFormat.format(calendar.getTime());

                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(getContext(), strDate, duration);
                        toast.show();

                    }
                });
            }
        };

        mTimerRunning = true;
        automatiStop.setText("Automatico");
    }

    /**
     * Datos de la Company
     */
    String NameCompany    = GlobalInfo.getNameCompany10;
    String RUCCompany     = GlobalInfo.getRucCompany10;
    String AddressCompany = GlobalInfo.getAddressCompany10;
    String BranchCompany  = GlobalInfo.getBranchCompany10;

    /**
     * Datos de la terminal
     */
    String TurnoTerminal  = String.valueOf(GlobalInfo.getterminalTurno10);
    String CajeroTerminal = GlobalInfo.getuserName10;

    private  void boletas(String NameCompany, String RUCCompany,String AddressCompany, String BranchCompany, String TurnoTerminal,String CajeroTerminal) {

        //Logo
        Bitmap logo = Printama.getBitmapFromVector(getContext(), R.drawable.logoroble);

        /**
         * Organizar la cadena de texto de Address y Branch
         */
        Matcher matcher;
        Pattern patronsintaxi;

        patronsintaxi = Pattern.compile("(?<!\\S)\\p{Lu}+\\.? \\w+ - \\w+ - \\w+(\\.? \\d+)?(?!\\S)");
        matcher = patronsintaxi.matcher(AddressCompany);

        String segundaAddress = null;
        String primeraAddress = null;
        if (matcher.find()) {
            segundaAddress    = matcher.group();
            String[] partes   = AddressCompany.split(segundaAddress);
            primeraAddress    = partes[0].trim();
            segundaAddress    = segundaAddress.trim();
        }

        String AddressU = segundaAddress;
        String AddressD = primeraAddress;


        matcher = patronsintaxi.matcher(BranchCompany);

        String segundaBranch = null;
        String primeraBranch = null;
        if (matcher.find()) {
            segundaBranch    = matcher.group();
            String[] partes  = BranchCompany.split(segundaBranch);
            primeraBranch    = partes[0].trim();
            segundaBranch    = segundaBranch.trim();
        }

        String BranchU = segundaBranch;
        String BranchD = primeraBranch;

        /**
         * Fin - Organizar la cadena de texto de Address y Branch
         */


        /**
         * Fecha y Hora que se imite el comprobante
         */
        Calendar cal          = Calendar.getInstance(TimeZone.getTimeZone("America/Lima"));
        SimpleDateFormat sdf  = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        String FechaHora      = sdf.format(cal.getTime());
        /**
         * Fin - Fecha y Hora que se imite el comprobante
         */

        Printama.with(getContext()).connect(printama -> {
            printama.setNormalText();
            printama.printTextlnBold(NameCompany, Printama.CENTER);
            printama.printTextlnBold("PRINCIPAL: " + AddressD, Printama.CENTER);
            printama.printTextlnBold(AddressU, Printama.CENTER);
            printama.printTextlnBold("SUCURSAL: " + BranchD, Printama.CENTER);
            printama.printTextlnBold(BranchU, Printama.CENTER);
            printama.printTextlnBold("RUC: " + RUCCompany, Printama.CENTER);
            printama.printTextlnBold("FACTURA DE VENTA ELECTRONICA", Printama.CENTER);
            printama.printTextlnBold("0000000",Printama.CENTER);
            printama.setSmallText();
            printama.printDoubleDashedLine();
            printama.addNewLine(1);
            printama.setSmallText();
            printama.printTextln("Fecha - Hora : "+ FechaHora + "   Turno: "+ TurnoTerminal,Printama.LEFT);
            printama.printTextln("Cajero : "+ CajeroTerminal , Printama.LEFT);
            printama.printTextln("Lado   :01 ", Printama.LEFT);
            printama.setSmallText();
            printama.printDoubleDashedLine();
            printama.addNewLine(1);
            printama.feedPaper();
            printama.close();
        }, this::showToast);

    }

    private void showToast(String message) {
        Toast.makeText(getContext(), "Conectar Bluetooth", Toast.LENGTH_SHORT).show();
    }

    private void findSetting(Integer id){

        Call<List<Setting>> call = mAPIService.findSetting(id);

        call.enqueue(new Callback<List<Setting>>() {
            @Override
            public void onResponse(Call<List<Setting>> call, Response<List<Setting>> response) {
                try {

                    if(!response.isSuccessful()){
                        Toast.makeText(getContext(), "Codigo de error: " + response.code(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    List<Setting> settingList = response.body();
                    for(Setting setting: settingList) {
                        GlobalInfo.getsettingCompanyId10       = Integer.valueOf(setting.getCompanyID());
                        GlobalInfo.getsettingTituloApp10       = String.valueOf(setting.getTituloApp());
                        GlobalInfo.getsettingFuelName10        = String.valueOf(setting.getFuel_Name());
                        GlobalInfo.getsettingFuelGrupoID10     = String.valueOf(setting.getFuel_GrupoID());
                        GlobalInfo.getsettingFuelLados10       = Integer.valueOf(setting.getFuel_Lados());
                        GlobalInfo.getsettingFuelMontoMinimo10 = Double.valueOf(setting.getFuel_Monto_Minimo());
                        GlobalInfo.getsettingImpuestoID110     = Integer.valueOf(setting.getImpuestoID1());
                        GlobalInfo.getsettingImpuestoValor110  = Integer.valueOf(setting.getImpuesto_Valor1());
                        GlobalInfo.getsettingImpuestoID210     = Integer.valueOf(setting.getImpuestoID2());
                        GlobalInfo.getsettingImpuestoValor210  = Integer.valueOf(setting.getImpuesto_Valor2());
                        GlobalInfo.getsettingMonedaID10        = String.valueOf(setting.getMonedaID());
                        GlobalInfo.getsettingMonedaValor10     = String.valueOf(setting.getMoneda_Valor());
                        GlobalInfo.getsettingClienteID10       = String.valueOf(setting.getClienteID());
                        GlobalInfo.getsettingClienteRZ10       = String.valueOf(setting.getClienteRZ());
                        GlobalInfo.getsettingNroPlaca10        = String.valueOf(setting.getNroplaca());
                        GlobalInfo.getsettingDNIMontoMinimo10  = Double.valueOf(setting.getDnI_Monto_Minimo());
                        GlobalInfo.getsettingtimerAppVenta10   = String.valueOf(setting.getTimerAppVenta());
                    }

                }catch (Exception ex){
                    Toast.makeText(getContext(),ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Setting>> call, Throwable t) {
                Toast.makeText(getContext(), "Error de conexión APICORE Cliente - RED - WIFI", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private  void findClienteDNI(String id){

        Call<List<Cliente>> call = mAPIService.findClienteDNI(id);

        call.enqueue(new Callback<List<Cliente>>() {
            @Override
            public void onResponse(Call<List<Cliente>> call, Response<List<Cliente>> response) {
                try {

                    if(!response.isSuccessful()){
                        Toast.makeText(getContext(), "Codigo de error: " + response.code(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    List<Cliente> clienteList = response.body();

                    if (clienteList == null || clienteList.isEmpty()) {
                        if (textdni.length() < 8){
                            alertdni.setError("* El DNI debe tener 8 dígitos");
                        }else{
                            alertdni.setError("* No se encontró ningún DNI");
                        }
                        return;
                    }

                    Cliente cliente = clienteList.get(0);

                    GlobalInfo.getclienteId10  = String.valueOf(cliente.getClienteID());
                    GlobalInfo.getclienteRUC10 = String.valueOf(cliente.getClienteRUC());
                    GlobalInfo.getclienteRZ10  = String.valueOf(cliente.getClienteRZ());
                    GlobalInfo.getclienteDR10  = String.valueOf(cliente.getClienteDR());

                    textnombre.setText(GlobalInfo.getclienteRZ10);
                    textdireccion.setText(GlobalInfo.getclienteDR10);

                }catch (Exception ex){
                    Toast.makeText(getContext(),ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Cliente>> call, Throwable t) {
                Toast.makeText(getContext(), "Error de conexión APICORE Cliente - RED - WIFI", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private  void findClienteRUC(String id){

        Call<List<Cliente>> call = mAPIService.findClienteRUC(id);

        call.enqueue(new Callback<List<Cliente>>() {
            @Override
            public void onResponse(Call<List<Cliente>> call, Response<List<Cliente>> response) {
                try {

                    if(!response.isSuccessful()){
                        Toast.makeText(getContext(), "Codigo de error: " + response.code(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    List<Cliente> clienteList = response.body();

                    if (clienteList == null || clienteList.isEmpty()) {
                        if (textruc.length() < 11){
                            alertruc.setError("* El RUC debe tener 11 dígitos");
                        }else{
                            alertruc.setError("* No se encontró ningún RUC");
                        }
                        return;
                    }

                    Cliente cliente = clienteList.get(0);

                    GlobalInfo.getclienteId10  = String.valueOf(cliente.getClienteID());
                    GlobalInfo.getclienteRUC10 = String.valueOf(cliente.getClienteRUC());
                    GlobalInfo.getclienteRZ10  = String.valueOf(cliente.getClienteRZ());
                    GlobalInfo.getclienteDR10  = String.valueOf(cliente.getClienteDR());

                    textrazsocial.setText(GlobalInfo.getclienteRZ10);
                    textdireccion.setText(GlobalInfo.getclienteDR10);

                }catch (Exception ex){
                    Toast.makeText(getContext(),ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Cliente>> call, Throwable t) {
                Toast.makeText(getContext(), "Error de conexión APICORE Cliente - RED - WIFI", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private  void findCliente(String id,String tipodoc){

        Call<List<Cliente>> call = mAPIService.findCliente(id);

        call.enqueue(new Callback<List<Cliente>>() {
            @Override
            public void onResponse(Call<List<Cliente>> call, Response<List<Cliente>> response) {
                try {

                    if(!response.isSuccessful()){
                        Toast.makeText(getContext(), "Codigo de error: " + response.code(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    List<Cliente> clienteList = response.body();

                    if (clienteList == null || clienteList.isEmpty()) {

                        if (tipodoc == "01"){
                            if (textruc.length() < 11){
                                alertruc.setError("* El RUC debe tener 11 dígitos");
                            }else{
                                alertruc.setError("* No se encontró ningún RUC");
                            }
                        }else if (tipodoc == "03"){
                            if (textdni.length() < 8){
                                alertdni.setError("* El DNI debe tener 8 dígitos");
                            }else{
                                alertdni.setError("* No se encontró ningún DNI");
                            }
                        }else if (tipodoc == "99") {
                            alertid.setError("* No se encontró ningún ID");
                        }

                        return;
                    }

                    Cliente cliente = clienteList.get(0);

                    GlobalInfo.getclienteId10  = String.valueOf(cliente.getClienteID());
                    GlobalInfo.getclienteRUC10 = String.valueOf(cliente.getClienteRUC());
                    GlobalInfo.getclienteRZ10  = String.valueOf(cliente.getClienteRZ());
                    GlobalInfo.getclienteDR10  = String.valueOf(cliente.getClienteDR());

                    if (tipodoc == "01"){
                        textrazsocial.setText(GlobalInfo.getclienteRZ10);
                        textdireccion.setText(GlobalInfo.getclienteDR10);
                    }else if (tipodoc == "03"){
                        textnombre.setText(GlobalInfo.getclienteRZ10);
                        textdireccion.setText(GlobalInfo.getclienteDR10);
                    }else if (tipodoc == "99"){
                        textruc.setText( GlobalInfo.getclienteRUC10);
                        textnombre.setText(GlobalInfo.getclienteRZ10);
                        textdireccion.setText(GlobalInfo.getclienteDR10);
                    }

                }catch (Exception ex){
                    Toast.makeText(getContext(),ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<List<Cliente>> call, Throwable t) {
                Toast.makeText(getContext(), "Error de conexión APICORE Cliente - RED - WIFI", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void findPlaca(String id, String tipodoc){

        Call<List<Placa>> call = mAPIService.findPlaca(id);

        call.enqueue(new Callback<List<Placa>>() {
            @Override
            public void onResponse(Call<List<Placa>> call, Response<List<Placa>> response) {

                try {

                    if(!response.isSuccessful()){
                        Toast.makeText(getContext(), "Codigo de error: " + response.code(), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    List<Placa> placaList = response.body();

                    if (placaList == null || placaList.isEmpty()) {
                        alertplaca.setError("* No se encontró ningúna Placa");
                        return;
                    }

                    Placa placa = placaList.get(0);

                    GlobalInfo.getNroPlaca10 = String.valueOf(placa.getNroPlaca());
                    GlobalInfo.getplacaClienteID10 = String.valueOf(placa.getClienteID());
                    GlobalInfo.getplacaClienteRZ10 = String.valueOf(placa.getClienteRZ());
                    GlobalInfo.getplacaClienteDR10 = String.valueOf(placa.getClienteDR());

                    if (tipodoc == "01"){
                        textruc.setText( GlobalInfo.getplacaClienteID10);
                        textrazsocial.setText(GlobalInfo.getplacaClienteRZ10);
                        textdireccion.setText(GlobalInfo.getplacaClienteDR10);
                    }else if (tipodoc == "03"){
                        textdni.setText(GlobalInfo.getplacaClienteID10);
                        textnombre.setText(GlobalInfo.getplacaClienteRZ10);
                        textdireccion.setText(GlobalInfo.getplacaClienteDR10);
                    }else if (tipodoc == "99"){
                        textid.setText( GlobalInfo.getplacaClienteID10);
                        textnombre.setText(GlobalInfo.getplacaClienteRZ10);
                        textdireccion.setText(GlobalInfo.getplacaClienteDR10);
                    }

                }catch (Exception ex){
                    Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Placa>> call, Throwable t) {
                Toast.makeText(getContext(), "Error de conexión APICORE Placa - RED - WIFI", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getCard(){
        Call<List<Card>> call = mAPIService.getCard();

        call.enqueue(new Callback<List<Card>>() {
            @Override
            public void onResponse(Call<List<Card>> call, Response<List<Card>> response) {
                try {

                    if(!response.isSuccessful()){
                        Toast.makeText(getContext(), "Codigo de error: " + response.code(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    List<Card> cardlist = response.body();

                    Resources res = getResources();
                    CardAdapter card = new CardAdapter(getContext(), R.layout.item, (ArrayList<Card>) cardlist, res);
                    dropStatus.setAdapter(card);

                }catch (Exception ex){
                    Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Card>> call, Throwable t) {
                Toast.makeText(getContext(), "Error de conexión APICORE Card - RED - WIFI", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void findLados(String id) {

        Call<List<Lados>> call = mAPIService.findLados(id);

        call.enqueue(new Callback<List<Lados>>() {
            @Override
            public void onResponse(Call<List<Lados>> call, Response<List<Lados>> response) {
                try {

                    if(!response.isSuccessful()){
                        Toast.makeText(getContext(), "Codigo de error: " + response.code(), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    List<Lados> ladosList = response.body();

                    caraAdapter = new CaraAdapter(ladosList, getContext(), new CaraAdapter.OnItemClickListener() {
                        @Override
                        public int onItemClick(Lados item) {

                            GlobalInfo.getCara10 = item.getNroLado();

                            mCara = item.getNroLado();

                            findPico(GlobalInfo.getCara10);

                            textcara =  getActivity().findViewById(R.id.textcara);
                            String numlado = item.getNroLado();
                            textcara.setText(numlado);

                            return 0;
                        }
                    });

                    recyclerCara.setAdapter(caraAdapter);

                }catch (Exception ex){
                    Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Lados>> call, Throwable t) {
                Toast.makeText(getContext(), "Error de conexión APICORE Cara - RED - WIFI", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void findPico(String id){

        Call<List<Picos>> call = mAPIService.findPico(id);

        call.enqueue(new Callback<List<Picos>>() {
            @Override
            public void onResponse(Call<List<Picos>> call, Response<List<Picos>> response) {
                try {

                    if(!response.isSuccessful()){
                        Toast.makeText(getContext(), "Codigo de error: " + response.code(), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    List<Picos> picosList = response.body();
                    mangueraAdapter = new MangueraAdapter(picosList, getContext(), new MangueraAdapter.OnItemClickListener(){
                        @Override
                        public void onItemClick(Picos item) {

                            textmanguera =  getActivity().findViewById(R.id.textmanguera);
                            String descripcionmanguera = item.getDescripcion();
                            textmanguera.setText(descripcionmanguera);
                            GlobalInfo.getPistola10 = item.getMangueraID();

                        }
                    });
                    recyclerManguera.setAdapter(mangueraAdapter);

                }catch (Exception ex){
                    Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Picos>> call, Throwable t) {
                Toast.makeText(getContext(), "Error de conexión APICORE Pico - RED - WIFI", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void findDetalleVenta(String id){

        Call<List<DetalleVenta>> call = mAPIService.findDetalleVenta(id);

        call.enqueue(new Callback<List<DetalleVenta>>() {
            @Override
            public void onResponse(Call<List<DetalleVenta>> call, Response<List<DetalleVenta>> response) {
                try {

                    if(!response.isSuccessful()){
                        Toast.makeText(getContext(), "Codigo de error: " + response.code(), Toast.LENGTH_SHORT).show();
                        return;
                    }

                 //   List<DetalleVenta> detalleVentaList = response.body();

                    detalleVentaList = response.body();

                    detalleVentaAdapter = new DetalleVentaAdapter(detalleVentaList, getContext(), new DetalleVentaAdapter.OnItemClickListener() {
                        @Override
                        public int onItemClick(DetalleVenta item) {
                            return 0;
                        }
                    });
                    recyclerDetalleVenta.setAdapter(detalleVentaAdapter);

                }catch (Exception ex){
                    Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<DetalleVenta>> call, Throwable t) {
                Toast.makeText(getContext(), "Error de conexión APICORE Detalle Venta - RED - WIFI", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void abrirmodal(){
        alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
        alertDialog.setCancelable(false);
    }
}