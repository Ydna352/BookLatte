package com.andrea.booklatte;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

public class MapsFragment extends Fragment {

    // Variables
    Spinner spType;
    Button btnFind;
    SupportMapFragment supportMapFragment;
    GoogleMap map;
    FusedLocationProviderClient fusedLocationProviderClient;
    double currentLat = 0, currentlong = 0;

    public MapsFragment() {
        //constructor vacio
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //inflamos el layout
        View view = inflater.inflate(R.layout.fragment_maps, container, false);

        //inicializar ui dentro del fragment
        spType = view.findViewById(R.id.sp_type);
        btnFind = view.findViewById(R.id.btn_find);

        // Para fragments anidados (mapa dentro de fragment), usamos getChildFragmentManager
        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map);

        // configuracion de listas
        String[] placeTypeList = {"book_store", "cafe"};
        String[] placeNameList = {"Librerías", "Cafeterías"};

        spType.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, placeNameList));

        // inicializamos la ubicacion
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        //checamos los permisos
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        } else {
            // pedir permisos (usamos requestPermissions del fragment)
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }

        btnFind.setOnClickListener(v -> {
            if (currentLat == 0 && currentlong == 0) {
                Toast.makeText(requireContext(), "Obteniendo ubicación, intenta de nuevo en un momento...", Toast.LENGTH_SHORT).show();
                getCurrentLocation();
                return;
            }

            int i = spType.getSelectedItemPosition();
            String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json" +
                    "?location=" + currentLat + "," + currentlong +
                    "&radius=5000" +
                    "&type=" + placeTypeList[i] +
                    "&sensor=true" +
                    "&key=" + getResources().getString(R.string.google_maps_key);

            new PlaceTask().execute(url);
        });

        return view;
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLat = location.getLatitude();
                    currentlong = location.getLongitude();

                    if (supportMapFragment != null) {
                        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(@NonNull GoogleMap googleMap) {
                                map = googleMap;
                                map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(currentLat, currentlong), 15)); //definimos zoom 15 para ver calles

                                // mostrar puntito azul de "mi ubicación"
                                if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                                    map.setMyLocationEnabled(true);
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 44) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            }
        }
    }

    private class PlaceTask extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            String data = null;
            try {
                data = downloadUrl(strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null) {
                new ParserTask().execute(s);
            }
        }
    }

    private String downloadUrl(String string) throws IOException {
        URL url = new URL(string);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.connect();
        InputStream stream = connection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder builder = new StringBuilder();
        String line = "";
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }
        String data = builder.toString();
        reader.close();
        return data;
    }

    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String, String>>> {
        @Override
        protected List<HashMap<String, String>> doInBackground(String... strings) {
            JsonParser jsonParser = new JsonParser();
            List<HashMap<String, String>> mapList = null;
            JSONObject object = null;
            try {
                if(strings[0] != null) {
                    object = new JSONObject(strings[0]);
                    mapList = jsonParser.parseResult(object);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return mapList;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> hashMaps) {
            if (map == null || hashMaps == null) return;

            map.clear(); //limpiamos marcadores

            for (int i = 0; i < hashMaps.size(); i++) {
                HashMap<String, String> hashMapList = hashMaps.get(i);

                // Protección contra nulos
                if(hashMapList.get("lat") != null && hashMapList.get("lng") != null) {
                    double lat = Double.parseDouble(hashMapList.get("lat"));
                    double lng = Double.parseDouble(hashMapList.get("lng"));
                    String name = hashMapList.get("name");

                    LatLng latLng = new LatLng(lat, lng);
                    MarkerOptions options = new MarkerOptions();
                    options.position(latLng);
                    options.title(name);
                    map.addMarker(options);
                }
            }
        }
    }
}