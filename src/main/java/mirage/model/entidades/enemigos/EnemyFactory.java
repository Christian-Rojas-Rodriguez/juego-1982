package mirage.model.entidades.enemigos;

import processing.core.PApplet;

/**
 * Fábrica de enemigos (Factory Method). Centraliza la creación: agregar un tipo
 * es crear la subclase de Enemigo, sumar su valor al enum Tipo y un case acá,
 * sin tocar EnemySpawner ni GameController.
 */
public class EnemyFactory {

    public enum Tipo {
        HARRIER
    }

    public static Enemigo crear(Tipo tipo, PApplet sketch, float x, float y) {
        switch (tipo) {
            case HARRIER: return new HarrierEnemigo(sketch, x, y);
            default: throw new IllegalArgumentException("Tipo desconocido: " + tipo);
        }
    }
}
