package com.ramon.widdall3.Entidades;

import android.media.Image;

public class Usuario {
    private String nombre;
    private String correo;
    private String userperfil;

    public Usuario() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getUserperfil() {
        return userperfil;
    }

    public void setUserperfil(String userperfil) {
        this.userperfil = userperfil;
    }
}
