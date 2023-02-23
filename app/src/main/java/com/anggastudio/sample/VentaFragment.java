package com.anggastudio.sample;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.anggastudio.sample.Adapter.CaraAdapter;
import com.anggastudio.sample.Adapter.DetalleVentaAdapter;
import com.anggastudio.sample.Adapter.GriasAdapter;
import com.anggastudio.sample.Adapter.MangueraAdapter;
import com.anggastudio.sample.Adapter.TransaccionAdapter;
import com.anggastudio.sample.WebApiSVEN.Controllers.APIService;
import com.anggastudio.sample.WebApiSVEN.Models.DetalleVenta;
import com.anggastudio.sample.WebApiSVEN.Models.Lados;
import com.anggastudio.sample.WebApiSVEN.Models.Optran;
import com.anggastudio.sample.WebApiSVEN.Models.Picos;
import com.anggastudio.sample.WebApiSVEN.Parameters.GlobalInfo;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VentaFragment extends Fragment{

    TextView  producto,cara,importetotal,textcara,textmanguera,textNplaca;
    CardView  grias,cardtrans;
    Button    btnlibre,btnsoles,btngalones,btnboleta,btnfactura,btnnotadespacho,btnserafin,btnpuntos;
    ImageButton regreso;

    RecyclerView recyclerCara, recyclerManguera, recyclerDetalleVenta;
    CaraAdapter caraAdapter;
    MangueraAdapter mangueraAdapter;
    DetalleVentaAdapter detalleVentaAdapter;

    List<DetalleVenta> detalleVentaList;

    private APIService mAPIService;
    private String mCara;

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

                        TextInputEditText  txtplaca,textdni,textnombre,textdireccion,textkilometraje,textobservacion;
                        TextInputLayout alertplaca,alertdni, alertnombre;
                        Button btnagregar,btncancelar,btngenerar,buscardni,buscarplaca;

                        txtplaca        = dialogView.findViewById(R.id.inputnplaca);
                        textdni         = dialogView.findViewById(R.id.inputdni);
                        textnombre      = dialogView.findViewById(R.id.inputnombre);
                        textdireccion   = dialogView.findViewById(R.id.inputdireccion);
                        textkilometraje = dialogView.findViewById(R.id.inputkilometraje);
                        textobservacion = dialogView.findViewById(R.id.inputobservacion);

                        btnagregar     = dialogView.findViewById(R.id.btnagregarboleta);
                        btncancelar    = dialogView.findViewById(R.id.btncancelarboleta);
                        btngenerar     = dialogView.findViewById(R.id.btngenerarcliente);
                        buscardni      = dialogView.findViewById(R.id.btnrenic);
                        buscarplaca    = dialogView.findViewById(R.id.btntarjeta);

                        alertplaca     = dialogView.findViewById(R.id.textnplaca);
                        alertdni       = dialogView.findViewById(R.id.textdni);
                        alertnombre    = dialogView.findViewById(R.id.textnombre);

                        buscarplaca.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String textnplaca    = txtplaca.getText().toString().trim();

                                if(textnplaca.isEmpty()){
                                    alertplaca.setError("El campo placa es obligatorio");
                                }else if(!textnplaca.equals("000-0000")){
                                    alertplaca.setError("El dato no es correcto");
                                }else {
                                    alertplaca.setErrorEnabled(false);
                                    textdni.setText("11111111");
                                    textnombre.setText("CLIENTE VARIOS");
                                }
                            }
                        });

                        buscardni.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String textndni    = textdni.getText().toString().trim();
                                if(textndni.isEmpty()){
                                    alertdni.setError("El campo dni es obligatorio");
                                }else if(!textndni.equals("11111111")){
                                    alertdni.setError("El dato no es correcto");
                                }else{
                                    alertdni.setErrorEnabled(false);
                                    textnombre.setText("CLIENTE VARIOS");
                                }
                            }
                        });

                        btncancelar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                alertDialog.dismiss();
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
                                    alertplaca.setError("El campo placa es obligatorio");
                                } else if (textndni.isEmpty()) {
                                    alertdni.setError("El campo DNI es obligatorio");
                                } else if (textnnombre.isEmpty()) {
                                    alertnombre.setError("El campo nombre es obligatorio");
                                } else {
                                    alertplaca.setErrorEnabled(false);
                                    alertdni.setErrorEnabled(false);
                                    alertnombre.setErrorEnabled(false);

                                    detalleVenta.setNroPlaca(txtplaca.getText().toString());
                                    detalleVenta.setClienteRUC(textdni.getText().toString());
                                    detalleVenta.setClienteRS(textnombre.getText().toString());
                                    detalleVenta.setClienteDR(textdireccion.getText().toString());
                                    detalleVenta.setKilometraje(textkilometraje.getText().toString());
                                    detalleVenta.setObservacion(textobservacion.getText().toString());

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

                    if(mnCara.equals(mCara)) {

                        detalleVenta.setClienteRS("mushi");
                        detalleVenta.setClienteDR("direccion");

                        recyclerDetalleVenta.setAdapter(detalleVentaAdapter);
                    }
                }

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
}