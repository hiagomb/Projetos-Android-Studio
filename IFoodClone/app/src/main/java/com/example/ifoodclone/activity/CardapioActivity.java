package com.example.ifoodclone.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ifoodclone.R;
import com.example.ifoodclone.adapter.AdapterProdutos;
import com.example.ifoodclone.api.NotificacaoService;
import com.example.ifoodclone.dao.PedidoDAO;
import com.example.ifoodclone.helper.Base64Custom;
import com.example.ifoodclone.helper.FirebaseSettings;
import com.example.ifoodclone.helper.QuantidadeDialog;
import com.example.ifoodclone.helper.RecyclerItemClickListener;
import com.example.ifoodclone.model.Cliente;
import com.example.ifoodclone.model.Empresa;
import com.example.ifoodclone.model.ItemPedido;
import com.example.ifoodclone.model.Notificacao;
import com.example.ifoodclone.model.NotificacaoDados;
import com.example.ifoodclone.model.Pedido;
import com.example.ifoodclone.model.Produto;
import com.example.ifoodclone.model.Token_Empresa;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CardapioActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView rv_cardapio;
    private CircleImageView img_empresa;
    private TextView txt_nome_empresa;
    private AdapterProdutos adapterProdutos;
    private List<Produto> lista;
    private DatabaseReference databaseReference;
    private Button btn_carrinho;
    private Cliente cliente;
    private List<ItemPedido> itens_carrinho;
    private Pedido pedido;
    private PedidoDAO pedidoDAO;
    private Empresa empresa;
    private int choice;
    private Retrofit retrofit;
    private String url_base;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardapio);

        //apagar
        FirebaseMessaging.getInstance().subscribeToTopic("noti");

        itens_carrinho= new ArrayList<>();
        toolbar= findViewById(R.id.toolbar);
        toolbar.setTitle("Cardápio");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rv_cardapio= findViewById(R.id.rv_cardapio);
        img_empresa= findViewById(R.id.img_empresa_cardapio);
        txt_nome_empresa= findViewById(R.id.txt_nome_empresa_cardapio);
        btn_carrinho= findViewById(R.id.btn_ver_carrinho);
        monta_tela();

        rv_cardapio.addOnItemTouchListener(new RecyclerItemClickListener(this,
                rv_cardapio, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                confirmar_quantidade(lista.get(position));
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        }));

        recupera_endereco();
    }

    private void recupera_endereco(){
        databaseReference= FirebaseSettings.getDatabaseReference();
        String id = Base64Custom.encode64(FirebaseSettings.getFirebaseAuth().
                getCurrentUser().getEmail());
        databaseReference.child("clientes").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    cliente= snapshot.getValue(Cliente.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void confirmar_quantidade(Produto produto){
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setTitle("Quantidade");
        builder.setMessage("Digite a quantidade");
        EditText edit_quantidade= new EditText(this);
        edit_quantidade.setText("1");
        builder.setView(edit_quantidade);
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                double quant= Double.parseDouble(edit_quantidade.getText().toString());
                ItemPedido itemPedido= new ItemPedido();
                itemPedido.setId_produto(produto.getId());
                itemPedido.setNome_produto(produto.getNome());
                itemPedido.setPreco(Double.parseDouble(produto.getPreco().substring(2)));
                itemPedido.setQuantidade(edit_quantidade.getText().toString());
                itens_carrinho.add(itemPedido);
                if(pedido == null){
                    pedido= new Pedido();
                    pedido.setId_cliente(Base64Custom.encode64(FirebaseSettings.getFirebaseAuth().getCurrentUser().getEmail()));
                    pedido.setId_empresa(empresa.getId());
                    pedido.setStatus("pendente");
                    pedido.setEndereco(cliente);
                    pedido.setItens(itens_carrinho);
                    pedido.setValor_total((itemPedido.getPreco() * quant));
                    pedidoDAO= new PedidoDAO();
                    pedidoDAO.salvar_pedido_usuario(pedido);
                }else{
                    List<ItemPedido> itens= pedido.getItens();
                    itens.add(itemPedido);
                    pedido.setItens(itens);
                    double valor= itemPedido.getPreco() * quant;
                    pedido.setValor_total(pedido.getValor_total() + valor);
                    pedidoDAO.salvar_pedido_usuario(pedido);
                }
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog dialog= builder.create();
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater= getMenuInflater();
        menuInflater.inflate(R.menu.menu_cardapio, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.item_confirmar_pedido){
            if(btn_carrinho.getText().toString().equalsIgnoreCase("ver carrinho")){
                AlertDialog.Builder builder= new AlertDialog.Builder(this);
                builder.setTitle("Confirmação de pedido");
                builder.setMessage("Insira algum item no carrinho para confirmar seu pedido");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog dialog= builder.create();
                dialog.show();
            }else{
                confirma_pedido();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void confirma_pedido(){
        databaseReference= FirebaseSettings.getDatabaseReference();
        String id_usuario= Base64Custom.encode64(FirebaseSettings.getFirebaseAuth().
                getCurrentUser().getEmail());
        pedido.setStatus("confirmado");

        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setTitle("Selecione um método de pagamento");

        CharSequence[] itens_r= new CharSequence[]{"Dinheiro", "Máquina de cartão"};
        choice= 0;
        builder.setSingleChoiceItems(itens_r, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                choice= i;
            }
        });
        EditText edit_obs= new EditText(this);
        edit_obs.setHint("Digite uma observação");
        builder.setView(edit_obs);
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                pedido.setObs(edit_obs.getText().toString());
                if(choice == 0){
                    pedido.setMetodo_pagamento("Dinheiro");
                }else{
                    pedido.setMetodo_pagamento("Máquina de cartão");
                }
                databaseReference.child("pedidos_usuario").child(empresa.getId()).child(id_usuario).removeValue();
                databaseReference.child("pedidos_empresa").child(empresa.getId()).
                            child(pedido.getId_pedido()).setValue(pedido);
                enviar_notificacao_pedido();
                btn_carrinho.setText("ver carrinho");
                pedido= null;
                itens_carrinho.clear();
            }
        });

        builder.create().show();
    }

    private void enviar_notificacao_pedido(){
        url_base= "https://fcm.googleapis.com/fcm/";
        retrofit= new Retrofit.Builder().baseUrl(url_base).addConverterFactory(GsonConverterFactory.create()).build();

        databaseReference= FirebaseSettings.getDatabaseReference();
        databaseReference.child("tokens").child(empresa.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Token_Empresa token= snapshot.getValue(Token_Empresa.class);
                    NotificacaoDados notificacaoDados= new NotificacaoDados();
                    notificacaoDados.setTo(token.getToken());
                    Notificacao notificacao= new Notificacao();
                    notificacao.setTitle("Novo pedido");
                    notificacao.setBody("Você recebeu um novo pedido");
                    notificacaoDados.setNotification(notificacao);

                    NotificacaoService service= retrofit.create(NotificacaoService.class);
                    Call<NotificacaoDados> call= service.salvar_notificacao(notificacaoDados);
                    call.enqueue(new Callback<NotificacaoDados>() {
                        @Override
                        public void onResponse(Call<NotificacaoDados> call, Response<NotificacaoDados> response) {
                            if(response.isSuccessful()){

                            }
                        }

                        @Override
                        public void onFailure(Call<NotificacaoDados> call, Throwable t) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void recupera_pedido_usuario(){
        databaseReference= FirebaseSettings.getDatabaseReference();
        String id_usuario= Base64Custom.encode64(FirebaseSettings.getFirebaseAuth().getCurrentUser().getEmail());
        databaseReference.child("pedidos_usuario").child(empresa.getId()).child(id_usuario)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            pedido= snapshot.getValue(Pedido.class);
                            btn_carrinho.setText("ver carrinho: R$"+pedido.getValor_total());
                            pedidoDAO= new PedidoDAO();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void monta_tela(){
        empresa= (Empresa) getIntent().getExtras().getSerializable("empresa");
        Glide.with(getApplicationContext()).load(Uri.parse
                (empresa.getFoto())).into(img_empresa);
        txt_nome_empresa.setText(empresa.getNome());
        //
        lista= new ArrayList<>();
        adapterProdutos= new AdapterProdutos(lista);
        RecyclerView.LayoutManager layoutManager= new
                LinearLayoutManager(getApplicationContext());
        rv_cardapio.setLayoutManager(layoutManager);
        rv_cardapio.setHasFixedSize(true);
        rv_cardapio.setAdapter(adapterProdutos);

        databaseReference= FirebaseSettings.getDatabaseReference();
        databaseReference.child("produtos").child(empresa.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lista.clear();
                if(snapshot.exists()){
                    for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                        lista.add(dataSnapshot.getValue(Produto.class));
                    }
                }
                adapterProdutos.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        recupera_pedido_usuario();
    }
}