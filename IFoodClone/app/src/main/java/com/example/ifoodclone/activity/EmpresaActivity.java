package com.example.ifoodclone.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.example.ifoodclone.R;
import com.example.ifoodclone.adapter.AdapterProdutos;
import com.example.ifoodclone.dao.ProdutoDAO;
import com.example.ifoodclone.helper.FirebaseSettings;
import com.example.ifoodclone.helper.RecyclerItemClickListener;
import com.example.ifoodclone.model.Produto;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EmpresaActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView rv_produtos;
    private List<Produto> lista;
    private DatabaseReference databaseReference;
    private ProdutoDAO produtoDAO;
    private AdapterProdutos adapterProdutos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empresa);

        toolbar= findViewById(R.id.toolbar);
        toolbar.setTitle("IFood Empresa");
        setSupportActionBar(toolbar);

        rv_produtos= findViewById(R.id.rv_produtos);
        rv_produtos.addOnItemTouchListener(new RecyclerItemClickListener(this,
                rv_produtos, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent i= new Intent(EmpresaActivity.this, NovoProdutoActivity.class);
                i.putExtra("produto", lista.get(position));
                startActivity(i);
            }

            @Override
            public void onLongItemClick(View view, int position) {
                AlertDialog.Builder alertDialog= new AlertDialog.Builder(EmpresaActivity.this);
                alertDialog.setTitle("Excluir produto");
                alertDialog.setMessage("Deseja realmente excluir esse produto?");
                alertDialog.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                alertDialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        produtoDAO= new ProdutoDAO();
                        produtoDAO.excluir_produto(lista.get(position));
                        adapterProdutos.notifyDataSetChanged();
                    }
                });
                alertDialog.create().show();
            }

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        }));
    }

    @Override
    protected void onStart() {
        super.onStart();

        fill_produtos();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater= getMenuInflater();
        menuInflater.inflate(R.menu.menu_empresa, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.item_sair_empresa){
            FirebaseSettings.getFirebaseAuth().signOut();
            startActivity(new Intent(EmpresaActivity.this,
                    AutenticacaoActivity.class));
        }
        if(item.getItemId() == R.id.item_config_empresa){
            startActivity(new Intent(EmpresaActivity.this,
                    ConfiguracoesEmpresaActivity.class));
        }
        if(item.getItemId() == R.id.item_add){
            startActivity(new Intent(EmpresaActivity.this,
                    NovoProdutoActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    private void fill_produtos(){
        lista= new ArrayList<>();

        adapterProdutos= new AdapterProdutos(lista);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(getApplicationContext());
        rv_produtos.setLayoutManager(layoutManager);
        rv_produtos.setHasFixedSize(true);
        rv_produtos.setAdapter(adapterProdutos);

        String id= FirebaseSettings.getId_user();
        databaseReference= FirebaseSettings.getDatabaseReference();
        databaseReference.child("produtos").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lista.clear();
                if(snapshot.exists()){
                    for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                        lista.add(dataSnapshot.getValue(Produto.class));
                    }
                }
                adapterProdutos.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}