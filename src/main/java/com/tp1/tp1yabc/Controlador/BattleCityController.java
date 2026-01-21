package com.tp1.tp1yabc.Controlador;

import com.tp1.tp1yabc.Modelo.Juego;
import com.tp1.tp1yabc.Modelo.Tanque.DireccionApuntado;
import com.tp1.tp1yabc.Modelo.Tanque.JugadorTanque;
import com.tp1.tp1yabc.Modelo.Tanque.Proyectil;
import com.tp1.tp1yabc.Utilidades.ValorTamaniosJuego;
import com.tp1.tp1yabc.Vista.ReproductorSonido;
import com.tp1.tp1yabc.Vista.VistaJuego;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.util.HashSet;
import java.util.Set;

public class BattleCityController {
    private Juego juego;
    private VistaJuego vista;
    private Stage stage;
    private MenuController menuController;
    private final Set<KeyCode> keysPressed = new HashSet<>();
    private boolean nivelCompletadoMostrado = false;
    private ReproductorSonido reproductorSonido = new ReproductorSonido();

    public BattleCityController(Juego juego, VistaJuego vista, Stage stage, MenuController menu) {
        this.juego = juego;
        this.vista = vista;
        this.stage = stage;
        this.menuController = menu;
    }

    public void keyPressed(KeyCode key) {
        keysPressed.add(key);
    }

    public void keyReleased(KeyCode key) {
        keysPressed.remove(key);
    }

    private void manejarMovimientoTanques(JugadorTanque jugador, KeyCode up, KeyCode down, KeyCode left, KeyCode right, KeyCode shoot) {
        boolean moviendo = false;
        if (keysPressed.contains(up)) {
            jugador.setDireccion(DireccionApuntado.NORTE);
            jugador.setMoviendo(true);
            moviendo = true;
        }
        if (keysPressed.contains(down)) {
            jugador.setDireccion(DireccionApuntado.SUR);
            jugador.setMoviendo(true);
            moviendo = true;
        }
        if (keysPressed.contains(left)) {
            jugador.setDireccion(DireccionApuntado.OESTE);
            jugador.setMoviendo(true);
            moviendo = true;
        }
        if (keysPressed.contains(right)) {
            jugador.setDireccion(DireccionApuntado.ESTE);
            jugador.setMoviendo(true);
            moviendo = true;
        }

        if (!moviendo) {
            jugador.setMoviendo(false);
        }

        if (keysPressed.contains(shoot)) {
            Proyectil p = jugador.dispararTanque();
            if (p != null && jugador.isTanqueActivo()) {
                juego.registrarDisparo(p);
                reproductorSonido.reproducirEfecto("disparo");
            }
        }
    }

    public void updateGame() {
        manejarMovimientoTanques(juego.getJugador1(), KeyCode.W, KeyCode.S, KeyCode.A, KeyCode.D, KeyCode.SPACE);

        if (juego.hayDosJugadores() && juego.getJugador2() != null) {
            manejarMovimientoTanques(juego.getJugador2(), KeyCode.UP, KeyCode.DOWN, KeyCode.LEFT, KeyCode.RIGHT, KeyCode.ENTER);
        }
        juego.actualizarJuego();
        manejarEstados();
    }

    public void iniciarJuego() {
        Scene escena = new Scene(vista.getRoot(), ValorTamaniosJuego.ANCHO_ENTORNO, ValorTamaniosJuego.ALTO_ENTORNO);
        stage.setScene(escena);
        escena.setOnKeyPressed(e -> keyPressed(e.getCode()));
        escena.setOnKeyReleased(e -> keyReleased(e.getCode()));
        reproductorSonido.reproducirMusicaFondo();

        vista.setOnMenuButton(() -> {
            vista.detenerLoop();
            menuController.mostrarMenu();
        });
        vista.iniciarLoop(this::updateGame);
    }

    private void manejarEstados() {
        String estado = juego.getNombreEstadoActual();

        if ("Perdido".equals(estado)) {
            vista.detenerLoop();
            reproductorSonido.detenerMusicaFondo();
            Platform.runLater(() -> mostrarFinDelJuego("Perdido"));
        } else if ("Ganado".equals(estado) && juego.getNivelActual() >= 3) {
            vista.detenerLoop();
            reproductorSonido.detenerMusicaFondo();
            Platform.runLater(() -> mostrarFinDelJuego("Ganado"));
        } else if ("NivelCompletado".equals(estado) && !nivelCompletadoMostrado) {
            nivelCompletadoMostrado = true;
            reproductorSonido.detenerMusicaFondo();
            vista.detenerLoop();
            Platform.runLater(() -> mostrarNivelCompletado(juego.getNivelActual()));
        }
    }

    private void mostrarNivelCompletado(int nivelActual) {
        keysPressed.clear();
        vista.mostrarNivelCompletado(nivelActual);
        reproductorSonido.reproducirMusicaFondo();
        juego.siguienteNivel();
        nivelCompletadoMostrado = false;
        vista.iniciarLoop(this::updateGame);
    }

    private void mostrarFinDelJuego(String estado) {
        boolean volverAlMenu = vista.mostrarFinDelJuego(estado);
        if (volverAlMenu)
            menuController.mostrarMenu();
        else
            stage.close();
    }
}
