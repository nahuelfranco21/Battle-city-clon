package com.tp1.tp1yabc.Modelo.Tanque;

import com.tp1.tp1yabc.Utilidades.ValorTamaniosJuego;

import java.util.ArrayList;
import java.util.List;

public abstract class Tanque {

    private List<Proyectil> proyectiles;

    private double posicionX;
    private double posicionY;
    private DireccionApuntado direccion;
    private double velocidadTanque;
    private int vidaTanque;
    private int fuerzaDisparo;
    private boolean bloqueMuerto = false;
    private boolean esEnemigo;

    private TipoTanque tipoDeTanque;
    private boolean tanqueActivo = true;
    private boolean estaMoviendo = false;

    public abstract void actualizarEstadoTanque();


    public void setTanqueActivo(boolean tanqueActivo) {
        this.tanqueActivo = tanqueActivo;
    }

    public void setFuerzaDisparo(int fuerzaDisparo) {
        this.fuerzaDisparo = fuerzaDisparo;
    }

    public void setDireccion(DireccionApuntado direccion) {
        this.direccion = direccion;
    }

    public void setMoviendo(boolean moviendo) {
        this.estaMoviendo = moviendo;
    }

    public boolean isMoviendo() {
        return estaMoviendo;
    }

    public double getX() {
        return posicionX;
    }

    public double getY() {
        return posicionY;
    }

    public DireccionApuntado getDireccion() {
        return direccion;
    }

    public int getVidaTanque() {
        return vidaTanque;
    }

    public double getVelocidadTanque() {
        return velocidadTanque;
    }

    public int getFuerzaDisparo() {
        return fuerzaDisparo;
    }

    public TipoTanque getTipoDeTanque() {
        return tipoDeTanque;
    }

    public List<Proyectil> getProyectiles() {
        return proyectiles;
    }

    public boolean isTanqueActivo() {
        return tanqueActivo;
    }

    public boolean esInvulnerable() {
        return false;
    }

    public Tanque(int x, int y, DireccionApuntado direccion, double velocidadTanque, int vidaTanque, int fuerzaDisparo, TipoTanque tipoTanque, boolean esEnemigo) {
        this.posicionX = x;
        this.posicionY = y;
        this.direccion = direccion;
        this.velocidadTanque = velocidadTanque;
        this.vidaTanque = vidaTanque;
        this.fuerzaDisparo = fuerzaDisparo;
        this.tipoDeTanque = tipoTanque;
        this.proyectiles = new ArrayList<>();
        this.esEnemigo = esEnemigo;
    }

    public void moverEnDireccion(DireccionApuntado nuevaDireccion) {
        this.direccion = nuevaDireccion;
        this.posicionX = nuevaDireccion.moverHorizontal(this.posicionX, velocidadTanque);
        this.posicionY = nuevaDireccion.moverVertical(this.posicionY, velocidadTanque);
    }

    public Proyectil dispararTanque() {
        int velocidadProyectil = 1;
        Proyectil nuevo = new Proyectil(getX(), getY(), this, getDireccion(), velocidadProyectil, getFuerzaDisparo());
        proyectiles.add(nuevo);
        return nuevo;
    }

    public boolean colisionaTanqueConProyectil(Proyectil proyectil) {
        double posicionXProyectil = proyectil.getX();
        double posicionYProyectil = proyectil.getY();
        int tamanioProyectil = ValorTamaniosJuego.TAMANIO_PROYECTIL;

        return posicionXProyectil < this.posicionX + ValorTamaniosJuego.TAMANIO_TANQUE &&
                posicionXProyectil + tamanioProyectil > this.posicionX &&
                posicionYProyectil < this.posicionY + ValorTamaniosJuego.TAMANIO_TANQUE &&
                posicionYProyectil + tamanioProyectil > this.posicionY;
    }

    public void recibirDanio(Proyectil proyectil) {
        if (colisionaTanqueConProyectil(proyectil)) {
            this.vidaTanque -= proyectil.getDanioProyectil();
            if (this.vidaTanque <= 0) {
                this.vidaTanque = 0;
                tanqueActivo = false;
            }
        }
    }

    public void limpiarDisparoActivo(Proyectil proyectil) {
    }

    public boolean esEnemigo() {
        return esEnemigo;
    }

    public boolean isBloqueMuertoCreado() {
        return bloqueMuerto;
    }

    public void setBloqueMuertoCreado(boolean bloqueMuertoCreado) {
        this.bloqueMuerto = bloqueMuertoCreado;
    }

}
