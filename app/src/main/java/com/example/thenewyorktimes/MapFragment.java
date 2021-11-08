package com.example.thenewyorktimes;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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

import org.jetbrains.annotations.NotNull;

import java.util.Objects;


public class MapFragment extends Fragment {
    /**
     * Se crean las variables globales necesarias
     */
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;
    private MapFragmentArgs args;
    Double latitud = 0.0;
    Double longitud = 0.0;

    /**
     * Constructor por defecto
     */
    public  MapFragment () {
    }

    /**
     * Esta función es la primera en ejecutarse al iniciar el fragmento
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // inicializar vista
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext());
        fetchLocation();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        args= MapFragmentArgs.fromBundle(getArguments());
    }

    /**
     * Esta función solicita permisos a los usuarios para acceder a su ubicación por GPS
     * si los permisos son rechazados la función retorna vacio, de lo contrario solicita la ubicación
     * haciendo del API de GoogleMaps y setea su ubicación en la variable currentLocation
     */
    private void fetchLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }

        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    latitud = location.getLatitude();
                    longitud = location.getLongitude();
                }else{
                    latitud = Double.parseDouble(Objects.requireNonNull(args.getLatitud()));
                    longitud = Double.parseDouble(Objects.requireNonNull(args.getLongitud()));
                }
                showMap();
            }
        });
    }

    /**
     * Una vez obtenida la ubicación actual del dispositivo, esta función es ejecutada para
     * mostrar el mapa, con un marcador que indica su ubicación actual.
     */
    private void showMap(){
        Toast.makeText(requireContext(), currentLocation.getLatitude() + ":" + currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map);
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull @NotNull GoogleMap googleMap) {
                LatLng latLng = new LatLng(latitud, longitud);
                MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("I am here!");
                googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5));
                googleMap.addMarker(markerOptions);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLocation();
                }
                break;
        }
    }
}