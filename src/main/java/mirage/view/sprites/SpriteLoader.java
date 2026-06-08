package mirage.view.sprites;

import processing.core.PApplet;
import processing.core.PImage;

import java.util.HashMap;
import java.util.Map;

public class SpriteLoader {

    /** Carpeta base dentro de data/ (Processing resuelve loadImage desde ahí). */
    private static final String BASE = "sprites/";

    private static final Map<String, PImage> cache = new HashMap<>();

    private SpriteLoader() { } // servicio estático, no instanciable

    public static void cargar(String nombre, PApplet sketch) {
        if (!cache.containsKey(nombre)) {
            // loadImage devuelve null (y loguea) si no encuentra el archivo;
            // lo guardamos igual para no reintentar cada frame.
            cache.put(nombre, sketch.loadImage(BASE + nombre));
        }
    }

    public static PImage get(String nombre) {
        return cache.get(nombre);
    }

    public static void precargarTodos(PApplet sketch) {
        cargar("player.png", sketch);         // Kenney Ships/ship_0012.png (nave gris)
        cargar("enemy-harrier.png", sketch);  // Kenney Ships/ship_0001.png
        cargar("bullet.png", sketch);         // Kenney Tiles/tile_0000.png
        cargar("fondo.png", sketch);
    }
}
