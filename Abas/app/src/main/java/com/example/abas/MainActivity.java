package com.example.abas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.abas.fragment.ContatoFragment;
import com.example.abas.fragment.HomeFragment;
import com.example.abas.fragment.LigacaoFragment;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

public class MainActivity extends AppCompatActivity {

    private SmartTabLayout smartTab;
    private ViewPager vp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        smartTab= findViewById(R.id.viewPagerTab);
        vp= findViewById(R.id.viewPager);

        //Configurar Adapter para as abas
        FragmentPagerItemAdapter adapter= new FragmentPagerItemAdapter(getSupportFragmentManager(),
                FragmentPagerItems.with(this).
                        add("Home", HomeFragment.class)
                        .add("Contatos", ContatoFragment.class)
                        .add("Ligações", LigacaoFragment.class).create());

        vp.setAdapter(adapter);
        smartTab.setViewPager(vp);

        getSupportActionBar().setElevation(0);
    }
}