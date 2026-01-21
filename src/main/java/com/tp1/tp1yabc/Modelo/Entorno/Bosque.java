package com.tp1.tp1yabc.Modelo.Entorno;

public class Bosque extends Bloque {

    public Bosque(int x, int y) {
        super(x, y, false);
    }

    @Override
    public String getTipo() {
        return "bosque";
    }

    @Override
    public boolean bloqueaDisparo() {
        return false;
    }

    @Override
    public boolean impideElPaso() {
        return false;
    }

}
