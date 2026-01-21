package com.tp1.tp1yabc.Vista;

import com.tp1.tp1yabc.Utilidades.ValorTamaniosJuego;
import javafx.animation.ScaleTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

import java.util.Objects;

public class MenuVista {
    private StackPane root;
    private Button btn1Jugador;
    private Button btn2Jugadores;
    private Button btnSalir;

    public MenuVista() {
        Label titulo = new Label("BATTLE CITY");
        titulo.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 48));
        titulo.setTextFill(Color.GREENYELLOW);
        titulo.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.5), 10, 0, 0, 0);");

        btn1Jugador = crearBoton("1 Jugador");
        btn2Jugadores = crearBoton("2 Jugadores");
        btnSalir = crearBoton("Salir");

        btn1Jugador.setMinWidth(200);
        btn2Jugadores.setMinWidth(200);
        btnSalir.setMinWidth(200);

        VBox menu = new VBox(20, titulo, btn1Jugador, btn2Jugadores, btnSalir);
        menu.setAlignment(Pos.CENTER);

        ImageView fondo = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/logo.png"))));
        fondo.setFitWidth(ValorTamaniosJuego.ANCHO_ENTORNO);
        fondo.setFitHeight(ValorTamaniosJuego.ALTO_ENTORNO);

        root = new StackPane(fondo, menu);
    }

    public StackPane getRoot() {
        return root;
    }

    public Button getBtn1Jugador() {
        return btn1Jugador;
    }

    public Button getBtn2Jugadores() {
        return btn2Jugadores;
    }

    public Button getBtnSalir() {
        return btnSalir;
    }

    private Button crearBoton(String texto) {
        Button boton = new Button(texto);
        boton.setMinSize(200, 50);
        boton.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        boton.setTextFill(Color.WHITE);
        boton.setStyle(
                "-fx-background-color: #4682B4;" +
                        "-fx-background-radius: 15;" +
                        "-fx-padding: 10 20;" +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 5, 0, 0, 2);"
        );

        boton.setOnMouseEntered(e -> {
            boton.setStyle(
                    "-fx-background-color: #32CD32;" +
                            "-fx-background-radius: 15;" +
                            "-fx-padding: 10 20;" +
                            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.5), 8, 0, 0, 3);"
            );

            ScaleTransition escala = new ScaleTransition(Duration.millis(200), boton);
            escala.setToX(1.05);
            escala.setToY(1.05);
            escala.play();
        });

        boton.setOnMouseExited(e -> {
            boton.setStyle(
                    "-fx-background-color: #4682B4;" +
                            "-fx-background-radius: 15;" +
                            "-fx-padding: 10 20;" +
                            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 5, 0, 0, 2);"
            );

            ScaleTransition escala = new ScaleTransition(Duration.millis(200), boton);
            escala.setToX(1.0);
            escala.setToY(1.0);
            escala.play();
        });

        return boton;
    }
}
