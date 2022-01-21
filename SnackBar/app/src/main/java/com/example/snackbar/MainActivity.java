package com.example.snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    private Button btn_abrir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_abrir= findViewById(R.id.btn_abrir);

        btn_abrir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Teste de Snack Bar", Snackbar.LENGTH_SHORT).
                        setAction("Confirmar", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                btn_abrir.setText("Botão alterado");
                            }
                        }).show();
            }
        });
    }
}