package mirage.model.entidades.enemigos;

import mirage.model.fisica.HitBox;
import mirage.view.sprites.SpriteLoader;
import processing.core.PApplet;
import processing.core.PImage;

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
    private static final int TAM_SPRITE  = 44;   // tamaño de dibujo del sprite

    public HarrierEnemigo(PApplet sketch, float x, float y) {
        super(sketch, x, y, 3f, 1, PUNTOS_VALOR);
    }

    @Override
    protected void moverIA() {
        x += velocidad;
        y += 1.5f;
        if (x <= 15 || x >= sketch.width - 15) {
            velocidad *= -1;
        }
    }

    @Override
    public void render(PApplet sk) {
        PImage sprite = SpriteLoader.get("enemy-harrier.png");
        if (sprite != null) {
            // El sprite Kenney apunta hacia arriba; lo volteamos en vertical (scale 1,-1)
            // para que el enemigo apunte hacia abajo, hacia el jugador.
            sk.imageMode(PApplet.CENTER);
            sk.pushMatrix();
            sk.translate(x, y);
            sk.scale(1, -1);
            sk.image(sprite, 0, 0, TAM_SPRITE, TAM_SPRITE);
            sk.popMatrix();
        } else {
            // Respaldo: triángulo rojo invertido centrado en (x, y).
            sk.fill(220, 50, 50);
            sk.triangle(x, y + ALTO / 2f, x - ANCHO / 2f, y - ALTO / 2f, x + ANCHO / 2f, y - ALTO / 2f);
        }
    }

    @Override
    public HitBox getHitBox() {
        return new HitBox(x - ANCHO/2f, y - ALTO/2f, ANCHO, ALTO);
    }

    @Override
    public String getTipo() {
        return "HARRIER";
    }
}
