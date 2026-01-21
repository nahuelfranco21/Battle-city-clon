package com.tp1.tp1yabc.Modelo.Estado;

import com.tp1.tp1yabc.Modelo.Entorno.Entorno;
import com.tp1.tp1yabc.Modelo.Juego;

public class EstadoJugando implements Estado {
    private Entorno entorno;

    public EstadoJugando(Entorno entorno) {
        this.entorno = entorno;
    }

    @Override
    public void actualizarEstadoJuego(Juego juego) {
        entorno.actualizar();
    }

    @Override
    public String getNombreEstado() {
        return "Jugando";
    }

}