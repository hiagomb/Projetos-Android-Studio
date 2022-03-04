package com.example.organizzeclone.activity;

import androidx.appcompat.app.AppCompatActivity;
import com.example.organizzeclone.R;
import com.example.organizzeclone.dao.MovimentacaoDAO;
import com.example.organizzeclone.helper.DateUtil;
import com.example.organizzeclone.model.Movimentacao;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class ReceitasActivity extends AppCompatActivity {

    private TextInputEditText input_data, input_categoria, input_descricao;
    private EditText input_valor;
    private FloatingActionButton fab_salvar;
    private Movimentacao movimentacao;
    private MovimentacaoDAO movimentacaoDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receitas);

        input_valor= findViewById(R.id.input_valor_receita);
        input_data = findViewById(R.id.input_data_receita);
        input_categoria= findViewById(R.id.input_categoria_receita);
        input_descricao= findViewById(R.id.input_descricao_receita);
        fab_salvar= findViewById(R.id.fab_salvar_receita);

        input_data.setText(DateUtil.getDataAtual());
        //definindo mascaras
        SimpleMaskFormatter mask_data= new SimpleMaskFormatter("NN/NN/NNNN");
        MaskTextWatcher maskTextWatcher= new MaskTextWatcher(input_data, mask_data);
        input_data.addTextChangedListener(maskTextWatcher);

        SimpleMaskFormatter mask_valor = new SimpleMaskFormatter("R$NNNNNNNNN");
        MaskTextWatcher mtw= new MaskTextWatcher(input_valor, mask_valor);
        input_valor.addTextChangedListener(mtw);


        fab_salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validarCampos()){
                    movimentacao= new Movimentacao();
                    String valor= input_valor.getText().toString().
                            replace("R", "");
                    valor= valor.replace("$", "");
                    movimentacao.setValor(valor);
                    movimentacao.setTipo("r");
                    movimentacao.setCategoria(input_categoria.getText().toString());
                    movimentacao.setData(input_data.getText().toString());
                    movimentacao.setDescricao(input_descricao.getText().toString());

                    movimentacaoDAO= new MovimentacaoDAO();
                    if(movimentacaoDAO.salvar_movimentacao(movimentacao)){
                        Toast.makeText(ReceitasActivity.this, "Receita" +
                                " salva com sucesso", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }
        });

    }


    public boolean validarCampos(){
        if(input_valor.getText().toString().isEmpty()){
            Toast.makeText(this, "Digite um valor", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(input_data.getText().toString().isEmpty()){
            Toast.makeText(this, "Digite uma data", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(input_categoria.getText().toString().isEmpty()){
            Toast.makeText(this, "Digite uma categoria", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(input_descricao.getText().toString().isEmpty()) {
            Toast.makeText(this, "Digite uma descrição", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}