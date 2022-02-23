package com.example.organizzeclone.activity;

import androidx.appcompat.app.AppCompatActivity;
import com.example.organizzeclone.R;
import com.example.organizzeclone.dao.UsuarioDAO;
import com.example.organizzeclone.model.Usuario;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class CadastroActivity extends AppCompatActivity {

    private TextInputEditText input_nome, input_email, input_senha;
    private Button btn_cadastrar;
    private Usuario usuario;
    private UsuarioDAO usuarioDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        input_nome= findViewById(R.id.input_nome);
        input_email= findViewById(R.id.input_email);
        input_senha= findViewById(R.id.input_senha);
        btn_cadastrar= findViewById(R.id.btn_cadastrar);
        btn_cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validar_campos()){
                    usuario= new Usuario();
                    usuario.setNome(input_nome.getText().toString());
                    usuario.setEmail(input_email.getText().toString());
                    usuario.setSenha(input_senha.getText().toString());

                    usuarioDAO= new UsuarioDAO(CadastroActivity.this);
                    if(usuarioDAO.cadastrar(usuario)){
                        //cadastrado com sucesso
                    }
                }
            }
        });

    }

    public boolean validar_campos(){
        if(input_nome.getText().toString().isEmpty()){
            Toast.makeText(this, "Digite o nome", Toast.LENGTH_SHORT).show();
            input_nome.requestFocus();
            return false;
        }
        if(input_email.getText().toString().isEmpty()){
            Toast.makeText(this, "Digite o email", Toast.LENGTH_SHORT).show();
            input_email.requestFocus();
            return false;
        }
        if(input_senha.getText().toString().isEmpty()){
            Toast.makeText(this, "Digite a senha", Toast.LENGTH_SHORT).show();
            input_senha.requestFocus();
            return false;
        }
        return true;
    }
}