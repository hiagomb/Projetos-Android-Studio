package com.example.ifoodclone.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ifoodclone.R;
import com.example.ifoodclone.dao.UsuarioDAO;
import com.example.ifoodclone.helper.Base64Custom;
import com.example.ifoodclone.helper.FirebaseSettings;
import com.example.ifoodclone.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Base64;

public class AutenticacaoActivity extends AppCompatActivity {

    private Button btn_acessar;
    private EditText edit_email, edit_senha;
    private Switch switch_cadastro, switch_tipo;
    private Usuario usuario;
    private UsuarioDAO usuarioDAO;
    private DatabaseReference databaseReference;
    private TextView txt_empresa, txt_cliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autenticacao);

        txt_empresa= findViewById(R.id.txt_empresa);
        txt_cliente= findViewById(R.id.txt_cliente);
        btn_acessar= findViewById(R.id.btn_acessar);
        edit_email= findViewById(R.id.edit_cadastro_email);
        edit_senha= findViewById(R.id.edit_cadastro_senha);
        switch_cadastro= findViewById(R.id.switch_cadastro);
        switch_tipo= findViewById(R.id.switch_tipo);
        switch_cadastro.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    switch_tipo.setVisibility(View.VISIBLE);
                    txt_empresa.setVisibility(View.VISIBLE);
                    txt_cliente.setVisibility(View.VISIBLE);
                }else{
                    switch_tipo.setVisibility(View.GONE);
                    txt_empresa.setVisibility(View.GONE);
                    txt_cliente.setVisibility(View.GONE);
                }
            }
        });

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
                    if(switch_tipo.isChecked()){
                        usuario.setTipo("empresa");
                    }else{
                        usuario.setTipo("cliente");
                    }
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
            String id= Base64Custom.encode64(FirebaseSettings.getFirebaseAuth().
                    getCurrentUser().getEmail());
            databaseReference= FirebaseSettings.getDatabaseReference();
            databaseReference.child("usuarios").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        Usuario u = snapshot.getValue(Usuario.class);
                        if(u.getTipo().equalsIgnoreCase("empresa")){
                            startActivity(new Intent(AutenticacaoActivity.this,
                                    EmpresaActivity.class));
                        }else{
                            startActivity(new Intent(AutenticacaoActivity.this,
                                    HomeActivity.class));
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}