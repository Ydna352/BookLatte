package com.andrea.booklatte;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HomeFragment extends Fragment {

    private DBHelper db;

    public HomeFragment() {
        //constructor vacio
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //inflamos nuestro layout
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        db = new DBHelper(getContext());

       //configuracion del recycler view
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview_home);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //recuperamos libros
        List<Libro> listaLibros = db.obtenerLibros();
        MyAdapter adapter = new MyAdapter(getContext(), listaLibros);
        recyclerView.setAdapter(adapter);

        return view;
    }
}