package com.example.whatsappclone.activity;

import androidx.appcompat.app.AppCompatActivity;
import com.example.whatsappclone.R;
import com.example.whatsappclone.dao.UsuarioDAO;
import com.example.whatsappclone.model.Usuario;
import com.google.android.material.textfield.TextInputEditText;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class CadastroEmailSenhaActivity extends AppCompatActivity {

    private TextInputEditText input_nome, input_email, input_senha;
    private Button btn_cadastrar;
    private UsuarioDAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_email_senha);

        input_nome= findViewById(R.id.input_nome);
        input_email= findViewById(R.id.input_email);
        input_senha= findViewById(R.id.input_senha);
        btn_cadastrar= findViewById(R.id.btn_cadastrar_email_senha);

        btn_cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Usuario usuario= new Usuario();
                usuario.setNome(input_nome.getText().toString());
                usuario.setEmail(input_email.getText().toString());
                usuario.setSenha(input_senha.getText().toString());

                dao= new UsuarioDAO(CadastroEmailSenhaActivity.this);
                if(dao.cadastrar(usuario)){
                    finish();
                }
            }
        });
    }


    public Activity getActivity(){
        return this;
    }

//    public Context getContext(){
//        return getApplicationContext();
//    }

}