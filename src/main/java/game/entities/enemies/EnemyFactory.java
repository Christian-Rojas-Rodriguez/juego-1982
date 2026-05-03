package game.entities.enemies;

import processing.core.PApplet;

/**
 * Factory para crear enemigos por tipo.
 * Para agregar un nuevo enemigo: crear la subclase de Enemy,
 * añadir su valor al enum Tipo, y agregar el case aquí.
 * El resto del código (GameManager, niveles) no necesita cambios.
 */
public class EnemyFactory {

    public enum Tipo {
        BASICO
    }

    public static Enemy crear(Tipo tipo, PApplet sketch, float x, float y) {
        switch (tipo) {
            case BASICO: return new EnemyBasico(sketch, x, y);
            default: throw new IllegalArgumentException("Tipo de enemigo desconocido: " + tipo);
        }
    }
}