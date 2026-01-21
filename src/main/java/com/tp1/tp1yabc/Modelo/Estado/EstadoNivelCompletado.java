package com.tp1.tp1yabc.Modelo.Estado;

import com.tp1.tp1yabc.Modelo.Juego;

public class EstadoNivelCompletado implements Estado {
    @Override
    public void actualizarEstadoJuego(Juego juego) {
    }

    @Override
    public String getNombreEstado() {
        return "NivelCompletado";
    }
}
