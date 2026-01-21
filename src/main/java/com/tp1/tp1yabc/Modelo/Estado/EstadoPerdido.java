package com.tp1.tp1yabc.Modelo.Estado;

public class EstadoPerdido implements Estado {
    @Override
    public void actualizarEstadoJuego(com.tp1.tp1yabc.Modelo.Juego juego) {
    }

    public String getNombreEstado() {
        return "Perdido";
    }
}
