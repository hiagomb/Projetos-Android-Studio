package com.example.recyclerview.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.recyclerview.R;
import com.example.recyclerview.RecyclerItemClickListener;
import com.example.recyclerview.adapter.Adapter;
import com.example.recyclerview.model.Filme;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rv;
    private List<Filme> listaFilmes= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rv= findViewById(R.id.recycler_list);

        //Listagem de filmes
        criarFilmes();

        //configurar adapter
        Adapter adapter= new Adapter(listaFilmes);

        //configurar recyclerView
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(layoutManager);
        rv.setHasFixedSize(true);
        rv.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
        rv.setAdapter(adapter);

        //evento de click
        rv.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), rv, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Filme filme= listaFilmes.get(position);
                        Toast.makeText(getApplicationContext(),
                                ""+filme.getTitulo(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        Toast.makeText(getApplicationContext(), "Click longo", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    }
                })
        );
    }

    public void criarFilmes(){
        Filme filme= new Filme();
        filme.setTitulo("Homem Aranha");
        filme.setGenero("Aventura");
        filme.setAno("2017");
        listaFilmes.add(filme);

        Filme filme2= new Filme();
        filme2.setTitulo("Batman");
        filme2.setGenero("Ação");
        filme2.setAno("2008");
        listaFilmes.add(filme2);

        Filme filme3= new Filme();
        filme3.setTitulo("Liga da Justiça");
        filme3.setGenero("Ficção");
        filme3.setAno("2017");
        listaFilmes.add(filme3);

        Filme filme4= new Filme();
        filme4.setTitulo("Capitão América - Guerra Civil");
        filme4.setGenero("Aventura");
        filme4.setAno("2016");
        listaFilmes.add(filme4);

        Filme filme5= new Filme();
        filme5.setTitulo("Toy Story");
        filme5.setGenero("Comédia");
        filme5.setAno("2005");
        listaFilmes.add(filme5);
    }
}