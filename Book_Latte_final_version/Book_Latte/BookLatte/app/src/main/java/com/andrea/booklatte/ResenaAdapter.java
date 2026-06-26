package com.andrea.booklatte;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ResenaAdapter extends RecyclerView.Adapter<ResenaAdapter.ResenaViewHolder> {

    private Context context;
    private List<ResenaItem> listaResenas;

    public ResenaAdapter(Context context, List<ResenaItem> listaResenas) {
        this.context = context;
        this.listaResenas = listaResenas;
    }

    @NonNull
    @Override
    public ResenaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //conexion con item_resenia.xml
        View view = LayoutInflater.from(context).inflate(R.layout.item_resenia, parent, false);
        return new ResenaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResenaViewHolder holder, int position) {
        ResenaItem item = listaResenas.get(position);

        //llenamos datos
        holder.txtTitulo.setText(item.getTituloLibro());
        holder.txtComentario.setText(item.getComentario());
        holder.ratingBar.setRating(item.getCalificacion());

        //ponemos imagen del libro
        holder.imgPortada.setImageResource(item.getImagenLibro());
    }

    @Override
    public int getItemCount() {
        return listaResenas.size();
    }

    public static class ResenaViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPortada;
        TextView txtTitulo, txtComentario;
        RatingBar ratingBar;

        public ResenaViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPortada = itemView.findViewById(R.id.img_portada_libro);
            txtTitulo = itemView.findViewById(R.id.txt_titulo_libro);
            txtComentario = itemView.findViewById(R.id.txt_comentario);
            ratingBar = itemView.findViewById(R.id.rating_resenia);
        }
    }
}