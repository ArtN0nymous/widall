package com.ramon.widdall3;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.PointerIcon;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.annotations.Nullable;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Adaptermensaje extends RecyclerView.Adapter<Holdermensaje> {
    private List<MensajeRecibir> listmensaje = new ArrayList<>();
    private Context c;

    public Adaptermensaje( Context c) {
        this.c = c;

    }
    public void addmensaje(MensajeRecibir m){
        listmensaje.add(m);
        notifyItemInserted((listmensaje.size()));

    }

    @Override
    public Holdermensaje onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(c).inflate(R.layout.card_view_mensajes,parent,false);
        return new Holdermensaje(v);
    }


    @Override
    public void onBindViewHolder(@NonNull Holdermensaje holder, int position) {

        holder.getNombre().setText(listmensaje.get(position).getNombre());
        holder.getMensajemensaje().setText(listmensaje.get(position).getMensaje());
        if(listmensaje.get(position).getType_mensaje().equals("2")){
            holder.getMensajefoto().setVisibility(View.VISIBLE);
            holder.getMensajemensaje().setVisibility(View.VISIBLE);
            holder.getReaccion().setVisibility(View.VISIBLE);
            Glide.with(c).load(listmensaje.get(position).getUrlimagen()).into(holder.getMensajefoto());
        }else if(listmensaje.get(position).getType_mensaje().equals("1")){
            holder.getMensajefoto().setVisibility(View.GONE);
            holder.getMensajemensaje().setVisibility(View.VISIBLE);
            holder.getReaccion().setVisibility(View.GONE);
        }
        if(listmensaje.get(position).getFotoperfil().isEmpty()){
            holder.getFotomensaje().setImageResource(R.mipmap.ic_launcher);
        }else{
            Glide.with(c).load(listmensaje.get(position).getFotoperfil()).into(holder.getFotomensaje());
        }

        Long codigohora = listmensaje.get(position).getHora();
        Date d = new Date(codigohora);
        SimpleDateFormat hor = new SimpleDateFormat("hh:mm:ss a");
        holder.getHora().setText(hor.format(d));
    }

    @Override
    public int getItemCount() {
        return listmensaje.size();
    }
}
