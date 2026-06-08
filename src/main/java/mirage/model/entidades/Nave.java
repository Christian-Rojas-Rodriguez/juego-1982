package mirage.model.entidades;

import mirage.model.fisica.HitBox;

/**
 * Clase abstracta base para todas las entidades móviles del juego.
 *
 * Define estado y comportamiento común: posición, velocidad, vida, hitBox.
 * GameController y ColisionDetector usan esta base para tratar Mirage,
 * Proyectil y Enemigo de forma uniforme donde sea posible.
 *
 * Template Method: update() define el ciclo de vida; cada subclase lo implementa.
 *
 * Patrón: Template Method (update abstracto)
 */
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
