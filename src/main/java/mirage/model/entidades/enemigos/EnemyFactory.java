package mirage.model.entidades.enemigos;

import processing.core.PApplet;

/**
 * Fábrica de enemigos del módulo Mirage.
 *
 * Centraliza la creación de todos los tipos de Enemigo.
 * Para agregar un nuevo tipo en MVPs futuros: crear la subclase de Enemigo,
 * agregar su valor al enum Tipo, y agregar el case aquí.
 * EnemySpawner y GameController no necesitan cambios.
 *
 * MVP 1: solo HARRIER.
 * MVP 6: se agregarán FRAGATA y KAMIKAZE.
 *
 * Patrón: Factory Method (static)
 */
public class EnemyFactory {

    /**
     * Tipos de enemigo disponibles en el módulo.
     * Expandir en MVPs futuros agregando nuevos valores.
     */
    public enum Tipo {
        HARRIER
        // FRAGATA  (MVP 6)
        // KAMIKAZE (MVP 5 — creado por JefeBarco)
    }

    public static Enemigo crear(Tipo tipo, PApplet sketch, float x, float y) {
        switch (tipo) {
            case HARRIER: return new HarrierEnemigo(sketch, x, y);
            default: throw new IllegalArgumentException("Tipo desconocido: " + tipo);
        }
    }
}
