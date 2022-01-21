package com.example.learningactivities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {

    private TextView name, idade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        name= findViewById(R.id.txt_nome);
        idade= findViewById(R.id.txt_idade);

        //recovering data from first activity
        Bundle data= getIntent().getExtras();
//        name.setText(data.getString("nome"));
//        idade.setText(String.valueOf(data.getInt("idade")));
        Usuario u= (Usuario) data.getSerializable("object");
        name.setText(u.getNome());
        idade.setText(u.getEmail());
    }
}