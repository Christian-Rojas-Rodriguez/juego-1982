package game.entities.enemies;

import game.entities.Nave;
import processing.core.PApplet;

/**
 * Clase abstracta para todos los enemigos.
 * Aplica Template Method: update() define el ciclo de vida;
 * cada subclase implementa su propia lógica de movimiento en moverIA().
 */
public abstract class Enemy extends Nave {

    protected int puntos;

    public Enemy(PApplet sketch, float x, float y, float velocidad, int vida, int puntos) {
        super(sketch, x, y, velocidad, vida);
        this.puntos = puntos;
    }

    @Override
    public final void update() {
        moverIA();
    }

    protected abstract void moverIA();

    public int getPuntos() { return puntos; }
}