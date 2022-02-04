package com.example.whatsappclone.activity;

import androidx.appcompat.app.AppCompatActivity;
import com.example.whatsappclone.R;
import com.example.whatsappclone.dao.UsuarioDAO;
import com.example.whatsappclone.model.Usuario;
import com.google.android.material.textfield.TextInputEditText;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginEmailSenhaActivity extends AppCompatActivity {

    private TextInputEditText input_email, input_senha;
    private Button btn_entrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_email_senha);

        UsuarioDAO dao= new UsuarioDAO(LoginEmailSenhaActivity.this);
        dao.checaUsuariologado();

        input_email= findViewById(R.id.input_email_login);
        input_senha= findViewById(R.id.input_senha_login);
        btn_entrar= findViewById(R.id.btn_entrar);
        btn_entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Usuario usuario= new Usuario();
                usuario.setEmail(input_email.getText().toString());
                usuario.setSenha(input_senha.getText().toString());
                dao.logar(usuario);
            }
        });
    }

    public void openCadastro(View view){
        Intent intent= new Intent(LoginEmailSenhaActivity.this, CadastroEmailSenhaActivity.class);
        startActivity(intent);
    }


    public Activity getActivity(){
        return this;
    }
}