// ============================================================
// SpriteLoader — Carga y cachea imágenes de Processing
// ============================================================
// GRASP : Pure Fabrication (servicio sin dominio propio)
// ============================================================
// Qué implementar:
//   - cargar(nombre, sketch): carga la imagen desde assets/images/
//                             y la guarda en el cache para no recargar
//   - get(nombre): retorna la imagen del cache (null si no existe)
// Uso: llamar cargar() en setup(), luego get() en render()
// Ruta base sugerida: "assets/images/mirage/"
// ============================================================

package mirage.view.sprites;

import processing.core.PApplet;
import processing.core.PImage;

import java.util.HashMap;
import java.util.Map;

public class SpriteLoader {

    // --- Cache singleton de imágenes ---
    private static final Map<String, PImage> cache = new HashMap<>();

    // --- Cargar imagen y guardar en cache ---
    public static void cargar(String nombre, PApplet sketch) {
        // TODO: si !cache.containsKey(nombre):
        //         PImage img = sketch.loadImage("assets/images/mirage/" + nombre)
        //         cache.put(nombre, img)
    }

    // --- Obtener imagen del cache ---
    public static PImage get(String nombre) {
        // TODO: return cache.get(nombre)
        return null;
    }

    // --- Precargar todos los sprites del módulo ---
    public static void precargarTodos(PApplet sketch) {
        // TODO: cargar("mirage.png", sketch)
        // TODO: cargar("harrier.png", sketch)
        // TODO: cargar("fragata.png", sketch)
        // TODO: cargar("proyectil.png", sketch)
        // TODO: cargar("explosion.png", sketch)
    }
}
