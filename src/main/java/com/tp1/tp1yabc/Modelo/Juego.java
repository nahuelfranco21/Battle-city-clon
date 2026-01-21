package com.tp1.tp1yabc.Modelo;

import com.tp1.tp1yabc.Modelo.Estado.*;
import com.tp1.tp1yabc.Modelo.Entorno.Bloque;
import com.tp1.tp1yabc.Modelo.Entorno.Entorno;
import com.tp1.tp1yabc.Modelo.Entorno.TanqueMuerto;
import com.tp1.tp1yabc.Modelo.PowerUps.Casco;
import com.tp1.tp1yabc.Modelo.PowerUps.Estrella;
import com.tp1.tp1yabc.Modelo.PowerUps.Granada;
import com.tp1.tp1yabc.Modelo.PowerUps.PowerUp;
import com.tp1.tp1yabc.Modelo.Tanque.*;
import com.tp1.tp1yabc.Utilidades.ValorTamaniosJuego;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Juego {

    public static final double VELOCIDAD_INICIAL_TANQUE_JUGADOR = 0.1;
    public static final int VIDA_INICIAL_TANQUE_JUGADOR = 3;
    public static final int FUERZA_DISPARO_INICIAL_TANQUE_JUGADOR = 1;
    public static final int CANTIDAD_NIVELES = 3;

    private final Entorno entorno;
    private JugadorTanque jugador1;
    private JugadorTanque jugador2;
    private final boolean hayDosJugadores;

    private final List<Proyectil> proyectiles;
    private List<EnemigoTanque> enemigos;
    private Estado estadoActual;
    private int nivelActual;
    private final Random random = new Random();

    private long ultimoSpawn = 0; // nanoTime del último spawn
    private static final long INTERVALO_SPAWN_NANOS = 10_000_000_000L;
    private int enemigosSpawnedEnMinuto = 0;
    private static final long TIEMPO_MINUTO_NANOS = 60_000_000_000L;
    private static final int MAX_ENEMIGOS_POR_MINUTO = 10;

    private final int anchoEntorno;
    private final int altoEntorno;
    private final int dimensionProyectil;
    private final int dimensionBloque;

    public Juego(boolean hayDosJugadores, int nivelInicial, int anchoEntorno, int altoEntorno, int dimensionProyectil, int dimensionBloque) {

        this.anchoEntorno = anchoEntorno;
        this.altoEntorno = altoEntorno;
        this.dimensionProyectil = dimensionProyectil;
        this.dimensionBloque = dimensionBloque;
        this.hayDosJugadores = hayDosJugadores;
        this.nivelActual = nivelInicial;

        this.proyectiles = new ArrayList<>();
        this.enemigos = new ArrayList<>();
        this.entorno = new Entorno(anchoEntorno, altoEntorno);
        this.estadoActual = new EstadoJugando(entorno);

        cargarNivel(nivelActual);
    }

    public void cargarNivel(int nivel) {
        this.nivelActual = nivel;
        CargadorNivel.cargarNivel(this, nivel); // Usa el loader XML
    }

    public void registrarDisparo(Proyectil proyectil) {
        proyectiles.add(proyectil);
    }

    public void actualizarJuego() {
        estadoActual.actualizarEstadoJuego(this);
        long tiempoActual = System.nanoTime();

        if (jugador1 != null && !jugador1.isTanqueActivo() && !jugador1.isBloqueMuertoCreado()) {
            Bloque bloqueMuerto = new TanqueMuerto((int) jugador1.getX(), (int) jugador1.getY());
            entorno.agregarBloque(bloqueMuerto);
            jugador1.setBloqueMuertoCreado(true);
        }

        if (jugador2 != null && !jugador2.isTanqueActivo() && !jugador2.isBloqueMuertoCreado()) {
            Bloque bloqueMuerto = new TanqueMuerto((int) jugador2.getX(), (int) jugador2.getY());
            entorno.agregarBloque(bloqueMuerto);
            jugador2.setBloqueMuertoCreado(true);
        }

        for (EnemigoTanque enemigo : new ArrayList<>(enemigos)) {
            enemigo.actualizarEstadoTanque();

            if (!enemigo.isTanqueActivo() && !enemigo.isBloqueMuertoCreado()) {
                Bloque bloqueMuerto = new com.tp1.tp1yabc.Modelo.Entorno.TanqueMuerto(
                        (int) enemigo.getX(), (int) enemigo.getY());
                entorno.agregarBloque(bloqueMuerto);

                for (Proyectil p : enemigo.getProyectiles()) {
                    p.desactivar();
                }
                enemigo.getProyectiles().clear();

                enemigo.setBloqueMuertoCreado(true);
            }

            if (enemigo.isTanqueActivo() && puedeMoverTanque(enemigo, enemigo.getDireccion())) {
                enemigo.moverEnDireccion(enemigo.getDireccion());
            }
        }

        enemigos.removeIf(e -> !e.isTanqueActivo() && e.isBloqueMuertoCreado());


        for (EnemigoTanque enemigo : enemigos) {
            for (Proyectil proyectil : enemigo.getProyectiles()) {
                proyectil.mover();
            }
        }
        List<Proyectil> todosProyectiles = new ArrayList<>(proyectiles);
        for (EnemigoTanque enemigo : enemigos) {
            todosProyectiles.addAll(enemigo.getProyectiles());
        }

        if (jugador1 != null && jugador1.isTanqueActivo()) {
            moverJugador(jugador1);
            verificarPowerUps(jugador1);
        }
        if (hayDosJugadores && jugador2 != null && jugador2.isTanqueActivo()) {
            moverJugador(jugador2);
            verificarPowerUps(jugador2);
        }

        moverProyectiles();
        verificarChoquesProyectil(todosProyectiles);
        verificarChoquesEntreProyectiles();
        intentarSpawnEnemigo(tiempoActual);
        entorno.actualizar();

        verificarJuegoPerdido();
        verificarJuegoGanado();

        verificarPowerUps(jugador1);
        if (hayDosJugadores && jugador2 != null)
            verificarPowerUps(jugador2);
    }

    private void moverJugador(JugadorTanque jugador) {
        if (jugador != null && jugador.isMoviendo() && puedeMoverTanque(jugador, jugador.getDireccion()))
            jugador.moverEnDireccion(jugador.getDireccion());
    }

    private void moverProyectiles() {
        List<Proyectil> proyectilesAEliminar = new ArrayList<>();
        for (Proyectil proyectil : proyectiles) {
            proyectil.mover();
            if (!proyectil.isActivo()) {
                proyectil.desactivar();
                proyectilesAEliminar.add(proyectil);
            }
        }
        proyectiles.removeAll(proyectilesAEliminar);
    }

    private void verificarChoquesEntreProyectiles() {
        List<Proyectil> proyectilesAEliminar = new ArrayList<>();
        List<Proyectil> todosLosProyectiles = new ArrayList<>(proyectiles);

        for (EnemigoTanque enemigo : enemigos) {
            todosLosProyectiles.addAll(enemigo.getProyectiles());
        }

        for (int i = 0; i < todosLosProyectiles.size(); i++) {
            Proyectil p1 = todosLosProyectiles.get(i);
            if (!p1.isActivo())
                continue;
            for (int j = i + 1; j < todosLosProyectiles.size(); j++) {
                Proyectil p2 = todosLosProyectiles.get(j);
                if (!p2.isActivo())
                    continue;
                if (choqueEntreProyectiles(p1, p2)) {
                    p1.desactivar();
                    p2.desactivar();
                    proyectilesAEliminar.add(p1);
                    proyectilesAEliminar.add(p2);
                }
            }
        }
        proyectiles.removeAll(proyectilesAEliminar);
        for (EnemigoTanque enemigo : enemigos) {
            enemigo.getProyectiles().removeAll(proyectilesAEliminar);
        }
    }

    private boolean choqueEntreProyectiles(Proyectil p1, Proyectil p2) {
        double px1 = p1.getX(), py1 = p1.getY();
        double px2 = p2.getX(), py2 = p2.getY();
        int tamanio = ValorTamaniosJuego.TAMANIO_PROYECTIL;

        return px1 < px2 + tamanio && px1 + tamanio > px2 &&
                py1 < py2 + tamanio && py1 + tamanio > py2;
    }

    private void verificarChoquesProyectil(List<Proyectil> proyectilesARevisar) {
        List<Proyectil> proyectilesAEliminar = new ArrayList<>();
        List<Bloque> bloquesAEliminar = new ArrayList<>();

        for (Proyectil proyectil : new ArrayList<>(proyectilesARevisar)) {
            boolean choco = false;

            if (chocoConBloque(proyectil, bloquesAEliminar)) {
                choco = true;
            }
            if (!choco && chocoConTanque(proyectil, jugador1)) {
                choco = true;
            }
            if (!choco && hayDosJugadores && chocoConTanque(proyectil, jugador2)) {
                choco = true;
            }
            if (!choco && chocoConEnemigos(proyectil)) {
                choco = true;
            }
            if (choco) {
                proyectil.desactivar();
                proyectilesAEliminar.add(proyectil);
            }
        }

        proyectiles.removeAll(proyectilesAEliminar);

        for (EnemigoTanque enemigo : enemigos) {
            enemigo.getProyectiles().removeAll(proyectilesAEliminar);
        }

        for (Bloque bloque : bloquesAEliminar) {
            entorno.eliminarBloque(bloque);
        }
    }


    private boolean chocoConBloque(Proyectil proyectil, List<Bloque> bloquesAEliminar) {
        double px = proyectil.getX();
        double py = proyectil.getY();

        if (!entorno.colisionaConBloques(px, py, dimensionProyectil, dimensionProyectil, false)) {
            return false;
        }

        for (Bloque bloque : entorno.getBloques()) {
            int bx = bloque.getX();
            int by = bloque.getY();

            if (px < bx + dimensionBloque && px + dimensionProyectil > bx &&
                    py < by + dimensionBloque && py + dimensionProyectil > by && bloque.bloqueaDisparo()) {

                bloque.recibirImpacto();
                if (bloque.estaDestruido()) {
                    bloquesAEliminar.add(bloque);
                }
                return true;
            }
        }
        return false;
    }

    private boolean chocoConTanque(Proyectil proyectil, Tanque tanque) {
        if (tanque != null && tanque.isTanqueActivo() && tanque != proyectil.getTanqueOrigen() && tanque.colisionaTanqueConProyectil(proyectil)) {
            tanque.recibirDanio(proyectil);
            return true;
        }
        return false;
    }


    private boolean chocoConEnemigos(Proyectil proyectil) {
        if (proyectil.getTanqueOrigen().esEnemigo()) {
            return false;
        }

        for (EnemigoTanque enemigo : enemigos) {
            if (enemigo.isTanqueActivo() && enemigo.colisionaTanqueConProyectil(proyectil) && enemigo != proyectil.getTanqueOrigen()) {
                enemigo.recibirDanio(proyectil);

                if (!enemigo.isTanqueActivo()) {
                    intentarGenerarPowerUp();
                }

                return true;
            }
        }
        return false;
    }

    private void intentarGenerarPowerUp() {
        if (random.nextDouble() < 0.2) { // 20% de probabilidad
            String[] tiposPowerUp = {"estrella", "casco", "granada"};
            String tipo = tiposPowerUp[random.nextInt(tiposPowerUp.length)];

            int x, y;
            boolean posicionValida;

            do {
                x = random.nextInt(anchoEntorno - dimensionBloque);
                y = random.nextInt(altoEntorno - dimensionBloque);

                posicionValida = !entorno.colisionaConBloques(x, y, dimensionBloque, dimensionBloque, false);

            } while (!posicionValida);

            PowerUp powerUp = switch (tipo) {
                case "estrella" -> new Estrella(x, y);
                case "casco" -> new Casco(x, y);
                case "granada" -> new Granada(x, y);
                default -> null;
            };

            if (powerUp != null) {
                entorno.agregarPowerUp(powerUp);
            }
        }
    }

    public void intentarSpawnEnemigo(long tiempoActual) {
        if (tiempoActual - ultimoSpawn >= TIEMPO_MINUTO_NANOS) {
            enemigosSpawnedEnMinuto = 0;
            ultimoSpawn = tiempoActual;
        }

        if (enemigosSpawnedEnMinuto >= MAX_ENEMIGOS_POR_MINUTO) return;
        if (tiempoActual - ultimoSpawn < INTERVALO_SPAWN_NANOS) return;

        int intentos = 0;
        boolean spawnExitoso = false;

        while (intentos < 10 && !spawnExitoso) {
            int x = random.nextInt(anchoEntorno - ValorTamaniosJuego.TAMANIO_TANQUE);
            int y = 1;

            TipoTanque tipo = TipoTanque.values()[random.nextInt(TipoTanque.values().length)];

            EnemigoTanque posible = new EnemigoTanque(x, y, DireccionApuntado.SUR, tipo, true);

            if (puedeMoverTanque(posible, DireccionApuntado.SUR)) {
                enemigos.add(posible);
                enemigosSpawnedEnMinuto++;
                spawnExitoso = true;
                ultimoSpawn = tiempoActual;
            } else {
                intentos++;
            }
        }
    }

    public boolean hayBloqueSolido(int x, int y) {
        Bloque bloque = entorno.getBloqueEn(x, y);
        if (bloque == null)
            return false;
        return bloque.impideElPaso();
    }

    public boolean puedeMoverTanque(Tanque tanque, DireccionApuntado direccion) {
        double proximaX = direccion.moverHorizontal(tanque.getX(), tanque.getVelocidadTanque());
        double proximaY = direccion.moverVertical(tanque.getY(), tanque.getVelocidadTanque());
        int tamanio = ValorTamaniosJuego.TAMANIO_TANQUE;

        boolean colisionConBloque = entorno.colisionaConBloques(proximaX, proximaY, tamanio, tamanio, true);

        boolean estaDentroDelEntorno = proximaX >= 0 && proximaX + tamanio <= anchoEntorno &&
                proximaY >= 0 && proximaY + tamanio <= altoEntorno;

        boolean colisionConTanque = false;
        for (EnemigoTanque e : enemigos) {
            if (e == tanque || !e.isTanqueActivo()) continue;

            boolean overlap = proximaX < e.getX() + ValorTamaniosJuego.TAMANIO_TANQUE &&
                    proximaX + tamanio > e.getX() &&
                    proximaY < e.getY() + ValorTamaniosJuego.TAMANIO_TANQUE &&
                    proximaY + tamanio > e.getY();

            if (overlap) {
                colisionConTanque = true;
                break;
            }
        }

        if (jugador1 != null && jugador2 != null) {
            Tanque otroJugador = (tanque == jugador1) ? jugador2 : jugador1;
            if (otroJugador.isTanqueActivo()) {
                boolean overlap = proximaX < otroJugador.getX() + ValorTamaniosJuego.TAMANIO_TANQUE &&
                        proximaX + tamanio > otroJugador.getX() &&
                        proximaY < otroJugador.getY() + ValorTamaniosJuego.TAMANIO_TANQUE &&
                        proximaY + tamanio > otroJugador.getY();

                if (overlap) {
                    colisionConTanque = true;
                }
            }
        }

        return !colisionConBloque && estaDentroDelEntorno && !colisionConTanque;
    }


    private boolean colisionaTanqueConPowerUp(Tanque tanque, PowerUp powerUp) {
        double posicionTanqueX = tanque.getX();
        double posicionPowerUpX = powerUp.getX();
        double tamanioTanque = ValorTamaniosJuego.TAMANIO_TANQUE;

        double posicionTanqueY = tanque.getY();
        double posicionPowerUpY = powerUp.getY();
        double tamanioPowerUp = 25;

        return posicionTanqueX < posicionPowerUpX + tamanioPowerUp &&
                posicionTanqueX + tamanioTanque > posicionPowerUpX &&
                posicionTanqueY < posicionPowerUpY + tamanioPowerUp &&
                posicionTanqueY + tamanioTanque > posicionPowerUpY;
    }

    private void verificarPowerUps(JugadorTanque jugador) {
        if (jugador == null || !jugador.isTanqueActivo()) return;

        for (PowerUp pu : new ArrayList<>(entorno.getPowerUps())) {
            if (pu.estaActivo() && colisionaTanqueConPowerUp(jugador, pu)) {
                if (Objects.equals(pu.getTipo(), "granada")) {
                    pu.aplicar(enemigos);
                } else {
                    pu.aplicar(jugador);
                }
                entorno.eliminarPowerUp(pu);
            }
        }
    }

    public void verificarJuegoPerdido() {
        boolean baseDestruida = entorno.baseDestruida();

        boolean jugador1Muerto = jugador1 == null || !jugador1.isTanqueActivo() || jugador1.getVidaTanque() == 0;
        boolean jugador2Muerto = !hayDosJugadores || jugador2 == null || !jugador2.isTanqueActivo() || jugador2.getVidaTanque() == 0;

        if (baseDestruida || (jugador1Muerto && jugador2Muerto)) {
            estadoActual = new EstadoPerdido();
        }
    }

    public void verificarJuegoGanado() {
        boolean todosMuertos = enemigos.stream().allMatch(e -> !e.isTanqueActivo());
        if (todosMuertos) {
            if (nivelActual < CANTIDAD_NIVELES) {
                estadoActual = new EstadoNivelCompletado();
            } else {
                estadoActual = new EstadoGanado();
            }
        }
    }

    public void siguienteNivel() {
        if (nivelActual < CANTIDAD_NIVELES) {
            nivelActual++;
            enemigos.clear();
            cargarNivel(nivelActual);
            estadoActual = new EstadoJugando(entorno);
        } else {
            estadoActual = new EstadoGanado();
        }
    }

    public JugadorTanque getJugador1() {
        return jugador1;
    }

    public JugadorTanque getJugador2() {
        return jugador2;
    }

    public List<EnemigoTanque> getEnemigos() {
        return enemigos;
    }

    public List<Proyectil> getProyectiles() {
        return proyectiles;
    }

    public Entorno getEntorno() {
        return entorno;
    }

    public String getNombreEstadoActual() {
        return estadoActual.getNombreEstado();
    }

    public boolean hayDosJugadores() {
        return hayDosJugadores;
    }

    public int getNivelActual() {
        return nivelActual;
    }

    public void setJugador1(JugadorTanque jugador) {
        this.jugador1 = jugador;
    }

    public void setJugador2(JugadorTanque jugador) {
        this.jugador2 = jugador;
    }
}