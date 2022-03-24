package com.example.ifoodclone.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ifoodclone.R;
import com.example.ifoodclone.dao.ClienteDAO;
import com.example.ifoodclone.helper.Base64Custom;
import com.example.ifoodclone.helper.FirebaseSettings;
import com.example.ifoodclone.helper.LoadingDialog;
import com.example.ifoodclone.model.Cliente;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ConfiguracaoesClienteActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText edit_cep, edit_cidade, edit_rua, edit_numero, edit_bairro;
    private Button btn_salvar, btn_buscar_cep;
    private LoadingDialog loadingDialog;
    private Cliente cliente;
    private ClienteDAO clienteDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracaoes_cliente);

        toolbar= findViewById(R.id.toolbar);
        toolbar.setTitle("Configurações");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edit_cep= findViewById(R.id.edit_cep);
        edit_cidade= findViewById(R.id.edit_cidade);
        edit_rua= findViewById(R.id.edit_rua);
        edit_numero= findViewById(R.id.edit_numero);
        edit_bairro= findViewById(R.id.edit_bairro);
        btn_salvar= findViewById(R.id.btn_salvar_endereco);
        btn_buscar_cep= findViewById(R.id.btn_buscar_cep);

        recupera_config();


        edit_bairro.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().trim().length() != 0){
                    if(edit_rua.getText().toString().trim().length() != 0){
                        if(edit_numero.getText().toString().trim().length() != 0){
                            btn_salvar.setVisibility(View.VISIBLE);
                        }else{
                            btn_salvar.setVisibility(View.GONE);
                        }
                    }
                }else{
                    btn_salvar.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        SimpleMaskFormatter smf= new SimpleMaskFormatter("NNNNN-NNN");
        MaskTextWatcher mtw= new MaskTextWatcher(edit_cep, smf);
        edit_cep.addTextChangedListener(mtw);

        btn_buscar_cep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cep= edit_cep.getText().toString().replace("-", "");
                if(cep.length() != 8){
                    Snackbar.make(findViewById(R.id.constraint_cliente),
                            "Digite um CEP válido", Snackbar.LENGTH_SHORT).show();
                }else{
                    loadingDialog= new LoadingDialog(ConfiguracaoesClienteActivity.this);
                    loadingDialog.load_alert_dialog();
                    MyTask myTask= new MyTask();
                    String url= "https://viacep.com.br/ws/"+cep+"/json/";
                    myTask.execute(url);
                }
            }
        });

        btn_salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cliente= new Cliente();
                cliente.setId(Base64Custom.encode64(FirebaseSettings.
                        getFirebaseAuth().getCurrentUser().getEmail()));
                cliente.setCep(edit_cep.getText().toString());
                cliente.setCidade(edit_cidade.getText().toString());
                cliente.setRua(edit_rua.getText().toString());
                cliente.setNumero(edit_numero.getText().toString());
                cliente.setBairro(edit_bairro.getText().toString());

                clienteDAO= new ClienteDAO();
                clienteDAO.set_endereco(cliente);
                Toast.makeText(ConfiguracaoesClienteActivity.this,
                        "Endereço cadastrado com sucesso", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void configura_campos(int visibility){
        edit_cidade.setEnabled(false);
        edit_cidade.setVisibility(visibility);
        edit_rua.setVisibility(visibility);
        edit_numero.setVisibility(visibility);
        edit_bairro.setVisibility(visibility);
        //btn_salvar.setVisibility(visibility);
    }

    private void recupera_config(){
        configura_campos(View.GONE);
        btn_salvar.setVisibility(View.GONE);

        DatabaseReference databaseReference= FirebaseSettings.getDatabaseReference();
        String id= Base64Custom.encode64(FirebaseSettings.getFirebaseAuth().
                getCurrentUser().getEmail());
        databaseReference.child("clientes").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Cliente cliente= snapshot.getValue(Cliente.class);
                    edit_cep.setText(cliente.getCep());
                    edit_cidade.setText(cliente.getCidade());
                    edit_rua.setText(cliente.getRua());
                    edit_numero.setText(cliente.getNumero());
                    edit_bairro.setText(cliente.getBairro());
                    configura_campos(View.VISIBLE);
                }else{
                    configura_campos(View.GONE);
                    btn_salvar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    class MyTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            String url_api= strings[0];
            StringBuffer buffer= new StringBuffer();
            try{
                URL url= new URL(url_api);
                HttpURLConnection connection= (HttpURLConnection) url.openConnection();
                //
                InputStream inputStream= connection.getInputStream();
                //
                InputStreamReader reader= new InputStreamReader(inputStream);
                //
                BufferedReader bufferedReader= new BufferedReader(reader);
                String linha= "";
                while((linha= bufferedReader.readLine())!= null){
                    buffer.append(linha);
                }
            }catch(Exception e){
                e.printStackTrace();
            }


            return buffer.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            String cidade= null;
            //processando resposta em json
            try {
                JSONObject jsonObject= new JSONObject(s);
                cidade= jsonObject.getString("localidade");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(cidade!= null){
                edit_cidade.setText(cidade);
                configura_campos(View.VISIBLE);
                edit_rua.requestFocus();
            }else{
                Toast.makeText(ConfiguracaoesClienteActivity.this,
                        "CEP não encontrado", Toast.LENGTH_SHORT).show();
            }
            loadingDialog.dismiss_dialog();

        }
    }

}