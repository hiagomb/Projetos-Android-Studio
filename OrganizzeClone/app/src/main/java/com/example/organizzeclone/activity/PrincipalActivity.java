package com.example.organizzeclone.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.organizzeclone.R;
import com.example.organizzeclone.adapter.AdapterListaMovimentacao;
import com.example.organizzeclone.helper.Base64Mine;
import com.example.organizzeclone.helper.DateUtil;
import com.example.organizzeclone.helper.FirebaseSettings;
import com.example.organizzeclone.model.Movimentacao;
import com.example.organizzeclone.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class PrincipalActivity extends AppCompatActivity {

    private MaterialCalendarView calendarView;
    private TextView texto_nome, texto_saldo;
    private Double receita_total, despesa_total, resumo_total;
    private RecyclerView rv_lista;
    private AdapterListaMovimentacao adapter;
    private List<Movimentacao> lista;
    private String mes_ano;
    private String id_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        id_user= FirebaseSettings.getFirebaseAuth().getCurrentUser().getEmail();
        id_user= Base64Mine.encode(id_user);
        mes_ano= DateUtil.getDataAtualForMov();

        calendarView= findViewById(R.id.calendarView);
        CharSequence meses[]= {"Janeiro", "Fevereiro", "Março", "Abril", "Maio",
        "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"};
        calendarView.setTitleMonths(meses);
        calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                mes_ano= "";
                if((date.getMonth()+1)< 10){
                    mes_ano= "0"+(date.getMonth()+1);
                }else{
                    mes_ano= ""+(date.getMonth()+1);
                }
                mes_ano += date.getYear();
                fill_lista_movimentacoes(mes_ano);
            }
        });

        texto_nome= findViewById(R.id.text_nome_usuario);
        texto_saldo= findViewById(R.id.text_saldo_geral);

        recuperar_resumo();

        getSupportActionBar().setTitle("");
        getSupportActionBar().setElevation(0);

        rv_lista= findViewById(R.id.rv_lista);
    }

    @Override
    protected void onStart() {
        super.onStart();
        texto_nome= findViewById(R.id.text_nome_usuario);
        texto_saldo= findViewById(R.id.text_saldo_geral);
        recuperar_resumo();
        fill_lista_movimentacoes(DateUtil.getDataAtualForMov());
        swipe_delete();
    }


    public void swipe_delete(){
        ItemTouchHelper.Callback itemTouch= new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                return makeMovementFlags(ItemTouchHelper.ACTION_STATE_IDLE, ItemTouchHelper.START);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                excluir_movimentacao(viewHolder);
            }
        };

        new ItemTouchHelper(itemTouch).attachToRecyclerView(rv_lista);
    }

    public void excluir_movimentacao(RecyclerView.ViewHolder viewHolder){
        AlertDialog.Builder alertDialog= new AlertDialog.Builder(this);
        alertDialog.setTitle("Excluir movimentação da conta");
        alertDialog.setMessage("Você tem certeza que deseja realmente excluir a movimentação?");
        alertDialog.setCancelable(false);
        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(PrincipalActivity.this, "Cancelado", Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();
            }
        });
        alertDialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int position= viewHolder.getAdapterPosition();
                Movimentacao mov= lista.get(position);
                DatabaseReference reference= FirebaseSettings.getDatabaseReference();
                reference.child("movimentacao").child(id_user).child(mes_ano).child(mov.getId()).removeValue();
                reference.child("usuarios").child(id_user).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            Usuario u= snapshot.getValue(Usuario.class);
                            if(mov.getTipo().equalsIgnoreCase("r")){
                                u.setReceita_total(u.getReceita_total() -
                                        Double.parseDouble(mov.getValor()));
                            }else{
                                u.setDespesa_total(u.getDespesa_total() -
                                        Double.parseDouble(mov.getValor()));
                            }
                            //fazendo isso pois está demorando para atualizar o saldo geral
                            //--------
                            reference.child("usuarios").child(id_user).setValue(u);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                recuperar_resumo();
                adapter.notifyDataSetChanged();
            }
        });

        alertDialog.create().show();
    }

    public void fill_lista_movimentacoes(String mes_ano){
        lista= new ArrayList<>();

        //calling adapter
        adapter= new AdapterListaMovimentacao(lista);

        //setting recycler view
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(this);
        rv_lista.setLayoutManager(layoutManager);
        rv_lista.setHasFixedSize(true);
        rv_lista.setAdapter(adapter);


        String id_user= Base64Mine.encode(FirebaseSettings.getFirebaseAuth().
                getCurrentUser().getEmail());
        DatabaseReference reference= FirebaseSettings.getDatabaseReference();
        reference.child("movimentacao").child(id_user).child(mes_ano).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lista.clear();
                for(DataSnapshot dados: snapshot.getChildren()){
                    Movimentacao mov= dados.getValue(Movimentacao.class);
                    mov.setId(dados.getKey());
                    lista.add(mov);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void abrirDespesas(View view){
        startActivity(new Intent(this, DespesasActivity.class));
    }

    public void abrirReceitas(View view){
        startActivity(new Intent(this, ReceitasActivity.class));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater= getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.acao_sair){
            FirebaseSettings.getFirebaseAuth().signOut();
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void recuperar_resumo(){
        DatabaseReference reference= FirebaseSettings.getDatabaseReference();
        String id_user= Base64Mine.encode(FirebaseSettings.
                getFirebaseAuth().getCurrentUser().getEmail());
        reference.child("usuarios").child(id_user).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Usuario u= snapshot.getValue(Usuario.class);
                    despesa_total= u.getDespesa_total();
                    receita_total= u.getReceita_total();
                    resumo_total= receita_total - despesa_total;
                    String saldo= resumo_total.toString();
                    saldo= saldo.replace(".", ",");
                    saldo = saldo+"0";
                    texto_nome.setText("Olá, "+u.getNome()+"!");
                    texto_saldo.setText("R$"+saldo);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}