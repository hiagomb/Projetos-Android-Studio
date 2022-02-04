package com.example.whatsappclone.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsappclone.R;
import com.example.whatsappclone.model.Mensagem;
import com.example.whatsappclone.model.Usuario;

import java.util.List;

public class AdapterListaConversaUnica extends RecyclerView.Adapter<AdapterListaConversaUnica.MyViewHolderContatos> {

    private List<Mensagem> lista;

    public AdapterListaConversaUnica(List<Mensagem> lista){
        this.lista= lista;
    }

    @NonNull
    @Override
    public MyViewHolderContatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item= LayoutInflater.from(parent.getContext()).
                inflate(R.layout.adapter_conversa_unica, parent, false);
        return new MyViewHolderContatos(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderContatos holder, int position) {
        holder.message.setText(lista.get(position).getMessage());
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class MyViewHolderContatos extends RecyclerView.ViewHolder{

        private TextView message;

        public MyViewHolderContatos(@NonNull View itemView) {
            super(itemView);
            message= itemView.findViewById(R.id.id_message_send);
        }
    }
}

