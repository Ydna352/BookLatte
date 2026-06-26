/*
======Integrantes del equipo======
Martínez Arroyo Andrea
Paúl Ortega Naomi Astrid
Piedras Mora Jorge Bryan
*/

package com.andrea.booklatte;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "db";
    public static final int DATA_BASE_VERSION = 7;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATA_BASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE usuarios (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombre TEXT, " +
                "email TEXT UNIQUE, " +
                "password TEXT, " +
                "biografia TEXT, " +
                "foto BLOB)");

        db.execSQL("CREATE TABLE libros (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombre TEXT, " +
                "autor TEXT, " +
                "sinopsis TEXT, " +
                "imageView INTEGER)");

        db.execSQL("CREATE TABLE resenas (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "id_usuario INTEGER, " +
                "id_libro INTEGER, " +
                "comentario TEXT, " +
                "calificacion REAL, " +
                "fecha TEXT DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY(id_usuario) REFERENCES usuarios(id), " +
                "FOREIGN KEY(id_libro) REFERENCES libros(id))");

        ContentValues cv = new ContentValues();

        cv.put("nombre", "La Teoría del Amor");
        cv.put("autor", "Ali Hazelwood");
        cv.put("sinopsis", "Una física teórica finge una relación con su némesis experimental para salvar su carrera, transformando su feroz guerra académica en un romance inesperado.");
        cv.put("imageView", R.drawable.teoria_amor);
        db.insert("libros", null, cv);

        cv.clear();
        cv.put("nombre", "El Príncipe del Sol");
        cv.put("autor", "Claudia Ramírez Lomelí");
        cv.put("sinopsis", "La paz milenaria entre el sol y la luna colapsa tras la desaparición de la reina Virian, dejando a ambos reinos al borde de una guerra inevitable.");
        cv.put("imageView", R.drawable.principe_sol);
        db.insert("libros", null, cv);

        cv.clear();
        cv.put("nombre", "Almendra");
        cv.put("autor", "Won-Pyung Sohn");
        cv.put("sinopsis", "Un joven sin emociones descubre la conexión humana tras una tragedia y una amistad improbable.");
        cv.put("imageView", R.drawable.almendra);
        db.insert("libros", null, cv);

        cv.clear();
        cv.put("nombre", "Crepúsculo");
        cv.put("autor", "Stephenie Meyer");
        cv.put("sinopsis", "Una adolescente se enamora de un vampiro, desatando un romance peligroso acechado por la sed de sangre y enemigos sobrenaturales.");
        cv.put("imageView", R.drawable.crepusculo);
        db.insert("libros", null, cv);

        cv.clear();
        cv.put("nombre", "Aura");
        cv.put("autor", "Carlos Fuentes");
        cv.put("sinopsis", "Un historiador se enamora de una joven que resulta ser una proyección mágica de la anciana que lo contrató.");
        cv.put("imageView", R.drawable.aura);
        db.insert("libros", null, cv);

        cv.clear();
        cv.put("nombre", "Leche y Miel");
        cv.put("autor", "Rupi Kaur");
        cv.put("sinopsis", "Poesía sobre la supervivencia femenina a través del dolor, el abuso, el amor y la sanación.");
        cv.put("imageView", R.drawable.leche_miel);
        db.insert("libros", null, cv);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS resenas");
        db.execSQL("DROP TABLE IF EXISTS libros");
        db.execSQL("DROP TABLE IF EXISTS usuarios");
        onCreate(db);
    }


    public boolean insertarUsuario(String nombre, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("nombre", nombre);
        cv.put("email", email);
        cv.put("password", password);
        // Biografia y foto inician vacíos
        long result = db.insert("usuarios", null, cv);
        return result != -1;
    }

    public int validarUsuario(String email, String password){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT id FROM usuarios WHERE email = ? AND password = ?",
                new String[]{email,password});

        int idUsuario = -1;

        if(cursor.moveToFirst()){
            idUsuario = cursor.getInt(0);
        }

        cursor.close();
        return idUsuario;
    }

    public String obtenerNombreUsuario(int idUsuario) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT nombre FROM usuarios WHERE id = ?",
                new String[]{String.valueOf(idUsuario)}
        );

        if (cursor.moveToFirst()) {
            String nombre = cursor.getString(0);
            cursor.close();
            return nombre;
        }
        cursor.close();
        return "";
    }


    public boolean actualizarPerfil(int idUsuario, byte[] foto, String biografia) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        if(foto != null) {
            cv.put("foto", foto);
        }
        cv.put("biografia", biografia);

        int filasAfectadas = db.update("usuarios", cv, "id = ?", new String[]{String.valueOf(idUsuario)});
        return filasAfectadas > 0;
    }

    public boolean actualizarBiografia(int idUsuario, String biografia) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("biografia", biografia);

        int filas = db.update("usuarios", cv, "id = ?", new String[]{String.valueOf(idUsuario)});
        return filas > 0;
    }


    public byte[] obtenerFotoUsuario(int idUsuario) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT foto FROM usuarios WHERE id = ?", new String[]{String.valueOf(idUsuario)});
        if (cursor.moveToFirst()) {
            byte[] blob = cursor.getBlob(0);
            cursor.close();
            return blob;
        }
        cursor.close();
        return null;
    }

    public boolean actualizarFoto(int idUsuario, byte[] foto) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("foto", foto);

        int filas = db.update("usuarios", cv, "id = ?", new String[]{String.valueOf(idUsuario)});
        return filas > 0;
    }



    public String obtenerBiografia(int idUsuario) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT biografia FROM usuarios WHERE id = ?", new String[]{String.valueOf(idUsuario)});
        if (cursor.moveToFirst()) {
            String bio = cursor.getString(0);
            cursor.close();
            return bio;
        }
        cursor.close();
        return "";
    }


    public boolean insertarLibro(String titulo, String autor, String sinopsis, int image){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("nombre", titulo);
        cv.put("autor", autor);
        cv.put("sinopsis", sinopsis);
        cv.put("imageView", image);
        return db.insert("libros", null, cv) != -1;
    }

    public List<Libro> obtenerLibros()
    {
        List<Libro> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM libros", null);

        if (cursor.moveToFirst()) {
            do {
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"));
                String autor = cursor.getString(cursor.getColumnIndexOrThrow("autor"));
                String sinopsis = cursor.getString(cursor.getColumnIndexOrThrow("sinopsis"));
                int imageView = cursor.getInt(cursor.getColumnIndexOrThrow("imageView"));

                lista.add(new Libro(nombre, autor, imageView, sinopsis));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return lista;
    }

    public boolean insertarResena(int idUsuario, int idLibro, String comentario, float calificacion) {
        SQLiteDatabase db = this.getWritableDatabase();

       //validacion de usuario al libro
        Cursor cursor = db.rawQuery("SELECT id FROM resenas WHERE id_usuario = ? AND id_libro = ?",
                new String[]{String.valueOf(idUsuario), String.valueOf(idLibro)});

        if (cursor.getCount() > 0) {
          //si ya existe hacemos la validacion
            cursor.close();
            return false;
        }
        cursor.close();

        //si no existe si le da permiso de hacer una
        ContentValues cv = new ContentValues();
        cv.put("id_usuario", idUsuario);
        cv.put("id_libro", idLibro);
        cv.put("comentario", comentario);
        cv.put("calificacion", calificacion);

        long result = db.insert("resenas", null, cv);
        return result != -1;
    }

    public List<ResenaItem> obtenerResenasPorUsuario(int idUsuario) {
        List<ResenaItem> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT r.comentario, r.calificacion, l.nombre, l.imageView " +
                "FROM resenas r " +
                "INNER JOIN libros l ON r.id_libro = l.id " +
                "WHERE r.id_usuario = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(idUsuario)});

        if (cursor.moveToFirst()) {
            do {
                String tituloLibro = cursor.getString(cursor.getColumnIndexOrThrow("nombre"));
                int imgLibro = cursor.getInt(cursor.getColumnIndexOrThrow("imageView"));
                String comentario = cursor.getString(cursor.getColumnIndexOrThrow("comentario"));
                float calificacion = cursor.getFloat(cursor.getColumnIndexOrThrow("calificacion"));

                lista.add(new ResenaItem(tituloLibro, imgLibro, comentario, calificacion));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return lista;
    }

    public static byte[] getBytesFromBitmap(Bitmap bitmap) {
        if (bitmap == null) return null;

        //limitamos el size de las imagenes para que no crashee todoooo
        int maxSize = 800;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }

        //redimensionamos el bitmap
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);

       //comprimimos
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);

        return stream.toByteArray();
    }

}



