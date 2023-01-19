package com.anggastudio.sample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.anggastudio.printama.Printama;
import com.anggastudio.sample.mock.Mock;
import com.anggastudio.sample.model.PrintBody;
import com.anggastudio.sample.model.PrintHeader;
import com.anggastudio.sample.model.PrintModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class Login extends AppCompatActivity {
    Button btniniciar;
    TextInputEditText usuario, contraseña;
    TextInputLayout alertuser,alertpassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btniniciar     = findViewById(R.id.btnlogin);
        usuario        = findViewById(R.id.usuario);
        contraseña     = findViewById(R.id.contraseña);
        alertuser      = findViewById(R.id.textusuario);
        alertpassword  = findViewById(R.id.textcontraseña);

        ImageButton configuracion = findViewById(R.id.btnconfiguracion);

        configuracion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this,MainActivity.class));
            }
        });

        btniniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usuarioUser    = usuario.getText().toString().trim();
                String contraseñaUser = contraseña.getText().toString().trim();

                if (usuarioUser.isEmpty()){
                    alertuser.setError("Ingresar usuario");
                }else{
                    alertuser.setErrorEnabled(false);
                }
                if (contraseñaUser.isEmpty()){
                    alertpassword.setError("Ingresar contraseña");
                }else{
                    alertpassword.setErrorEnabled(false);
                    startActivity(new Intent(Login.this,Menu.class));
                }
                if (usuarioUser.length()>10){
                    alertuser.setError("Debe ser menor a 10 digitos");
                }else {
                    alertuser.setErrorEnabled(false);
                }
                if (contraseñaUser.length()>6){
                    alertpassword.setError("Debe ser menor a 6 digitos");
                }else {
                    alertpassword.setErrorEnabled(false);
                }
            }
        });
    }
}