package com.ramon.widdall3;

import android.content.Context;
import android.media.Image;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

public class Holdermensaje extends RecyclerView.ViewHolder {

    private TextView nombre;
    private TextView mensajemensaje;
    private CircleImageView fotomensaje;
    private ImageView mensajefoto;
    private ImageView fotoperfil;
    private TextView hora;
    private LinearLayout reaccion;
    public Holdermensaje(@NonNull View itemView) {
        super(itemView);

        nombre= (TextView) itemView.findViewById(R.id.nombre);
        mensajemensaje=(TextView)itemView.findViewById(R.id.mensajemensaje);
        fotomensaje= (CircleImageView)itemView.findViewById(R.id.fotomensaje);
        mensajefoto=(ImageView)itemView.findViewById(R.id.mensajefoto);
        fotoperfil=(ImageView)itemView.findViewById(R.id.fotoperfil);
        hora=(TextView)itemView.findViewById(R.id.hora);
        reaccion= (LinearLayout) itemView.findViewById(R.id.layout_reaccion);

    }

    public TextView getNombre() {
        return nombre;
    }

    public void setNombre(TextView nombre) {
        this.nombre = nombre;
    }

    public TextView getMensajemensaje() {
        return mensajemensaje;
    }

    public void setMensajemensaje(TextView mensajemensaje) {
        this.mensajemensaje = mensajemensaje;
    }

    public CircleImageView getFotomensaje() {
        return fotomensaje;
    }

    public void setFotomensaje(CircleImageView fotomensaje) {
        this.fotomensaje = fotomensaje;
    }

    public ImageView getMensajefoto() {
        return mensajefoto;
    }

    public void setMensajefoto(ImageView mensajefoto) {
        this.mensajefoto = mensajefoto;
    }

    public ImageView getFotoperfil() {
        return fotoperfil;
    }

    public void setFotoperfil(ImageView fotoperfil) {
        this.fotoperfil = fotoperfil;
    }

    public TextView getHora() {
        return hora;
    }

    public void setHora(TextView hora) {
        this.hora = hora;
    }

    public LinearLayout getReaccion() {
        return reaccion;
    }

    public void setReaccion(LinearLayout reaccion) {
        this.reaccion = reaccion;
    }
}
