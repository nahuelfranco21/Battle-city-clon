package com.tp1.tp1yabc.Modelo.Entorno;

public abstract class Bloque {
    private int x, y;
    private boolean destructible;
    private boolean destruido;

    public Bloque(int x, int y, boolean destructible) {
        this.x = x;
        this.y = y;
        this.destructible = destructible;
        this.destruido = false;
    }

    public boolean esDestructible() {
        return destructible;
    }

    public boolean estaDestruido() {
        return destruido;
    }

    public void setDestruido(boolean destruido) {
        this.destruido = destruido;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void recibirImpacto() {

    }

    public abstract String getTipo();

    public abstract boolean bloqueaDisparo();

    public abstract boolean impideElPaso();
}
