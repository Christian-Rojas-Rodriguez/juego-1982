// ============================================================
// EstadoJuego — Interfaz del patrón State para el juego
// ============================================================
// GRASP : Polymorphism, Protected Variations
// Patrón: State
// ============================================================
// Qué implementar:
//   - Esta interfaz no tiene lógica. Define el contrato que todos
//     los estados concretos deben respetar.
//   - Cada estado recibe el GameController para acceder al model
//     sin acoplarse a él en el constructor.
// ============================================================

package mirage.model.estado;

import mirage.controller.GameController;

public interface EstadoJuego {

    void update(GameController controller);

    void render(GameController controller);

    void alEntrar(GameController controller);

    void onKeyPressed(GameController controller, char key, int keyCode);
}
