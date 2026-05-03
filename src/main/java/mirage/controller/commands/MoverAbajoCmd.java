// ============================================================
// MoverAbajoCmd — Comando: mover el Mirage hacia abajo
// ============================================================
// GRASP : Low Coupling
// Patrón: Command (implementación concreta)
// ============================================================

package mirage.controller.commands;

import mirage.model.entidades.Mirage;

public class MoverAbajoCmd implements Comando {

    @Override
    public void ejecutar(Mirage mirage) {
        // TODO: mirage.setMoverAbajo(true)
    }

    @Override
    public void deshacer(Mirage mirage) {
        // TODO: mirage.setMoverAbajo(false)
    }
}
