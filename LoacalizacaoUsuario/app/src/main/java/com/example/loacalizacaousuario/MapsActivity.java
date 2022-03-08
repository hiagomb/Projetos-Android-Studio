package com.example.loacalizacaousuario;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.loacalizacaousuario.databinding.ActivityMapsBinding;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private String[] permissoes;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        permissoes = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
        Permissoes.validarPermissoes(permissoes, this, 1);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            finish();
        }else{
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    0,
                    0,
                    new LocationListener() {
                        @Override
                        public void onLocationChanged(@NonNull Location location) {
                            LatLng local_usuario = new LatLng(location.getLatitude(),
                                    location.getLongitude());
                            mMap.clear(); //limpa marcadores -- se não vai duplicando
                            mMap.addMarker(new MarkerOptions().position(local_usuario).
                                    title("Sua localização atual"));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(local_usuario, 15));

                            //geocoding -- transformar lat/long em endereço e vice-versa
                            Geocoder geocoder= new Geocoder(getApplicationContext(), Locale.getDefault());
                            try {
                                List<Address> addresses= geocoder.getFromLocation(location.getLatitude(),
                                        location.getLongitude(), 2);
                                if(addresses.size()> 0){
                                    System.out.println(addresses.get(0).toString());
                                }else{
                                    System.out.println("não recuperou endereço");
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                        }
                    }
            );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int permissaoResultado : grantResults) {
            if (permissaoResultado == PackageManager.PERMISSION_DENIED) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle("Permissão Negada");
                alertDialog.setMessage("Para acessar a localização do usuário é necessário" +
                        " aceitar a permissão");
                alertDialog.setCancelable(false);
                alertDialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
                alertDialog.create().show();
            } else {
                //recuperando localização do usuário
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            0,
                            0,
                            new LocationListener() {
                                @Override
                                public void onLocationChanged(@NonNull Location location) {
                                    System.out.println("location: "+location.toString());
                                }
                            }
                    );
                }
            }
        }
    }
}