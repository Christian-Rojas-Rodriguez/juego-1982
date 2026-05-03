// ============================================================
// Comando — Interfaz del patrón Command
// ============================================================
// GRASP : Protected Variations, Low Coupling
// Patrón: Command
// ============================================================
// Qué implementar:
//   - Esta es la interfaz base. No tiene lógica.
//   - ejecutar(mirage): acción al presionar la tecla
//   - deshacer(mirage): acción al soltar la tecla (útil para movimiento continuo)
// ============================================================

package mirage.controller.commands;

import mirage.model.entidades.Mirage;

public interface Comando {

    void ejecutar(Mirage mirage);

    void deshacer(Mirage mirage);
}
