package com.tp1.tp1yabc.Modelo.PowerUps;

import com.tp1.tp1yabc.Modelo.Tanque.EnemigoTanque;

import java.util.List;

public class Granada extends PowerUp {
    public Granada(int x, int y) {
        super(x, y);
    }

    @Override
    public String getTipo() {
        return "granada";
    }

    @Override
    public void aplicar(List<EnemigoTanque> enemigos) {
        for (EnemigoTanque enemigo : enemigos) {
            enemigo.setTanqueActivo(false);
        }
        this.setActivo(false);
    }
}
