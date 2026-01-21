package com.tp1.tp1yabc.Modelo.Tanque;

public class JugadorTanque extends Tanque {
    private boolean invulnerable = false;
    private long tiempoFinInvulnerable;
    private Proyectil disparoActivo;


    public JugadorTanque(int x, int y, DireccionApuntado direccion, double velocidadTanque, int vidaTanque, int fuerzaDisparo, TipoTanque tipoTanque, boolean esEnemigo) {
        super(x, y, direccion, velocidadTanque, vidaTanque, fuerzaDisparo, tipoTanque, esEnemigo);
    }

    public boolean puedeDisparar() {
        return disparoActivo == null || !disparoActivo.isActivo();
    }

    public void activarInvulnerabilidad(int duracionSegundos) {
        invulnerable = true;
        tiempoFinInvulnerable = System.currentTimeMillis() + duracionSegundos * 1000L;
    }

    @Override
    public boolean esInvulnerable() {
        if (invulnerable && System.currentTimeMillis() > tiempoFinInvulnerable) {
            invulnerable = false;
        }
        return invulnerable;
    }

    @Override
    public void recibirDanio(Proyectil proyectil) {
        if (esInvulnerable())
            return;
        super.recibirDanio(proyectil);
    }

    public Proyectil dispararTanque() {
        if (!puedeDisparar()) {
            return null;
        }
        Proyectil nuevoProyectil = new Proyectil(getX(), getY(), this, getDireccion(), 0.5, getFuerzaDisparo());
        getProyectiles().add(nuevoProyectil);
        disparoActivo = nuevoProyectil;
        return nuevoProyectil;
    }

    @Override
    public void actualizarEstadoTanque() {

    }

    public void limpiarDisparoActivo(Proyectil proyectil) {
        if (this.disparoActivo == proyectil) {
            this.disparoActivo = null;
        }
    }

}
