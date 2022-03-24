package com.example.ifoodclone.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.ifoodclone.R;
import com.example.ifoodclone.adapter.AdapterPedidos;
import com.example.ifoodclone.helper.Base64Custom;
import com.example.ifoodclone.helper.FirebaseSettings;
import com.example.ifoodclone.model.Pedido;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PedidosActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView rv_pedidos;
    private List<Pedido> lista;
    private AdapterPedidos adapterPedidos;
    private DatabaseReference databaseReference;
    private TextView txt_aguardando;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos);

        toolbar= findViewById(R.id.toolbar);
        toolbar.setTitle("Pedidos");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        txt_aguardando= findViewById(R.id.txt_aguardando);

        rv_pedidos= findViewById(R.id.rv_pedidos_empresa);
        fill_pedidos();
    }

    private void fill_pedidos(){
        lista= new ArrayList<>();
        adapterPedidos= new AdapterPedidos(lista);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(getApplicationContext());
        rv_pedidos.setLayoutManager(layoutManager);
        rv_pedidos.setHasFixedSize(true);
        rv_pedidos.setAdapter(adapterPedidos);


        String id= Base64Custom.encode64(FirebaseSettings.getFirebaseAuth().getCurrentUser().getEmail());
        databaseReference= FirebaseSettings.getDatabaseReference();
        databaseReference.child("pedidos_empresa").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lista.clear();
                if(snapshot.exists()){
                    rv_pedidos.setVisibility(View.VISIBLE);
                    txt_aguardando.setVisibility(View.GONE);
                    for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                        lista.add(dataSnapshot.getValue(Pedido.class));
                    }
                }else{
                    rv_pedidos.setVisibility(View.GONE);
                    txt_aguardando.setVisibility(View.VISIBLE);
                }
                adapterPedidos.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}