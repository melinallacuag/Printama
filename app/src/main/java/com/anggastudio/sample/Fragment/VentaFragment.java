package com.anggastudio.sample.Fragment;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import com.anggastudio.sample.Adapter.ClienteAdapter;
import com.anggastudio.sample.Adapter.DetalleVentaAdapter;
import com.anggastudio.sample.Adapter.MangueraAdapter;
import com.anggastudio.sample.Numero_Letras;
import com.anggastudio.sample.R;
import com.anggastudio.sample.WebApiSVEN.Controllers.APIService;
import com.anggastudio.sample.WebApiSVEN.Models.Card;
import com.anggastudio.sample.WebApiSVEN.Models.Cliente;
import com.anggastudio.sample.WebApiSVEN.Models.Correlativo;
import com.anggastudio.sample.WebApiSVEN.Models.DetalleVenta;
import com.anggastudio.sample.WebApiSVEN.Models.Lados;
import com.anggastudio.sample.WebApiSVEN.Models.Optran;
import com.anggastudio.sample.WebApiSVEN.Models.Picos;
import com.anggastudio.sample.WebApiSVEN.Models.Placa;
import com.anggastudio.sample.WebApiSVEN.Models.Setting;
import com.anggastudio.sample.WebApiSVEN.Models.SettingTask;
import com.anggastudio.sample.WebApiSVEN.Models.VentaCA;
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

    /**  Atributos de la Venta */
    TextView  terminalID;
    Button    btnlibre,btnsoles,btngalones,btnboleta,btnfactura,btnnotadespacho,btnserafin,btnpuntos,automatiStop;

    /**  AdapterList - Recycler */
    RecyclerView recyclerCara, recyclerManguera, recyclerDetalleVenta,recyclerCliente;
    ClienteAdapter clienteAdapter;
    List<Cliente> clienteList;
    CaraAdapter caraAdapter;
    MangueraAdapter mangueraAdapter;
    DetalleVentaAdapter detalleVentaAdapter;
    List<DetalleVenta> detalleVentaList;

    /** Time Task */
    boolean mTimerRunning;
    Timer timer;
    TimerTask timerTask;

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

    /** Buscador por nombres del Cliente */
    SearchView buscadorUser;

    /** Formularios */
    private Dialog modalBoleta,modalFactura,modalNDespacho,modalCliente,modalClienteRUC;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_venta, container, false);

        mAPIService  = GlobalInfo.getAPIService();

        /** Nombre de la Terminal ID */
        terminalID      = view.findViewById(R.id.terminalID);
        automatiStop    = view.findViewById(R.id.automatiStop);
        btnlibre        = view.findViewById(R.id.btnlibre);
        btnsoles        = view.findViewById(R.id.btnsoles);
        btngalones      = view.findViewById(R.id.btngalones);
        btnboleta       = view.findViewById(R.id.btnboleta);
        btnfactura      = view.findViewById(R.id.btnfactura);
        btnnotadespacho = view.findViewById(R.id.btnnotadespacho);
        btnserafin      = view.findViewById(R.id.btnserafin);
        btnpuntos       = view.findViewById(R.id.btnpuntos);

        terminalID.setText(GlobalInfo.getterminalID10);

        /** Boton Time Task */
        automatiStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mTimerRunning) {
                    stoptimertask();
                } else {
                    startTimerGR1();
                }
            }
        });

        /** Boton Bloqueados */
        btnlibre.setEnabled(false);
        btnsoles.setEnabled(false);
        btngalones.setEnabled(false);
        btnboleta.setEnabled(false);
        btnfactura.setEnabled(false);
        btnnotadespacho.setEnabled(false);
        btnserafin.setEnabled(false);
        btnpuntos.setEnabled(false);

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

        /** Mostrar Formulario de Boleta y Realizar la Operacion */
        modalBoleta = new Dialog(getContext());
        modalBoleta.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        modalBoleta.setContentView(R.layout.fragment_boleta);
        modalBoleta.setCancelable(false);

        btnboleta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for(DetalleVenta detalleVenta : detalleVentaList){

                    String mnCara = detalleVenta.getCara().toString();

                    if(mCara == null){
                        Toast.makeText(getContext(),"Seleccionar Cara",Toast.LENGTH_SHORT).show();
                    }else if(mnCara.equals(mCara)) {

                        modalBoleta.show();

                        alertplaca       = modalBoleta.findViewById(R.id.alertPlaca);
                        alertdni         = modalBoleta.findViewById(R.id.alertDNI);
                        alertnombre      = modalBoleta.findViewById(R.id.alertNombre);
                        alertpefectivo   = modalBoleta.findViewById(R.id.alertPEfectivo);
                        alertoperacion   = modalBoleta.findViewById(R.id.alertOperacion);

                        txtplaca         = modalBoleta.findViewById(R.id.inputPlaca);
                        textdni          = modalBoleta.findViewById(R.id.inputDNI);
                        textnombre       = modalBoleta.findViewById(R.id.inputNombre);
                        textdireccion    = modalBoleta.findViewById(R.id.inputDireccion);
                        textkilometraje  = modalBoleta.findViewById(R.id.inputKilometraje);
                        textobservacion  = modalBoleta.findViewById(R.id.inputObservacion);
                        textNroOperacio  = modalBoleta.findViewById(R.id.inputOperacion);
                        textpagoefectivo = modalBoleta.findViewById(R.id.inputPEfectivo);
                        modopagoefectivo = modalBoleta.findViewById(R.id.modopagoefectivo);

                        radioGroup       = modalBoleta.findViewById(R.id.radioformapago);
                        cbefectivo       = modalBoleta.findViewById(R.id.radioEfectivo);
                        cbtarjeta        = modalBoleta.findViewById(R.id.radioTarjeta);
                        cbcredito        = modalBoleta.findViewById(R.id.radioCredito);

                        dropStatus       = modalBoleta.findViewById(R.id.dropStatus);
                        textdropStatus   = modalBoleta.findViewById(R.id.textdropStatus);

                        btnagregar       = modalBoleta.findViewById(R.id.btnagregarboleta);
                        btncancelar      = modalBoleta.findViewById(R.id.btncancelar);
                        btngenerar       = modalBoleta.findViewById(R.id.btngenerarcliente);
                        buscardni        = modalBoleta.findViewById(R.id.btnrenic);
                        buscarplaca      = modalBoleta.findViewById(R.id.btnplaca);


                        /** Mostrar Formulario de Cliente y Realizar la Operacion */
                        modalCliente = new Dialog(getContext());
                        modalCliente.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        modalCliente.setContentView(R.layout.fragment_clientes);
                        modalCliente.setCancelable(false);

                        final GestureDetector gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
                            @Override
                            public boolean onDoubleTap(MotionEvent e) {

                                modalCliente.show();

                                btncancelar    = modalCliente.findViewById(R.id.btncancelar);
                                buscadorUser   = modalCliente.findViewById(R.id.searchView);

                                /** API Retrofit - CLIENTE DNI */
                                getClienteDNI();

                                /**  Listado de Cliente */
                                recyclerCliente = modalCliente.findViewById(R.id.recyclercliente);
                                recyclerCliente.setLayoutManager(new LinearLayoutManager(getContext()));

                                /**  Buscador por Nombre - DNI */
                                buscadorUser.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                    @Override
                                    public boolean onQueryTextSubmit(String query) {
                                        return false;
                                    }

                                    @Override
                                    public boolean onQueryTextChange(String newText) {
                                        clienteAdapter.filtrado(newText);
                                        return false;
                                    }
                                });

                                btncancelar.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        modalCliente.dismiss();

                                        buscadorUser.setQuery("", false);

                                    }
                                });
                                return true;
                            }
                        });

                        textdni.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                gestureDetector.onTouchEvent(event);
                                if (!gestureDetector.onTouchEvent(event)) {

                                    return false;
                                }
                                return true;
                            }
                        });



                        /** API Retrofit - Tipo de Pago */
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
                                radioButton = modalBoleta.findViewById(checkedId);
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

                        /** Boton para cancelar operación */
                        btncancelar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                modalBoleta.dismiss();

                                txtplaca.setText("000-0000");
                                textdni.getText().clear();
                                textnombre.getText().clear();
                                textdireccion.getText().clear();
                                textkilometraje.getText().clear();
                                textobservacion.getText().clear();
                                textNroOperacio.getText().clear();
                                textpagoefectivo.getText().clear();

                                alertplaca.setErrorEnabled(false);
                                alertdni.setErrorEnabled(false);
                                alertnombre.setErrorEnabled(false);
                                alertoperacion.setErrorEnabled(false);
                                alertpefectivo.setErrorEnabled(false);
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

                                    textdni.getText().clear();
                                    textnombre.getText().clear();
                                    textdireccion.getText().clear();
                                }
                            }
                        });

                        /** Buscar DNI - Boleta */
                        buscardni.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {

                                String getClienteDni = textdni.getText().toString();

                                if (getClienteDni.isEmpty() || getClienteDni == null) {
                                    alertdni.setError("* El campo DNI es obligatorio");
                                }else {

                                    alertdni.setErrorEnabled(false);

                                    findClienteDNI(getClienteDni);

                                    textnombre.getText().clear();
                                    textdireccion.getText().clear();
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
                                } else if (textndni.length() < 8) {
                                    alertdni.setError("* El DNI debe tener 8 dígitos");
                                    return;
                                }else if (textnnombre.isEmpty()) {
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
                                detalleVenta.setMontoSoles(0.00);


                                if (datotipotarjeta.equals("Tarjeta")){

                                    Double value = Double.valueOf(textnpagoefectivo);
                                    DecimalFormat decimalFormat = new DecimalFormat("#.##");

                                    detalleVenta.setTarjetaCredito(String.valueOf(Integer.valueOf(cards.getCardID())));
                                    detalleVenta.setOperacionREF(textNroOperacio.getText().toString());

                                    if(value != Double.parseDouble(decimalFormat.format(value))){
                                        alertpefectivo.setError("* Por favor ingrese un valor con dos decimales solamente");
                                        return;
                                    }else{
                                        detalleVenta.setMontoSoles(Double.parseDouble(textpagoefectivo.getText().toString()));
                                    }

                                }else if (datotipotarjeta.equals("Credito")) {

                                    Double value = Double.valueOf(textnpagoefectivo);
                                    DecimalFormat decimalFormat = new DecimalFormat("#.##");

                                    if(value != Double.parseDouble(decimalFormat.format(value))){
                                        alertpefectivo.setError("* Por favor ingrese un valor con dos decimales solamente");
                                        return;
                                    }else{
                                        detalleVenta.setMontoSoles(Double.parseDouble(textpagoefectivo.getText().toString()));
                                    }
                                }

                                recyclerDetalleVenta.setAdapter(detalleVentaAdapter);
                                Toast.makeText(getContext(), "Se agrego correctamente", Toast.LENGTH_SHORT).show();

                                modalBoleta.dismiss();

                                txtplaca.setText("000-0000");
                                textdni.getText().clear();
                                textnombre.getText().clear();
                                textdireccion.getText().clear();
                                textkilometraje.getText().clear();
                                textobservacion.getText().clear();
                                textNroOperacio.getText().clear();
                                textpagoefectivo.getText().clear();
                            }
                        });
                    }
                }

            }
        });

        /** Mostrar Formulario de Factura y Realizar la Operacion */
        modalFactura = new Dialog(getContext());
        modalFactura.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        modalFactura.setContentView(R.layout.fragment_factura);
        modalFactura.setCancelable(false);

        btnfactura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for(DetalleVenta detalleVenta : detalleVentaList){

                    String mnCara = detalleVenta.getCara().toString();

                    if(mCara == null){
                        Toast.makeText(getContext(),"Seleccionar Cara",Toast.LENGTH_SHORT).show();
                    } else if(mnCara.equals(mCara) ) {

                        modalFactura.show();

                        alertplaca       = modalFactura.findViewById(R.id.alertPlaca);
                        alertruc         = modalFactura.findViewById(R.id.alertRUC);
                        alertrazsocial   = modalFactura.findViewById(R.id.alertRazSocial);
                        alertpefectivo   = modalFactura.findViewById(R.id.alertPEfectivo);
                        alertoperacion   = modalFactura.findViewById(R.id.alertOperacion);

                        txtplaca         = modalFactura.findViewById(R.id.inputPlaca);
                        textruc          = modalFactura.findViewById(R.id.inputRUC);
                        textrazsocial    = modalFactura.findViewById(R.id.inputRazSocial);
                        textdireccion    = modalFactura.findViewById(R.id.inputDireccion);
                        textkilometraje  = modalFactura.findViewById(R.id.inputKilometraje);
                        textobservacion  = modalFactura.findViewById(R.id.inputObservacion);
                        textNroOperacio  = modalFactura.findViewById(R.id.inputOperacion);
                        textpagoefectivo = modalFactura.findViewById(R.id.inputPEfectivo);
                        modopagoefectivo = modalFactura.findViewById(R.id.modopagoefectivo);

                        radioGroup       = modalFactura.findViewById(R.id.radioformapago);
                        cbefectivo       = modalFactura.findViewById(R.id.radioEfectivo);
                        cbtarjeta        = modalFactura.findViewById(R.id.radioTarjeta);
                        cbcredito        = modalFactura.findViewById(R.id.radioCredito);

                        dropStatus       = modalFactura.findViewById(R.id.dropStatus);
                        textdropStatus   = modalFactura.findViewById(R.id.textdropStatus);

                        btnagregar       = modalFactura.findViewById(R.id.btnagregarboleta);
                        btncancelar      = modalFactura.findViewById(R.id.btncancelar);
                        buscarruc        = modalFactura.findViewById(R.id.btnsunat);
                        buscarplaca      = modalFactura.findViewById(R.id.btnplaca);


                        /** Mostrar Formulario de Cliente y Realizar la Operacion */
                        modalClienteRUC = new Dialog(getContext());
                        modalClienteRUC.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        modalClienteRUC.setContentView(R.layout.fragment_clientes);
                        modalClienteRUC.setCancelable(false);

                        final GestureDetector gestureDetector2 = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {


                            public boolean onDoubleTap(MotionEvent e) {

                                modalClienteRUC.show();

                                btncancelar    = modalClienteRUC.findViewById(R.id.btncancelar);
                                buscadorUser   = modalClienteRUC.findViewById(R.id.searchView);

                                /** API Retrofit - CLIENTE DNI */
                                getClienteRUC();

                                /**  Listado de Cliente */
                                recyclerCliente = modalClienteRUC.findViewById(R.id.recyclercliente);
                                recyclerCliente.setLayoutManager(new LinearLayoutManager(getContext()));

                                btncancelar.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {

                                                modalClienteRUC.dismiss();

                                                buscadorUser.setQuery("", false);

                                            }
                                        });

                                /**  Buscador por Nombre - DNI */
                                buscadorUser.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                            @Override
                                            public boolean onQueryTextSubmit(String query) {
                                                return false;
                                            }

                                            @Override
                                            public boolean onQueryTextChange(String newText) {
                                                clienteAdapter.filtrado(newText);
                                                return false;
                                            }
                                        });

                                return true;
                            }
                        });

                        textruc.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event2) {

                                gestureDetector2.onTouchEvent(event2);
                                if (!gestureDetector2.onTouchEvent(event2)) {

                                    return false;
                                }
                                return true;
                            }
                        });


                        /** API Retrofit - Tipo de Pago */
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
                                radioButton = modalFactura.findViewById(checkedId);

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

                        /** Boton para cancelar operación */
                        btncancelar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                modalFactura.dismiss();

                                txtplaca.setText("000-0000");
                                textruc.getText().clear();
                                textrazsocial.getText().clear();
                                textdireccion.getText().clear();
                                textkilometraje.getText().clear();
                                textobservacion.getText().clear();
                                textNroOperacio.getText().clear();
                                textpagoefectivo.getText().clear();

                                alertplaca.setErrorEnabled(false);
                                alertruc.setErrorEnabled(false);
                                alertrazsocial.setErrorEnabled(false);
                                alertoperacion.setErrorEnabled(false);
                                alertpefectivo.setErrorEnabled(false);
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

                                    textruc.getText().clear();
                                    textrazsocial.getText().clear();
                                    textdireccion.getText().clear();

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

                                    textrazsocial.getText().clear();
                                    textdireccion.getText().clear();

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
                                } else if (textnruc.length() < 8) {
                                    alertruc.setError("* El RUC debe tener 11 dígitos");
                                    return;
                                }else if (textnnombre.isEmpty()) {
                                    alertrazsocial.setError("* El campo Nombre es obligatorio");
                                    return;
                                }else if (radioGroup.getCheckedRadioButtonId() == -1){
                                    Toast.makeText(getContext(), "Por favor, seleccione Tipo de Pago", Toast.LENGTH_SHORT).show();
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
                                detalleVenta.setMontoSoles(0.00);

                                if (datotipotarjeta.equals("Tarjeta")){
                                    Double value = Double.parseDouble(textnpagoefectivo);
                                    DecimalFormat decimalFormat = new DecimalFormat("#.##");

                                    detalleVenta.setTarjetaCredito(String.valueOf(Integer.valueOf(cards.getCardID())));
                                    detalleVenta.setOperacionREF(textNroOperacio.getText().toString());

                                    if(value != Double.parseDouble(decimalFormat.format(value))){
                                        alertpefectivo.setError("* Por favor ingrese un valor con dos decimales solamente");
                                        return;
                                    }else{
                                        detalleVenta.setMontoSoles(Double.parseDouble(textpagoefectivo.getText().toString()));
                                    }

                                }else if (datotipotarjeta.equals("Credito")) {

                                    Double value = Double.parseDouble(textnpagoefectivo);
                                    DecimalFormat decimalFormat = new DecimalFormat("#.##");

                                    if(value != Double.parseDouble(decimalFormat.format(value))){
                                        alertpefectivo.setError("* Por favor ingrese un valor con dos decimales solamente");
                                        return;
                                    }else{
                                        detalleVenta.setMontoSoles(Double.parseDouble(textpagoefectivo.getText().toString()));
                                    }

                                }

                                recyclerDetalleVenta.setAdapter(detalleVentaAdapter);
                                Toast.makeText(getContext(), "Se agrego correctamente", Toast.LENGTH_SHORT).show();

                                modalFactura.dismiss();

                                txtplaca.setText("000-0000");
                                textruc.getText().clear();
                                textrazsocial.getText().clear();
                                textdireccion.getText().clear();
                                textkilometraje.getText().clear();
                                textobservacion.getText().clear();
                                textNroOperacio.getText().clear();
                                textpagoefectivo.getText().clear();
                            }
                        });
                    }
                }
            }
        });

        /** Mostrar Formulario de Nota de Despacho y Realizar la Operacion */
        modalNDespacho = new Dialog(getContext());
        modalNDespacho.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        modalNDespacho.setContentView(R.layout.fragment_nota_despacho);
        modalNDespacho.setCancelable(false);

        btnnotadespacho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for (DetalleVenta detalleVenta : detalleVentaList){
                    String mnCara = detalleVenta.getCara().toString();

                    if(mCara == null){
                        Toast.makeText(getContext(),"Seleccionar Cara",Toast.LENGTH_SHORT).show();
                    }else if(mnCara.equals(mCara) ) {

                        modalNDespacho.show();

                        alertplaca      = modalNDespacho.findViewById(R.id.alertPlacalaca);
                        alertid         = modalNDespacho.findViewById(R.id.alertIDCliente);
                        alertruc        = modalNDespacho.findViewById(R.id.alertRUC);
                        alertrazsocial  = modalNDespacho.findViewById(R.id.alertRazSocial);

                        txtplaca        = modalNDespacho.findViewById(R.id.inputPlaca);
                        textid          = modalNDespacho.findViewById(R.id.inputIDCliente);
                        textruc         = modalNDespacho.findViewById(R.id.inputRUC);
                        textnombre      = modalNDespacho.findViewById(R.id.inputRazSocial);
                        textdireccion   = modalNDespacho.findViewById(R.id.inputDireccion);
                        textkilometraje = modalNDespacho.findViewById(R.id.inputKilometraje);
                        textobservacion = modalNDespacho.findViewById(R.id.inputObservacion);

                        btnagregar      = modalNDespacho.findViewById(R.id.btnagregarboleta);
                        btncancelar     = modalNDespacho.findViewById(R.id.btncancelarboleta);
                        buscarid        = modalNDespacho.findViewById(R.id.btnsunat);
                        buscarplaca     = modalNDespacho.findViewById(R.id.btnplaca);

                        btncancelar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                modalNDespacho.dismiss();
                            }
                        });

                        buscarplaca.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                builder = new AlertDialog.Builder(getActivity());
                                LayoutInflater inflater = getActivity().getLayoutInflater();
                                View dialogView = inflater.inflate(R.layout.fragment_clientes, null);
                                builder.setView(dialogView);


                                btncancelar    = dialogView.findViewById(R.id.btncancelar);

                                btncancelar.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        modalNDespacho.dismiss();
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

                                    textruc.getText().clear();
                                    textnombre.getText().clear();
                                    textdireccion.getText().clear();

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

                                    modalNDespacho.dismiss();

                                    txtplaca.getText().clear();
                                    textid.getText().clear();
                                    textruc.getText().clear();
                                    textnombre.getText().clear();
                                    textdireccion.getText().clear();
                                    textkilometraje.getText().clear();
                                    textobservacion.getText().clear();
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
                SerafinFragment serafinFragment = new SerafinFragment();
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
        findLados(GlobalInfo.getterminalImei10);
        findDetalleVenta(GlobalInfo.getterminalImei10);
        findSetting(GlobalInfo.getterminalCompanyID10);

        return view;
    }

    private void startTimerGR1() {

        timer = new Timer();

        realizarOperacion();

        timer.schedule(timerTask,1000,3000);

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

                findOptran(GlobalInfo.getterminalImei10);

                if (GlobalInfo.getpase10 == true){

                    String mCara = GlobalInfo.getoptranNroLado10;



                    for(DetalleVenta detalleVenta : detalleVentaList) {

                        String mnCara = detalleVenta.getCara().toString();
                        String mnTipoPago = detalleVenta.getTipoPago().toString();
                        Double mnImpuesto = detalleVenta.getImpuesto();
                        String mnNroPlaca = detalleVenta.getNroPlaca();
                        String mnTarjetaPuntos = detalleVenta.getTarjetaPuntos();
                        String mnClienteID = detalleVenta.getClienteID();
                        String mnClienteRUC = detalleVenta.getClienteRUC();
                        String mnClienteRS = detalleVenta.getClienteRS().toString();
                        String mnCliernteDR = detalleVenta.getClienteDR().toString();
                        String mnTarjND = detalleVenta.getTarjetaND().toString();
                        String mnTarjetaCredito = detalleVenta.getTarjetaCredito().toString();
                        String mnOperacionREF = detalleVenta.getOperacionREF().toString();
                        String mnObservacion = detalleVenta.getObservacion().toString();
                        String mnKilometraje = detalleVenta.getKilometraje().toString();
                        Double mnMontoSoles = detalleVenta.getMontoSoles();
                        Double mnMtoSaldoCredito = detalleVenta.getMtoSaldoCredito();
                        Double mnPtosDisponibles = detalleVenta.getPtosDisponible();



                        if(mnCara.equals(mCara)) {

                            String   mnTipoDocumento = "03";

                            if (mnClienteRUC.length() == 11){

                                mnTipoDocumento = "01";

                                findCorrelativo(GlobalInfo.getterminalImei10,mnTipoDocumento);

                                String NumeroSerie = GlobalInfo.getcorrelativoSerie;
                                String NumeroDocumento = GlobalInfo.getcorrelativoNumero;

                                GlobalInfo.getcorrelativoSerie = "";
                                GlobalInfo.getcorrelativoNumero = "";

                                GlobalInfo.getpase10 = false;

                                if (NumeroSerie == null){
                                    return;
                                }

                                if (NumeroSerie.isEmpty()) {
                                    return;
                                }

                                if (NumeroDocumento == null){
                                    return;
                                }

                                if (NumeroDocumento.isEmpty()) {
                                    return;
                                }

                                guardar_ventaCa(GlobalInfo.getterminalCompanyID10,mnTipoDocumento.toString(), NumeroSerie.toString(),NumeroDocumento.toString(),
                                        GlobalInfo.getterminalID10,GlobalInfo.getsettingClienteID10,GlobalInfo.getsettingClienteRZ10,GlobalInfo.getterminalTurno10,GlobalInfo.getterminalFecha10,
                                        GlobalInfo.getoptranFechaTran10,GlobalInfo.getoptranSoles10, GlobalInfo.getsettingNroPlaca10,
                                        GlobalInfo.getoptranUniMed10,
                                        GlobalInfo.getoptranNroLado10, GlobalInfo.getoptranManguera10);

                                NumeroSerie = "";
                                NumeroDocumento = "";

                                return;
                            }
                            if (mnClienteID.length() == 8 && mnClienteRUC.length() == 0){

                                mnTipoDocumento = "03";

                                findCorrelativo(GlobalInfo.getterminalImei10,mnTipoDocumento);

                                String NumeroSerie = GlobalInfo.getcorrelativoSerie;
                                String NumeroDocumento = GlobalInfo.getcorrelativoNumero;

                                GlobalInfo.getcorrelativoSerie = "";
                                GlobalInfo.getcorrelativoNumero = "";

                                GlobalInfo.getpase10 = false;

                                if (NumeroSerie == null){
                                    return;
                                }

                                if (NumeroSerie.isEmpty()) {
                                    return;
                                }

                                if (NumeroDocumento == null){
                                    return;
                                }

                                if (NumeroDocumento.isEmpty()) {
                                    return;
                                }

                                guardar_ventaCa(GlobalInfo.getterminalCompanyID10,mnTipoDocumento.toString(), NumeroSerie.toString(),NumeroDocumento.toString(),
                                        GlobalInfo.getterminalID10,GlobalInfo.getsettingClienteID10,GlobalInfo.getsettingClienteRZ10,GlobalInfo.getterminalTurno10,GlobalInfo.getterminalFecha10,
                                        GlobalInfo.getoptranFechaTran10,GlobalInfo.getoptranSoles10, GlobalInfo.getsettingNroPlaca10,
                                        GlobalInfo.getoptranUniMed10,
                                        GlobalInfo.getoptranNroLado10, GlobalInfo.getoptranManguera10);

                                NumeroSerie = "";
                                NumeroDocumento = "";

                                return;
                            }

                            findCorrelativo(GlobalInfo.getterminalImei10,mnTipoDocumento);

                            String NumeroSerie = GlobalInfo.getcorrelativoSerie;
                            String NumeroDocumento = GlobalInfo.getcorrelativoNumero;

                            GlobalInfo.getcorrelativoSerie = "";
                            GlobalInfo.getcorrelativoNumero = "";

                            GlobalInfo.getpase10 = false;

                            if (NumeroSerie == null){
                                return;
                            }

                            if (NumeroSerie.isEmpty()) {
                                return;
                            }

                            if (NumeroDocumento == null){
                                return;
                            }

                            if (NumeroDocumento.isEmpty()) {
                                return;
                            }

                            guardar_ventaCa(GlobalInfo.getterminalCompanyID10,mnTipoDocumento.toString(), NumeroSerie.toString(),NumeroDocumento.toString(),
                                    GlobalInfo.getterminalID10,GlobalInfo.getsettingClienteID10,GlobalInfo.getsettingClienteRZ10,GlobalInfo.getterminalTurno10,GlobalInfo.getterminalFecha10,
                                    GlobalInfo.getoptranFechaTran10,GlobalInfo.getoptranSoles10, GlobalInfo.getsettingNroPlaca10,
                                    GlobalInfo.getoptranUniMed10,
                                    GlobalInfo.getoptranNroLado10, GlobalInfo.getoptranManguera10);

                            NumeroSerie = "";
                            NumeroDocumento = "";
                        }


                    }



                }

            }
        };

        mTimerRunning = true;
        automatiStop.setText("Automático");
        automatiStop.setBackgroundColor(Color.parseColor("#001E8A"));

    }

    private void guardar_ventaCa(Integer _companyID,String mnTipoDocumento, String _serieDocumento, String _nroDocumento, String _terminalID, String _clienteID, String _clienteRZ, Integer _turno,String _fechaproceso,
                                 String _fechaAtencion, Double _mtoTotal, String _nroPlaca,
                                 String _uniMed,
                                 String _nroLado, String _manguera){

        GlobalInfo.getNumeroVecesIMP10 = 0;

        if (mnTipoDocumento == "01"){
            facturacion(GlobalInfo.getNameCompany10,GlobalInfo.getRucCompany10, GlobalInfo.getAddressCompany10,
                    GlobalInfo.getBranchCompany10,GlobalInfo.getoptranFechaTran10, GlobalInfo.getterminalTurno10,
                    GlobalInfo.getuserName10, GlobalInfo.getoptranNroLado10,GlobalInfo.getoptranProductoDs10,
                    GlobalInfo.getoptranUniMed10, GlobalInfo.getoptranPrecio10, GlobalInfo.getoptranGalones10,
                    GlobalInfo.getoptranSoles10, _serieDocumento.toString(), _nroDocumento.toString());
        }else if (mnTipoDocumento == "03"){
            boletas(GlobalInfo.getNameCompany10,GlobalInfo.getRucCompany10, GlobalInfo.getAddressCompany10,
                    GlobalInfo.getBranchCompany10,GlobalInfo.getoptranFechaTran10, GlobalInfo.getterminalTurno10,
                    GlobalInfo.getuserName10, GlobalInfo.getoptranNroLado10,GlobalInfo.getoptranProductoDs10,
                    GlobalInfo.getoptranUniMed10, GlobalInfo.getoptranPrecio10, GlobalInfo.getoptranGalones10,
                    GlobalInfo.getoptranSoles10, _serieDocumento.toString(), _nroDocumento.toString());
        }else if (mnTipoDocumento == "98"){
            serafin(GlobalInfo.getNameCompany10,GlobalInfo.getRucCompany10, GlobalInfo.getAddressCompany10,
                    GlobalInfo.getBranchCompany10,GlobalInfo.getoptranFechaTran10, GlobalInfo.getterminalTurno10,
                    GlobalInfo.getuserName10, GlobalInfo.getoptranNroLado10,GlobalInfo.getoptranProductoDs10,
                    GlobalInfo.getoptranUniMed10, GlobalInfo.getoptranPrecio10, GlobalInfo.getoptranGalones10,
                    GlobalInfo.getoptranSoles10, _serieDocumento.toString(), _nroDocumento.toString());
        }



        String tranIDR = String.valueOf(GlobalInfo.getoptranTranID10);
        /** Fecha de Impresión */
        Calendar calendarprint       = Calendar.getInstance(TimeZone.getTimeZone("America/Lima"));
        SimpleDateFormat formatdate  = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
        String _fechadocumento    = formatdate.format(calendarprint.getTime());

        final VentaCA ventaCA = new VentaCA(_companyID,mnTipoDocumento,_serieDocumento,_nroDocumento,_terminalID,_clienteID,"",_clienteRZ,"",_turno,_fechaproceso,
                "",_fechaAtencion,0.00,0.00,0.00,_mtoTotal,_nroPlaca,"","T","","","",
                "",0.00,0.00,0.00,GlobalInfo.getuserName10,1,"",GlobalInfo.getoptranProductoDs10,_uniMed,1,10,18,GlobalInfo.getoptranPrecio10,GlobalInfo.getoptranPrecio10,
                GlobalInfo.getoptranGalones10,0.00,tranIDR,_nroLado,_manguera,"",1,0,"",_mtoTotal,0.00,"");

        Call<VentaCA> call = mAPIService.postVentaCA(ventaCA);

        call.enqueue(new Callback<VentaCA>() {
            @Override
            public void onResponse(Call<VentaCA> call, Response<VentaCA> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(getContext(), "Codigo de error: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            @Override
            public void onFailure(Call<VentaCA> call, Throwable t) {
                Toast.makeText(getContext(), "Error de conexión APICORE", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /** Impresión de Boletas Simple */
    private  void boletas(String NameCompany, String RUCCompany,String AddressCompany, String BranchCompany,
                          String FechaTranOptran,  Integer TurnoTerminal,String CajeroTerminal, String NroLadoOptran,
                          String ProductoOptran,String UndMedOptran,Double PrecioOptran, Double GalonesOptran,
                          Double SolesOptran,String NumeroSerie, String NumeroDocumento) {

        /** Fecha de Impresión */
        Calendar calendarprint       = Calendar.getInstance(TimeZone.getTimeZone("America/Lima"));
        SimpleDateFormat formatdate  = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        String FechaHoraImpresion    = formatdate.format(calendarprint.getTime());

        /** Logo de la Empresa */
        Bitmap logoRobles = BitmapFactory.decodeResource(getResources(), R.drawable.logoprincipal);

        /** Dirección Principal de la Empresa */
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




        /**  Sucursal de la Empresa */

        String sucursal = BranchCompany;

        int indiceGuion1 = sucursal.indexOf("-");
        int indiceGuion2 = sucursal.lastIndexOf("-");

        String direccion = sucursal.substring(0, indiceGuion1).trim();
        String ubicacion = sucursal.substring(indiceGuion1 + 1, sucursal.length()).trim();


        String PrecioPRIM2 = String.format("%.2f",PrecioOptran);

        String GALONESPRIM3 = String.format("%.3f",GalonesOptran);

        String TotalPRIM2 = String.format("%.2f",SolesOptran);

        /** Operación Gravada */
        double Subtotal     = (Double.valueOf(SolesOptran) /1.18);
        double SubtotalOff  = Math.round(Subtotal*100.0)/100.0;
        String OpeGravada   = String.format("%.2f",SubtotalOff);

        /** IGV */
        double Impuesto     = (Double.valueOf(SolesOptran)-SubtotalOff);
        double ImpuestoOff  = Math.round(Impuesto*100.0)/100.0;
        String IGV          = String.format("%.2f",ImpuestoOff);

        /** Convertir número a letras */
        Numero_Letras NumLetra = new Numero_Letras();
        String LetraSoles      = NumLetra.Convertir(String.valueOf(SolesOptran),true);

        /** Fecha para Codigo QR */
        String tipodocumento    = "03";
        String boleta           = NumeroSerie  + "-" +   NumeroDocumento ;
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

            GlobalInfo.getNumeroVecesIMP10 += 1;

            if(GlobalInfo.getNumeroVecesIMP10 > 1){
                printama.close();
                return;
            }

            printama.printTextln("                 ", Printama.CENTER);
            printama.printImage(logoRobles, 200);
            printama.setSmallText();
            printama.printTextlnBold(NameCompany, Printama.CENTER);
            printama.printTextlnBold("PRINCIPAL: " + AddressD, Printama.CENTER);
            printama.printTextlnBold(AddressU, Printama.CENTER);
            printama.printTextlnBold("SUCURSAL: " + direccion, Printama.CENTER);
            printama.printTextlnBold(ubicacion, Printama.CENTER);
            printama.printTextlnBold("RUC: " + RUCCompany, Printama.CENTER);
            printama.printTextlnBold("BOLETA DE VENTA ELECTRONICA", Printama.CENTER);
            printama.printTextlnBold(boleta,Printama.CENTER);
            printama.setSmallText();
            printama.printDoubleDashedLine();
            printama.addNewLine(1);
            printama.setSmallText();
            printama.printTextln("Fecha - Hora : "+ FechaHoraImpresion + "  Turno: "+ TurnoTerminal,Printama.LEFT);
            printama.printTextln("Cajero : "+ CajeroTerminal , Printama.LEFT);
            printama.printTextln("Lado   : "+ NroLadoOptran, Printama.LEFT);
            printama.setSmallText();
            printama.printDoubleDashedLine();
            printama.addNewLine(1);
            printama.setSmallText();
            printama.printTextlnBold("PRODUCTO      "+"U/MED   "+"PRECIO   "+"CANTIDAD  "+"IMPORTE",Printama.RIGHT);
            printama.setSmallText();
            printama.printTextln(ProductoOptran,Printama.LEFT);
            printama.printTextln(UndMedOptran+"    " + PrecioPRIM2 + "      " + GALONESPRIM3 +"     "+ TotalPRIM2,Printama.RIGHT);
            printama.setSmallText();
            printama.printDoubleDashedLine();
            printama.addNewLine(1);
            printama.setSmallText();
            printama.printTextlnBold("TOTAL VENTA: S/ "+ TotalPRIM2 , Printama.RIGHT);
            printama.setSmallText();
            printama.printDoubleDashedLine();
            printama.addNewLine(1);
            printama.setSmallText();
            printama.printTextlnBold("CONDICION DE PAGO:", Printama.LEFT);
            printama.printTextlnBold("CONTADO: S/ " + TotalPRIM2, Printama.RIGHT);
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

    /** Impresión de Transferencia Gratuita  */
    private  void gratuita(String NameCompany, String RUCCompany,String AddressCompany, String BranchCompany,
                           String FechaTranOptran,  Integer TurnoTerminal,String CajeroTerminal, String NroLadoOptran,
                           String ProductoOptran,String UndMedOptran,Double PrecioOptran, Double GalonesOptran,
                           Double SolesOptran, String NumeroDocumento) {

        /** Logo */
        Bitmap logoRobles = BitmapFactory.decodeResource(getResources(), R.drawable.logoprincipal);

        String modoCero = "0.00";

        /** Organizar la cadena de texto de Address y Branch */
        Matcher matcher,matcher2;
        Pattern patronsintaxi,patronsintaxi2;

        patronsintaxi = Pattern.compile("(?<!\\S)\\p{Lu}+\\.? \\w+ - \\w+ - \\w+(\\.? \\d+)?(?!\\S)");
        /** Direccion principal **/
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

        String sucursal = BranchCompany;

        int indiceGuion1 = sucursal.indexOf("-");
        int indiceGuion2 = sucursal.lastIndexOf("-");

        String direccion = sucursal.substring(0, indiceGuion1).trim();
        String ubicacion = sucursal.substring(indiceGuion1 + 1, sucursal.length()).trim();

      /*  String[] partesDireccion = sucursal.split(" - ");
        String direccion = partesDireccion[0];
        String ubicacion = partesDireccion[1];*/


        String PrecioPRIM2 = String.format("%.2f",PrecioOptran);

        String GALONESPRIM3 = String.format("%.3f",GalonesOptran);

        String TotalPRIM2 = String.format("%.2f",SolesOptran);

        /** Operación Gravada */
        double Subtotal     = (Double.valueOf(SolesOptran) /1.18);
        double SubtotalOff  = Math.round(Subtotal*100.0)/100.0;
        String OpeGravada   = String.format("%.2f",SubtotalOff);

        /** IGV */
        double Impuesto     = (Double.valueOf(SolesOptran)-SubtotalOff);
        double ImpuestoOff  = Math.round(Impuesto*100.0)/100.0;
        String IGV          = String.format("%.2f",ImpuestoOff);

        /** Convertir número a letras */
        Numero_Letras NumLetra = new Numero_Letras();
        String LetraSoles      = NumLetra.Convertir(String.valueOf(modoCero),true);

        /** Fecha para Codigo QR */
        String tipodocumento    = "03";
        String boleta           = GlobalInfo.getcorrelativoSerie  + "-" +   NumeroDocumento.toString() ;
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

            GlobalInfo.getNumeroVecesIMP10 += 1;

            if(GlobalInfo.getNumeroVecesIMP10 > 1){
                printama.close();
                return;
            }

            printama.printTextln("                 ", Printama.CENTER);
            printama.printImage(logoRobles, 200);
            printama.setSmallText();
            printama.printTextlnBold(NameCompany, Printama.CENTER);
            printama.printTextlnBold("PRINCIPAL: " + AddressD, Printama.CENTER);
            printama.printTextlnBold(AddressU, Printama.CENTER);
            printama.printTextlnBold(" SUCURSAL: " + direccion, Printama.CENTER);
            printama.printTextlnBold(ubicacion, Printama.CENTER);
            printama.printTextlnBold("RUC: " + RUCCompany, Printama.CENTER);
            printama.printTextlnBold("        ", Printama.CENTER);
            printama.printTextlnBold("***** TRANSFERENCIA GRATUITA *****", Printama.CENTER);
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
            printama.printTextln(UndMedOptran+"    " + PrecioPRIM2 + "      " + GALONESPRIM3 +"     "+ modoCero,Printama.RIGHT);
            printama.setSmallText();
            printama.printDoubleDashedLine();
            printama.addNewLine(1);
            printama.setSmallText();
            printama.printTextlnBold("OP. GRATUITA: S/  "+ TotalPRIM2 , Printama.RIGHT);
            printama.setSmallText();
            printama.printTextlnBold("TOTAL VENTA: S/ "+ modoCero , Printama.RIGHT);
            printama.setSmallText();
            printama.printDoubleDashedLine();
            printama.addNewLine(1);
            printama.setSmallText();
            printama.printTextlnBold("CONDICION DE PAGO:", Printama.LEFT);
            printama.printTextlnBold("CONTADO: S/ " + modoCero, Printama.RIGHT);
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
            printama.printTextln("Autorizado mediante resolución de Superintendencia Nro. 203-2015 SUNAT. Representación impresa de la boleta de venta electrónica. Consulte desde\n"+ "http://4-fact.com/sven/auth/consulta");
            printama.feedPaper();
            printama.close();
        }, this::showToast);

    }

    /** Impresión de Factura Simple */
    private  void facturacion(String NameCompany, String RUCCompany,String AddressCompany, String BranchCompany,
                              String FechaTranOptran,  Integer TurnoTerminal,String CajeroTerminal, String NroLadoOptran,
                              String ProductoOptran,String UndMedOptran,Double PrecioOptran, Double GalonesOptran,
                              Double SolesOptran,String NumeroSerie, String NumeroDocumento) {

        /** Fecha de Impresión */
        Calendar calendarprint       = Calendar.getInstance(TimeZone.getTimeZone("America/Lima"));
        SimpleDateFormat formatdate  = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String FechaHoraImpresion    = formatdate.format(calendarprint.getTime());

        /** Logo */
        Bitmap logoRobles = BitmapFactory.decodeResource(getResources(), R.drawable.logoprincipal);

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



        String sucursal = BranchCompany;

        int indiceGuion1 = sucursal.indexOf("-");
        int indiceGuion2 = sucursal.lastIndexOf("-");

        String direccion = sucursal.substring(0, indiceGuion1).trim();
        String ubicacion = sucursal.substring(indiceGuion1 + 1, sucursal.length()).trim();


        String PrecioPRIM2 = String.format("%.2f",PrecioOptran);

        String TotalPRIM2 = String.format("%.2f",SolesOptran);

        /** Operación Gravada */
        double Subtotal     = (Double.valueOf(SolesOptran) /1.18);
        double SubtotalOff  = Math.round(Subtotal*100.0)/100.0;
        String OpeGravada   = String.format("%.2f",SubtotalOff);

        /** IGV */
        double Impuesto     = (Double.valueOf(SolesOptran)-SubtotalOff);
        double ImpuestoOff  = Math.round(Impuesto*100.0)/100.0;
        String IGV          = String.format("%.2f",ImpuestoOff);

        /** Convertir número a letras */
        Numero_Letras NumLetra = new Numero_Letras();
        String LetraSoles      = NumLetra.Convertir(String.valueOf(SolesOptran),true);

        /** Fecha para Codigo QR */
        String tipodocumento    = "01";
        String factura          = NumeroSerie  + "-" +   NumeroDocumento ;
        String tipodni          = GlobalInfo.getsettingClienteID10;
        String dni              = GlobalInfo.getsettingClienteRZ10;
        String fechaEmision     = FechaTranOptran.substring(6,10) + "-" + FechaTranOptran.substring(3,5) + "-" + FechaTranOptran.substring(0,2);

        /** Generar codigo QR */
        StringBuilder QRBoleta = new StringBuilder();
        QRBoleta.append(RUCCompany + "|".toString());
        QRBoleta.append(tipodocumento+ "|".toString());
        QRBoleta.append(factura+ "|".toString());
        QRBoleta.append(IGV+ "|".toString());
        QRBoleta.append(SolesOptran+ "|".toString());
        QRBoleta.append(fechaEmision+ "|".toString());
        QRBoleta.append(tipodni+ "|".toString());
        QRBoleta.append(dni+ "|".toString());

        String Qrboleta = QRBoleta.toString();

        //GENERAR QR
        String clientes          ="Cliente Varios";
        String placa             ="000-0000";
        String ruccliente        ="11111111111";
        String direccioncliente  ="-";

        Printama.with(getContext()).connect(printama -> {

            GlobalInfo.getNumeroVecesIMP10 += 1;

            if(GlobalInfo.getNumeroVecesIMP10 > 1){
                printama.close();
                return;
            }
            printama.printTextln("                 ", Printama.CENTER);
            printama.printImage(logoRobles, 200);
            printama.setSmallText();
            printama.printTextlnBold(NameCompany, Printama.CENTER);
            printama.printTextlnBold("PRINCIPAL: " + AddressD, Printama.CENTER);
            printama.printTextlnBold(AddressU, Printama.CENTER);
            printama.printTextlnBold("SUCURSAL: " + direccion, Printama.CENTER);
            printama.printTextlnBold(ubicacion, Printama.CENTER);
            printama.printTextlnBold("RUC: " + RUCCompany, Printama.CENTER);
            printama.printTextlnBold("FACTURA DE VENTA ELECTRONICA", Printama.CENTER);
            printama.printTextlnBold(factura,Printama.CENTER);
            printama.setSmallText();
            printama.printDoubleDashedLine();
            printama.addNewLine(1);
            printama.setSmallText();
            printama.printTextln("Fecha - Hora : "+ FechaHoraImpresion + "   Turno: "+ TurnoTerminal,Printama.LEFT);
            printama.printTextln("Cajero : "+ CajeroTerminal , Printama.LEFT);
            printama.printTextln("Lado   : "+ NroLadoOptran, Printama.LEFT);
            printama.printTextln("Placa  : "+ placa, Printama.LEFT);
            printama.printTextln("R.U.C.    : "+ ruccliente, Printama.LEFT);
            printama.printTextln("Cliente   : "+clientes, Printama.LEFT);
            printama.printTextln("Dirección : "+direccioncliente, Printama.LEFT);
            printama.setSmallText();
            printama.printDoubleDashedLine();
            printama.addNewLine(1);
            printama.setSmallText();
            printama.printTextlnBold("PRODUCTO      "+"U/MED   "+"PRECIO   "+"CANTIDAD  "+"IMPORTE",Printama.RIGHT);
            printama.setSmallText();
            printama.printTextln(ProductoOptran,Printama.LEFT);
            printama.printTextln(UndMedOptran+"    " + PrecioPRIM2 + "      " + GalonesOptran +"     "+ TotalPRIM2,Printama.RIGHT);
            printama.setSmallText();
            printama.printDoubleDashedLine();
            printama.addNewLine(1);
            printama.setSmallText();
            printama.printTextln("OP. GRAVADAS: S/ "+OpeGravada, Printama.RIGHT);
            printama.printTextln("OP. EXONERADAS: S/  "+ "0.00" , Printama.RIGHT);
            printama.printTextln("I.G.V. 18%: S/  "+ IGV, Printama.RIGHT);
            printama.printTextlnBold("TOTAL VENTA: S/ "+ TotalPRIM2 , Printama.RIGHT);
            printama.setSmallText();
            printama.printDoubleDashedLine();
            printama.addNewLine(1);
            printama.setSmallText();
            printama.printTextlnBold("CONDICION DE PAGO:", Printama.LEFT);
            printama.printTextlnBold("CONTADO: S/ " + TotalPRIM2, Printama.RIGHT);
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

    /** Impresión de Nota de Despacho Simple */
    private  void notaDespacho(String NameCompany, String RUCCompany,String AddressCompany, String BranchCompany,
                               String FechaTranOptran,  Integer TurnoTerminal,String CajeroTerminal, String NroLadoOptran,
                               String ProductoOptran,String UndMedOptran,Double PrecioOptran, Double GalonesOptran,Double SolesOptran) {
        /** Logo */
        Bitmap logoRobles = BitmapFactory.decodeResource(getResources(), R.drawable.logoprincipal);

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


        String PrecioPRIM2 = String.format("%.2f",PrecioOptran);

        String TotalPRIM2 = String.format("%.2f",SolesOptran);

        /** Serie y Número Correlativo */
        String SerieNumero           = GlobalInfo.getcorrelativoSerie  + "-" +   GlobalInfo.getcorrelativoNumero ;

        String clientes     = "Cliente Varios";
        String placa        = "000-0000";
        String ruccliente   = "11111111111";
        String tarjeta      = "7020130000000309";

        Printama.with(getContext()).connect(printama -> {
            printama.printTextln("                 ", Printama.CENTER);
            printama.printImage(logoRobles, 200);
            printama.setSmallText();
            printama.printTextlnBold(NameCompany, Printama.CENTER);
            printama.printTextlnBold("PRINCIPAL: " + AddressD, Printama.CENTER);
            printama.printTextlnBold(AddressU, Printama.CENTER);
            printama.printTextlnBold("SUCURSAL: " + BranchD, Printama.CENTER);
            printama.printTextlnBold(BranchU, Printama.CENTER);
            printama.printTextlnBold("RUC: " + RUCCompany, Printama.CENTER);
            printama.printTextlnBold("NOTA DE DESPACHO", Printama.CENTER);
            printama.printTextlnBold(SerieNumero,Printama.CENTER);
            printama.setSmallText();
            printama.printDoubleDashedLine();
            printama.addNewLine(1);
            printama.setSmallText();
            printama.printTextln("Fecha - Hora : "+ FechaTranOptran + "   Turno: "+ TurnoTerminal,Printama.LEFT);
            printama.printTextln("Cajero : "+ CajeroTerminal , Printama.LEFT);
            printama.printTextln("Lado   : "+ NroLadoOptran, Printama.LEFT);
            printama.printTextln("Placa  : "+ placa, Printama.LEFT);
            printama.printTextln("RUC / DNI : "+ ruccliente, Printama.LEFT);
            printama.printTextln("Cliente   : "+ clientes, Printama.LEFT);
            printama.printTextln("#Tarjeta  : "+ tarjeta, Printama.LEFT);
            printama.setSmallText();
            printama.printDoubleDashedLine();
            printama.addNewLine(1);
            printama.setSmallText();
            printama.printTextlnBold("PRODUCTO      "+"U/MED   "+"PRECIO   "+"CANTIDAD  "+"IMPORTE",Printama.RIGHT);
            printama.setSmallText();
            printama.printTextln(ProductoOptran,Printama.LEFT);
            printama.printTextln(UndMedOptran+"    " + PrecioPRIM2 + "      " + GalonesOptran +"     "+ TotalPRIM2,Printama.RIGHT);
            printama.setSmallText();
            printama.printDoubleDashedLine();
            printama.setSmallText();
            printama.addNewLine(1);
            printama.printTextlnBold("TOTAL VENTA: S/ "+ TotalPRIM2, Printama.RIGHT);
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

    /** Impresión de Serafin */
    private  void serafin(String NameCompany, String RUCCompany,String AddressCompany, String BranchCompany,
                          String FechaTranOptran,  Integer TurnoTerminal,String CajeroTerminal, String NroLadoOptran,
                          String ProductoOptran,String UndMedOptran,Double PrecioOptran, Double GalonesOptran,
                          Double SolesOptran, String NumeroDocumento, String SerieDocumento) {
        /** Logo */
        Bitmap logoRobles = BitmapFactory.decodeResource(getResources(), R.drawable.logoprincipal);

        /** Fecha de Impresión */
        Calendar calendarprint       = Calendar.getInstance(TimeZone.getTimeZone("America/Lima"));
        SimpleDateFormat formatdate  = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        String FechaHoraImpresion    = formatdate.format(calendarprint.getTime());

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


        String PrecioPRIM2 = String.format("%.2f",PrecioOptran);

        String TotalPRIM2 = String.format("%.2f",SolesOptran);

        /** Serie y Número Correlativo */
        String SerieNumero           = GlobalInfo.getcorrelativoSerie  + "-" +   GlobalInfo.getcorrelativoNumero ;


        String kilometraje  = "0000000";

        Printama.with(getContext()).connect(printama -> {
            printama.printTextln("                 ", Printama.CENTER);
            printama.printImage(logoRobles, 200);
            printama.setSmallText();
            printama.printTextlnBold(NameCompany, Printama.CENTER);
            printama.printTextlnBold("PRINCIPAL: " + AddressD, Printama.CENTER);
            printama.printTextlnBold(AddressU, Printama.CENTER);
            printama.printTextlnBold("SUCURSAL: " + BranchD, Printama.CENTER);
            printama.printTextlnBold(BranchU, Printama.CENTER);
            printama.printTextlnBold("RUC: " + RUCCompany, Printama.CENTER);
            printama.printTextlnBold("TICKET SERAFIN", Printama.CENTER);
            printama.printTextlnBold(SerieNumero,Printama.CENTER);
            printama.setSmallText();
            printama.printDoubleDashedLine();
            printama.addNewLine(1);
            printama.setSmallText();
            printama.printTextln("Fecha - Hora : "+ FechaHoraImpresion + "   Turno: "+ TurnoTerminal,Printama.LEFT);
            printama.printTextln("Cajero : "+ CajeroTerminal , Printama.LEFT);
            printama.printTextln("Lado   : " + NroLadoOptran + "   Kilometraje : " +kilometraje, Printama.LEFT);
            printama.setSmallText();
            printama.printDoubleDashedLine();
            printama.addNewLine(1);
            printama.setSmallText();
            printama.printTextlnBold("PRODUCTO      "+"U/MED   "+"PRECIO   "+"CANTIDAD  "+"IMPORTE",Printama.RIGHT);
            printama.setSmallText();
            printama.printTextln(ProductoOptran,Printama.LEFT);
            printama.printTextln(UndMedOptran+"    " + PrecioPRIM2 + "      " + GalonesOptran +"     "+ TotalPRIM2,Printama.RIGHT);
            printama.setSmallText();
            printama.printDoubleDashedLine();
            printama.setSmallText();
            printama.addNewLine(1);
            printama.printTextlnBold("TOTAL VENTA: S/ "+ TotalPRIM2, Printama.RIGHT);
            printama.feedPaper();
            printama.close();
        }, this::showToast);
    }


    /** API SERVICE - Correlativo */
    private void findCorrelativo(String id , String mnTipoDocumento){

        Call<List<Correlativo>> call = mAPIService.findCorrelativo(id,mnTipoDocumento);

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

                    if (optranList == null || optranList.isEmpty()) {
                        return;
                    }

                    for(Optran optran: optranList) {
                        GlobalInfo.getpase10 = true;
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
                        if (clienteList == null || clienteList.isEmpty()) {
                            if (textdni.length() < 8){
                                alertdni.setError("* El DNI debe tener 8 dígitos");
                                return;
                            }else{
                                alertdni.setErrorEnabled(false);
                            }
                            return;
                        }
                        Toast.makeText(getContext(), "Codigo de error: " + response.code(), Toast.LENGTH_SHORT).show();
                        return;
                    }


                    List<Cliente> clienteList = response.body();



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
                        if (clienteList == null || clienteList.isEmpty()) {
                            if (textruc.length() < 11){
                                alertruc.setError("* El RUC debe tener 11 dígitos");
                            }else{
                                alertruc.setErrorEnabled(false);
                            }
                            return;
                        }
                        Toast.makeText(getContext(), "Codigo de error: " + response.code(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    List<Cliente> clienteList = response.body();



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

                            btnlibre.setEnabled(false);
                            btnsoles.setEnabled(false);
                            btngalones.setEnabled(false);
                            btnboleta.setEnabled(false);
                            btnfactura.setEnabled(false);
                            btnnotadespacho.setEnabled(false);
                            btnserafin.setEnabled(false);

                            GlobalInfo.getCara10 = item.getNroLado();
                            mCara = item.getNroLado();
                            findPico(GlobalInfo.getCara10);

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

                            btnlibre.setEnabled(true);
                            btnsoles.setEnabled(true);
                            btngalones.setEnabled(true);
                            btnboleta.setEnabled(true);
                            btnfactura.setEnabled(true);
                            btnserafin.setEnabled(true);

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

    /** Listado de Clientes con DNI */
    private void getClienteDNI(){
        Call<List<Cliente>> call = mAPIService.getClienteDNI();

        call.enqueue(new Callback<List<Cliente>>() {
            @Override
            public void onResponse(Call<List<Cliente>> call, Response<List<Cliente>> response) {
                try {

                    if(!response.isSuccessful()){
                        Toast.makeText(getContext(), "Codigo de error: " + response.code(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    List<Cliente> clienteList = response.body();

                    clienteAdapter = new ClienteAdapter(clienteList, getContext(), new ClienteAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(Cliente item) {
                            String SelectDNI       = item.getClienteID();
                            String SelectNombre    = item.getClienteRZ();
                            String SelectDireccion = item.getClienteDR();

                            textdni.setText(SelectDNI);
                            textnombre.setText(SelectNombre);
                            textdireccion.setText(SelectDireccion);
                            Toast.makeText(getContext(), "Se seleccionó : " + SelectDNI , Toast.LENGTH_SHORT).show();
                            modalCliente.dismiss();

                            buscadorUser.setQuery("", false);
                        }
                    });

                    recyclerCliente.setAdapter(clienteAdapter);

                }catch (Exception ex){
                    Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Cliente>> call, Throwable t) {
                Toast.makeText(getContext(), "Error de conexión APICORE Tarjetas - RED - WIFI", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /** Listado de Clientes con DNI */
    private void getClienteRUC(){
        Call<List<Cliente>> call = mAPIService.getClienteRUC();

        call.enqueue(new Callback<List<Cliente>>() {
            @Override
            public void onResponse(Call<List<Cliente>> call, Response<List<Cliente>> response) {
                try {

                    if(!response.isSuccessful()){
                        Toast.makeText(getContext(), "Codigo de error: " + response.code(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    List<Cliente> clienteList = response.body();

                    clienteAdapter = new ClienteAdapter(clienteList, getContext(), new ClienteAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(Cliente item) {
                            String SelectRUC       = item.getClienteID();
                            String SelectRZSocial    = item.getClienteRZ();
                            String SelectDireccion = item.getClienteDR();

                            textruc.setText(SelectRUC);
                            textrazsocial.setText(SelectRZSocial);
                            textdireccion.setText(SelectDireccion);
                            Toast.makeText(getContext(), "Se seleccionó : " + SelectRUC , Toast.LENGTH_SHORT).show();
                            modalClienteRUC.dismiss();

                            buscadorUser.setQuery("", false);
                        }
                    });

                    recyclerCliente.setAdapter(clienteAdapter);

                }catch (Exception ex){
                    Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Cliente>> call, Throwable t) {
                Toast.makeText(getContext(), "Error de conexión APICORE Tarjetas - RED - WIFI", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /** Alerta de Conexión de Bluetooth */
    private void showToast(String message) {
        Toast.makeText(getContext(), "Conectar Bluetooth", Toast.LENGTH_SHORT).show();
    }

}