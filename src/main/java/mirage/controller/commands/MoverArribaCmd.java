// ============================================================
// MoverArribaCmd — Comando: mover el Mirage hacia arriba
// ============================================================
// GRASP : Low Coupling
// Patrón: Command (implementación concreta)
// ============================================================

package mirage.controller.commands;

import mirage.model.entidades.Mirage;

public class MoverArribaCmd implements Comando {

    @Override
    public void ejecutar(Mirage mirage) {
        // TODO: mirage.setMoverArriba(true)
    }

    @Override
    public void deshacer(Mirage mirage) {
        // TODO: mirage.setMoverArriba(false)
    }
}
