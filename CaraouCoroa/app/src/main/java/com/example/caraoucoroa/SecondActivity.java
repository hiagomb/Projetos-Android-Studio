package com.example.caraoucoroa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class SecondActivity extends AppCompatActivity {

    private ImageView img_voltar, img_moeda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        img_voltar= findViewById(R.id.img_voltar);
        img_moeda= findViewById(R.id.img_moeda);

        Bundle data= getIntent().getExtras();
        if(data.getDouble("randomNumber") > 0.5){
            img_moeda.setImageResource(R.drawable.moeda_cara);
        }else{
            img_moeda.setImageResource(R.drawable.moeda_coroa);
        }


        img_voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}