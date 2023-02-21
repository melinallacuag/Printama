package com.anggastudio.sample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.anggastudio.sample.WebApiSVEN.Controllers.APIService;
import com.anggastudio.sample.WebApiSVEN.Models.Company;
import com.anggastudio.sample.WebApiSVEN.Models.Terminal;
import com.anggastudio.sample.WebApiSVEN.Models.Users;
import com.anggastudio.sample.WebApiSVEN.Parameters.GlobalInfo;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Login extends AppCompatActivity {

    Button btniniciar;
    TextInputEditText usuario, contraseña;
    TextInputLayout alertuser,alertpassword;
    TextView imeii;

    String getPass10;

    private APIService mAPIService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ImageButton configuracion = findViewById(R.id.btnconfiguracion);

        configuracion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this,MainActivity.class));
            }
        });

        imeii = findViewById(R.id.imei);
        imeii.setText(ObtenerIMEI.getDeviceId(getApplicationContext()));

        btniniciar     = findViewById(R.id.btnlogin);
        usuario        = findViewById(R.id.usuario);
        contraseña     = findViewById(R.id.contraseña);
        alertuser      = findViewById(R.id.textusuario);
        alertpassword  = findViewById(R.id.textcontraseña);

        mAPIService = GlobalInfo.getAPIService();

        GlobalInfo.getImei10 = imeii.getText().toString();

        findTerminal(GlobalInfo.getImei10);

        //findCompany(GlobalInfo.getCompanyID10);

        btniniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usuarioUser    = usuario.getText().toString();
                String contraseñaUser = contraseña.getText().toString();

                if(usuarioUser.isEmpty()){
                    alertuser.setError("El campo usuario es obligatorio");
                }else if(contraseñaUser.isEmpty()){
                    alertpassword.setError("El campo contraseña es obligatorio");
                }else{
                    alertuser.setErrorEnabled(false);
                    alertpassword.setErrorEnabled(false);

                    findUsers(usuario.getText().toString());
                }
            }
        });
    }

    private void findUsers(String id){

        Call<List<Users>> call = mAPIService.findUsers(id);

        call.enqueue(new Callback<List<Users>>() {
            @Override
            public void onResponse(Call<List<Users>> call, Response<List<Users>> response) {

                try {

                    if(!response.isSuccessful()){
                        Toast.makeText(Login.this, "Codigo de error: " + response.code(), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    List<Users> usersList = response.body();

                    for(Users user: usersList){
                        usuario.setText(user.getUserID());
                        GlobalInfo.getName10 = user.getNames();
                        getPass10 = user.getPassword();
                    }

                    String getPass = checkpassword(contraseña.getText().toString());

                    if(getPass.equals(getPass10)){
                        Toast.makeText(Login.this, "Bienvenido al Sistema SVEN", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Login.this,Menu.class));
                    }
                    else {
                        Toast.makeText(Login.this, "El usuario o la contraseña son incorrectos", Toast.LENGTH_SHORT).show();
                    }


                }catch (Exception ex){
                    Toast.makeText(Login.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<List<Users>> call, Throwable t) {
                Toast.makeText(Login.this, "Error de conexión APICORE Users - RED - WIFI", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void findTerminal(String id){

        Call<List<Terminal>> call = mAPIService.findTerminal(id);

        call.enqueue(new Callback<List<Terminal>>() {
            @Override
            public void onResponse(Call<List<Terminal>> call, Response<List<Terminal>> response) {

                try {

                    if(!response.isSuccessful()){
                        Toast.makeText(Login.this, "Codigo de error: " + response.code(), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    List<Terminal> terminalList = response.body();

                    for(Terminal terminal: terminalList){

                        GlobalInfo.getterminalID10 = String.valueOf(terminal.getTerminalID());
                        GlobalInfo.getfecha10 = String.valueOf(terminal.getFecha_Proceso());
                        GlobalInfo.getturno10 = Integer.valueOf(terminal.getTurno());
                        GlobalInfo.getCompanyID10 = Integer.valueOf(terminal.getCompanyID());
                        GlobalInfo.getalmacenID10 = Integer.valueOf(terminal.getAlmacenID());
                        GlobalInfo.getconsultaSunat10 = Boolean.valueOf(terminal.getConsulta_Sunat());
                        GlobalInfo.getventaAutomatica10 = Boolean.valueOf(terminal.getVenta_Automatica());
                        GlobalInfo.getventaPlaya10 = Boolean.valueOf(terminal.getVenta_Playa());
                        GlobalInfo.getventaTienda10 = Boolean.valueOf(terminal.getVenta_Tienda());
                        GlobalInfo.getventaCredito10 = Boolean.valueOf(terminal.getVenta_Credito());
                        GlobalInfo.getventaTarjeta10 = Boolean.valueOf(terminal.getVenta_Tarjeta());
                        GlobalInfo.getventaGratuita10 = Boolean.valueOf(terminal.getVenta_Gratuita());
                        GlobalInfo.getventaSerafin10 = Boolean.valueOf(terminal.getVenta_Serafin());

                    }

                }catch (Exception ex){
                    Toast.makeText(Login.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<List<Terminal>> call, Throwable t) {
                Toast.makeText(Login.this, "Error de conexión APICORE Terminal - RED - WIFI", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void findCompany(Integer id){

        Call<List<Company>> call = mAPIService.findCompany(id);

        call.enqueue(new Callback<List<Company>>() {
            @Override
            public void onResponse(Call<List<Company>> call, Response<List<Company>> response) {
                try {

                    if(!response.isSuccessful()){
                        Toast.makeText(Login.this, "Codigo de error: " + response.code(), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    List<Company> companyList = response.body();

                    for(Company company: companyList){

                        GlobalInfo.getNameCompany10 = String.valueOf(company.getNames());
                        GlobalInfo.getBranchCompany10 = String.valueOf(company.getBranch());
                        GlobalInfo.getSloganCompany10 = String.valueOf(company.getEslogan());

                    }

                }catch (Exception ex){
                    Toast.makeText(Login.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Company>> call, Throwable t) {
                Toast.makeText(Login.this, "Error de conexión APICORE Company - RED - WIFI", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String checkpassword(String clave){

        String lResult = "";
        String lasc1 = "";

        int lValor = 0;
        int lTam = 0;
        int lCar = 0;
        int lasc2 = 0;

        lTam = clave.length();

        for(int lcont = 1 ; lcont <= lTam; lcont += 1){

            switch (lcont){
                case 1:
                    lCar = 1;
                    lasc1 = clave.substring(0,1);
                    lasc2 = lasc1.charAt(0);
                    break;
                case 2:
                    lCar = 3;
                    lasc1 = clave.substring(1,2);
                    lasc2 = lasc1.charAt(0);
                    break;
                case 3:
                    lCar = 5;
                    lasc1 = clave.substring(2,3);
                    lasc2 = lasc1.charAt(0);
                    break;
                case 4:
                    lCar = 7;
                    lasc1 = clave.substring(3,4);
                    lasc2 = lasc1.charAt(0);
                    break;
                case 5:
                    lCar = 9;
                    lasc1 = clave.substring(4,5);
                    lasc2 = lasc1.charAt(0);
                    break;
                case 6:
                    lCar = 11;
                    lasc1 = clave.substring(5,6);
                    lasc2 = lasc1.charAt(0);
                    break;
            }

            lValor = lValor + lasc2 * lCar;

        }

        lResult = String.valueOf(lValor);

        return lResult;
    }

}