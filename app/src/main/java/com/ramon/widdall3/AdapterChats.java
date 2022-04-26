package com.ramon.widdall3;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ramon.widdall3.Entidades.Chat;

import java.util.ArrayList;
import java.util.List;

public class AdapterChats extends RecyclerView.Adapter<HolderChats>{
    private List<Chat> listchat = new ArrayList<>();
    private Context c;
    CardView cardView;
    private String code;
    int posicionmarcada = 0;

    public AdapterChats(Context c) {
        this.c = c;
    }
    public void addChat(Chat ch){
        listchat.add(ch);
        notifyItemInserted(listchat.size());
    }

    @NonNull
    @Override
    public HolderChats onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.card_view_chat,parent,false);
        cardView = v.findViewById(R.id.tarjetaChat);
        //Escuha el evento de selccion
        return new HolderChats(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final HolderChats holder, final int position) {
        holder.getNombre().setText(listchat.get(position).getNombre());
        holder.getMensaje_del_chat().setText(listchat.get(position).getMensaje());
        if(listchat.get(position).getUrlperfil().isEmpty()){
            holder.getPerfil_del_chat().setImageResource(R.mipmap.ic_launcher);
        } else {
            Glide.with(c).load(listchat.get(position).getUrlperfil()).into(holder.getPerfil_del_chat());
        }
        holder.tarjetaChats.setOnClickListener(new View.OnClickListener() {
            int post = position;
            @Override
            public void onClick(View v) {
                posicionmarcada = post;
                //Toma la posicion del clic y la tarjeta lo compara en la lista y toma el codigo del chat, lo manda al activity principal
                code = listchat.get(posicionmarcada).getCodigo_del_chat();
                Toast.makeText(c, code, Toast.LENGTH_SHORT).show();
                Intent intent= new Intent(holder.getContext(),MainActivity.class);
                intent.putExtra("codigo",code);
                //Toma el contexto de los objetos llamados en el holder
                holder.getContext().startActivity(intent);
            }
        });


    }
    @Override
    public int getItemCount() {
        return listchat.size();
    }

}
