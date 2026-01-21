package com.tp1.tp1yabc.Modelo.PowerUps;

import com.tp1.tp1yabc.Modelo.Tanque.EnemigoTanque;
import com.tp1.tp1yabc.Modelo.Tanque.JugadorTanque;

import java.util.List;

public abstract class PowerUp {
    private int x, y;
    private boolean activo;

    public PowerUp(int x, int y) {
        this.x = x;
        this.y = y;
        this.activo = true;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean estaActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public abstract String getTipo();

    public void aplicar(JugadorTanque jugador) {
    }

    public void aplicar(List<EnemigoTanque> enemigos) {
    }

}
