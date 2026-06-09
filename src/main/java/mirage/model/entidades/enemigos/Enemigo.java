package mirage.model.entidades.enemigos;

import mirage.model.entidades.Nave;
import mirage.model.fisica.HitBox;
import processing.core.PApplet;

public abstract class Enemigo extends Nave {

    protected int puntos;
    protected final PApplet sketch;

    public Enemigo(PApplet sketch, float x, float y, float velocidad, int vida, int puntos) {
        super(x, y, velocidad, vida);
        this.sketch = sketch;
        this.puntos = puntos;
    }

    /**
     * Ciclo de vida fijo: llamado cada frame por GameController.
     * Delega el movimiento a moverIA() — no sobreescribir en subclases.
     */
    @Override
    public final void update() {
        moverIA();
    }

    /**
     * Hook del Template Method: cada subclase implementa su patrón de movimiento.
     * HarrierEnemigo: zigzag horizontal + descenso.
     */
    protected abstract void moverIA();

    /** Dibuja el sprite del enemigo. Implementado por cada subclase. */
    public abstract void render(PApplet sk);

    @Override
    public abstract HitBox getHitBox();

    /**
     * Retorna el identificador de tipo para estadísticas.
     * Ejemplo: "HARRIER". Usado en EstadisticasMirage.registrarDerribo().
     */
    public abstract String getTipo();

    public int getPuntos() { return puntos; }
}
