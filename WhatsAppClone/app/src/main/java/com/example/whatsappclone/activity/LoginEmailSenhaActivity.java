package com.example.whatsappclone.activity;

import androidx.appcompat.app.AppCompatActivity;
import com.example.whatsappclone.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class LoginEmailSenhaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_email_senha);
    }

    public void openCadastro(View view){
        Intent intent= new Intent(LoginEmailSenhaActivity.this, CadastroEmailSenhaActivity.class);
        startActivity(intent);
    }
}