package com.tp1.tp1yabc.Modelo.Entorno;

public class TanqueMuerto extends Bloque {

    public TanqueMuerto(int x, int y) {
        super(x, y, false);
    }

    @Override
    public String getTipo() {
        return "tanque_muerto";
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
