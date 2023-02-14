package com.anggastudio.sample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
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
    TextView imeii;
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
        imeii.setText("IMEI: " + ObtenerIMEI.getDeviceId(getApplicationContext()));

        btniniciar     = findViewById(R.id.btnlogin);
        usuario        = findViewById(R.id.usuario);
        contraseña     = findViewById(R.id.contraseña);
        alertuser      = findViewById(R.id.textusuario);
        alertpassword  = findViewById(R.id.textcontraseña);

        btniniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usuarioUser    = usuario.getText().toString();
                String contraseñaUser = contraseña.getText().toString();

                if(usuarioUser.isEmpty()){
                    alertuser.setError("El campo usuario es obligatorio");
                }else if(contraseñaUser.isEmpty()){
                    alertpassword.setError("El campo contraseña es obligatorio");
                }else if(!usuarioUser.equals("2023")|| !contraseñaUser.equals("admin")){
                    Toast.makeText(Login.this, "El usuario o la contraseña son incorrectos", Toast.LENGTH_SHORT).show();
                }else{
                    alertuser.setErrorEnabled(false);
                    alertpassword.setErrorEnabled(false);
                    startActivity(new Intent(Login.this,Menu.class));
                    Toast.makeText(Login.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}