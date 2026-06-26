package com.andrea.booklatte;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProfileFragment extends Fragment {

    private static final int REQUEST_IMAGE_PICK = 100;

    private int idUsuarioActual;
    //variables de la interfaz
    private ImageView fotoPerfil;
    private TextView txtUsername;
    private EditText edtBiografia;
    private Button btnAgregarResenia;
    private RecyclerView recyclerResenas;

    //variables de logica
    private DBHelper db;
    private ResenaAdapter adapter;


    public ProfileFragment() {
       //constructor vacio
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        android.content.SharedPreferences prefs = requireActivity().getSharedPreferences("SesionUsuario", android.content.Context.MODE_PRIVATE);
        idUsuarioActual = prefs.getInt("idUsuario", -1);

        if (idUsuarioActual == -1) {
            Toast.makeText(getContext(), "Error de sesión", Toast.LENGTH_SHORT).show();
        }

        db = new DBHelper(getContext());

        fotoPerfil = view.findViewById(R.id.fotoPerfil);
        txtUsername = view.findViewById(R.id.edtUsername);
        edtBiografia = view.findViewById(R.id.edtBiografia);
        btnAgregarResenia = view.findViewById(R.id.btnAgregarResenia);
        recyclerResenas = view.findViewById(R.id.recycler_resenas_perfil);


        recyclerResenas.setLayoutManager(new LinearLayoutManager(getContext()));


        cargarDatosDelPerfil();
        cargarResenasDelUsuario();

        fotoPerfil.setOnClickListener(v -> abrirGaleria());

       //para no perder la biografia
        edtBiografia.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String nuevaBio = edtBiografia.getText().toString();
                db.actualizarBiografia(idUsuarioActual, nuevaBio);
            }
        });


        btnAgregarResenia.setOnClickListener(v -> {


            AgregarResenaDialog dialog = new AgregarResenaDialog(idUsuarioActual, new AgregarResenaDialog.OnResenaGuardadaListener() {
                @Override
                public void onResenaGuardada() {
                    cargarResenasDelUsuario();
                }
            });

            dialog.show(getParentFragmentManager(), "AgregarResenaDialog");
        });

        return view;
    }
    private void abrirGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_IMAGE_PICK);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_PICK && data != null) {
            try {
                Uri imageUri = data.getData();

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imageUri);

               //mostramos en la interfaz
                fotoPerfil.setImageBitmap(bitmap);

              //conversion a bytes
                byte[] bytes = DBHelper.getBytesFromBitmap(bitmap);
                db.actualizarFoto(idUsuarioActual, bytes);


                // guardar en BD
                //db.actualizarPerfil(idUsuarioActual, bytes, edtBiografia.getText().toString());

                Toast.makeText(getContext(), "Foto de perfil actualizada", Toast.LENGTH_SHORT).show();

            } catch (Exception e) {
                Toast.makeText(getContext(), "Error al cargar la imagen", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //RECARGAR DATOS
        cargarResenasDelUsuario();
    }

    private void cargarDatosDelPerfil() {
        //cargamos la biografia
        String biografia = db.obtenerBiografia(idUsuarioActual);
        if (biografia != null && !biografia.isEmpty()) {
            edtBiografia.setText(biografia);
        }

        //cargar foto de blop a bitmap
        byte[] fotoBytes = db.obtenerFotoUsuario(idUsuarioActual);
        if (fotoBytes != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(fotoBytes, 0, fotoBytes.length);
            fotoPerfil.setImageBitmap(bitmap);
        } else {
            //ponemos una imagen por defectooo
            fotoPerfil.setImageResource(R.drawable.ic_launcher_background);
        }

        String nombre = db.obtenerNombreUsuario(idUsuarioActual);
        txtUsername.setText(nombre);

    }

    private void cargarResenasDelUsuario() {
        //obtenemos lo del join de la base de datos
        List<ResenaItem> lista = db.obtenerResenasPorUsuario(idUsuarioActual);

        //se la pasamos al adaptador
        adapter = new ResenaAdapter(getContext(), lista);
        recyclerResenas.setAdapter(adapter);
    }
}