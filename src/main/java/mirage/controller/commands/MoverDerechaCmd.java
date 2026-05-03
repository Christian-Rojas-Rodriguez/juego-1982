// ============================================================
// MoverDerechaCmd — Comando: mover el Mirage hacia la derecha
// ============================================================
// GRASP : Low Coupling
// Patrón: Command (implementación concreta)
// ============================================================

package mirage.controller.commands;

import mirage.model.entidades.Mirage;

public class MoverDerechaCmd implements Comando {

    @Override
    public void ejecutar(Mirage mirage) {
        // TODO: mirage.setMoverDerecha(true)
    }

    @Override
    public void deshacer(Mirage mirage) {
        // TODO: mirage.setMoverDerecha(false)
    }
}
