package com.anggastudio.sample.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.anggastudio.printama.Printama;
import com.anggastudio.sample.Adapter.CaraAdapter;
import com.anggastudio.sample.Adapter.ClienteAdapter;
import com.anggastudio.sample.Adapter.ListaComprobanteAdapter;
import com.anggastudio.sample.Login;
import com.anggastudio.sample.R;
import com.anggastudio.sample.WebApiSVEN.Models.DetalleVenta;
import com.anggastudio.sample.WebApiSVEN.Models.ListaComprobante;
import com.anggastudio.sample.WebApiSVEN.Parameters.GlobalInfo;

import java.util.ArrayList;
import java.util.List;


public class ListaComprobantesFragment extends Fragment  implements  SearchView.OnQueryTextListener {

    RecyclerView recyclerLComprobante ;
    ListaComprobanteAdapter listaComprobanteAdapter;
    List<ListaComprobante> listaComprobanteList;

    private Dialog modalReimpresion;
    Button btncancelar,btnaceptar;

    SearchView buscadorRSocial;

    List<ListaComprobante> listatemporal = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista_comprobantes, container, false);

        buscadorRSocial   = view.findViewById(R.id.searchView);

        buscadorRSocial.setOnQueryTextListener(this);

        listatemporal = listaComprobanteList;


        recyclerLComprobante = view.findViewById(R.id.recyclerListaComprobante);
        recyclerLComprobante.setLayoutManager(new LinearLayoutManager(getContext()));

        listaComprobanteList = new ArrayList<>();

        for (int i = 0; i < 1; i++){

            listaComprobanteList.add(new ListaComprobante("12/05/2022 09:54","10759912748","HUMERTO PORRAS",30.00,"NO"));
            listaComprobanteList.add(new ListaComprobante("13/05/2022 09:50","10759452748","MANUEL ROJAS",20.00,"NO"));
            listaComprobanteList.add(new ListaComprobante("14/05/2022 09:45","10784912748","PAUL ROJAS",5.00,"NO"));
            listaComprobanteList.add(new ListaComprobante("15/05/2022 09:40","10710912748","JOSE ROJAS",15.00,"NO"));
            listaComprobanteList.add(new ListaComprobante("16/05/2022 09:35","10559912748","CAMILA ROJAS",18.00,"NO"));
            listaComprobanteList.add(new ListaComprobante("17/05/2022 09:30","10758412748","JUANA ROJAS",100.00,"NO"));
            listaComprobanteList.add(new ListaComprobante("18/05/2022 09:25","10759312748","LUZ ROJAS",25.00,"NO"));
            listaComprobanteList.add(new ListaComprobante("19/05/2022 09:22","10759955748","PAULA ROJAS",90.00,"NO"));
            listaComprobanteList.add(new ListaComprobante("20/05/2022 09:10","10700912748","JUANA ROJAS",66.00,"NO"));

            listaComprobanteList.add(new ListaComprobante("12/05/2022 09:54","10759912748","HUMERTO ROJAS",30.00,"NO"));
            listaComprobanteList.add(new ListaComprobante("13/05/2022 09:50","10759452748","MANUEL ROJAS",20.00,"NO"));
            listaComprobanteList.add(new ListaComprobante("14/05/2022 09:45","10784912748","PAUL ROJAS",5.00,"NO"));
            listaComprobanteList.add(new ListaComprobante("15/05/2022 09:40","10710912748","JOSE ROJAS",15.00,"NO"));
            listaComprobanteList.add(new ListaComprobante("16/05/2022 09:35","10559912748","CAMILA ROJAS",18.00,"NO"));
            listaComprobanteList.add(new ListaComprobante("17/05/2022 09:30","10758412748","JUANA ROJAS",100.00,"NO"));
            listaComprobanteList.add(new ListaComprobante("18/05/2022 09:25","10759312748","LUZ ROJAS",25.00,"NO"));
            listaComprobanteList.add(new ListaComprobante("19/05/2022 09:22","10759955748","PAULA ROJAS",90.00,"NO"));
            listaComprobanteList.add(new ListaComprobante("20/05/2022 09:10","10700912748","JUANA ROJAS",66.00,"NO"));

            listaComprobanteList.add(new ListaComprobante("12/05/2022 09:54","10759912748","HUMERTO ROJAS",30.00,"NO"));
            listaComprobanteList.add(new ListaComprobante("13/05/2022 09:50","10759452748","MANUEL ROJAS",20.00,"NO"));
            listaComprobanteList.add(new ListaComprobante("14/05/2022 09:45","10784912748","PAUL ROJAS",5.00,"NO"));
            listaComprobanteList.add(new ListaComprobante("15/05/2022 09:40","10710912748","JOSE ROJAS",15.00,"NO"));
            listaComprobanteList.add(new ListaComprobante("16/05/2022 09:35","10559912748","CAMILA ROJAS",18.00,"NO"));
            listaComprobanteList.add(new ListaComprobante("17/05/2022 09:30","10758412748","JUANA ROJAS",100.00,"NO"));
            listaComprobanteList.add(new ListaComprobante("18/05/2022 09:25","10759312748","LUZ ROJAS",25.00,"NO"));
            listaComprobanteList.add(new ListaComprobante("19/05/2022 09:22","10759955748","PAULA ROJAS",90.00,"NO"));
            listaComprobanteList.add(new ListaComprobante("20/05/2022 09:10","10700912748","JUANA ROJAS",66.00,"NO"));
        }


        listaComprobanteAdapter = new ListaComprobanteAdapter(listaComprobanteList, getContext(),new ListaComprobanteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ListaComprobante item) {

                /** Mostrar Modal de Cambio de Turno */
                modalReimpresion = new Dialog(getContext());
                modalReimpresion.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                modalReimpresion.setContentView(R.layout.modal_reimprimir);
                modalReimpresion.setCancelable(false);

                modalReimpresion.show();

                btncancelar    = modalReimpresion.findViewById(R.id.btncancelarreimpresion);
                btnaceptar    = modalReimpresion.findViewById(R.id.btnagregarreimpresion);

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
                    }
                });


            }
        });

        recyclerLComprobante.setAdapter(listaComprobanteAdapter);
        recyclerLComprobante.setLayoutManager(new LinearLayoutManager(getContext()));



        return view;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        listaComprobanteAdapter.filtrado(s);
        return false;
    }

}
