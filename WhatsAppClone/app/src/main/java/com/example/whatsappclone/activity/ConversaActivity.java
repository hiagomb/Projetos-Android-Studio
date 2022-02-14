package com.example.whatsappclone.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.whatsappclone.R;
import com.example.whatsappclone.adapter.AdapterListaConversaUnica;
import com.example.whatsappclone.helper.Base64Custom;
import com.example.whatsappclone.helper.ConfigFirebase;
import com.example.whatsappclone.helper.Permission;
import com.example.whatsappclone.model.Conversa;
import com.example.whatsappclone.model.Grupo;
import com.example.whatsappclone.model.Mensagem;
import com.example.whatsappclone.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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
import android.hardware.lights.LightState;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class ConversaActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private String nome_dest, email_dest, foto_contato, is_group;
    private EditText input_message;
    private ImageButton btn_send;
    private RecyclerView recycler_cv_unica;
    private FirebaseAuth auth;
    private DatabaseReference reference;
    private List<Mensagem> lista;
    private ImageView img_toolbar;
    private TextView texto_toolbar;
    public String aux_id;
    private ImageView img_camera;
    private Grupo grupo;
    private String email_dest_64;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversa);

        Bundle extra= getIntent().getExtras();
        if(extra != null){
            nome_dest= extra.getString("nomeContato");
            email_dest= extra.getString("emailContato");
            foto_contato= extra.getString("fotoContato");
            is_group= extra.getString("is_group");
            if(is_group.equalsIgnoreCase("true")){
                grupo= (Grupo) extra.getSerializable("grupo");
                email_dest_64= email_dest;
            }else{
                email_dest_64= Base64Custom.encode64(email_dest);
            }

        }

        toolbar= findViewById(R.id.toolbar_conversa);
