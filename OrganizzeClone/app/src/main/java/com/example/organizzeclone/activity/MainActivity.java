package com.example.organizzeclone.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.organizzeclone.R;
import com.example.organizzeclone.helper.FirebaseSettings;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;

public class MainActivity extends IntroActivity {

    private Button btn_go_to_cadastro;
    private TextView go_to_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(FirebaseSettings.getFirebaseAuth().getCurrentUser()!= null){
            startActivity(new Intent(this, PrincipalActivity.class));
        }

        setButtonBackVisible(false);
        setButtonNextVisible(false);
        

        addSlide(new FragmentSlide.Builder()
                .background(R.color.white)
                .fragment(R.layout.intro_1).build());

        addSlide(new FragmentSlide.Builder()
                .background(R.color.white)
                .fragment(R.layout.intro_2).build());

        addSlide(new FragmentSlide.Builder()
                .background(R.color.white)
                .fragment(R.layout.intro_3).build());

        addSlide(new FragmentSlide.Builder()
                .background(R.color.white)
                .fragment(R.layout.intro_4).build());

        addSlide(new FragmentSlide.Builder()
                .background(R.color.white).canGoForward(false)
                .fragment(R.layout.fragment_cadastro_login).build());


        btn_go_to_cadastro= findViewById(R.id.btn_ir_para_cadastro);
        go_to_login= findViewById(R.id.text_ir_para_login);
    }



    public void ir_para_cadastro(View view){
        startActivity(new Intent(MainActivity.this, CadastroActivity.class));
    }

    public void ir_para_login(View view){
        startActivity(new Intent(MainActivity.this,LoginActivity.class));
    }
}