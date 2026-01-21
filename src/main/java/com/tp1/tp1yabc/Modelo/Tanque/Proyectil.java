package com.tp1.tp1yabc.Modelo.Tanque;

import com.tp1.tp1yabc.Utilidades.ValorTamaniosJuego;

public class Proyectil {

    private double x;
    private double y;
    private Tanque tanqueOrigen;
    private DireccionApuntado direccion;
    private double velocidadProyectil;
    private int danioProyectil;

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getDanioProyectil() {
        return danioProyectil;
    }

    public Proyectil(double x, double y, Tanque tanqueOrigen, DireccionApuntado direccion, double velocidadProyectil, int danioProyectil) {
        this.x = x;
        this.y = y;
        this.tanqueOrigen = tanqueOrigen;
        this.direccion = direccion;
        this.velocidadProyectil = velocidadProyectil;
        this.danioProyectil = danioProyectil;
    }

    public void mover() {
        x = direccion.moverHorizontal(this.x, velocidadProyectil);
        y = direccion.moverVertical(this.y, velocidadProyectil);
    }

    public Tanque getTanqueOrigen() {
        return tanqueOrigen;
    }

    public boolean isActivo() {
        return x >= 0 && x <= ValorTamaniosJuego.ANCHO_ENTORNO && y >= 0 && y <= ValorTamaniosJuego.ALTO_ENTORNO;
    }

    public void desactivar() {
        tanqueOrigen.limpiarDisparoActivo(this);
    }
}
