package com.ramon.widdall3;

import java.util.Map;

public class MensajeEnviar extends Mensaje {
    private Map hora;

    public MensajeEnviar(Map hora) {
        this.hora = hora;
    }

    public MensajeEnviar() {
    }

    public MensajeEnviar(String mensaje, String nombre, String fotoperfil, String type_mensaje, String urlimagen, Map hora) {
        super(mensaje, nombre, fotoperfil, type_mensaje, urlimagen);
        this.hora = hora;
    }

    public MensajeEnviar(String toString, String nombre_log, String urlimagen, String s) {
    }


    public Map getHora() {
        return hora;
    }

    public void setHora(Map hora) {
        this.hora = hora;
    }

    public MensajeEnviar(String mensaje, String nombre, String fotoperfil, String type_mensaje, Map hora) {
        super(mensaje, nombre, fotoperfil, type_mensaje);
        this.hora = hora;
    }
}
