package com.anggastudio.sample;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.text.AllCapsTransformationMethod;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.anggastudio.sample.Adapter.DetalleVentaAdapter;
import com.anggastudio.sample.Adapter.TipoTarjetaAdapter;
import com.anggastudio.sample.WebApiSVEN.Models.DetalleVenta;
import com.anggastudio.sample.WebApiSVEN.Models.Tipotarjeta;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;


public class BoletaFragment extends DialogFragment {


    Activity actividad;
    Spinner comboModo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_boleta, container, false);



        comboModo = (Spinner) view.findViewById(R.id.dropStatus);
        ArrayList<String> listaModo = new ArrayList<>();
        listaModo.add("Todos");
        listaModo.add("Intentos");
        listaModo.add("Tiempo");

ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),R.array.modo_juego,android.R.layout.simple_list_item_1);
        comboModo.setAdapter(adapter);
      //  ArrayAdapter<CharSequence> adapterModo=new ArrayAdapter(actividad, android.R.layout.simple_spinner_dropdown_item,listaModo);
    //    comboModo.setAdapter(adapterModo);



        return view;
    }



}