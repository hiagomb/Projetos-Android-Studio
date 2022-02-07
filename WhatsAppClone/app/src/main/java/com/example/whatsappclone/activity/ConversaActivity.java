package com.example.whatsappclone.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsappclone.R;
import com.example.whatsappclone.adapter.AdapterListaConversaUnica;
import com.example.whatsappclone.helper.Base64Custom;
import com.example.whatsappclone.helper.ConfigFirebase;
import com.example.whatsappclone.model.Conversa;
import com.example.whatsappclone.model.Mensagem;
import com.example.whatsappclone.model.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import android.hardware.lights.LightState;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;


public class ConversaActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private String nome_dest, email_dest;
    private EditText input_message;
    private ImageButton btn_send;
    private RecyclerView recycler_cv_unica;
    private FirebaseAuth auth;
    private DatabaseReference reference;
    private List<Mensagem> lista;
    public String aux_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversa);

        Bundle extra= getIntent().getExtras();
        if(extra != null){
            nome_dest= extra.getString("nomeContato");
            email_dest= extra.getString("emailContato");
        }

        toolbar= findViewById(R.id.toolbar_conversa);
        toolbar.setTitle(nome_dest);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        setSupportActionBar(toolbar);

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
                    String email_dest_64= Base64Custom.encode64(email_dest);

                    reference= ConfigFirebase.getFirebase();
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
                                conversa.setNome(u.getNome());
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


                    input_message.setText("");
                }
            }
        });
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

        DatabaseReference ref_user= ConfigFirebase.getFirebase().child("mensagens").
                child(user_source).child(user_dest);

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
}