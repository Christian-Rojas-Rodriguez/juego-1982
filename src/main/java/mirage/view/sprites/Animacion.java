// ============================================================
// Animacion — Secuencia de frames para animar sprites
// ============================================================
// Qué implementar:
//   - Constructor: recibir lista de nombres de frames y velocidad
//   - update(): incrementar contador, avanzar frame al ritmo indicado
//   - getFrameActual(): retornar el PImage del frame activo
//   - reset(): volver al frame inicial (útil para explosiones)
// Uso: crear una Animacion por entidad que tenga animación
//      (ej: explosión al destruir un enemigo)
// ============================================================

package mirage.view.sprites;

import processing.core.PApplet;
import processing.core.PImage;

import java.util.List;

public class Animacion {

    // --- Atributos ---
    private final List<String> nombresFrames;
    private final int velocidad; // frames de Processing por frame de animación
    private int frameActual;
    private int contador;
    private boolean terminada;

    // --- Constructor ---
    public Animacion(List<String> nombresFrames, int velocidad) {
        // TODO: asignar parámetros, frameActual = 0, contador = 0, terminada = false
    }

    // --- Avanzar la animación un frame ---
    public void update() {
        // TODO: contador++
        // TODO: si contador >= velocidad:
        //         frameActual++
        //         contador = 0
        //         si frameActual >= nombresFrames.size() → terminada = true, frameActual = size-1
    }

    // --- Obtener imagen actual del cache ---
    public PImage getFrameActual() {
        // TODO: return SpriteLoader.get(nombresFrames.get(frameActual))
        return null;
    }

    public boolean isTerminada() { return terminada; }

    public void reset() {
        // TODO: frameActual = 0; contador = 0; terminada = false
    }
}
