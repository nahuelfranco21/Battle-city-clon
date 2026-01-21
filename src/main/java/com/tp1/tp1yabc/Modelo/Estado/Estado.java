package com.tp1.tp1yabc.Modelo.Estado;

import com.tp1.tp1yabc.Modelo.Juego;

public interface Estado {
    void actualizarEstadoJuego(Juego juego);

    String getNombreEstado();
}