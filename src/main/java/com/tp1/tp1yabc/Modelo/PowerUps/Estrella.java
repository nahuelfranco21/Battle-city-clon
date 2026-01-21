package com.tp1.tp1yabc.Modelo.PowerUps;

import com.tp1.tp1yabc.Modelo.Tanque.JugadorTanque;

public class Estrella extends PowerUp {
    public static final int FUERZA_DISPARO_ESTRELLA = 100;

    public Estrella(int x, int y) {
        super(x, y);
    }

    @Override
    public String getTipo() {
        return "estrella";
    }

    @Override
    public void aplicar(JugadorTanque jugador) {
        jugador.setFuerzaDisparo(FUERZA_DISPARO_ESTRELLA);
        this.setActivo(false);
    }
}
