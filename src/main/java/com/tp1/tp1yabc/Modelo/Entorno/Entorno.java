package com.tp1.tp1yabc.Modelo.Entorno;

import com.tp1.tp1yabc.Modelo.PowerUps.PowerUp;
import com.tp1.tp1yabc.Utilidades.ValorTamaniosJuego;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Entorno {
    private List<Bloque> bloques;
    private final List<PowerUp> powerUps;
    private int ancho;
    private int alto;
    private Base base;

    public Entorno(int ancho, int alto) {
        this.ancho = ancho;
        this.alto = alto;
        bloques = new ArrayList<>();
        powerUps = new ArrayList<>();
    }

    public void agregarBloque(Bloque bloque) {
        bloques.add(bloque);

        if (Objects.equals(bloque.getTipo(), "base")) {
            base = (Base) bloque;
        }
    }

    public void agregarPowerUp(PowerUp powerUp) {
        powerUps.add(powerUp);
    }

    public List<Bloque> getBloques() {
        return bloques;
    }

    public List<PowerUp> getPowerUps() {
        return powerUps;
    }

    public Bloque getBloqueEn(int x, int y) {
        for (Bloque b : bloques) {
            if (b.getX() == x && b.getY() == y) return b;
        }
        return null;
    }

    public boolean colisionaConBloques(double x, double y, int alto, int ancho, boolean ignorarSuelo) {
        for (Bloque b : bloques) {
            if (ignorarSuelo && !b.impideElPaso()) {
                continue;
            }
            if (x < b.getX() + ValorTamaniosJuego.TAMANIO_TANQUE && x + ancho > b.getX() &&
                    y < b.getY() + ValorTamaniosJuego.TAMANIO_TANQUE && y + alto > b.getY()) {
                return true;
            }
        }
        return false;
    }


    public void eliminarBloque(Bloque bloque) {
        bloques.remove(bloque);
    }

    public void eliminarPowerUp(PowerUp powerUp) {
        powerUps.remove(powerUp);
    }

    public boolean baseDestruida() {
        return base != null && base.estaDestruido();
    }

    public void actualizar() {
        bloques.removeIf(Bloque::estaDestruido);
        powerUps.removeIf(p -> !p.estaActivo());
    }

    public void reiniciar() {
        bloques.clear();
        powerUps.clear();
    }
}
