package com.andrea.booklatte;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.List;

public class AgregarResenaDialog extends DialogFragment {

    private DBHelper db;
    private int idUsuarioActual;

    private List<Libro> listaLibrosObj;
    private List<String> nombresLibros;

    public interface OnResenaGuardadaListener {
        void onResenaGuardada();
    }

    private OnResenaGuardadaListener listener;

    public AgregarResenaDialog(int idUsuario, OnResenaGuardadaListener listener) {
        this.idUsuarioActual = idUsuario;
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_agregar_resena, null);

        db = new DBHelper(getContext());

        // para vincular las vistas
        Spinner spinner = view.findViewById(R.id.spinner_libros);
        RatingBar ratingBar = view.findViewById(R.id.rating_nueva_resena);
        EditText edtComentario = view.findViewById(R.id.edt_nuevo_comentario);
        Button btnGuardar = view.findViewById(R.id.btn_guardar_resena);

        //llenar el spinner con los libros de la base de datos
        cargarLibrosEnSpinner(spinner);

        btnGuardar.setOnClickListener(v -> {
            // Validaciones básicas
            if (listaLibrosObj.isEmpty()) {
                Toast.makeText(getContext(), "No hay libros disponibles", Toast.LENGTH_SHORT).show();
                return;
            }

            int posicionSeleccionada = spinner.getSelectedItemPosition();

            int idLibro = posicionSeleccionada + 1;

            String comentario = edtComentario.getText().toString();
            float calificacion = ratingBar.getRating();

            if (comentario.isEmpty()) {
                edtComentario.setError("Escribe algo");
                return;
            }

            //guardar en base de datos
            boolean exito = db.insertarResena(idUsuarioActual, idLibro, comentario, calificacion);

            if (exito) {
                Toast.makeText(getContext(), "Reseña guardada exitosamente", Toast.LENGTH_SHORT).show();
                dismiss(); //cerramos dialogo
            } else {
               //condicionamos las resenias
                Toast.makeText(getContext(), "¡Ya hiciste una reseña para este libro!", Toast.LENGTH_LONG).show();
            }
        });

        builder.setView(view);
        return builder.create();
    }

    private void cargarLibrosEnSpinner(Spinner spinner) {
        listaLibrosObj = db.obtenerLibros();
        nombresLibros = new ArrayList<>();

        for (Libro libro : listaLibrosObj) {
            nombresLibros.add(libro.getNombre());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_dropdown_item, nombresLibros);
        spinner.setAdapter(adapter);
    }
}