//        toolbar.setTitle(nome_dest);
        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        setSupportActionBar(toolbar);

        img_toolbar= findViewById(R.id.img_toolbar);
        texto_toolbar= findViewById(R.id.nome_chat);

        if(foto_contato!= null){
            Uri uri_foto= Uri.parse(foto_contato);
            Glide.with(ConversaActivity.this).load(uri_foto).into(img_toolbar);
        }
        texto_toolbar.setText(nome_dest);

        img_camera= findViewById(R.id.img_anexar_cam);
        img_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] permissoes= new String[]{Manifest.permission.CAMERA};
                Permission.valida_Permissoes(ConversaActivity.this, permissoes, 1);
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(i, 100);
            }
        });

        input_message= findViewById(R.id.input_message);
        btn_send= findViewById(R.id.btn_send_message);
        recycler_cv_unica= findViewById(R.id.recycler_conversa_unica);
        fill_lista_mensagens();


        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message= input_message.getText().toString();
                if(!message.isEmpty()){
                    Mensagem mensagem= new Mensagem();
                    String id= Base64Custom.encode64(ConfigFirebase.getFirebaseAuth().getCurrentUser().getEmail());
                    mensagem.setId_rem(id);
                    mensagem.setMessage(message);



                    reference= ConfigFirebase.getFirebase();
                    if(is_group.equalsIgnoreCase("true")){
                        for(Usuario usuario: grupo.getMembros()){
                            String usuario_id= Base64Custom.encode64(usuario.getEmail());
                            mensagem.setIs_group("true");
                            mensagem.setId_dest(email_dest);
                            mensagem.setNome_usuario_for_group(ConfigFirebase.getUsuarioLogado().getNome());
                            reference.child("mensagens").child(usuario_id).child(email_dest).
                                    push().setValue(mensagem);
                            Conversa conversa= new Conversa();
                            conversa.setIsGroup("true");
                            conversa.setGrupo(grupo);
                            conversa.setId_usuario(email_dest);
                            conversa.setMensagem(message);
                            reference.child("conversas").child(usuario_id).child(email_dest).
                                    setValue(conversa);
                        }
                    }else{
                        mensagem.setIs_group("false");
                        reference.child("mensagens").child(id).child(email_dest_64).push().
                                setValue(mensagem);
                        reference.child("mensagens").child(email_dest_64).child(id).push().
                                setValue(mensagem);
                        Conversa conversa= new Conversa();

                        //pegando o nome do destinatario
                        reference.child("usuarios").child(email_dest_64).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.getValue()!= null){
                                    Usuario u= snapshot.getValue(Usuario.class);

                                    conversa.setId_usuario(email_dest_64);
                                    conversa.setMensagem(message);
                                    if(is_group.equalsIgnoreCase("true")){
                                        conversa.setNome(grupo.getNome());
                                    }else{
                                        conversa.setNome(u.getNome());
                                    }
                                    reference.child("conversas").child(id).child(email_dest_64).setValue(conversa);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });



                        //tenho que inverter os atributos do objeto para salvar no destinatario

                        reference.child("usuarios").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.getValue()!= null){
                                    Usuario u= snapshot.getValue(Usuario.class);
                                    conversa.setNome(u.getNome());
                                    conversa.setId_usuario(id);
                                    reference.child("conversas").child(email_dest_64).child(id).setValue(conversa);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }





                    input_message.setText("");
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            Bitmap img= null;
            try{
                img= (Bitmap) data.getExtras().get("data");
                String email_id= Base64Custom.encode64(ConfigFirebase.
                        getFirebaseAuth().getCurrentUser().getEmail());
                ByteArrayOutputStream baos= new ByteArrayOutputStream();
                img.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                byte[] dados_img= baos.toByteArray();
                String nomeImagem= UUID.randomUUID().toString();
                StorageReference ref_img = ConfigFirebase.getFirebaseStorage().
                        child("imagens").child("mensagens").child(email_id).child(nomeImagem+".jpeg");
                UploadTask uploadTask= ref_img.putBytes(dados_img);
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        ref_img.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                Uri uri= task.getResult();
                                Mensagem mensagem= new Mensagem();
                                mensagem.setId_rem(email_id);
                                mensagem.setMessage("imagem.jpeg");
                                mensagem.setFoto(uri.toString());

                                reference= ConfigFirebase.getFirebase();

                                if(is_group.equalsIgnoreCase("true")){
                                    mensagem.setIs_group("true");
                                    mensagem.setId_dest(email_dest);
                                    for(Usuario u: grupo.getMembros()){
                                        String usuario_id= Base64Custom.encode64(u.getEmail());
                                        mensagem.setNome_usuario_for_group(ConfigFirebase.getUsuarioLogado().getNome());
                                        reference.child("mensagens").child(usuario_id).child(email_dest).
                                                push().setValue(mensagem);
                                    }
                                }else{
                                    mensagem.setIs_group("false");
                                    reference.child("mensagens").child(email_id).child(email_dest_64).push().
                                            setValue(mensagem);
                                    reference.child("mensagens").child(email_dest_64).child(email_id).push().
                                            setValue(mensagem);
                                }
                            }
                        });
                    }
                });
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public void fill_lista_mensagens(){
        lista= new ArrayList<>();

        AdapterListaConversaUnica adapterListaConversaUnica= new AdapterListaConversaUnica(lista);
        //setting recycler view
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(getApplicationContext());
        recycler_cv_unica.setLayoutManager(layoutManager);
        recycler_cv_unica.setHasFixedSize(true);
        recycler_cv_unica.setAdapter(adapterListaConversaUnica);

        String user_source= Base64Custom.encode64(ConfigFirebase.getFirebaseAuth().getCurrentUser().getEmail());
        String user_dest= Base64Custom.encode64(email_dest);

        DatabaseReference ref_user = null;

        if(is_group.equalsIgnoreCase("true")){
            ref_user= ConfigFirebase.getFirebase().child("mensagens").
                    child(user_source).child(email_dest);
        }else{
            ref_user= ConfigFirebase.getFirebase().child("mensagens").
                    child(user_source).child(user_dest);
        }

        ref_user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lista.clear();
                for(DataSnapshot dados: snapshot.getChildren()){
                    System.out.println("teste de conversa");
                    Mensagem mensagem= dados.getValue(Mensagem.class);

                    aux_id= mensagem.getId_rem();
                    System.out.println("teste aqui dentro: "+aux_id);
                    lista.add(mensagem);
                }
                adapterListaConversaUnica.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
}