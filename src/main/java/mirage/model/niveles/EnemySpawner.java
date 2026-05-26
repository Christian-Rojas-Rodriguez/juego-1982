package mirage.model.niveles;

import mirage.model.entidades.enemigos.Enemigo;
import mirage.model.entidades.enemigos.EnemyFactory;
import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;

/**
 * Genera oleadas de enemigos a intervalos regulares de frames de Processing.
 *
 * Cada vez que frameCounter alcanza intervaloFrames, crea un batch de tamanoOleada
 * enemigos en posiciones X aleatorias fuera de la pantalla (y = -20).
 *
 * Los enemigos nuevos se acumulan en nuevosEsteFrame; getEnemigosNuevos()
 * los retorna y limpia el buffer. GameController los agrega a su lista activa.
 *
 * En MVP 2 recibirá ConfiguradorDificultad para escalar vida y velocidad.
 *
 * Patrón: GRASP Pure Fabrication
 */
public class EnemySpawner {

    private final PApplet sketch;
    private int intervaloFrames;
    private final int tamanoOleada;
    private int frameCounter;

    /** Buffer de enemigos generados en el frame actual. Se limpia tras cada consulta. */
    private final List<Enemigo> nuevosEsteFrame = new ArrayList<>();

    public EnemySpawner(PApplet sketch, int intervaloFrames, int tamanoOleada) {
        this.sketch          = sketch;
        this.intervaloFrames = intervaloFrames;
        this.tamanoOleada    = tamanoOleada;
        this.frameCounter    = 0;
    }

    /** Avanza el contador y genera una oleada cuando se alcanza el intervalo. */
    public void update() {
        // TODO: frameCounter++
        // TODO: if (frameCounter >= intervaloFrames):
        //         for i in 0..tamanoOleada-1:
        //           float x = sketch.random(20, sketch.width - 20)
        //           nuevosEsteFrame.add(EnemyFactory.crear(EnemyFactory.Tipo.HARRIER, sketch, x, -20))
        //         frameCounter = 0
    }

    /**
     * Retorna los enemigos generados en este frame y limpia el buffer.
     * Llamado por NivelMirage.getEnemigosNuevos() cada frame.
     */
    public List<Enemigo> getEnemigosNuevos() {
        // TODO: List<Enemigo> resultado = new ArrayList<>(nuevosEsteFrame)
        // TODO: nuevosEsteFrame.clear()
        // TODO: return resultado
        return new ArrayList<>();
    }

    /** Permite ajustar la cadencia en MVPs futuros (ConfiguradorDificultad). */
    public void setIntervalo(int nuevoIntervalo) {
        // TODO: this.intervaloFrames = nuevoIntervalo
    }
}
