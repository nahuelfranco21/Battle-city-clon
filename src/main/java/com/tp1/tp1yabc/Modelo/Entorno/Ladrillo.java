package com.tp1.tp1yabc.Modelo.Entorno;

public class Ladrillo extends Bloque {
    private int resistencia;

    public Ladrillo(int x, int y) {
        super(x, y, true);
        this.resistencia = 3;
    }

    @Override
    public String getTipo() {
        return "ladrillo";
    }

    @Override
    public void recibirImpacto() {
        resistencia--;
        if (resistencia <= 0) {
            setDestruido(true);
        }
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
