package com.tp1.tp1yabc.Modelo.Entorno;

public class Base extends Bloque {
    public Base(int x, int y) {
        super(x, y, true);
    }

    @Override
    public String getTipo() {
        return "base";
    }

    @Override
    public void recibirImpacto() {
        setDestruido(true);
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
