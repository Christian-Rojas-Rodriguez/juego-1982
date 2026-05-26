package mirage.model.entidades.enemigos;

import mirage.model.fisica.HitBox;
import processing.core.PApplet;

/**
 * Avión Harrier enemigo — único tipo de enemigo en MVP 1.
 *
 * Patrón de movimiento (moverIA): zigzag horizontal + descenso lento.
 * Al alcanzar un borde de pantalla invierte la dirección horizontal.
 *
 * Patrón: Template Method (sobreescribe moverIA de Enemigo)
 */
public class HarrierEnemigo extends Enemigo {

    private static final int ANCHO       = 30;
    private static final int ALTO        = 30;
    private static final int PUNTOS_VALOR = 100;

    public HarrierEnemigo(PApplet sketch, float x, float y) {
        super(sketch, x, y, 3f, 1, PUNTOS_VALOR);
    }

    @Override
    protected void moverIA() {
        // TODO: x += velocidad
        // TODO: y += 1.5f    (descenso lento hacia el Mirage)
        // TODO: si x <= 15 o x >= sketch.width - 15 → velocidad *= -1 (rebote en bordes)
    }

    @Override
    public void render(PApplet sk) {
        // TODO: dibujar sprite del Harrier centrado en (x, y)
        // Placeholder pixel-art: triángulo rojo invertido
        // sk.fill(220, 50, 50)
        // sk.triangle(x, y + ALTO/2, x - ANCHO/2, y - ALTO/2, x + ANCHO/2, y - ALTO/2)
    }

    @Override
    public HitBox getHitBox() {
        // TODO: return new HitBox(x - ANCHO/2, y - ALTO/2, ANCHO, ALTO)
        return null;
    }

    @Override
    public String getTipo() {
        return "HARRIER";
    }
}
