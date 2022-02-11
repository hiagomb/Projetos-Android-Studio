package com.example.whatsappclone.activity;

import android.content.Intent;
import android.os.Bundle;

import com.example.whatsappclone.adapter.AdapterListaContatos;
import com.example.whatsappclone.adapter.AdapterMembrosSelecionados;
import com.example.whatsappclone.databinding.ActivityGrupoBinding;
import com.example.whatsappclone.helper.Base64Custom;
import com.example.whatsappclone.helper.ConfigFirebase;
import com.example.whatsappclone.helper.RecyclerItemClickListener;
import com.example.whatsappclone.model.Usuario;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;

import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsappclone.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GrupoActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private RecyclerView rv_todos_mebros, rv_membros_selecionados;
    private List<Usuario> lista_membros;
    private List<Usuario> lista_membros_selecionados = new ArrayList<>();
    private AdapterListaContatos adapterListaContatos;
    private AdapterMembrosSelecionados adapterMembrosSelecionados;
    private Toolbar toolbar_grupo;
    private FloatingActionButton fab_grupo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grupo);


        toolbar_grupo= findViewById(R.id.toolbar_grupo);
        setSupportActionBar(toolbar_grupo);

        fab_grupo= findViewById(R.id.fab_grupo_forward);



        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_grupo);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Novo grupo");

        rv_todos_mebros= findViewById(R.id.recycler_todos_membros);
        rv_membros_selecionados= findViewById(R.id.recycler_membros_selecionados);
        fill_lista_membros();


        //calling adapter para membros selecionados
//        lista_membros_selecionados= new ArrayList<>();

        adapterMembrosSelecionados = new AdapterMembrosSelecionados(lista_membros_selecionados);

        //setting recycler view
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(getApplicationContext(),
        LinearLayoutManager.HORIZONTAL, false);
        rv_membros_selecionados.setLayoutManager(layoutManager);
        rv_membros_selecionados.setHasFixedSize(true);
        rv_membros_selecionados.setAdapter(adapterMembrosSelecionados);
        rv_membros_selecionados.addOnItemTouchListener(new RecyclerItemClickListener(
                getApplicationContext(), rv_membros_selecionados, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Usuario usuario_des_selecionado= lista_membros_selecionados.get(position);
                lista_membros.add(usuario_des_selecionado);
                adapterListaContatos= new AdapterListaContatos(lista_membros, null);
                rv_todos_mebros.setAdapter(adapterListaContatos);


                lista_membros_selecionados.remove(usuario_des_selecionado);
                adapterMembrosSelecionados= new AdapterMembrosSelecionados(lista_membros_selecionados);
                rv_membros_selecionados.setAdapter(adapterMembrosSelecionados);

                toolbar_grupo.setSubtitle(lista_membros_selecionados.size()+" de "+(lista_membros.size()
                        +lista_membros_selecionados.size())+" selecionados");
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        }));



        rv_todos_mebros.addOnItemTouchListener(new RecyclerItemClickListener(
                getApplicationContext(), rv_todos_mebros, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Usuario usuario_selecionado= lista_membros.get(position);
                lista_membros_selecionados.add(usuario_selecionado);
                adapterMembrosSelecionados= new AdapterMembrosSelecionados(lista_membros_selecionados);
                rv_membros_selecionados.setAdapter(adapterMembrosSelecionados);



                lista_membros.remove(usuario_selecionado);
                adapterListaContatos= new AdapterListaContatos(lista_membros, null);
                rv_todos_mebros.setAdapter(adapterListaContatos);

                toolbar_grupo.setSubtitle(lista_membros_selecionados.size()+" de "+(lista_membros.size()
                +lista_membros_selecionados.size())+" selecionados");
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        }));
        fab_grupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(GrupoActivity.this, GrupoCadastroActivity.class);
                i.putExtra("membros", (Serializable) lista_membros_selecionados);
                startActivity(i);
            }
        });
    }

    public List<Usuario> getLista_membros_selecionados(){
        return lista_membros_selecionados;
    }

    public void setLista_membros_selecionados(List<Usuario> lista_membros_selecionados){
        this.lista_membros_selecionados= lista_membros_selecionados;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_grupo);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void fill_lista_membros(){
        lista_membros= new ArrayList<>();

        //calling adapter
        adapterListaContatos= new AdapterListaContatos(lista_membros, null);


        //setting recycler view
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(getApplicationContext());
        rv_todos_mebros.setLayoutManager(layoutManager);
        rv_todos_mebros.setHasFixedSize(true);
        rv_todos_mebros.setAdapter(adapterListaContatos);



        String email64 = Base64Custom.encode64(ConfigFirebase.getFirebaseAuth().getCurrentUser().getEmail());
        DatabaseReference reference= ConfigFirebase.getFirebase().child("contatos").child(email64);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lista_membros.clear();
                for(DataSnapshot dados: snapshot.getChildren()){
                    Usuario u= dados.getValue(Usuario.class);
                    DatabaseReference ref_usuario= ConfigFirebase.getFirebase().
                            child("usuarios").child(Base64Custom.encode64(u.getEmail()));
                    ref_usuario.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                Usuario usuario_com_foto= snapshot.getValue(Usuario.class);
                                lista_membros.add(usuario_com_foto);
                                adapterListaContatos.notifyDataSetChanged();
                                toolbar_grupo.setSubtitle("0 de "+lista_membros.size()+" selecionados");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}