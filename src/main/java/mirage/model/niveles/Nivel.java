// ============================================================
// Nivel — Clase abstracta base para los niveles del módulo
// ============================================================
// GRASP : Polymorphism, Protected Variations
// ============================================================
// Qué implementar:
//   - update(): avanzar la lógica del nivel (oleadas, tiempo, etc.)
//   - isTerminado(): condición de victoria del nivel
//   - getEnemigosNuevos(): retornar los enemigos generados en este frame
//     El Controller los agrega a su lista. Lista vacía si no hay nuevos.
// ============================================================

package mirage.model.niveles;

import mirage.model.entidades.enemigos.Enemigo;

import java.util.List;

public abstract class Nivel {

    public abstract void update();

    public abstract boolean isTerminado();

    public abstract List<Enemigo> getEnemigosNuevos();
}
