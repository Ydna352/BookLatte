package com.andrea.booklatte;

import android.content.Context; // IMPORTANTE: Agregar esto
import android.content.Intent;
import android.content.SharedPreferences; // IMPORTANTE: Agregar esto
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class IniciarSesion extends AppCompatActivity {
    private EditText edtEmail, edtPassword;
    private Button btnInicioSesion, btnRegistrate;
    private DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion);

        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnRegistrate = findViewById(R.id.btnRegistrate);
        btnInicioSesion = findViewById(R.id.btnInicioSesion);

        db = new DBHelper(this);

        btnRegistrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IniciarSesion.this, CrearCuenta.class);
                startActivity(intent);
            }
        });

        btnInicioSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edtEmail.getText().toString();
                String password = edtPassword.getText().toString();

                if(email.isEmpty()||password.isEmpty())
                {
                    Toast.makeText(IniciarSesion.this,"Completar todos los campos",Toast.LENGTH_SHORT).show();
                    return;
                }

                //obtenemos el id desde la base de datos
                int idUsuario = db.validarUsuario(email, password);

                if(idUsuario != -1)
                {
                    SharedPreferences preferences = getSharedPreferences("SesionUsuario", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putInt("idUsuario", idUsuario); //guardar el id real
                    editor.apply();//confirmacion de guardado

                    Toast.makeText(IniciarSesion.this,"Inicio de sesión exitoso",Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(IniciarSesion.this, PantalladeCarga.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(IniciarSesion.this,"Credenciales incorrectas",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}