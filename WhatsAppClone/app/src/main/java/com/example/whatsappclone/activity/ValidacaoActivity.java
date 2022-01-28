package com.example.whatsappclone.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.whatsappclone.R;
import com.example.whatsappclone.helper.Preferences;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;

public class ValidacaoActivity extends AppCompatActivity {

    private TextInputEditText input_validar;
    private Button btn_validar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validacao);

        input_validar= findViewById(R.id.input_validacao);
        btn_validar= findViewById(R.id.btn_validar);
        Preferences preferences= new Preferences(this);

        SimpleMaskFormatter maskFormatter= new SimpleMaskFormatter("NNNN");
        MaskTextWatcher watcher= new MaskTextWatcher(input_validar, maskFormatter);
        input_validar.addTextChangedListener(watcher);

        btn_validar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, String> dados= preferences.getDadosUsuario();
                String token= dados.get("token");
                if(input_validar.getText().toString().equalsIgnoreCase(token)){
                    Toast.makeText(ValidacaoActivity.this, "Token Validado", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(ValidacaoActivity.this, "Token não Validado", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}