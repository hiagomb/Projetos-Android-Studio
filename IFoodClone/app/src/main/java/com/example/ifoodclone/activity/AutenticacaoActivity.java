package com.example.ifoodclone.activity;

import androidx.appcompat.app.AppCompatActivity;
import com.example.ifoodclone.R;
import com.example.ifoodclone.dao.UsuarioDAO;
import com.example.ifoodclone.helper.Base64Custom;
import com.example.ifoodclone.helper.FirebaseSettings;
import com.example.ifoodclone.model.Usuario;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import java.util.Base64;

public class AutenticacaoActivity extends AppCompatActivity {

    private Button btn_acessar;
    private EditText edit_email, edit_senha;
    private Switch switch_cadastro;
    private Usuario usuario;
    private UsuarioDAO usuarioDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autenticacao);

        getSupportActionBar().hide();
        btn_acessar= findViewById(R.id.btn_acessar);
        edit_email= findViewById(R.id.edit_cadastro_email);
        edit_senha= findViewById(R.id.edit_cadastro_senha);
        switch_cadastro= findViewById(R.id.switch_cadastro);

        verifica_usuario_logado();

        btn_acessar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validar_campos()){
                    usuario= new Usuario();
                    usuarioDAO= new UsuarioDAO(AutenticacaoActivity.this);
                    usuario.setEmail(edit_email.getText().toString());
                    usuario.setSenha(edit_senha.getText().toString());
                    usuario.setId(Base64Custom.encode64(usuario.getEmail()));
                    if(switch_cadastro.isChecked()){
                        usuarioDAO.cadastrar(usuario);
                    }else{
                        usuarioDAO.logar(usuario);
                    }
                }
            }
        });
    }

    private boolean validar_campos(){
        if(edit_email.getText().toString().isEmpty()){
            Toast.makeText(this, "Digite um email", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(edit_senha.getText().toString().isEmpty()){
            Toast.makeText(this, "Digite uma senha", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void verifica_usuario_logado(){
        if(FirebaseSettings.getFirebaseAuth().getCurrentUser()!= null){
            startActivity(new Intent(this, HomeActivity.class));
        }
    }
}