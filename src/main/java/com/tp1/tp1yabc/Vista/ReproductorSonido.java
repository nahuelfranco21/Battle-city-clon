package com.tp1.tp1yabc.Vista;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.HashMap;
import java.util.Map;

public class ReproductorSonido {
    private Map<String, MediaPlayer> efectos = new HashMap<>();
    private MediaPlayer musicaFondo;

    public ReproductorSonido() {
        cargarEfectos();
    }

    private void cargarEfectos() {
        efectos.put("disparo", crearMediaPlayer("/sonidos/laser-gun-280344.wav"));
    }

    private MediaPlayer crearMediaPlayer(String ruta) {
        try {
            Media media = new Media(getClass().getResource(ruta).toExternalForm());
            MediaPlayer player = new MediaPlayer(media);
            return player;
        } catch (Exception e) {
            System.err.println("Error cargando sonido: " + ruta);
            return null;
        }
    }

    public void reproducirEfecto(String efecto) {
        MediaPlayer mp = new MediaPlayer(efectos.get(efecto).getMedia());
        mp.play();
    }

    public void reproducirMusicaFondo() {
        if (musicaFondo == null) {
            Media media = new Media(getClass().getResource("/sonidos/musica-fondo.wav").toExternalForm());
            musicaFondo = new MediaPlayer(media);
            musicaFondo.setCycleCount(MediaPlayer.INDEFINITE);
            musicaFondo.play();
        }
    }

    public void detenerMusicaFondo() {
        if (musicaFondo != null) {
            musicaFondo.stop();
            musicaFondo = null;
        }
    }
}


