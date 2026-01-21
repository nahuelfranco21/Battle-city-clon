package com.tp1.tp1yabc.Modelo;

import com.tp1.tp1yabc.Modelo.Entorno.*;
import com.tp1.tp1yabc.Modelo.PowerUps.*;
import com.tp1.tp1yabc.Modelo.Tanque.DireccionApuntado;
import com.tp1.tp1yabc.Modelo.Tanque.EnemigoTanque;
import com.tp1.tp1yabc.Modelo.Tanque.JugadorTanque;
import com.tp1.tp1yabc.Modelo.Tanque.TipoTanque;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;

public class CargadorNivel {

    public static void cargarNivel(Juego juego, int nivel) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputStream is = CargadorNivel.class.getResourceAsStream("/niveles/nivel" + nivel + ".xml");
            if (is == null) throw new RuntimeException("Nivel no encontrado");
            Document doc = builder.parse(is);


            Element root = doc.getDocumentElement();
            int idNivel = Integer.parseInt(root.getAttribute("id"));

            Entorno entorno = juego.getEntorno();
            entorno.getBloques().clear();
            entorno.getPowerUps().clear();
            juego.getEnemigos().clear();
            juego.getProyectiles().clear();

            NodeList nodoBloques = root.getElementsByTagName("bloque");
            for (int i = 0; i < nodoBloques.getLength(); i++) {
                Element bloqueElem = (Element) nodoBloques.item(i);
                int bx = Integer.parseInt(bloqueElem.getAttribute("x"));
                int by = Integer.parseInt(bloqueElem.getAttribute("y"));
                String tipo = bloqueElem.getAttribute("tipo");

                Bloque bloque;
                switch (tipo) {
                    case "ladrillo":
                        bloque = new Ladrillo(bx, by);
                        break;
                    case "acero":
                        bloque = new Acero(bx, by);
                        break;
                    case "agua":
                        bloque = new Agua(bx, by);
                        break;
                    case "bosque":
                        bloque = new Bosque(bx, by);
                        break;
                    case "base":
                        bloque = new Base(bx, by);
                        break;
                    default:
                        continue;
                }
                entorno.agregarBloque(bloque);
            }

            NodeList nodoJugadores = root.getElementsByTagName("jugador");
            for (int i = 0; i < nodoJugadores.getLength(); i++) {
                Element jugadorElem = (Element) nodoJugadores.item(i);
                int x = Integer.parseInt(jugadorElem.getAttribute("x"));
                int y = Integer.parseInt(jugadorElem.getAttribute("y"));
                String id = jugadorElem.getAttribute("id");

                JugadorTanque jugador = new JugadorTanque(
                        x, y,
                        DireccionApuntado.NORTE,
                        Juego.VELOCIDAD_INICIAL_TANQUE_JUGADOR,
                        Juego.VIDA_INICIAL_TANQUE_JUGADOR,
                        Juego.FUERZA_DISPARO_INICIAL_TANQUE_JUGADOR,
                        TipoTanque.BASICO,
                        false
                );

                if (id.equalsIgnoreCase("player1")) {
                    juego.setJugador1(jugador);
                } else if (id.equalsIgnoreCase("player2")) {
                    juego.setJugador2(jugador);
                }
            }

            NodeList nodoEnemigos = root.getElementsByTagName("enemigo");
            for (int i = 0; i < nodoEnemigos.getLength(); i++) {
                Element enemigoElem = (Element) nodoEnemigos.item(i);
                int ex = Integer.parseInt(enemigoElem.getAttribute("x"));
                int ey = Integer.parseInt(enemigoElem.getAttribute("y"));
                String tipoStr = enemigoElem.getAttribute("tipo");
                String dirStr = enemigoElem.getAttribute("direction");

                DireccionApuntado dir = DireccionApuntado.valueOf(dirStr.toUpperCase());
                TipoTanque tipo = TipoTanque.valueOf(tipoStr.toUpperCase());
                EnemigoTanque enemigo = new EnemigoTanque(ex, ey, dir, tipo, true);
                if (enemigo != null) {
                    juego.getEnemigos().add(enemigo);
                }
            }

            NodeList nodoPowerUps = root.getElementsByTagName("powerup");
            for (int i = 0; i < nodoPowerUps.getLength(); i++) {
                Element powerUpElem = (Element) nodoPowerUps.item(i);
                int px = Integer.parseInt(powerUpElem.getAttribute("x"));
                int py = Integer.parseInt(powerUpElem.getAttribute("y"));
                String tipo = powerUpElem.getAttribute("tipo");

                PowerUp powerUp;
                switch (tipo) {
                    case "casco":
                        powerUp = new Casco(px, py);
                        break;
                    case "estrella":
                        powerUp = new Estrella(px, py);
                        break;
                    case "granada":
                        powerUp = new Granada(px, py);
                        break;
                    default:
                        continue;
                }
                entorno.getPowerUps().add(powerUp);
            }

            System.out.println("Nivel " + idNivel + " cargado exitosamente.");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error al cargar nivel " + nivel + ": " + e.getMessage());
        }
    }
}
