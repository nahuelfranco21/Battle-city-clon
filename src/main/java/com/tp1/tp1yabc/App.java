package com.tp1.tp1yabc;

import com.tp1.tp1yabc.Controlador.MenuController;
import javafx.application.Application;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage stage) {
        MenuController menuController = new MenuController(stage);
        menuController.mostrarMenu();
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
