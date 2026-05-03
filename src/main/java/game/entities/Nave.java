package game.entities;

import processing.core.PApplet;

/**
 * Clase abstracta base para todas las naves del juego (jugador y enemigos).
 * Centraliza estado y comportamiento común: posición, velocidad, vida.
 */
public abstract class Nave {

    protected final PApplet sketch;
    protected float x, y;
    protected float velocidad;
    protected int vida;

    public Nave(PApplet sketch, float x, float y, float velocidad, int vida) {
        this.sketch    = sketch;
        this.x         = x;
        this.y         = y;
        this.velocidad = velocidad;
        this.vida      = vida;
    }

    public abstract void update();
    public abstract void render();

    public boolean estaViva() {
        return vida > 0;
    }

    public void recibirDanio(int danio) {
        vida -= danio;
    }

    public float getX() { return x; }
    public float getY() { return y; }
}