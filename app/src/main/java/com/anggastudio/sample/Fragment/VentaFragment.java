package com.anggastudio.sample.Fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import com.anggastudio.sample.WebApiSVEN.Models.Tipotarjeta;
import com.anggastudio.sample.WebApiSVEN.Parameters.GlobalInfo;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VentaFragment extends Fragment{

    TextView  producto,cara,importetotal,textcara,textmanguera,textNplaca;
    CardView  grias;
    Button    btnlibre,btnsoles,btngalones,btnboleta,btnfactura,btnnotadespacho,btnserafin,btnpuntos;
    ImageButton regreso;

    RecyclerView recyclerCara, recyclerManguera, recyclerDetalleVenta;
    CaraAdapter caraAdapter;
    MangueraAdapter mangueraAdapter;
    DetalleVentaAdapter detalleVentaAdapter;

    List<DetalleVenta> detalleVentaList;
    private APIService mAPIService;
    private String mCara;
    Tipotarjeta tipotarjeta = null;

    /* Boleta-Factura-NotadeDespacho */
    Card cards = null;
    RadioGroup radioGroup;
    Spinner dropStatus;
    TextView modopagoefectivo;
    RadioButton cbefectivo,cbtarjeta,cbcredito,radioButton;
    TextInputEditText  textid,txtplaca,textrazsocial,textdni,textruc,textnombre,textdireccion,textkilometraje,textobservacion,textpagoefectivo,textNroOperacio;
    TextInputLayout alertid,alertplaca,alertdni,alertruc, alertnombre,textinputpagoefectivo,textnrooperacion,textdropStatus;
    Button btnagregar,btncancelar,btngenerar,buscardni,buscarsunat,buscarplaca,buscarruc,buscarid;

    String getNroPlacas10,getClienteDNI10, getClienteId10,getClienteRUC10;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_venta, container, false);

        mAPIService  = GlobalInfo.getAPIService();

        producto     = view.findViewById(R.id.textmanguera);
        cara         = view.findViewById(R.id.textcara);
        importetotal = view.findViewById(R.id.txtimporte);
        grias        = view.findViewById(R.id.card);

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

        regreso         = view.findViewById(R.id.volverdasboard);

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
                for(DetalleVenta detalleVenta : detalleVentaList){

                    String mnCara = detalleVenta.getCara().toString();

                    if(mnCara.equals(mCara)) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        LayoutInflater inflater = getActivity().getLayoutInflater();

                        View dialogView = inflater.inflate(R.layout.fragment_boleta, null);
                        builder.setView(dialogView);

                        AlertDialog alertDialog = builder.create();
                        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        alertDialog.show();
                        alertDialog.setCancelable(false);

                        txtplaca         = dialogView.findViewById(R.id.inputnplaca);
                        textdni          = dialogView.findViewById(R.id.inputdni);
                        textnombre       = dialogView.findViewById(R.id.inputnombre);
                        textdireccion    = dialogView.findViewById(R.id.inputdireccion);
                        textkilometraje  = dialogView.findViewById(R.id.inputkilometraje);
                        textobservacion  = dialogView.findViewById(R.id.inputobservacion);
                        textpagoefectivo = dialogView.findViewById(R.id.inputpagoefectivo);
                        textNroOperacio  = dialogView.findViewById(R.id.inputnrooperacion);

                        textinputpagoefectivo = dialogView.findViewById(R.id.textpagoefectivo);
                        textnrooperacion      = dialogView.findViewById(R.id.textnrooperacion);
                        modopagoefectivo      = dialogView.findViewById(R.id.modopagoefectivo);
                        dropStatus            = dialogView.findViewById(R.id.dropStatus);
                        textdropStatus        = dialogView.findViewById(R.id.textdropStatus);

                        btnagregar     = dialogView.findViewById(R.id.btnagregarboleta);
                        btncancelar    = dialogView.findViewById(R.id.btncancelar);
                        btngenerar     = dialogView.findViewById(R.id.btngenerarcliente);
                        buscardni      = dialogView.findViewById(R.id.btnrenic);
                        buscarplaca    = dialogView.findViewById(R.id.btnplaca);

                        alertplaca     = dialogView.findViewById(R.id.textnplaca);
                        alertdni       = dialogView.findViewById(R.id.textdni);
                        alertnombre    = dialogView.findViewById(R.id.textnombre);

                        radioGroup     = dialogView.findViewById(R.id.radioformapago);
                        cbefectivo     = dialogView.findViewById(R.id.radioEfectivo);
                        cbtarjeta      = dialogView.findViewById(R.id.radioTarjeta);
                        cbcredito      = dialogView.findViewById(R.id.radioCredito);

                        //Array de los select
                        getCard();

                        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(RadioGroup group, int checkedId) {
                                radioButton = dialogView.findViewById(checkedId);
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

                        cbefectivo.setOnCheckedChangeListener(new RadioButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean a) {
                               if (a){
                                   modopagoefectivo.setVisibility(View.VISIBLE);
                                   textdropStatus.setVisibility(View.GONE);
                                   textnrooperacion.setVisibility(View.GONE);
                                   textinputpagoefectivo.setVisibility(View.GONE);
                               }else {
                                   modopagoefectivo.setVisibility(View.GONE);
                               }
                            }
                        });

                        cbtarjeta.setOnCheckedChangeListener(new RadioButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean a) {
                                if (a){
                                    textdropStatus.setVisibility(View.VISIBLE);
                                    textinputpagoefectivo.setVisibility(View.VISIBLE);
                                    textnrooperacion.setVisibility(View.VISIBLE);
                                }else {
                                    // No se seleccionó ningún elemento
                                }
                            }
                        });

                        cbcredito.setOnCheckedChangeListener(new RadioButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean a) {
                                if (a){
                                    textinputpagoefectivo.setVisibility(View.VISIBLE);
                                    textdropStatus.setVisibility(View.GONE);
                                    textnrooperacion.setVisibility(View.GONE);
                                }else {
                                    // No se seleccionó ningún elemento
                                }
                            }
                        });

                        buscarplaca.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String getPlaca = txtplaca.getText().toString();

                                if (getPlaca.isEmpty()) {
                                    alertplaca.setError("* El campo Placa es obligatorio");
                                } else {
                                    findPlaca(getPlaca);
                                   // alertplaca.setErrorEnabled(false);
                                    textdni.setText( GlobalInfo.getClienteIDPlaca10);
                                    textnombre.setText(GlobalInfo.getClienteRZPlaca10);
                                    textdireccion.setText(GlobalInfo.getClienteDRPlaca10);
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
                                    findCliente(getClienteDni);
                                    textnombre.setText(GlobalInfo.getclienteRZ10);
                                    textdireccion.setText(GlobalInfo.getclienteDR10);
                                }

                            }
                        });

                        btngenerar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                txtplaca.setText("000-0000");
                                textdni.setText("11111111");
                                textnombre.setText("CLIENTE VARIOS");
                            }
                        });

                        btnagregar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String textnplaca     = txtplaca.getText().toString();
                                String textndni       = textdni.getText().toString();
                                String textnnombre    = textnombre.getText().toString();

                                if (textnplaca.isEmpty()) {
                                    alertplaca.setError("* El campo Placa es obligatorio");
                                } else if (textndni.isEmpty()) {
                                    alertdni.setError("* El campo DNI es obligatorio");
                                } else if (textnnombre.isEmpty()) {
                                    alertnombre.setError("* El campo Nombre es obligatorio");
                                } else {
                                    alertplaca.setErrorEnabled(false);
                                    alertdni.setErrorEnabled(false);
                                    alertnombre.setErrorEnabled(false);

                                    detalleVenta.setNroPlaca(txtplaca.getText().toString());
                                    detalleVenta.setClienteID(textdni.getText().toString());
                                    detalleVenta.setClienteRS(textnombre.getText().toString());
                                    detalleVenta.setClienteDR(textdireccion.getText().toString());
                                    detalleVenta.setKilometraje(textkilometraje.getText().toString());
                                    detalleVenta.setObservacion(textobservacion.getText().toString());
                                    detalleVenta.setOperacionREF(textNroOperacio.getText().toString());
                                    detalleVenta.setMontoSoles(Double.parseDouble(textpagoefectivo.getText().toString()));
                                    detalleVenta.setTipoPago(radioButton.getText().toString().substring(0,1));
                                    String datotipotarjeta =radioButton.getText().toString();
                                    if (datotipotarjeta.equals("Tarjeta")){
                                        detalleVenta.setTarjetaCredito(String.valueOf(Integer.valueOf(cards.getCardID())));
                                    }else if (datotipotarjeta.equals("Credito")){
                                        detalleVenta.setTarjetaCredito("");
                                    }else if (datotipotarjeta.equals("Efectivo")){
                                        detalleVenta.setTarjetaCredito("");
                                    }else {
                                        Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
                                    }

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

        btnfactura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for(DetalleVenta detalleVenta : detalleVentaList){

                    String mnCara = detalleVenta.getCara().toString();

                    if(mnCara.equals(mCara) ) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        LayoutInflater inflater = getActivity().getLayoutInflater();

                        View dialogView = inflater.inflate(R.layout.fragment_factura, null);
                        builder.setView(dialogView);

                        AlertDialog alertDialog = builder.create();
                        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        alertDialog.show();
                        alertDialog.setCancelable(false);

                        txtplaca          = dialogView.findViewById(R.id.inputnplaca);
                        textruc           = dialogView.findViewById(R.id.inputruc);
                        textrazsocial     = dialogView.findViewById(R.id.inputrazsocial);
                        textdireccion     = dialogView.findViewById(R.id.inputdireccion);
                        textkilometraje   = dialogView.findViewById(R.id.inputkilometraje);
                        textobservacion   = dialogView.findViewById(R.id.inputobservacion);
                        textpagoefectivo  = dialogView.findViewById(R.id.inputpagoefectivo);
                        textNroOperacio   = dialogView.findViewById(R.id.inputnrooperacion);

                        textinputpagoefectivo = dialogView.findViewById(R.id.textpagoefectivo);
                        textnrooperacion      = dialogView.findViewById(R.id.textnrooperacion);
                        modopagoefectivo      = dialogView.findViewById(R.id.modopagoefectivo);
                        dropStatus            = dialogView.findViewById(R.id.dropStatus);
                        textdropStatus        = dialogView.findViewById(R.id.textdropStatus);

                        btnagregar     = dialogView.findViewById(R.id.btnagregarboleta);
                        btncancelar    = dialogView.findViewById(R.id.btncancelar);
                        buscarruc      = dialogView.findViewById(R.id.btnsunat);
                        buscarplaca    = dialogView.findViewById(R.id.btnplaca);

                        alertplaca     = dialogView.findViewById(R.id.textnplaca);
                        alertruc       = dialogView.findViewById(R.id.textruc);
                        alertnombre    = dialogView.findViewById(R.id.textrazsocial);

                        radioGroup     = dialogView.findViewById(R.id.radioformapago);
                        cbefectivo     = dialogView.findViewById(R.id.radioEfectivo);
                        cbtarjeta      = dialogView.findViewById(R.id.radioTarjeta);
                        cbcredito      = dialogView.findViewById(R.id.radioCredito);

                        //Array de los select
                        getCard();

                        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(RadioGroup group, int checkedId) {
                                radioButton = dialogView.findViewById(checkedId);
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

                        cbefectivo.setOnCheckedChangeListener(new RadioButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean a) {
                                if (a){
                                    modopagoefectivo.setVisibility(View.VISIBLE);
                                    textdropStatus.setVisibility(View.GONE);
                                    textnrooperacion.setVisibility(View.GONE);
                                    textinputpagoefectivo.setVisibility(View.GONE);
                                }else {
                                    modopagoefectivo.setVisibility(View.GONE);
                                }
                            }
                        });

                        cbtarjeta.setOnCheckedChangeListener(new RadioButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean a) {
                                if (a){
                                    textdropStatus.setVisibility(View.VISIBLE);
                                    textinputpagoefectivo.setVisibility(View.VISIBLE);
                                    textnrooperacion.setVisibility(View.VISIBLE);
                                }else {
                                    // No se seleccionó ningún elemento
                                }
                            }
                        });

                        cbcredito.setOnCheckedChangeListener(new RadioButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean a) {
                                if (a){
                                    textinputpagoefectivo.setVisibility(View.VISIBLE);
                                    textdropStatus.setVisibility(View.GONE);
                                    textnrooperacion.setVisibility(View.GONE);
                                }else {
                                    // No se seleccionó ningún elemento
                                }
                            }
                        });

                        buscarplaca.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                String getPlaca = txtplaca.getText().toString();

                                if (getPlaca.isEmpty()) {
                                    alertplaca.setError("* El campo Placa es obligatorio");
                                } else {
                                    findPlaca(getPlaca);
                                    alertplaca.setErrorEnabled(false);
                                    textruc.setText( GlobalInfo.getClienteIDPlaca10);
                                    textrazsocial.setText(GlobalInfo.getClienteRZPlaca10);
                                    textdireccion.setText(GlobalInfo.getClienteDRPlaca10);
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
                                    findCliente(getClienteRuc);
                                    textrazsocial.setText(GlobalInfo.getclienteRZ10);
                                    textdireccion.setText(GlobalInfo.getclienteDR10);
                                }
                            }
                        });

                        btnagregar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String textnplaca     = txtplaca.getText().toString();
                                String textnruc       = textruc.getText().toString();
                                String textnnombre    = textnombre.getText().toString();

                                if (textnplaca.isEmpty()) {
                                    alertplaca.setError("* El campo Placa es obligatorio");
                                } else if (textnruc.isEmpty()) {
                                    alertruc.setError("* El campo DNI es obligatorio");
                                } else if (textnnombre.isEmpty()) {
                                    alertnombre.setError("* El campo Nombre es obligatorio");
                                } else {
                                    alertplaca.setErrorEnabled(false);
                                    alertdni.setErrorEnabled(false);
                                    alertnombre.setErrorEnabled(false);

                                    detalleVenta.setNroPlaca(txtplaca.getText().toString());
                                    detalleVenta.setClienteRUC(textruc.getText().toString());
                                    detalleVenta.setClienteRS(textnombre.getText().toString());
                                    detalleVenta.setClienteDR(textdireccion.getText().toString());
                                    detalleVenta.setKilometraje(textkilometraje.getText().toString());
                                    detalleVenta.setObservacion(textobservacion.getText().toString());
                                    detalleVenta.setOperacionREF(textNroOperacio.getText().toString());
                                    detalleVenta.setMontoSoles(Double.parseDouble(textpagoefectivo.getText().toString()));
                                    detalleVenta.setTipoPago(radioButton.getText().toString().substring(0,1));
                                    String datotipotarjeta =radioButton.getText().toString();
                                    if (datotipotarjeta.equals("Tarjeta")){
                                        detalleVenta.setTarjetaCredito(String.valueOf(Integer.valueOf(cards.getCardID())));
                                    }else if (datotipotarjeta.equals("Credito")){
                                        detalleVenta.setTarjetaCredito("");
                                    }else if (datotipotarjeta.equals("Efectivo")){
                                        detalleVenta.setTarjetaCredito("");
                                    }else {
                                        Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
                                    }
                                    recyclerDetalleVenta.setAdapter(detalleVentaAdapter);
                                    Toast.makeText(getContext(), "Se agrego correctamente", Toast.LENGTH_SHORT).show();
                                    alertDialog.dismiss();
                                }
                            }
                        });
                    }else {
                        Toast.makeText(getContext(), "Seleccionar Cara", Toast.LENGTH_SHORT).show();
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

                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        LayoutInflater inflater = getActivity().getLayoutInflater();

                        View dialogView = inflater.inflate(R.layout.fragment_nota_despacho, null);
                        builder.setView(dialogView);

                        AlertDialog alertDialog = builder.create();
                        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        alertDialog.show();
                        alertDialog.setCancelable(false);

                        txtplaca        = dialogView.findViewById(R.id.inputnplaca);
                        textid         = dialogView.findViewById(R.id.inputid);
                        textruc         = dialogView.findViewById(R.id.inputruc);
                        textnombre      = dialogView.findViewById(R.id.inputnombre);
                        textdireccion   = dialogView.findViewById(R.id.inputdireccion);
                        textkilometraje = dialogView.findViewById(R.id.inputkilometraje);
                        textobservacion = dialogView.findViewById(R.id.inputobservacion);

                        btnagregar     = dialogView.findViewById(R.id.btnagregarboleta);
                        btncancelar    = dialogView.findViewById(R.id.btncancelarboleta);
                        buscarid      = dialogView.findViewById(R.id.btnsunat);
                        buscarplaca    = dialogView.findViewById(R.id.btnplaca);

                        alertplaca     = dialogView.findViewById(R.id.textnplaca);
                        alertid       = dialogView.findViewById(R.id.textid);
                        alertnombre    = dialogView.findViewById(R.id.textnombre);

                        btncancelar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                alertDialog.dismiss();
                            }
                        });

                        buscarplaca.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String getPlaca = txtplaca.getText().toString();

                                if (getPlaca.isEmpty()) {
                                    alertplaca.setError("* El campo Placa es obligatorio");
                                } else {
                                    findPlaca(getPlaca);
                                    alertplaca.setErrorEnabled(false);
                                    textid.setText( GlobalInfo.getClienteIDPlaca10);
                                    textnombre.setText(GlobalInfo.getClienteRZPlaca10);
                                    textdireccion.setText(GlobalInfo.getClienteDRPlaca10);
                                }
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
                                    findCliente(getClienteId);
                                    textruc.setText(GlobalInfo.getclienteRUC10);
                                    textnombre.setText(GlobalInfo.getclienteRZ10);
                                    textdireccion.setText(GlobalInfo.getclienteDR10);
                                }
                            }
                        });

                        btnagregar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String textnplaca     = txtplaca.getText().toString();
                                String textndni       = textid.getText().toString();
                                String textnnombre    = textnombre.getText().toString();

                                if (textnplaca.isEmpty()) {
                                    alertplaca.setError("* El campo Placa es obligatorio");
                                } else if (textndni.isEmpty()) {
                                    alertid.setError("* El campo DNI es obligatorio");
                                } else if (textnnombre.isEmpty()) {
                                    alertnombre.setError("* El campo Nombre es obligatorio");
                                } else {
                                    alertplaca.setErrorEnabled(false);
                                    alertid.setErrorEnabled(false);
                                    alertnombre.setErrorEnabled(false);

                                    detalleVenta.setNroPlaca(txtplaca.getText().toString());
                                    detalleVenta.setClienteID(textid.getText().toString());
                                    detalleVenta.setClienteRUC(textruc.getText().toString());
                                    detalleVenta.setClienteRS(textnombre.getText().toString());
                                    detalleVenta.setClienteDR(textdireccion.getText().toString());
                                    detalleVenta.setKilometraje(textkilometraje.getText().toString());
                                    detalleVenta.setObservacion(textobservacion.getText().toString());
                                    detalleVenta.setOperacionREF(textNroOperacio.getText().toString());
                                    detalleVenta.setMontoSoles(Double.parseDouble(textpagoefectivo.getText().toString()));
                                    detalleVenta.setTipoPago(radioButton.getText().toString().substring(0,1));
                                    String datotipotarjeta =radioButton.getText().toString();
                                    if (datotipotarjeta.equals("Tarjeta")){
                                        detalleVenta.setTarjetaCredito(tipotarjeta.getIdTarjeta());
                                    }else if (datotipotarjeta.equals("Credito")){
                                        detalleVenta.setTarjetaCredito("");
                                    }else if (datotipotarjeta.equals("Efectivo")){
                                        detalleVenta.setTarjetaCredito("");
                                    }else {
                                        Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
                                    }
                                    recyclerDetalleVenta.setAdapter(detalleVentaAdapter);
                                    Toast.makeText(getContext(), "Se agrego correctamente", Toast.LENGTH_SHORT).show();
                                    alertDialog.dismiss();
                                }
                            }
                        });
                    }else {
                        Toast.makeText(getContext(), "Seleccionar Cara", Toast.LENGTH_SHORT).show();
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

        findLados(GlobalInfo.getImei10);
        findDetalleVenta(GlobalInfo.getImei10);


        return view;
    }

    private  void findCliente(String id){

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
                        Toast.makeText(getContext(), "No se encontró ningún cliente con el ID proporcionado", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Cliente cliente = clienteList.get(0);

                        GlobalInfo.getclienteId10  = String.valueOf(cliente.getClienteID());
                        GlobalInfo.getclienteRUC10 = String.valueOf(cliente.getClienteRUC());
                        GlobalInfo.getclienteRZ10  = String.valueOf(cliente.getClienteRZ());
                        GlobalInfo.getclienteDR10  = String.valueOf(cliente.getClienteDR());
                        getClienteDNI10  = cliente.getClienteID();


                }catch (Exception ex){
                    Toast.makeText(getContext(), "ex.getMessage()", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<List<Cliente>> call, Throwable t) {
                Toast.makeText(getContext(), "Error de conexión APICORE Cara - RED - WIFI", Toast.LENGTH_SHORT).show();
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

    private void findPlaca(String id){

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
                        Toast.makeText(getContext(), "No se encontró ningún placa con el ID proporcionado", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Placa placa = placaList.get(0);

                        GlobalInfo.getNroPlaca10 = String.valueOf(placa.getNroPlaca());
                        GlobalInfo.getClienteIDPlaca10 = String.valueOf(placa.getClienteID());
                        GlobalInfo.getClienteRZPlaca10 = String.valueOf(placa.getClienteRZ());
                        GlobalInfo.getClienteDRPlaca10 = String.valueOf(placa.getClienteDR());
                        getNroPlacas10 = placa.getNroPlaca();


                }catch (Exception ex){
                    Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Placa>> call, Throwable t) {
                Toast.makeText(getContext(), "Error de conexión APICORE Cara - RED - WIFI", Toast.LENGTH_SHORT).show();
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
                          //  findOptran(GlobalInfo.getCara10);

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

                    //List<DetalleVenta> detalleVentaList = response.body();

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
    /*   private  void findOptran(String id){

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

                    transaccionAdapter = new TransaccionAdapter(optranList, getContext());
                    recyclerTransaccion.setAdapter(transaccionAdapter);

                }catch (Exception ex){
                    Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Optran>> call, Throwable t) {
                Toast.makeText(getContext(), "Error de conexión APICORE - RED - WIFI", Toast.LENGTH_SHORT).show();

            }
        });
    }
*/
}