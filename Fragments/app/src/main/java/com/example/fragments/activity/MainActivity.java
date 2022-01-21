package com.example.fragments.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.fragments.R;
import com.example.fragments.fragment.ContatosFragment;
import com.example.fragments.fragment.ConversasFragment;

public class MainActivity extends AppCompatActivity {

    private Button btn_conv, btn_cont;
    private ConversasFragment cv_f;
    private ContatosFragment ct_f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_conv= findViewById(R.id.btn_conversa);
        btn_cont= findViewById(R.id.btn_contato);
        cv_f= new ConversasFragment();
        ct_f= new ContatosFragment();

        //configurar objeto para gerenciar o fragmento

        //neste caso aqui o fragment conversas foi deixado como padrão inicial
        FragmentTransaction transaction= getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.frame_conteudo, cv_f);
        transaction.commit();

        btn_conv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction= getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_conteudo, cv_f);
                transaction.commit();
            }
        });

        btn_cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction= getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_conteudo, ct_f);
                transaction.commit();
            }
        });


    }
}