package com.example.uberclonec.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.uberclonec.R;
import com.example.uberclonec.helper.FirebaseSettings;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.textfield.TextInputEditText;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Pass_Activity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private TextInputEditText input_destino;
    private Button btn_chamar_uber;
    private Geocoder geocoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass);

        getSupportActionBar().setTitle("Iniciar minha viagem");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        input_destino= findViewById(R.id.input_destino);
        btn_chamar_uber= findViewById(R.id.btn_chamar_uber);
        geocoder= new Geocoder(this, Locale.getDefault());
        btn_chamar_uber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(input_destino.getText().toString().isEmpty()){
                    Toast.makeText(Pass_Activity.this, "Digite um destino", Toast.LENGTH_SHORT).show();
                }else{
                    try {
                        List<Address> addresses= geocoder.getFromLocationName
                                (input_destino.getText().toString(), 2);
                        if(addresses.size() == 0){
                            Toast.makeText(Pass_Activity.this, "Destino inválido", Toast.LENGTH_SHORT).show();
                        }else{
                            
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_sair) {
            FirebaseSettings.getFirebaseAuth().signOut();
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
                new LocationListener() {
                    @Override
                    public void onLocationChanged(@NonNull Location location) {
                        LatLng local_user = new LatLng(location.getLatitude(), location.getLongitude());
                        mMap.clear();
                        mMap.addMarker(new MarkerOptions().position(local_user).title("Minha localização"));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(local_user, 20));
                    }
                });
    }
}