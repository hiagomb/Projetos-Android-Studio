package com.example.uberclonec.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uberclonec.R;
import com.example.uberclonec.adapter.AdapterListaRequisicoes;
import com.example.uberclonec.helper.Base64Custom;
import com.example.uberclonec.helper.FirebaseSettings;
import com.example.uberclonec.helper.RecyclerItemClickListener;
import com.example.uberclonec.model.Requisicao;
import com.example.uberclonec.model.Usuario;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class RequisicaoAcitivity extends AppCompatActivity {

    private ImageView img_driver;
    private TextView text_driver;
    private RecyclerView rv_requisicoes;
    private List<Requisicao> lista;
    private AdapterListaRequisicoes adapter;
    private LocationManager locationManager; //recuperar localização do motorista
    private DatabaseReference databaseReference;
    private Usuario u;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requisicao_acitivity);

        getSupportActionBar().setTitle("Requisições de Corrida");
        img_driver = findViewById(R.id.img_driver);
        text_driver = findViewById(R.id.text_driver);
        rv_requisicoes = findViewById(R.id.rv_requisicoes);
        recupera_localizacao(false);
        rv_requisicoes.addOnItemTouchListener(new RecyclerItemClickListener(this, rv_requisicoes,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Requisicao requisicao= lista.get(position);
                        Intent i= new Intent(RequisicaoAcitivity.this, CorridaActivity.class);
                        i.putExtra("requisicao", requisicao);
                        i.putExtra("motorista", u);
                        i.putExtra("corrida_aceita", false);
                        startActivity(i);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }

                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    }
                }));
    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseReference= FirebaseSettings.getDatabaseReference();
        databaseReference.child("requisicoes_ativas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                recupera_localizacao(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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

    public void fill_requisicoes(Usuario motorista) {
        lista = new ArrayList<>();

        //instantiating adapter
        adapter = new AdapterListaRequisicoes(lista, motorista);

        //setting recycler view
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        rv_requisicoes.setLayoutManager(layoutManager);
        rv_requisicoes.setHasFixedSize(true);
        rv_requisicoes.setAdapter(adapter);

        String id_motorista= Base64Custom.encode64(FirebaseSettings.
                getFirebaseAuth().getCurrentUser().getEmail());

        databaseReference = FirebaseSettings.getDatabaseReference();
        databaseReference.child("requisicoes_ativas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    lista.clear();
                    List<Integer> count= new ArrayList<>();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Requisicao r= dataSnapshot.getValue(Requisicao.class);
                        lista.add(r);
                        //verifica se motorista já está com uma requisição aceita
                        if(!r.getStatus().equalsIgnoreCase(Requisicao.STATUS_AGUARDANDO)){
                            System.out.println("entrou aqui");
                            count.add(lista.size()-1);
                        }
                    }
                    if(count.size()> 0){
                        for(int i=0; i<count.size(); i++){
                            Requisicao r_aceita= lista.get(count.get(i));
                            lista.remove(count);
                            if(r_aceita.getId_motorista()!= null &&
                                    r_aceita.getId_motorista().equalsIgnoreCase(id_motorista)
                                    && !r_aceita.getStatus().equalsIgnoreCase(Requisicao.STATUS_FINALIZADA)){
                                Intent intent= new Intent(RequisicaoAcitivity.this, CorridaActivity.class);
                                intent.putExtra("requisicao", r_aceita);
                                intent.putExtra("motorista", u);
                                intent.putExtra("corrida_aceita", true);
                                startActivity(intent);
                            }
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
                if (lista.size() > 0) {
                    rv_requisicoes.setVisibility(View.VISIBLE);
                    text_driver.setVisibility(View.GONE);
                    img_driver.setVisibility(View.GONE);
                } else {
                    rv_requisicoes.setVisibility(View.GONE);
                    text_driver.setVisibility(View.VISIBLE);
                    img_driver.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void recupera_localizacao(boolean fill_lista) {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 10,
                    new LocationListener() {
                        @Override
                        public void onLocationChanged(@NonNull Location location) {
                            //LatLng location_user= new LatLng(location.getLatitude(), location.getLongitude());
                            String id_motorista= Base64Custom.encode64(FirebaseSettings.
                                    getFirebaseAuth().getCurrentUser().getEmail());
                            databaseReference= FirebaseSettings.getDatabaseReference();
                            databaseReference.child("usuarios").child(id_motorista).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists()){
                                        u= snapshot.getValue(Usuario.class);
                                        u.setLatitude(location.getLatitude());
                                        u.setLongitude(location.getLongitude());
                                        databaseReference.child("usuarios").child(id_motorista).setValue(u);
                                        if(fill_lista){
                                            fill_requisicoes(u);
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    });
        }
    }



}