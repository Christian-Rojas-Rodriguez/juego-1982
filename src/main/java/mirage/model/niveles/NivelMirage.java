package mirage.model.niveles;

import mirage.model.entidades.enemigos.Enemigo;
import processing.core.PApplet;

import java.util.List;

/**
 * Nivel concreto del módulo Mirage — MVP 1: oleadas infinitas de HarrierEnemigo.
 *
 * Gestiona el spawner de enemigos. No tiene condición de nivel completado en MVP 1:
 * la partida termina solo por Game Over (vidas = 0).
 *
 * En MVP 5 se agregará la secuencia de bosses y la condición de nivel completado.
 *
 * Patrón: Template Method (extiende Nivel abstracto)
 */
public class NivelMirage extends Nivel {

    private static final int INTERVALO_INICIAL = 180; // frames (3 seg a 60 fps)
    private static final int TAMANO_OLEADA     = 3;

    private final EnemySpawner spawner;

    public NivelMirage(PApplet sketch) {
        this.spawner = new EnemySpawner(sketch, INTERVALO_INICIAL, TAMANO_OLEADA);
    }

    @Override
    public void update() {
        spawner.update();
    }

    /**
     * Retorna los enemigos generados en este frame y limpia el buffer del spawner.
     * GameController llama este método y agrega los enemigos a su lista activa.
     */
    @Override
    public List<Enemigo> getEnemigosNuevos() {
        return spawner.getEnemigosNuevos();
    }

    /**
     * En MVP 1 el nivel nunca termina — la partida finaliza por Game Over.
     * En MVP 5 retornará true cuando se venzan los 4 bosses.
     */
    @Override
    public boolean isTerminado() {
        return false;
    }
}
