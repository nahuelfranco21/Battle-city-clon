package com.tp1.tp1yabc.Vista;

import com.tp1.tp1yabc.Modelo.Entorno.Bloque;
import com.tp1.tp1yabc.Modelo.Juego;
import com.tp1.tp1yabc.Modelo.PowerUps.PowerUp;
import com.tp1.tp1yabc.Modelo.Tanque.Proyectil;
import com.tp1.tp1yabc.Modelo.Tanque.Tanque;
import com.tp1.tp1yabc.Utilidades.ValorTamaniosJuego;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.*;

public class VistaJuego {
    private final Pane root;
    private final Canvas canvas;
    private final Set<KeyCode> keysPressed = new HashSet<>();
    private Juego juego;
    private final Map<String, Image> imagenesBloques = new HashMap<>();
    private final Map<String, Image> imagenesPowerUps = new HashMap<>();
    private final Map<String, VistaTanque> imagenesTanques = new HashMap<>();
    private final Map<String, Image> imagenesProyectiles = new HashMap<>();
    private final Image imagenCorazon;
    private final Button btnMenu;
    private AnimationTimer gameLoop;

    public VistaJuego(Juego juego) {
        this.juego = juego;
        this.root = new Pane();
        this.canvas = new Canvas(ValorTamaniosJuego.ANCHO_ENTORNO, ValorTamaniosJuego.ALTO_ENTORNO);
        root.getChildren().add(canvas);
        this.btnMenu = new Button("Menú");
        root.getChildren().add(btnMenu);

        String[] jugadores = {"P1", "P2"};
        String[] enemigos = {"BASICO", "RAPIDO", "POTENTE", "BLINDADO"};
        String[] tipos = {"ladrillo", "acero", "agua", "bosque", "base"};
        String[] powerUps = {"estrella", "casco", "granada"};

        for (String tipo : tipos) {
            String ruta = "file:src/main/resources/img/imagenesBloques/" + tipo + ".png";
            imagenesBloques.put(tipo, new Image(ruta));
        }

        for (String power : powerUps) {
            String ruta = "/img/imagenesPowerUps/" + power + ".png";
            imagenesPowerUps.put(power, new Image(Objects.requireNonNull(getClass().getResourceAsStream(ruta))));
        }

        Image tanqueMuertoImg = new Image("file:src/main/resources/img/imagenesTanques/tanque_muerto.png");
        imagenesBloques.put("tanque_muerto", tanqueMuertoImg);

        imagenCorazon = new Image("file:src/main/resources/img/imagenesTanques/vida.png");

        for (String jugador : jugadores) {
            Image spriteA = new Image("file:src/main/resources/img/imagenesTanques/tanque_" + jugador + "_1.png");
            Image spriteB = new Image("file:src/main/resources/img/imagenesTanques/tanque_" + jugador + "_2.png");
            imagenesTanques.put(jugador, new VistaTanque(spriteA, spriteB));
        }


        for (String enemigo : enemigos) {
            Image spriteA = new Image("file:src/main/resources/img/imagenesTanques/tanque_enem_" + enemigo + "_1.png");
            Image spriteB = new Image("file:src/main/resources/img/imagenesTanques/tanque_enem_" + enemigo + "_2.png");
            imagenesTanques.put(enemigo, new VistaTanque(spriteA, spriteB));
        }

        imagenesProyectiles.put("proyectil", new Image("file:src/main/resources/img/proyectil.png"));

    }

    public Pane getRoot() {
        return root;
    }

    public void setOnMenuButton(Runnable action) {
        btnMenu.setFocusTraversable(false);
        btnMenu.setStyle("-fx-background-color: transparent; " + "-fx-text-fill: white; ");
        btnMenu.setLayoutX(740);
        btnMenu.setLayoutY(560);
        btnMenu.setOnAction(e -> action.run());
    }

