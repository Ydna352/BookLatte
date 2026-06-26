package com.andrea.booklatte;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CrearCuenta extends AppCompatActivity {
    private EditText edtNombreUsuario, edtEmail, edtPassword;
    private Button btnCrearCuenta;
    private DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_cuenta);

        edtNombreUsuario = findViewById(R.id.edtNombreUsuario);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);

        btnCrearCuenta = findViewById(R.id.btnCrearCuenta);

        db = new DBHelper(this);
        btnCrearCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombre = edtNombreUsuario.getText().toString();
                String email = edtEmail.getText().toString();
                String password = edtPassword.getText().toString();

                if(nombre.isEmpty()||email.isEmpty()||password.isEmpty())
                {
                    // usamos un toast para colocar un mensaje pequeño que aparecera en la pantalla
                    Toast.makeText(CrearCuenta.this,"Completar todos los campos solicitados",Toast.LENGTH_SHORT).show();
                    return;
                }

                boolean registrado = db.insertarUsuario(nombre,email,password);

                if(registrado)
                {
                    //usamos un toast para colocar un mensaje pequeño que aparecera en la pantalla
                    Toast.makeText(CrearCuenta.this,"Cuenta creada exitosamente",Toast.LENGTH_SHORT).show();
                    finish(); // Vuelve a iniciar sesión
                }

                else{
                    Toast.makeText(CrearCuenta.this,"Error al crear cuenta",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}