package com.example.ifoodclone.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.ifoodclone.R;
import com.example.ifoodclone.helper.FirebaseSettings;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class HomeActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private MaterialSearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        searchView= findViewById(R.id.search_view);
        toolbar= findViewById(R.id.toolbar);
        toolbar.setTitle("IFood Cliente");
        setSupportActionBar(toolbar);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater= getMenuInflater();
        menuInflater.inflate(R.menu.menu_cliente, menu);

        MenuItem item= menu.findItem(R.id.item_pesquisa);
        searchView.setMenuItem(item);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.item_config_cliente){
            startActivity(new Intent(HomeActivity.this,
                    ConfiguracaoesClienteActivity.class));
        }
        if(item.getItemId() == R.id.item_sair_cliente){
            FirebaseSettings.getFirebaseAuth().signOut();
            startActivity(new Intent(HomeActivity.this,
                    AutenticacaoActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }
}