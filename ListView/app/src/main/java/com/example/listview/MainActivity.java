package com.example.listview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView lista;
    private ArrayList<String> lista_cidades;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lista= findViewById(R.id.list_locais);
        lista_cidades= new ArrayList<>();
        lista_cidades.add("Tanabi");
        lista_cidades.add("São José do Rio Preto");
        lista_cidades.add("Votuporanga");
        lista_cidades.add("Araçatuba");
        lista_cidades.add("Votorantim");
        lista_cidades.add("Cosmorama");
        lista_cidades.add("Monte Aprazivel");
        lista_cidades.add("Bálsamo");

        //criando adaptador para a lista
        ArrayAdapter<String> adapter= new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_list_item_1, android.R.id.text1, lista_cidades);

        //adiciona adaptador para a lista
        lista.setAdapter(adapter);

        //adiciona clique na lista
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(),lista.getItemAtPosition(i).toString() ,Toast.LENGTH_SHORT).show();
            }
        });
    }
}