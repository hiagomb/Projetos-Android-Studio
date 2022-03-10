package com.example.uberclonec.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.uberclonec.R;
import com.example.uberclonec.dao.UsuarioDAO;
import com.example.uberclonec.helper.Base64Custom;
import com.example.uberclonec.model.Usuario;
import com.google.android.material.textfield.TextInputEditText;

public class CadastroActivity extends AppCompatActivity {

    private TextInputEditText input_nome, input_email, input_senha;
    private Button btn_cadastrar;
    private Switch switch_tipo;
    private Usuario usuario;
    private UsuarioDAO usuarioDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        getSupportActionBar().setTitle("Cadastrar uma conta");
        input_nome= findViewById(R.id.input_nome);
        input_email= findViewById(R.id.input_email);
        input_senha= findViewById(R.id.input_senha);
        btn_cadastrar= findViewById(R.id.btn_cadastrar);
        switch_tipo= findViewById(R.id.switch_tipo);
        usuario= new Usuario();
        usuarioDAO= new UsuarioDAO(this);

        btn_cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validarCampos()){
                    usuario.setNome(input_nome.getText().toString());
                    usuario.setEmail(input_email.getText().toString());
                    usuario.setSenha(input_senha.getText().toString());
                    if(switch_tipo.isChecked() == true){
                        usuario.setTipo("motorista");
                    }else{
                        usuario.setTipo("passageiro");
                    }
                    usuario.setId(Base64Custom.encode64(usuario.getEmail()));
                    if(usuarioDAO.cadastrarUsuario(usuario)){
                       //
                    }else{
                        Toast.makeText(CadastroActivity.this, "Erro " +
                                "ao cadastrar usuário", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private boolean validarCampos(){
        if(input_nome.getText().toString().isEmpty()){
            Toast.makeText(this, "Digite o nome ",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        if(input_email.getText().toString().isEmpty()){
            Toast.makeText(this, "Digite o email ",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        if(input_senha.getText().toString().isEmpty()){
            Toast.makeText(this, "Digite a senha ",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}