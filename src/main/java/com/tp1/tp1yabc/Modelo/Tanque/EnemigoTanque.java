package com.tp1.tp1yabc.Modelo.Tanque;

public class EnemigoTanque extends Tanque {

    private final TipoTanque tipoTanque;
    private final IATanque iaTanque;
    private long tiempoDesdeUltimoDisparo;

    public EnemigoTanque(int x, int y, DireccionApuntado direccion, TipoTanque tipoTanque, boolean esEnemigo) {
        super(x, y, direccion, tipoTanque.getVelocidad(), tipoTanque.getVida(), tipoTanque.getFuerzaDisparo(), tipoTanque, esEnemigo);
        this.tipoTanque = tipoTanque;
        this.iaTanque = new IATanque();
        this.tiempoDesdeUltimoDisparo = System.currentTimeMillis();
    }

    @Override
    public void actualizarEstadoTanque() {
        setDireccion(iaTanque.decidirDireccion(false));

        long tiempoActual = System.currentTimeMillis();
        if (tiempoActual - tiempoDesdeUltimoDisparo >= tipoTanque.getVelocidadDisparo() * 1000) {
            dispararTanque();
            tiempoDesdeUltimoDisparo = tiempoActual;
        }
    }
}
