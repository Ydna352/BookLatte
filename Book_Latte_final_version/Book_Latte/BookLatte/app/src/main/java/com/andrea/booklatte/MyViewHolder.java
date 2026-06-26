package com.andrea.booklatte;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder {
    ImageView imageView;
    TextView nombre, autor, sinopsis;

    public MyViewHolder(@NonNull @org.jetbrains.annotations.NotNull View itemView)
    {
        super(itemView);
        imageView = itemView.findViewById(R.id.imageView);
        nombre = itemView.findViewById(R.id.nombre);
        autor = itemView.findViewById(R.id.autor);
        sinopsis = itemView.findViewById(R.id.sinopsis);
    }
}