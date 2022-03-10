package com.example.uberclonec.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.uberclonec.R;
import com.example.uberclonec.dao.UsuarioDAO;
import com.example.uberclonec.helper.Base64Custom;
import com.example.uberclonec.model.Usuario;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText input_email, input_senha;
    private Button btn_logar;
    private Usuario usuario;
    private UsuarioDAO usuarioDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setTitle("Acessar minha conta");
        input_email= findViewById(R.id.input_email_login);
        input_senha= findViewById(R.id.input_senha_login);
        btn_logar= findViewById(R.id.btn_logar);
        usuario= new Usuario();
        usuarioDAO= new UsuarioDAO(this);
        btn_logar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validarCampos()){
                    usuario.setEmail(input_email.getText().toString());
                    usuario.setSenha(input_senha.getText().toString());
                    usuario.setId(Base64Custom.encode64(usuario.getEmail()));
                    usuarioDAO.logar_usuario(usuario);
                }
            }
        });
    }

    private boolean validarCampos(){
        if(input_email.getText().toString().isEmpty()){
            Toast.makeText(this, "Digite um email",
                    Toast.LENGTH_SHORT).show();
            input_email.requestFocus();
            return false;
        }
        if(input_senha.getText().toString().isEmpty()){
            Toast.makeText(this, "Digite uma senha",
                    Toast.LENGTH_SHORT).show();
            input_senha.requestFocus();
            return false;
        }
        return true;
    }
}