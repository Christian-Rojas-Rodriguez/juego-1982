// ============================================================
// EnemyFactory — Fábrica de enemigos del módulo Mirage
// ============================================================
// GRASP : Creator, Low Coupling
// Patrón: Factory Method con enum
// ============================================================
// Qué implementar:
//   - Agregar nuevos tipos al enum Tipo
//   - Agregar el case correspondiente en crear()
//   - El resto del sistema (EnemySpawner, NivelMirage) nunca
//     instancia enemigos directamente, siempre usa esta factory
// ============================================================

package mirage.model.entidades.enemigos;

import processing.core.PApplet;

public class EnemyFactory {

    public enum Tipo {
        HARRIER,
        FRAGATA
    }

    public static Enemigo crear(Tipo tipo, PApplet sketch, float x, float y) {
        // TODO:
        // switch (tipo) {
        //     case HARRIER: return new HarrierEnemigo(sketch, x, y);
        //     case FRAGATA: return new FragataEnemiga(sketch, x, y);
        //     default: throw new IllegalArgumentException("Tipo desconocido: " + tipo);
        // }
        return null;
    }
}
