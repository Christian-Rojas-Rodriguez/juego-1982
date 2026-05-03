// ============================================================
// MoverIzquierdaCmd — Comando: mover el Mirage hacia la izquierda
// ============================================================
// GRASP : Low Coupling
// Patrón: Command (implementación concreta)
// ============================================================
// Qué implementar:
//   - ejecutar(): activar flag de movimiento izquierda en el Mirage
//   - deshacer(): desactivar flag (al soltar la tecla)
// ============================================================

package mirage.controller.commands;

import mirage.model.entidades.Mirage;

public class MoverIzquierdaCmd implements Comando {

    @Override
    public void ejecutar(Mirage mirage) {
        // TODO: mirage.setMoverIzquierda(true)
    }

    @Override
    public void deshacer(Mirage mirage) {
        // TODO: mirage.setMoverIzquierda(false)
    }
}
