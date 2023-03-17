package com.anggastudio.sample.Fragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.AutomaticZenRule;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
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
import com.anggastudio.sample.Numero_Letras;
import com.anggastudio.sample.R;
import com.anggastudio.sample.WebApiSVEN.Controllers.APIService;
import com.anggastudio.sample.WebApiSVEN.Models.Card;
import com.anggastudio.sample.WebApiSVEN.Models.Cliente;
import com.anggastudio.sample.WebApiSVEN.Models.Company;
import com.anggastudio.sample.WebApiSVEN.Models.Correlativo;
import com.anggastudio.sample.WebApiSVEN.Models.DetalleVenta;
import com.anggastudio.sample.WebApiSVEN.Models.Lados;
import com.anggastudio.sample.WebApiSVEN.Models.Optran;
import com.anggastudio.sample.WebApiSVEN.Models.Picos;
import com.anggastudio.sample.WebApiSVEN.Models.Placa;
import com.anggastudio.sample.WebApiSVEN.Models.Setting;
import com.anggastudio.sample.WebApiSVEN.Models.SettingTask;
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
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VentaFragment extends Fragment{

    private APIService mAPIService;

    /**  Id Cara */
    private String mCara;

    private CaraAdapter opcionSeleccionada1 = null;
    private MangueraAdapter opcionSeleccionada2 = null;


    /**  Atributos de la Venta */
    TextView  producto,cara,importetotal;
    Button    btnlibre,btnsoles,btngalones,btnboleta,btnfactura,btnnotadespacho,btnserafin,btnpuntos,automatiStop;

    /**  AdapterList - Recycler */
    RecyclerView recyclerCara, recyclerManguera, recyclerDetalleVenta;
    CaraAdapter caraAdapter;
    MangueraAdapter mangueraAdapter;
    DetalleVentaAdapter detalleVentaAdapter;
    List<DetalleVenta> detalleVentaList;

    /** Time Task */
    boolean mTimerRunning;
    Timer timer;
    TimerTask timerTask;
    final Handler handler = new Handler();

    /** Datos de la Boleta - Factura */
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

        automatiStop    = view.findViewById(R.id.automatiStop);
        btnlibre        = view.findViewById(R.id.btnlibre);
        btnsoles        = view.findViewById(R.id.btnsoles);
        btngalones      = view.findViewById(R.id.btngalones);
        btnboleta       = view.findViewById(R.id.btnboleta);
        btnfactura      = view.findViewById(R.id.btnfactura);
        btnnotadespacho = view.findViewById(R.id.btnnotadespacho);
        btnserafin      = view.findViewById(R.id.btnserafin);
        btnpuntos       = view.findViewById(R.id.btnpuntos);

        /** Boton Time Task */
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

        btnlibre.setEnabled(false);
        btnsoles.setEnabled(false);
        btngalones.setEnabled(false);

        /** Modalidad Libre */
        btnlibre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LibreFragment libreFragment = new LibreFragment();
                libreFragment.show(getActivity().getSupportFragmentManager(), "Libre");
                libreFragment.setCancelable(false);
            }
        });

        /** Modalidad Soles */
        btnsoles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    SolesFragment solesFragment = new SolesFragment();
                    solesFragment.show(getActivity().getSupportFragmentManager(), "Soles");
                    solesFragment.setCancelable(false);

            }
        });

        /** Modalidad Galones */
        btngalones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GalonesFragment galonesFragment = new GalonesFragment();
                galonesFragment.show(getActivity().getSupportFragmentManager(), "Galones");
                galonesFragment.setCancelable(false);
            }
        });

        /** Operación de Boleta */
        btnboleta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                for(DetalleVenta detalleVenta : detalleVentaList){

                    String mnCara = detalleVenta.getCara().toString();

                    if(mCara == null){
                        Toast.makeText(getContext(),"Seleccionar Cara",Toast.LENGTH_SHORT).show();
                    }else if(mnCara.equals(mCara)) {

                        /** Abrir Modal */
                        builder = new AlertDialog.Builder(getActivity());
                        LayoutInflater inflater = getActivity().getLayoutInflater();
                        View dialogView = inflater.inflate(R.layout.fragment_boleta, null);
                        builder.setView(dialogView);
                        abrirmodal();

                        alertplaca       = dialogView.findViewById(R.id.alertPlaca);
                        alertdni         = dialogView.findViewById(R.id.alertDNI);
                        alertnombre      = dialogView.findViewById(R.id.alertNombre);
                        alertpefectivo   = dialogView.findViewById(R.id.alertPEfectivo);
                        alertoperacion   = dialogView.findViewById(R.id.alertOperacion);

                        txtplaca         = dialogView.findViewById(R.id.inputPlaca);
                        textdni          = dialogView.findViewById(R.id.inputDNI);
                        textnombre       = dialogView.findViewById(R.id.inputNombre);
                        textdireccion    = dialogView.findViewById(R.id.inputDireccion);
                        textkilometraje  = dialogView.findViewById(R.id.inputKilometraje);
                        textobservacion  = dialogView.findViewById(R.id.inputObservacion);
                        textNroOperacio  = dialogView.findViewById(R.id.inputOperacion);
                        textpagoefectivo = dialogView.findViewById(R.id.inputPEfectivo);
                        modopagoefectivo = dialogView.findViewById(R.id.modopagoefectivo);

                        radioGroup       = dialogView.findViewById(R.id.radioformapago);
                        cbefectivo       = dialogView.findViewById(R.id.radioEfectivo);
                        cbtarjeta        = dialogView.findViewById(R.id.radioTarjeta);
                        cbcredito        = dialogView.findViewById(R.id.radioCredito);

                        dropStatus       = dialogView.findViewById(R.id.dropStatus);
                        textdropStatus   = dialogView.findViewById(R.id.textdropStatus);

                        btnagregar       = dialogView.findViewById(R.id.btnagregarboleta);
                        btncancelar      = dialogView.findViewById(R.id.btncancelar);
                        btngenerar       = dialogView.findViewById(R.id.btngenerarcliente);
                        buscardni        = dialogView.findViewById(R.id.btnrenic);
                        buscarplaca      = dialogView.findViewById(R.id.btnplaca);


                        txtplaca.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                String inputText = s.toString().toUpperCase().replace("-", "");

                                String formattedText = "";
                                if (inputText.length() >= 2 && inputText.length() <= 6) {
                                    formattedText = inputText.substring(0, 2) + "-" + inputText.substring(2);
                                } else if (inputText.length() > 6 && inputText.length() <= 6) {
                                    formattedText = inputText.substring(0, 3) + "-" + inputText.substring(3);
                                } else {
                                    txtplaca.setTextColor(ContextCompat.getColor(getContext(), android.R.color.holo_red_dark));
                                }

                                if (!formattedText.isEmpty()) {
                                    txtplaca.removeTextChangedListener(this);
                                    txtplaca.setText(formattedText);
                                    txtplaca.setSelection(formattedText.length());
                                    txtplaca.addTextChangedListener(this);
                                    txtplaca.setTextColor(ContextCompat.getColor(getContext(), android.R.color.black));
                                }
                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                            }
                        });

                        /** Spinner de Tipo de Pago */
                        getCard();
                        dropStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                cards = (Card) dropStatus.getSelectedItem();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                            }
                        });

                        /** Radio Button de Visiluación de campos */
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

                        btncancelar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                alertDialog.dismiss();
                            }
                        });

                        /** Buscar Placa - Boleta */
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

                        /** Buscar DNI - Boleta */
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

                        /** Generar Datos Simples - Boleta */
                        btngenerar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                txtplaca.setText(GlobalInfo.getsettingNroPlaca10);
                                textdni.setText(GlobalInfo.getsettingClienteID10);
                                textnombre.setText(GlobalInfo.getsettingClienteRZ10);
                            }
                        });

                        radioGroup.check(cbefectivo.getId());

                        /** Agregar - Boleta */
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

        /** Operación de Factura */
        btnfactura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for(DetalleVenta detalleVenta : detalleVentaList){

                    String mnCara = detalleVenta.getCara().toString();

                    if(mCara == null){
                        Toast.makeText(getContext(),"Seleccionar Cara",Toast.LENGTH_SHORT).show();
                    } else if(mnCara.equals(mCara) ) {

                        /** Abrir Modal */
                        builder = new AlertDialog.Builder(getActivity());
                        LayoutInflater inflater = getActivity().getLayoutInflater();
                        View dialogView = inflater.inflate(R.layout.fragment_factura, null);
                        builder.setView(dialogView);
                        abrirmodal();

                        alertplaca       = dialogView.findViewById(R.id.alertPlaca);
                        alertruc         = dialogView.findViewById(R.id.alertRUC);
                        alertrazsocial   = dialogView.findViewById(R.id.alertRazSocial);
                        alertpefectivo   = dialogView.findViewById(R.id.alertPEfectivo);
                        alertoperacion   = dialogView.findViewById(R.id.alertOperacion);

                        txtplaca         = dialogView.findViewById(R.id.inputPlaca);
                        textruc          = dialogView.findViewById(R.id.inputRUC);
                        textrazsocial    = dialogView.findViewById(R.id.inputRazSocial);
                        textdireccion    = dialogView.findViewById(R.id.inputDireccion);
                        textkilometraje  = dialogView.findViewById(R.id.inputKilometraje);
                        textobservacion  = dialogView.findViewById(R.id.inputObservacion);
                        textNroOperacio  = dialogView.findViewById(R.id.inputOperacion);
                        textpagoefectivo = dialogView.findViewById(R.id.inputPEfectivo);
                        modopagoefectivo = dialogView.findViewById(R.id.modopagoefectivo);

                        radioGroup     = dialogView.findViewById(R.id.radioformapago);
                        cbefectivo     = dialogView.findViewById(R.id.radioEfectivo);
                        cbtarjeta      = dialogView.findViewById(R.id.radioTarjeta);
                        cbcredito      = dialogView.findViewById(R.id.radioCredito);

                        dropStatus            = dialogView.findViewById(R.id.dropStatus);
                        textdropStatus        = dialogView.findViewById(R.id.textdropStatus);

                        btnagregar     = dialogView.findViewById(R.id.btnagregarboleta);
                        btncancelar    = dialogView.findViewById(R.id.btncancelar);
                        buscarruc      = dialogView.findViewById(R.id.btnsunat);
                        buscarplaca    = dialogView.findViewById(R.id.btnplaca);

                        /** Spinner de Tipo de Pago */
                        getCard();
                        dropStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                cards = (Card) dropStatus.getSelectedItem();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                            }
                        });

                        /** Radio Button de Visiluación de campos */
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

                        btncancelar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                alertDialog.dismiss();
                            }
                        });

                        /** Buscar Placa - Factura */
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

                        /** Buscar RUC - Factura */
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

                        /** Agregar - Factura */
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
                                            detalleVenta.setMontoSoles(Double.parseDouble(textpagoefectivo.getText().toString()));
                                        }

                                    }else if (datotipotarjeta.equals("Credito")) {

                                        if(value != Double.parseDouble(decimalFormat.format(value))){
                                            alertpefectivo.setError("* Por favor ingrese un valor con dos decimales solamente");
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

        /** Operación de Nota de Despacho */
        btnnotadespacho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for (DetalleVenta detalleVenta : detalleVentaList){
                    String mnCara = detalleVenta.getCara().toString();

                    if(mCara == null){
                        Toast.makeText(getContext(),"Seleccionar Cara",Toast.LENGTH_SHORT).show();
                    }else if(mnCara.equals(mCara) ) {

                        builder = new AlertDialog.Builder(getActivity());
                        LayoutInflater inflater = getActivity().getLayoutInflater();
                        View dialogView = inflater.inflate(R.layout.fragment_nota_despacho, null);
                        builder.setView(dialogView);
                        abrirmodal();

                        alertplaca      = dialogView.findViewById(R.id.alertPlacalaca);
                        alertid         = dialogView.findViewById(R.id.alertIDCliente);
                        alertruc        = dialogView.findViewById(R.id.alertRUC);
                        alertrazsocial  = dialogView.findViewById(R.id.alertRazSocial);

                        txtplaca        = dialogView.findViewById(R.id.inputPlaca);
                        textid          = dialogView.findViewById(R.id.inputIDCliente);
                        textruc         = dialogView.findViewById(R.id.inputRUC);
                        textnombre      = dialogView.findViewById(R.id.inputRazSocial);
                        textdireccion   = dialogView.findViewById(R.id.inputDireccion);
                        textkilometraje = dialogView.findViewById(R.id.inputKilometraje);
                        textobservacion = dialogView.findViewById(R.id.inputObservacion);

                        btnagregar      = dialogView.findViewById(R.id.btnagregarboleta);
                        btncancelar     = dialogView.findViewById(R.id.btncancelarboleta);
                        buscarid        = dialogView.findViewById(R.id.btnsunat);
                        buscarplaca     = dialogView.findViewById(R.id.btnplaca);

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
                                    alertrazsocial.setError("* El campo Nombre es obligatorio");
                                } else {
                                    alertplaca.setErrorEnabled(false);
                                    alertid.setErrorEnabled(false);
                                    alertruc.setErrorEnabled(false);
                                    alertrazsocial.setErrorEnabled(false);

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

        /** Operación de Serafin */
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

        /** Operación de Puntos */
        btnpuntos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PuntosFragment puntosFragment = new PuntosFragment();
                puntosFragment.show(getActivity().getSupportFragmentManager(), "Puntos");

            }
        });

        /** Listado de Dettalles de Venta  */
        recyclerDetalleVenta = view.findViewById(R.id.recyclerdetalleventa);
        recyclerDetalleVenta.setLayoutManager(new LinearLayoutManager(getContext()));

        /** Listado de Mangueras  */
        recyclerManguera = view.findViewById(R.id.recyclerlado);
        recyclerManguera.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        /** Listado de Caras  */
        recyclerCara = view.findViewById(R.id.recycler);
        recyclerCara.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        /** API Retrofit - Consumiendo */
        findOptran(GlobalInfo.getterminalImei10);
        findLados(GlobalInfo.getterminalImei10);
        findDetalleVenta(GlobalInfo.getterminalImei10);
        findSetting(GlobalInfo.getterminalCompanyID10);
        findCorrelativo(GlobalInfo.getterminalImei10);

        return view;
    }

    private void startTimer() {

        timer = new Timer();

        realizarOperacion();

        timer.schedule(timerTask,5000);
    }

    public void stoptimertask() {

        timer.cancel();
        mTimerRunning = false;
        automatiStop.setText("Stop");
        automatiStop.setBackgroundColor(Color.parseColor("#6c757d"));
    }

    private void realizarOperacion() {

        timerTask = new TimerTask() {
            public void run() {
                handler.post(new Runnable() {
                    public void run() {

                        boletas(GlobalInfo.getNameCompany10,GlobalInfo.getRucCompany10, GlobalInfo.getAddressCompany10,
                                GlobalInfo.getBranchCompany10,GlobalInfo.getoptranFechaTran10, GlobalInfo.getterminalTurno10,
                                GlobalInfo.getuserName10, GlobalInfo.getoptranNroLado10,GlobalInfo.getoptranProductoDs10,
                                GlobalInfo.getoptranUniMed10, GlobalInfo.getoptranPrecio10, GlobalInfo.getoptranGalones10,
                                GlobalInfo.getoptranSoles10);
                    }
                });
            }
        };

        mTimerRunning = true;
        automatiStop.setText("Automático");
        automatiStop.setBackgroundColor(Color.parseColor("#001E8A"));
    }

    /** Impresión de Boletas Simple */
    private  void boletas(String NameCompany, String RUCCompany,String AddressCompany, String BranchCompany,
                          String FechaTranOptran,  Integer TurnoTerminal,String CajeroTerminal, String NroLadoOptran,
                          String ProductoOptran,String UndMedOptran,Double PrecioOptran, Double GalonesOptran,Double SolesOptran) {

        /** Logo */
        Bitmap logo = Printama.getBitmapFromVector(getContext(), R.drawable.logoroble);

        /** Organizar la cadena de texto de Address y Branch */
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

        /** Operación Gravada */
        double Subtotal     = (SolesOptran/1.18);
        double SubtotalOff  = Math.round(Subtotal*100.0)/100.0;
        String OpeGravada   = String.valueOf(SubtotalOff);

        /** IGV */
        double Impuesto     = (SolesOptran-SubtotalOff);
        double ImpuestoOff  = Math.round(Impuesto*100.0)/100.0;
        String IGV          = String.valueOf(ImpuestoOff);

        /** Convertir número a letras */
        Numero_Letras NumLetra = new Numero_Letras();
        String LetraSoles      = NumLetra.Convertir(String.valueOf(SolesOptran),true);

        /** Fecha para Codigo QR */
        String tipodocumento    = "03";
        String boleta           = GlobalInfo.getcorrelativoSerie  + "-" +   GlobalInfo.getcorrelativoNumero ;
        String tipodni          = GlobalInfo.getsettingClienteID10;
        String dni              = GlobalInfo.getsettingClienteRZ10;
        String fechaEmision     = FechaTranOptran.substring(6,10) + "-" + FechaTranOptran.substring(3,5) + "-" + FechaTranOptran.substring(0,2);

        /** Generar codigo QR */
        StringBuilder QRBoleta = new StringBuilder();
        QRBoleta.append(RUCCompany + "|".toString());
        QRBoleta.append(tipodocumento+ "|".toString());
        QRBoleta.append(boleta+ "|".toString());
        QRBoleta.append(IGV+ "|".toString());
        QRBoleta.append(SolesOptran+ "|".toString());
        QRBoleta.append(fechaEmision+ "|".toString());
        QRBoleta.append(tipodni+ "|".toString());
        QRBoleta.append(dni+ "|".toString());

        String Qrboleta = QRBoleta.toString();


        /** Imprimir Comprobante */
        Printama.with(getContext()).connect(printama -> {
            printama.printTextln("                 ", Printama.CENTER);
            printama.printImage(logo, 200);
            printama.setSmallText();
            printama.printTextlnBold(NameCompany, Printama.CENTER);
            printama.printTextlnBold("PRINCIPAL: " + AddressD, Printama.CENTER);
            printama.printTextlnBold(AddressU, Printama.CENTER);
            printama.printTextlnBold("SUCURSAL: " + BranchD, Printama.CENTER);
            printama.printTextlnBold(BranchU, Printama.CENTER);
            printama.printTextlnBold("RUC: " + RUCCompany, Printama.CENTER);
            printama.printTextlnBold("BOLETA DE VENTA ELECTRONICA", Printama.CENTER);
            printama.printTextlnBold(boleta,Printama.CENTER);
            printama.setSmallText();
            printama.printDoubleDashedLine();
            printama.addNewLine(1);
            printama.setSmallText();
            printama.printTextln("Fecha - Hora : "+ FechaTranOptran + "   Turno: "+ TurnoTerminal,Printama.LEFT);
            printama.printTextln("Cajero : "+ CajeroTerminal , Printama.LEFT);
            printama.printTextln("Lado   : "+ NroLadoOptran, Printama.LEFT);
            printama.setSmallText();
            printama.printDoubleDashedLine();
            printama.addNewLine(1);
            printama.setSmallText();
            printama.printTextlnBold("PRODUCTO      "+"U/MED   "+"PRECIO   "+"CANTIDAD  "+"IMPORTE",Printama.RIGHT);
            printama.setSmallText();
            printama.printTextln(ProductoOptran,Printama.LEFT);
            printama.printTextln(UndMedOptran+"    " + PrecioOptran+"      " + GalonesOptran +"     "+ SolesOptran,Printama.RIGHT);
            printama.setSmallText();
            printama.printDoubleDashedLine();
            printama.addNewLine(1);
            printama.setSmallText();
            printama.printTextln("OP. GRAVADAS: S/ "+ OpeGravada, Printama.RIGHT);
            printama.printTextln("OP. EXONERADAS: S/  "+ "0.00" , Printama.RIGHT);
            printama.printTextln("I.G.V. 18%: S/  "+ IGV, Printama.RIGHT);
            printama.printTextlnBold("TOTAL VENTA: S/ "+ SolesOptran, Printama.RIGHT);
            printama.setSmallText();
            printama.printDoubleDashedLine();
            printama.addNewLine(1);
            printama.setSmallText();
            printama.printTextlnBold("CONDICION DE PAGO:", Printama.LEFT);
            printama.printTextlnBold("CONTADO: S/ " + SolesOptran, Printama.RIGHT);
            printama.setSmallText();
            printama.printTextln("SON: " + LetraSoles, Printama.LEFT);
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
            printama.feedPaper();
            printama.close();
        }, this::showToast);

    }

    /** API SERVICE - Correlativo */

    private void findCorrelativo(String id){

        Call<List<Correlativo>> call = mAPIService.findCorrelativo(id);

        call.enqueue(new Callback<List<Correlativo>>() {
            @Override
            public void onResponse(Call<List<Correlativo>> call, Response<List<Correlativo>> response) {
                try {

                    if(!response.isSuccessful()){
                        Toast.makeText(getContext(), "Codigo de error: " + response.code(), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    List<Correlativo> correlativoList = response.body();

                    for(Correlativo correlativo: correlativoList) {

                        GlobalInfo.getcorrelativoTerminalID  = String.valueOf(correlativo.getTerminalID());
                        GlobalInfo.getcorrelativoImei        = String.valueOf(correlativo.getImei());
                        GlobalInfo.getcorrelativoTurno       = Integer.valueOf(correlativo.getTurno());
                        GlobalInfo.getcorrelativoSerie       = String.valueOf(correlativo.getSerie());
                        GlobalInfo.getcorrelativoNumero      = String.valueOf(correlativo.getNumero());

                    }

                }catch (Exception ex){
                    Toast.makeText(getContext(),ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Correlativo>> call, Throwable t) {
                Toast.makeText(getContext(), "Error de conexión APICORE Correlativo - RED - WIFI", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /** API SERVICE - Optran */
    private void findOptran(String id){

        Call<List<Optran>> call = mAPIService.findOptran(id);
        call.enqueue(new Callback<List<Optran>>() {
            @Override
            public void onResponse(Call<List<Optran>> call, Response<List<Optran>> response) {

                try {

                    if(!response.isSuccessful()){
                        Toast.makeText(getContext(), "Codigo de error: " + response.code(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    List<Optran> optranList = response.body();

                    for(Optran optran: optranList) {

                        GlobalInfo.getoptranTranID10     = Integer.valueOf(optran.getTranID());
                        GlobalInfo.getoptranNroLado10    = String.valueOf(optran.getNroLado());
                        GlobalInfo.getoptranManguera10   = String.valueOf(optran.getManguera());
                        GlobalInfo.getoptranFechaTran10  = String.valueOf(optran.getFechaTran());
                        GlobalInfo.getoptranArticuloID10 = String.valueOf(optran.getArticuloID());
                        GlobalInfo.getoptranProductoDs10 = String.valueOf(optran.getProductoDs());
                        GlobalInfo.getoptranPrecio10     = Double.valueOf(optran.getPrecio());
                        GlobalInfo.getoptranGalones10    = Double.valueOf(optran.getGalones());
                        GlobalInfo.getoptranSoles10      = Double.valueOf(optran.getSoles());
                        GlobalInfo.getoptranOperador10   = String.valueOf(optran.getOperador());
                        GlobalInfo.getoptranCliente10    = String.valueOf(optran.getCliente());
                        GlobalInfo.getoptranUniMed10     = String.valueOf(optran.getUniMed());
                    }

                }catch (Exception ex){
                    Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<List<Optran>> call, Throwable t) {
                Toast.makeText(getContext(), "Error de conexión APICORE Optran - RED - WIFI", Toast.LENGTH_SHORT).show();
            }
        });

    }

    /** API SERVICE - Setting */
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
                Toast.makeText(getContext(), "Error de conexión APICORE Setting - RED - WIFI", Toast.LENGTH_SHORT).show();
            }
        });

    }

    /** API SERVICE - Buscar Cliente DNI */
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
                Toast.makeText(getContext(), "Error de conexión APICORE Cliente DNI - RED - WIFI", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /** API SERVICE - Buscar Cliente RUC */
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
                Toast.makeText(getContext(), "Error de conexión APICORE Cliente RUC - RED - WIFI", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /** API SERVICE - Cliente */
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

    /** API SERVICE - Buscar Placa */
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

    /** API SERVICE - Lados */
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

                        /*    textcara =  getActivity().findViewById(R.id.textcara);
                            String numlado = item.getNroLado();
                            textcara.setText(numlado);*/

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

    /** API SERVICE - Pico o Manguera */
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

                            if (mCara == null && GlobalInfo.getPistola10 == null ) {
                                btnlibre.setEnabled(false);
                                btnsoles.setEnabled(false);
                                btngalones.setEnabled(false);



                            } else {
                                btnlibre.setEnabled(true);
                                btnsoles.setEnabled(true);
                                btngalones.setEnabled(true);
                            }

                           /* btnlibre.setEnabled(true);
                            btnsoles.setEnabled(true);
                            btngalones.setEnabled(true);*/

                            GlobalInfo.getPistola10 = item.getMangueraID();

                              /*  textmanguera =  getActivity().findViewById(R.id.textmanguera);
                            String descripcionmanguera = item.getDescripcion();
                            textmanguera.setText(descripcionmanguera);*/

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

    /** API SERVICE - Card Detalle de Ventas */
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

    /** API SERVICE - Setting Task */
    private void findSettingTask(String id){

        Call<List<SettingTask>> call = mAPIService.findSettingTask(id);
        call.enqueue(new Callback<List<SettingTask>>() {
            @Override
            public void onResponse(Call<List<SettingTask>> call, Response<List<SettingTask>> response) {
                try {

                    if(!response.isSuccessful()){
                        Toast.makeText(getContext(), "Codigo de error: " + response.code(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    List<SettingTask> settingTaskList = response.body();

                    for(SettingTask settingTask: settingTaskList) {

                        GlobalInfo.getsettingtaskID10     = settingTask.getTaskID();
                        GlobalInfo.getsettingtaskName10   = settingTask.getName();
                        GlobalInfo.getsettingtaskIsTask10 = Boolean.valueOf(settingTask.getTask());

                    }

                }catch (Exception ex){
                    Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<SettingTask>> call, Throwable t) {
                Toast.makeText(getContext(), "Error de conexión APICORE Setting Task - RED - WIFI", Toast.LENGTH_SHORT).show();

            }
        });
    }

    /** Spinner de Tipo de Tarjetas */
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
                Toast.makeText(getContext(), "Error de conexión APICORE Tarjetas - RED - WIFI", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /** Modal - Dialog */
    private void abrirmodal(){
        alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
        alertDialog.setCancelable(false);
    }

    /** Alerta de Conexión de Bluetooth */
    private void showToast(String message) {
        Toast.makeText(getContext(), "Conectar Bluetooth", Toast.LENGTH_SHORT).show();
    }
}