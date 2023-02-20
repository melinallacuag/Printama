package com.anggastudio.sample;

import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.anggastudio.sample.Adapter.CaraAdapter;
import com.anggastudio.sample.Adapter.GriasAdapter;
import com.anggastudio.sample.Adapter.MangueraAdapter;
import com.anggastudio.sample.Adapter.Transaccion;
import com.anggastudio.sample.Adapter.TransaccionAdapter;
import com.anggastudio.sample.WebApiSVEN.Controllers.AppSvenAPI;
import com.anggastudio.sample.WebApiSVEN.Models.Lados;
import com.anggastudio.sample.WebApiSVEN.Models.Picos;
import com.anggastudio.sample.WebApiSVEN.Parameters.GlobalInfo;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VentaFragment extends Fragment{

    private static final int REQUEST_CODE_PERMISSION = 1;
    TextView  producto,cara,importetotal,textcara,textmanguera,txtproductogria,cajero;

    RecyclerView recyclerCara, recyclerManguera, recyclerGrias , recyclerTransaccion;
    CaraAdapter caraAdapter;
    MangueraAdapter mangueraAdapter;
    TransaccionAdapter transaccionAdapter;
    GriasAdapter griasAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_venta, container, false);

        producto     = view.findViewById(R.id.textmanguera);
        cara     = view.findViewById(R.id.textcara);
        importetotal = view.findViewById(R.id.txtimporte);
        cajero = getActivity().findViewById(R.id.nombreuser);

        CardView grias        = view.findViewById(R.id.card);
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


        recyclerTransaccion = view.findViewById(R.id.recyclertransacciones);
        recyclerTransaccion.setLayoutManager(new LinearLayoutManager(getContext()));

        List<Transaccion> transaccionList = new ArrayList<>();

        for (int i = 0; i < 10; i++){
            transaccionList.add(new Transaccion("03","3","05","19.35","3.824","73.99"));
        }

        TransaccionAdapter transaccionAdapter = new TransaccionAdapter(transaccionList, getContext());
        recyclerTransaccion.setAdapter(transaccionAdapter);
        recyclerTransaccion.setLayoutManager(new LinearLayoutManager(getContext()));

        //Seleccion de Grias
        //    recyclerGrias = view.findViewById(R.id.recyclergrias);
        //    recyclerGrias.setLayoutManager(new LinearLayoutManager(getContext()));

        //Seleccion de Mangueras
        recyclerManguera = view.findViewById(R.id.recyclerlado);
        recyclerManguera.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        //Seleccion de Caras
        recyclerCara = view.findViewById(R.id.recycler);
        recyclerCara.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        findLados(GlobalInfo.getImei10);
        // findGrias(GlobalInfo.getImei10);

        return view;
    }
    /*  private void findGrias(String id) {

          Retrofit retrofit = new Retrofit.Builder()
                  .baseUrl("http://192.168.1.6:8081/")
                  .addConverterFactory(GsonConverterFactory.create())
                  .build();

          AppSvenAPI appSvenAPI = retrofit.create(AppSvenAPI.class);
          Call<List<Grias>> call = appSvenAPI.findGrias(id);

          call.enqueue(new Callback<List<Grias>>() {
              @Override
              public void onResponse(Call<List<Grias>> call, Response<List<Grias>> response) {
                  try {

                      if(!response.isSuccessful()){
                          Toast.makeText(getContext(), "Codigo de error: " + response.code(), Toast.LENGTH_SHORT).show();
                          return;
                      }

                      List<Grias> griasList = response.body();

                      griasAdapter = new GriasAdapter(griasList, getContext(), new GriasAdapter.OnItemClickListener() {
                          @Override
                          public void onItemClick(Grias item) {
                              txtproductogria =  getActivity().findViewById(R.id.txtproductogria);
                              String descripcionmanguera = item.getDescripcion();
                              txtproductogria.setText(descripcionmanguera);
                          }
                      });

                      recyclerGrias.setAdapter(griasAdapter);

                  }catch (Exception ex){
                      Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                  }
              }

              @Override
              public void onFailure(Call<List<Grias>> call, Throwable t) {
                  Toast.makeText(getContext(), "Error de conexión APICORE - RED - WIFI", Toast.LENGTH_SHORT).show();
              }
          });

      }*/
    private void findLados(String id) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.6:8081/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AppSvenAPI appSvenAPI = retrofit.create(AppSvenAPI.class);
        Call<List<Lados>> call = appSvenAPI.findLados(id);

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
                Toast.makeText(getContext(), "Error de conexión APICORE - RED - WIFI", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void findPico(String id){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.6:8081/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AppSvenAPI appSvenAPI = retrofit.create(AppSvenAPI.class);
        Call<List<Picos>> call = appSvenAPI.findPico(id);
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
                Toast.makeText(getContext(), "Error de conexión APICORE - RED - WIFI", Toast.LENGTH_SHORT).show();
            }
        });

    }
}