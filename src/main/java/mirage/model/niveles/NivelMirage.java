// ============================================================
// NivelMirage — Nivel concreto del módulo Avión Mirage
// ============================================================
// GRASP : Information Expert, Creator (crea el EnemySpawner)
// ============================================================
// Qué implementar:
//   - Constructor: crear EnemySpawner con intervalos configurados
//   - update(): avanzar oleada, preguntar al spawner si hay enemigos nuevos
//   - isTerminado(): definir condición de victoria
//     Opciones: N enemigos destruidos, tiempo transcurrido, oleadas completadas
//   - getEnemigosNuevos(): delegar al spawner y retornar la lista
// ============================================================

package mirage.model.niveles;

import processing.core.PApplet;
import mirage.model.entidades.enemigos.Enemigo;

import java.util.ArrayList;
import java.util.List;

public class NivelMirage extends Nivel {

    // --- Constantes ---
    private static final int TOTAL_OLEADAS = 5;

    // --- Atributos ---
    private final PApplet sketch;
    private final EnemySpawner spawner;
    private int oleadaActual;
    private int enemigosTotalesDestruidos;

    // --- Constructor ---
    public NivelMirage(PApplet sketch) {
        this.sketch = sketch;
        // TODO: spawner = new EnemySpawner(sketch, intervaloFrames)
        // TODO: oleadaActual = 1
    }

    @Override
    public void update() {
        // TODO: spawner.update()
        // TODO: lógica de oleadas: si se destruyeron N enemigos → oleadaActual++
        //       aumentar dificultad (reducir intervalo del spawner, agregar FragataEnemiga)
    }

    @Override
    public boolean isTerminado() {
        // TODO: return oleadaActual > TOTAL_OLEADAS
        return false;
    }

    @Override
    public List<Enemigo> getEnemigosNuevos() {
        // TODO: return spawner.getEnemigosNuevos()
        return new ArrayList<>();
    }

    public void registrarDerribo() {
        // TODO: enemigosTotalesDestruidos++
    }
}
