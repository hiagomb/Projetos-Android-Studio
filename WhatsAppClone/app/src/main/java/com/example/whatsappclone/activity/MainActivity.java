package com.example.whatsappclone.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.whatsappclone.R;
import com.example.whatsappclone.dao.UsuarioDAO;
import com.example.whatsappclone.fragment.ContatosFragment;
import com.example.whatsappclone.fragment.ConversasFragment;
import com.example.whatsappclone.helper.Base64Custom;
import com.example.whatsappclone.helper.ConfigFirebase;
import com.example.whatsappclone.model.Usuario;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private SmartTabLayout smartTabLayout;
    private ViewPager viewPager;
    private DatabaseReference reference;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth= ConfigFirebase.getFirebaseAuth();

        toolbar= findViewById(R.id.toolbar_principal);
        toolbar.setTitle("WhatsApp");
        setSupportActionBar(toolbar); //método apenas de suporte para funcionar corretamente

        smartTabLayout= findViewById(R.id.viewpagertab);
        viewPager= findViewById(R.id.viewpager);

        FragmentPagerItemAdapter adapter= new FragmentPagerItemAdapter(getSupportFragmentManager(),
                FragmentPagerItems.with(this).add("Conversas", ConversasFragment.class).
                        add("Contatos", ContatosFragment.class).create());
        viewPager.setAdapter(adapter);
        smartTabLayout.setViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater= getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        MenuItem menuItem= menu.getItem(0);
        
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.acao_sair:
                UsuarioDAO dao= new UsuarioDAO(MainActivity.this);
                dao.deslogar();
                startActivity(new Intent(MainActivity.this, LoginEmailSenhaActivity.class));
                finish();
                return true;
            case R.id.acao_adicionar:
                openCadastroContato();
                return true;
            case R.id.acao_pesquisar:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void openCadastroContato(){
        AlertDialog.Builder alertDialog= new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Novo contato");
        alertDialog.setMessage("Email do usuário");
        alertDialog.setCancelable(false);
        alertDialog.setIcon(R.drawable.person_add_black);

        EditText editText= new EditText(MainActivity.this);
        alertDialog.setView(editText);

        alertDialog.setPositiveButton("Cadastrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String email_contato= editText.getText().toString();
                if(email_contato.isEmpty()){
                    Toast.makeText(MainActivity.this, "Digite um email", Toast.LENGTH_SHORT).show();
                }else{
                    String email64= Base64Custom.encode64(email_contato);
                    reference= ConfigFirebase.getFirebase().child("usuarios").child(email64);
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.getValue()!= null){
                                Usuario usuarioContato= snapshot.getValue(Usuario.class);

                                reference= ConfigFirebase.getFirebase();
                                String email64_usuario= Base64Custom.encode64(auth.getCurrentUser().getEmail());
                                reference.child("contatos").child(email64_usuario).child(email64).
                                        setValue(usuarioContato);
                            }else{
                                Toast.makeText(MainActivity.this, "O usuário não possui cadastro",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alertDialog.create().show();
    }
}