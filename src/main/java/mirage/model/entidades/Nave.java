package mirage.model.entidades;

import mirage.model.fisica.HitBox;

public abstract class Nave {

    protected float x;
    protected float y;
    protected float velocidad;
    protected int vida;

    public Nave(float x, float y, float velocidad, int vida) {
        this.x         = x;
        this.y         = y;
        this.velocidad = velocidad;
        this.vida      = vida;
    }

    /** Actualiza posición y estado interno. Llamado cada frame por GameController. */
    public abstract void update();

    /** Retorna la hitBox centrada en la posición actual. */
    public abstract HitBox getHitBox();

    public boolean estaViva() {
        return vida > 0;
    }

    public void recibirDanio(int danio) {
        vida -= danio;
    }

    public float getX() { return x; }
    public float getY() { return y; }
}
