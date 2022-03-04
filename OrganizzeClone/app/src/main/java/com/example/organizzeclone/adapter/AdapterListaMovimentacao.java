package com.example.organizzeclone.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.organizzeclone.R;
import com.example.organizzeclone.model.Movimentacao;

import java.util.List;

public class AdapterListaMovimentacao extends RecyclerView.Adapter<AdapterListaMovimentacao.MyViewHolderMovimentacao> {

    private List<Movimentacao> lista;

    public AdapterListaMovimentacao(List<Movimentacao> lista) {
        this.lista = lista;
    }

    @NonNull
    @Override
    public MyViewHolderMovimentacao onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item= LayoutInflater.from(parent.getContext()).
                inflate(R.layout.adapter_lista_movimentacao, parent, false);
        return new MyViewHolderMovimentacao(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderMovimentacao holder, int position) {
        holder.txt_categoria.setText(lista.get(position).getCategoria());
        holder.txt_descricao.setText(lista.get(position).getDescricao());
        holder.txt_valor.setText("R$"+lista.get(position).getValor());
        if(lista.get(position).getTipo().equalsIgnoreCase("r")){
            holder.txt_valor.setTextColor(Color.GREEN);
        }else {
            holder.txt_valor.setTextColor(Color.RED);
        }
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }


    public class MyViewHolderMovimentacao extends RecyclerView.ViewHolder{

        private TextView txt_descricao, txt_valor, txt_categoria;

        public MyViewHolderMovimentacao(@NonNull View itemView) {
            super(itemView);
            txt_categoria= itemView.findViewById(R.id.txt_categ_mov);
            txt_descricao= itemView.findViewById(R.id.txt_desc_mov);
            txt_valor= itemView.findViewById(R.id.txt_valor_mov);
        }
    }
}
