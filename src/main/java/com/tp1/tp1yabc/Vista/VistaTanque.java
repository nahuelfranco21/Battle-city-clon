package com.tp1.tp1yabc.Vista;

import com.tp1.tp1yabc.Modelo.Tanque.DireccionApuntado;
import com.tp1.tp1yabc.Modelo.Tanque.Tanque;
import com.tp1.tp1yabc.Utilidades.ValorTamaniosJuego;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.EnumMap;

public class VistaTanque {

    private final Image spriteA;
    private final Image spriteB;
    private Image spriteActual;
    private long tiempoUltimoCambio;
    private final EnumMap<DireccionApuntado, Image> spriteARotado = new EnumMap<>(DireccionApuntado.class);
    private final EnumMap<DireccionApuntado, Image> spriteBRotado = new EnumMap<>(DireccionApuntado.class);
    private final Image overlayInvulnerable = new Image("file:src/main/resources/img/invulnerable.png");

    private static final long INTERVALO_CAMBIO = 200_000_000L;

    public VistaTanque(Image spriteA, Image spriteB) {
        this.spriteA = spriteA;
        this.spriteB = spriteB;
        this.spriteActual = spriteA;
        this.tiempoUltimoCambio = System.nanoTime();

        for (DireccionApuntado dir : DireccionApuntado.values()) {
            spriteARotado.put(dir, rotarSprite(spriteA, dir));
            spriteBRotado.put(dir, rotarSprite(spriteB, dir));
        }
    }

    public void render(GraphicsContext gc, Tanque tanque) {
        long tiempoActual = System.nanoTime();
        if (tanque.isMoviendo() && tiempoActual - tiempoUltimoCambio > INTERVALO_CAMBIO) {
            spriteActual = (spriteActual == spriteA) ? spriteB : spriteA;
            tiempoUltimoCambio = tiempoActual;
        }

        Image spriteRotado = (spriteActual == spriteA) ? spriteARotado.get(tanque.getDireccion())
                : spriteBRotado.get(tanque.getDireccion());

        gc.drawImage(spriteRotado, tanque.getX(), tanque.getY(),
                ValorTamaniosJuego.TAMANIO_TANQUE, ValorTamaniosJuego.TAMANIO_TANQUE);

        if (tanque.esInvulnerable()) {
            gc.drawImage(overlayInvulnerable, tanque.getX() - 10, tanque.getY() - 10,
                    ValorTamaniosJuego.TAMANIO_TANQUE * 2, ValorTamaniosJuego.TAMANIO_TANQUE * 2);
        }
    }

    private Image rotarSprite(Image sprite, DireccionApuntado direccion) {
        double grados;
        switch (direccion) {
            case NORTE -> grados = 0;
            case ESTE -> grados = 90;
            case SUR -> grados = 180;
            case OESTE -> grados = 270;
            default -> grados = 0;
        }

        Canvas canvas = new Canvas(ValorTamaniosJuego.TAMANIO_TANQUE, ValorTamaniosJuego.TAMANIO_TANQUE);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.save();
        gc.translate(ValorTamaniosJuego.TAMANIO_TANQUE / 2.0, ValorTamaniosJuego.TAMANIO_TANQUE / 2.0);
        gc.rotate(grados);
        gc.drawImage(sprite, -ValorTamaniosJuego.TAMANIO_TANQUE / 2.0, -ValorTamaniosJuego.TAMANIO_TANQUE / 2.0,
                ValorTamaniosJuego.TAMANIO_TANQUE, ValorTamaniosJuego.TAMANIO_TANQUE);
        gc.restore();
        return canvas.snapshot(null, null);
    }
}
