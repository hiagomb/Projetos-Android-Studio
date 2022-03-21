package com.example.ifoodclone.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ifoodclone.R;
import com.example.ifoodclone.model.Produto;

import java.util.List;

public class AdapterProdutos extends RecyclerView.Adapter<AdapterProdutos.MyViewHolderProdutos> {

    private List<Produto> lista;

    public AdapterProdutos(List<Produto> lista) {
        this.lista = lista;
    }

    @NonNull
    @Override
    public MyViewHolderProdutos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.adapter_produtos, parent, false);
        return new MyViewHolderProdutos(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderProdutos holder, int position) {
        Produto produto= lista.get(position);

        holder.txt_nome.setText(produto.getNome());
        holder.txt_desc.setText(produto.getDescricao());
        holder.txt_valor.setText(produto.getPreco());
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    class MyViewHolderProdutos extends RecyclerView.ViewHolder{

        private TextView txt_nome, txt_desc, txt_valor;

        public MyViewHolderProdutos(@NonNull View itemView) {
            super(itemView);
            txt_nome= itemView.findViewById(R.id.txt_nome_produto);
            txt_desc= itemView.findViewById(R.id.txt_descricao_produto);
            txt_valor= itemView.findViewById(R.id.txt_valor_produto);
        }
    }
}
