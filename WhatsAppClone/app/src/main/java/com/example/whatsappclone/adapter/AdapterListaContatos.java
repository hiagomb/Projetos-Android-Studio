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
import com.example.whatsappclone.activity.MainActivity;
import com.example.whatsappclone.fragment.ContatosFragment;
import com.example.whatsappclone.helper.Base64Custom;
import com.example.whatsappclone.helper.ConfigFirebase;
import com.example.whatsappclone.model.Conversa;
import com.example.whatsappclone.model.Usuario;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class AdapterListaContatos extends RecyclerView.Adapter<AdapterListaContatos.MyViewHolderContatos> {

    private List<Usuario> lista;
    private List<Conversa> lista_conversa;

    public AdapterListaContatos(List<Usuario> lista, List<Conversa> lista_conversa){

        this.lista= lista;
        this.lista_conversa= lista_conversa;
    }

    public List<Conversa> getListaConversas(){
        return this.lista_conversa;
    }

    public List<Usuario> getListaContatos(){
        return this.lista;
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
       /*vou recuperar dessa forma pois usuarios, contatos e conversas são nós diferentes
       e eu só tenho a foto no nó usuários*/
        String id_64;

        if(lista!= null){
            holder.nome_contato.setText(lista.get(position).getNome());
            holder.email_contato.setText(lista.get(position).getEmail());
            id_64= Base64Custom.encode64(lista.get(position).getEmail());

            if(lista.get(position).getEmail().isEmpty()){
                holder.email_contato.setVisibility(LinearLayout.GONE);
                holder.img_perfil.setImageResource(R.drawable.icone_grupo);
            }
            if(lista.get(position).getPhoto()!= null){
                Uri photo= Uri.parse(lista.get(position).getPhoto());
                Glide.with(holder.img_perfil.getContext()).load(photo).into(holder.img_perfil);
            }

        }else{
            if(lista_conversa.get(position).getIsGroup().equalsIgnoreCase("true")){
                holder.nome_contato.setText(lista_conversa.get(position).getGrupo().getNome());
                String uri= lista_conversa.get(position).getGrupo().getFoto();
                if(uri!= null && !uri.isEmpty()){
                    Uri uri_photo= Uri.parse(uri);
                    Glide.with(holder.img_perfil.getContext()).load(uri_photo).into(holder.img_perfil);
                }
            }else{
                holder.nome_contato.setText(lista_conversa.get(position).getNome());
            }

            holder.email_contato.setText(lista_conversa.get(position).getMensagem());
            id_64= lista_conversa.get(position).getId_usuario();
            DatabaseReference reference= ConfigFirebase.getFirebase().child("usuarios").child(id_64);
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        Usuario u= snapshot.getValue(Usuario.class);
                        if(u.getPhoto()!= null && !u.getPhoto().isEmpty()){
                            Uri uri_imagem= Uri.parse(u.getPhoto());
                            Glide.with(holder.img_perfil.getContext()).load(uri_imagem).into(holder.img_perfil);
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }

    @Override
    public int getItemCount() {
        int amount= 0;
        if(lista!=null){
            amount= lista.size();
        }else{
            amount = lista_conversa.size();
        }
        return amount;
    }

    public class MyViewHolderContatos extends RecyclerView.ViewHolder{

        private TextView nome_contato, email_contato;
        private ImageView img_perfil;

        public MyViewHolderContatos(@NonNull View itemView) {
            super(itemView);
            nome_contato= itemView.findViewById(R.id.id_message_send);
            email_contato= itemView.findViewById(R.id.text_email_contato);
            img_perfil= itemView.findViewById(R.id.contact_img);
        }
    }
}
