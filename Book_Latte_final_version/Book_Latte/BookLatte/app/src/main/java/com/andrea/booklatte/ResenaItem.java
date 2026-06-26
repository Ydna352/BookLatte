package com.andrea.booklatte;

public class ResenaItem {
    private String tituloLibro;
    private int imagenLibro; //id del recurso del drawable
    private String comentario;
    private float calificacion;

    public ResenaItem(String tituloLibro, int imagenLibro, String comentario, float calificacion) {
        this.tituloLibro = tituloLibro;
        this.imagenLibro = imagenLibro;
        this.comentario = comentario;
        this.calificacion = calificacion;
    }

    // Getters
    public String getTituloLibro() { return tituloLibro; }
    public int getImagenLibro() { return imagenLibro; }
    public String getComentario() { return comentario; }
    public float getCalificacion() { return calificacion; }
}