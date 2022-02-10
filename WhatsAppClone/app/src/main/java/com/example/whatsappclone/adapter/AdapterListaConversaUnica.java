package com.example.whatsappclone.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.whatsappclone.R;
import com.example.whatsappclone.activity.ConversaActivity;
import com.example.whatsappclone.helper.Base64Custom;
import com.example.whatsappclone.helper.ConfigFirebase;
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
        String id_logado= Base64Custom.encode64(ConfigFirebase.getFirebaseAuth().getCurrentUser().getEmail());
        if(id_logado.equalsIgnoreCase(lista.get(position).getId_rem())){
            holder.layout_right.setVisibility(LinearLayout.VISIBLE);
            holder.message_send.setText(lista.get(position).getMessage());
            holder.layout_left.setVisibility(LinearLayout.GONE);
            //setting the foto
            if(lista.get(position).getMessage().equalsIgnoreCase("imagem.jpeg")){
                Uri uri= Uri.parse(lista.get(position).getFoto());
                Glide.with(holder.img_remetente.getContext()).load(uri).into(holder.img_remetente);
                holder.message_send.setVisibility(LinearLayout.GONE);
            }else{
                holder.right_foto.setVisibility(LinearLayout.GONE);
            }
        }else{
            holder.layout_left.setVisibility(LinearLayout.VISIBLE);
            holder.message_received.setText(lista.get(position).getMessage());
            holder.layout_right.setVisibility(LinearLayout.GONE);
            //setting the foto
            if(lista.get(position).getMessage().equalsIgnoreCase("imagem.jpeg")){
                Uri uri= Uri.parse(lista.get(position).getFoto());
                Glide.with(holder.img_dest.getContext()).load(uri).into(holder.img_dest);
                holder.message_received.setVisibility(LinearLayout.GONE);
            }else{
                holder.left_foto.setVisibility(LinearLayout.GONE);
            }
        }

    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class MyViewHolderContatos extends RecyclerView.ViewHolder{

        private TextView message_send, message_received;
        private LinearLayout layout_left, layout_right, left_foto, right_foto;
        private ImageView img_remetente, img_dest;

        public MyViewHolderContatos(@NonNull View itemView) {
            super(itemView);
            layout_left= itemView.findViewById(R.id.chat_left_msg_layout);
            layout_right= itemView.findViewById(R.id.chat_right_msg_layout);
            left_foto= itemView.findViewById(R.id.chat_left_foto);
            right_foto= itemView.findViewById(R.id.chat_right_foto);
            message_send= itemView.findViewById(R.id.id_message_send);
            message_received= itemView.findViewById(R.id.id_message_received);
            img_remetente= itemView.findViewById(R.id.img_foto_remetente);
            img_dest= itemView.findViewById(R.id.img_foto_destinatario);
        }
    }
}

