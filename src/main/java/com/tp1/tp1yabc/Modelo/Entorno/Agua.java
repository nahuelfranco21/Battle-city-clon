package com.tp1.tp1yabc.Modelo.Entorno;

public class Agua extends Bloque {
    public Agua(int x, int y) {
        super(x, y, false);
    }

    @Override
    public String getTipo() {
        return "agua";
    }

    @Override
    public boolean bloqueaDisparo() {
        return false;
    }

    @Override
    public boolean impideElPaso() {
        return true;
    }

}
