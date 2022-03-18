package com.example.uberclonec.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.uberclonec.R;
import com.example.uberclonec.dao.RequisicaoDAO;
import com.example.uberclonec.helper.Base64Custom;
import com.example.uberclonec.helper.FirebaseSettings;
import com.example.uberclonec.helper.Local;
import com.example.uberclonec.model.Requisicao;
import com.example.uberclonec.model.Usuario;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;

public class CorridaActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private Usuario motorista;
    private Requisicao requisicao;
    private DatabaseReference databaseReference;
    private Button btn_aceitar_corrida;
    private boolean corrida_aceita;
    private RequisicaoDAO requisicaoDAO;
    private FloatingActionButton fab_rotas;
    private LatLng local_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_corrida);
        getSupportActionBar().setTitle("Aceitar Corrida");

        motorista= (Usuario) getIntent().getExtras().getSerializable("motorista");
        requisicao= (Requisicao) getIntent().getExtras().getSerializable("requisicao");
        corrida_aceita= getIntent().getExtras().getBoolean("corrida_aceita");
        btn_aceitar_corrida= findViewById(R.id.btn_aceitar_corrida);
        fab_rotas= findViewById(R.id.fab_rotas);
        requisicaoDAO= new RequisicaoDAO();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);

        get_localizacao_usuario();

        fab_rotas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                set_rota(requisicao);
            }
        });

        btn_aceitar_corrida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aceitar_corrida();
            }
        });
    }

    private void get_localizacao_usuario(){
        if(!requisicao.getStatus().equalsIgnoreCase(Requisicao.STATUS_FINALIZADA)){
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 10,
                    new LocationListener() {
                        @Override
                        public void onLocationChanged(@NonNull Location location) {
                            local_user = new LatLng(location.getLatitude(), location.getLongitude());
                            mMap.clear();
                            mMap.addMarker(new MarkerOptions().position(local_user).title("Minha localização").
                                    icon(BitmapDescriptorFactory.fromResource(R.drawable.usuario)));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(local_user, 16));
                            //atualizar localização na requisição
                            requisicao.setLatitude_motorista(location.getLatitude());
                            requisicao.setLongitude_motorista(location.getLongitude());
                            requisicaoDAO.atualiza_local_motorista_req(requisicao);
                            verifica_localizacoes(local_user, requisicao);
                            atualiza_inteface(requisicao);
                            //verifica se motorista chegou no passageiro ou destino

                        }
                    });
        }
    }

    private void verifica_localizacoes(LatLng latLng_motorista, Requisicao requisicao){
        if(requisicao.getStatus().equalsIgnoreCase(Requisicao.STATUS_A_CAMINHO)){
            databaseReference= FirebaseSettings.getDatabaseReference();
            databaseReference.child("usuarios").child(requisicao.getId_passageiro()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        Usuario u= snapshot.getValue(Usuario.class);
                        LatLng latLng_passageiro= new LatLng(u.getLatitude(), u.getLongitude());
                        if(Local.calcular_valor_ou_dist(latLng_motorista, latLng_passageiro, false)<= 0.03){
                            requisicao.setStatus(Requisicao.STATUS_VIAGEM);
                            requisicaoDAO.atualiza_status_requisicao(requisicao);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else if(requisicao.getStatus().equalsIgnoreCase(Requisicao.STATUS_VIAGEM)){
            LatLng latLng_destino= new LatLng(Double.parseDouble(requisicao.getDestino().getLatitude()),
                   Double.parseDouble(requisicao.getDestino().getLongitude()));
            if(Local.calcular_valor_ou_dist(latLng_motorista, latLng_destino, false)<= 0.03){
                requisicao.setStatus(Requisicao.STATUS_FINALIZADA);
                requisicaoDAO.atualiza_status_requisicao(requisicao);
            }
        }

    }

    private void aceitar_corrida(){
        if(btn_aceitar_corrida.getText().toString().equalsIgnoreCase("aceitar corrida")){
            databaseReference= FirebaseSettings.getDatabaseReference();
            requisicao.setId_motorista(Base64Custom.encode64(FirebaseSettings.getFirebaseAuth().
                    getCurrentUser().getEmail()));
            requisicao.setStatus(Requisicao.STATUS_A_CAMINHO);
            databaseReference.child("requisicoes_ativas").child(requisicao.getId()).setValue(requisicao);
            databaseReference.child("requisicoes").child("passageiros").
                    child(requisicao.getId_passageiro()).child(requisicao.getId()).setValue(requisicao);
            Toast.makeText(this, "Corrida aceita", Toast.LENGTH_SHORT).show();
            btn_aceitar_corrida.setText("A caminho");
        }
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
    }



    @Override
    public boolean onSupportNavigateUp() {
        if(corrida_aceita== true && !requisicao.getStatus().equalsIgnoreCase(Requisicao.STATUS_FINALIZADA)){
            Toast.makeText(CorridaActivity.this, "É necessário encerrar a corrida atual", Toast.LENGTH_SHORT).show();
        }else{
            startActivity(new Intent(CorridaActivity.this, RequisicaoAcitivity.class));
            finish();
        }
        return super.onSupportNavigateUp();
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


    private void set_rota(Requisicao requisicao){
        if(requisicao.getStatus().equalsIgnoreCase(Requisicao.STATUS_A_CAMINHO)){
            String id = requisicao.getId_passageiro();
            databaseReference= FirebaseSettings.getDatabaseReference();
            databaseReference.child("usuarios").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        Usuario passageiro= snapshot.getValue(Usuario.class);
                        String lat= String.valueOf(passageiro.getLatitude());
                        String lon= String.valueOf(passageiro.getLongitude());
                        Uri location_passenger= Uri.parse("google.navigation:q="+lat+","+lon);
                        Intent i= new Intent(Intent.ACTION_VIEW, location_passenger);
                        i.setPackage("com.google.android.apps.maps");
                        startActivity(i);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else if(requisicao.getStatus().equalsIgnoreCase(Requisicao.STATUS_VIAGEM)){
            String lat= requisicao.getDestino().getLatitude();
            String lon= requisicao.getDestino().getLongitude();
            Uri location_destino= Uri.parse("google.navigation:q="+lat+","+lon);
            Intent i= new Intent(Intent.ACTION_VIEW, location_destino);
            i.setPackage("com.google.android.apps.maps");
            startActivity(i);
        }
    }

    private void atualiza_inteface(Requisicao requisicao){
        if(requisicao.getStatus().equalsIgnoreCase(Requisicao.STATUS_AGUARDANDO)){
            btn_aceitar_corrida.setText("Aceitar corrida");
            btn_aceitar_corrida.setEnabled(true);
            fab_rotas.setVisibility(View.GONE);
        }else if(requisicao.getStatus().equalsIgnoreCase(Requisicao.STATUS_A_CAMINHO)){
            btn_aceitar_corrida.setText("A caminho");
            btn_aceitar_corrida.setEnabled(false);
            fab_rotas.setVisibility(View.VISIBLE);
        }else if(requisicao.getStatus().equalsIgnoreCase(Requisicao.STATUS_VIAGEM)){
            btn_aceitar_corrida.setText("Em viagem para destino");
            btn_aceitar_corrida.setEnabled(false);
            fab_rotas.setVisibility(View.VISIBLE);
        }else if(requisicao.getStatus().equalsIgnoreCase(Requisicao.STATUS_FINALIZADA)){
            btn_aceitar_corrida.setText("Corrida Finalizada - R$XX");
            btn_aceitar_corrida.setEnabled(false);
            fab_rotas.setVisibility(View.GONE);
        }
        centraliza_marcadores(requisicao);
    }



    private void centraliza_marcadores(Requisicao requisicao){
        LatLng latLng_motorista= null;
        mMap.clear();

        if(requisicao.getStatus().equalsIgnoreCase(Requisicao.STATUS_A_CAMINHO)){
            databaseReference= FirebaseSettings.getDatabaseReference();
            databaseReference.child("usuarios").child(requisicao.getId_passageiro()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        Usuario passageiro= snapshot.getValue(Usuario.class);
                        LatLng latLng_mot= new LatLng(requisicao.getLatitude_motorista(),
                                requisicao.getLongitude_motorista());
                        mMap.addMarker(new MarkerOptions().position(latLng_mot).title("Minha localização").
                                icon(BitmapDescriptorFactory.fromResource(R.drawable.carro)));
                        LatLng latLng_pass= new LatLng(passageiro.getLatitude(), passageiro.getLongitude());
                        mMap.addMarker(new MarkerOptions().position(latLng_pass).title("Passageiro").
                                icon(BitmapDescriptorFactory.fromResource(R.drawable.usuario)));

                        LatLngBounds.Builder builder= new LatLngBounds.Builder();
                        builder.include(latLng_mot);
                        builder.include(latLng_pass);
                        int largura= getResources().getDisplayMetrics().widthPixels;
                        int altura= getResources().getDisplayMetrics().heightPixels;
                        int margem= (int) (largura* 0.2);
                        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(),
                                largura, altura, margem));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else if(requisicao.getStatus().equalsIgnoreCase(Requisicao.STATUS_VIAGEM)) {
            latLng_motorista= new LatLng(requisicao.getLatitude_motorista(),
                    requisicao.getLongitude_motorista());
            mMap.addMarker(new MarkerOptions().position(latLng_motorista).title("Minha localização").
                    icon(BitmapDescriptorFactory.fromResource(R.drawable.carro)));
            LatLng local_destino= new LatLng(
                    Double.parseDouble(requisicao.getDestino().getLatitude()),
                    Double.parseDouble(requisicao.getDestino().getLongitude()));
            mMap.addMarker(new MarkerOptions().position(local_destino).title("Destino").
                    icon(BitmapDescriptorFactory.fromResource(R.drawable.destino)));

            LatLngBounds.Builder builder= new LatLngBounds.Builder();
            builder.include(latLng_motorista);
            builder.include(local_destino);
            int largura= getResources().getDisplayMetrics().widthPixels;
            int altura= getResources().getDisplayMetrics().heightPixels;
            int margem= (int) (largura* 0.2);
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(),
                    largura, altura, margem));
        }
    }
}