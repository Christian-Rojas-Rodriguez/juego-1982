// ============================================================
// FragataEnemiga — Fragata enemiga (se mueve lento y dispara)
// ============================================================
// GRASP : Polymorphism, Creator (crea sus propios proyectiles)
// Patrón: Template Method (implementa moverIA y render)
// ============================================================
// Qué implementar:
//   - moverIA(): movimiento lento de izquierda a derecha
//                contador de frames para decidir cuándo disparar
//   - disparar(): crear un Proyectil que baja hacia el Mirage
//   - render(sketch): dibujar sprite de fragata (rectángulo pixel-art)
//   - getHitBox(): hitbox más grande (es una fragata, no un avión)
// Nota: GameController debe preguntar si la fragata dispara en cada update
// ============================================================

package mirage.model.entidades.enemigos;

import processing.core.PApplet;
import mirage.model.entidades.Proyectil;
import mirage.model.fisica.HitBox;

public class FragataEnemiga extends Enemigo {

    private static final int ANCHO = 50;
    private static final int ALTO  = 25;
    private static final int PUNTOS_VALOR = 250;
    private static final int INTERVALO_DISPARO = 90; // frames

    private int framesSinDisparar;

    public FragataEnemiga(PApplet sketch, float x, float y) {
        super(sketch, x, y, 1f, 3, PUNTOS_VALOR);
        // TODO: framesSinDisparar = 0
    }

    @Override
    protected void moverIA() {
        // TODO: x += velocidad (lento, horizontal)
        // TODO: rebote en bordes
        // TODO: framesSinDisparar++
    }

    public boolean debeDisparar() {
        // TODO: if framesSinDisparar >= INTERVALO_DISPARO → resetear y return true
        return false;
    }

    public Proyectil disparar() {
        // TODO: return new Proyectil(x, y + ALTO/2, sketch) con velocidad hacia abajo
        return null;
    }

    @Override
    public void render(PApplet sk) {
        // TODO: dibujar sprite de fragata
    }

    @Override
    public HitBox getHitBox() {
        // TODO: return new HitBox(x - ANCHO/2, y - ALTO/2, ANCHO, ALTO)
        return null;
    }
}
