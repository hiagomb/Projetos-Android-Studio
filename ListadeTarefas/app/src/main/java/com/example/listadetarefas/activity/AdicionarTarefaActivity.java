package com.example.listadetarefas.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.listadetarefas.R;
import com.google.android.material.textfield.TextInputEditText;

public class AdicionarTarefaActivity extends AppCompatActivity {

    private TextInputEditText edit_tarefa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_tarefa);

        edit_tarefa= findViewById(R.id.text_input_tarefa);
    }
}