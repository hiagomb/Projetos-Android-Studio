package com.example.cardview.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cardview.R;
import com.example.cardview.model.Postagem;

import java.util.List;

public class AdapterPostagem extends RecyclerView.Adapter<AdapterPostagem.MyViewHolder>{


    private List<Postagem> lista_posts;

    public AdapterPostagem(List<Postagem> lista_posts) {
        this.lista_posts = lista_posts;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item_Card= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_postagem, parent, false);
        return new MyViewHolder(item_Card);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Postagem post= lista_posts.get(position);
        holder.txt_name.setText(post.getAuthor());
        holder.txt_horario.setText(post.getTime());
        holder.txt_post.setText(post.getPost_text());
        holder.img.setImageResource(post.getImg());
    }

    @Override
    public int getItemCount() {
        return lista_posts.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView txt_name, txt_post, txt_horario;
        private ImageView img;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_name= itemView.findViewById(R.id.txt_author);
            txt_post= itemView.findViewById(R.id.txt_post);
            txt_horario= itemView.findViewById(R.id.txt_time);
            img= itemView.findViewById(R.id.imageView2);
        }
    }
}
