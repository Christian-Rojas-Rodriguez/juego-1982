// ============================================================
// Mirage — El avión del jugador (Fuerza Aérea Argentina)
// ============================================================
// GRASP : Information Expert, Creator (crea sus propios Proyectiles)
// ============================================================
// Qué implementar:
//   - update(): aplicar movimiento según flags booleanos (izquierda/derecha/etc.)
//               constrain dentro de los límites de pantalla
//               decrementar frameInvencible si invencible
//   - render(sketch): dibujar el sprite del Mirage
//               si invencible, parpadear (blink cada N frames)
//   - disparar(): crear y retornar un nuevo Proyectil en la posición actual
//   - setters de movimiento: llamados por los Commands
//   - invencibilidad: activar al recibir daño (sobreescribir recibirDanio)
// ============================================================

package mirage.model.entidades;

import processing.core.PApplet;
import mirage.model.fisica.HitBox;

public class Mirage extends Nave {

    // --- Constantes ---
    private static final int DURACION_INVENCIBILIDAD = 120; // frames
    private static final int ANCHO_HITBOX  = 30;
    private static final int ALTO_HITBOX   = 30;

    // --- Atributos ---
    private int vidas;
    private int puntuacion;
    private boolean invencible;
    private int frameInvencible;

    // Flags de movimiento (activados por Commands)
    private boolean moverIzquierda;
    private boolean moverDerecha;
    private boolean moverArriba;
    private boolean moverAbajo;

    // Referencia al sketch solo para constrain y width/height
    private final PApplet sketch;

    // --- Constructor ---
    public Mirage(PApplet sketch) {
        super(sketch.width / 2f, sketch.height - 100, 4f, 3);
        this.sketch = sketch;
        // TODO: inicializar vidas = 3, puntuacion = 0
    }

    @Override
    public void update() {
        // TODO: mover según flags → x +/- velocidad, y +/- velocidad
        // TODO: constrain(x, 0, sketch.width), constrain(y, 0, sketch.height)
        // TODO: si invencible, decrementar frameInvencible
        //       si frameInvencible <= 0 → invencible = false
    }

    public void render(PApplet sk) {
        // TODO: dibujar sprite del Mirage (triángulo pixel-art o imagen)
        // TODO: si invencible y frameCount % 10 < 5 → no dibujar (parpadeo)
    }

    public Proyectil disparar() {
        // TODO: return new Proyectil(x, y - 20, sketch)
        return null;
    }

    @Override
    public void recibirDanio(int danio) {
        // TODO: si invencible → ignorar
        // TODO: vidas--
        // TODO: activar invencibilidad: invencible = true, frameInvencible = DURACION
    }

    @Override
    public HitBox getHitBox() {
        // TODO: return new HitBox(x - ANCHO_HITBOX/2, y - ALTO_HITBOX/2, ANCHO_HITBOX, ALTO_HITBOX)
        return null;
    }

    // --- Setters de movimiento (llamados por Commands) ---
    public void setMoverIzquierda(boolean v) { moverIzquierda = v; }
    public void setMoverDerecha(boolean v)   { moverDerecha   = v; }
    public void setMoverArriba(boolean v)    { moverArriba    = v; }
    public void setMoverAbajo(boolean v)     { moverAbajo     = v; }

    // --- Getters ---
    public int getVidas()        { return vidas; }
    public int getPuntuacion()   { return puntuacion; }
    public boolean isInvencible(){ return invencible; }

    public void sumarPuntos(int puntos) {
        // TODO: puntuacion += puntos
    }
}
