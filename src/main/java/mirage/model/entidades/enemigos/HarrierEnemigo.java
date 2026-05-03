// ============================================================
// HarrierEnemigo — Avión Harrier enemigo (movimiento en zigzag)
// ============================================================
// GRASP : Polymorphism
// Patrón: Template Method (implementa moverIA y render)
// ============================================================
// Qué implementar:
//   - moverIA(): movimiento horizontal en zigzag + descenso gradual
//                rebote en los bordes de pantalla
//   - render(sketch): dibujar sprite del Harrier (color rojo/gris)
//   - getHitBox(): hitbox del avión
// ============================================================

package mirage.model.entidades.enemigos;

import processing.core.PApplet;
import mirage.model.fisica.HitBox;

public class HarrierEnemigo extends Enemigo {

    private static final int ANCHO = 30;
    private static final int ALTO  = 30;
    private static final int PUNTOS_VALOR = 100;

    public HarrierEnemigo(PApplet sketch, float x, float y) {
        super(sketch, x, y, 3f, 1, PUNTOS_VALOR);
    }

    @Override
    protected void moverIA() {
        // TODO: x += velocidad
        // TODO: y += 1.5f  (descenso lento)
        // TODO: si x <= 15 o x >= sketch.width-15 → velocidad *= -1
    }

    @Override
    public void render(PApplet sk) {
        // TODO: dibujar sprite del Harrier (triángulo invertido o imagen)
    }

    @Override
    public HitBox getHitBox() {
        // TODO: return new HitBox(x - ANCHO/2, y - ALTO/2, ANCHO, ALTO)
        return null;
    }
}
