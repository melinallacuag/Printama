package com.anggastudio.sample.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.anggastudio.printama.Printama;
import com.anggastudio.sample.Adapter.CaraAdapter;
import com.anggastudio.sample.Adapter.ClienteAdapter;
import com.anggastudio.sample.Adapter.DetalleVentaAdapter;
import com.anggastudio.sample.Adapter.ListaComprobanteAdapter;
import com.anggastudio.sample.Login;
import com.anggastudio.sample.R;
import com.anggastudio.sample.WebApiSVEN.Controllers.APIService;
import com.anggastudio.sample.WebApiSVEN.Models.Anular;
import com.anggastudio.sample.WebApiSVEN.Models.CTurno;
import com.anggastudio.sample.WebApiSVEN.Models.Cliente;
import com.anggastudio.sample.WebApiSVEN.Models.Descuentos;
import com.anggastudio.sample.WebApiSVEN.Models.DetalleVenta;
import com.anggastudio.sample.WebApiSVEN.Models.ListaComprobante;
import com.anggastudio.sample.WebApiSVEN.Models.Picos;
import com.anggastudio.sample.WebApiSVEN.Models.Placa;
import com.anggastudio.sample.WebApiSVEN.Parameters.GlobalInfo;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ListaComprobantesFragment extends Fragment  {

    private APIService mAPIService;

    RecyclerView recyclerLComprobante ;
    ListaComprobanteAdapter listaComprobanteAdapter;
    List<ListaComprobante> listaComprobanteList;

    private Dialog modalReimpresion;
    Button btncancelar,btnaceptar,btnanular;

    SearchView buscadorRSocial;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista_comprobantes, container, false);
        mAPIService  = GlobalInfo.getAPIService();

        buscadorRSocial   = view.findViewById(R.id.searchView);




        /** Listado de Consulta Venta  */
        recyclerLComprobante = view.findViewById(R.id.recyclerListaComprobante);
        recyclerLComprobante.setLayoutManager(new LinearLayoutManager(getContext()));

        /** Mostrar Modal de Cambio de Turno */
        modalReimpresion = new Dialog(getContext());
        modalReimpresion.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        modalReimpresion.setContentView(R.layout.modal_reimprimir);
        modalReimpresion.setCancelable(false);

        /** API Retrofit - Consumiendo */
        findConsultarVenta(GlobalInfo.getterminalID10);

        buscadorRSocial.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if (listaComprobanteList.isEmpty()) {

                    Toast.makeText(getContext(), "No se encontró el dato", Toast.LENGTH_SHORT).show();

                }

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                listaComprobanteAdapter.filtrado(newText);
                return false;
            }
        });

        return view;
    }

    private void findConsultarVenta(String id){

        Call<List<ListaComprobante>> call = mAPIService.findConsultarVenta(id);

        call.enqueue(new Callback<List<ListaComprobante>>() {
            @Override
            public void onResponse(Call<List<ListaComprobante>> call, Response<List<ListaComprobante>> response) {

                try {

                    if(!response.isSuccessful()){
                        Toast.makeText(getContext(), "Codigo de error: " + response.code(), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    List<ListaComprobante> listaComprobanteList = response.body();

                    listaComprobanteAdapter = new ListaComprobanteAdapter(listaComprobanteList, getContext(),new ListaComprobanteAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(ListaComprobante item) {

                            moveToDescription(item);

                            modalReimpresion.show();

                            final FragmentManager fragmentManager = getFragmentManager();

                            btncancelar    = modalReimpresion.findViewById(R.id.btncancelarreimpresion);
                            btnaceptar     = modalReimpresion.findViewById(R.id.btnagregarreimpresion);
                            btnanular      = modalReimpresion.findViewById(R.id.btnanular);

                            btnanular.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    if (GlobalInfo.getconsultaventaAnulado10.equals("NO")) {

                                        Anular(GlobalInfo.getconsultaventaTipoDocumentoID10,GlobalInfo.getconsultaventaSerieDocumento10 ,GlobalInfo.getconsultaventaNroDocumento10,GlobalInfo.getuserID10);

                                        fragmentManager.popBackStack();

                                        modalReimpresion.dismiss();

                                    } else {
                                        Toast.makeText(getContext(), "Documento se encuntra anulado", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });

                            btncancelar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    modalReimpresion.dismiss();
                                }
                            });

                            btnaceptar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Printama.with(getContext()).connect(printama -> {

                                        printama.setSmallText();
                                        printama.printText("small___________");
                                        printama.printTextln("TEXTtext");

                                        printama.setNormalText();
                                        printama.printText("normal__________");
                                        printama.printTextln("TEXTtext");


                                        printama.setNormalText();
                                        printama.feedPaper();
                                        printama.close();

                                    });
                                    modalReimpresion.dismiss();
                                }
                            });


                        }
                    });

                    recyclerLComprobante.setAdapter(listaComprobanteAdapter);


                }catch (Exception ex){
                    Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ListaComprobante>> call, Throwable t) {
                Toast.makeText(getContext(), "Error de conexión APICORE Consulta Venta - RED - WIFI", Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void moveToDescription(ListaComprobante item){

        GlobalInfo.getconsultaventaTipoDocumentoID10  = item.getTipoDocumento();
        GlobalInfo.getconsultaventaSerieDocumento10   = item.getSerieDocumento();
        GlobalInfo.getconsultaventaNroDocumento10     = item.getNroDocumento();
        GlobalInfo.getconsultaventaAnulado10          = item.getAnulado();

        Toast.makeText(getContext(),  GlobalInfo.getconsultaventaAnulado10, Toast.LENGTH_SHORT).show();
    }

    private void Anular(String tipodoc, String seriedoc, String nrodoc, String anuladoid){

        Call<Anular> call = mAPIService.postAnular(tipodoc,seriedoc,nrodoc,anuladoid);

        call.enqueue(new Callback<Anular>() {
            @Override
            public void onResponse(Call<Anular> call, Response<Anular> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(getContext(), "Codigo de error: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            @Override
            public void onFailure(Call<Anular> call, Throwable t) {
                Toast.makeText(getContext(), "Error de conexión APICORE", Toast.LENGTH_SHORT).show();

            }
        });

    }


}
