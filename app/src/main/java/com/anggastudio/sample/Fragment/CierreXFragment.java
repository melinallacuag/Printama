package com.anggastudio.sample.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.anggastudio.printama.Printama;
import com.anggastudio.sample.R;


public class CierreXFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_cierre_x, container, false);

        view.findViewById(R.id.imprimircierrex).setOnClickListener(v -> cierrex());
        return view;
    }
    private  void cierrex() {
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
            printama.printTextJustify("EFECTIVO","100.00\n");
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
   /* private void cierrex() {
        View view = getView().findViewById(R.id.linearLayout2);
        Printama.with(getContext()).connect(printama -> {
            printama.printFromView(view);
            new Handler().postDelayed(printama::close, 2000);
        }, this::showToast);
    }*/
    private void showToast(String message) {
        Toast.makeText(getContext(), "Conectar Bluetooth", Toast.LENGTH_SHORT).show();
    }
}