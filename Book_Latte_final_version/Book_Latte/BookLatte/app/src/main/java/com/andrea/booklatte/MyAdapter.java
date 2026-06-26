package com.andrea.booklatte;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    Context context;
    List<Libro> libros;

    public MyAdapter(Context context, List<Libro> libros) {
        this.context = context;
        this.libros = libros;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_view, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.nombre.setText(libros.get(position).getNombre());
        holder.autor.setText(libros.get(position).getAutor());
        holder.sinopsis.setText(libros.get(position).getSinopsis());
        holder.imageView.setImageResource(libros.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        return libros.size();
    }
}
