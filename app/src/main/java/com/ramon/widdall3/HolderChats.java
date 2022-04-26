package com.ramon.widdall3;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

public class HolderChats extends RecyclerView.ViewHolder{
    private TextView nombre;
    private CircleImageView perfil_del_chat;
    private TextView mensaje_del_chat;
    CardView tarjetaChats;
    Context context;
    public HolderChats(@NonNull View itemView) {
        super(itemView);
        nombre=(TextView)itemView.findViewById(R.id.nombre_del_chat);
        perfil_del_chat =(CircleImageView)itemView.findViewById(R.id.fotoperfil_del_chat);
        mensaje_del_chat=(TextView)itemView.findViewById(R.id.mensaje_del_chat);
        tarjetaChats=(CardView)itemView.findViewById(R.id.tarjetaChat);
        context = itemView.getContext();
    }

    public TextView getNombre() {
        return nombre;
    }

    public void setNombre(TextView nombre) {
        this.nombre = nombre;
    }

    public CircleImageView getPerfil_del_chat() {
        return perfil_del_chat;
    }

    public void setPerfil_del_chat(CircleImageView perfil_del_chat) {
        this.perfil_del_chat = perfil_del_chat;
    }

    public TextView getMensaje_del_chat() {
        return mensaje_del_chat;
    }

    public void setMensaje_del_chat(TextView mensaje_del_chat) {
        this.mensaje_del_chat = mensaje_del_chat;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
