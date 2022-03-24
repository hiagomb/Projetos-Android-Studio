package com.example.ifoodclone.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ifoodclone.R;
import com.example.ifoodclone.adapter.AdapterEmpresas;
import com.example.ifoodclone.helper.FirebaseSettings;
import com.example.ifoodclone.helper.RecyclerItemClickListener;
import com.example.ifoodclone.model.Empresa;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private MaterialSearchView searchView;
    private RecyclerView rv_empresas;
    private List<Empresa> lista;
    private AdapterEmpresas adapterEmpresas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        searchView= findViewById(R.id.search_view);
        toolbar= findViewById(R.id.toolbar);
        toolbar.setTitle("IFood Cliente");
        setSupportActionBar(toolbar);
        rv_empresas= findViewById(R.id.rv_empresas);
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {
                recarregar_empresas();
            }
        });

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.isEmpty()){
                    recarregar_empresas();
                }else{
                    pesquisar_empresas(newText.toLowerCase());
                }

                return true;
            }
        });

        rv_empresas.addOnItemTouchListener(new RecyclerItemClickListener(this,
                rv_empresas, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                List<Empresa> lista_atual= adapterEmpresas.getListaAtualizada();
                Intent i= new Intent(HomeActivity.this, CardapioActivity.class);
                i.putExtra("empresa", lista_atual.get(position));
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
        fill_empresas();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater= getMenuInflater();
        menuInflater.inflate(R.menu.menu_cliente, menu);

        MenuItem item= menu.findItem(R.id.item_pesquisa);
        searchView.setMenuItem(item);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.item_config_cliente){
            startActivity(new Intent(HomeActivity.this,
                    ConfiguracaoesClienteActivity.class));
        }
        if(item.getItemId() == R.id.item_sair_cliente){
            FirebaseSettings.getFirebaseAuth().signOut();
            startActivity(new Intent(HomeActivity.this,
                    AutenticacaoActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    private void fill_empresas(){
        lista= new ArrayList<>();
        adapterEmpresas= new AdapterEmpresas(lista);

        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager
                (getApplicationContext());
        rv_empresas.setLayoutManager(layoutManager);
        rv_empresas.setHasFixedSize(true);
        rv_empresas.setAdapter(adapterEmpresas);
        DatabaseReference databaseReference= FirebaseSettings.getDatabaseReference();
        databaseReference.child("empresas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lista.clear();
                if(snapshot.exists()){
                    for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                        lista.add(dataSnapshot.getValue(Empresa.class));
                    }
                }
                adapterEmpresas.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void pesquisar_empresas(String text){
        List<Empresa> lista_busca= new ArrayList<>();
        for(Empresa empresa: lista){
            String nome_empresa = empresa.getNome().toLowerCase();
            if(nome_empresa.contains(text)){
                lista_busca.add(empresa);
            }
        }

        adapterEmpresas= new AdapterEmpresas(lista_busca);
        rv_empresas.setAdapter(adapterEmpresas);
        adapterEmpresas.notifyDataSetChanged();
    }

    private void recarregar_empresas(){
        adapterEmpresas= new AdapterEmpresas(lista);
        rv_empresas.setAdapter(adapterEmpresas);
        adapterEmpresas.notifyDataSetChanged();
    }
}