package com.anggastudio.sample;

import static android.text.TextUtils.isEmpty;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.anggastudio.sample.WebApiSVEN.Controllers.APIService;
import com.anggastudio.sample.WebApiSVEN.Models.Picos;
import com.anggastudio.sample.WebApiSVEN.Parameters.GlobalInfo;
import com.google.android.material.textfield.TextInputLayout;

import java.math.BigDecimal;
import java.math.RoundingMode;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class SolesFragment extends DialogFragment {

    Button btncancelar, agregar;
    EditText montosoles;
    TextInputLayout alertsoles;
    TextView textsol;

    private APIService mAPIService;

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_soles, container, false);

        btncancelar   = view.findViewById(R.id.btncancelarsoles);
        agregar       = view.findViewById(R.id.btnagregarsoles);
        montosoles    = view.findViewById(R.id.montosoles);
        alertsoles    = view.findViewById(R.id.textsoles);

        mAPIService = GlobalInfo.getAPIService();

        btncancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { dismiss();}
        });


        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                textsol = getActivity().findViewById(R.id.txtimporte);

                if(isEmpty(montosoles.getText().toString())){
                    alertsoles.setError("El campo soles es obligatorio");
                    return;
                }

                BigDecimal bd = new BigDecimal(montosoles.getText().toString());
                bd = bd.setScale(2, RoundingMode.HALF_UP);

                textsol.setText(bd.toString());

                String textsol = montosoles.getText().toString();

                Double solesmonto = Double.parseDouble(textsol);

                int numsol   = Integer.parseInt(textsol);

                if (numsol < 5){
                    alertsoles.setError("El valor debe ser mínimo 5.00");
                }else if(9999 < numsol){
                    alertsoles.setError("El valor debe ser maximo 9999");
                }else {
                    guardar_monto(GlobalInfo.getPistola10,solesmonto);
                    alertsoles.setErrorEnabled(false);
                    Toast.makeText(getContext(), "SE AGREGO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                    dismiss();
                }

            }
        });

        return view;
    }

    private void guardar_monto(String manguera, Double valor){

        final Picos picos = new Picos(manguera,"01","1","05","DB5","G",valor);

        Call<Picos> call = mAPIService.postPicos(picos);

        call.enqueue(new Callback<Picos>() {
            @Override
            public void onResponse(Call<Picos> call, Response<Picos> response) {

                if(!response.isSuccessful()){
                    Toast.makeText(getContext(), "Codigo de error: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

            }

            @Override
            public void onFailure(Call<Picos> call, Throwable t) {
                Toast.makeText(getContext(), "Error de conexión APICORE", Toast.LENGTH_SHORT).show();
            }
        });

    }

}