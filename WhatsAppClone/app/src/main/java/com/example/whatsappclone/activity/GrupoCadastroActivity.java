package com.example.whatsappclone.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsappclone.R;
import com.example.whatsappclone.adapter.AdapterMembrosSelecionados;
import com.example.whatsappclone.dao.GrupoDAO;
import com.example.whatsappclone.helper.Base64Custom;
import com.example.whatsappclone.helper.ConfigFirebase;
import com.example.whatsappclone.helper.Permission;
import com.example.whatsappclone.model.Grupo;
import com.example.whatsappclone.model.Mensagem;
import com.example.whatsappclone.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
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
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class GrupoCadastroActivity extends AppCompatActivity {

    private List<Usuario> lista_membros_selecionados;
    private TextView txt_participantes;
    private CircleImageView imagem_grupo;
    private FloatingActionButton fab_criar_grupo;
    private Toolbar toolbar;
    private RecyclerView rv_criar_grupo;
    private AdapterMembrosSelecionados adapterMembrosSelecionados;
    private UUID random;
    private TextInputEditText input_nome_grupo;
    private Grupo grupo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grupo_cadastro);

        random= UUID.randomUUID();
        input_nome_grupo= findViewById(R.id.txt_nome_grupo_criar);
        grupo = new Grupo();

        toolbar= findViewById(R.id.toolbar_grupo_criar);
        toolbar.setTitle("Novo grupo");
        toolbar.setSubtitle("Defina o nome");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(getIntent().getExtras()!= null){
            List<Usuario> membros= (List<Usuario>) getIntent().getExtras().getSerializable("membros");
            lista_membros_selecionados= new ArrayList<>();
            lista_membros_selecionados.addAll(membros);
        }

        txt_participantes= findViewById(R.id.txt_total_participantes);
        txt_participantes.setText("Participantes: "+lista_membros_selecionados.size());

        //recycler view
        rv_criar_grupo= findViewById(R.id.rv_membros_grupo_criar);
        adapterMembrosSelecionados= new AdapterMembrosSelecionados(lista_membros_selecionados);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.HORIZONTAL, false);
        rv_criar_grupo.setLayoutManager(layoutManager);
        rv_criar_grupo.setHasFixedSize(true);
        rv_criar_grupo.setAdapter(adapterMembrosSelecionados);

        fab_criar_grupo= findViewById(R.id.fab_criar_grupo);
        fab_criar_grupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                grupo.setId(random.toString());
                grupo.setNome(input_nome_grupo.getText().toString());
//                grupo.setFoto("");
                lista_membros_selecionados.add(ConfigFirebase.getUsuarioLogado());
                grupo.setMembros(lista_membros_selecionados);
                if(new GrupoDAO().salvar(grupo)){
                    Intent i= new Intent(GrupoCadastroActivity.this, ConversaActivity.class);
                    //tenho que passar os extras
                    i.putExtra("nomeContato", grupo.getNome());
                    i.putExtra("emailContato",
                            grupo.getId());
                    i.putExtra("fotoContato", grupo.getFoto());
                    i.putExtra("grupo", grupo);
                    i.putExtra("is_group", "true");
                    startActivity(i);
                }
            }
        });

        imagem_grupo= findViewById(R.id.img_grupo_criar);
        imagem_grupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] permissoes= new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
                Permission.valida_Permissoes(GrupoCadastroActivity.this, permissoes, 1);
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(i, 100);
            }
        });


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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            Bitmap img= null;
            try{
                img= (Bitmap) data.getExtras().get("data");
                imagem_grupo.setImageBitmap(img);

                ByteArrayOutputStream baos= new ByteArrayOutputStream();
                img.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                byte[] dados_img= baos.toByteArray();

                StorageReference ref_img = ConfigFirebase.getFirebaseStorage().
                        child("imagens").child("grupos").child(random.toString()).child("foto_grupo.jpeg");
                UploadTask uploadTask= ref_img.putBytes(dados_img);
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        ref_img.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                Uri uri= task.getResult();
                                grupo.setFoto(uri.toString());
                            }
                        });
                    }
                });
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}