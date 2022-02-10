package com.example.whatsappclone.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.whatsappclone.R;
import com.example.whatsappclone.helper.Base64Custom;
import com.example.whatsappclone.helper.ConfigFirebase;
import com.example.whatsappclone.helper.Permission;
import com.example.whatsappclone.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class ConfiguracoesActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView img;
    private TextInputEditText input_nome;
    public String[] permissoes;
    private ImageButton img_camera, img_photo;
    private static final int SELECAO_CAMERA= 100;
    private static final int SELECAO_GALERIA= 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes);

        toolbar= findViewById(R.id.toolbar_principal);
        toolbar.setTitle("Configurações");
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        setSupportActionBar(toolbar);

        img= findViewById(R.id.profile_image);
        //recuperar foto do usuario
        if(ConfigFirebase.getFirebaseAuth().getCurrentUser().getPhotoUrl()!= null){
            Glide.with(getApplicationContext()).load(ConfigFirebase.
                    getFirebaseAuth().getCurrentUser().getPhotoUrl()).into(img);
        }else{
            img.setImageResource(R.drawable.padrao);
        }
        input_nome= findViewById(R.id.edit_nome_config);
        img_camera= findViewById(R.id.imageButton_camera);
        img_photo= findViewById(R.id.imageButton_photo);

        img_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(i, SELECAO_CAMERA);

            }
        });

        img_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(i, "Selecionar foto"), SELECAO_GALERIA);
            }
        });



        permissoes= new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        Permission.valida_Permissoes(ConfiguracoesActivity.this, permissoes, 1);

        String email64= Base64Custom.encode64(ConfigFirebase.getFirebaseAuth().getCurrentUser().getEmail());
        DatabaseReference reference= ConfigFirebase.getFirebase().child("usuarios").child(email64);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue()!= null){
                    Usuario u= snapshot.getValue(Usuario.class);
                    input_nome.setText(u.getNome());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== RESULT_OK){
            Bitmap imagem= null;
            try{
                if(requestCode == SELECAO_CAMERA){
                    imagem= (Bitmap) data.getExtras().get("data");
                }else if(requestCode == SELECAO_GALERIA){
                    Uri local_imagem= data.getData();
                    imagem= MediaStore.Images.Media.getBitmap(getContentResolver(), local_imagem);
                }
                if(imagem!= null){
                    img.setImageBitmap(imagem);

                    String email_id= Base64Custom.encode64(ConfigFirebase.
                            getFirebaseAuth().getCurrentUser().getEmail());

                    ByteArrayOutputStream baos= new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                    byte[] dados_img= baos.toByteArray();
                    StorageReference ref_img = ConfigFirebase.getFirebaseStorage().
                            child("imagens").child("perfil").child(email_id).
                            child("perfil.jpeg");
                    UploadTask uploadTask= ref_img.putBytes(dados_img);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ConfiguracoesActivity.this, "Erro", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            ref_img.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    Uri uri= task.getResult();
                                    atualizarFotoEmConfig(uri);
                                }
                            });
                        }
                    });
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public void atualizarNome(View view){
        if(input_nome.getText().toString().isEmpty()){
            Toast.makeText(ConfiguracoesActivity.this, "O nome não pode ser vazio",
                    Toast.LENGTH_SHORT).show();
        }else{
            String email_id= Base64Custom.encode64(ConfigFirebase.getFirebaseAuth().
                    getCurrentUser().getEmail());
            DatabaseReference reference= ConfigFirebase.getFirebase().child("usuarios").child(email_id);
            HashMap<String, Object> hashMap= new HashMap<>();
            hashMap.put("email", ConfigFirebase.getFirebaseAuth().getCurrentUser().getEmail());
            hashMap.put("nome", input_nome.getText().toString());
            reference.updateChildren(hashMap);
        }
    }

    public void atualizarFotoEmConfig(Uri uri){
        FirebaseUser user = ConfigFirebase.getFirebaseAuth().getCurrentUser();
        UserProfileChangeRequest profileChangeRequest= new UserProfileChangeRequest.Builder().
                setPhotoUri(uri).build();

        //atualizar perfil (foto do usuario) - via getCurrentUser
        user.updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(!task.isSuccessful()){
                    System.out.println("erro");
                }
            }
        });

        //recuperar foto do usuario
        if(user.getPhotoUrl()!= null){
            Glide.with(getApplicationContext()).load(user.getPhotoUrl()).into(img);
        }else{
            img.setImageResource(R.drawable.padrao);
        }

        //vou setar no objeto tambem e na base de usuarios para ficar mais facil na listagem
        String id= Base64Custom.encode64(user.getEmail());
        ConfigFirebase.atualizarObjetoeBaseComFoto(id, user.getPhotoUrl());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for(int permissaoResultado: grantResults){
            if(permissaoResultado == PackageManager.PERMISSION_DENIED){
                AlertDialog.Builder builder= new AlertDialog.Builder(this);
                builder.setTitle("Permissões negadas");
                builder.setCancelable(false);
                builder.setMessage("Para utilizar essa seção é ncessário autorizar as permissões");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
                builder.create().show();
            }
        }
    }
}