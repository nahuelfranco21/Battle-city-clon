package com.tp1.tp1yabc.Modelo.PowerUps;

import com.tp1.tp1yabc.Modelo.Tanque.JugadorTanque;

public class Casco extends PowerUp {

    public static final int DURACION = 10;

    public Casco(int x, int y) {
        super(x, y);
    }

    @Override
    public String getTipo() {
        return "casco";
    }

    @Override
    public void aplicar(JugadorTanque jugador) {
        jugador.activarInvulnerabilidad(this.DURACION);
        this.setActivo(false);
    }
}
