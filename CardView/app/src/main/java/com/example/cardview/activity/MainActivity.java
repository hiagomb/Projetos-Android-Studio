package com.example.cardview.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.example.cardview.R;
import com.example.cardview.adapter.AdapterPostagem;
import com.example.cardview.model.Postagem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recycler_post;
    private List<Postagem> lista= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recycler_post= findViewById(R.id.recycler_postagem);

        //Define layout
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(getApplicationContext());
        recycler_post.setLayoutManager(layoutManager);
        recycler_post.setHasFixedSize(true);

        //Define Adapter
        createPosts();
        AdapterPostagem ap= new AdapterPostagem(lista);
        recycler_post.setAdapter(ap);
    }


    public void createPosts(){
        Postagem p= new Postagem("Hiago Brajato", "Agora mesmo", R.drawable.imagem1, "TBT Viagem");
        lista.add(p);

        p= new Postagem("Claudio Torres", "Duas horas atrás", R.drawable.imagem2, "Saudades desse dia...");
        lista.add(p);

        p= new Postagem("Almir Sater", "Dois minutos atrás", R.drawable.imagem3, "Música é vida");
        lista.add(p);

        p= new Postagem("Arthur Lustri", "Há alguns segundos", R.drawable.imagem4, "Meu amor");
        lista.add(p);
    }
}