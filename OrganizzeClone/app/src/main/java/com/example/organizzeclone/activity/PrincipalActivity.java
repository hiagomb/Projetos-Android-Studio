package com.example.organizzeclone.activity;

import androidx.appcompat.app.AppCompatActivity;
import com.example.organizzeclone.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class PrincipalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
    }

    public void abrirDespesas(View view){
        startActivity(new Intent(this, DespesasActivity.class));
    }

    public void abrirReceitas(View view){
        startActivity(new Intent(this, ReceitasActivity.class));
    }
}