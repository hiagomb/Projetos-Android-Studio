package com.example.whatsappclone.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.whatsappclone.R;
import com.example.whatsappclone.helper.Permission;
import com.example.whatsappclone.helper.Preferences;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.Random;

public class LoginActivity extends AppCompatActivity {

    private EditText edit_region, edit_ddd, edit_number;
    private Button btn_cadastrar;
    private TextInputEditText edit_nome;
    private String[] permissions= new String[]{Manifest.permission.SEND_SMS};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Permission.valida_Permissoes(this, permissions, 1);

        edit_region= findViewById(R.id.edit_region);
        edit_ddd= findViewById(R.id.edit_ddd);
        edit_number= findViewById(R.id.edit_phone);
        btn_cadastrar= findViewById(R.id.btn_cadastrar);
        edit_nome= findViewById(R.id.input_nome);

        SimpleMaskFormatter maskFormatter= new SimpleMaskFormatter("NNNNN - NNNN");
        MaskTextWatcher watcher= new MaskTextWatcher(edit_number, maskFormatter);
        edit_number.addTextChangedListener(watcher);

        SimpleMaskFormatter maskFormatter_reg= new SimpleMaskFormatter("+NN");
        MaskTextWatcher watcher_reg= new MaskTextWatcher(edit_region, maskFormatter_reg);
        edit_region.addTextChangedListener(watcher_reg);

        SimpleMaskFormatter maskFormatter_ddd= new SimpleMaskFormatter("NN");
        MaskTextWatcher watcher_ddd= new MaskTextWatcher(edit_ddd, maskFormatter_ddd);
        edit_ddd.addTextChangedListener(watcher_ddd);

        btn_cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nome= edit_nome.getText().toString();
                String phone= edit_region.getText().toString()+edit_ddd.getText().toString()+
                        edit_number.getText().toString();
                phone= phone.replace("+", "");
                phone= phone.replace(" ", "");
                phone= phone.replace("-", "");

                int random= new Random().nextInt(8999);
                random+= 1000;

                String random_number= String.valueOf(random);

                Preferences preferences= new Preferences(getApplicationContext());
                preferences.salvar_usuario_preferences(nome, phone, random_number);

                HashMap<String, String> dados= preferences.getDadosUsuario();
                System.out.println("Nome: "+dados.get("nome")+"  ||  Telefone: "+
                        dados.get("phone")+"  ||  Token: "+dados.get("token"));

                if(send_Token_SMS("5554", "Código de confirmação" +
                        " do WhatsApp: "+dados.get("token"))){
                    Intent intent= new Intent(LoginActivity.this, ValidacaoActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(LoginActivity.this, "Erro ao enviar SMS," +
                            " tente novamente", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public boolean send_Token_SMS(String phone, String message){
        try{
            SmsManager smsManager= SmsManager.getDefault();
            smsManager.sendTextMessage(phone, null, message, null, null);
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void onRequestPermissionsResult(int request_code, String[] permissions, int[] grantResults){//callback caso permissão seja negada
        super.onRequestPermissionsResult(request_code, permissions, grantResults);
        for(int resultado: grantResults){
            if(resultado == PackageManager.PERMISSION_DENIED){
                alerta_validacao_permissao();
            }
        }
    }

    public void alerta_validacao_permissao(){
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setTitle("Permissões Negadas");
        builder.setMessage("Para utilizar esse app é necessário aceitar as permissões");
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });

        builder.create().show();
    }

}