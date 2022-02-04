package com.example.whatsappclone.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsappclone.R;
import com.example.whatsappclone.model.Usuario;

import java.util.List;

public class AdapterListaContatos extends RecyclerView.Adapter<AdapterListaContatos.MyViewHolderContatos> {

    private List<Usuario> lista;

    public AdapterListaContatos(List<Usuario> lista){
        this.lista= lista;
    }

    @NonNull
    @Override
    public MyViewHolderContatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item= LayoutInflater.from(parent.getContext()).
                inflate(R.layout.adapter_contatos, parent, false);
        return new MyViewHolderContatos(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderContatos holder, int position) {
        holder.nome_contato.setText(lista.get(position).getNome());
        holder.email_contato.setText(lista.get(position).getEmail());
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class MyViewHolderContatos extends RecyclerView.ViewHolder{

        private TextView nome_contato, email_contato;

        public MyViewHolderContatos(@NonNull View itemView) {
            super(itemView);
            nome_contato= itemView.findViewById(R.id.id_message_send);
            email_contato= itemView.findViewById(R.id.text_email_contato);
        }
    }
}
