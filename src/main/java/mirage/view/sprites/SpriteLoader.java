// ============================================================
// SpriteLoader — Carga y cachea imágenes de Processing
// ============================================================
// GRASP : Pure Fabrication (servicio sin dominio propio)
// ============================================================
// Carga los sprites del pack Kenney "Pixel Shmup" (CC0) desde
// data/sprites/ y los cachea para no recargarlos cada frame.
//
// Uso: precargarTodos() una vez (en el primer render, con el
//      PApplet vivo), luego get(nombre) en cada render().
//
// Tolerante a fallos: si una imagen no se encuentra, get() devuelve
// null y las entidades dibujan su forma de respaldo (triángulo/rect).
// Esto mantiene los tests headless en verde (nunca llaman render()).
// ============================================================

package mirage.view.sprites;

import processing.core.PApplet;
import processing.core.PImage;

import java.util.HashMap;
import java.util.Map;

public class SpriteLoader {

    /** Carpeta base dentro de data/ (Processing resuelve loadImage desde ahí). */
    private static final String BASE = "sprites/";

    // --- Cache singleton de imágenes ---
    private static final Map<String, PImage> cache = new HashMap<>();

    private SpriteLoader() { } // servicio estático, no instanciable

    // --- Cargar imagen y guardar en cache ---
    public static void cargar(String nombre, PApplet sketch) {
        if (!cache.containsKey(nombre)) {
            // loadImage devuelve null (y loguea) si no encuentra el archivo;
            // lo guardamos igual para no reintentar cada frame.
            cache.put(nombre, sketch.loadImage(BASE + nombre));
        }
    }

    // --- Obtener imagen del cache (null si no existe) ---
    public static PImage get(String nombre) {
        return cache.get(nombre);
    }

    // --- Precargar todos los sprites del módulo ---
    public static void precargarTodos(PApplet sketch) {
        cargar("player.png", sketch);         // Kenney Ships/ship_0012.png (nave gris)
        cargar("enemy-harrier.png", sketch);  // Kenney Ships/ship_0001.png
        cargar("bullet.png", sketch);         // Kenney Tiles/tile_0000.png
        // Fondo de mar con islas (Kenney Tiles/) — autotile de costa.
        cargar("agua.png", sketch);           // tile_0042 (mar)
        cargar("isla-centro.png", sketch);    // tile_0050 (césped sólido)
        cargar("isla-borde-sup.png", sketch); // tile_0038
        cargar("isla-borde-izq.png", sketch); // tile_0049
        cargar("isla-borde-der.png", sketch); // tile_0051
        cargar("isla-esq-sup-izq.png", sketch); // tile_0037
        cargar("isla-esq-sup-der.png", sketch); // tile_0039
    }
}
