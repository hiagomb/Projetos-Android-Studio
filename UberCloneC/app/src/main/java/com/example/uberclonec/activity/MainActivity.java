package com.example.uberclonec.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.uberclonec.R;
import com.example.uberclonec.helper.Base64Custom;
import com.example.uberclonec.helper.FirebaseSettings;
import com.example.uberclonec.helper.Permissoes;
import com.example.uberclonec.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private Button btn_go_login, btn_go_cadastro;
    private String[] permissoes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        btn_go_login= findViewById(R.id.btn_ir_para_login);
        btn_go_cadastro= findViewById(R.id.btn_ir_para_cadastro);

        btn_go_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });

        btn_go_cadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CadastroActivity.class));
            }
        });

        permissoes= new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
        Permissoes.validarPermissoes(permissoes, this, 1);
        verifica_usuario_logado();
    }

    public void verifica_usuario_logado(){
        if(FirebaseSettings.getFirebaseAuth().getCurrentUser() != null){
            String id= Base64Custom.encode64(FirebaseSettings.getFirebaseAuth().
                    getCurrentUser().getEmail());
            DatabaseReference databaseReference= FirebaseSettings.getDatabaseReference();
            databaseReference.child("usuarios").child(id).
                    addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                Usuario u= snapshot.getValue(Usuario.class);
                                if(u.getTipo().equalsIgnoreCase("passageiro")){
                                    startActivity(new Intent(MainActivity.this, Pass_Activity.class));
                                }else{
                                    startActivity(new Intent(MainActivity.this, RequisicaoAcitivity.class));
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for(int permissao_resultado: grantResults){
            if(permissao_resultado == PackageManager.PERMISSION_DENIED){
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle("Permissão Negada");
                alertDialog.setMessage("Para acessar a localização do usuário é necessário" +
                        " aceitar a permissão");
                alertDialog.setCancelable(false);
                alertDialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
                alertDialog.create().show();
            }else{
                verifica_usuario_logado();
            }
        }
    }
}