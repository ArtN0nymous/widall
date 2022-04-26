package com.ramon.widdall3.Entidades;

public class Chat {
    private String nombre;
    private String urlperfil;
    private String mensaje;
    private String codigo_del_chat;

    public String getCodigo_del_chat() {
        return codigo_del_chat;
    }

    public void setCodigo_del_chat(String codigo_del_chat) {
        this.codigo_del_chat = codigo_del_chat;
    }

    public Chat() {
    }

    public Chat(String nombre, String urlperfil, String mensaje, String codigo_del_caht) {
        this.nombre = nombre;
        this.urlperfil = urlperfil;
        this.mensaje = mensaje;
        this.codigo_del_chat = codigo_del_chat;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUrlperfil() {
        return urlperfil;
    }

    public void setUrlperfil(String urlperfil) {
        this.urlperfil = urlperfil;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }


}
