package com.anggastudio.sample.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.anggastudio.printama.Printama;
import com.anggastudio.sample.Adapter.VContometroAdapter;
import com.anggastudio.sample.Adapter.VProductoAdapter;
import com.anggastudio.sample.Adapter.VTipoPagoAdapter;
import com.anggastudio.sample.R;
import com.anggastudio.sample.WebApiSVEN.Models.VContometro;
import com.anggastudio.sample.WebApiSVEN.Models.VProducto;
import com.anggastudio.sample.WebApiSVEN.Models.VTipoPago;
import com.anggastudio.sample.WebApiSVEN.Parameters.GlobalInfo;

import java.util.ArrayList;
import java.util.List;


public class CierreXFragment extends Fragment {

    TextView TotalImprsionX,TotalDocAnulados,DocAnulados,Total,IGV,SubTotal,NroDespacho,Cajero,Turno,FechaTrabajo,FechaHoraFin,FechaHoraIni,TotalVolumenContometro;

    VContometroAdapter vContometroAdapter;
    RecyclerView recyclerVContometro;
    List<VContometro> vContometroList;

    VProductoAdapter vProductoAdapter;
    RecyclerView recyclerVProducto;
    List<VProducto> vProductoList;

    VTipoPagoAdapter vTipoPagoAdapter;
    RecyclerView recyclerVTipoPago;
    List<VTipoPago> vTipoPagoList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_cierre_x, container, false);

        TotalImprsionX      = view.findViewById(R.id.TotalImprsionX);
        TotalDocAnulados    = view.findViewById(R.id.TotalDocAnulados);
        DocAnulados         = view.findViewById(R.id.DocAnulados);
        Total               = view.findViewById(R.id.Total);
        IGV                 = view.findViewById(R.id.IGV);
        SubTotal            = view.findViewById(R.id.SubTotal);
        NroDespacho         = view.findViewById(R.id.NroDespacho);
        Cajero              = view.findViewById(R.id.Cajero);
        Turno               = view.findViewById(R.id.Turno);
        FechaTrabajo        = view.findViewById(R.id.FechaTrabajo);
        FechaHoraFin        = view.findViewById(R.id.FechaHoraFin);
        FechaHoraIni        = view.findViewById(R.id.FechaHoraIni);
        TotalVolumenContometro = view.findViewById(R.id.TotalVolumenContometro);

        FechaHoraIni.setText(GlobalInfo.getterminalFecha10);
        FechaHoraFin.setText("21/03/2023 12:27:50");
        FechaTrabajo.setText("21/03/2023");
        Turno.setText(GlobalInfo.getterminalTurno10.toString());
        Cajero.setText(GlobalInfo.getuserName10);
        NroDespacho.setText("3");
        SubTotal.setText("199.15");
        IGV.setText("39.15");
        Total.setText("235.15");
        DocAnulados.setText("0");
        TotalDocAnulados.setText("0.00");
        TotalImprsionX.setText("1");
        TotalVolumenContometro.setText("13.468");


        /** VENTA POR  CONTOMETRO */
        recyclerVContometro = view.findViewById(R.id.recyclerVContometro);
        recyclerVContometro.setLayoutManager(new LinearLayoutManager(getContext()));

        vContometroList = new ArrayList<>();

        for (int i = 0; i < 1; i++){
            vContometroList.add(new VContometro("01","DB5","221436.283","221447.718","11.435"));
            vContometroList.add(new VContometro("02","G84","2069.288","2069.288","0.000"));
            vContometroList.add(new VContometro("01","DB5","221436.283","221447.718","11.435"));
            vContometroList.add(new VContometro("02","G84","2069.288","2069.288","0.000"));
        }


        vContometroAdapter = new VContometroAdapter(vContometroList, getContext());

        recyclerVContometro.setAdapter(vContometroAdapter);
        recyclerVContometro.setLayoutManager(new LinearLayoutManager(getContext()));

            /** VENTA POR  PRODUCTO */

        recyclerVProducto = view.findViewById(R.id.recyclerVProductos);
        recyclerVProducto.setLayoutManager(new LinearLayoutManager(getContext()));

        vProductoList = new ArrayList<>();

        for (int i = 0; i < 1; i++){
            vProductoList.add(new VProducto("DIESEL B5 S50","11.435","200.00"," "));
            vProductoList.add(new VProducto("DIESEL B5 S50","11.435","200.00"," "));
            vProductoList.add(new VProducto("DIESEL B5 S50","11.435","200.00"," "));
            vProductoList.add(new VProducto("DIESEL B5 S50","11.435","200.00"," "));
        }


        vProductoAdapter = new VProductoAdapter(vProductoList, getContext());

        recyclerVProducto.setAdapter(vProductoAdapter);
        recyclerVProducto.setLayoutManager(new LinearLayoutManager(getContext()));

        /** TIPO DE PAGO */

        recyclerVTipoPago = view.findViewById(R.id.recyclerVTipoPago);
        recyclerVTipoPago.setLayoutManager(new LinearLayoutManager(getContext()));

        vTipoPagoList = new ArrayList<>();

        for (int i = 0; i < 1; i++){
            vTipoPagoList.add(new VTipoPago("EFECTIVO                      ","215.00"));
            vTipoPagoList.add(new VTipoPago("TARJETA                       ","20.00"));
          }


        vTipoPagoAdapter = new VTipoPagoAdapter(vTipoPagoList, getContext());

        recyclerVTipoPago.setAdapter(vTipoPagoAdapter);
        recyclerVTipoPago.setLayoutManager(new LinearLayoutManager(getContext()));


        view.findViewById(R.id.imprimircierrex).setOnClickListener(v -> cierrex());
        return view;
    }
    private  void cierrexj() {
        String importe = "120.00";
        Printama.with(getContext()).connect(printama -> {
            printama.setSmallText();
            printama.addNewLine(1);
            printama.printText("VENTAS POR CARA\n", Printama.CENTER);
            printama.printTextJustify("Cara Nro.1","120.00\n");
            printama.printTextJustify("T.Gratuita","0.00\n");
            printama.addNewLine(1);
            printama.printTextln("Total S/: "+ importe, Printama.RIGHT);
            printama.setNormalText();
            printama.printDoubleDashedLine();
            printama.setSmallText();
            printama.printText("VENTAS POR TIPO DE PAGO\n", Printama.CENTER);
            printama.printTextJustify("EFECTIVO", String.valueOf(GlobalInfo.getoptranSoles10));
            printama.printTextJustify("TARJETA DE CREDITO","20.00\n");
            printama.printTextJustify("SERAFINES","0.00\n");
            printama.printTextJustify("T.Gratuita","0.00\n");
            printama.addNewLine(1);
            printama.printTextln("Total Neto S/: "+ importe, Printama.RIGHT);
            printama.setNormalText();
            printama.printDoubleDashedLine();
            printama.setSmallText();
            printama.printTextJustify("Tot.Descuento","0.00\n");
            printama.printTextJustify("Tot.Incremento","0.00\n");
            printama.addNewLine(1);
            printama.printTextln("Total Bruto S/: "+ importe, Printama.RIGHT);
            printama.feedPaper();
            printama.close();
        }, this::showToast);
        DasboardFragment dasboardFragment  = new DasboardFragment();
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,dasboardFragment).commit();
    }
    private void cierrex() {
        View view = getView().findViewById(R.id.linearLayout2);
        Printama.with(getContext()).connect(printama -> {
            printama.printFromView(view);
            new Handler().postDelayed(printama::close, 2000);
        }, this::showToast);
    }
    private void showToast(String message) {
        Toast.makeText(getContext(), "Conectar Bluetooth", Toast.LENGTH_SHORT).show();
    }
}