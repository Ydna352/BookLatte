package com.andrea.booklatte;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    private ProgressBar pgbProgreso;

    private Handler handler = new Handler(Looper.getMainLooper())
    {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            pgbProgreso.incrementProgressBy(msg.arg1);//aumenta el progresa de la barra lo que te devuelve el mensaje
            //arg1 es donde el hilo guarda lo que va avanzando {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        pgbProgreso = findViewById(R.id.pgbProgreso);
        pgbProgreso.setProgress(0);

        Thread timer = new Thread(){
            @Override
            public void run()
            {
                super.run();
                try // si todo va bien se ejecuta este
                {
                    //simular que cada medio segundo avanzamos 10% en la Barra de progreso
                    for(int i =0; i<=10; i++) { //cada 10 voy a ir avanzando en el proceso medio segundo
                        sleep(500); //irte a dormir sin hacer nada para no consumir recursos del procesador
                        Message msg = Message.obtain(); //saca el mensaje de la pila de mensajes del looper
                        msg.arg1 = 10;
                        handler.sendMessage(msg); //mando al handler el mensaje de avance del proceso
                    }
                }
                catch (InterruptedException e) //si el try arrojó erroe, se ejecuta este
                {

                }
                finally //independientemente de lo que pase se ejecuta este
                {
                    Intent intent = new Intent(SplashActivity.this, IniciarSesion.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
        timer.start(); //comienza el hilo
    }

    @Override //si pierdo la pantalla (paso por on Pause) pierdo el sonido
    protected void onPause() {
        super.onPause();
    }

}
