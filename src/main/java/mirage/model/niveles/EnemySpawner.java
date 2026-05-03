// ============================================================
// EnemySpawner — Genera enemigos según intervalos de tiempo
// ============================================================
// GRASP : Pure Fabrication, Low Coupling
// ============================================================
// Qué implementar:
//   - update(): incrementar frameCounter, si alcanzó el intervalo
//               generar un enemigo con EnemyFactory y agregar a la lista interna
//   - getEnemigosNuevos(): retornar y LIMPIAR la lista de nuevos de este frame
//     (el Controller los agrega a su lista maestra)
//   - setIntervalo(): permite aumentar dificultad desde NivelMirage
// Diferencia con el EnemySpawner anterior (game.levels):
//   Este NO mutata una lista externa — devuelve los nuevos al Controller.
// ============================================================

package mirage.model.niveles;

import processing.core.PApplet;
import mirage.model.entidades.enemigos.Enemigo;
import mirage.model.entidades.enemigos.EnemyFactory;

import java.util.ArrayList;
import java.util.List;

public class EnemySpawner {

    // --- Atributos ---
    private final PApplet sketch;
    private int intervaloFrames;
    private int frameCounter;
    private final List<Enemigo> nuevosEsteFrame = new ArrayList<>();

    // --- Constructor ---
    public EnemySpawner(PApplet sketch, int intervaloFrames) {
        this.sketch          = sketch;
        // TODO: this.intervaloFrames = intervaloFrames
        // TODO: this.frameCounter = 0
    }

    // --- Avanzar un frame ---
    public void update() {
        // TODO: frameCounter++
        // TODO: si frameCounter >= intervaloFrames:
        //         spawnEnemigo()
        //         frameCounter = 0
    }

    private void spawnEnemigo() {
        // TODO: float x = sketch.random(20, sketch.width - 20)
        // TODO: nuevosEsteFrame.add(EnemyFactory.crear(EnemyFactory.Tipo.HARRIER, sketch, x, -20))
    }

    // --- Retornar y limpiar nuevos de este frame ---
    public List<Enemigo> getEnemigosNuevos() {
        // TODO:
        // List<Enemigo> resultado = new ArrayList<>(nuevosEsteFrame)
        // nuevosEsteFrame.clear()
        // return resultado
        return new ArrayList<>();
    }

    // --- Ajustar dificultad ---
    public void setIntervalo(int nuevoIntervalo) {
        // TODO: this.intervaloFrames = nuevoIntervalo
    }
}
