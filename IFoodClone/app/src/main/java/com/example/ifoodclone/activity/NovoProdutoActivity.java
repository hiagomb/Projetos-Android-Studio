package com.example.ifoodclone.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.icu.util.Currency;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cottacush.android.currencyedittext.CurrencyInputWatcher;
import com.example.ifoodclone.R;
import com.example.ifoodclone.dao.ProdutoDAO;
import com.example.ifoodclone.model.Produto;

import java.util.Locale;


public class NovoProdutoActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Button btn_cadastrar;
    private EditText edit_nome, edit_descricao, edit_preco;
    private Produto produto;
    private ProdutoDAO produtoDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_produto);

        toolbar= findViewById(R.id.toolbar);
        toolbar.setTitle("Novo produto");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btn_cadastrar= findViewById(R.id.btn_cadastrar_produto);
        edit_nome= findViewById(R.id.edit_nome_produto);
        edit_descricao= findViewById(R.id.edit_desc_produto);
        edit_preco= findViewById(R.id.edit_valor_produto);
        edit_preco.addTextChangedListener(new CurrencyInputWatcher(edit_preco, "R$", Locale.getDefault()));

        recupera_produto();
        btn_cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validar_campos()){
                    produtoDAO= new ProdutoDAO();
                    if(produto!= null){
                        produto.setNome(edit_nome.getText().toString());
                        produto.setDescricao(edit_descricao.getText().toString());
                        produto.setPreco(edit_preco.getText().toString());
                        if(produtoDAO.editar_produto(produto)){
                            Toast.makeText(NovoProdutoActivity.this,
                                    "Produto editado com sucesso", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }else{
                        produto= new Produto();
                        produto.setNome(edit_nome.getText().toString());
                        produto.setDescricao(edit_descricao.getText().toString());
                        produto.setPreco(edit_preco.getText().toString());
                        if(produtoDAO.cadastrar_produto(produto)){
                            Toast.makeText(NovoProdutoActivity.this,
                                    "Produto cadastrado com sucesso", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                }
            }
        });
    }


    private boolean validar_campos(){
        if(edit_nome.getText().toString().isEmpty()){
            Toast.makeText(this, "Digite o nome do produto",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        if(edit_descricao.getText().toString().isEmpty()){
            Toast.makeText(this, "Digite a descrição do produto",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        if(edit_preco.getText().toString().isEmpty()){
            Toast.makeText(this, "Digite o valor do produto",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void recupera_produto(){
        if(getIntent().getExtras()!= null){
            produto= (Produto) getIntent().getExtras().getSerializable("produto");
            edit_nome.setText(produto.getNome().trim());
            edit_descricao.setText(produto.getDescricao().trim());
            edit_preco.setText(produto.getPreco().trim());
        }
    }

}