    public void render() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, ValorTamaniosJuego.ANCHO_ENTORNO, ValorTamaniosJuego.ALTO_ENTORNO);

        for (Bloque b : juego.getEntorno().getBloques()) {
            if (!(Objects.equals(b.getTipo(), "bosque"))) {
                Image img = imagenesBloques.get(b.getTipo());
                if (img != null) {
                    gc.drawImage(img, b.getX(), b.getY(), ValorTamaniosJuego.TAMANIO_BLOQUE, ValorTamaniosJuego.TAMANIO_BLOQUE);
                } else {
                    gc.setFill(Color.WHITE); // fallback
                    gc.fillRect(b.getX(), b.getY(), ValorTamaniosJuego.TAMANIO_BLOQUE, ValorTamaniosJuego.TAMANIO_BLOQUE);
                }
            }
        }

        List<Tanque> todosLosTanques = new ArrayList<>();
        todosLosTanques.add(juego.getJugador1());
        if (juego.hayDosJugadores()) {
            todosLosTanques.add(juego.getJugador2());
        }
        todosLosTanques.addAll(juego.getEnemigos());

        for (Tanque t : todosLosTanques) {
            if (!t.isTanqueActivo()) {
                Image muerto = imagenesBloques.get("tanque_muerto");
                gc.drawImage(muerto, t.getX(), t.getY(),
                        ValorTamaniosJuego.TAMANIO_TANQUE,
                        ValorTamaniosJuego.TAMANIO_TANQUE);
            } else {
                VistaTanque vista;
                if (t.esEnemigo()) {
                    vista = imagenesTanques.get(t.getTipoDeTanque().name());
                } else {
                    vista = imagenesTanques.get(t == juego.getJugador1() ? "P1" : "P2");
                }
                if (vista != null) {
                    vista.render(gc, t);
                }
            }
        }


        for (Bloque b : juego.getEntorno().getBloques()) {
            if (Objects.equals(b.getTipo(), "bosque")) {
                Image img = imagenesBloques.get("bosque");
                gc.drawImage(img, b.getX(), b.getY(), ValorTamaniosJuego.TAMANIO_BLOQUE, ValorTamaniosJuego.TAMANIO_BLOQUE);
            }
        }

        for (Proyectil p : juego.getProyectiles()) {
            Image imgProyectil = imagenesProyectiles.get("proyectil");
            gc.drawImage(imgProyectil, p.getX(), p.getY(), ValorTamaniosJuego.TAMANIO_PROYECTIL, ValorTamaniosJuego.TAMANIO_PROYECTIL);
        }

        for (Tanque enemigo : juego.getEnemigos()) {
            for (Proyectil p : enemigo.getProyectiles()) {
                Image imgProyectil = imagenesProyectiles.get("proyectil");
                gc.drawImage(imgProyectil, p.getX(), p.getY(), ValorTamaniosJuego.TAMANIO_PROYECTIL, ValorTamaniosJuego.TAMANIO_PROYECTIL);
            }
        }

        for (PowerUp p : juego.getEntorno().getPowerUps()) {
            gc.drawImage(imagenesPowerUps.get(p.getTipo()), p.getX(), p.getY(), ValorTamaniosJuego.TAMANIO_TANQUE, ValorTamaniosJuego.TAMANIO_TANQUE);
        }

        for (Proyectil p : juego.getProyectiles()) {
            Image imgProyectil = imagenesProyectiles.get("proyectil");
            gc.drawImage(imgProyectil, p.getX(), p.getY(), ValorTamaniosJuego.TAMANIO_PROYECTIL, ValorTamaniosJuego.TAMANIO_PROYECTIL);
        }

        if (juego.hayDosJugadores() && juego.getJugador2() != null) {
            for (Proyectil p : juego.getProyectiles()) {
                Image imgProyectil = imagenesProyectiles.get("proyectil");
                gc.drawImage(imgProyectil, p.getX(), p.getY(), ValorTamaniosJuego.TAMANIO_PROYECTIL, ValorTamaniosJuego.TAMANIO_PROYECTIL);
            }
        }

        if (juego.getJugador1() != null) {
            for (int i = 0; i < juego.getJugador1().getVidaTanque(); i++) {
                gc.drawImage(imagenCorazon, 10 + i * 25, 10, ValorTamaniosJuego.TAMANIO_TANQUE, ValorTamaniosJuego.TAMANIO_TANQUE);
            }
        }

        if (juego.hayDosJugadores() && juego.getJugador2() != null) {
            for (int i = 0; i < juego.getJugador2().getVidaTanque(); i++) {
                gc.drawImage(imagenCorazon, ValorTamaniosJuego.ANCHO_ENTORNO - (i + 1) * 25, 10, ValorTamaniosJuego.TAMANIO_TANQUE, ValorTamaniosJuego.TAMANIO_TANQUE);
            }
        }
    }

    public void mostrarNivelCompletado(int nivelActual) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Nivel completado");
        alert.setHeaderText(null);
        alert.setContentText("¡Has completado el nivel " + nivelActual + "!");
        ButtonType btnContinuar = new ButtonType("Continuar");
        alert.getButtonTypes().setAll(btnContinuar);
        alert.showAndWait();
    }

    public boolean mostrarFinDelJuego(String estado) {
        String mensaje;
        if ("Perdido".equals(estado))
            mensaje = "¡Has perdido!";
        else if ("Ganado".equals(estado))
            mensaje = "¡Has ganado!";
        else if ("NivelCompletado".equals(estado))
            mensaje = "¡Nivel completado!";
        else
            mensaje = "Fin del juego.";

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Fin del Juego");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);

        ButtonType btnMenu = new ButtonType("Volver al menú");
        ButtonType btnSalir = new ButtonType("Salir");
        alert.getButtonTypes().setAll(btnMenu, btnSalir);

        Optional<ButtonType> result = alert.showAndWait();

        return (result.isPresent() && result.get() == btnMenu);
    }

    public void iniciarLoop(Runnable onUpdate) {
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                onUpdate.run();
                render();
            }
        };
        gameLoop.start();
    }

    public void detenerLoop() {
        if (gameLoop != null) gameLoop.stop();
    }
}

