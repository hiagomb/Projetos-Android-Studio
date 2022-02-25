package com.example.organizzeclone.activity;

import androidx.appcompat.app.AppCompatActivity;
import com.example.organizzeclone.R;
import com.example.organizzeclone.dao.UsuarioDAO;
import com.example.organizzeclone.model.Usuario;
import com.google.android.material.textfield.TextInputEditText;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private Button btn_logar;
    private TextInputEditText input_email, input_senha;
    private UsuarioDAO usuarioDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn_logar= findViewById(R.id.btn_logar);
        input_email= findViewById(R.id.input_email_login);
        input_senha= findViewById(R.id.input_senha_login);

        usuarioDAO= new UsuarioDAO(this);

        btn_logar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validarCampos()){
                    Usuario usuario= new Usuario();
                    usuario.setEmail(input_email.getText().toString());
                    usuario.setSenha(input_senha.getText().toString());

                    usuarioDAO.logar(usuario);
                }
            }
        });

    }

    public boolean validarCampos(){
        if(input_email.getText().toString().isEmpty()){
            Toast.makeText(this, "Digite o email", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(input_senha.getText().toString().isEmpty()){
            Toast.makeText(this, "Digite a senha", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}