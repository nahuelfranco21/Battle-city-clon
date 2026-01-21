package com.tp1.tp1yabc.Controlador;

import com.tp1.tp1yabc.Modelo.Juego;
import com.tp1.tp1yabc.Utilidades.ValorTamaniosJuego;
import com.tp1.tp1yabc.Vista.*;
import com.tp1.tp1yabc.Vista.VistaJuego;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MenuController {
    private Stage stage;
    private MenuVista vista;

    public MenuController(Stage stage) {
        this.stage = stage;
        this.vista = new MenuVista();
    }

    public void mostrarMenu() {
        vista = new MenuVista();

        Scene scene = new Scene(vista.getRoot(), ValorTamaniosJuego.ANCHO_ENTORNO, ValorTamaniosJuego.ALTO_ENTORNO);
        stage.setScene(scene);
        stage.setTitle("Battle City");

        vista.getBtn1Jugador().setOnAction(e -> iniciarJuego(false));
        vista.getBtn2Jugadores().setOnAction(e -> iniciarJuego(true));
        vista.getBtnSalir().setOnAction(e -> stage.close());
    }

    private void iniciarJuego(boolean dosJugadores) {
        Juego juego = new Juego(dosJugadores, 1, ValorTamaniosJuego.ANCHO_ENTORNO, ValorTamaniosJuego.ALTO_ENTORNO,
                ValorTamaniosJuego.TAMANIO_PROYECTIL, ValorTamaniosJuego.TAMANIO_BLOQUE);
        VistaJuego vistaJuego = new VistaJuego(juego);
        BattleCityController controller = new BattleCityController(juego, vistaJuego, stage, this);
        controller.iniciarJuego();
    }
}



