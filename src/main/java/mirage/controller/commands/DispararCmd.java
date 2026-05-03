// ============================================================
// DispararCmd — Comando: el Mirage dispara un proyectil
// ============================================================
// GRASP : Creator (quien necesita el proyectil lo crea via Mirage)
// Patrón: Command (implementación concreta)
// ============================================================
// Qué implementar:
//   - ejecutar(): llamar mirage.disparar() y agregar el proyectil
//     devuelto a la lista de proyectiles del GameController
//   - deshacer(): no aplica para disparos (dejar vacío)
// Nota: para acceder a la lista de proyectiles, considerar
//       pasar el GameController como parámetro adicional o
//       guardar referencia en el constructor.
// ============================================================

package mirage.controller.commands;

import mirage.model.entidades.Mirage;
import mirage.model.entidades.Proyectil;

public class DispararCmd implements Comando {

    @Override
    public void ejecutar(Mirage mirage) {
        // TODO: Proyectil p = mirage.disparar()
        // TODO: agregar p a la lista de proyectiles (ver nota arriba)
    }

    @Override
    public void deshacer(Mirage mirage) {
        // Los disparos no se deshacen
    }
}
