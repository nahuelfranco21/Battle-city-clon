# Battle City
 
**Materia:** Paradigmas de Programación (TB025)  

## Descripción del proyecto
Este proyecto fue realizado como trabajo práctico para la materia **Paradigmas de Programación (TB025)**.  

Consiste en una reimplementación del videojuego clásico **Battle City**, desarrollado en **Java** utilizando **JavaFX**.  

El objetivo del juego es **defender la base (representada por un águila)**, destruyendo los tanques enemigos hasta completar todos los niveles, evitando perder todas las vidas del tanque.  

El juego se puede ejecutar perfectamente en linux, tiene un bug con el Movimiento de los tanques en windows.

---

## Instrucciones para ejecución

En primer lugar, clonar el repositorio:  

```bash
git clone https://github.com/nahuelfranco21/Battle-city-clon.git
cd Battle-city-clon
```

Este proyecto utiliza **Maven** con el plugin `javafx-maven-plugin`.
Para compilar el proyecto:

```bash
mvn clean install
```
Para ejecutar el proyecto:
```bash
mvn javafx:run
```

## Instrucciones de juego
En el menú del juego se podrá elegir si se quiere jugar de a uno o dos jugadores.
Cada jugador contará con tres vidas que se pueden perder al recibir impactos de bala de los tanques enemigos 

**Jugador 1**

- Movimiento: W, A, S, D

- Disparo: SPACE

**Jugador 2** (en modo multijugador)

- Movimiento: Flechas del teclado (← ↑ → ↓)

- Disparo: ENTER

## Diagrama UML

### Este es el diagrama que se utilizo al principio del proyecto como guia:

![Diagrama UML](/home/nahuelf/Escritorio/tps/tp1-los-rompe-paredes/UML/UMLINICIO.png)

### Este es el diagrama que se utilizo al final del proyecto:
![Diagrama UML](/home/nahuelf/Escritorio/tps/tp1-los-rompe-paredes/UML/UMLFINAL.png)
