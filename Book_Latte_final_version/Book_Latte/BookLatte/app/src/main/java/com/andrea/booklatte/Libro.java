package com.andrea.booklatte;

import javax.xml.namespace.QName;

public class Libro {
    String nombre;
    String autor;
    String sinopsis;
    int image;

    public Libro(String nombre, String autor, int image, String sinopsis) {
        this.nombre = nombre;
        this.autor = autor;
        this.image = image;
        this.sinopsis = sinopsis;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }
}
