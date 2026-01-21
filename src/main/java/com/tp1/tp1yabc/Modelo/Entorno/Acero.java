package com.tp1.tp1yabc.Modelo.Entorno;

public class Acero extends Bloque {

    public Acero(int x, int y) {
        super(x, y, false);
    }

    @Override
    public String getTipo() {
        return "acero";
    }

    @Override
    public boolean bloqueaDisparo() {
        return true;
    }

    @Override
    public boolean impideElPaso() {
        return true;
    }

}
