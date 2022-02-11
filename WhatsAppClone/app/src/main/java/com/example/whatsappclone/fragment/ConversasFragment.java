package com.example.whatsappclone.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.example.whatsappclone.R;
import com.example.whatsappclone.activity.ConversaActivity;
import com.example.whatsappclone.adapter.AdapterListaContatos;
import com.example.whatsappclone.helper.Base64Custom;
import com.example.whatsappclone.helper.ConfigFirebase;
import com.example.whatsappclone.helper.RecyclerItemClickListener;
import com.example.whatsappclone.model.Conversa;
import com.example.whatsappclone.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConversasFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConversasFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ConversasFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ConversasFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ConversasFragment newInstance(String param1, String param2) {
        ConversasFragment fragment = new ConversasFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private RecyclerView recyclerView;
    private List<Conversa> lista;
    private AdapterListaContatos adapterListaContatos;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_conversas, container, false);

        recyclerView= view.findViewById(R.id.recycler_conversas);
        fill_lista_conversas(view);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(view.getContext(),
                        recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if(lista.get(position).getIsGroup().equalsIgnoreCase("true")){
                            Intent intent= new Intent(getActivity(), ConversaActivity.class);
                            intent.putExtra("nomeContato", lista.get(position).getGrupo().getNome());
                            intent.putExtra("emailContato", lista.get(position).getId_usuario());
                            intent.putExtra("fotoContato", lista.get(position).getGrupo().getFoto());
                            intent.putExtra("grupo", lista.get(position).getGrupo());
                            intent.putExtra("is_group", "true");
                            startActivity(intent);
                        }else{
                            DatabaseReference reference= ConfigFirebase.getFirebase().
                                    child("usuarios").child(lista.get(position).getId_usuario());
                            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists()){
                                        Usuario u= snapshot.getValue(Usuario.class);
                                        Intent intent= new Intent(getActivity(), ConversaActivity.class);
                                        intent.putExtra("nomeContato", lista.get(position).getNome());
                                        intent.putExtra("emailContato",
                                                Base64Custom.decode64(lista.get(position).getId_usuario()));
                                        intent.putExtra("fotoContato", u.getPhoto());
                                        intent.putExtra("is_group", "false");

                                        startActivity(intent);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }

                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    }
                })
        );

        return view;
    }

    public View getView(){
        return this.view;
    }

    public void fill_lista_conversas(View view){
        //vou usar o mesmo adapter do contatos
        lista= new ArrayList<>();

        //calling adapter
        adapterListaContatos= new AdapterListaContatos(null, lista);

        //setting recycler view
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapterListaContatos);

        String email64 = Base64Custom.encode64(ConfigFirebase.getFirebaseAuth().getCurrentUser().getEmail());

        DatabaseReference reference= ConfigFirebase.getFirebase().child("conversas").child(email64);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lista.clear();
                for(DataSnapshot dados: snapshot.getChildren()){
                    lista.add(dados.getValue(Conversa.class));
                    System.out.println("teste: "+dados.getKey());
                }
                adapterListaContatos.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void pesquisar_conversas(String texto){
        List<Conversa> lista_conversas_busca= new ArrayList<>();
        for(Conversa conversa: lista){
            String nome= conversa.getNome().toLowerCase();
            System.out.println("nome: "+nome);
            System.out.println("texto: "+texto);
            if(nome.contains(texto)){
                lista_conversas_busca.add(conversa);
                System.out.println("teste de poesquisa");
            }
        }

        adapterListaContatos= new AdapterListaContatos(null, lista_conversas_busca);
        recyclerView.setAdapter(adapterListaContatos);
        adapterListaContatos.notifyDataSetChanged();
    }

    public void recarregar_conversas(){
        adapterListaContatos= new AdapterListaContatos(null, lista);
        recyclerView.setAdapter(adapterListaContatos);
        adapterListaContatos.notifyDataSetChanged();
    }
}