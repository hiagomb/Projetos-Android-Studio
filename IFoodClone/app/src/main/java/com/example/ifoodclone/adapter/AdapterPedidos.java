package com.example.ifoodclone.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ifoodclone.R;
import com.example.ifoodclone.helper.Base64Custom;
import com.example.ifoodclone.model.Pedido;

import java.util.List;

public class AdapterPedidos extends RecyclerView.Adapter<AdapterPedidos.ViewHolderPedidos> {

    private List<Pedido> lista;

    public AdapterPedidos(List<Pedido> lista) {
        this.lista = lista;
    }

    @NonNull
    @Override
    public ViewHolderPedidos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.adapter_pedidos, parent, false);
        return new AdapterPedidos.ViewHolderPedidos(item);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderPedidos holder, int position) {
        Pedido pedido= lista.get(position);

        holder.txt_nome.setText(Base64Custom.decode64(pedido.getId_cliente()));
        if(pedido.getEndereco()!= null){
            String endereco= pedido.getEndereco().getRua()+", "+pedido.getEndereco().getNumero()+
                    ", "+pedido.getEndereco().getBairro()+", "+pedido.getEndereco().getCidade();
            holder.txt_endereco.setText(endereco);
        }else{
            holder.txt_endereco.setText("Sem endereço cadastrado");
        }

        holder.txt_pagamento.setText(pedido.getMetodo_pagamento());
        if(pedido.getObs().equalsIgnoreCase("")){
            holder.txt_obs.setText("Obs: -");
        }else{
            holder.txt_obs.setText(pedido.getObs());
        }
        //itens
        String itens= "";
        for(int i=0; i<pedido.getItens().size(); i++){
            itens+= (i+1)+") "+pedido.getItens().get(i).getNome_produto()+ " ("+
                    pedido.getItens().get(i).getQuantidade()+" X R$"+pedido.getItens().get(i).getPreco()+
                    ")\n";
        }
        holder.txt_itens.setText(itens);
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    class ViewHolderPedidos extends RecyclerView.ViewHolder{

        private TextView txt_nome, txt_endereco, txt_pagamento, txt_obs, txt_itens;

        public ViewHolderPedidos(@NonNull View itemView) {
            super(itemView);
            txt_nome= itemView.findViewById(R.id.txt_nome_cliente);
            txt_endereco= itemView.findViewById(R.id.txt_endereco_cliente);
            txt_pagamento= itemView.findViewById(R.id.txt_pagamento_cliente);
            txt_obs= itemView.findViewById(R.id.txt_obs_cliente);
            txt_itens= itemView.findViewById(R.id.txt_itens_cliente);
        }
    }
}
