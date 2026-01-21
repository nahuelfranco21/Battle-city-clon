package com.tp1.tp1yabc.Modelo.Tanque;

public enum DireccionApuntado {
    NORTE(0, -1), SUR(0, 1), ESTE(1, 0), OESTE(-1, 0);

    private final int direccionX;
    private final int direccionY;

    DireccionApuntado(int direccionX, int direccionY) {
        this.direccionX = direccionX;
        this.direccionY = direccionY;
    }

    public double moverHorizontal(double x, double velocidad) {
        return x + direccionX * velocidad;
    }

    public double moverVertical(double y, double velocidad) {
        return y + direccionY * velocidad;
    }
}

