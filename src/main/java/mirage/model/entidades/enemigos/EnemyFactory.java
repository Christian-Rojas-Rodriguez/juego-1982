package mirage.model.entidades.enemigos;

import processing.core.PApplet;

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
