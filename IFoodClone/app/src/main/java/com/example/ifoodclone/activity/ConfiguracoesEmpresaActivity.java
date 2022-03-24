package com.example.ifoodclone.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ifoodclone.R;
import com.example.ifoodclone.dao.EmpresaDAO;
import com.example.ifoodclone.dao.UsuarioDAO;
import com.example.ifoodclone.helper.Base64Custom;
import com.example.ifoodclone.helper.FirebaseSettings;
import com.example.ifoodclone.helper.LoadingDialog;
import com.example.ifoodclone.model.Empresa;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class ConfiguracoesEmpresaActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText edit_nome, edit_categoria, edit_tempo_entrega, edit_valor_entrega;
    private Button btn_salvar;
    private CircleImageView img_perfil;
    private Empresa empresa;
    private Uri uri_selecionada;
    private EmpresaDAO empresaDAO;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes_empresa);

        toolbar= findViewById(R.id.toolbar);
        toolbar.setTitle("Configurações");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edit_nome= findViewById(R.id.edit_nome_empresa);
        edit_categoria= findViewById(R.id.edit_categoria);
        edit_tempo_entrega= findViewById(R.id.edit_tempo_entrega);
        edit_valor_entrega= findViewById(R.id.edit_valor_entrega);
        btn_salvar= findViewById(R.id.btn_salvar_config_emp);
        img_perfil= findViewById(R.id.img_perfil_emp);
        recupera_configs();
        img_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(i, "Selecionar imagem"),
                        1);
            }
        });
        btn_salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validar_campos()){
                    empresa= new Empresa();
                    empresa.setId(Base64Custom.encode64(FirebaseSettings.
                            getFirebaseAuth().getCurrentUser().getEmail()));
                    empresa.setNome(edit_nome.getText().toString());
                    empresa.setCategoria(edit_categoria.getText().toString());
                    empresa.setTempo_entrega(edit_tempo_entrega.getText().toString());
                    empresa.setValor_entrega(edit_valor_entrega.getText().toString());
                    if(uri_selecionada!= null){
                        empresa.setFoto(uri_selecionada.toString());
                    }
                    empresaDAO= new EmpresaDAO(ConfiguracoesEmpresaActivity.this);
                    empresaDAO.salvar_dados_empresa(empresa);
                }
            }
        });
    }

    private boolean validar_campos(){
        if(edit_nome.getText().toString().isEmpty()){
            Toast.makeText(this, "Digite o nome da empresa",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        if(edit_categoria.getText().toString().isEmpty()){
            Toast.makeText(this, "Digite a categoria",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        if(edit_tempo_entrega.getText().toString().isEmpty()){
            Toast.makeText(this, "Digite o tempo de entrega",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        if(edit_valor_entrega.getText().toString().isEmpty()){
            Toast.makeText(this, "Digite o valor de entrega",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LoadingDialog loadingDialog= new LoadingDialog(ConfiguracoesEmpresaActivity.this);
        loadingDialog.load_alert_dialog();
        String id= Base64Custom.encode64(FirebaseSettings.getFirebaseAuth().
                getCurrentUser().getEmail());
        if(requestCode == 1){
            Uri img= data.getData();
            try {
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(), img);
                ByteArrayOutputStream baos= new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                byte[] dados_img= baos.toByteArray();
                StorageReference storageReference= FirebaseSettings.getStorage().
                        child("imagens").child("perfil_empresa").child(id).child("perfil.jpeg");
                UploadTask uploadTask= storageReference.putBytes(dados_img);
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                uri_selecionada= task.getResult();
                                Glide.with(getApplicationContext()).load(uri_selecionada).into(img_perfil);
                                loadingDialog.dismiss_dialog();
                            }
                        });
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void recupera_configs(){
        databaseReference= FirebaseSettings.getDatabaseReference();
        String id= Base64Custom.encode64(FirebaseSettings.getFirebaseAuth().getCurrentUser()
            .getEmail());
        databaseReference.child("empresas").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Empresa empresa= snapshot.getValue(Empresa.class);
                    Glide.with(getApplicationContext()).load(Uri.parse(empresa.getFoto())).
                            into(img_perfil);
                    edit_nome.setText(empresa.getNome());
                    edit_categoria.setText(empresa.getCategoria());
                    edit_tempo_entrega.setText(empresa.getTempo_entrega());
                    edit_valor_entrega.setText(empresa.getValor_entrega());
                    uri_selecionada= Uri.parse(empresa.getFoto());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}