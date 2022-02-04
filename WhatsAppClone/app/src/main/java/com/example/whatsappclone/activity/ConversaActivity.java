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
import com.example.whatsappclone.model.Mensagem;
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
//                    String id= Base64Custom.encode64(ConfigFirebase.getFirebaseAuth().getCurrentUser().getEmail());
//                    mensagem.setIdUsuario(id);
                    mensagem.setMessage(message);

//                    reference= ConfigFirebase.getFirebase();
//                    reference.child("mensagens").child(id).child(Base64Custom.encode64(email_dest)).
//                            setValue(mensagem);
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
//                    lista.add(dados.getValue())
                }
                adapterListaConversaUnica.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}