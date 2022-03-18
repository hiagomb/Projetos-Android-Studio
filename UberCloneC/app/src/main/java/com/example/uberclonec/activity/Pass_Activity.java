package com.example.uberclonec.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.uberclonec.R;
import com.example.uberclonec.dao.RequisicaoDAO;
import com.example.uberclonec.helper.Base64Custom;
import com.example.uberclonec.helper.FirebaseSettings;
import com.example.uberclonec.model.Destino;
import com.example.uberclonec.model.Requisicao;
import com.example.uberclonec.model.Usuario;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Pass_Activity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private TextInputEditText input_destino;
    private Button btn_chamar_uber;
    private Double passageiro_latitude;
    private Double passageiro_longitude;
    private LinearLayout linear_pass;
    private String chave_requisicao;
    private RequisicaoDAO requisicaoDAO;
    private DatabaseReference databaseReference;
    private List<Requisicao> lista;
    private Destino destino;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass);



        getSupportActionBar().setTitle("Iniciar minha viagem");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        linear_pass= findViewById(R.id.linear_pass);

        input_destino= findViewById(R.id.input_destino);
        btn_chamar_uber= findViewById(R.id.btn_chamar_uber);
        get_localizacao_usuario();
    }

    private void verificaStatusRequisicao(){
//        DatabaseReference databaseReference= FirebaseSettings.getDatabaseReference();
//        databaseReference.child("requisicao").addValueEventListener(new )
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
        requisicaoDAO= new RequisicaoDAO();
    }

    private void get_localizacao_usuario(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 10,
                new LocationListener() {
                    @Override
                    public void onLocationChanged(@NonNull Location location) {
                        LatLng local_user = new LatLng(location.getLatitude(), location.getLongitude());
                        passageiro_latitude= new Double(location.getLatitude());
                        passageiro_longitude= new Double(location.getLongitude());
                        mMap.clear();
                        mMap.addMarker(new MarkerOptions().position(local_user).title("Minha localização").
                                icon(BitmapDescriptorFactory.fromResource(R.drawable.usuario)));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(local_user, 16));
                        add_listener_requisicao();
                    }
                });
    }


    public void chamar_uber(View view){
        if(btn_chamar_uber.getText().toString().equalsIgnoreCase("chamar uber")){
            if(input_destino.getText().toString().isEmpty()){
                Toast.makeText(Pass_Activity.this, "Digite um destino", Toast.LENGTH_SHORT).show();
            }else{
                try {
                    Geocoder geocoder= new Geocoder(this);
                    List<Address> addresses= geocoder.getFromLocationName
                            (input_destino.getText().toString(), 2);
                    if(addresses.size() == 0){
                        Toast.makeText(Pass_Activity.this, "Destino inválido", Toast.LENGTH_SHORT).show();
                    }else{
                        Address address= addresses.get(0);
                        destino= new Destino();
                        destino.setCidade(address.getSubLocality());
                        destino.setCep(address.getPostalCode());
                        destino.setBairro(address.getSubLocality());
                        destino.setRua(address.getThoroughfare());
                        destino.setNumero(address.getFeatureName());
                        destino.setLatitude(String.valueOf(address.getLatitude()));
                        destino.setLongitude(String.valueOf(address.getLongitude()));

                        StringBuilder mensagem= new StringBuilder();
                        mensagem.append("Cidade: "+destino.getCidade());
                        mensagem.append("\nRua: "+destino.getRua());
                        mensagem.append("\nBairro: "+destino.getBairro());
                        mensagem.append("\nNúmero: "+destino.getNumero());
                        mensagem.append("\nCEP: "+destino.getCep());

                        AlertDialog.Builder alertDialog= new AlertDialog.Builder(this);
                        alertDialog.setTitle("Confirmar localização");
                        alertDialog.setMessage(mensagem);
                        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        alertDialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Usuario u= FirebaseSettings.getUsuarioPassageiro(passageiro_latitude, passageiro_longitude);
                                chave_requisicao= requisicaoDAO.criar_requisicao_corrida(destino, u);
                                btn_chamar_uber.setText("Cancelar Uber");
                                linear_pass.setVisibility(LinearLayout.GONE);

                            }
                        });
                        alertDialog.create().show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else if (btn_chamar_uber.getText().toString().equalsIgnoreCase("cancelar uber")){
            btn_chamar_uber.setText("Chamar Uber");
            linear_pass.setVisibility(LinearLayout.VISIBLE);
            input_destino.setText("");
            requisicaoDAO.cancelar_corrida(chave_requisicao);
        }else if (btn_chamar_uber.getText().toString().equalsIgnoreCase("finalizar corrida")){
            btn_chamar_uber.setText("Chamar Uber");
            linear_pass.setVisibility(LinearLayout.VISIBLE);
            input_destino.setText("");
            requisicaoDAO.exclui_requisicao_ativa(chave_requisicao);
        }
    }

    private void add_listener_requisicao(){
        databaseReference= FirebaseSettings.getDatabaseReference();
        String user_id= Base64Custom.encode64(FirebaseSettings.
                getFirebaseAuth().getCurrentUser().getEmail());
        databaseReference.child("requisicoes_ativas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lista= new ArrayList<>();
                if(snapshot.exists()){
                    for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                        lista.add(dataSnapshot.getValue(Requisicao.class));
                        if(lista.get((lista.size())-1).getId_passageiro().equalsIgnoreCase(user_id)){
                            atualiza_inteface(lista.get((lista.size())-1));
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void atualiza_inteface(Requisicao requisicao){
        if(requisicao.getStatus().equalsIgnoreCase(Requisicao.STATUS_AGUARDANDO)){
            btn_chamar_uber.setText("Cancelar Uber");
            btn_chamar_uber.setEnabled(true);
        }else if(requisicao.getStatus().equalsIgnoreCase(Requisicao.STATUS_A_CAMINHO)){
            btn_chamar_uber.setText("Motorista a caminho");
            btn_chamar_uber.setEnabled(false);
        }else if(requisicao.getStatus().equalsIgnoreCase(Requisicao.STATUS_VIAGEM)){
            btn_chamar_uber.setText("Em viagem para destino");
            btn_chamar_uber.setEnabled(false);
        }else if(requisicao.getStatus().equalsIgnoreCase(Requisicao.STATUS_FINALIZADA)){
            btn_chamar_uber.setText("Finalizar corrida");
            btn_chamar_uber.setEnabled(true);
        }
        centraliza_marcadores(requisicao);
    }

    private void centraliza_marcadores(Requisicao requisicao){
        LatLng latLng_motorista= null;
        mMap.clear();

        if(requisicao.getStatus().equalsIgnoreCase(Requisicao.STATUS_A_CAMINHO)){
            latLng_motorista= new LatLng(requisicao.getLatitude_motorista(),
                    requisicao.getLongitude_motorista());
            mMap.addMarker(new MarkerOptions().position(latLng_motorista).title("Motorista").
                    icon(BitmapDescriptorFactory.fromResource(R.drawable.carro)));
            LatLng meu_local= new LatLng(passageiro_latitude, passageiro_longitude);
            mMap.addMarker(new MarkerOptions().position(meu_local).title("Minha localizacao").
                    icon(BitmapDescriptorFactory.fromResource(R.drawable.usuario)));

            LatLngBounds.Builder builder= new LatLngBounds.Builder();
            builder.include(latLng_motorista);
            builder.include(meu_local);
            int largura= getResources().getDisplayMetrics().widthPixels;
            int altura= getResources().getDisplayMetrics().heightPixels;
            int margem= (int) (largura* 0.2);
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(),
                    largura, altura, margem));
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
        }else{
            LatLng meu_local= new LatLng(passageiro_latitude, passageiro_longitude);
            mMap.addMarker(new MarkerOptions().position(meu_local).title("Minha localização").
                    icon(BitmapDescriptorFactory.fromResource(R.drawable.usuario)));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(meu_local, 16));
        }
    }
}