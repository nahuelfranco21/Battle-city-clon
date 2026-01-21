package com.tp1.tp1yabc.Modelo.Tanque;

public enum TipoTanque {
    BASICO(0.1, 1, 1, 4),
    RAPIDO(0.5, 1, 1, 2),
    POTENTE(0.1, 2, 2, 1),
    BLINDADO(0.1, 3, 1, 2);

    private final double velocidad;
    private final int vida;
    private final int fuerzaDisparo;
    private double velocidadDisparo;

    TipoTanque(double velocidad, int vida, int fuerzaDisparo, double velocidadDisparo) {
        this.velocidad = velocidad;
        this.vida = vida;
        this.fuerzaDisparo = fuerzaDisparo;
        this.velocidadDisparo = velocidadDisparo;
    }

    public double getVelocidad() {
        return velocidad;
    }

    public int getVida() {
        return vida;
    }

    public int getFuerzaDisparo() {
        return fuerzaDisparo;
    }

    public double getVelocidadDisparo() {
        return velocidadDisparo;
    }
}
