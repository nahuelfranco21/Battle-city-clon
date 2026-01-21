package com.tp1.tp1yabc.Modelo.Tanque;

import java.util.Random;

public class IATanque {
    static final int TIEMPO_CONDUCTA_INICIAL = 1000;
    static final int TIEMPO_CONDUCTA_FINAL = 5000;
    static final int TIEMPO_QUIETO = 2000;

    private DireccionApuntado direccionActual;
    private long tiempoFinConducta;
    private long tiempoQuieto;
    private EstadoEnemigo estadoEnemigo;
    private final Random random = new Random();

    public IATanque() {
        sortearNuevaDireccion();
        sortearTiempoConducta();
        tiempoQuieto = 0;
        estadoEnemigo = EstadoEnemigo.NORMAL;
    }

    private void sortearNuevaDireccion() {
        DireccionApuntado[] direcciones = DireccionApuntado.values();
        int indice = random.nextInt(direcciones.length);
        direccionActual = direcciones[indice];
    }

    private void sortearTiempoConducta() {
        long duracion = TIEMPO_CONDUCTA_INICIAL + random.nextInt(TIEMPO_CONDUCTA_FINAL - TIEMPO_CONDUCTA_INICIAL);
        tiempoFinConducta = System.currentTimeMillis() + duracion;
    }

    public DireccionApuntado decidirDireccion(boolean estaBloqueado) {
        long tiempoActuyal = System.currentTimeMillis();

        if (estaBloqueado) {
            tiempoQuieto += 16;
            estadoEnemigo = EstadoEnemigo.BLOQUEADO;
        } else {
            tiempoQuieto = 0;
            estadoEnemigo = EstadoEnemigo.NORMAL;
        }

        if (tiempoActuyal >= tiempoFinConducta || tiempoQuieto >= TIEMPO_QUIETO) {
            sortearNuevaDireccion();
            sortearTiempoConducta();
            tiempoQuieto = 0;
        }

        return direccionActual;
    }

}
